package ascii_art.shell_commands;

/**
 * A shell command that manages the resolution of the ASCII art.
 * The resolution determines how many characters are used per row in the ASCII art representation.
 * Users can increase or decrease the resolution within defined boundaries.
 * @author ron.stein
 */
public class ResCommand implements ShellCommand {
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String EXCEED_BOUNDARIES_MSG = "Did not change resolution due to exceeding boundaries.";
    private static final String BAD_RES_FORMAT_MSG = "Did not change resolution due to incorrect format.";

    /**
     * Executes the 'res' command.
     * @param args the arguments for the command. args[0] is the command name, usually can be ignored.
     *             if the second argument is not provided, simply prints the current resolution.
     *             if the second argument is "up" resolution is multiplied by 2
     *             is the second argument is "down" resolution is divided by 2
     * @param shellState the current state of the shell
     */
    @Override
    public void execute(String[] args, ascii_art.ShellState shellState) throws ShellException {
        int curResolution = shellState.getResolution();
        if(args.length > 1){
            if(!(args[1].equals(UP) || args[1].equals(DOWN))){
                throw new ShellException(BAD_RES_FORMAT_MSG);
            }
            if(args[1].equals(UP) && (curResolution*2) <= shellState.getMaxCharsInRow()){
                shellState.setResolution(curResolution * 2);
            } else if (args[1].equals(DOWN) && (curResolution/2) >= shellState.getMinCharsInRow()) {
                shellState.setResolution(curResolution / 2);
            } else {
                throw new ShellException(EXCEED_BOUNDARIES_MSG);
            }
        }
        System.out.println("Resolution set to " + shellState.getResolution() + ".");
    }
}
