package exercise1.simulation;

import exercise1.model.Grid;

public interface IDistanceCalculationStrategy {
    double[][] calculateCostMatrix(Grid grid);
}
