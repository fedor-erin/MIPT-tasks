/* 
 * File:   main.c
 * Author: Fedor
 */

#include <stdlib.h>             /* константа NULL */
#include "longdiv.h"         /* функция Div, константа MAX_DIV_OPERAND_SIZE */
#include <time.h>
#include <stdio.h> 
#include <memory.h>            

/*--------------------------------------------------------
    Определение новых типов
--------------------------------------------------------*/
typedef unsigned short Digit;   /* тип цифра - беззнаковое слово */
typedef unsigned long TwoDigit; /* тип двойная цифра - беззнаковое двойное слово */
typedef long LongDigit;         /* тип двойная цифра со знаком */

/*--------------------------------------------------------
    Константы
--------------------------------------------------------*/
#define MAXDIGIT 0xFFFF         /* максимальное значение цифры(все биты равны 1) */
#define L 16
#define t 16

/*--------------------------------------------------------
    Макросы
--------------------------------------------------------*/
/* выделение младшей цифпры из двойной цифры twodigit */
#define LODIGIT(twodigit) (Digit)(twodigit)
/* выделение старшшей цифпры из двойной цифры twodigit */
#define HIDIGIT(twodigit) (Digit)((twodigit)>>(sizeof(Digit)*8))
/* формирование двойной цифры из digit1,digit2, где digit2-младшая,а digit1-старшая цифры */
#define MAKE_TWO_DIGIT(digit1,digit2) (((TwoDigit)(digit1)<<(sizeof(Digit)*8))|(digit2))

#define LOWORD(T) ((unsigned short)(T))
#define HIWORD(T) ((unsigned short)(T>>16))
#define Cmp(a,b,s) memcmp((void*)(a),(void*)(b),2*(s))

/*--------------------------------------------------------
    Обнуление длинного числа num = 0
--------------------------------------------------------*/
static void Zero(
    Digit num[],                /* обнуляемое число (size цифр) */
    int size)                   /* размер числа в цифрах */
{
    int i;                      /* идексная переменная цикла */

    for(i=0;i<size;i++)         /* организация цикла по i */
        num[i]=0;               /* обнуление i-ой цифры числа num */
}

/*--------------------------------------------------------
    Присвоение одного длинного числа другому 
--------------------------------------------------------*/
static void Assign(
    Digit num1[],               /* получатель (size цифр) */
    const Digit num2[],         /* источник (size цифр) */
    int size)                   /* размер чисел в цифрах */
{
    int i;                      /* идексная переменная цикла */

    for(i=0;i<size;i++)         /* организация цикла по i */
        num1[i]=num2[i];        /* присвоение i-ой цифре числа num1 i-ой цифры числа num2 */
}

/*--------------------------------------------------------
    Умножение длинного числа на цифру (*pcf,Res) = num * x
--------------------------------------------------------*/
static void ShortMul(
    Digit Res[],                /* результат (size цифр) */
    const Digit num[],          /* первый сомножитель (size цифр) */
    Digit x,                    /* второй сомножитель (1 цифра) */
    Digit *pcf,                 /* перенос старшего разряда (1 цифра), м.б. NULL */
    int size)                   /* размер числа в цифрах */
{
    TwoDigit buf;               /* переменная для хранения промежуточного результата умножения двух слов */
    Digit cf=0;                 /* переменная для хранения промежуточного результата переноса */
    int i;                      /* идексная переменная цикла */
    
    for(i=0;i<size;i++)         /* организация цикла по i */
    {
        buf=(TwoDigit)num[i]*x+cf;/* buf - сумма реультата умножения и предыдущего переноса */
        Res[i]=LODIGIT(buf);    /* i-я цифра Res - младшая цифра buf */
        cf=HIDIGIT(buf);        /* cf - перенос (старшая цифра buf) */
   }
   if(pcf) *pcf=cf;             /* если адрес pcf не 0, возвращаем перенос */
}

