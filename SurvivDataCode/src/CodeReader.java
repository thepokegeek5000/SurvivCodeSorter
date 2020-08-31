import javax.script.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * An instance to read the image code for surviv.io
 */
public class CodeReader {
    private File targetFile;
    private File utilFile;
    private String functName;

    private Scanner in;
    private Scanner in2;
    private String[] bigArray;
    private OutputStreamWriter writer;
    ScriptEngineManager manager;
    ScriptEngine engine;


    /**
     * Creates an instance of an image code reader
     * @param inFile reference of input file
     * @param outFile reference of output file
     * @param functFile reference of the function file
     * @param functName name of the function
     * @throws FileNotFoundException
     */
    public CodeReader(String inFile, String outFile, String functFile, String functName) throws FileNotFoundException {
        this.targetFile=new File(inFile);
        this.utilFile=new File(functFile);
        this.functName=functName;
        this.writer = new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8);
        this.in = new Scanner(targetFile);
        this.in2 = new Scanner(utilFile);
        this.manager = new ScriptEngineManager();
        this.engine = manager.getEngineByName("javascript");
    }

    public CodeReader(String inFile, String outFile, String functFile) throws FileNotFoundException {
        this.targetFile=new File(inFile);
        this.utilFile=new File(functFile);
        //this.functName=functName;
        this.writer = new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8);
        this.in = new Scanner(targetFile);
        this.in2 = new Scanner(utilFile);
        this.manager = new ScriptEngineManager();
        this.engine = manager.getEngineByName("javascript");
    }

    /**
     * Runs through the code and generates output
     */
    public void run() throws ScriptException {
        parseUtilFile();
    }

    private void parseUtilFile() throws ScriptException {
        String allCode=in2.nextLine();
        String temp = in2.nextLine();
        while (true) {
            String temp2=in2.nextLine();
            temp = temp+temp2;
            if (!temp2.startsWith("    ")) break;
        }
        allCode=allCode+temp;

        temp = in2.nextLine();
        functName = temp.substring(4, 13);
        //System.out.println(functName);
        while (in2.hasNextLine()) {
            String temp2=in2.nextLine();
            temp = temp+temp2;
        }
        allCode=allCode+temp;

        engine.eval(allCode);

        System.out.println(engine.eval(functName+"(\'0x165b\');"));
    }

}
