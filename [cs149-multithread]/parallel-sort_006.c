#define _GNU_SOURCE

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>

int cmpintp(const void *p1, const void *p2, void *p3) {
    int v1 = *((int *) p1);
    int v2 = *((int *) p2);
    return (v1 - v2);
}

/* structure for passing data to threads */
typedef struct {
    int *pData;
    int x;
    int size;
} phaseOneParam;

typedef struct {
    int *arrayPntr;
    int *tempPntr;
    int iteration;
    int thread;
    int size;
} phaseTwoParam;

// merge
// sorted array segement 1: a[left] a[left+1] .... a[middle - 1]
// sorted array segement 2: a[middle] a[middle+1] .... a[right - 1]
// into
// sorted array segement: a[left] a[left+1] ... a[middle-1] a[middle] ... a[right-1]
// tmp[ ]: a help array used in the merge operation, same # of elements as a[]
void merge(int a[], int left, int middle, int right, int tmp[]) {
    int i = left, j = middle, k = left;
    while (i < middle || j < right) {
        if (i < middle && j < right) { // Both array have elements
            if (a[i] < a[j]) {
                tmp[k++] = a[i++];
            } else {
                tmp[k++] = a[j++];
            }
        } else if (i == middle) {
            tmp[k++] = a[j++]; // a is empty
        } else if (j == right) {
            tmp[k++] = a[i++]; // b is empty
        }
    }
    for (i = left; i < right; i++)
        a[i] = tmp[i]; /* copy tmp[] back to a[] */
}

void *phaseonerunner(void *param) {
    phaseOneParam *para = ((phaseOneParam *) param);
    qsort_r(para->pData, para->size, sizeof(int), cmpintp, NULL);
    printf("sorted[%d]: base=%p, numElmsSorted=%d\r\n", para->x, para->pData, para->size);
    pthread_exit(NULL);
}

void *phasetworunner(void *param) {
    phaseTwoParam *para = ((phaseTwoParam *) param);
    int left = para->thread * para->size;
    merge(para->arrayPntr, left, left + para->size, (left + (2 * para->size)), para->tempPntr);
    printf("merged[%d, %d]: base=%p, left=%d, middle=%d, right=%d\r\n", para->iteration, para->thread,
           para->arrayPntr, left, left + para->size, (left + (2 * para->size)));
    free(param);
    pthread_exit(NULL);
}