/*--------------------------------------------------------
    Деление длинного числа на цифру Res = num/x, *pmod = num % x
--------------------------------------------------------*/
static void ShortDiv(
    Digit Res[],                /* частное (size цифр) */
    const Digit num[],          /* делимое  (size цифр) */
    Digit x,                    /* делитель (цифра) */
    Digit *pmod,                /* остаток (цифра), м.б. NULL */
    int size)                   /* размер длинных чисел в цифрах */
{
    TwoDigit buf=0;             /* вспомогательная переменная */
    int i;                      /* идексная переменная цикла */

    if(!x) return;              /* если x равен 0 то бесславно завершаем деление */
    for(i=size-1;i>=0;i--)      /* организация цикла по i */
    {
        buf<<=sizeof(Digit)*8;  /* старшая цифра buf - предыдущий остаток */
        buf|=num[i];            /* младшая цифра buf - i-я цифра числа num */
        Res[i]=LODIGIT(buf/x);  /* i-я цифра Res - результат деления */
        buf%=x;                 /* младшая цифра buf - остаток */
    }
    if(pmod) *pmod=LODIGIT(buf);/* если адрес pmod не 0, возвращаем остаток */
}

/*--------------------------------------------------------
    Сложение длинных чисел (*pcf,Res) = first + second
--------------------------------------------------------*/
static void Add(
    Digit Res[],                /* сумма (size цифр) */
    const Digit first[],        /* первое слагаемое (size цифр) */
    const Digit second[],       /* второе слагаемое (size цифр) */
    Digit *pcf,                 /* флаг переноса (1 цифра), м.б. NULL */
    int size)                   /* размер чисел в цифрах */
 {
    TwoDigit buf;               /* для хранения промежуточного результата сложения */
    Digit cf=0;                 /* для хранения промежуточного переноса */
    int i;                      /* идексная переменная цикла */

    for(i=0;i<size;i++)         /* организация цикла по i */
    {
        buf=(TwoDigit)first[i]+second[i]+cf;/* сложение i-х цифр и предыдущего переноса */
        Res[i]=LODIGIT(buf);    /* i-я цифра Res - младшая цифра суммы */
        cf=HIDIGIT(buf);        /* перенос - старшая цифра суммы */
    }
    if(pcf) *pcf=cf;            /* если адрес pcf не 0, возвращаем перенос */
}

/*--------------------------------------------------------
    Вычитание длинных чисел (*pcf,Res) = first - second
--------------------------------------------------------*/
static void Sub(
    Digit Res[],                /* разность (size цифр) */
    const Digit first[],        /* уменьшаемое (size цифр) */
    const Digit second[],       /* вычитаемое (size цифр) */
    Digit *pcf,                 /* флаг заема разрядов (1 цифра), м.б. NULL */
    int size)                   /* размер чисел в цифрах */
{
    LongDigit buf;              /* знаковая переменная для выделения заема разрядов */
    Digit cf=0;                 /* для хранения промежуточного заема */
    int i;                      /* идексная переменная цикла */

    for(i=0;i<size;i++)         /* организация цикла по i */
    {
        buf=(LongDigit)first[i]-second[i]-cf;/* вычитание i-х цифр с учетом предыдущего заема */
        Res[i]=LODIGIT(buf);    /* i-я цифра Res - младшая цифра разности buf */
        cf=HIDIGIT(buf);        /* заем - старшая цифра разности buf */
        if(cf) cf=1;            /* если заем был, флагу заема присваеваем 1 */
    }
    if(pcf) *pcf=cf;            /* если адрес pcf не 0, возвращаем заем */
}

