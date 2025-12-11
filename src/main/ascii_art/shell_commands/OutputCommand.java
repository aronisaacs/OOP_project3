package ascii_art.shell_commands;

import ascii_art.OutputMode;

/**
 * A shell command that changes the output method for ASCII art generation.
 * Usage: output [console|html]
 * @author ron.stein
 */
public class OutputCommand implements ShellCommand {
    private static final String CONSOLE = "console";
    private static final String HTML = "html";
    private static final String BAD_OUTPUT_FORMAT_MSG = "Did not change output method due to incorrect format.";

    /**
     * Executes the 'output' command.
     * @param args the arguments for the command. args[0] is the command name, usually can be ignored.
     * @param shellState the current state of the shell
     * @throws ShellException if the output format is incorrect
     */
    @Override
    public void execute(String[] args, ascii_art.ShellState shellState) throws ShellException {
        if(args.length > 1){
            if(args[1].equals(CONSOLE)){
                shellState.setOutputMode(OutputMode.CONSOLE);
                return;
            } else if (args[1].equals(HTML)) {
                shellState.setOutputMode(OutputMode.HTML);
                return;
            }
        }
        throw new ShellException(BAD_OUTPUT_FORMAT_MSG);
    }
}
