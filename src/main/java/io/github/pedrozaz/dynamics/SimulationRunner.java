package io.github.pedrozaz.dynamics;

import io.github.pedrozaz.dynamics.domain.model.CarSetup;
import io.github.pedrozaz.dynamics.domain.model.RaceResult;
import io.github.pedrozaz.dynamics.domain.service.GeneticEvolutionService;
import io.github.pedrozaz.dynamics.domain.service.SimulationOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SimulationRunner implements CommandLineRunner {

    private final SimulationOrchestrator orchestrator;
    private final GeneticEvolutionService evolutionService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting Evolution Engine...");

        List<CarSetup> population = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            population.add(CarSetup.random());
        }

        int generations = 10;

        for (int gen = 0; gen < generations; gen++) {
            long start = System.currentTimeMillis();

            List<RaceResult> results = orchestrator.runGeneration(population);

            List<Double> fitnessScores = results.stream()
                    .map(RaceResult::calculateFitness)
                    .toList();

            RaceResult best = results.stream()
                    .min((r1, r2) -> Double.compare(r1.finishTime(), r2.finishTime()))
                    .orElseThrow();

            System.out.printf("Gen %d | Best Time: %.3fs | Top Speed Setup: Wing=%.1f deg\n",
                    gen, best.finishTime(), best.setup().frontWingAngle());

            if (gen < generations - 1) {
                population = evolutionService.evolve(population, fitnessScores);
            }

            long duration = System.currentTimeMillis() - start;
            System.out.println("Gen computed in " + duration + "ms");
        }

        System.out.println("Evolution Complete.");
    }
}