/*--------------------------------------------------------
    Деление длинных чисел Q = U/V, R = U % V
    Исключение деления на 0 не обрабатывается
--------------------------------------------------------*/
void Div(
    const Digit U[],            /* делимое (sizeU цифр) */
    const Digit V[],            /* делитель (sizeV цифр) */
    Digit Q[],                  /* частное (sizeU цифр), м.б. NULL */
    Digit R[],                  /* остаток (sizeV цифр), м.б. NULL */
    int sizeU,                  /* размер чисел U и Q в цифрах */
    int sizeV)                  /* размер чисел V и R в цифрах */
{
    Digit q, buf1, buf2;                /* для промежуточного хранения */
    Digit U2[MAX_DIV_OPERAND_SIZE+1],   /* для нормализованного U */
          V2[MAX_DIV_OPERAND_SIZE+1];   /* для нормализованного V */
    TwoDigit inter;                     /* для промежуточных операций */
    int i,j,k;                          /* индексные переменные */
    Digit d;                            /* нормализующий множитель */

/*--- Проверки, подготовка: */
    if(R) Zero(R,sizeV);                /* если адрес остатка R не 0, обнуляем остаток */
    if(Q) Zero(Q,sizeU);                /* если адрес частного Q не 0, обнуляем частное */

    for(i=sizeV-1;(i>=0)&(!V[i]);i--);  /* анализ делителя, отсекаем старшие незначащие нули */
    sizeV=i+1;                          /* новый размер делителя */
    if(!sizeV) return;                  /* исключение "Деление на ноль" (просто уходим) */

    for(k=sizeU-1;(k>=0)&(!U[k]);k--);  /* анализ делимого, отсекаем старшие незначащие нули */
    sizeU=k+1;                          /* новый размер делимого */

    if(sizeV>sizeU)                     /* если делитель больше делимого, то */
    {
        if(R) Assign(R,U,sizeU);        /* остаток равен делимому */
        return ;                        /* уходим */
    }
    else if(sizeV==1)                   /* если делитель - 1 цифра, то */
    {
        ShortDiv(Q,U,V[0],R,sizeU);     /* применяем упрощенный алгоритм */
        return ;                        /* уходим */
    }

/*--- Нормализация: */
    d=(Digit)(((TwoDigit)MAXDIGIT+1)/((TwoDigit)V[sizeV-1]+1)); /* нормализующий множитель */
    if(d!=1)                            /* если d не 1, */
    {
        ShortMul(V2,V,d,&buf1,sizeV);   /* умножаем V на d */
        V2[sizeV]=buf1;
        ShortMul(U2,U,d,&buf1,sizeU);   /* умножаем U на d */
        U2[sizeU]=buf1;
    }
    else
    {                                   /* если d == 1, */
        Assign(V2,V,sizeV);             /* V2 = V */
        V2[sizeV]=0;
        Assign(U2,U,sizeU);             /* U2 = U */
        U2[sizeU]=0;
    }

/*--- Основной цикл */
    for(j=sizeU;j>=sizeV;j--)           /* организация главного цикла по j (sizeU-sizeV раз) */
    {
/*--- Очередная цифра частного */
        inter=MAKE_TWO_DIGIT(U2[j],U2[j-1]); /* пригодится */
        if(U2[j]==V2[sizeV-1])          /* если старшие цифры равны, */
            q=MAXDIGIT;                 /* цифра частного q = MAXDIGIT */
		else    {                        /* иначе */
            q=(Digit)(inter/V2[sizeV-1]);/* j-ю цифру частного q находим делением */
                                        /* если q великоват, */

/*--- Коррекция цифры частного */
        if(((TwoDigit)V2[sizeV-2]*q)>(MAKE_TWO_DIGIT((Digit)(inter%V2[sizeV-1]),U2[j-2])))
        {q--;}   
		}/* коррекция цифры частного уменьшением q на 1 */
		
/*--- Вычитание кратного делителя */
        ShortMul(R,V2,q,&buf1,sizeV);   /* умножаем V на q */
        Sub(U2+j-sizeV,U2+j-sizeV,R,&buf2,sizeV);/* вычитаем результат умножения */
        inter=(LongDigit)U2[j]-buf1-buf2;
        U2[j]=LODIGIT(inter); 

/*--- Коррекция остатка и частного */
        if(HIDIGIT(inter))              /* если результат шага отрицательный, */
        {                               /* компенсирующее сложение */
            Add(U2+j-sizeV,U2+j-sizeV,V2,&buf2,sizeV);
            U2[j]+=buf2;
            q--;                        /* коррекция цифры частного q на 1 */
        }
        if(Q)                           /* если адрес частного Q не 0, */
            Q[j-sizeV]=q;               /* запоминаем j-ю цифру частного q */
    }

/*--- Завершение */
    if(R)                               /* если адрес остатка R не 0, */
    {
        ShortDiv(R,U2,d,NULL,sizeV);    /* денормализация остатка R */
    }
}

