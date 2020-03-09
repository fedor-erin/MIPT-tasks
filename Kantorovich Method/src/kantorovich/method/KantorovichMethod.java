package kantorovich.method;
/**
 * @author Fedor
 */
public class KantorovichMethod {
    public static void main(String[] args) {
        System.out.println("I = " + (Simpson(0, 2, 100) + FI(2) - FI(0)));
    }
    public static double func (double x){
        double f;
        if (x == 0){
            f = 0;
        }
        else{
            f = Math.pow(x, -0.5) * (1 / (Math.exp(x / 2) + 3) - ((double)1 / 4) - 
                    (-((double)1 / 32) * Math.pow(x, 1) - ((double)1 / 256) * Math.pow(x, 2)));
        }
        return f;
    }
    
    public static double FI(double x)
        {
            return ((double)1 / 2) * Math.pow(x, 0.5) - ((double)1 / 32) * ((double)2 / 3) * 
                    Math.pow(x, 1.5) - ((double)1 / 256) * ((double)2 / 5) * Math.pow(x, 2.5);
        }
    public static double Simpson(double a, double b, int n){
        double h = (b - a) / (2 * n);
        double sum = 0;
        for (int i = 0; i <= 2*n; i++){
            if ((i == 0) || (i == 2 * n)){
                sum += func(a + i * h);
            }
            else if (i % 2 == 1){
                sum += 4 * func(a + i * h);
            }
            else{
                sum += 2 * func(a + i * h);
            }
        }
        sum *= h / 3;
        return sum;
    }
}