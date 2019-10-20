package exercise1.model;

import exercise1.simulation.ISimulationStrategy;

import java.util.List;

public class System {

    private Grid initialState;
    private List<Grid> simulationSteps;
    private ISimulationStrategy simulationStrategy;

    public System(ISimulationStrategy simulationStrategy) {
        this.simulationStrategy = simulationStrategy;
        this.defaultInitialization();
    }

    private void defaultInitialization() {
        this.initialState = new Grid(50, 50);
        this.initialState.addPedestrian(5, 25);
        this.initialState.addTarget(25, 25);
    }


    public List<Grid> simulate() {
        if (simulationSteps == null) {
            simulationSteps = simulationStrategy.getSimulationSteps(initialState);
        }
        return simulationSteps;
    }

    public Grid getInitialState() {
        return initialState;
    }

    public void setInitialState(Grid initialState) {
        this.initialState = initialState;
    }
}
