package pr3.task1.work_dealing;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MatrixMultiplierWorkDealing {

    public static int[][] generateMatrix(int rows, int cols, int min, int max) {
        Random r = new Random();
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                matrix[i][j] = r.nextInt(max - min + 1) + min;
        return matrix;
    }

    public static void printMatrix(String title, int[][] matrix) {
        System.out.println(title);
        for (int[] row : matrix) {
            for (int v : row) System.out.printf("%5d", v);
            System.out.println();
        }
        System.out.println();
    }

    public static void runInteractive(int n, int m, int k, int min, int max) throws InterruptedException {
        int[][] A = generateMatrix(n, m, min, max);
        int[][] B = generateMatrix(m, k, min, max);
        int[][] result = new int[n][k];

        printMatrix("Матриця A:", A);
        printMatrix("Матриця B:", B);

        AtomicInteger nextRow = new AtomicInteger(0);
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(numThreads);

        long start = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            pool.submit(new Worker(A, B, result, nextRow, n));
        }

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.HOURS);
        long end = System.currentTimeMillis();

        printMatrix("Результат множення (Work Dealing):", result);
        System.out.println("Час виконання: " + (end - start) + " ms");
        System.out.println("Використано потоків: " + numThreads);
    }
}