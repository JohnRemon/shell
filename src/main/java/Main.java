import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        do {
            System.out.print("$ ");
            Scanner sc = new Scanner(System.in);
            String command = sc.nextLine();
            System.out.print(command + ": command not found");
            sc.close();
        } while (true);
    }
}
