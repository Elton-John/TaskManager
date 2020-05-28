package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.validator.GenericValidator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;


public class TaskManager {

    public static void main(String[] args) {
        start();

        String[][] taskTabFromFile = getStringMultiArrayFromFile();

        showOptions();

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        while (true) {
            switch (input) {
                case "add":
                    taskTabFromFile = add(scanner, taskTabFromFile);
                    showOptions();
                    input = scanner.nextLine();
                    break;
                case "remove":
                    taskTabFromFile = removeTask(scanner, taskTabFromFile);
                    showOptions();
                    input = scanner.nextLine();
                    break;
                case "list":
                    list(taskTabFromFile);
                    showOptions();
                    input = scanner.nextLine();
                    break;
                case "exit":
                    try {
                        writeFromTaskTabToFile(taskTabFromFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    System.out.println(ConsoleColors.CYAN_BOLD + "All changes saved");
                    System.out.println(ConsoleColors.RED_BOLD + "Bye bye");
                    System.exit(0);
                    break;
                default:
                    System.out.println(ConsoleColors.RED_BOLD + "Can't understand you. Try again.");
                    input = scanner.nextLine();

            }
        }
    }

    private static void start() {
        Path path = Paths.get("task.csv");
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeFromTaskTabToFile(String[][] taskTabFromFile) throws FileNotFoundException {
        try (PrintWriter printWriter = new PrintWriter("task.csv")) {
            for (String[] strings : taskTabFromFile) {
                StringBuilder builder = new StringBuilder(strings[0]);
                for (int j = 1; j < strings.length; j++) {
                    builder.append(",").append(strings[j]);
                }
                printWriter.println(builder);
            }
        }
    }


    private static void list(String[][] taskTabFromFile) {
        System.out.println(ConsoleColors.CYAN_BOLD + "Here are your tasks:");
        for (int i = 0; i < taskTabFromFile.length; i++) {
            System.out.print(i + "  ");
            for (int j = 0; j < taskTabFromFile[i].length; j++) {
                System.out.print(taskTabFromFile[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static String[][] removeTask(Scanner scanner, String[][] taskTabFromFile) {
        System.out.println(ConsoleColors.GREEN_BOLD + "Please select number of task to remove: ");

        int numberToRemove = getCorrectNumberFromInput(scanner);

        while (numberToRemove < 0 || numberToRemove > taskTabFromFile.length) {
            System.out.println(ConsoleColors.RED_BOLD + "You don't have task number " + numberToRemove + ". Try again.");
            numberToRemove = getCorrectNumberFromInput(scanner);
        }

        taskTabFromFile = ArrayUtils.remove(taskTabFromFile, numberToRemove);
        System.out.println(ConsoleColors.CYAN_BOLD + "Task " + numberToRemove + " successfully deleted.");

        return taskTabFromFile;
    }

    public static int getCorrectNumberFromInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print(ConsoleColors.RED_BOLD + "It's not a number. Give me a number:");
        }
        return scanner.nextInt();
    }

    private static String[][] add(Scanner scanner, String[][] taskTabFromFile) {
        StringBuilder newTask = new StringBuilder();
        System.out.println(ConsoleColors.GREEN_BOLD + "Please add task description: ");
        newTask.append(scanner.nextLine()).append(",");

        System.out.println(ConsoleColors.GREEN_BOLD + "Please add task due data:");
        String data;
        while (true) {
            data = scanner.nextLine();
            if (GenericValidator.isDate(data, "dd-MM-yyyy", true)) {
                break;
            } else {
                System.out.println("Please use this format DD-MM-YYYY");
            }
        }
        newTask.append(data).append(",");

        System.out.println(ConsoleColors.GREEN_BOLD + "Is your task important? (true or false): ");
        String importance = scanner.nextLine();
        while (!importance.equals("true") && !importance.equals("false")) {
            System.out.println("True or false?");
            importance = scanner.nextLine();
        }
        newTask.append(importance);


        System.out.println(ConsoleColors.CYAN_BOLD + "Your task added. \n It looks like: " + newTask);

        taskTabFromFile = Arrays.copyOf(taskTabFromFile, taskTabFromFile.length + 1);
        String newTaskForAdd = String.valueOf(newTask);
        taskTabFromFile[taskTabFromFile.length - 1] = newTaskForAdd.split(",");


        return taskTabFromFile;
    }

    private static void showOptions() {

        System.out.println(ConsoleColors.GREEN_BOLD + "Please select an option:");
        String[] options = {"add", "remove", "list", "exit"};
        for (String everyOption :
                options) {
            System.out.println(ConsoleColors.BLACK + everyOption);
        }
    }

    private static String[][] getStringMultiArrayFromFile() {
        Path taskFile = Paths.get("task.csv");

        String[][] taskTabFromFile = new String[0][];
        int i = 0;

        try {
            for (String line :
                    Files.readAllLines(taskFile)) {
                taskTabFromFile = Arrays.copyOf(taskTabFromFile, taskTabFromFile.length + 1);
                taskTabFromFile[i] = line.split(",");
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return taskTabFromFile;
    }
}
