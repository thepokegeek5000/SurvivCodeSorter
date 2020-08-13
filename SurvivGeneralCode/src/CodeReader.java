import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * An instance to read the image code for surviv.io
 */
public class CodeReader {
    private File codeFile;
    private OutputStreamWriter writer;

    /**
     * Creates an instance of an image code reader
     * @param inFile reference of input file
     * @param outFile reference of output file
     */
    public CodeReader(String inFile, String outFile) throws FileNotFoundException {
        this.codeFile=new File(inFile);
        writer = new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8);
    }

    /**
     * Sorts the images and generates file
     */
    public void run() {
    }

}
