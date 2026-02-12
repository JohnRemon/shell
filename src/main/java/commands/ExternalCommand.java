package commands;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ExternalCommand implements Command {

    private final Shell shell;

    public ExternalCommand(Shell shell) {
        this.shell = shell;
    }

    @Override
    public void execute(List<String> args, PrintStream out) throws Exception {
        List<String> fullCommand = new ArrayList<>();
        String executable = shell.findExecutable(args.getFirst());

        if (executable == null) {
            return;
        }

        fullCommand.add(args.getFirst());

        if (args.size() > 1) {
            fullCommand.addAll(args.subList(1, args.size()));
        }

        ProcessBuilder processBuilder = new ProcessBuilder(fullCommand);
        processBuilder.directory(shell.getCurrentDirectory().toFile());

        File redirectFile = shell.getRedirectFile();
        if (redirectFile != null) {
            processBuilder.redirectOutput(redirectFile);
        } else {
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        }
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

        Process process = processBuilder.start();
        process.waitFor();
    }
}
