package ascii_art.shell_commands;

import ascii_art.AsciiArtAlgorithm;
import ascii_art.OutputMode;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;

public class AsciiArtCommand implements ShellCommand {
    @Override
    public void execute(String[] args, ascii_art.ShellState shellState) throws ShellException{
        //check if the charset is too small
        checkCharSetSize(shellState.getCharSetSize());

        //generate the ascii art
        AsciiArtAlgorithm algorithm = new AsciiArtAlgorithm(shellState.getImg(), shellState.getSubImgCharMatcher(), shellState.getResolution());
        char[][] twoDimensionArt = algorithm.run();
        //choose output method
        AsciiOutput output;
        if(shellState.getOutputMode() == OutputMode.HTML){
            output = new HtmlAsciiOutput("output.html", "Courier New");
        } else {
            output = new ConsoleAsciiOutput();
        }
        output.out(twoDimensionArt);
    }
    private void checkCharSetSize(int size) throws ShellException{
        if (size < 2){
            throw new ShellException("Did not execute. Charset is too small.");
        }
    }
}
