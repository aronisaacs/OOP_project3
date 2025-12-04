package ascii_art;

import image.Image;
import ascii_art.shell_commands.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Shell {
    private static final String CHARS = "chars";
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String RES = "res";
    private static final String OUTPUT = "output";
    private static final String REVERSE = "reverse";
    private static final String ASCII_ART = "asciiArt";
    private static final String EXIT = "exit";

    private ShellState shellState;
    private Map<String, ShellCommand> commands;

    /**
     * Constructs a Shell object.
     */
    public Shell() {}

    /**
     * The main method to start the shell.
     *
     * @param args from the command line
     */
    public static void main(String[] args) {
        new Shell().run(args[0]);
    }

    /**
     * Translate the commands from the user to actions.
     *
     * @param imageName the file path of the image to be processed
     */
    public void run(String imageName) {
        //try to load the image and create the shell state
        if(!initializeShellState(imageName)){
            return;
        }
        //initialize the commands map
        initializeCommandsMap();
        //start the shell loop
        while (true) {
            System.out.print(">>> ");
            String line = KeyboardInput.readLine();
            if(line == null){
                break;
            }
            line = line.trim();
            if (line.isEmpty()){
                continue;
            }
            String[] tokens = line.split("\\s+");
            String commandName = tokens[0];
            //exit shortcut
            if (commandName.equals(EXIT)) {
                break;
            }
            //check if command exists
            ShellCommand command = commands.get(commandName);
            if (command == null) {
                System.out.println("Did not execute due to incorrect command.");
                continue;
            }
            try {
                //passes the String[] tokens where tokens[0] is the command name
                command.execute(tokens, shellState);
            } catch (ShellException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean initializeShellState(String imageName){
        try {
            Image image = new Image(imageName);
            this.shellState = new ShellState(image);
            return true;
        } catch (IOException e) {
//            System.out.println("Error loading image");
            return false;
        }
    }

    private void initializeCommandsMap() {
        this.commands = new HashMap<>();
        commands.put(CHARS, new CharsCommand());
        commands.put(ADD, new AddCommand());
        commands.put(REMOVE, new RemoveCommand());
        commands.put(RES, new ResCommand());
        commands.put(OUTPUT, new OutputCommand());
        commands.put(REVERSE, new ReverseCommand());
        commands.put(ASCII_ART, new AsciiArtCommand());
    }
}

