import java.util.List;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import commands.Shell;

public class Main {

    public static void main(String[] args) throws Exception {
        Shell shell = new Shell();

        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        DefaultParser parser = new DefaultParser();
        parser.setEscapeChars(null);

        StringsCompleter completer = new StringsCompleter("echo", "exit");

        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .parser(parser)
                .completer(completer)
                .build();

        while (true) {
            String input = reader.readLine("> ");

            if (input.isEmpty()) {
                continue;
            }

            List<String> tokens = Tokenizer.tokenize(input);
            shell.executeCommand(tokens);
        }
    }
}
