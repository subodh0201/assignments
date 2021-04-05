/******************************************************************************
 * Name : Subodh Singh
 * Roll : ************
 * Dept : Information Technology
 * Year : 2nd   Semester : 4th
 * Subject : Software Engineering Lab
 * ----------------------------------------------------------------------------
 * Assignment : Matrix Multiplication Using a Parallel Algorithm 
 * ----------------------------------------------------------------------------
 *  The objective of this assignment is to write a C program which will do 
 *  matrix multiplication using a parallel algorithm. The two input matrices 
 *  are A and B. The resultant matrix after multiplication is C. 
 * 
 *  The program will accept three arguments; the first one is for creating 
 *  number of parallel threads, the second one is the dimension of the 
 *  square matrix and the third one is about displaying matrices. 
 * 
 *  The value of the first argument should be equal to the number of 
 *  CPUs/Logical processors in the system. 
 *  The value of the 2​nd argument should be  around 1000/1500/2000. 
 *  The value of the third argument should be either 1 or 0. 
 *  If it is 1 then your program will display all matrices at the end. 
 *  If the value of the third argument is 0, then no matrix will be displayed.
 * ----------------------------------------------------------------------------
 * Compilation : 
 *      gcc 40_Subodh_Singh.c -o 40_Subodh_Singh -lpthread
 * ----------------------------------------------------------------------------
 * Execution :
 *      ./40_Subodh_Singh [threads] [dimension] [display]
 * 
 *          * [threads]   : - number of threads to be used 
 *                          - Should be greater than 0
 *          * [dimension] : - dimension of the square matrix
 *                          - Should be greater than 0    
 *          * [display]   : - Should be either 0 or 1 
 *                          - If it is 1 all matrices are displayed at the end
 * ----------------------------------------------------------------------------
 * Sample Execution[1] : [Validating Correctness of Matrix Multiplication]
 *  ./40_Subodh_Singh 8 4 1
 * 
 * Output Generated :
 *  Time taken = 0.000976 seconds
 * 
 *  Matrix A : 
 *  1 1 1 1
 *  1 0 0 1
 *  1 0 0 1
 *  0 0 1 1
 * 
 *  Matrix B : 
 *  1 1 1 1
 *  0 0 0 0
 *  0 0 0 1
 *  1 1 0 0
 * 
 *  Matrix C : 
 *  2 2 1 2
 *  2 2 1 1
 *  2 2 1 1
 *  1 1 0 1
 * ----------------------------------------------------------------------------
 * Sample Execution[2] : Validating usage of multiple Processors
 *  ./40_Subodh_Singh 8 1000 0
 * 
 * Output Generated :
 *  Time taken = 2.265608 seconds
 * 
 * Simultaneously mpstat was used to measure CPU usage
 * 
 * CPU usage before running the program :
 * 09:12:37 PM IST  CPU    %usr   %nice    %sys %iowait    %irq   %soft  %steal  %guest  %gnice   %idle
 * 09:12:38 PM IST  all   19.23    0.00    3.75    0.00    0.00    0.37    0.00    0.00    0.00   76.65
 * 09:12:38 PM IST    0   16.16    0.00    3.03    0.00    0.00    0.00    0.00    0.00    0.00   80.81
 * 09:12:38 PM IST    1   15.46    0.00    2.06    0.00    0.00    0.00    0.00    0.00    0.00   82.47
 * 09:12:38 PM IST    2   17.17    0.00    3.03    0.00    0.00    0.00    0.00    0.00    0.00   79.80
 * 09:12:38 PM IST    3   23.08    0.00    2.88    0.00    0.00    0.00    0.00    0.00    0.00   74.04
 * 09:12:38 PM IST    4   19.59    0.00    3.09    0.00    0.00    0.00    0.00    0.00    0.00   77.32
 * 09:12:38 PM IST    5   19.00    0.00    4.00    0.00    0.00    0.00    0.00    0.00    0.00   77.00
 * 09:12:38 PM IST    6   18.81    0.00    4.95    0.00    0.00    0.99    0.00    0.00    0.00   75.25
 * 09:12:38 PM IST    7   24.04    0.00    6.73    0.00    0.00    1.92    0.00    0.00    0.00   67.31
 * 
 * CPU usage while the program was running
 * 09:12:38 PM IST  CPU    %usr   %nice    %sys %iowait    %irq   %soft  %steal  %guest  %gnice   %idle
 * 09:12:39 PM IST  all   98.37    0.00    1.63    0.00    0.00    0.00    0.00    0.00    0.00    0.00
 * 09:12:39 PM IST    0   97.00    0.00    3.00    0.00    0.00    0.00    0.00    0.00    0.00    0.00
 * 09:12:39 PM IST    1   99.00    0.00    1.00    0.00    0.00    0.00    0.00    0.00    0.00    0.00
 * 09:12:39 PM IST    2   98.00    0.00    2.00    0.00    0.00    0.00    0.00    0.00    0.00    0.00
 * 09:12:39 PM IST    3  100.00    0.00    0.00    0.00    0.00    0.00    0.00    0.00    0.00    0.00
 * 09:12:39 PM IST    4   97.00    0.00    3.00    0.00    0.00    0.00    0.00    0.00    0.00    0.00
 * 09:12:39 PM IST    5  100.00    0.00    0.00    0.00    0.00    0.00    0.00    0.00    0.00    0.00
 * 09:12:39 PM IST    6   99.01    0.00    0.99    0.00    0.00    0.00    0.00    0.00    0.00    0.00
 * 09:12:39 PM IST    7   96.97    0.00    3.03    0.00    0.00    0.00    0.00    0.00    0.00    0.00
 * 
 * As we can see when we run the program the usage of all 8 CPUs rise to 
 * around 100% which confirms that all the 8 CPUs are used in this process.
 *****************************************************************************/

