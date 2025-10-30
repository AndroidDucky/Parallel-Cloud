#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <cuda_runtime.h>
#include <string.h>
#include "cuFunctions.h"
#include <sys/time.h>

extern "C"{
#include "cFunctions.h"
#include "mergeSort_common.h"
}


int main(int argc, char *argv[])
{
    FILE *fp;
    float time = 0;
    //declarations for Gpu
    char** arrayLines = NULL;
    char** words = NULL;
    uint* wordCount;
    char** uniqueWords;
    int count=0;
    int uniqueCount=0;
    const uint N = 1048576;

    //for cpu
    char word[100];

    //Initializing variables
    wordCount = (uint *)malloc(N * sizeof(uint));
    const int placeholder =  30000;
    uniqueWords = (char **)malloc(placeholder * sizeof(char *));
    int lineLength;
    words = (char **)malloc(30000 * sizeof(char *));
    for(int i = 0; i<30000;i++){
        words[i] =  (char *)malloc(lineLength + 1);
    }




    //file opening
    fp = fopen(argv[1], "r");
    //checking if the file is accessible
    if (fp == NULL)
    {
        printf("Unable to open file.\n");
        exit(EXIT_FAILURE);
    }



    //For Gpu code unstub code for disappointment
    //arrayLines = readLines(fp, &count);
    //parseLine(arrayLines, count,words);
    //distinctCount(words,wordCount,&uniqueCount, uniqueWords);




    //cpu code
    words=fillArray(fp);
    distinctCount(words,wordCount,&uniqueCount, uniqueWords);
    rewind(fp);
    double then = currentTime();
    cpuTest(fp);
    double now =  currentTime();
    printf( "Processing time: %f (ms) CPU\n", (now - then) * 1000.0);

    //testing for distinctCount and parseLine


    fclose(fp);
   /* for (int i = 0; i < count; i++) {
        free(arrayLines[i]);
    }*/


    /* MERGE SORT STUFF DON'T TOUCH PLEASE! */

    /*printf("Word: %s\n", uniqueWords[0]);*/
    //h_SrcKey ====> wordCount
    //h_words =====> uniqueWords
    //TrueN ====> uniqueCount
    uint *h_SrcVal, *h_DstKey, *h_DstVal;
    char **h_wordsFinal;
    uint *d_SrcKey, *d_SrcVal, *d_BufKey, *d_BufVal, *d_DstKey, *d_DstVal;

    //assert: mergeSort.cu line 595
    /*const uint   N = 48 * 1048576;*/
    const uint DIR = 1;
    const uint numValues = 65536;

    //This is how many unique words we have (it will not be 10, that is a placeholder)
    //const int TrueN = 10;  //TrueN is now called uniqueCount


    printf("Allocating and initializing host arrays...\n\n");
    //h_SrcKey = (uint *)malloc(N * sizeof(uint));
    h_SrcVal = (uint *)malloc(N * sizeof(uint));
    h_DstKey = (uint *)malloc(N * sizeof(uint));
    h_DstVal = (uint *)malloc(N * sizeof(uint));
    //h_words = (char **)malloc(TrueN * sizeof(char *));
    h_wordsFinal = (char **)malloc(uniqueCount * sizeof(char *));

    fillValues(h_SrcVal, N);

    cudaMalloc((void **)&d_DstKey, N * sizeof(uint));
    cudaMalloc((void **)&d_DstVal, N * sizeof(uint));
    cudaMalloc((void **)&d_BufKey, N * sizeof(uint));
    cudaMalloc((void **)&d_BufVal, N * sizeof(uint));
    cudaMalloc((void **)&d_SrcKey, N * sizeof(uint));
    cudaMalloc((void **)&d_SrcVal, N * sizeof(uint));
    cudaMemcpy(d_SrcKey, wordCount, N * sizeof(uint), cudaMemcpyHostToDevice);
    cudaMemcpy(d_SrcVal, h_SrcVal, N * sizeof(uint), cudaMemcpyHostToDevice);

    //mergeSort.cu
    //printf("Initializing GPU merge sort...\n");
    initMergeSort();

    cudaDeviceSynchronize();

    mergeSort(
            d_DstKey,
            d_DstVal,
            d_BufKey,
            d_BufVal,
            d_SrcKey,
            d_SrcVal,
            N,
            DIR
    );
    cudaDeviceSynchronize();

    cudaMemcpy(h_DstKey, d_DstKey, N * sizeof(uint), cudaMemcpyDeviceToHost);
    cudaMemcpy(h_DstVal, d_DstVal, N * sizeof(uint), cudaMemcpyDeviceToHost);

    /*printf("Word: %s\n", uniqueWords[0]);
    printf("Unique Count: %d\n", uniqueCount);*/


    int b = 0;
    int counting = 0;
    while (uniqueWords[b] != NULL){
        /*printf("%s\n", uniqueWords[b]);*/
        b++;
        counting++;
    }

    uniqueCount = counting;

    uint index;
    uint j = 0;
    for (uint k = N - uniqueCount; k < N; k++){
        /*printf("k: %u\n", k);*/
        index = h_DstVal[k];
        /*printf("h_DstVal[%u](index of original array): %u\n", k, h_DstVal[k]);
        printf("h_DstKey[%u](count that corresponds): %u\n", k, h_DstKey[k]);*/
        h_wordsFinal[j] = uniqueWords[index];
        //strcpy(h_wordsFinal[j], uniqueWords[index]);
        j++;
    }

    /*for (int m = 0; m < uniqueCount; m++){
        printf("Word: %s +%-15d\n", h_wordsFinal[m], h_DstKey[N - uniqueCount + m]);
    }*/

    uint *h_CountFinal;
    h_CountFinal = (uint *)malloc(uniqueCount * sizeof(uint));
    for (int x = 0; x < uniqueCount; x++){
        h_CountFinal[x] = h_DstKey[N - uniqueCount + x];
    }

    /*printf("Word: %s +%-15d\n", h_wordsFinal[0], h_CountFinal[0]);*/

    /*printf("\n\n-----------------------------------------------\n\n");
    for (int m = 0; m < uniqueCount; m++){
        printf("Word: %s +%-15d\n", h_wordsFinal[m], h_CountFinal[m]);
    }*/

    countSortPrint(h_wordsFinal, h_CountFinal, 'c', uniqueCount);


    printf("Inspecting the results...\n");
    uint keysFlag = validateSortedKeys(
            h_DstKey,
            wordCount,
            1,
            N,
            numValues,
            DIR
    );

    uint valuesFlag = validateSortedValues(
            h_DstKey,
            h_DstVal,
            wordCount,
            1,
            N
    );

    //countSortPrint(h_wordsFinal, wordCount, "c");

    printf("Shutting down...\n");
    closeMergeSort();
    cudaFree(d_SrcVal);
    cudaFree(d_SrcKey);
    cudaFree(d_BufVal);
    cudaFree(d_BufKey);
    cudaFree(d_DstVal);
    cudaFree(d_DstKey);
    free(h_DstVal);
    free(h_DstKey);
    free(h_SrcVal);
    free(wordCount);
    free(uniqueWords);
    free(h_wordsFinal);
    cudaDeviceReset();

    exit((keysFlag && valuesFlag) ? EXIT_SUCCESS : EXIT_FAILURE);

    return (0);
}


