#include <stdio.h>
#include "ctype.h"
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include "cFunctions.h"


char** readLines(FILE* fp, int* count) {
    char** lineArray = NULL;
    char   line[100];
    int    lineCount = 0;
    int    lineLength;

    while (fgets(line, sizeof(line), fp) != NULL) {
        lineCount++;
    }
    rewind(fp);

    lineArray = malloc(lineCount * sizeof(char *));
    if (lineArray == NULL) {
        return NULL;
    }

    for (int i = 0; i < lineCount; i++) {
        fgets(line, sizeof(line), fp);

        lineLength = strlen(line);
        line[lineLength - 1] = '\0';
        lineLength--;

        lineArray[i] = malloc(lineLength + 1);

        strcpy(lineArray[i], line);
    }
    *count = lineCount;

    return lineArray;
}


void countSortPrint(char **words, uint *count, char character, int uniqueCount)
{
    int i;
    FILE * fp;
    if(character == 'c')
    	fp = fopen("sortedOccur.txt", "w+");
    else{
    	fp = fopen("sortedWord.txt", "w+");
    }
    fputs("|-----------------------|\n",fp);
    fprintf(fp,"|%-15s |%s\n","English Word", "Count |");
    fprintf(fp,"|-----------------------|\n");

    i = uniqueCount - 1;
    while (i >= 0)
    {
        fprintf(fp,"|%-15s |  %d   |\n", words[i], count[i]);
        fprintf(fp,"|-----------------------|\n");
        i--;
    }

    fclose(fp);
}


void countSortPrintCpu(wordStruct *words, char character)
{
    int i;
    FILE * fp;
    if(character == 'c')
        fp = fopen("sortedOccurCpu.txt", "w+");
    else{
        fp = fopen("sortedWordCpu.txt", "w+");
    }
    fputs("|-----------------------|\n",fp);
    fprintf(fp,"|%-15s |%s\n","English Word", "Count |");
    fprintf(fp,"|-----------------------|\n");


    i = 0;
    while (words[i].word)
    {
        fprintf(fp,"|%-15s |  %d   |\n", words[i].word, words[i].count);
        fprintf(fp,"|-----------------------|\n");
        i++;
    }
    fclose(fp);
}




void distinctCount(char** words, uint* count, int* uniqueCount, char** uniqueWords){
    int wordcount=0;
    for(int i = 0; i<30000;i++){
        if(words[i]==NULL){
            break;
        }
        for(int j =0; j<30000;j++){
            if(uniqueWords[j]==NULL){
                uniqueCount++;
                count[i]++;
                uniqueWords[j]=words[i];
                break;
            }
            else if(strcmp(words[i],uniqueWords[j])==0){
                count[j]++;
                break;
            }
        }
    }
}

//for testing sort
char** fillArray(FILE* fp)
{
    char** lineArray = NULL;
    char   line[100];
    int    lineCount = 0;
    int    wordLength;
    int index =0;
    char word[100];


    lineArray = malloc(30000 * sizeof(char *));

    //going through the file and getting the words to add them to a list if they are unique or not
    while (fscanf(fp, "%s", word)==1)
    {
        if (strcmp(word, ",") > 0 )
        {
            cleanWords(word);
            wordLength = strlen(word);
            lineArray[index] = malloc(wordLength + 1);

            strcpy(lineArray[index], word);

            index++;

        }
    }
    return lineArray;
}

void cpuTest(FILE* fp){
    wordStruct list[30000];
    //Initializing cpu list
    for (int i = 0; i < 30000; i++)
    {
        list[i].word =NULL;
        list[i].count = 0;
    }


    int index;
    int NumOfWords=0;
    char word[100];
    //going through the file and getting the words to add them to a list if they are unique or not
    while (fscanf(fp, "%s", word)==1)
    {
        if (strcmp(word, ",") > 0 )
        {
            cleanWords(word);

            index = addUnique(word, list);
            if (index)
            {
                NumOfWords = index;
            }
        }
    }
    qsort(list, NumOfWords, sizeof(wordStruct), (int (*)(const void *, const void *))wordCmp);
    countSortPrintCpu(list, 'w');
    double then = currentTime();
    qsort(list, NumOfWords, sizeof(wordStruct), (int (*)(const void *, const void *))countCmp);
    countSortPrintCpu(list,'c');
    double now =  currentTime();
    printf( "Processing time: %f (ms) CPU sort\n", (now - then) * 1000.0);

}

int addUnique(const char *w, wordStruct *words)
{
    int i;
    i = 0;
    while (i < 30000 && words[i].word != NULL)
    {
        if (!strcmp(words[i].word, w))
        {
            words[i].count++;
            return (0);
        }
        i++;
    }
    if (i < 30000)
    {
        words[i].word = strdup(w);
        words[i].count = 1;
        return (i + 1);
    }
    fprintf(stderr, "Not enough space in the array of words\n");
    exit(1);
}



//compares counts
int countCmp(const wordStruct *w1, const wordStruct *w2)
{
    if (w1->count > w2->count)
    {
        return (-1);
    }
    if (w1->count < w2->count)
    {
        return (1);
    }
    return (0);
}

//compares words
int wordCmp(const wordStruct *w1, const wordStruct *w2)
{
    if (strcmp(w1->word,w2->word)<1)
    {
        return (-1);
    }
    if (strcmp(w1->word,w2->word)>-1)
    {
        return (1);
    }
    return (0);
}


void cleanWords(char *word)
{
    int i;

    i = 0;
    while (word[i])
    {
        word[i] = tolower(word[i]);
        if (!isalpha(word[i]))
        {
            removeSpecialCharacter(word, i);
            i--;
        }
        i++;
    }
}

void removeSpecialCharacter(char *w, const int pos)
{
    char tmp[50];

    strcpy(tmp, w);
    strcpy(w + pos, tmp + pos + 1);
}

double currentTime(){

    struct timeval now;
    gettimeofday(&now, NULL);

    return now.tv_sec + now.tv_usec/1000000.0;
}


