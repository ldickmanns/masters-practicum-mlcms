package exercise1.simulation;

import exercise1.model.CellStateObjects.Obstacle;
import exercise1.model.CellStateObjects.Pedestrian;
import exercise1.model.Direction;
import exercise1.model.Grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.lang.Double.min;

public class CostCalculation {

    private double[][] costMatrix;
    private Grid grid;
    private IDistanceCalculationStrategy dcs;
    private List<Pedestrian> pedestrians = new ArrayList<>();

    public CostCalculation(Grid grid, IDistanceCalculationStrategy dcs) {
        this.costMatrix = new double[grid.getColumnCount()][grid.getRowCount()];
        this.grid = grid;
        this.dcs = dcs;
        // The state is read in from left to right. Therefore, pedestrians are also added from left to right
        // when loading a scenario. But as for most scenarios pedestrians move from left to right, it makes
        // sense to start with moving the rightmost pedestrians.
        this.pedestrians.addAll(grid.getPedestrians());
        Collections.reverse(this.pedestrians);
        updateCostMatrix();
    }

    public void nextStep() {
        pedestrians.forEach(p -> {
            // Check dynamic cost matrix for the next move
            double up = costMatrix[p.x][p.y - 1];
            double right = costMatrix[p.x + 1][p.y];
            double down = costMatrix[p.x][p.y +1];
            double left = costMatrix[p.x -1][p.y];
            double min = min(min(up, right), min(down, left));
            if (costMatrix[p.x][p.y] - 100 < min) return;

            // Decide for the direction to move to based on the cost matrix
            // In case of a tie (two or more possible moves are equal to the minimun cost)
            // We just pick a random move from Set of minima
            List<String> decisionList = new ArrayList<String>();
            if (min == up) decisionList.add("UP");
            if (min == right) decisionList.add("RIGHT");
            if (min == down) decisionList.add("DOWN");
            if (min == left) decisionList.add("LEFT");
            String decision = (String) decisionList.toArray()[new Random().nextInt(decisionList.size())];

            switch (decision) {
                case "UP":
                    grid.movePedestrian(p.x, p.y, Direction.UP);
                    break;
                case "RIGHT":
                    grid.movePedestrian(p.x, p.y, Direction.RIGHT);
                    break;
                case "DOWN":
                    grid.movePedestrian(p.x, p.y, Direction.DOWN);
                    break;
                case "LEFT":
                    grid.movePedestrian(p.x, p.y, Direction.LEFT);
                    break;
            }
            updateCostMatrix();
        });
    }

    private void updateCostMatrix() {
        this.costMatrix = dcs.calculateCostMatrix(grid);
        this.addPedestrianCost();
        this.addObstacleCost();
    }

    private void addObstacleCost() {
        List<Obstacle> obstacles = grid.getObstacles();
        for (Obstacle o : obstacles){
            costMatrix[o.x][o.y] += Double.MAX_VALUE;
        }
    }

    private void addPedestrianCost() {
        List<Pedestrian> pedestrians = grid.getPedestrians();
        for (Pedestrian p : pedestrians){
            costMatrix[p.x][p.y] += 100;
        }
    }
}