#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <time.h>

// Matrices 
unsigned int **A;       // matrix A
unsigned int **B;       // matrix B
unsigned int **C;       // resultant matrix C = A x B

// Arguments
int threadCount;        // number of threads to be used
int matrixDimension;    // dimension of sqaure matrices
int toDisplay;          // display matrices if equal to 1

// function to display matrices
void printMatrices();
// function to initialise matrices
void initMatrices(); 
// function called by each thread to calculate a part of the result
void *childThreadMultiply(void* partPtr);   


int main(int argc, char *argv[]) {
    // checking number of arguments
    if (argc != 4) {
        printf("Invalid number of arguments\n");
        return EXIT_FAILURE;
    }

    // converting arguments from string to integers 
    threadCount = atoi(argv[1]);        // number of threads to be used
    matrixDimension = atoi(argv[2]);    // dimension of sqaure matrices
    toDisplay = atoi(argv[3]);          // display matrices if equal to 1

    // checking each argument
    if (threadCount <= 0) {     // threadCount should be positive
        printf("Number of threads [1st arg] should be greater than 0\n");
        return EXIT_FAILURE;
    }

    if (matrixDimension <= 0) { // matrixDimension should be positive
        printf("Dimension of matrix [2nd arg] should be greater than 0\n");
        return EXIT_FAILURE;
    }

    if (toDisplay < 0 || toDisplay > 1) {
        printf("Third argument should be either 0 or 1\n");
        return EXIT_FAILURE;
    }

    if (threadCount > matrixDimension) {    // unnecessary threads
        threadCount = matrixDimension;
    }

    if (matrixDimension % threadCount != 0) {   // dimension not divisible
        printf("Dimension of matrix cannot be divided into equal parts\n");
        return EXIT_FAILURE;
    }

    // allocate space for A, B and C and initialize A and B with 1s and 0s
    initMatrices();     
    
    // variables to keep track of time
    struct timeval start, end;
    // getting the current time of the day : start time
    gettimeofday(&start, NULL); 

    // pthread handles
    pthread_t pthread_hndl[threadCount];

    // argument for function called through each thread 
    // to be stored in this array
    int part[threadCount];

    for (int i = 0; i < threadCount; i++) {
        // ith part of the matrix to be calculated by ith thread
        part[i] = i;
        // creating thread
        int ret = pthread_create(&pthread_hndl[i], NULL, childThreadMultiply, &part[i]);
        if (ret != 0) printf("Error in pthread_create\n");
    }

    // waiting for threads to complete and joining them
    for (int i = 0; i < threadCount; i++)
        pthread_join(pthread_hndl[i], NULL);

    // getting the current time of the day : end time
    gettimeofday(&end, NULL);   
    // start time in second
    double stime = (start.tv_sec * 1000000 + start.tv_usec)/1000000.0;
    // end time in second
    double etime = (end.tv_sec * 1000000 + end.tv_usec)/1000000.0;
    // time taken for matrix multiplication using parallel processing
    double timeTaken = (etime - stime);
    
    printf("Time taken = %lf seconds\n", timeTaken);
    
    // print matrices if toDisplay is 1
    if (toDisplay) printMatrices();

    return EXIT_SUCCESS;
}    


