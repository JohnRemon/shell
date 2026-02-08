import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        do {
            System.out.print("$ ");
            String command = sc.next();
            String arguments = sc.nextLine().trim();

            switch (command) {
                case "exit" -> exit();
                case "echo" -> echo(arguments);
                case "type" -> type(arguments);
                default -> System.out.println(command + ": command not found");
            }

        } while (true);
    }

    private static void echo(String s) {
        System.out.println(s);
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
        System.out.print(System.getenv("PATH"));
        for (String path : paths) {
            if (Files.isExecutable(Path.of(path)) && path.endsWith(arguments)) {
                return path;
            }
        }
        return null;
    }

    private static boolean isBuiltin(String arguments) {
        return arguments.equals("exit") || arguments.equals("type") || arguments.equals("echo");
    }
}
