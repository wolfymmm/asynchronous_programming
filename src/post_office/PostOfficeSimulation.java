package post_office;

import java.util.*;
import java.util.concurrent.*;

// -----------------------------
// Головний клас
// -----------------------------

public class PostOfficeSimulation {
    static void main() {
        int SIMULATION_MS = 20000;

        System.out.println("=============================================================");
        System.out.println("                      СИМУЛЯЦІЯ РОБОТИ ПОШТИ                 ");
        System.out.println("=============================================================\n");

        BlockingQueue<MailItem> dropBox = new LinkedBlockingQueue<>(50);
        Map<String, BlockingQueue<MailItem>> receiverQueues = new HashMap<>();

        List<String> senderNames = Arrays.asList("Оксана", "Андрій", "Марія");
        List<String> receiverNames = Arrays.asList("Ірина", "Петро", "Світлана");

        for (String name : receiverNames) {
            receiverQueues.put(name, new LinkedBlockingQueue<>(20));
        }

        long simulationEnd = System.currentTimeMillis() + SIMULATION_MS;
        List<Thread> allThreads = new ArrayList<>();

        // Отримувачі
        for (String r : receiverNames) {
            Thread t = new Thread(new Receiver(r, receiverQueues.get(r), simulationEnd), r);
            allThreads.add(t);
            t.start();
        }

        // Відправники
        for (String s : senderNames) {
            Thread t = new Thread(new Sender(s, dropBox, receiverNames, 5), s);
            allThreads.add(t);
            t.start();
        }

        // Працівник пошти
        Thread worker = new Thread(new PostWorker("Максим", dropBox, receiverQueues, simulationEnd), "Worker");
        allThreads.add(worker);
        worker.start();

        try {
            Thread.sleep(SIMULATION_MS + 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n=============================================================");
        System.out.println("                      СИМУЛЯЦІЮ ЗАВЕРШЕНО                      ");
        System.out.println("=============================================================");
    }
}