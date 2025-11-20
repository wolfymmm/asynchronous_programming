package pr3.task1.work_stealing;

import java.util.concurrent.RecursiveAction;

public class MultiplyTask extends RecursiveAction {

    private static final int THRESHOLD = 50;

    private final int[][] A, B, result;
    private final int startRow, endRow;

    public MultiplyTask(int[][] A, int[][] B, int[][] result, int startRow, int endRow) {
        this.A = A;
        this.B = B;
        this.result = result;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    protected void compute() {
        if (endRow - startRow <= THRESHOLD) {
            multiply();
        } else {
            int mid = (startRow + endRow) / 2;
            invokeAll(
                    new MultiplyTask(A, B, result, startRow, mid),
                    new MultiplyTask(A, B, result, mid, endRow)
            );
        }
    }

    private void multiply() {
        int colsB = B[0].length;
        int colsA = A[0].length;

        for (int i = startRow; i < endRow; i++)
            for (int j = 0; j < colsB; j++) {
                int sum = 0;
                for (int k = 0; k < colsA; k++)
                    sum += A[i][k] * B[k][j];
                result[i][j] = sum;
            }
    }
}