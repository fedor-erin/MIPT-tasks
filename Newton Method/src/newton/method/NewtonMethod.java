package newton.method;
/**
 * @author Fedor
 */
import java.lang.Math;

public class NewtonMethod {
    static double eps = 0.00001;
    static double alpha = 0.9;
    static double k = 0.4;
    
    public static double func1(double x, double y){
    return Math.tan(x*y + k) - x*x;
    }
 
    public static double func2(double x, double y){
    return alpha*x*x + 2*y*y - 1;
    }
 
    //df1/dx
    public static double func11(double x, double y){
        return 1/Math.cos(x*y + k) * y - 2*x;
    }
 
    //df1/dy
    public static double func12(double x, double y){
        return 1/Math.cos(x*y + k) * x;
    }
 
    //df2/dx
    public static double func21(double x, double y){
        return alpha*2*x;
    }
 
    //df2/dy
    public static double func22(double x, double y){
        return 4*y;
    }
 
    public static void obr_matr(double[][] a){
        double det, aa;
        det = a[0][0]*a[1][1] - a[0][1]*a[1][0];
        aa = a[0][0];
        a[0][0] = a[1][1]/det;
        a[1][1] = aa/det;
        aa = a[0][1];
        a[0][1] = -a[1][0]/det;
        a[1][0] = -aa/det;
    }
    
    public static void main(String[] args) {
        //First pair of roots
        double x = 0.4;
        double y = -0.7;
        int i = 1;
        double[][] a = new double [2][2]; //Yakobi matrix
        double[] b = new double [2];
        double dx, dy, norm;
        do
        {
            a[0][0] = func11(x, y);
            a[0][1] = func12(x, y);
            a[1][0] = func21(x, y);
            a[1][1] = func22(x, y);
            obr_matr(a);
            dx = - a[0][0]*func1(x, y) - a[0][1]*func2(x, y);
            dy = - a[1][0]*func1(x, y) - a[1][1]*func2(x, y);
            x = x + dx;
            y = y + dy;
            b[0] = func1(x, y);
            b[1] = func2(x, y);
            norm = Math.sqrt(b[0]*b[0] + b[1]*b[1]);
            i++;
        }
        while (norm >= eps);
        System.out.println("x = " + x + "; y = " + y);
        System.out.println("x = " + -x + "; y = " + -y);
        
        //Second pair of roots
        x = 0.9;
        y = 0.3;
        i = 1;
        do
        {
            a[0][0] = func11(x, y);
            a[0][1] = func12(x, y);
            a[1][0] = func21(x, y);
            a[1][1] = func22(x, y);
            obr_matr(a);
            dx = - a[0][0]*func1(x, y) - a[0][1]*func2(x, y);
            dy = - a[1][0]*func1(x, y) - a[1][1]*func2(x, y);
            x = x + dx;
            y = y + dy;
            b[0] = func1(x, y);
            b[1] = func2(x, y);
            norm = Math.sqrt(b[0]*b[0] + b[1]*b[1]);
            i++;
        }
        while (norm >= eps);
        System.out.println("x = " + x + "; y = " + y);
        System.out.println("x = " + -x + "; y = " + -y);
        System.out.println("Number of iterations: " + i);
    }                                                                                   
}