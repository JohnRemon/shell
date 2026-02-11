import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Path currentDirectory = Path.of(System.getProperty("user.dir"));
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        try {
            do {
                System.out.print("$ ");
                String input = sc.nextLine();

                if (input.isEmpty()) {
                    continue;
                }

                List<String> tokens = Tokenizer.tokenize(input);

                String command = tokens.get(0);

                List<String> arguments = (tokens.size() > 1)
                        ? tokens.subList(1, tokens.size())
                        : Collections.emptyList();

                if (isBuiltin(command)) {
                    switch (command) {
                        case "exit" -> exit();
                        case "echo" -> echo(arguments);
                        case "type" -> type(tokens.get(0));
                        case "pwd" -> pwd();
                        case "cd" -> cd(arguments);
                    }
                } else if (isExecutable(command)) {
                    execute(tokens);
                } else {
                    System.out.println(command + ": command not found");
                }

            } while (true);
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private static void exit() {
        System.exit(0);
        sc.close();
    }

    private static void echo(List<String> tokens) {
        System.out.println(String.join(" ", tokens));
    }

    private static void type(String argument) {
        if (isBuiltin(argument)) {
            System.out.println(argument + " is a shell builtin");
        } else if (isExecutable(argument)) {
            System.out.println(argument + " is " + getPath(argument));
        } else {
            System.out.println(argument + ": not found");
        }
    }

    private static void pwd() {
        System.out.println(currentDirectory);
    }

    private static void cd(List<String> directories) throws IOException {
        if (directories.size() != 1) {
            System.out.println("cd: " + directories + ": No such file or directory");
            return;
        }

        String directory = directories.get(0);

        if (directory.equals("~")) {
            currentDirectory = Path.of(System.getenv("HOME"));
            return;
        }

        Path newDirectory = currentDirectory.resolve(directory).normalize();
        if (Files.isDirectory(newDirectory)) {
            currentDirectory = newDirectory;
            return;
        }

        System.out.println("cd: " + directories.get(0) + ": No such file or directory");
    }

    private static void execute(List<String> tokens) throws IOException {
        Process process = new ProcessBuilder(tokens)
                .directory(currentDirectory.toFile())
                .start();

        try (BufferedReader reader = process.inputReader()) {
            reader.lines().forEach(System.out::println);
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

    private static boolean isBuiltin(String command) {
        return command.equals("exit") || command.equals("type") || command.equals("echo")
                || command.equals("pwd") || command.equals(("cd"));
    }
}
