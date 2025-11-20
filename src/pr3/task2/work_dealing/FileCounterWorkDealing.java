package pr3.task2.work_dealing;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Scanner;

public class FileCounterWorkDealing {

    public static void runInteractive() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Work Dealing File Counter ===");
        System.out.print("Введіть шлях до директорії: ");
        String directoryPath = scanner.nextLine();

        System.out.print("Введіть літеру або слово для пошуку: ");
        String searchPattern = scanner.nextLine();

        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Помилка: директорія не існує або не є директорією!");
            return;
        }

        long startTime = System.currentTimeMillis();

        ConcurrentLinkedQueue<File> directories = new ConcurrentLinkedQueue<>();
        directories.offer(directory);

        AtomicInteger totalCount = new AtomicInteger(0);
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executor.submit(new FileSearchWorker(directories, searchPattern, totalCount));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        long endTime = System.currentTimeMillis();

        System.out.println("\nРезультати пошуку (Work Dealing):");
        System.out.println("Директорія: " + directoryPath);
        System.out.println("Шаблон пошуку: '" + searchPattern + "'");
        System.out.println("Знайдено файлів: " + totalCount.get());
        System.out.println("Час виконання: " + (endTime - startTime) + " мс");
        System.out.println("Використано потоків: " + numThreads);
    }
}