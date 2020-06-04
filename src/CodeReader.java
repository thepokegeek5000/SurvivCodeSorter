import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * An instance to read the image code for surviv.io
 */
public class CodeReader {
    private File codeFile;
    private String[][] fileArray;
    /*
    [
        [asset name, image1, image2, etc],
        [asset name, image1, image2, etc],
        etc
    ]
    asset name is kept at [0] as an identifyer while all the various images are sorted
    */
    private boolean[] canFetch;//[is sorted, file generated]
    private File outFile;

    /**
     * Creates an instance of an image code reader
     * @param codeFile File of image code
     */
    @Contract(pure = true)
    public CodeReader(File codeFile) {
        this.codeFile=codeFile;
        this.canFetch=new boolean[] {false, false};
    }

    /**
     * Returns file if file is generated
     * @return file
     */
    public File getFile() {
        return canFetch[1]?outFile:null;
    }

    /**
     * Sorts the images and generates file
     */
    public void runAll() throws IOException {
        sortImages();
        genFile();
    }

    /**
     * Sorts the image code
     * @throws IOException
     */
    public void sortImages() throws IOException {
        fileArray=genLists(codeFile);
        sortLists(fileArray);
        canFetch[0]=true;
    }

    /**
     * Generates output as a single file if you have run the sorting function before
     */
    public void genFile() throws IOException {
        if (canFetch[0]) {
            FileWriter writer = new FileWriter("images.txt");
            for (int i = 0; i<fileArray.length; i++) {
                for (int j = 0; j<fileArray[i].length; j++) {
                    if (j==0) {
                        writer.write("== "+fileArray[i][j]+" ==");
                    } else {
                        writer.write("\n* "+fileArray[i][j]);
                    }
                }
                String bonusLine = i==fileArray.length-1?"":"\n";
                writer.write("\n"+bonusLine);
            }
            outFile=new File("images.txt");
            canFetch[1]=true;
        } else System.out.println("Error: Failed to use sortImages() first");
    }

    /**
     * Removes excess data from the file and splits it into multiple files
     * @param file File to read
     * @return File array
     * @throws IOException
     */
    @NotNull
    private String[][] genLists(File file) throws IOException {
        File temp = file;
        Scanner in = new Scanner(temp);
        ArrayList<ArrayList<String>> output = new ArrayList<>(0);

        int[] outSlots = {-1, -1};
        String asset = "";
        boolean isFrames = false;

        //Read File
        while (in.hasNextLine()) {
            String line = in.nextLine();
            int l = line.length();

            //Calculate the amount of "tabs" (4 spaces) the line starts with
            int tabs = 0;
            for (int i=0; i<l; i+=4) {
                if (line.charAt(i)==' '&&line.charAt(i+1)==' '&&line.charAt(i+2)==' '&&line.charAt(i+3)==' ') {
                    tabs++;
                } else {
                    break;
                }
            }

            //Analyse line based on the tab count on the line and other data
            switch (tabs) {
                case 0:
                case 1:
                    isFrames=false;
                    break;
                case 2:
                    if (!(line.startsWith("        }"))) {
                        asset=line.substring(8, l-4);
                        outSlots[0]++;
                        outSlots[1]=0;
                        output.add(outSlots[0], new ArrayList<>(0));
                        output.get(outSlots[0]).add(outSlots[1], asset);
                    }
                    isFrames=false;//Always has a meta section after not frames
                    break;
                case 3:
                    isFrames = line.endsWith("frames: {")?true:false;//check if it's frames
                    break;
                case 4:
                    if (isFrames && !(line.endsWith("}")) && !(line.endsWith("},"))) {
                        outSlots[1]++;
                        output.get(outSlots[0]).add(outSlots[1], line.substring(17, l-4));
                    }
                    break;
                default: break;
            }

        }

        //Convert output from ArrayList<ArrayList<String>> to String[][]
        String[][] out = new String[output.size()][];
        for (int i=0; i<output.size(); i++) {
            out[i] = new String[output.get(i).size()];
            out[i] = output.get(i).toArray(out[i]);
        }
        return out;
    }

    /**
     * Takes an array and sorts the strings in it in alphabetical order, except the first value which is an indicator value
     * @param data array
     */
    private void sortLists(@NotNull String[][] data) {
        for (int a=0; a<data.length; a++) {
            String temp;
            for (int i=1; i<data[a].length; i++) {
                for (int j=i+1; j<data[a].length; j++) {
                    if (data[a][i].compareTo(data[a][j]) > 0) {
                        temp = data[a][i];
                        data[a][i] = data[a][j];
                        data[a][j] = temp;
                    }
                }
            }
        }
        for (int i=0; i<data.length; i++) {
            String[] temp;
            for (int j=i+1; j<data.length; j++) {
                if (data[i][0].compareTo(data[j][0]) > 0) {
                    temp = data[i];
                    data[i] = data[j];
                    data[j] = temp;
                }
            }
        }
        //System.out.println();
    }
}
