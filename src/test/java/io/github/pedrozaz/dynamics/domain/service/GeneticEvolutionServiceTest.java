package io.github.pedrozaz.dynamics.domain.service;

import io.github.pedrozaz.dynamics.domain.model.CarSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeneticEvolutionServiceTest {

    private final GeneticEvolutionService evoService = new GeneticEvolutionService();

    @Test
    @DisplayName("Evolution should preserve population size and create valid objects")
    void testEvolutionIntegrity() {
        List<CarSetup> population = new ArrayList<>();
        List<Double> fitness = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            population.add(CarSetup.random());
            fitness.add((double) i); // Mock fitness
        }

        List<CarSetup> nextGen = evoService.evolve(population, fitness);

        assertEquals(10, nextGen.size(), "Population size should remain constant");
        assertNotNull(nextGen.getFirst(), "New individuals should not be null");
    }
}
