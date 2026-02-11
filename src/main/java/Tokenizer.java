import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Tokenizer {

    public static List<String> tokenize(String s) {
        List<String> tokens = new ArrayList<>();
        Stack<Character> quotationStack = new Stack<>();
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // If we hit a space AND we aren't in a quote
            if (c == ' ' && quotationStack.isEmpty()) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                // Skip the space
                continue;
            }

            // if we hit a quote (opening or closing)
            if (c == '\'') {
                if (!quotationStack.isEmpty()) {
                    quotationStack.pop();
                } else {
                    quotationStack.push(c);
                }
                continue;
            }

            current.append(c);
        }

        // Add the final token if it exists
        if (current.length() > 0) {
            tokens.add(current.toString());
        }

        return tokens;
    }
}
