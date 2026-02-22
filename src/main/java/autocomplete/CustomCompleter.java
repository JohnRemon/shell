package autocomplete;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class CustomCompleter {
    private static final List<String> COMMANDS = List.of("cd", "echo", "exit", "pwd");
    private Trie trie;

    public CustomCompleter() {
        this.trie = new Trie();

        for (String command : COMMANDS) {
            trie.insert(command);
        }

        String[] paths = System.getenv("PATH").split(File.pathSeparator);
        for (String dir : paths) {
            Path fullPath = Path.of(dir);
            try (Stream<Path> stream = Files.list(fullPath)) {
                stream.filter(Files::isExecutable)
                        .map(path -> path.getFileName().toString())
                        .forEach(trie::insert);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public Set<String> findMatches(String prefix) {
        return trie.findWordsWithPrefix(prefix);
    }

    public String findLowestCommonPrefix(List<String> strs) {
        return trie.lowestCommonPrefix(strs);
    }
}
