package adamsmethod;

public class AdamsMethod {
    static double a = 2.2;
    static double b = 2.2;
    static double h = 0.1;
    static double x0 = 0, y0 = 0;
    
    //y' = func(x,y)
    public static double func(double x, double y){
        return a/(Math.pow(x,2) + Math.pow(y,2) + b);
    }
    
    //вернет y[i+1], где x[i+1] = x[i] + h
    public static double RungeCoot(double x, double y){
        double delta_y, K_1, K_2, K_3, K_4;
        K_1 = h * func(x, y);
        K_2 = h * func(x + h/2, y + K_1/2);
        K_3 = h * func(x + h/2, y + K_2/2);
        K_4 = h * func(x + h, y + K_3);
        delta_y = (K_1 + 2*K_2 + 2*K_3 + K_4)/6;
        return y + delta_y;
    }
    
    public static void main(String[] args) {
        double x1, x2, x3, x4, x5, y1, y2, y3, y4, y4_prec, y5, y5_prec;
        x1 = x0 + h;
        x2 = x0 + 2*h;
        x3 = x0 + 3*h;
        x4 = x0 + 4*h;
        x5 = x0 + 5*h;
        y1 = RungeCoot(x0, y0);
        y2 = RungeCoot(x1, y1);
        y3 = RungeCoot(x2, y2);
        
        y4 = y3 + h/24 * (55*func(x3,y3)-59*func(x2,y2)+37*func(x1,y1)-9*func(x0,y0));
        y4_prec = y3 + h/24 * (9*func(x4,y4)+19*func(x3,y3)-5*func(x2,y2)+func(x1,y1));
        
        y5 = y4_prec + h/24 * (55*func(x4,y4_prec)-59*func(x3,y3)+37*func(x2,y2)-9*func(x1,y1));
        y5_prec = y4_prec + h/24 * (9*func(x5,y5)+19*func(x4,y4_prec)-5*func(x3,y3)+func(x2,y2));
        
        System.out.println("y4 = " + y4_prec + " (при x = 0.4)");
        System.out.println("y5 = " + y5_prec + " (при x = 0.5)");
        //ответ: 0.38256, 0.46770
    }
}
