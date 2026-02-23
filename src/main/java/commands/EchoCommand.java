package commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public class EchoCommand implements Command {

    @Override
    public void execute(List<String> args, InputStream in, PrintStream out, PrintStream err) {
        out.println(String.join(" ", args));
    }
}
