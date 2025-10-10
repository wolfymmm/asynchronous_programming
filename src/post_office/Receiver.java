package post_office;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

// -----------------------------
// Отримувач
// -----------------------------

public class Receiver implements Runnable {
    private final String name;
    private final BlockingQueue<MailItem> deliveriesQueue;
    private final long simulationEndTime;

    Receiver(String name, BlockingQueue<MailItem> deliveriesQueue, long simulationEndTime) {
        this.name = name;
        this.deliveriesQueue = deliveriesQueue;
        this.simulationEndTime = simulationEndTime;
    }

    @Override
    public void run() {
        try {
            while (System.currentTimeMillis() < simulationEndTime || !deliveriesQueue.isEmpty()) {
                MailItem item = deliveriesQueue.poll(1, TimeUnit.SECONDS);
                if (item != null) {
                    System.out.printf("    [Отримувач %s] <- Отримав лист від %s.%n", name, item.from);
                    Thread.sleep(200 + new Random().nextInt(300));
                } else {
                    System.out.printf("    [Отримувач %s] ... Очікує пошту.%n", name);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("    [Отримувач " + name + "] Завершив очікування (кінець симуляції).");
        }
    }
}