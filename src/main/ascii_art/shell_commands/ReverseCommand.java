package ascii_art.shell_commands;

/**
 * A shell command that enables reverse mode for ASCII art generation.
 * @author ron.stein
 */
public class ReverseCommand implements ShellCommand {
    /**
     * Executes the 'reverse' command by setting the shell state to reverse mode.
     * @param args the arguments for the command. args[0] is the command name, usually can be ignored.
     * @param shellState the current state of the shell
     */
    @Override
    public void execute(String[] args, ascii_art.ShellState shellState) {
        //returns true already in reverse mode
        shellState.setReverseMode(!shellState.getReverseMode());
    }
}
