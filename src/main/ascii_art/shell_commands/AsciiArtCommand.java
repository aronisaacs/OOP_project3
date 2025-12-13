package ascii_art.shell_commands;

import ascii_art.AsciiArtAlgorithm;
import ascii_art.OutputMode;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;

/**
 * The AsciiArtCommand class implements the ShellCommand interface to generate ASCII art from an image.
 * It uses the AsciiArtAlgorithm to process the image and outputs the result using the specified output
 * method.
 *
 * @author ron.stein
 */
public class AsciiArtCommand implements ShellCommand {
	/**
	 * Executes the ASCII art generation command.
	 * Enables the asciiartalgorithm to use a cached brightness grid for optimization.
	 *
	 * @param args       the arguments for the command. args[0] is the command name, usually can be ignored.
	 * @param shellState the current state of the shell
	 * @throws ShellException for example if the char set is too small
	 */
	@Override
	public void execute(String[] args, ascii_art.ShellState shellState) throws ShellException {
		double[][] cachedGrid = shellState.getCachedBrightnessGrid();
		try {
			//generate the ascii art algorithm with the current shell state
			AsciiArtAlgorithm algorithm = new AsciiArtAlgorithm(shellState.getImg(),
					shellState.getSubImgCharMatcher(),
					shellState.getResolution(),
					shellState.getReverseMode(),
					cachedGrid,
					shellState::setCachedBrightnessGrid); //pass method reference for caching

			char[][] twoDimensionArt = algorithm.run();

			//use correct output method
			AsciiOutput output;
			if (shellState.getOutputMode() == OutputMode.HTML) {
				output = new HtmlAsciiOutput("out.html", "Courier New");
			} else {
				output = new ConsoleAsciiOutput();
			}

			//generate the output with the ascii art and relevant output method
			output.out(twoDimensionArt);
		} catch (IllegalStateException e) {
			throw new ShellException(e.getMessage());
		}
	}
}
