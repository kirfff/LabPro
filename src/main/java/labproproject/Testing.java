import java.util.*;
public class Testing {
    public static void main(String[] args){
        ArrayList<Double> xData = new ArrayList<Double>();
        for(double i = 0 ; i < 10 ;i ++){
            xData.add(i);
        }

        ArrayList<Double> yData = new ArrayList<Double>();
        for(double i = 0 ; i < 10 ;i ++){
            yData.add(i);
        }

        ArrayList<Double> sigmaY = new ArrayList<Double>();
        for(double i = 1 ; i < 11 ;i ++){
            sigmaY.add(i*0.1);
        }

        LSM lsm = new LSM(xData, yData, sigmaY);

        System.out.println(lsm.sigmaYString());
    }
}