import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.print("$ ");
            String command = sc.next();
            String arguments = sc.nextLine();

            if (command.equals("exit")) {
                return;
            }
            switch (command) {
                case "exit":
                    return;
                case "echo":
                    echo(arguments);
                    break;

                default:
                    System.out.println(command + ": command not found");
            }

        } while (true);
    }

    private static void echo(String s) {
        System.out.println(s.trim());
    }
}
