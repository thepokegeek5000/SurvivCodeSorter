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
    private Scanner in;
    private String[] bigArray;

    /**
     * Creates an instance of an image code reader
     * @param inFile reference of input file
     * @param outFile reference of output file
     */
    public CodeReader(String inFile, String outFile) throws FileNotFoundException {
        this.codeFile=new File(inFile);
        writer = new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8);
        in = new Scanner(codeFile);
    }

    /**
     * Runs through the code and generates output
     */
    public void run() {
    }

    private void start() {
        String a = in.nextLine();
        ArrayList<String> bigArrayList = new ArrayList<>();
        Boolean[] checks = {false, false, false};//pastVar, inArray, isString
        String temp = "";
        for (int i=0; i<a.length(); i++) {
            char temp2 = a.charAt(i);
        }
    }

}
