package commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public interface Command {
    void execute(List<String> args, InputStream in, PrintStream out, PrintStream err) throws Exception;
}
