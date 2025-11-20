package pr3.task2.work_stealing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FileSearchTask extends RecursiveTask<Integer> {
    private final File directory;
    private final String searchPattern;
    private static final int THRESHOLD = 100;

    public FileSearchTask(File directory, String searchPattern) {
        this.directory = directory;
        this.searchPattern = searchPattern;
    }

    @Override
    protected Integer compute() {
        int count = 0;
        List<FileSearchTask> subtasks = new ArrayList<>();

        File[] files = directory.listFiles();
        if (files == null) return 0;

        for (File file : files) {
            if (file.isDirectory()) {
                FileSearchTask subtask = new FileSearchTask(file, searchPattern);
                subtask.fork();
                subtasks.add(subtask);
            } else {
                if (file.getName().toLowerCase().contains(searchPattern.toLowerCase())) {
                    count++;
                }
            }
        }

        for (FileSearchTask subtask : subtasks) {
            count += subtask.join();
        }

        return count;
    }
}