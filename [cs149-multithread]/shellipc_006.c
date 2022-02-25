#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <stdbool.h>
#include <wait.h>
#include <bits/types/struct_timeval.h>
#include <sys/time.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <fcntl.h>

#define MAXLINE 80 /* The maximum length of a command line */
#define MAXARGS 40 /* The maximum # of tokens in command line */

#define BUFFER_SIZE 25
#define READ_END    0
#define WRITE_END    1

int main(void) {
    char cmdline[MAXLINE];
    char *args[MAXARGS];
    printf("CS149 Fall 2020 Shellipc from Carter Gale\n");
    while (1) {
        printf("CarterGale-006> ");
        fflush(stdout);

        //Get user input
        char input[301];
        fgets(input, 300, stdin);

        // Get the tokens from a string
        args[0] = strtok(input, " ");
        int indexForArgsArray = 0;
        int pipeIndex = -1; // hold this for later,
        while (args[indexForArgsArray] != NULL) {
            if (strcmp(args[indexForArgsArray], "|") == 0) {
                pipeIndex = indexForArgsArray;
            }
            indexForArgsArray += 1;
            args[indexForArgsArray] = strtok(NULL, " ");
        }

        //Remove trailing newline character
        if (indexForArgsArray > 0) {
            strtok(args[indexForArgsArray - 1], "\n");
        }

        //If the user enters "exit", terminate the shell (parent)
        if (strcmp(args[0], "exit") == 0) {
            return 0;
        }

        //Check for the ampersand
        bool ampersand = (strcmp(args[indexForArgsArray - 1], "&") == 0);
        if (ampersand) {
            args[indexForArgsArray - 1] = NULL; //remove ampersand
            indexForArgsArray -= 1; //in case we use this again, we want it to be correct
        }

        //open shared memory
        int shm_fileDescriptor = shm_open("hw1", O_CREAT | O_RDWR, 0666);
        if (shm_fileDescriptor == -1) {
            fprintf(stderr, "Error in shared memory\r\n");
            continue;
        }

        //Allocate the memory to not be length 0
        ftruncate(shm_fileDescriptor, 1024);

        //map shared memory
        void *address = mmap(NULL, 1024, PROT_READ | PROT_WRITE, MAP_SHARED, shm_fileDescriptor, 0);
        if (address == NULL || address == MAP_FAILED) {
            fprintf(stderr, "Memory mapping failed\r\n");
            continue;
        }
        struct timeval *tv = (struct timeval *) address;

        //Fork the processes
        int pid = fork();
        if (pid < 0) {
            //error
            fprintf(stderr, "Fork Failed\r\n");
            continue;
        } else if (pid == 0) {
            //child process
            gettimeofday(tv, NULL);
            if (pipeIndex == -1) {
                int retid = execvp(args[0], args);
                if (retid == -1) {
                    fprintf(stderr, "Child process execution failed on pid: %d\r\n", pid);
                }
            } else {
                int fd[2];
                if (pipe(fd) == -1) {
                    fprintf(stderr, "Pipe failed\r\n");
                    continue;
                }

                int retid = fork();
                if (retid < 0) {
                    fprintf(stderr, "Fork failed\r\n");
                    continue;
                }

                if (retid == 0) {
                    //First command
                    //terminate args
                    args[pipeIndex] = NULL;
                    close(fd[READ_END]);
                    dup2(fd[WRITE_END], STDOUT_FILENO);
                    close(fd[WRITE_END]);
                    execvp(args[0], args);
                    _exit(1);
                } else {
                    char *cmdAndArgs2[MAXARGS];
                    int i = 0;
                    //copy over the new args and cmd of the pipe
                    while (args[pipeIndex + i + 1] != NULL) {
//                        printf("args of child pipe: %s\n", args[pipeIndex + i + 1]);
                        cmdAndArgs2[i] = args[pipeIndex + i + 1];
                        i++;
                    }
                    cmdAndArgs2[i + 1] = NULL;
                    close(fd[WRITE_END]);
                    dup2(fd[READ_END], STDIN_FILENO);
                    close(fd[READ_END]);
                    execvp(cmdAndArgs2[0], cmdAndArgs2);
                    waitpid(pid, NULL, 0);
                }
            }

            //exit out of the while loop, terminate process with exit code 0
            return 0;
        } else {
            //this is the parent process
            //wait until the child is complete if there is NOT an ampersand
            if (ampersand) {
                //last character is an ampersand, thus we do not wait
            } else {
                int retid = waitpid(pid, NULL, 0);
                struct timeval current;
                gettimeofday(&current, NULL);
                struct timeval previous = *tv;
                printf("(elapsed time: %ld.%06ld seconds)\n", current.tv_sec - previous.tv_sec,
                       current.tv_usec - previous.tv_usec);
                if (retid == -1) {
                    fprintf(stderr, "Wait failed on pid: %d\r\n", pid);
                    continue;
                }
            }
        }
    }
    return 0;
}

