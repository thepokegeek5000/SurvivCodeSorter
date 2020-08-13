import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * An instance to read the image code for surviv.io
 */
public class CodeReader {
    private File codeFile;
    private String[][][] fileArray;
/*
    [
        [[asset name], [url1, url2, etc.], [image1, image2, etc.]],
        [[asset name], [url1, url2, etc.], [image1, image2, etc.]],
    ]
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
        fileArray=genLists(codeFile);           //Generates Data Array
        sortLists(fileArray); canFetch[0]=true; //Sorts Data Array
        genFile();                              //Generates File
    }

    /**
     * Removes excess data from the file and splits it into multiple files
     * @param file File to read
     * @return File array
     * @throws IOException
     */
    @NotNull
    private String[][][] genLists(File file) throws IOException {
        File temp = file;
        Scanner in = new Scanner(temp);
        ArrayList<ArrayList<ArrayList<String>>> output = new ArrayList<>(0);

        int[] outSlots = {-1, -1, -1};//[asset slot, image slot, file slot]
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
                    isFrames=false;
                    break;
                case 1:
                    if (!(line.startsWith("    }"))) {
                        asset=line.substring(5, l-5);
                        outSlots[0]++;
                        outSlots[1]=0;
                        outSlots[2]=0;
                        output.add(outSlots[0], new ArrayList<>());
                        output.get(outSlots[0]).add(new ArrayList<>());//0 - asset name
                        output.get(outSlots[0]).add(new ArrayList<>());//1 - files
                        output.get(outSlots[0]).add(new ArrayList<>());//2 - image names
                        output.get(outSlots[0]).get(0).add(outSlots[1], asset);
                    }
                    isFrames=false;//Always has a meta section after not frames
                    break;
                case 2:
                    isFrames = line.endsWith("\"frames\": {")?true:false;//check if it's frames
                    break;
                case 3:
                    if (isFrames && !(line.endsWith("}")) && !(line.endsWith("},"))) {
                        output.get(outSlots[0]).get(2).add(outSlots[2], line.substring(13, l-4));
                        outSlots[2]++;
                    } else if (!isFrames && line.startsWith("            \"image\":")) {
                        output.get(outSlots[0]).get(1).add(outSlots[1], line.substring(22, l-2));
                        outSlots[1]++;
                    }
                    break;
                default: break;
            }

        }

        //Convert output from ArrayList<ArrayList<String>> to String[][]
        String[][][] out = new String[output.size()][][];
        for (int i=0; i<output.size(); i++) {
            out[i] = new String[output.get(i).size()][];
            for (int j=0; j<output.get(i).size(); j++) {
                out[i][j] = new String[output.get(i).get(j).size()];
                out[i][j] = output.get(i).get(j).toArray(out[i][j]);
            }
        }
        return out;
    }

    /**
     * Takes an array and sorts the strings in it in alphabetical order, except the first value which is an indicator value
     * @param data array
     */
    private void sortLists(@NotNull String[][][] data) {
        for (int a=0; a<data.length; a++) {
            String temp;
            for (int i=0; i<data[a][2].length; i++) {
                for (int j=i+1; j<data[a][2].length; j++) {
                    if (data[a][2][i].compareTo(data[a][2][j]) > 0) {
                        temp = data[a][2][i];
                        data[a][2][i] = data[a][2][j];
                        data[a][2][j] = temp;
                    }
                }
            }
        }
        for (int i=0; i<data.length; i++) {
            String[][] temp;
            for (int j=i+1; j<data.length; j++) {
                if (data[i][0][0].compareTo(data[j][0][0]) > 0) {
                    temp = data[i];
                    data[i] = data[j];
                    data[j] = temp;
                }
            }
        }
        //System.out.println();
    }

    /**
     * Generates output as a single file if you have run the sorting function before
     */
    public void genFile() throws IOException {
        if (canFetch[0]) {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("images.txt"), StandardCharsets.UTF_8);
            for (int i=0; i<fileArray.length; i++) {
                writer.write("== "+fileArray[i][0][0]+" ==");
                for (int j=0; j<fileArray[i][1].length; j++) {
                    writer.write("\n# https://surviv.io/assets/"+fileArray[i][1][j]);
                }
                writer.write("\n");
                for (int k=0; k<fileArray[i][2].length; k++) {
                    writer.write("\n* "+fileArray[i][2][k]);
                }
                String bonusLine = i==fileArray.length-1?"":"\n\n";
                writer.write("\n"+bonusLine);
            }
            writer.close();
            outFile=new File("imagesBeta.txt");
            canFetch[1]=true;
        } else System.out.println("Error: Failed to use sortImages() first");
    }
}
