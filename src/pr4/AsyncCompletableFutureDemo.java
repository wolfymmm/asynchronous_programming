package pr4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class AsyncCompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("=== ЗАПУСК ПРОГРАМИ ===");

        // --- Завдання 1 ---
        System.out.println("\n--- Завдання 1 ---");

        CompletableFuture<Void> task1 = CompletableFuture.supplyAsync(() -> {
            long start = System.nanoTime();
            int[][] matrix = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    matrix[i][j] = ThreadLocalRandom.current().nextInt(1, 100);
                }
            }
            printExecutionTime("Генерація матриці", start);
            return matrix;
        }).thenApplyAsync(matrix -> {
            long start = System.nanoTime();
            System.out.println("Початковий масив:");
            for (int[] row : matrix) {
                System.out.println(Arrays.toString(row));
            }
            printExecutionTime("Вивід масиву на екран", start);
            return matrix;
        }).thenAcceptAsync(matrix -> {
            long start = System.nanoTime();
            System.out.println("Вивід стовпців:");
            for (int col = 0; col < 3; col++) {
                StringBuilder sb = new StringBuilder("Стовпець " + (col + 1) + ": ");
                for (int row = 0; row < 3; row++) {
                    sb.append(matrix[row][col]).append(row < 2 ? ", " : "");
                }
                System.out.println(sb);
            }
            printExecutionTime("Вивід стовпців", start);
        }).thenRunAsync(() -> {
            long start = System.nanoTime();
            System.out.println("Усі операції з масивом завершено.");
            printExecutionTime("Фінальний метод thenRunAsync", start);
        });

        task1.get();

        // --- Завдання 2 ---
        System.out.println("\n--- Завдання 2 ---");

        long globalStartTask2 = System.nanoTime();

        CompletableFuture<Void> task2 = CompletableFuture
                .runAsync(() -> {
                    long start = System.nanoTime();
                    System.out.println("Старт обчислень послідовності...");
                    printExecutionTime("Стартове повідомлення (runAsync)", start);
                })
                .thenCompose(ignore -> CompletableFuture.supplyAsync(() -> {
                    long start = System.nanoTime();

                    List<Double> sequence = new ArrayList<>();
                    System.out.print("Згенерована послідовність: ");
                    for (int i = 0; i < 20; i++) {
                        double val = Math.round(ThreadLocalRandom.current().nextDouble(1, 50) * 100.0) / 100.0;
                        sequence.add(val);
                    }
                    System.out.println(sequence);

                    printExecutionTime("Генерація послідовності (supplyAsync)", start);
                    return sequence;
                }))
                .thenApplyAsync(sequence -> {
                    long start = System.nanoTime();

                    double minOdd = Double.POSITIVE_INFINITY;
                    double maxEven = Double.NEGATIVE_INFINITY;

                    for (int i = 0; i < sequence.size(); i++) {
                        double val = sequence.get(i);
                        if (i % 2 == 0) minOdd = Math.min(minOdd, val);
                        else maxEven = Math.max(maxEven, val);
                    }

                    System.out.println("Min (a1, a3...): " + minOdd);
                    System.out.println("Max (a2, a4...): " + maxEven);

                    printExecutionTime("Обчислення min+max (thenApplyAsync)", start);
                    return minOdd + maxEven;
                })
                .thenAcceptAsync(result -> {
                    long start = System.nanoTime();

                    System.out.println("------------------------------------------------");
                    System.out.println("Результат (min + max): " + result);

                    printExecutionTime("Виведення результату (thenAcceptAsync)", start);

                    long globalEnd = System.nanoTime();
                    double durationMs = (globalEnd - globalStartTask2) / 1_000_000.0;
                    System.out.println("Загальний час роботи усіх асинхронних операцій Завдання 2: " + durationMs + " мс");
                });

        task2.join();

        System.out.println("\n=== ПРОГРАМА ЗАВЕРШЕНА ===");
    }

    private static void printExecutionTime(String taskName, long startTimeNano) {
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTimeNano) / 1_000_000.0;
        System.out.printf("[%s] Час виконання: %.3f мс%n", taskName, durationMs);
    }
}
