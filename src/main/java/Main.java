import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.print("$ ");
            String command = sc.nextLine();
            if (command.equals("exit")) {
                return;
            }
            System.out.println(command + ": command not found");
        } while (true);
    }
}
