package autocomplete;

public class TrieNode {
    private char data;
    private TrieNode[] children;
    private boolean endOfWord;

    public TrieNode() {
        this.children = new TrieNode[128];
        this.endOfWord = false;
    }

    public TrieNode(char data) {
        this.data = data;
        this.children = new TrieNode[128];
        this.endOfWord = false;
    }

    public char getData() {
        return data;
    }

    public void setData(char data) {
        this.data = data;
    }

    public TrieNode[] getChildren() {
        return children;
    }

    public boolean isEndOfWord() {
        return endOfWord;
    }

    public void setEndOfWord(boolean endOfWord) {
        this.endOfWord = endOfWord;
    }

}
