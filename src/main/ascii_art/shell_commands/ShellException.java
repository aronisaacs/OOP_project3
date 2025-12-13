package ascii_art.shell_commands;

/**
 * Exception thrown for errors related to shell commands.
 * @author ron.stein
 */
public class ShellException extends Exception {
    public ShellException(String message) {
        super(message);
    }
}
