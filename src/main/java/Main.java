import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        do {
            System.out.print("$ ");
            String command = sc.next();
            String arguments = sc.nextLine().trim();

            if (isExecutable(command)) {
                execute(command, arguments);
                continue;
            }

            switch (command) {
                case "exit" -> exit();
                case "echo" -> echo(arguments);
                case "type" -> type(arguments);
                default -> System.out.println(command + ": command not found");
            }

        } while (true);
    }

    private static void execute(String command, String arguments) throws IOException {
        String[] args = (command + " " + arguments).split(" ");
        Process process = new ProcessBuilder(args)
                .start();

        try (BufferedReader reader = process.inputReader()) {
            reader.lines().forEach(System.out::println);
        }
    }

    private static void echo(String arguments) {
        System.out.println(arguments);
    }

    private static void exit() {
        System.exit(0);
    }

    private static void type(String arguments) {
        if (isBuiltin(arguments)) {
            System.out.println(arguments + " is a shell builtin");
        } else if (isExecutable(arguments)) {
            System.out.println(arguments + " is " + getPath(arguments));
        } else {
            System.out.println(arguments + ": not found");
        }
    }

    private static boolean isExecutable(String arguments) {
        return getPath(arguments) != null;
    }

    private static String getPath(String arguments) {
        String[] paths = System.getenv("PATH").split(File.pathSeparator);
        for (String dir : paths) {
            Path fullPath = Path.of(dir, arguments);
            if (Files.isExecutable(fullPath)) {
                return fullPath.toString();
            }
        }
        return null;
    }

    private static boolean isBuiltin(String arguments) {
        return arguments.equals("exit") || arguments.equals("type") || arguments.equals("echo");
    }
}
