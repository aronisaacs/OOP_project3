package ascii_art;

public class Shell {
    /**
     * Constructs a Shell object.
     */
    public Shell(){

    }

    /**
     * Translate the commands from the user to actions.
     * @param imageName the file path of the image to be processed
     */
    public static void run(String imageName){
        System.out.println(imageName);
    }
    /**
     *
     * @param args from the command line
     */
    public static void main(String[] args){
        String imageName = args[0];
        run(imageName);
    }
}
