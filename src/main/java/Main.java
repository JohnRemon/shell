import java.util.List;
import java.util.Scanner;

import commands.Shell;

public class Main {

    public static void main(String[] args) throws Exception {
        Shell shell = new Shell();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");
            String input = sc.nextLine();

            if (input.isEmpty()) {
                continue;
            }

            List<String> tokens = Tokenizer.tokenize(input);
            shell.executeCommand(tokens);
        }
    }
}
