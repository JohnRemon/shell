import java.util.List;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import autocomplete.CustomCompleter;
import commands.Shell;
import utils.TerminalRawMode;
import utils.Tokenizer;
import utils.InputHandler;

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
