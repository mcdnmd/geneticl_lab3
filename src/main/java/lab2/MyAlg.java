package lab2;

import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MyAlg {

    public static int dimension = 2;
    public static int populationSize = 2;
    public static int generations = 10;

    public static Config config;

    public static void main(String[] args) {
        config = new Config(
                0.35,
                0.3,
                0.9,
                0.01
        );

        List<Double> fits = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            fits.add(evolve());
        }
        System.out.println("Mean fit: " + mean(fits));
    }

    public static double evolve() {
        // random
        Random random = new Random();

        // generation of solutions
        CandidateFactory<double[]> factory = new MyFactory(dimension);

        // Создание операторов и пайплайна эволюции
        ArrayList<EvolutionaryOperator<double[]>> operators = createOperators();
        EvolutionPipeline<double[]> pipeline = new EvolutionPipeline<double[]>(operators);

        // Selection operator
        SelectionStrategy<Object> selection = new RouletteWheelSelection();

        // Fitness function
        FitnessEvaluator<double[]> evaluator = new FitnessFunction(dimension);

        EvolutionEngine<double[]> algorithm = new SteadyStateEvolutionEngine<double[]>(
                factory, pipeline, evaluator, selection, populationSize, false, random);

        return execute(algorithm);
    }

    private static ArrayList<EvolutionaryOperator<double[]>> createOperators() {
        // Создание операторов над множеством особей в популяции
        ArrayList<EvolutionaryOperator<double[]>> operators = new ArrayList<EvolutionaryOperator<double[]>>();
        operators.add(
                new MyCrossover(config.CROSSOVER_ALPHA)
        ); // Crossover
        operators.add(
                new MyMutation(
                        config.MUTATION_PROBABILITY,
                        config.MUTATION_DECAY_RATE,
                        config.MIN_MUTATION_PROBABILITY
                )
        ); // Mutation
        return operators;
    }

    public static double execute(EvolutionEngine<double[]> algorithm) {
        //  Запуска алгоритма

        final double[] globalBestFit = {0};
        algorithm.addEvolutionObserver(new EvolutionObserver() {
            public void populationUpdate(PopulationData populationData) {
                double bestFit = populationData.getBestCandidateFitness();
                System.out.println("Generation " + populationData.getGenerationNumber() + ": " + bestFit);
                System.out.println("\tBest solution = " + Arrays.toString((double[])populationData.getBestCandidate()));
                System.out.println("\tPop size = " + populationData.getPopulationSize());

                if (bestFit > globalBestFit[0]) {
                    globalBestFit[0] = bestFit;
                }
            }
        });

        TerminationCondition terminate = new GenerationCount(generations);
        algorithm.evolve(populationSize, 1, terminate);

        return globalBestFit[0];
    }

    public static double mean(List<Double> values) {
        double sum = 0.0;
        for (Double value : values) {
            sum += value;
        }

        return sum / values.size();
    }
}
