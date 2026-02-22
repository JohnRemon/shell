package autocomplete;

import java.util.ArrayList;
import java.util.List;

public class Trie {

    TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode curr = root;

        for (char c : word.toCharArray()) {
            int index = (int) c;
            if (curr.getChildren()[index] == null) {
                curr.getChildren()[index] = new TrieNode(c);
            }
            curr = curr.getChildren()[index];
        }
        curr.setEndOfWord(true);
    }

    public boolean search(String word) {
        TrieNode curr = root;

        for (char c : word.toCharArray()) {
            int index = (int) c;
            if (curr.getChildren()[index] == null) {
                return false;
            }
            curr = curr.getChildren()[index];
        }
        return curr.isEndOfWord();
    }

    public boolean startsWith(String prefix) {
        TrieNode curr = root;

        for (char c : prefix.toCharArray()) {
            int index = (int) c;
            if (curr.getChildren()[index] == null) {
                return false;
            }
            curr = curr.getChildren()[index];
        }
        return true;
    }

    public List<String> findWordsWithPrefix(String prefix) {
        List<String> res = new ArrayList<>();
        TrieNode curr = root;

        for (char c : prefix.toCharArray()) {
            int index = (int) c;
            if (curr.getChildren()[index] == null) {
                return res;
            }
            curr = curr.getChildren()[index];
        }

        findWordsWithPrefixHelper(res, curr, new StringBuilder(prefix));

        return res;
    }

    private void findWordsWithPrefixHelper(List<String> res, TrieNode curr, StringBuilder word) {

        if (curr.isEndOfWord()) {
            res.add(word.toString());
        }

        for (int i = 0; i < 128; i++) {
            TrieNode child = curr.getChildren()[i];
            if (child != null) {
                word.append(child.getData());
                findWordsWithPrefixHelper(res, child, word);
                word.setLength(word.length() - 1);
            }
        }
    }
}