// Доп функции

void mult(unsigned short* a, unsigned short* b, unsigned short* c, int s)
{
    unsigned short d;
    unsigned long T;
    int i, j;
    
    for(i = 0; i < s*2; i++)
        c[i] = 0;
    for(i = 0; i < s; i++)
    {
        d = 0;
        for(j = 0; j < s; j++)
        {
            T = (unsigned long) c[i+j] + (unsigned long) a[i] * (unsigned long) b[j] + d;
            c[i+j] = LOWORD(T);
            d = HIWORD (T);
        }
        c[i+j] = d;
    }
}

void generate(unsigned short* a, int s)
{
    int i;
    for (i = 0; i < s; i++)
        a[i] = 0x1111 + rand()%(0xffff - 0x1111);    
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

void multMod(unsigned short* a, unsigned short* b, unsigned short* n, unsigned short* c, int s)
{
    unsigned short e[L*2];
    unsigned short trash[L*2];
    mult(a,b,e,s);
    Div(e,n,trash,c,s*2,s);
}


void binAlg(unsigned short* a, unsigned short* b, unsigned short* n, unsigned short* c, int s1, int s2)
{
    int i;
    c[0] = 1;
    
    for(i = 1; i < s1; i++)
        c[i] = 0;

    for(i = 16*s2-1; i >= 0; i--)
    {
        multMod(c,c,n,c,s1);
        if((b[i/16]>>(i%16))&1)
            multMod(c,a,n,c,s1);
    }
}

int htd(char c[])
{
    int i, code;
    int q = 1, res = 0;

    for(i = 3; i >= 0; i--)
    {
        code = (int)c[i];
        if(code < 60)
        {
            res = res + (code-48)*q; //цифры
        } 
        else
        {
            res = res + (code-87)*q;//буквы
        }
        q = q*16;//степень
    }
    return res;
}

void report1()
{
    srand(time(0));
    unsigned short a[L];
    unsigned short c[L];
    //Из приложения B
    //n = d9ae 128b 74cd 0aa3 678f 8a17 3d6d 4387 1b7b dae5 9c72 1158 ba83 d640 ffcb 6d13
    unsigned short n[L]={0x6d13, 0xffcb, 0xd640, 0xba83, 0x1158, 0x9c72, 0xdae5, 0x1b7b, 0x4387, 0x3d6d, 0x8a17, 0x678f, 0x0aa3, 0x74cd, 0x128b, 0xd9ae};
    //Вариант 3
    //e = d645 7f94 27cd 2de9 83d0 2dbb 5179 e326 063a 1178 e6df baf1 2fbf 14e5 89e2 a6e5
    //d = 1c20 0cec 28ef e524 48dc 64e9 5b30 44f1 0cc8 4831 5e78 25f9 e4e5 ef5d 7af9 08ad
    unsigned short e[L] = {0xa6e5, 0x89e2, 0x14e5, 0x2fbf, 0xbaf1, 0xe6df, 0x1178, 0x063a, 0xe326, 0x5179, 0x2dbb, 0x83d0, 0x2de9, 0x27cd, 0x7f94, 0xd645};
    unsigned short d[L] = {0x08ad, 0x7af9, 0xef5d, 0xe4e5, 0x25f9, 0x5e78, 0x4831, 0x0cc8, 0x44f1, 0x5b30, 0x64e9, 0x48dc, 0xe524, 0x28ef, 0x0cec, 0x1c20};

    int i, s;
    int j = 0, buf = 1;
    FILE *READ, *WRITE;
    char readFile[L], writeFile[L];
    
    printf("Enter file name for encryption:\n");
    gets(readFile);

    printf("Enter file name for writing:\n");
    gets(writeFile);

    READ = fopen(readFile, "r");
    if(READ == NULL)
    {
        printf("File doesn't exist '%s'\n", readFile);
    } 
    else
    {
        fseek(READ, 0, SEEK_END);
        s = ftell(READ);
        fclose(READ);
        READ = fopen(readFile, "r");
        WRITE = fopen(writeFile, "ab");
        printf("Encryption is in process...\n");
        while(j < s)
        {
            for(i = 0; i < L; i++)
                a[i]=0;

            i = 0;
            while(i < L && j < s)
            {
                a[i] = (unsigned short)fgetc(READ);
                i++;
                j++;
            }

            binAlg(a,e,n,c,L,t);
            
            for(i = 0; i < L; i++)
                fprintf(WRITE, "%04x", c[i]);

            printf("Buffer number: %i\n", buf);
            buf++;
        }

        printf("Encryption is completed.\n");
        fclose(READ);
        fclose(WRITE);
    }
}

void report2()
{
    srand(time(0));
    unsigned short a[L];
    unsigned short c[L];
    //Из приложения B
    //n = d9ae 128b 74cd 0aa3 678f 8a17 3d6d 4387 1b7b dae5 9c72 1158 ba83 d640 ffcb 6d13
    unsigned short n[L]={0x6d13, 0xffcb, 0xd640, 0xba83, 0x1158, 0x9c72, 0xdae5, 0x1b7b, 0x4387, 0x3d6d, 0x8a17, 0x678f, 0x0aa3, 0x74cd, 0x128b, 0xd9ae};
    //Вариант 3
    //e = d645 7f94 27cd 2de9 83d0 2dbb 5179 e326 063a 1178 e6df baf1 2fbf 14e5 89e2 a6e5
    //d = 1c20 0cec 28ef e524 48dc 64e9 5b30 44f1 0cc8 4831 5e78 25f9 e4e5 ef5d 7af9 08ad
    unsigned short e[L] = {0xa6e5, 0x89e2, 0x14e5, 0x2fbf, 0xbaf1, 0xe6df, 0x1178, 0x063a, 0xe326, 0x5179, 0x2dbb, 0x83d0, 0x2de9, 0x27cd, 0x7f94, 0xd645};
    unsigned short d[L] = {0x08ad, 0x7af9, 0xef5d, 0xe4e5, 0x25f9, 0x5e78, 0x4831, 0x0cc8, 0x44f1, 0x5b30, 0x64e9, 0x48dc, 0xe524, 0x28ef, 0x0cec, 0x1c20};

    int i, k, s;
    int j = 0, buf = 1;
    char H[4];
    FILE *READ, *WRITE;
    char readFile[L], writeFile[L];
    
    printf("Enter file name for decryption:\n");
    gets(readFile);

    printf("Enter file name for writing:\n");
    gets(writeFile);

    READ = fopen(readFile, "r");
    if(READ == NULL)
    {
        printf("File doesn't exist '%s'\n", readFile);
    } 
    else
    {
        fseek(READ, 0, SEEK_END);
        s = ftell(READ);
        fclose(READ);
        READ = fopen(readFile, "r");
        WRITE = fopen(writeFile, "ab");
        printf("Decryption is in process...\n");
        while(j < s)
        {
            for(i = 0; i < L; i++)
                a[i]=0;

            i = 0;
            while(i < L && j < s)
            {
                for(k = 0; k < 4; k++)
                {
                    H[k] = fgetc(READ);
                    j++;
                }
                a[i] = htd(H);
                i++;
            }

            binAlg(a,d,n,c,L,t);
            
            for(i = 0; i < L; i++)
            {
                if(c[i] != 0)
                    fputc(c[i], WRITE);
            }

            printf("Buffer number: %i\n", buf);
            buf++;
        }

        printf("Decryption is completed.\n");
        fclose(READ);
        fclose(WRITE);
    }
}

int main(int argc, char** argv) 
{
    report1();
    //report2();
    return (EXIT_SUCCESS);
}