import java.io.IOException;

public class FileOutputter {
    public static void main(String[] args) throws IOException {
        String[] refs = {"SurvivGeneralCode/src/", "inCode.txt", "outCode.txt"};
        CodeReader reader = new CodeReader(refs[0]+refs[1],refs[0]+refs[2]);
        reader.run();
        System.out.println("Finished, Analysed the content of file \""+refs[1]+"\" and outputted to \""+refs[2]+"\"");
    }
}
