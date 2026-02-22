package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import autocomplete.CustomCompleter;

public class InputHandler {
    private static CustomCompleter customCompleter = new CustomCompleter();

    public static String getInput() throws IOException {

        StringBuilder buffer = new StringBuilder();
        int tabCount = 0;

        while (true) {
            int ch = System.in.read();
            if (ch == -1)
                System.exit(0);

            // ENTER
            if (ch == '\n' || ch == '\r') {
                System.out.println();
                return buffer.toString();
            }

            // TAB
            if (ch == '\t') {
                tabCount = handleTab(buffer, tabCount);
                continue;
            }

            // BACKSPACE
            if (ch == 127 || ch == 8) {
                if (!buffer.isEmpty()) {
                    buffer.setLength(buffer.length() - 1);
                    System.out.print("\b \b");
                }
                System.out.flush();
                tabCount = 0;
                continue;
            }

            // NORMAL CHAR
            if (ch >= 32) {
                buffer.append((char) ch);
                System.out.print((char) ch);
                System.out.flush();
                tabCount = 0;
            }
        }
    }

    // ================= TAB HANDLING =================

    private static int handleTab(StringBuilder buffer, int tabCount) {
        String input = buffer.toString();

        // Only autocomplete first word
        if (input.contains(" ")) {
            bell();
            return 0;
        }

        // find all matches
        Set<String> matches = customCompleter.findMatches(input);
        if (matches.isEmpty()) {
            bell();
            return 0;
        }

        // sort alphabetically
        List<String> sorted = new ArrayList<>(matches);
        Collections.sort(sorted);

        // FIRST TAB: ring bell and lcp
        if (tabCount == 0) {
            bell();

            // lowest common prefix logic
            String lowestCommonPrefix = customCompleter.findLowestCommonPrefix(sorted);

            if (lowestCommonPrefix.length() > input.length()) {
                String completion = lowestCommonPrefix.substring(input.length());
                buffer.append(completion);
                System.out.print(completion);
                System.out.flush();
            }

            // complete if only one match
            if (sorted.size() == 1 && buffer.length() == sorted.get(0).length()) {
                buffer.append(" ");
                System.out.print(" ");
            }

            System.out.flush();
            return 1;
        }

        // SECOND TAB: print all matches
        if (tabCount == 1) {
            System.out.print("\r\n");
            System.out.print(String.join("  ", sorted));
            System.out.print("\r\n");
            System.out.print("$ ");
            System.out.print(buffer);
            System.out.flush();
            return 0; // reset
        }

        return 0;
    }

    private static void bell() {
        System.out.print("\u0007");
        System.out.flush();
    }
}
