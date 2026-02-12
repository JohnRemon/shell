package commands;

import java.io.PrintStream;
import java.util.List;

public class TypeCommand implements Command {

    private final Shell shell;

    public TypeCommand(Shell shell) {
        this.shell = shell;
    }

    @Override
    public void execute(List<String> args, PrintStream out, PrintStream err) {
        String argument = args.get(0);

        if (shell.isBuiltin(argument)) {
            out.println(argument + " is a shell builtin");
        } else {
            String path = shell.findExecutable(argument);
            if (path != null) {
                out.println(argument + " is " + path);
            } else {
                err.println(argument + ": not found");
            }
        }
    }
}
