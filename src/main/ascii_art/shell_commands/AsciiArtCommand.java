package ascii_art.shell_commands;

import ascii_art.AsciiArtAlgorithm;
import ascii_art.OutputMode;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;

public class AsciiArtCommand implements ShellCommand {
    @Override
    public void execute(String[] args, ascii_art.ShellState shellState) throws ShellException {
        //check if the charset is too small
        //TODO: the ascii art algorithm should handle this
//        checkCharSetSize(shellState.getCharSetSize());

        double[][] cachedGrid = shellState.getCachedBrightnessGrid();

        try {
            //generate the ascii art algorithm with the current shell state
            AsciiArtAlgorithm algorithm = new AsciiArtAlgorithm(shellState.getImg(),
                    shellState.getSubImgCharMatcher(),
                    shellState.getResolution(),
                    shellState.isReverseMode(),
                    cachedGrid,
                    shellState::setCachedBrightnessGrid); //pass method reference for caching

            char[][] twoDimensionArt = algorithm.run();

            //use correct output method
            AsciiOutput output;
            if (shellState.getOutputMode() == OutputMode.HTML) {
                output = new HtmlAsciiOutput("output.html", "Courier New");
            } else {
                output = new ConsoleAsciiOutput();
            }

            //generate the output with the ascii art and relevant output method
            output.out(twoDimensionArt);
        } catch (IllegalStateException e) {
            throw new ShellException(e.getMessage());
        }
    }
//    private void checkCharSetSize(int size) throws ShellException{
//        if (size < 2){
//            throw new ShellException("Did not execute. Charset is too small.");
//        }
//    }
}
