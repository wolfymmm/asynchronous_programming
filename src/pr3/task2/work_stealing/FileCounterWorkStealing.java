package pr3.task2.work_stealing;

import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.Scanner;

public class FileCounterWorkStealing {

    public static void runInteractive() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Work Stealing File Counter ===");
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

        ForkJoinPool pool = ForkJoinPool.commonPool();
        FileSearchTask task = new FileSearchTask(directory, searchPattern);
        int fileCount = pool.invoke(task);

        long endTime = System.currentTimeMillis();

        System.out.println("\nРезультати пошуку (Work Stealing):");
        System.out.println("Директорія: " + directoryPath);
        System.out.println("Шаблон пошуку: '" + searchPattern + "'");
        System.out.println("Знайдено файлів: " + fileCount);
        System.out.println("Час виконання: " + (endTime - startTime) + " мс");
        System.out.println("Використано потоків: " + pool.getParallelism());
    }
}