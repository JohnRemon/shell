package commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ExternalCommand implements Command {

    private final Shell shell;

    public ExternalCommand(Shell shell) {
        this.shell = shell;
    }

    @Override
    public void execute(List<String> args, InputStream in, PrintStream out, PrintStream err) throws Exception {
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
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

        if (in != System.in) {
            processBuilder.redirectInput(ProcessBuilder.Redirect.PIPE);
        } else {
            processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
        }

        File redirectFile = shell.getRedirectFile();
        String redirectOperator = shell.getRedirectOperator();

        // if there is redirection to a file
        if (redirectFile != null) {
            // correct redirection
            if (redirectOperator.equals(">") || redirectOperator.equals("1>")) {
                // Truncate stdout
                processBuilder.redirectOutput(redirectFile);
            } else if (redirectOperator.equals(">>") || redirectOperator.equals("1>>")) {
                // Append stdout
                processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(redirectFile));
            } else if (redirectOperator.equals("2>")) {
                // Truncate stderr
                processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                processBuilder.redirectError(redirectFile);
            } else if (redirectOperator.equals("2>>")) {
                // Append stderr
                processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                processBuilder.redirectError(ProcessBuilder.Redirect.appendTo(redirectFile));
            }

            Process process = processBuilder.start();
            getInput(in, process.getOutputStream());
            process.waitFor();
        } else {
            // normal stdout and stderr but check for pipe rerouting
            if (out != System.out) {
                processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
                Process process = processBuilder.start();
                getInput(in, process.getOutputStream());
                process.getInputStream().transferTo(out);
                process.waitFor();
            } else {
                processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                Process process = processBuilder.start();
                getInput(in, process.getOutputStream());
                process.waitFor();
            }
        }
    }

    private void getInput(InputStream in, OutputStream out) throws IOException {
        if (in != System.in) {
            new Thread(() -> {
                try {
                    in.transferTo(out);
                    out.close();
                } catch (IOException e) {
                }
            }).start();
        }
    }
}
