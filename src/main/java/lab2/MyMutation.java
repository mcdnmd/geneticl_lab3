package lab2;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyMutation implements EvolutionaryOperator<double[]> {

    // Вероятность мутации
    private static final double MUTATION_PROBABILITY = 0.01;
    private static final MyRandomGenerator randomGenerator = new MyRandomGenerator();

    public List<double[]> apply(List<double[]> population, Random random) {
        // initial population
        // need to change individuals, but not their number!

        for (double[] individual : population) {
            mutate(individual);
        }

        //result population
        return population;
    }

    private static void mutate(double[] individual) {
        Random random = new Random();
        for (int i = 0; i < individual.length; i++) {
            // Генерация случайного числа для определения, произойдет ли мутация для данного гена
            if (random.nextDouble() < MUTATION_PROBABILITY) {
                // Замена значения гена случайным числом при помощи генератора случайных чисел
                individual[i] = randomGenerator.generateRandomDouble();
            }
        }
    }
}
