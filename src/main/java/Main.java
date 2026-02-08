import java.util.Scanner;

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
            ;

        } while (true);
    }

    private static void echo(String s) {
        System.out.println(s);
    }

    private static void exit() {
        System.exit(0);
    }

    private static void type(String arguments) {
        if (arguments.equals("echo") || arguments.equals("exit") | arguments.equals("type")) {
            System.out.println(arguments + " is a shell builtin");
        } else {
            System.out.println(arguments + ": not found");
        }
    }

}
