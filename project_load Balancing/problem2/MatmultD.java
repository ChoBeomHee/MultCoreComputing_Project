package Task;

import java.util.*;
import java.lang.*;

// command-line execution example) java Task.MatmultD 6 < mat500.txt
// 6 means the number of threads to use
// < mat500.txt means the file that contains two matrices is given as standard input
//
// In eclipse, set the argument value and file input by using the menu [Run]->[Run Configurations]->{[Arguments], [Common->Input File]}.

// Original JAVA source code: http://stackoverflow.com/questions/21547462/how-to-multiply-2-dimensional-arrays-matrix-multiplication
public class MatmultD {
    private static int Matrix_Size;
    private static int [][]result;
    private static Scanner sc = new Scanner(System.in);
    public static void main(String [] args) throws InterruptedException {
        int thread_no=16;
        int a[][]=readMatrix();
        int b[][]=readMatrix();

        Matrix_Size = a.length;
        result = new int[Matrix_Size][Matrix_Size];

        if(thread_no > Matrix_Size)
            thread_no = Matrix_Size;

        int Block_Size = Matrix_Size / thread_no;
        Mat_Thread[] mat_threads = new Mat_Thread[thread_no];

        int st = 0;
        int en = Block_Size;
        for(int i = 0; i < thread_no; i++) {
            mat_threads[i] = new Mat_Thread(i, st, en, a, b); // Block 할당
            st += Block_Size;
            if (i == thread_no - 2)
                en = Matrix_Size;
            else
                en += Block_Size;

        }
        long startTime = System.currentTimeMillis();

        System.out.println();
        for(int i = 0; i < thread_no; i++)
            mat_threads[i].start();
        for(int i = 0; i < thread_no; i++)
            mat_threads[i].join();

        printMatrix(result);

        //System.out.printf("thread_no: %d\n" , thread_no);
        //System.out.printf("Calculation Time: %d ms\n" , endTime-startTime);
        long endTime = System.currentTimeMillis();

        System.out.printf("[thread_no]:%2d , [Time]:%4d ms\n", thread_no, endTime-startTime);
    }

    public static int[][] readMatrix() {
        int rows = sc.nextInt();
        int cols = sc.nextInt();
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = sc.nextInt();
            }
        }
        return result;
    }

    public static void printMatrix(int[][] mat) {
        System.out.println("Matrix["+mat.length+"]["+mat[0].length+"]");
        int rows = mat.length;
        int columns = mat[0].length;
        long sum = 0;
        for (int i = 0; i < Matrix_Size; i++) {
            for (int j = 0; j < Matrix_Size; j++) {
                //System.out.printf("%4d " , mat[i][j]);
                sum+=mat[i][j];
            }
            //System.out.println();
        }
        System.out.println();
        System.out.println("Matrix Sum = " + sum + "\n");
    }

    public static class Mat_Thread extends Thread{
        int index_start;
        int index_end;
        int Mat_Thread_num;
        int [][]a;
        int [][]b;

        Mat_Thread(int Mat_Thread_num, int index_start, int index_end, int[][] a, int[][] b){
            this.Mat_Thread_num = Mat_Thread_num;
            this.index_start = index_start;
            this.index_end = index_end;
            this.a = a;
            this.b = b;
        }

        public void run(){
            long startTime = System.currentTimeMillis();
            for(int i = 0; i < Matrix_Size; i++){
                for(int j = index_start; j < index_end; j++){
                    for(int k = 0; k < Matrix_Size; k++){
                        result[i][j] += a[i][k]*b[k][j];
                    }
                }
            }
            long endTime = System.currentTimeMillis();
            long timeDiff = endTime - startTime;
            System.out.println("Thread #" + Mat_Thread_num +" Program Excution Time : " + timeDiff + "ms");
        }
    }
}