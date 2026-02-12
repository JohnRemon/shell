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
    public void execute(List<String> args, PrintStream out, PrintStream err) throws Exception {
        List<String> fullCommand = new ArrayList<>();

        // find the executable in PATH
        String executable = shell.findExecutable(args.getFirst());

        // executable can't be null (invalid)
        if (executable == null) {
            return;
        }

        // add the name only not the full path
        fullCommand.add(args.getFirst());

        // add arguments to the full command
        if (args.size() > 1) {
            fullCommand.addAll(args.subList(1, args.size()));
        }

        // start the process builder with full command and set its directory
        ProcessBuilder processBuilder = new ProcessBuilder(fullCommand);
        processBuilder.directory(shell.getCurrentDirectory().toFile());

        File redirectFile = shell.getRedirectFile();
        String redirectOperator = shell.getRedirectOperator();

        // if there is redirection to a file
        if (redirectFile != null) {
            // redirect output to the correct redirection file
            if (redirectOperator.equals(">") || redirectOperator.equals("1>")) {
                processBuilder.redirectOutput(redirectFile);
                processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
            } else {
                processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                processBuilder.redirectError(redirectFile);
            }
        } else {
            // redirect to normal output
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        }

        Process process = processBuilder.start();
        process.waitFor();
    }
}
