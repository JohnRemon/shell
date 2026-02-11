import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public static List<String> tokenize(String s) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        boolean quotation = false;
        boolean doubleQuotation = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // If we hit a space AND we aren't in a quote
            if (c == ' ' && !quotation && !doubleQuotation) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                // Skip the space
                continue;
            }

            // if we hit a quote (opening or closing)
            if (c == '\'' && !doubleQuotation) {
                quotation = !quotation;
                continue;
            }

            if (c == '\"') {
                doubleQuotation = !doubleQuotation;
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
