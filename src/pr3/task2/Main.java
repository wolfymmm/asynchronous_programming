package pr3.task2;

import pr3.task2.work_stealing.FileCounterWorkStealing;
import pr3.task2.work_dealing.FileCounterWorkDealing;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Підрахунок файлів за шаблоном ===");
        System.out.println("Оберіть варіант виконання:");
        System.out.println("1 - Work Stealing (Fork/Join Framework)");
        System.out.println("2 - Work Dealing (Executor Service)");
        System.out.println("3 - Порівняння обох підходів");
        System.out.print("Ваш вибір: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                FileCounterWorkStealing.runInteractive();
                break;
            case 2:
                FileCounterWorkDealing.runInteractive();
                break;
            case 3:
                System.out.println("\n=== Work Stealing ===");
                FileCounterWorkStealing.runInteractive();

                System.out.println("\n=== Work Dealing ===");
                FileCounterWorkDealing.runInteractive();
                break;
            default:
                System.out.println("Некоректний вибір!");
        }

        scanner.close();
    }
}