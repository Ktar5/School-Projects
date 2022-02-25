#include <stdio.h>
#include <pthread.h>
#include <time.h>
#include <stdlib.h>
#include <unistd.h>

/* the maximum time (in seconds) to sleep */
#define MAX_SLEEP_TIME 3
/* max number of customers in bank */
#define MAX_WAITING_CUSTOMERS 2
/* number of customers */
#define NUM_CUSTOMERS 5
/* number of bank tellers */
#define NUM_TELLERS 2
/* # of services each customer must get before exit */
#define MAX_SERVICES 2


// Protects the waiting_customers variable
pthread_mutex_t mutex;
/* # of waiting customers */
int waiting_customers;

// Control synchronization between customer and teller threads
pthread_cond_t cond_customer, cond_teller;

typedef struct {
    int id;
} customer_params;

typedef struct {
    int id;
} teller_params;

void *customer_thread(void *param) {
    int retcheck = 0;
    //Get parameters
    customer_params *para = ((customer_params *) param);

    //The random seed value for this thread's sleep time below
    unsigned int seed = time(NULL);

    //The number of services that this customer has transacted
    //the customer will exit the bank once this reaches MAX_SERVICES
    int services_had = 0;

    while (services_had < MAX_SERVICES) {
        //Run an errand
        int sleep_time = (rand_r(&seed) % MAX_SLEEP_TIME) + 1;
        printf("customer[%d, %d]: running errand for %d seconds\r\n", para->id, services_had, sleep_time);
        fflush(NULL);
        sleep(sleep_time);

        //Attempt to lock so that we can reliably read/write to waiting customers
        retcheck = pthread_mutex_lock(&mutex);
        if (retcheck != 0) {
            fprintf(stderr, "Something went wrong with customer cond init, error code: %d\r\n", retcheck);
            fflush(NULL);
            return 0;
        }

        //Run errands and sometimes check back on the waiting line
        while (waiting_customers >= MAX_WAITING_CUSTOMERS) {
            printf("customer[%d, %d]: line is full \r\n", para->id, services_had);
            fflush(NULL);

            //Unlock before sleeping
            retcheck = pthread_mutex_unlock(&mutex);
            if (retcheck != 0) {
                fprintf(stderr, "Something went wrong with customer condition unlocking, error code: %d\r\n", retcheck);
                fflush(NULL);
                return 0;
            }

            //Go shopping
            sleep_time = (rand_r(&seed) % MAX_SLEEP_TIME) + 1;
            printf("customer[%d, %d]: running errand for %d seconds\r\n", para->id, services_had, sleep_time);
            fflush(NULL);
            sleep(sleep_time);

            //Return the lock when we check again
            retcheck = pthread_mutex_lock(&mutex);
            if (retcheck != 0) {
                fprintf(stderr, "Something went wrong with customer condition locking, error code: %d\r\n", retcheck);
                fflush(NULL);
                return 0;
            }
        }

        waiting_customers += 1;
        printf("customer[%d, %d]: now standing in line at place: %d\r\n", para->id, services_had,
               waiting_customers);
        fflush(NULL);

        retcheck = pthread_cond_signal(&cond_customer);
        if (retcheck != 0) {
            fprintf(stderr, "Something went wrong with customer cond signal, error code: %d\r\n", retcheck);
            fflush(NULL);
            return 0;
        }

        //now we need to have our second shared state for waiting for the teller
        retcheck = pthread_cond_wait(&cond_teller, &mutex);
        if (retcheck != 0) {
            fprintf(stderr, "Something went wrong with customer cond wait, error code: %d\r\n", retcheck);
            fflush(NULL);
            return 0;
        }

        retcheck = pthread_mutex_unlock(&mutex);
        if (retcheck != 0) {
            fprintf(stderr, "Something went wrong with customer cond unlock, error code: %d\r\n", retcheck);
            fflush(NULL);
            return 0;
        }

        services_had += 1;
        printf("customer[%d, %d]: receive service (service num: %d)\r\n", para->id, services_had, services_had);
        fflush(NULL);
    }

    printf("customer[%d, %d]: reached max services\r\n", para->id, services_had);
    fflush(NULL);

    free(param);
    pthread_exit(NULL);

}

