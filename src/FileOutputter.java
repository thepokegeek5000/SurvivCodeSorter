import java.io.File;
import java.io.IOException;
/*
How to use:

Take surviv.io image function, go to https://beautifier.io/ , paste the JS, and use Ctrl+Enter to beautify the code
Copy the beautified code to The file "ImageCode" (double click to open)
Run the program by clicking one of the two play buttons on this file
After it's finished, open the file "images.txt" and there you go
 */
public class FileOutputter {
    public static void main(String[] args) throws IOException {
        CodeReader reader = new CodeReader(new File("ImageCode"));
        reader.runAll();
        System.out.println("Finished, Analysed the content of file \"ImageCode\" and outputted to \""+reader.getFile()+"\"");
    }
}
