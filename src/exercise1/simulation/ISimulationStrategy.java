package exercise1.simulation;

import exercise1.model.Grid;

import java.util.List;

public interface ISimulationStrategy {

    /**
     * Returns a {@link List} of Grids. Each grid represents one step of the simulation.
     *
     * @param grid the initial grid.
     */
    List<Grid> getSimulationSteps(Grid grid);
}
