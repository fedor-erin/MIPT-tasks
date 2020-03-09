package ru.ncedu.java.tasks;
/**
 * ЗАДАНИЕ
 * Вот как должна выглядеть реализация данного интерфейса:
 * public class ControlFlowStatements2Impl implements ControlFlowStatements2 {  }
 * Метод main не тестируется, но для себя в main вы можете проверить, что возвращают ваши методы, например: 
 * ControlFlowStatements2 object = new ControlFlowStatements2Impl();
 * System.out.println(object.decodeMark(4));
 */
public interface ControlFlowStatements2{
	/**
	 * Для данного целого x найти значение следующей функции f, принимающей значения целого типа:<br/>
	 * <pre>
	 *        | 2*x, если x<-2 или x>2,
	 *  f(x)= | 
	 *        | -3*x, в противном случае.
	 * </pre>
	 * @return значение f.
	 */
	int getFunctionValue(int x);
	/**
	 * Дано целое число mark.
	 * Вернуть строку - описание оценки, соответствующей числу mark:<br/>
	 * 1 — Fail, 2 — Poor, 3 — Satisfactory, 4 — Good, 5 — Excellent.<br/> 
	 * Если mark не лежит в диапазоне 1–5, то вывести строку "Error".
	 * @param mark
	 * @return строковое представление mark
	 */
	String decodeMark(int mark);
	
	/**
	 * Создать двумерный массив, содержащий 5x8 вещественных элементов,
	 * и присвоить каждому элементу следующее значение, зависящее от его индексов:
	 * array[i][j] = (i в степени 4) минус корень квадратный из j.<br/>
	 * Для возведения в степень и взятия корня рекомендуется использовать 
	 * статические методы класса {@link Math}.
	 * @return массив
	 */
	double[][] initArray();
	/**
	* Найти максимальный элемент заданного двумерного массива.
	* @param array массив, содержащий как минимум один элемент
	* @return максимальный элемент массива array.
	*/
	double getMaxValue(double[][] array);
	
	/**
	 * Спортсмен начал тренировки, пробежав в первый день 10 км.
	 * Каждый следующий день он увеличивал длину пробега на P процентов от
	 * пробега предыдущего дня.<br/>
	 * По заданному P определить, через сколько дней суммарный пробег спортсмена за все дни
	 * превысит 200 км (целое число дней), а также сам суммарный пробег (вещественное число).
	 * @param P процент увеличения пробега в день
	 * @return информация о пробеге (в виде экземпляра класса {@link Sportsman}) после наступления вышеуказанного условия
	 */
	Sportsman calculateSportsman(float P);
	/**
	 * Вспомогательный тип для метода {@link ControlFlowStatements2#calculateSportsman(float)}.<br/>
	 * Примечание: Специальный тип здесь необходим, поскольку метод должен вернуть ДВА значения,
	 * в то время как через аргументы метода значения просто так вернуть нельзя 
	 * (передача параметров в методы в Java - только по значению, а не по ссылке).<br/>
	 * ВОПРОС: как можно было бы обойтись без специального типа, но вернуть два значения в calculateSportsman?
	 * (есть несколько способов, возможных с точки зрения синтаксиса, хотя и все они нехороши с точки зрения ООП)
	 */
	public static class Sportsman{
		private int trainingDays = 0;
		private float totalDistance;
		/**
		 * Количество дней, сколько тренировался спортсмен.
		 */
		public final int getTrainingDays() {
			return trainingDays;
		}
		/**
		 * Суммарный пробег спортсмена по истечению {@link #trainingDays} дней.
		 */
		public float getTotalDistance() {
			return totalDistance;
		}
		/**
		 * Изменяет объект по итогам одного прошедшего дня
		 * @param distance пробег в этот день
		 */
		public void addDay(float distance) {
			this.trainingDays ++;
			this.totalDistance += distance;
		}
		
		@Override
		public String toString() {
			return trainingDays+" days: "+totalDistance+" km";
		}
	}
}