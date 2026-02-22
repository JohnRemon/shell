package autocomplete;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

public class CustomCompleter implements Completer {
    private static final List<String> COMMANDS = List.of("cd", "echo", "exit", "pwd");
    private Trie trie;

    public CustomCompleter() throws IOException {
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

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        String word = line.word().toLowerCase();

        List<String> matches = trie.findWordsWithPrefix(word);

        for (String match : matches) {
            candidates.add(new Candidate(match));
        }
    }
}
