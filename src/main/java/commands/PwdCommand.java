package commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

public class PwdCommand implements Command {

    private final Shell shell;

    public PwdCommand(Shell shell) {
        this.shell = shell;
    }

    @Override
    public void execute(List<String> args, InputStream in, PrintStream out, PrintStream err) {
        out.println(shell.getCurrentDirectory());
    }
}
