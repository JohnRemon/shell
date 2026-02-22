import java.util.List;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import autocomplete.CustomCompleter;
import commands.Shell;

public class Main {

    public static void main(String[] args) throws Exception {
        Shell shell = new Shell();

        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        DefaultParser parser = new DefaultParser();
        parser.setEscapeChars(null);

        CustomCompleter completer = new CustomCompleter();

        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .parser(parser)
                .completer(completer)
                .build();

        while (true) {
            String input = reader.readLine("$ ");

            if (input.isEmpty()) {
                continue;
            }

            List<String> tokens = Tokenizer.tokenize(input);
            shell.executeCommand(tokens);
        }
    }
}
