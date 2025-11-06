package n_numbers;

import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        System.out.print("Введіть мінімальне значення діапазону: ");
        int min = sc.nextInt();
        System.out.print("Введіть максимальне значення діапазону: ");
        int max = sc.nextInt();

        if (min >= max) {
            System.out.println("Помилка: мінімальне значення має бути менше максимального!");
            return;
        }

        int size = 40 + rand.nextInt(21);
        CopyOnWriteArrayList<Integer> numbers = new CopyOnWriteArrayList<>();

        for (int i = 0; i < size; i++) {
            numbers.add(rand.nextInt(max - min + 1) + min);
        }

        System.out.println("\nЗгенерований масив:");
        System.out.println(numbers);

        int threads = 4;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        List<Future<Integer>> futures = new ArrayList<>();
        int partSize = (int) Math.ceil((double) numbers.size() / threads);

        for (int i = 0; i < threads; i++) {
            int start = i * partSize;
            int end = Math.min(start + partSize, numbers.size());
            List<Integer> sublist = numbers.subList(start, end);
            futures.add(executor.submit(new ArrayProcessor(sublist)));
        }

        int totalSum = 0;
        for (Future<Integer> future : futures) {
            try {
                while (!future.isDone() && !future.isCancelled()) {
                    Thread.sleep(5);
                }
                if (future.isCancelled()) {
                    System.out.println("Одне із завдань було скасовано.");
                    continue;
                }
                totalSum += future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\nСума всіх елементів масиву: " + totalSum);

        int N;
        while (true) {
            System.out.print("\nВведіть число N (яке має бути у масиві): ");
            N = sc.nextInt();
            if (numbers.contains(N)) break;
            System.out.println("Помилка: число " + N + " відсутнє у масиві! Спробуйте ще раз.");
        }

        int rangePerThread = N / threads;
        List<Future<List<Integer>>> primeFutures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            int start = i * rangePerThread + 1;
            int end = (i == threads - 1) ? N : (i + 1) * rangePerThread;
            primeFutures.add(executor.submit(new PrimeFinder(start, end)));
        }

        CopyOnWriteArrayList<Integer> allPrimes = new CopyOnWriteArrayList<>();

        for (Future<List<Integer>> future : primeFutures) {
            try {
                while (!future.isDone() && !future.isCancelled()) {
                    Thread.sleep(5);
                }
                if (future.isCancelled()) {
                    System.out.println("Пошук простих чисел у потоці було скасовано.");
                    continue;
                }
                allPrimes.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

        System.out.println("\nПрості числа до " + N + ": " + allPrimes);
        long endTime = System.currentTimeMillis();
        System.out.println("\nЧас роботи програми: " + (endTime - startTime) + " мс");
    }
}