int main(int argc, char **argv) {
    printf("CS149 Fall 2020 parallel sort from Carter Gale\r\n");

    int numInts;
    int numPartitions;

    //The value we use to check various returns for errors
    int retcheck;
    retcheck = sscanf(argv[1], "%d", &numInts);
    if (retcheck == 0) {
        fprintf(stderr, "Invalid number of integers, please make sure it is an integer");
        return 0;
    }
    if (numInts < 1) {
        fprintf(stderr, "Number of integers cannot be less than 1\r\n");
        return 0;
    }

    retcheck = sscanf(argv[2], "%d", &numPartitions);
    if (retcheck == 0) {
        fprintf(stderr, "Invalid number of partitions, please make sure it is an integer\r\n");
        return 0;
    }
    if (numPartitions < 1) {
        fprintf(stderr, "Number of partitions cannot be less than 1\r\n");
        return 0;
    }

    //Check for power of two using some fun bitwise operations
    if (numPartitions & (numPartitions - 1)) {
        fprintf(stderr, "Number of partitions must be a power of two\r\n");
        return 0;
    }

    if (numInts % numPartitions != 0) {
        fprintf(stderr, "Number of integers must be divisible equally by number of partitions\r\n");
        return 0;
    }

    //random value for the seed of our randomizer
    unsigned int seed = time(NULL);

    //int* = int[] type, here we allocate the memory for our array
    int *numberArray = (int *) malloc(sizeof(int) * numInts);
    printf("before: ");
    for (int i = 0; i < numInts; i++) {
        numberArray[i] = rand_r(&seed) % 10000;
        printf("%d ", numberArray[i]);
    }
    printf("\r\n");

    if (numPartitions == 1) {
        qsort_r(numberArray, numInts, sizeof(int), cmpintp, NULL);
    } else {
        pthread_t workers[numPartitions];
        phaseOneParam* paramArray[numPartitions];
        for (int i = 0; i < numPartitions; i++) {
            //Allocate and set the data of the parameters for the thread
            phaseOneParam *pParam = (phaseOneParam *) malloc(sizeof(phaseOneParam));

            //Add the numbers to our data as well as allocate space for our data array
            pParam->pData = malloc(sizeof(int) * (numInts / numPartitions));
            for (int z = 0; z < numInts / numPartitions; z++) {
                pParam->pData[z] = numberArray[(i * (numInts / numPartitions)) + z];
            }
            pParam->x = i;
            pParam->size = numInts / numPartitions;

            //Add our parameters to the array of parameters
            paramArray[i] = pParam;

            pthread_t tid;
            pthread_attr_t attr;

            //Set the attributes
            retcheck = pthread_attr_init(&attr);
            if (retcheck != 0) {
                fprintf(stderr, "Something went wrong with setting attributes of the thread with error code: %d\r\n",
                        retcheck);
                return 0;
            }
            //Now create the thread passing it data as a parameter
            pthread_create(&tid, &attr, phaseonerunner, (void *) pParam);
            workers[i] = tid;
        }

        //Wait for the threads from phase 1 to complete
        for (int i = 0; i < numPartitions; i++) {
            retcheck = pthread_join(workers[i], NULL);
            if (retcheck != 0) {
                fprintf(stderr, "Something went wrong with joining of thread %d with error code %d\r\n", i, retcheck);
                return 0;
            }

            //Combine the data back from the parameters into the original array
            for (int z = 0; z < numInts / numPartitions; z++) {
                numberArray[(i * (numInts / numPartitions)) + z] = paramArray[i]->pData[z];
            }

            //Free memory that was used that is no longer needed
            free(paramArray[i]->pData);
            free(paramArray[i]);
        }

        //Some variables that get modified within the context of the loop
        int sizeOfCombination = numInts / numPartitions; // the current amount of partitions we have
        int iteration = 0;
        int *tempNumberArray = (int *) malloc(sizeof(int) * numInts);
        //We iterate until just before we'd be combining the array with itself
        while (sizeOfCombination != numInts) {
            //Create the threads
            for (int i = 0; i < numInts / sizeOfCombination; i += 2) {
                //Now create the thread passing it data as a parameter
                pthread_t tid;
                pthread_attr_t attr;
                //Set the attributes
                retcheck = pthread_attr_init(&attr);
                if (retcheck != 0) {
                    fprintf(stderr,
                            "Something went wrong with setting attributes of the thread with error code: %d\r\n",
                            retcheck);
                    return 0;
                }
                //Allocate and set the data of the parameters for the thread
                phaseTwoParam *pParam = (phaseTwoParam *) malloc(sizeof(phaseTwoParam));

                //Add the numbers to our data as well as allocate space for our data array
                pParam->arrayPntr = numberArray;
                pParam->tempPntr = tempNumberArray;
                pParam->thread = i;
                pParam->iteration = iteration;
                pParam->size = sizeOfCombination;

                pthread_create(&tid, &attr, phasetworunner, (void *) pParam);
                workers[i] = tid;
            }

            for (int i = 0; i < numInts / sizeOfCombination; i += 2) {
                retcheck = pthread_join(workers[i], NULL);
                if (retcheck != 0) {
                    fprintf(stderr, "Something went wrong with joining of thread %d with error code %d\r\n", i,
                            retcheck);
                    return 0;
                }
            }
            iteration += 1;
            sizeOfCombination *= 2;
        }

        //Get rid of the temp memory we used
        free(tempNumberArray);
    }

    printf("after: ");
    for (int i = 0; i < numInts; i++) {
        printf("%d ", numberArray[i]);
    }
    printf("\r\n");

    free(numberArray);
    return 0;
}