package commands;

import java.io.PrintStream;
import java.util.List;

public class PwdCommand implements Command {

    private final Shell shell;

    public PwdCommand(Shell shell) {
        this.shell = shell;
    }

    @Override
    public void execute(List<String> args, PrintStream out) {
        out.println(shell.getCurrentDirectory());
    }
}
