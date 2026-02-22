import java.util.List;

import commands.Shell;
import utils.InputHandler;
import utils.TerminalRawMode;
import utils.Tokenizer;

public class Main {
    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(TerminalRawMode::disable));
        Shell shell = new Shell();

        while (true) {
            TerminalRawMode.enable();
            System.out.print("$ ");
            String input = InputHandler.getInput();
            System.out.print("\r");
            System.out.flush();
            TerminalRawMode.disable();

            if (input.isEmpty()) {
                continue;
            }

            List<String> tokens = Tokenizer.tokenize(input);
            shell.executeCommand(tokens);
        }
    }
}
