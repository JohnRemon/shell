package commands;

import java.io.PrintStream;
import java.util.List;

public interface Command {
    void execute(List<String> args, PrintStream out, PrintStream err) throws Exception;
}
