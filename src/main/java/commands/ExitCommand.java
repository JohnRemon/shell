package commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public class ExitCommand implements Command {

    @Override
    public void execute(List<String> args, InputStream in, PrintStream out, PrintStream err) {
        System.exit(0);
    }
}
