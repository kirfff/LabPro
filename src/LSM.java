import java.util.*;
import java.lang.Math;
/**
 * Class which contains LSM analysis functionality.
 * @author Liam Burke
 * @version 1.0 March 18th 2024
 */
public class LSM {
    //True if weighted, false otherwise.
    private boolean weighted;

    private ArrayList<Double> xData = new ArrayList<Double>();
    private ArrayList<Double> yData = new ArrayList<Double>();
    private ArrayList<Double> sigmaY = new ArrayList<Double>();
    //All results used within the analysis.
    private double delta;
    private double m;
    private double b;
    private double sigmaM;
    private double sigmaB;
    private double sigmaYCalculation;
    private String xUnits;
    private String yUnits;

    /**
     * Unweighted constructor for LSM
     */
    public LSM(ArrayList<Double> x, ArrayList<Double> y){
        xData = x;
        yData = y;
        this.weighted = false;
    }

    /**
     * Weighted constructor for LSM
     */
    public LSM(ArrayList<Double> x, ArrayList<Double> y, ArrayList<Double> sigmay){
        xData = x;
        yData = y;
        sigmaY = sigmay;
        this.weighted = true;
    }

    /**
     * Calculates the required LSM variables depending on weighted or unweighted data.
     */
    private void calculateValues(){
        if(weighted){ //Weighted analysis
            double sum1 = 0;
            double sum2 = 0;
            double sum3 = 0;
            double sum4 = 0;
            double sum5 = 0;
            for(int i = 0 ; i < xData.size() ; i ++){
                sum1 += 1/(sigmaY.get(i)*sigmaY.get(i));
                sum2 += xData.get(i)*xData.get(i)/(sigmaY.get(i)*sigmaY.get(i));
                sum3 += xData.get(i)/(sigmaY.get(i)*sigmaY.get(i));
                sum4 += yData.get(i)/(sigmaY.get(i)*sigmaY.get(i));
                sum5 += xData.get(i)*yData.get(i)/(sigmaY.get(i)*sigmaY.get(i));
            }

            delta = sum1*sum2 - sum3*sum3;

            m = (sum1*sum5 - sum3*sum4)/delta;
            b = (sum2*sum4 - sum3*sum5)/delta;

            sigmaM = Math.sqrt(sum1/delta);
            sigmaB = Math.sqrt(sum2/delta);
        }else{ //Unweighted analysis
            double sum1 = 0;
            double sum2 = 0;
            double sum3 = 0;
            double sum4 = 0;
            int n = xData.size();

            for(int i = 0 ; i < n + 1 ; i ++){
                sum1 += xData.get(i)*xData.get(i);
                sum2 += xData.get(i);
                sum3 += yData.get(i);
                sum4 += yData.get(i)*xData.get(i);
            }
            delta = n*sum1 - sum2*sum2;
            m = (n*sum4 - sum2*sum3)/delta;
            b = (sum1*sum3 - sum2*sum4)/delta;
            double diffSum = 0;
            for (int i = 0 ; i < n + 1 ; i++){
                diffSum += (yData.get(i) - m*xData.get(i) - b)*(yData.get(i) - m*xData.get(i) - b);
            }
            sigmaYCalculation = Math.sqrt(diffSum/(n-2));
            sigmaM = sigmaYCalculation*Math.sqrt(n/delta);
            sigmaB = sigmaYCalculation*Math.sqrt(sum1/delta);

        }

    }

    public String deltaString(){
        String sum1;
        String sum2;
        String sum3;
        String sum4;

        if(weighted){
            if(xData.size() <= 3){ //Adjust display if data set is too short

            }else{
                //Find a way to display the units of x and y
            }
        }else{

        }

        return "";
    }



}
