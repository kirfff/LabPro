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
    private String sum1;
    private String sum2;
    private String sum3;
    private String sum4;
    /* Only used for weighted */
    private String sum5;
    /* Only used for unweighted */
    private String ysum;

    /**
     * Unweighted constructor for LSM
     */
    public LSM(ArrayList<Double> x, ArrayList<Double> y){
        xData = x;
        yData = y;
        this.weighted = false;
        calculateValues();
        initializeSums();
    }

    /**
     * Weighted constructor for LSM
     */
    public LSM(ArrayList<Double> x, ArrayList<Double> y, ArrayList<Double> sigmay){
        xData = x;
        yData = y;
        sigmaY = sigmay;
        this.weighted = true;
        calculateValues();
        initializeSums();
    }


    /**
     * Getter method for the delta value
     * @return The delta value
     */
    public double getDelta(){
        return delta;
    }

    /**
     * Getter method for the slope value
     * @return The slope value
     */
    public double getM(){
        return m;
    }

    /**
     * Getter method for the y intercept value
     * @return The intercept value
     */
    public double getB(){
        return b;
    }

    /**
     * Getter method for the sigma m value
     * @return The sigma m value
     */
    public double getSigmaM(){
        return sigmaM;
    }

    public void createUnits(){
        return;
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

    /**
     * Using the data uploaded, writes the string summations
     */
    private void initializeSums(){
        //Weighted
        if(weighted){
            sum1 = "( \\frac{1}{" + sigmaY.getFirst() + "^{2}} +...+ " + "\\frac{1}{" + sigmaY.getLast() + "^{2}}) " + yUnits + "^{-1}";
            sum2 = "( \\frac{" + xData.getFirst() +"^{2}}{" + sigmaY.getFirst() + "^{2}} +...+ " + "\\frac{"+ xData.getLast() +"^{2}}{" + sigmaY.getLast() + "^{2}}) \\frac{"+ xUnits +"^{2}}{"+ yUnits +"^{2}})";
            sum3 = "( \\frac{" + xData.getFirst() +"}{" + sigmaY.getFirst() + "^{2}} +...+ " + "\\frac{"+ xData.getLast() +"}{" + sigmaY.getLast() + "^{2}}) \\frac{"+ xUnits +"}{"+ yUnits +"^{2}}";
            sum4 = "( \\frac{" + yData.getFirst() +"}{" + sigmaY.getFirst() + "^{2}} +...+ " + "\\frac{"+ yData.getLast() +"}{" + sigmaY.getLast() + "^{2}})" + yUnits + "^{-2}";
            sum5 = "( \\frac{" + xData.getFirst() + " \\cdot " + yData.getFirst() + "}{" + sigmaY.getFirst() + "^{2}} +...+ \\frac{" + xData.getLast() + " \\cdot " + yData.getLast() + "}{" + sigmaY.getLast() + "^{2}}) \\frac{"+ xUnits +"}{"+yUnits+"}";
        }else{ //Unweighted
            sum1 = "(" + xData.getFirst() +  "^{2} + ... + "+ xData.getLast() + "^{2})" + xUnits + "^{2}";
            sum2 = "(" + xData.getFirst() + "+...+ "+ xData.getLast() +  ")" + xUnits;
            sum3 =  "(" + xData.getFirst() + " \\cdot " + yData.getFirst() + " + ... + " + xData.getLast() + " \\cdot " + yData.getLast() + ")" + xUnits + yUnits;
            sum4 = "(" + yData.getFirst() + " +...+ " + yData.getLast() + ")" + yUnits;
            ysum = "((" +  yData.getFirst() + " - " + m + " \\cdot " + xData.getFirst() + " - " + b + yUnits +")^{2} +...+ (" + yData.getLast() + " - " + m + " \\cdot " + xData.getLast() + " - " + b + yUnits + ")^{2})";
        }
    }


    /**
     * With calculated values, returns the string equation block for the delta value
     * Works for both weighted and unweighted
     * @return A string containing the equation block for the delta calculations.
     */
    public String deltaString(){
        /* First, second and third lines in the delta text output respectively */
        String deltaFirst;
        String deltaNum;
        String deltaFinal;

        if(weighted){
            deltaFirst = "\\Delta &=& \\sum_{k = 1}^{N}(\\frac{1}{\\sigma_{y_{k}}}) \\sum_{k = 1}^{N}(\\frac{x_{k}^{2}}{\\sigma_{y_{k}}^{2}}) - (\\sum_{k = 1}^{N}(\\frac{x_{k}}{\\sigma_{y_{k}}^{2}}))^{2} \\\\ \\nonumber";
            deltaNum = "\\Delta &=& " + sum1 + sum2 + " - " + "("+ sum3 +")^{2} \\\\ \\nonumber";

        }else{//Unweighted
            deltaFirst = " \\Delta &=& N \\sum_{k = 1}^{N}(x_{k}^{2}) - (\\sum_{k =1}^{N}(x_{k}))^{2} \\\\ \\nonumber";
            deltaNum = "\\Delta &=& " + xData.size() + sum1 + " - (" + sum2 + ")^{2} \\\\ \\nonumber";
        }
        /* Result line is same for weighted and unweighted */
        deltaFinal = "\\Delta &=& " + delta + xUnits + "^{2}";

        return "\\begin{eqnarray} \n \t" + deltaFirst + "\n \t" + deltaNum + "\n \t" + deltaFinal + "\n \\end{eqnarray}";
    }

    /**
     * With calculated values, returns the string equation block for the slope
     * Works for both weighted and unweighted
     * @return A string containing the equation block for the slope calculations.
     */
    public String slopeString(){
        /* First, second and third lines in the slope text output respectively */
        String mFirst;
        String mNum;
        String mFinal;

        if(weighted){
            mFirst = "m &=& \\frac{1}{\\Delta}(\sum_{k = 1}^{N}(\\frac{1}{\\sigma_{y_{k}}}) \\sum_{k = 1}^{N}(\\frac{x_{k} y_{k}}{\\sigma_{y_{k}}^{2}}) - \\sum_{k = 1}^{N}(\\frac{x_{k}}{\\sigma_{y_{k}}^{2}}) \\sum_{k = 1}^{N}(\\frac{y_{k}}{\\sigma_{y_{k}}^{2}}) ) \\\\ \\nonumber";
            mNum = "m &=& \\frac{1}{" + delta + "}("+ sum1 + sum5 + " - " + sum2 + sum4 + ") \\\\ \\nonumber";
        }else{
            mFirst = "m &=& \\frac{1}{\\Delta}(\\sum_{k}(\\frac{x_{k}y_{k}}{\\sigma_{y_{k}}^{2}})\\sum_{k}(\\frac{1}{\\sigma_{y_{k}}^{2}}) - \\sum_{k}(\\frac{x_{k}}{\\sigma_{y_{k}}^{2}})\\sum_{k}(\\frac{y_{k}}{\\sigma_{y_{k}}^{2}}) ) \\\\ \\nonumber";
            mNum = "m &=& \\frac{" + xData.size() + sum3 + " - " + sum2 + sum4 +  "}{" + delta +"} \\\\ \\nonumber";
        }
        /* Result line is same for weighted and unweighted */
        mFinal = "m &=& " + m + "\\frac{" + yUnits + "}{" + xUnits +"}";

        return "\\begin{eqnarray} \n \t" + mFirst + "\n \t" + mNum + "\n \t" + mFinal + "\n \\end{eqnarray}";

    }

    /**
     * With calculated values, returns the string equation block for the y intercept
     * Works for both weighted and unweighted
     * @return A string containing the equation block for the intercept calculations.
     */
    public String interceptString(){
        /* First, second and third lines in the intercept text output respectively */
        String bFirst;
        String bNum;
        String bFinal;

        if(weighted){
            bFirst = "b &=& \\frac{1}{\\Delta}(\\sum_{k = 1}^{N}(\\frac{x_{k}^{2}}{\\sigma_{y_{k}}}) \\sum_{k = 1}^{N}(\\frac{y_{k}}{\\sigma_{y_{k}}^{2}}) - \\sum_{k = 1}^{N}(\\frac{x_{k}}{\\sigma_{y_{k}}^{2}}) \\sum_{k = 1}^{N}(\\frac{x_{k} y_{k}}{\\sigma_{y_{k}}^{2}}) ) \\\\ \\nonumber";
            bNum = "b &=& \\frac{1}{" + delta + "}("+ sum2 + sum4 + " - " + sum3 + sum5 + ") \\\\ \\nonumber";
        }else{
            bFirst = "b &=& \\frac{1}{\\Delta} (\\sum_{k = 1}^{N}(x_{k}^{2}) \\sum_{k = 1}^{N}(y_{k}) - \\sum_{k = 1}^{N}(x_{k}) \\sum_{k = 1}^{N}(x_{k} y_{k})) \\\\ \\nonumber";
            bNum = "b &=& \\frac{1}{" + delta + "}" + "(" +sum1 + sum4 + " - " + sum2 + sum3 + ") \\\\ \\nonumber";
        }
        /* Result line is same for weighted and unweighted */
        bFinal = "b &=& " + b + yUnits;

        return "\\begin{eqnarray} \n \t" + bFirst + "\n \t" + bNum + "\n \t" + bFinal + "\n \\end{eqnarray}";
    }


    /**
     * With calculated values, returns the string equation block for the sigma y block
     * Works for only unweighted, otherwise returns null.
     * @return A string containing the equation block for the sigma y calculations.
     */
    public String sigmaYString() {
        if (!weighted) {
            /* First, second and third lines in the intercept text output respectively */
            String sigmaYFirst;
            String sigmaYNum;
            String sigmaYFinal;
            sigmaYFirst = "\\sigma_{y} &=& \\sqrt{\\frac{1}{N - 2} \\sum_{k = 1}^{N}(y_{k} - mx - b)^{2}} \\\\ \\nonumber";
            sigmaYNum = "\\sigma_{y} &=& \\sqrt{\\frac{1}{" + xData.size() + "- 2}" + ysum + yUnits + "} \\\\ \\nonumber";

            sigmaYFinal = "\\sigma_{y} &=&" + b + yUnits;

            return "\\begin{eqnarray} \n \t" + sigmaYFirst + "\n \t" + sigmaYNum + "\n \t" + sigmaYFinal + "\n \\end{eqnarray}";
        }
        return null; //Sigma y is only present for unweighted LSM
    }

    /**
     * With calculated values, returns the string equation block for the sigma m block
     * Works for weighted and unweighted
     * @return A string containing the equation block for the sigma m calculations.
     */
    public String sigmaMString(){
        /* First, second and third lines in the intercept text output respectively */
        String sigmaMFirst;
        String sigmaMNum;
        String sigmaMFinal;

        if(weighted){
            sigmaMFirst = "\\sigma_{m} &=& \\frac{1}{\\Delta}(\\sum_{k = 1}^{N}(\\frac{1}{\\sigma_{y_{k}}}) \\sum_{k = 1}^{N}(\\frac{x_{k} y_{k}}{\\sigma_{y_{k}}^{2}}) - \\sum_{k = 1}^{N}(\\frac{x_{k}}{\\sigma_{y_{k}}^{2}}) \\sum_{k = 1}^{N}(\\frac{y_{k}}{\\sigma_{y_{k}}^{2}}) ) \\\\ \\nonumber";
            sigmaMNum = "\\sigma_{m} &=& \\frac{1}{" + delta + "}("+ sum1 + sum5 + " - " + sum2 + sum4 + ") \\\\ \\nonumber";
        }else{
            sigmaMFirst = "\\sigma_{m} &=& \\sigma_{y} \\sqrt{\\frac{N}{\\Delta}} \\\\ \\nonumber";
            sigmaMNum = "\\sigma_{m} &=& " + sigmaY + "\\sqrt{\\frac{" + xData.size() + "}{" + delta  + "}} \\\\ \\nonumber";
        }
        /* Result line is same for weighted and unweighted */
        sigmaMFinal = "\\sigma_{m} &=& " + sigmaM + "\\frac{" + yUnits + "}{" + xUnits +"}";

        return "\\begin{eqnarray} \n \t" + sigmaMFirst + "\n \t" + sigmaMNum + "\n \t" + sigmaMFinal + "\n \\end{eqnarray}";
    }


    /**
     * With calculated values, returns the string equation block for the sigma b block
     * Works for weighted and unweighted
     * @return A string containing the equation block for the sigma b calculations.
     */
    public String sigmaBString(){
        /* First, second and third lines in the intercept text output respectively */
        String sigmaBFirst;
        String sigmaBNum;
        String sigmaBFinal;

        if(weighted){
            sigmaBFirst = "\\sigma_{b} &=& \\sqrt{\\frac{1}{\\Delta}\\sum_{k = 1}^{N}(\\frac{x_{k}^{2}}{\\sigma_{y_{k}}^{2}})} \\\\ \\nonumber";
            sigmaBNum = "\\sigma_{b} &=& \\sqrt{\\frac{1}{"+ delta + "}" + sum2 + "} \\\\ \\nonumber";
        }else{
            sigmaBFirst = "\\sigma_{b} &=& \\sigma_{y} \\sqrt{\\frac{1}{\\Delta} \\sum_{k = 1}^{N}(x_{k}^{2})} \\\\ \\nonumber";
            sigmaBNum = "\\sigma_{b} &=& " + sigmaY + "\\sqrt{\\frac{1}{" + delta + "}" + sum1 + "} \\\\ \\nonumber";
        }
        /* Result line is same for weighted and unweighted */
        sigmaBFinal = "\\sigma_{b} &=& " + sigmaB + yUnits;

        return "\\begin{eqnarray} \n \t" + sigmaBFirst + "\n \t" + sigmaBNum + "\n \t" + sigmaBFinal + "\n \\end{eqnarray}";
    }


}
