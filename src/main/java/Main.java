import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static List<Path> PATH = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        addPath(System.getenv("PATH"));

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
            Path path = getPath(arguments);
            System.out.println(arguments + " is " + path);
        } else {
            System.out.println(arguments + ": not found");
        }
    }

    private static boolean isExecutable(String arguments) {
        return getPath(arguments) != null;
    }

    private static Path getPath(String arguments) {
        for (Path path : PATH) {
            if (Files.isExecutable(path) && path.endsWith(arguments)) {
                return path;
            }
        }
        return null;
    }

    private static void addPath(String arguments) {
        String[] directories = arguments.split(File.pathSeparator);
        List<Path> paths = Arrays.stream(directories)
                .map(Path::of)
                .collect(Collectors.toList());

        PATH.addAll(paths);

    }

    private static boolean isBuiltin(String arguments) {
        return arguments.equals("exit") || arguments.equals("type") || arguments.equals("echo");
    }
}
