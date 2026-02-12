package commands;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CdCommand implements Command {

    private final Shell shell;

    public CdCommand(Shell shell) {
        this.shell = shell;
    }

    @Override
    public void execute(List<String> args, PrintStream out) {
        if (args.size() != 1) {
            out.println("cd: " + args + ": No such file or directory");
            return;
        }

        String directory = args.get(0);

        if (directory.equals("~")) {
            shell.setCurrentDirectory(Path.of(System.getenv("HOME")));
            return;
        }

        Path newDirectory = shell.getCurrentDirectory().resolve(directory).normalize();
        if (Files.isDirectory(newDirectory)) {
            shell.setCurrentDirectory(newDirectory);
            return;
        }

        out.println("cd: " + directory + ": No such file or directory");
    }
}
