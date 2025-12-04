package ascii_art.shell_commands;

import java.util.TreeSet;

/**
 * A shell command that manages the characters used for ASCII art generation.
 * Currently, this command is a placeholder and does not implement any functionality.
 * @author ron.stein
 */
public class CharsCommand implements ShellCommand{
    /**
     * Executes the 'chars' command.
     * @param args the arguments for the command. args[0] is the command name, usually can be ignored.
     * @param shellState the current state of the shell
     */
    @Override
    public void execute(String [] args, ascii_art.ShellState shellState) {
        TreeSet<Character> charSet = shellState.getSubImgCharMatcher().getCharset();
        if(charSet.isEmpty()){
            return;
        }
        prettyPrintCharSet(charSet);
    }

    private void prettyPrintCharSet(TreeSet<Character> charSet) {
        StringBuilder string = new StringBuilder();
        for (Character c : charSet) {
            string.append(c).append(" ");
        }
        System.out.println(string.toString().trim());
    }
}
