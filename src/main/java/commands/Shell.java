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
        int redirectIndex = findRedirectIndex(tokens);
        List<String> commandTokens;
        File outputFile = null;

        if (redirectIndex != -1) {
            commandTokens = tokens.subList(0, redirectIndex);
            Path filePath = Path.of(tokens.get(redirectIndex + 1));
            filePath.getParent().toFile().mkdirs();
            outputFile = filePath.toFile();
            outputFile.createNewFile();
        } else {
            commandTokens = tokens;
        }

        if (commandTokens.isEmpty()) {
            return;
        }

        String command = commandTokens.get(0);
        List<String> arguments = commandTokens.size() > 1
                ? commandTokens.subList(1, commandTokens.size())
                : Collections.emptyList();

        this.redirectFile = outputFile;

        try {
            if (builtins.containsKey(command)) {
                PrintStream out;
                if (outputFile != null) {
                    out = new PrintStream(Files.newOutputStream(outputFile.toPath()));
                } else {
                    out = System.out;
                }
                try {
                    builtins.get(command).execute(arguments, out);
                    out.flush();
                } finally {
                    if (outputFile != null) {
                        out.close();
                    }
                }
            } else if (findExecutable(command) != null) {
                externalCommand.execute(commandTokens, System.out);
            } else {
                System.out.println(command + ": command not found");
            }
        } finally {
            this.redirectFile = null;
        }
    }

    private int findRedirectIndex(List<String> tokens) {
        int idx = tokens.indexOf(">");
        if (idx != -1)
            return idx;
        idx = tokens.indexOf("1>");
        return idx;
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
