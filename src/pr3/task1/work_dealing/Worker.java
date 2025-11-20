package pr3.task1.work_dealing;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker implements Runnable {
    private final int[][] A, B, result;
    private final AtomicInteger nextRow;
    private final int totalRows;

    public Worker(int[][] A, int[][] B, int[][] result, AtomicInteger nextRow, int totalRows) {
        this.A = A;
        this.B = B;
        this.result = result;
        this.nextRow = nextRow;
        this.totalRows = totalRows;
    }

    @Override
    public void run() {
        int colsB = B[0].length;
        int colsA = A[0].length;

        int row;
        while ((row = nextRow.getAndIncrement()) < totalRows) {
            for (int j = 0; j < colsB; j++) {
                int sum = 0;
                for (int k = 0; k < colsA; k++)
                    sum += A[row][k] * B[k][j];
                result[row][j] = sum;
            }
        }
    }
}