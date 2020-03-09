package zeidel;
/**
 * @author Fedor
 */
public class Zeidel {
    public static void main(String[] args) {
        int size = 4;
        // исходная СЛАУ
        double[][] matrix = {
            {24, 2, 4, -9, -9},
            {-6, -27, -8, -6, -76},
            {-4, 8, 19, 6, -79},
            {4, 5, -3, -13, -70}
        };
        // шаг итерации
        int step = 0;
        // точность
        double eps = 0.001;
        // начальный вектор
        double[] previousVector = {0.0, 0.0, 0.0, 0.0};
        while (true) {
            double[] currentVector = new double[size];
            for (int i = 0; i < size; i++) {
                // i-ой координате присвоим значение свободного члена i-ой строки 
                currentVector[i] = matrix[i][size];
                // вычитаем сумму по всем отличным от i-ой неизвестным
                for (int j = 0; j < size; j++) {
                    // при j < i используем уже посчитанные на этой итерации значения
                    if (j < i) {
                        currentVector[i] -= matrix[i][j] * currentVector[j];
                    }
                    // при j > i используем значения с прошлой итерации
                    if (j > i) {
                        currentVector[i] -= matrix[i][j] * previousVector[j];
                    }
                }
                // деление на коэффициент при i-ой неизвестной
                currentVector[i] /= matrix[i][i];
            }
            // текущая погрешность относительно предыдущей итерации
            double error = 0.0;
            for (int i = 0; i < size; i++) {
                error += Math.abs(currentVector[i] - previousVector[i]);
            }
            step += 1;
            // вывод текущих значений
            System.out.print(Integer.toString(step) + " step: ");
            for (int i = 0; i < size; i++) {
                System.out.print(currentVector[i] + " ");
            }
            // перенос строки
            System.out.println();
            // выходим, если необходимая точность достигнута
            if (error < eps) {
                break;
            }
            // следующий шаг итерации
            previousVector = currentVector;
        }
    }
}