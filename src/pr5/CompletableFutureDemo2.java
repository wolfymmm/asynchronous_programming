package pr5;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class CompletableFutureDemo2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("=== ЗАПУСК ПРОГРАМИ ===");

        System.out.println("\n--- Завдання 1: Гран-прі Абу-Дабі 2025 ---");
        long startTask1 = System.nanoTime();

        CompletableFuture<String> racer1 = CompletableFuture.supplyAsync(() -> simulateRace("Макс Ферстаппен", 1000, 3000));
        CompletableFuture<String> racer2 = CompletableFuture.supplyAsync(() -> simulateRace("Оскар Піастрі", 1000, 3000));
        CompletableFuture<String> racer3 = CompletableFuture.supplyAsync(() -> simulateRace("Ландо Норріс", 1000, 3000));

        CompletableFuture<Object> raceWinner = CompletableFuture.anyOf(racer1, racer2, racer3);

        raceWinner.thenAccept(result -> {
            long end = System.nanoTime();
            double duration = (end - startTask1) / 1_000_000.0;
            System.out.printf("[Фінішував %s] Час: %.3f мс%n", result, duration);
            System.out.println(">>> Переможець гонки: " + result);
        }).get();

        printExecutionTime("Час гонки", startTask1);

        System.out.println("\n--- Завдання 2: Бронювання квитка ---");
        long startTask2 = System.nanoTime();

        CompletableFuture<Double> bookingProcess = CompletableFuture.supplyAsync(() -> {
                    System.out.println("[1] Перевірка наявності місць...");
                    sleep(500);
                    return true;
                })
                .thenCompose(isAvailable -> CompletableFuture.supplyAsync(() -> {
                    System.out.println("[2] Пошук найкращої ціни...");
                    double price = Math.round(ThreadLocalRandom.current().nextDouble(100, 500) * 100.0) / 100.0;
                    System.out.println("-> Ціна квитка: " + price + " USD");
                    return price;
                }))
                .thenCombine(CompletableFuture.supplyAsync(() -> {
                    System.out.println("[2b] Розрахунок збору...");
                    sleep(300);
                    return 25.50;
                }), (price, tax) -> {
                    double total = price + tax;
                    System.out.printf("-> Разом до сплати: %.2f USD%n", total);
                    return total;
                });

        CompletableFuture<Void> finalStep = bookingProcess.thenCompose(totalPrice -> CompletableFuture.supplyAsync(() -> {
            System.out.println("[3] Обробка платежу...");
            sleep(1000);
            return totalPrice;
        })).thenCompose(paidAmount -> {
            System.out.println("[4] Надсилання квитків...");

            CompletableFuture<Void> sendEmail = CompletableFuture.runAsync(() -> {
                sleep(200);
                System.out.println("   -> Email надіслано.");
            });

            CompletableFuture<Void> sendSms = CompletableFuture.runAsync(() -> {
                sleep(150);
                System.out.println("   -> SMS надіслано.");
            });

            CompletableFuture<Void> updateApp = CompletableFuture.runAsync(() -> {
                sleep(100);
                System.out.println("   -> Додаток оновлено.");
            });

            return CompletableFuture.allOf(sendEmail, sendSms, updateApp);
        });

        finalStep.thenRun(() -> {
            System.out.println("------------------------------------------------");
            System.out.println("БРОНЮВАННЯ УСПІШНЕ!");
            printExecutionTime("Весь процес бронювання", startTask2);
        }).get();

        System.out.println("\n=== ПРОГРАМА ЗАВЕРШЕНА ===");
    }

    private static String simulateRace(String name, int minTime, int maxTime) {
        int delay = ThreadLocalRandom.current().nextInt(minTime, maxTime);
        sleep(delay);
        return name;
    }

    private static void sleep(int millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void printExecutionTime(String taskName, long startTimeNano) {
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTimeNano) / 1_000_000.0;
        System.out.printf("[%s] Час: %.3f мс%n", taskName, durationMs);
    }
}