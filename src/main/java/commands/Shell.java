package commands;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shell {

    private Path currentDirectory;
    private final Map<String, Command> builtins;
    private final ExternalCommand externalCommand;

    private File redirectFile;

    public Shell() {
        this.currentDirectory = Path.of(System.getProperty("user.dir"));
        this.builtins = new HashMap<>();
        this.externalCommand = new ExternalCommand(this);

        builtins.put("echo", new EchoCommand());
        builtins.put("exit", new ExitCommand());
        builtins.put("type", new TypeCommand(this));
        builtins.put("pwd", new PwdCommand(this));
        builtins.put("cd", new CdCommand(this));
    }

    public void executeCommand(List<String> tokens) throws Exception {
        // Find the index of '>' || '1>' || '2>'
        int redirectIndex = findRedirectIndex(tokens);

        // Initialize command tokens and output file if any
        List<String> commandTokens;
        File outputFile = null;

        // if redirection exists
        if (redirectIndex != -1) {
            // get tokens before the '>' || '1>' || '2>'
            commandTokens = tokens.subList(0, redirectIndex);

            // get the file path to output to
            Path filePath = Path.of(tokens.get(redirectIndex + 1));

            // create parents if needed
            filePath.getParent().toFile().mkdirs();

            // create the file if needed
            outputFile = filePath.toFile();
            outputFile.createNewFile();
        } else {
            // normal command with no redirection
            commandTokens = tokens;
        }

        // there is no commands (invalid)
        if (commandTokens.isEmpty()) {
            return;
        }

        // extract the first word (command)
        String command = commandTokens.get(0);

        // extract the arguments
        List<String> arguments = commandTokens.size() > 1
                ? commandTokens.subList(1, commandTokens.size())
                : Collections.emptyList();

        // set the redirect file
        this.redirectFile = outputFile;

        try {
            // if it is a built in
            if (builtins.containsKey(command)) {
                // to print either to redirection file or normal stdout ('>' || '1>')
                PrintStream out;
                // to print error to redirection file or normal stderr ('2>')
                PrintStream err;

                // if redirection file exists
                if (outputFile != null) {
                    // print to file
                    out = new PrintStream(Files.newOutputStream(outputFile.toPath()));
                    err = new PrintStream(Files.newOutputStream(outputFile.toPath()));
                } else {
                    // print to normal stdout
                    out = System.out;
                    err = System.out;
                }

                try {
                    // execute the command
                    builtins.get(command).execute(arguments, out, err);

                    // flush for files
                    out.flush();
                } finally {

                    // close output to file
                    if (outputFile != null) {
                        out.close();
                    }
                }
            } else if (findExecutable(command) != null) {
                externalCommand.execute(commandTokens, System.out, System.err);
            } else {
                System.out.println(command + ": command not found");
            }
        } finally {
            // set redirection file to null
            this.redirectFile = null;
        }
    }

    private int findRedirectIndex(List<String> tokens) {
        int idx = tokens.indexOf(">");
        if (idx != -1) {
            return idx;
        }

        idx = tokens.indexOf("1>");
        if (idx != -1) {
            return idx;
        }

        return tokens.indexOf("2>");
    }

    public boolean isBuiltin(String command) {
        return builtins.containsKey(command);
    }

    public String findExecutable(String command) {
        if (System.getenv("PATH") == null) {
            return null;
        }

        String[] paths = System.getenv("PATH").split(File.pathSeparator);
        for (String dir : paths) {
            Path fullPath = Path.of(dir, command);
            if (Files.isExecutable(fullPath)) {
                return fullPath.toString();
            }
        }
        return null;
    }

    public Path getCurrentDirectory() {
        return currentDirectory;
    }

    public void setCurrentDirectory(Path path) {
        this.currentDirectory = path;
    }

    public File getRedirectFile() {
        return redirectFile;
    }
}