void *teller_thread(void *param) {
    int retcheck = 0;
    //Get parameters
    teller_params *para = ((teller_params *) param);

    int totalNumServices = 0;

    //The random seed value for this thread's sleep time below
    unsigned int seed = time(NULL);

    while (1) {
        //Acquire mutex lock because we're going to check if there are any waiting
        retcheck = pthread_mutex_lock(&mutex);
        if (retcheck != 0) {
            fprintf(stderr, "Something went wrong with locking the mutex, error code: %d\r\n", retcheck);
            fflush(NULL);
            return 0;
        }

        //Wait for customers
        while (waiting_customers == 0) {
            printf("teller[%d, %d]: waiting for customer\r\n", para->id, totalNumServices);
            fflush(NULL);
            //Wait for condition wait and unlock mutex
            retcheck = pthread_cond_wait(&cond_customer, &mutex);
            if (retcheck != 0) {
                fprintf(stderr, "Something went wrong with teller cond_customer wait, error code: %d\r\n", retcheck);
                fflush(NULL);
                return 0;
            }
        }

        //We now have a customer
        printf("teller[%d, %d]: got a new customer\r\n", para->id, totalNumServices);
        fflush(NULL);

        waiting_customers -= 1;

        //Provide service
        int sleep_time = (rand_r(&seed) % MAX_SLEEP_TIME) + 1;
        printf("teller[%d, %d]: starting service to customer for %d seconds, waiting customers = %d\r\n", para->id,
               totalNumServices, sleep_time, waiting_customers);
        fflush(NULL);
        sleep(sleep_time);

        //Signal that service is complete
        retcheck = pthread_cond_signal(&cond_teller);
        if (retcheck != 0) {
            fprintf(stderr, "Something went wrong with teller cond signal, error code: %d\r\n", retcheck);
            fflush(NULL);
            return 0;
        }

        retcheck = pthread_mutex_unlock(&mutex);
        if (retcheck != 0) {
            fprintf(stderr, "Something went wrong with mutex unlock, error code: %d\r\n", retcheck);
            fflush(NULL);
            return 0;
        }

        totalNumServices += 1;

        printf("teller[%d, %d]: completed service to customer, waiting customers = %d\r\n", para->id,
               totalNumServices, waiting_customers);
        fflush(NULL);


    }

    free(param);
    pthread_exit(NULL);
}

int main() {
    printf("CS149 Fall 2020 Spartan Bank from Carter Gale\n");
    fflush(NULL);

    //Initialize the various condition and mutex variables
    int retcheck = pthread_mutex_init(&mutex, NULL);
    if (retcheck != 0) {
        fprintf(stderr, "Something went wrong with mutex init, error code: %d\r\n", retcheck);
        fflush(NULL);
        return 0;
    }

    retcheck = pthread_cond_init(&cond_customer, NULL);
    if (retcheck != 0) {
        fprintf(stderr, "Something went wrong with customer cond init, error code: %d\r\n", retcheck);
        fflush(NULL);
        return 0;
    }

    retcheck = pthread_cond_init(&cond_teller, NULL);
    if (retcheck != 0) {
        fprintf(stderr, "Something went wrong with teller cond init, error code: %d\r\n", retcheck);
        fflush(NULL);
        return 0;
    }

    //Create arrays to store our thread IDs
    pthread_t customer_threads[NUM_CUSTOMERS];
    pthread_t teller_threads[NUM_TELLERS];

    //Create all the teller threads
    for (int i = 0; i < NUM_TELLERS; i++) {
        pthread_t tid;
        pthread_attr_t attr;

        //Allocate and set the data of the parameters for the thread
        teller_params *tellerParams = (teller_params *) malloc(sizeof(teller_params));
        tellerParams->id = i;

        //Set the attributes
        retcheck = pthread_attr_init(&attr);
        if (retcheck != 0) {
            fprintf(stderr, "Something went wrong with setting attributes of the thread with error code: %d\r\n",
                    retcheck);
            fflush(NULL);
            return 0;
        }
        //Now create the thread passing it data as a parameter
        retcheck = pthread_create(&tid, &attr, teller_thread, (void *) tellerParams);
        if (retcheck != 0) {
            fprintf(stderr, "Something went wrong with creating threads.. error code: %d\r\n", retcheck);
            fflush(NULL);
            return 0;
        }
        teller_threads[i] = tid;
    }


    //Create all the customer threads
    for (int i = 0; i < NUM_CUSTOMERS; i++) {
        pthread_t tid;
        pthread_attr_t attr;

        //Allocate and set the data of the parameters for the thread
        customer_params *customerParams = (customer_params *) malloc(sizeof(customer_params));
        customerParams->id = i;

        //Set the attributes
        retcheck = pthread_attr_init(&attr);
        if (retcheck != 0) {
            fprintf(stderr, "Something went wrong with setting attributes of the thread with error code: %d\r\n",
                    retcheck);
            fflush(NULL);
            return 0;
        }
        //Now create the thread passing it data as a parameter
        retcheck = pthread_create(&tid, &attr, customer_thread, (void *) customerParams);
        if (retcheck != 0) {
            fprintf(stderr, "Something went wrong with creating threads.. error code: %d\r\n", retcheck);
            fflush(NULL);
            return 0;
        }
        customer_threads[i] = tid;
    }

    //Wait for all the customers to be done then remove them
    for (int i = 0; i < NUM_CUSTOMERS; i++) {
        int retcheck = pthread_join(customer_threads[i], NULL);
        if (retcheck != 0) {
            fprintf(stderr, "Something went wrong joining customer threads with error code: %d\r\n", retcheck);
            fflush(NULL);
            return 0;
        }
    }

    //Once all customers are done (above) cancel all tellers
    for (int i = 0; i < NUM_TELLERS; i++) {
        int retcheck = pthread_cancel(teller_threads[i]);
        if (retcheck != 0) {
            fprintf(stderr, "Something went wrong joining teller threads with error code: %d\r\n", retcheck);
            fflush(NULL);
            return 0;
        }
    }

    //Destroy all our mutex/condition variables
    pthread_mutex_destroy(&mutex);
    pthread_cond_destroy(&cond_customer);
    pthread_cond_destroy(&cond_teller);

    printf("Main is done\r\n");

    return 0;
}
