#include <stdio.h>
#include "ctype.h"
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include "cuFunctions.h"

__device__ int globalIdx = 0;
__device__ int* mutex;
__device__ void lock(){
    while(atomicCAS(mutex, 0, 1)!=0);
}
__device__ void unlock(){
    atomicExch(mutex,0);
}


__device__ int IsLetter(char c){
    if(c == 'a'|| c == 'b'||c == 'c'||c == 'd'||c == 'e'||c == 'f'||c == 'g'||c == 'h'||c == 'i'||c == 'j'||c == 'k'||c == 'l'||c == 'm'||c == 'n'||c == 'o'||c == 'p'||c == 'q'||c == 'r'||c == 's'||c == 't'||c =='u'||c == 'v'||c == 'w'||c == 'x'||c == 'y'||c == 'z'||c == 'A'|| c == 'B'||c == 'C'||c == 'D'||c == 'E'||c == 'F'||c== 'G'||c == 'H'||c == 'I'||c == 'J'||c == 'K'||c == 'L'||c == 'M'||c == 'N'||c == 'O'||c == 'P'||c == 'Q'||c == 'R'||c =='S'||c == 'T'||c == 'U'||c == 'V'||c == 'W'||c == 'X'||c == 'Y'||c == 'Z'){
        return 1;
    }
    return 0;
}

__device__ char * my_strcpy(char *dest, const char *src){
  int i = 0;
  do {
    dest[i] = src[i];}
  while (src[i++] != 0);
  return dest;
}

__global__ void parselineKernel( char **linesIn, char** arrOut, const int linesLength){

    int x = blockIdx.x * blockDim.x + threadIdx.x;
    //const int y = blockIdx.y * blockDim.y + Threa     ^~~~dIdx.y;

    
    if(x < linesLength) {
        char aline[10000]={"\0"};


	char* arr[100];
        for(int i =0; i<100;i++){
        	arr[i] = '\0';
        }
        my_strcpy(aline, linesIn[x]);
        //:make sure that aline actually has
        // a length and is not passed a pointer

        int wrdCntPerThread = 0;

        bool inWord = false; //

        int i = -1;//first index-> first char in sentence
        int wLen = 0;// length of word
        int start = 0;//start of word

        do {
            i++;
            if (IsLetter(aline[i])) {
                //aline[i] = tolower(aline[i]);
                if (!inWord) {
                    start = i;//reached the start of a new word
                    inWord = true;
                }//end 2nd if
                wLen++;
            }//end 1st if
            else if (inWord) {
                char newWord[50] = {'\0'};
                //cudaMemset(newWord, '\0', sizeof(newWord));//og: memset(...)
                //strncpy(newWord, &aline[start], wLen);//copies a word at the start of the new word of length wLen
                for(int j = 0; j < 50; j++){
                    int k = start;
                    if(k != start+wLen){
                        newWord[j] = aline[k];
                        k++;
                    }
                }
                if (wLen != 1 || newWord[0] == 'a' || newWord[0] == 'A' ||
                    newWord[0] == 'i' || newWord[0] == 'I') {
                    arr[wrdCntPerThread]=newWord;//
                    wrdCntPerThread++;
                }//end if
                wLen = 0;
                inWord = false;
                start = 0;
            }//end else if
        } while (aline[i] != '\n' && aline[i] != '\0');//end of while


        lock();
        for(int i = 0; i < wrdCntPerThread; i++){
            arrOut[globalIdx] = arr[i];
            globalIdx++;
        }
        unlock();

    }
}


//method call to parse the line
int parseLine( char** arr, int numCols, char** arrOut) {
    int dimx = numCols;
    int dimy = 10000;//10,000 or 100,000?
    dim3 grid, block;
    int num_bytes = dimx*sizeof(char*);

    char **d_a;
    cudaMalloc((void **) &d_a, num_bytes);
    for(int i = 0; i < dimx; i++){
        cudaMalloc((void**)&d_a[i], dimy* sizeof(char));
    }

    char **d_words;
    cudaMalloc((void**)&d_words, 30000*sizeof(char*));
    for(int i = 0; i < 30000; i++){
        cudaMalloc((void**)&d_a[i], 50* sizeof(char));
    }

    if (d_a == 0) {
        printf("error");
        return 0;
    }

    if(d_words == 0){
        printf("err device arr for words");
        return 0;
    }
    printf("fault2? \n");
    //cudaMemset(d_a, '\0', dimy);
    cudaMemset2D(d_a,dimx,'\0',dimx,dimy);
    //cudaMemcpy(d_a, arr, dimy, cudaMemcpyHostToDevice);
    cudaMemcpy2D(d_a,dimx ,arr ,dimx ,dimx ,10000 , cudaMemcpyHostToDevice);
    printf("fault3? \n");
    //cudaMemset(d_words,'\0', 50);
    cudaMemset2D(d_words,30000,'\0',30000,50);

    block.x = 4;
    block.y = 4;
    grid.x = ceil((float) dimx / block.x);
    grid.y = ceil((float) dimy / block.y);
    printf("fault3? \n");
    parselineKernel<<<grid, block>>>(d_a, d_words , numCols);
    printf("fault4? \n");
    //cudaMemcpy(arrOut, d_words, 50, cudaMemcpyDeviceToHost);
    cudaMemcpy2D(arrOut,30000 ,d_words ,30000 ,30000 ,50 , cudaMemcpyDeviceToHost);
    cudaFree(d_words);
    cudaFree(d_a);
    return 0;

}