/**
 * A function called by each thread which calculates a part of the matrix C
 * It takes a int * argument (as void *) which is pointer to part number
 * This part number determines which part of the matrix will be calculated
 *
 * @params partPtr pointer to the part number
 *
 */
void *childThreadMultiply(void *partPtr) {
    int part = *(int*)partPtr;  // part to be calculated
    // number of rows this function calculates
    int size = matrixDimension / threadCount; 

    // part of the matrix calculated  
    for (int i = part*size; i < (part + 1) * size; i++) {
        for (int j = 0; j < matrixDimension; j++) {
            for (int k = 0; k < matrixDimension; k++) {
                C[i][j] += (A[i][k] * B[k][j]);
            }
        }
    }
    pthread_exit (0);
}


/**
 * A function to allocate space for matrices and 
 * initialise matrices A and B with 0s and 1s
 * randomly
 */
void initMatrices() {
    // allocating space for matrix A
    A = (unsigned int**)malloc(sizeof(unsigned int*) * matrixDimension);
    for (int i = 0; i < matrixDimension; i++) {
        A[i] = (unsigned int*)malloc(sizeof(unsigned int) * matrixDimension);
    }

    // allocating space for matrix B
    B = (unsigned int**)malloc(sizeof(unsigned int*) * matrixDimension);
    for (int i = 0; i < matrixDimension; i++) {
        B[i] = (unsigned int*)malloc(sizeof(unsigned int) * matrixDimension);
    }

    // allocating space for matrix C
    C = (unsigned int**)malloc(sizeof(unsigned int*) * matrixDimension);
    for (int i = 0; i < matrixDimension; i++) {
        C[i] = (unsigned int*)malloc(sizeof(unsigned int) * matrixDimension);
    }
    
    // seeding the random number generator with current time
    srand(time(NULL));
    // intialising A and B with 1s and 0s at random
    for (int i = 0; i < matrixDimension; i++) {
        for (int j = 0; j < matrixDimension; j++) {
            A[i][j] = rand() % 2;
            B[i][j] = rand() % 2;
        }
    }
}


/**
 * A function to print the matrices A, B, and C
 */
void printMatrices() {
    // printing the matrix A
    printf("\nMatrix A : \n");
    for (int i = 0; i < matrixDimension; i++) {
        for (int j = 0; j < matrixDimension; j++) {
            printf("%d%s", A[i][j], (j == matrixDimension - 1) ? "\n" : " ");
        }
    }

    // printing the matrix B
    printf("\nMatrix B : \n");
    for (int i = 0; i < matrixDimension; i++) {
        for (int j = 0; j < matrixDimension; j++) {
            printf("%d%s", B[i][j], (j == matrixDimension - 1) ? "\n" : " ");
        }
    }

    // printing the matrix C
    printf("\nMatrix C : \n");
    for (int i = 0; i < matrixDimension; i++) {
        for (int j = 0; j < matrixDimension; j++) {
            printf("%d%s", C[i][j], (j == matrixDimension - 1) ? "\n" : " ");
        }
    }
    printf("\n");
}
