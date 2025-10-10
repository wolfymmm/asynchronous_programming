package post_office;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

// -----------------------------
// Працівник пошти
// -----------------------------

public class PostWorker implements Runnable {
    private final String name;
    private final BlockingQueue<MailItem> dropBox;
    private final Map<String, BlockingQueue<MailItem>> receiverQueues;
    private final long simulationEndTime;

    private final int openDurationMs = 4000;
    private final int closedDurationMs = 2000;

    PostWorker(String name,
               BlockingQueue<MailItem> dropBox,
               Map<String, BlockingQueue<MailItem>> receiverQueues,
               long simulationEndTime) {
        this.name = name;
        this.dropBox = dropBox;
        this.receiverQueues = receiverQueues;
        this.simulationEndTime = simulationEndTime;
    }

    @Override
    public void run() {
        System.out.println("\n-------------------------------------------------------------");
        System.out.println("[Працівник " + name + "] -> Готується до роботи...");
        System.out.println("-------------------------------------------------------------\n");

        try {
            boolean open = false;

            while (System.currentTimeMillis() < simulationEndTime) {
                open = !open;

                if (open) {
                    System.out.println("\n=============================================================");
                    System.out.println("                       ПОШТУ ВІДЧИНЕНО                         ");
                    System.out.println("=============================================================\n");

                    long workUntil = System.currentTimeMillis() + openDurationMs;
                    while (System.currentTimeMillis() < workUntil && System.currentTimeMillis() < simulationEndTime) {
                        MailItem item = dropBox.poll(1, TimeUnit.SECONDS);
                        if (item != null) {
                            BlockingQueue<MailItem> destQueue = receiverQueues.get(item.to);
                            if (destQueue != null) {
                                System.out.printf("[Працівник %s] Доставляє лист від %s до %s.%n",
                                        name, item.from, item.to);
                                destQueue.offer(item, 1, TimeUnit.SECONDS);
                            }
                        } else {
                            System.out.println("[Працівник " + name + "] Перевіряє скриньку — порожньо.");
                        }
                        Thread.sleep(300);
                    }

                    System.out.println("\n=============================================================");
                    System.out.println("                        ПОШТУ ЗАЧИНЕНО                         ");
                    System.out.println("=============================================================\n");

                    Thread.sleep(closedDurationMs);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("[Працівник " + name + "] Завершив роботу (кінець симуляції).");
        }
    }
}