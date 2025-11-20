package pr3.task2.work_dealing;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class FileSearchWorker implements Runnable {
    private final ConcurrentLinkedQueue<File> directories;
    private final String searchPattern;
    private final AtomicInteger totalCount;

    public FileSearchWorker(ConcurrentLinkedQueue<File> directories, String searchPattern, AtomicInteger totalCount) {
        this.directories = directories;
        this.searchPattern = searchPattern;
        this.totalCount = totalCount;
    }

    @Override
    public void run() {
        File directory;
        while ((directory = directories.poll()) != null) {
            processDirectory(directory);
        }
    }

    private void processDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                directories.offer(file);
            } else {
                if (file.getName().toLowerCase().contains(searchPattern.toLowerCase())) {
                    totalCount.incrementAndGet();
                }
            }
        }
    }
}