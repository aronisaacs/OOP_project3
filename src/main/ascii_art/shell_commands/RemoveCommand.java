package ascii_art.shell_commands;

import image_char_matching.SubImgCharMatcher;

/**
 * A shell command that removes character mappings for ASCII art generation.
 * @author ron.stein
 */
public class RemoveCommand extends AddCommand{
    private static final String INCORRECT_REMOVE_FORMAT_MSG = "Did not remove due to incorrect format.";

    /**
     * Executes the 'remove' command.
     * @param args the arguments for the command. args[0] is the command name, usually can be ignored.
     * @param shellState the current state of the shell
     */
    @Override
    public void execute(String[] args, ascii_art.ShellState shellState) throws ShellException{
        if(args.length < 2){
            throw new ShellException(INCORRECT_REMOVE_FORMAT_MSG);
        }
        String cmd = args[1];
        SubImgCharMatcher matcher = shellState.getSubImgCharMatcher();
        try {
            if (cmd.equals(ALL)) {
                removeAllChars(matcher);
            } else if (cmd.equals(SPACE)) {
                matcher.removeChar(' ');
            } else if (cmd.length() == 1) {
                matcher.removeChar(cmd.charAt(0));
            } else if (isRange(cmd)) {
                removeRange(cmd, matcher);
            } else {
                throw new ShellException(INCORRECT_REMOVE_FORMAT_MSG);
            }
        } catch (IllegalArgumentException e) {
            throw new ShellException(INCORRECT_REMOVE_FORMAT_MSG);
        }
    }

    private void removeRange(String cmd, SubImgCharMatcher matcher) {
        char start = cmd.charAt(0);
        char end = cmd.charAt(2);
        if (start <= end ) {
            for(char c = start; c <= end; c++){
                matcher.removeChar(c);
            }
        } else {
            for(char c = end; c <= start; c++){
                matcher.removeChar(c);
            }
        }
    }

    private void removeAllChars(SubImgCharMatcher matcher) {
        for (char c : matcher.getCharset()) {
            matcher.removeChar(c);
        }
    }

}
