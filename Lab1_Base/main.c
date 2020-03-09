/* 
 * File:   main.c
 * Author: Fedor
 */

#include <stdio.h>
#include <stdlib.h>
#include <memory.h>
#include <time.h>

#define LOWORD(T) ((unsigned short)(T))
#define HIWORD(T) ((unsigned short)(T>>16))
#define MAKELONG(a,b) ((((unsigned long)(a))<<16)+b)
#define Cmp(a,b,s) memcmp((void*)(a),(void*)(b),2*(s))

void sum(unsigned short* a, unsigned short* b, unsigned short* c, int s)
{
    unsigned short d = 0;
    unsigned long T = 0;
    int i;
    for (i = 0; i < s; i++)
    {
        T = (unsigned long)a[i] + (unsigned long)b[i] + d;
        c[i] = LOWORD(T);
        d = HIWORD(T);
    }
    c[s] = d;
}

void dif(unsigned short* a, unsigned short* b, unsigned short* c, int s)
{
    unsigned short d = 0;
    unsigned long T = 0;
    int i;
    for (i = 0; i < s; i++)
    {
        T = (unsigned long)a[i] - (unsigned long)b[i] - d;
        c[i] = LOWORD(T);
        if (HIWORD(T) == 0) 
            d = 0;
        else 
            d = 1;
    }
    c[s] = d;
}

void generate(unsigned short* a, int s)
{
    int i;
    for (i = 0; i < s; i++)
        a[i] = rand()%65536;    //2^16
}

void output(unsigned short* a, int s)
{
    int i;
    for (i = s-1; i >= 0; i--)
    {
        if (a[i] != 0)
            printf("%04x ", a[i]);
    }
    printf("\n");
}

void report1(int s)
{
    srand(time(NULL));
    unsigned short* a = (unsigned short*)calloc(s, sizeof(unsigned short));
    unsigned short* b = (unsigned short*)calloc(s, sizeof(unsigned short));
    unsigned short* c = (unsigned short*)calloc(s+1, sizeof(unsigned short));
    unsigned short* d = (unsigned short*)calloc(s+1, sizeof(unsigned short));
    int code;

    generate(a, s);
    generate(b, s);
    sum(a, b, c, s);
    dif(c, b, d, s);
    code = Cmp(a,d,s);
        
    printf("First number:\n");
    output(a, s);
    printf("Second number:\n");
    output(b, s);
    printf("Sum:\n");
    output(c, s+1);
    printf("Difference:\n");
    output(d, s+1);
    printf("Return code: %i\n\n", code);
    free(a);
    free(b);
    free(c);
    free(d);
}

void report2(int s)
{
    srand(time(NULL));
    int i;
    for (i = 1; ; i++)
    {
        unsigned short* a = (unsigned short*)calloc(s, sizeof(unsigned short));
        unsigned short* b = (unsigned short*)calloc(s, sizeof(unsigned short));
        unsigned short* c = (unsigned short*)calloc(s+1, sizeof(unsigned short));
        unsigned short* d = (unsigned short*)calloc(s+1, sizeof(unsigned short));
        int code;

        generate(a, s);
        generate(b, s);
        sum(a, b, c, s);
        dif(c, b, d, s);
        code = Cmp(a,d,s);
        if (i%1000000 == 1)
        printf("Iteration: %i\n", i);
        
        if (code != 0)
        {
            printf ("ERROR!");
            printf("First number:\n");
            output(a, s);
            printf("Second number:\n");
            output(b, s);
            printf("Sum:\n");
            output(c, s+1);
            printf("Difference:\n");
            output(d, s+1);
        }
        free(a);
        free(b);
        free(c);
        free(d);
    }
}

int main(int argc, char** argv)
{
    report1(8);
    report2(8);
    return (EXIT_SUCCESS);
}