import javax.script.ScriptException;
import java.io.IOException;

public class FileOutputter {
    public static void main(String[] args) throws IOException, ScriptException {
        String[] refs = {"SurvivDataCode/src/", "inCode.txt", "outCode.txt", "inAUtil.txt"};
        String functNames = "a0_0x2222";
        //CodeReader reader = new CodeReader(refs[0]+refs[1],refs[0]+refs[2],refs[0]+refs[3], functNames);
        CodeReader reader = new CodeReader(refs[0]+refs[1],refs[0]+refs[2],refs[0]+refs[3]);
        reader.run();
        System.out.println("Finished, Analysed the content of file \""+refs[1]+"\" and outputted to \""+refs[2]+"\"");

    }
}
