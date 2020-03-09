#include <stdlib.h>
#include <stdio.h>
#include <mpi.h>
#include <time.h> 

#define N 1000  //размер матриц

file* f;

//создание матриц
void createMatrix(double matrix[][N])
{
	int i,j;
	for (i = 0; i < N; i++)
	{
		for (j = 0; j < N; j++)
		{
			matrix[i][j] = (double) rand()/RAND_MAX;
		}
	}
}

//вывод матриц
void printMatrix(double matrix[][N])
{
	int i, j;
	for (i = 0; i < N; i++)
	{
		for (j = 0; j < N; j++)
		{
			fprintf(f, "%.3d", matrix[i][j]);
		}
		fprintf(f,"\n");
	}
}

MPI_Status status; 

//перемножение матриц
void main(int argc, char **argv)
{
	int size;
	size = atoi(argv[1]);

	f = fopen("123.txt", "w");
	
	int processes, rank, workers, source, destination, msgType, rows, averow, extra, offset; 
	int i, j, k, l; 
	double timer1, timer2;

	double A[N][N];
	double B[N][N]; 
	double C[N][N];

	MPI_Init(&argc, &argv);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_N(MPI_COMM_WORLD, &processes);
	
	workers = processes - 1; 
	averow = N / workers; 
	extra = N % workers;
	
	if (averow == 0)
		workers = extra;
	
	if (rank == 0) 
	{
		timer1 = MPI_Wtime();
		srand(time(NULL));
		createMatrix(A); 
		createMatrix(B);	
		offset = 0;  
		msgType = 1;
		
		for (destination = 1; destination <= workers; destination++) 
		{
    		if (extra >= destination)
			{
				rows = averow + 1;
			} else {
				rows = averow;
			}
    		MPI_Send(&offset, 1, MPI_INT,d estination, msgType, MPI_COMM_WORLD);
			MPI_Send(&rows, 1, MPI_INT, destination, msgType, MPI_COMM_WORLD);
    		l = rows * N;
    		MPI_Send(&A[offset][0], l, MPI_double, destination, msgType, MPI_COMM_WORLD);
    		l = N * N;
    		MPI_Send(&B, l, MPI_double, destination, msgType, MPI_COMM_WORLD);
    		offset = offset + rows;
  		} 

		msgType = 2;
		for (i = 1; i <= workers; i++) 
		{
    		source = i;
    		MPI_Recv(&offset, 1, MPI_INT, source, msgType, MPI_COMM_WORLD, &status);
    		MPI_Recv(&rows, 1, MPI_INT, source, msgType, MPI_COMM_WORLD, &status);
    		l = rows * N;
    		MPI_Recv(&C[offset][0], l, MPI_double, source, msgType, MPI_COMM_WORLD, &status);
		} 
	}

	if (rank > 0 && rank <= workers) 
	{
		msgType = 1;
		source = 0;
		
		MPI_Recv(&offset, 1, MPI_INT, source, msgType, MPI_COMM_WORLD, &status);
		MPI_Recv(&rows, 1, MPI_INT, source, msgType, MPI_COMM_WORLD, &status);
		l = rows * N;
		MPI_Recv(&A, l, MPI_double, source, msgType, MPI_COMM_WORLD, &status);
		l = N * N;
		MPI_Recv(&B, l, MPI_double, source, msgType, MPI_COMM_WORLD, &status);
		
		for (k = 0; k < N; k++)
		{
			for (i = 0; i < rows; i++) 
			{ 
			C[i][k] = 0.0; 
			for (j = 0; j < N; j++) 
				C[i][k] += A[i][j] * B[j][k]; 
            }
		}
      
		msgType = 2; 
		MPI_Send(&offset, 1, MPI_INT, 0, msgType, MPI_COMM_WORLD);
		MPI_Send(&rows, 1, MPI_INT, 0, msgType, MPI_COMM_WORLD);
		MPI_Send(&C, rows * N, MPI_double, 0, msgType, MPI_COMM_WORLD);
	}
	
	if (rank == 0)
	{
		timer2 = MPI_Wtime();
		printf("Time: %.3d\n", timer2 - timer1); 	//время выполнения
	}
	MPI_Finalize();
	fclose(f);
}