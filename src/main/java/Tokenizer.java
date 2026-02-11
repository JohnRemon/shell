import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public static List<String> tokenize(String s) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        boolean quotation = false;
        boolean doubleQuotation = false;
        boolean backSlash = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // if we hit backslash (without quotation)
            if (backSlash) {
                current.append(c);
                // consume backslash
                backSlash = false;
                continue;
            }

            // If we hit a space AND we aren't in a quote
            if (c == ' ' && !quotation && !doubleQuotation) {
                if (current.length() > 0) {
                    tokens.add(current.toString());
                    current.setLength(0);
                }
                // Skip the space
                continue;
            }

            // if we hit a single quote (opening or closing)
            if (c == '\'') {
                quotation = !quotation;
                continue;
            }

            // If we hit a double quote (opening or closing)
            if (c == '\"' && !quotation) {
                doubleQuotation = !doubleQuotation;
                continue;
            }

            // if we hit backslash AND we aren't in a quote
            if (c == '\\' && !quotation) {
                backSlash = true;
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
