import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Main class serves as the entry point for processing zoo files.
 *
 * <p>This class reads file paths from command-line arguments,
 * redirects output streams to the specified output file, and
 * invokes ZooManager.processFiles to handle animal, command,
 * food, and person data.</p>
 *
 * <p>Usage:
 * <pre>
 *   java Main &lt;animalFile&gt; &lt;personsFile&gt; &lt;foodsFile&gt; &lt;commandsFile&gt; &lt;outputFile&gt;
 * </pre>
 * </p>
 *
 * @see ZooManager
 */
public class Main {
    /**
     * Application entry point.
     *
     * @param args command-line arguments:
     *             <ul>
     *               <li>args[0] = path to animal file</li>
     *               <li>args[1] = path to persons file</li>
     *               <li>args[2] = path to foods file</li>
     *               <li>args[3] = path to commands file</li>
     *               <li>args[4] = path to output file</li>
     *             </ul>
     */
    public static void main(String[] args) {
        String animalFile   = args[0];
        String personsFile  = args[1];
        String foodsFile    = args[2];
        String commandsFile = args[3];
        String outputFile   = args[4];

        try (PrintStream fileOut = new PrintStream(new FileOutputStream(outputFile))) {
            // Redirect standard output and error to the file
            System.setOut(fileOut);
            System.setErr(fileOut);

            // Process all files using ZooManager
            ZooManager.ProcessFiles(animalFile, commandsFile, foodsFile, personsFile);
        } catch (FileNotFoundException e) {
            // Print stack trace if the output file cannot be created
            e.printStackTrace();
        }
    }
}
