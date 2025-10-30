#include <stdio.h>
#include "ctype.h"
#include <stdlib.h>
#include <math.h>
#include <string.h>



typedef struct structWord
{
    char *word;
    unsigned int count;
}wordStruct;

typedef struct structWord wordStruct;
double currentTime();
void countSortPrintCpu(wordStruct *words, char character);
int addUnique(const char *w, wordStruct *words);
void cpuTest(FILE* fp);
int countCmp(const wordStruct *w1, const wordStruct *w2);
int wordCmp(const wordStruct *w1, const wordStruct *w2);
void distinctCount(char** inArr, uint* count,int* uniqueCount, char** words);
char** readLines(FILE* fp, int* count);
void countSortPrint(char** words, uint* count, char character, int uniqueCount);
char** fillArray(FILE* fp);
void removeSpecialCharacter(char *w, const int pos);
void cleanWords(char *word);
