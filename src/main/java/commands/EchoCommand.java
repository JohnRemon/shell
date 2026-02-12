package commands;

import java.io.PrintStream;
import java.util.List;

public class EchoCommand implements Command {

    @Override
    public void execute(List<String> args, PrintStream out) {
        out.println(String.join(" ", args));
    }
}
