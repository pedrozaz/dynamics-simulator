package io.github.pedrozaz.dynamics.domain.service;

import io.github.pedrozaz.dynamics.domain.model.CarSetup;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Service responsible for the biological operations: Selection, Crossover and Mutation.
 * It transforms Generation N into Generation N+1
 */
@Service
public class GeneticEvolutionService {

    private final RandomGenerator random = RandomGenerator.getDefault();

    private static final double MUTATION_RATE = 0.10; // 10% chance of gene mutating
    private static final double MUTATION_STRENGTH = 0.05; // 5% variance applied
    private static final int TOURNAMENT_SIZE = 5; // Number of objects
    private static final int ELITISM_COUNT = 2; // Number of unchanged for next generation


    /**
     * Creates the next generation of vehicle setups based on the performance of the current one.
     *
     * @param currentPopulation List of car setups from the current generation.
     * @param fitnessScores     Corresponding fitness scores.
     * @return A new list of CarSetup objects.
     */
    public List<CarSetup> evolve(List<CarSetup> currentPopulation, List<Double> fitnessScores) {
        List<CarSetup> nextGen = new ArrayList<>();

        // 1. Elitism
        List<Integer> sortedIndices = new ArrayList<>();
        for (int i = 0; i < currentPopulation.size(); i++) sortedIndices.add(i);

        sortedIndices.sort(Comparator.comparingDouble(fitnessScores::get).reversed());

        for (int i = 0; i < ELITISM_COUNT; i++) {
            int bestIndex = sortedIndices.get(i);
            nextGen.add(currentPopulation.get(bestIndex));
        }

        // 2. Breeding Loop
        while (nextGen.size() < currentPopulation.size()) {
            CarSetup parent1 = tournamentSelection(currentPopulation, fitnessScores);
            CarSetup parent2 = tournamentSelection(currentPopulation, fitnessScores);

            CarSetup child = uniformCrossover(parent1, parent2);

            child = gaussianMutation(child);

            nextGen.add(child);
        }
        return nextGen;
    }

    /**
     * Tournament Selection:
     * Randomly picks `N` individuals and returns the one with the best fitness.
     */
    private CarSetup tournamentSelection(List<CarSetup> p, List<Double> f) {
        int bestIndex = -1;
        double bestFitness = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            int randomIndex = random.nextInt(p.size());
            if (f.get(randomIndex) > bestFitness) {
                bestFitness = f.get(randomIndex);
                bestIndex = randomIndex;
            }
        }
        return p.get(bestIndex);
    }

    /**
     * Uniform Crossover:
     * For each gene, flip a coin to decide if it comes from Parent A or Parent B
     */
    private CarSetup uniformCrossover(CarSetup p1, CarSetup p2) {
        return new CarSetup(
                random.nextBoolean() ? p1.frontSpringStiffness() : p2.frontSpringStiffness(),
                random.nextBoolean() ? p1.rearSpringStiffness() : p2.rearSpringStiffness(),
                random.nextBoolean() ? p1.frontDamping() : p2.frontDamping(),
                random.nextBoolean() ? p1.rearDamping() : p2.rearDamping(),
                random.nextBoolean() ? p1.frontWingAngle() : p2.frontWingAngle(),
                random.nextBoolean() ? p1.rearWingAngle() : p2.rearWingAngle(),
                random.nextBoolean() ? p1.finalGearRatio() : p2.finalGearRatio(),
                random.nextBoolean() ? p1.brakeBalance() : p2.brakeBalance()
        );
    }


    /**
     * Gaussian Mutation:
     * Adds a small value from a Normal Distribution (Bell Curve) to the gene.
     * This allows for fine-tuning rather than completely random changes.
     */
    private CarSetup gaussianMutation(CarSetup c) {
        if (random.nextDouble() > MUTATION_RATE) return c;

        return new CarSetup(
                mutateGene(c.frontSpringStiffness()),
                mutateGene(c.rearSpringStiffness()),
                mutateGene(c.frontDamping()),
                mutateGene(c.rearDamping()),
                mutateGene(c.frontWingAngle()),
                mutateGene(c.rearWingAngle()),
                mutateGene(c.finalGearRatio()),
                Math.clamp(mutateGene(c.brakeBalance()), 0.0, 1.0)
        );
    }

    private double mutateGene(double o) {
        // New Value = Old Value * (1 + GaussianNoise * Strength)
        double factor = 1.0 + (random.nextGaussian() * MUTATION_STRENGTH);
        return Math.max(0.1, o * factor);
    }
}
