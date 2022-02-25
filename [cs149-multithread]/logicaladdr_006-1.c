#include <stdio.h>

int main(int argc, char **argv) {
    printf("CS149 Fall 2020 Logical address translation from Carter Gale\r\n");

    // The integer input as the first argument
    int input;

    //The value we use to check various returns for errors
    int retcheck;
    retcheck = sscanf(argv[1], "%d", &input);
    if (retcheck == 0) {
        fprintf(stderr, "Invalid number of integers, please make sure it is an integer");
        return 0;
    }
    if (input < 0) {
        fprintf(stderr, "Number of integers cannot be less than 0 (negative)\r\n");
        return 0;
    }

    printf("logical address = %d\r\n", input);

    printf("page size = %d => page number = %d, offset = %d\r\n", 1024, input / 1024, input % 1024);
    printf("page size = %d => page number = %d, offset = %d\r\n", 2048, input / 2048, input % 2048);
    printf("page size = %d => page number = %d, offset = %d\r\n", 4096, input / 4096, input % 4096);
    printf("page size = %d => page number = %d, offset = %d\r\n", 8192, input / 8192, input % 8192);
}