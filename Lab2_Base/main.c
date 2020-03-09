/* 
 * File:   main.c
 * Author: Fedor
 */

/********************************************************************
    Проект:     Комплект ПО для лабораторных работ по теме 
                "Реализация асимметричных криптографических алгоритмов"
    Файл:       longdiv.c
    Автор:      Грунтович М.М.
    Содержание: Функция деления длинных чисел
    Описание:   Необходимо
                - скопировать файлы longdiv.c и longdiv.h в каталог проекта, 
                - добавить файл longdiv.c в проект лабораторной работы,
                - включить файл longdiv.h инструкцией
                  #include "longdiv.h"
                  в начале модуля, использующего функцию деления длинных чисел.
    История:    1998 - базовая версия
                04.06.2005 - пересмотренная C-версия с использованием типа Digit
*********************************************************************/

#include <stdlib.h>             /* константа NULL */
#include "longdiv.h"         /* функция Div, константа MAX_DIV_OPERAND_SIZE */
#include <time.h>
#include <stdio.h> 

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
    Умножение длинного числа на цифру (*pcf,Res) = num * x
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
    for (i = s*2-1; i >= 0; i--)
    {
        if (a[i] != 0)
            printf("%04x ", a[i]);
    }
    printf("\n");
}

void report1(int s)
{
    srand(time(0));
    unsigned short* a = (unsigned short*)calloc(s, sizeof(unsigned short));
    unsigned short* b = (unsigned short*)calloc(s, sizeof(unsigned short));
    unsigned short* c = (unsigned short*)calloc(s*2, sizeof(unsigned short));
    unsigned short* d = (unsigned short*)calloc(s, sizeof(unsigned short));
    unsigned short* r = (unsigned short*)calloc(s, sizeof(unsigned short));
    unsigned short* zero = (unsigned short*)calloc(s, sizeof(unsigned short));
    
    int i;
    for (i = 0; i < s; i++)   
        zero[i] = 0;            // для сравнения остатка с нулем
    
    generate(a,s);
    generate(b,s);
    mult(a,b,c,s);
    Div (c,b,d,r,s*2,s);
    int code_1 = Cmp(a,d,s);
    int code_2 = Cmp(r,zero,s);
    
    printf("A equals:      ");
    output(a,s);  
    printf("B equals:      ");
    output(b,s);
    printf("A*B equals:    ");
    output(c,s);
    printf("C/B equals:    ");
    output(d,s);
    printf("R equals:      ");
    if(code_2 == 0) printf("0000");       // вывод 0000, если нулевой остаток
    output(r,s);
    printf("CODE_1 equals: %i", code_1);
    printf("\n");
    printf("CODE_2 equals: %i", code_2);
    printf("\n");
    free(a);
    free(b);
    free(c);
    free(d);
    free(r);
    free(zero);
}

void report2(int s)
{
    srand(time(0));
    unsigned short* a = (unsigned short*)calloc(s, sizeof(unsigned short));
    unsigned short* b = (unsigned short*)calloc(s, sizeof(unsigned short));
    unsigned short* c = (unsigned short*)calloc(s*2, sizeof(unsigned short));
    unsigned short* d = (unsigned short*)calloc(s, sizeof(unsigned short));
    unsigned short* r = (unsigned short*)calloc(s, sizeof(unsigned short));
    unsigned short* zero = (unsigned short*)calloc(s, sizeof(unsigned short));
    
    int i;
    for (i = 0; i < s; i++)   
        zero[i] = 0;            // для сравнения остатка с нулем
    for (i = 0; ; i++)
    {
        generate(a,s);
        generate(b,s);
        mult(a,b,c,s);
        Div (c,b,d,r,s*2,s);
        int code_1 = Cmp(a,d,s);
        int code_2 = Cmp(r,zero,s);
        if (i%1000000 == 1)
        printf("Iteration: %i\n", i);
        if (code_1 != 0 || code_2 != 0)
        {
            printf ("ERROR!");
            printf("A equals:      ");
            output(a,s);  
            printf("B equals:      ");
            output(b,s);
            printf("A*B equals:    ");
            output(c,s);
            printf("C/B equals:    ");
            output(d,s);
            printf("R equals:      ");
            if(code_2 == 0) printf("0000");       // вывод 0000, если нулевой остаток
            output(r,s);
        }
    }
    free(a);
    free(b);
    free(c);
    free(d);
    free(r);
    free(zero);
}

int main(int argc, char** argv) 
{
    report1(4);
    //report2(5);
    return (EXIT_SUCCESS);
}

