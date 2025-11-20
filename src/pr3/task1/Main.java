package pr3.task1;

import pr3.task1.work_stealing.MatrixMultiplierWorkStealing;
import pr3.task1.work_dealing.MatrixMultiplierWorkDealing;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Множення двох матриць ===");
        System.out.print("Введіть кількість рядків першої матриці (n): ");
        int n = sc.nextInt();

        System.out.print("Введіть кількість стовпців першої матриці / рядків другої (m): ");
        int m = sc.nextInt();

        System.out.print("Введіть кількість стовпців другої матриці (k): ");
        int k = sc.nextInt();

        System.out.print("Мінімальне значення елементів: ");
        int min = sc.nextInt();

        System.out.print("Максимальне значення елементів: ");
        int max = sc.nextInt();

        System.out.println("\nОберіть варіант:");
        System.out.println("1 — Work Stealing (ForkJoinPool)");
        System.out.println("2 — Work Dealing (ThreadPool)");
        System.out.println("3 — Запустити обидва і порівняти час");
        System.out.print("Ваш вибір: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1 -> MatrixMultiplierWorkStealing.runInteractive(n, m, k, min, max);
            case 2 -> MatrixMultiplierWorkDealing.runInteractive(n, m, k, min, max);
            case 3 -> {
                System.out.println("\n=== Work Stealing ===");
                MatrixMultiplierWorkStealing.runInteractive(n, m, k, min, max);

                System.out.println("\n=== Work Dealing ===");
                MatrixMultiplierWorkDealing.runInteractive(n, m, k, min, max);
            }
            default -> System.out.println("Некоректний вибір.");
        }
    }
}