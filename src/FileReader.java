
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
public class FileReader {
    private final int WEIGHTED_SIZE = 3; //4 Data points for weighted analysis in form: xData | yData | yError
    private final int UNWEIGHTED_SIZE = 2; //2 Data points for unweighted analysis in form xData | yData
    private boolean weighted;
    private File file;
    private ArrayList<Double> x = new ArrayList<Double>();
    private ArrayList<Double> y = new ArrayList<Double>();
    private ArrayList<Double> sigmaX = new ArrayList<Double>();
    private ArrayList<Double> sigmaY = new ArrayList<Double>();
    public FileReader(String path, boolean weighted){
        this.weighted = weighted;
        file = new File(path);
    }

    /**
     * Reads a given file with the scanner class
     */
    public boolean readGivenFile() throws FileNotFoundException {
        if(!checkFile()){
            return false;
        }
        Scanner sc = new Scanner(file);
        sc.useDelimiter(",");
        if(sc.hasNext()){
            sc.next();
        }

        String str;

        while(sc.hasNext()){
            str = sc.next();
            addXY(str,weighted);
        }
        return true;
    }

    /**
     * Checks the following conditions of the file, and returns false if one or more does not pass
     * 1. File exists in path
     * 2. If weighted, contains 4 columns, if unweighted 2 columns
     * 3. All values are cast-able as doubles.
     *
     * @return true if conditions pass, false otherwise
     */
    private boolean checkFile() throws FileNotFoundException {
        //Check if the file exists and is in the directory
        if(file.exists() && file.isDirectory()){
            Scanner sc = new Scanner(file); //Exception is handled already
            if(sc.hasNext()){
                sc.next();
            }
            String str;
            str = sc.next();
            List<String> curr = Arrays.asList(str.split(","));

            int length;
            if(curr.size() == WEIGHTED_SIZE && weighted){
                length = WEIGHTED_SIZE;
            }else if (curr.size() == UNWEIGHTED_SIZE && !weighted){
                length = UNWEIGHTED_SIZE;
            }else{
                return false; //Condition 2.
            }

            //NOTE: Disregards first row, used for titles
            while(sc.hasNext()){
                str = sc.next();
                curr = Arrays.asList(str.split(","));
                for (int i = 0; i < length + 1; i++) {
                    try {
                        Double.valueOf(curr.get(i));
                    } catch (Exception NumberFormatException) {
                        return false; //Condition 3.
                    }
                }
            }
            return true; //Add conditions pass.
        }
        return false; //Condition 1.


    }



    /**
     * Getter for the x data array list.
     * @return the x array list of data
     */
    public ArrayList<Double> getX(){
        return x;
    }

    /**
     * Getter for the y data array list.
     * @return the y array list of data
     */
    public ArrayList<Double> getY(){
        return y;
    }

    /**
     * Getter for the weighted boolean value
     * @return true if weighted, false otherwise
     */
    public boolean getWeighted(){
        return weighted;
    }

    /**
     * Adds the x and y value for a given pair to the x and y data.
     * @param str The string containing x and y separated by a comma as a string
     */
    private void addXY(String str, boolean weighted){
        List<String> curr = Arrays.asList(str.split(","));
        if(weighted) {
            x.add(Double.valueOf(curr.get(0)));
            y.add(Double.valueOf(curr.get(1)));
        }else{
            x.add(Double.valueOf(curr.get(0))); //Cast and add first Double, the x value
            sigmaX.add(Double.valueOf(curr.get(1)));

            y.add(Double.valueOf(curr.get(2)));
            sigmaY.add(Double.valueOf(curr.get(3)));
        }
    }

}
