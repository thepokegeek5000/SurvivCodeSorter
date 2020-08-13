import java.io.File;
import java.io.IOException;

public class FileOutputter {
    public static void main(String[] args) throws IOException {
        CodeReader reader = new CodeReader(new File("SurvivImageCode/src/ImageCode"));
        reader.runAll();
        System.out.println("Finished, Analysed the content of file \"ImageCode\" and outputted to \"images.txt\"");
    }
}
