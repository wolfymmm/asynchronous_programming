package post_office;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

// -----------------------------
// Відправник
// -----------------------------

public class Sender implements Runnable {
    private final String name;
    private final BlockingQueue<MailItem> dropBox;
    private final List<String> receivers;
    private final int maxMessages;
    private final Random rng = new Random();

    Sender(String name, BlockingQueue<MailItem> dropBox, List<String> receivers, int maxMessages) {
        this.name = name;
        this.dropBox = dropBox;
        this.receivers = receivers;
        this.maxMessages = maxMessages;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < maxMessages; i++) {
                String to = receivers.get(rng.nextInt(receivers.size()));
                MailItem item = new MailItem(name, to);
                Thread.sleep(300 + rng.nextInt(400));

                if (dropBox.offer(item, 1, TimeUnit.SECONDS)) {
                    System.out.printf("[Відправник %s] -> Лист для %s покладено у скриньку.%n", name, to);
                } else {
                    System.out.printf("[Відправник %s] -> Не вдалося покласти лист (скринька переповнена).%n", name);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("[Відправник " + name + "] -> Завершив відправку.");
        }
    }
}