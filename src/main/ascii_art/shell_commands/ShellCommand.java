package ascii_art.shell_commands;

import ascii_art.ShellState;

public interface ShellCommand {
	/**
	 * Execute the command with the given arguments.
	 *
	 * @param args       the arguments for the command. args[0] is the command name, usually can be ignored.
	 * @param shellState the current state of the shell
	 */
	void execute(String[] args, ShellState shellState) throws ShellException;
}
