package utils;

import java.io.IOException;

public class TerminalRawMode {
    private static boolean enabled = false;

    public static void enable() {
        if (enabled) {
            return;
        }

        try {
            exec("stty -echo raw < /dev/tty");
            enabled = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to enable raw mode");
        }
    }

    public static void disable() {
        if (!enabled) {
            return;
        }

        try {
            exec("stty echo cooked < /dev/tty");
            enabled = false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to disable raw mode");
        }
    }

    private static void exec(String cmd) throws IOException, InterruptedException {
        new ProcessBuilder("sh", "-c", cmd)
                .inheritIO()
                .start()
                .waitFor();
    }
}
