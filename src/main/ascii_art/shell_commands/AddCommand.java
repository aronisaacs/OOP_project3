package ascii_art.shell_commands;

import image_char_matching.SubImgCharMatcher;

/**
 * A shell command that adds a new character mapping for ASCII art generation.
 *
 * @author ron.stein
 */
public class AddCommand implements ShellCommand {
	/**
	 * Constant used in RemoveCommand as well
	 */
	protected static final String ALL = "all";
	/**
	 * Constant used in RemoveCommand as well
	 */
	protected static final String SPACE = "space";
	private static final String INCORRECT_FORMAT_MSG = "Did not add due to incorrect format.";

	/**
	 * Executes the 'add' command.
	 *
	 * @param args       the arguments for the command. args[0] is the command name, usually can be ignored.
	 * @param shellState the current state of the shell
	 */
	@Override
	public void execute(String[] args, ascii_art.ShellState shellState) throws ShellException {
		if (args.length < 2) {
			throw new ShellException(INCORRECT_FORMAT_MSG);
		}
		String cmd = args[1];
		SubImgCharMatcher matcher = shellState.getSubImgCharMatcher();
		try {
			if (cmd.equals(ALL)) {
				addAllChars(matcher);
			} else if (cmd.equals(SPACE)) {
				matcher.addChar(' ');
			} else if (cmd.length() == 1) {
				matcher.addChar(cmd.charAt(0));
			} else if (isRange(cmd)) {
				addRange(cmd, matcher);
			} else {
				throw new ShellException(INCORRECT_FORMAT_MSG);
			}
		} catch (IllegalArgumentException e) {
			throw new ShellException(INCORRECT_FORMAT_MSG);
		}
	}

	private void addRange(String cmd, SubImgCharMatcher matcher) throws IllegalArgumentException {
		char start = cmd.charAt(0);
		char end = cmd.charAt(2);
		if (start <= end) {
			for (char c = start; c <= end; c++) {
				matcher.addChar(c);
			}
		} else {
			for (char c = end; c <= start; c++) {
				matcher.addChar(c);
			}
		}
	}

	/**
	 * Checks if the given command string represents a character range.
	 *
	 * @param cmd the command string to check
	 * @return true if the command represents a range, false otherwise
	 */
	protected boolean isRange(String cmd) {
		return cmd.length() == 3 && cmd.charAt(1) == '-';
	}

	private void addAllChars(SubImgCharMatcher matcher) {
		for (char c = 32; c <= 126; c++) {
			matcher.addChar(c);
		}
	}

}