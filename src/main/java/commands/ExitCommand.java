package commands;

import java.io.PrintStream;
import java.util.List;

public class ExitCommand implements Command {

    @Override
    public void execute(List<String> args, PrintStream out, PrintStream err) {
        System.exit(0);
    }
}
