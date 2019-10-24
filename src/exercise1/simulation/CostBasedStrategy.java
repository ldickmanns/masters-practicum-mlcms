package exercise1.simulation;

import exercise1.model.CellStateObjects.Target;
import exercise1.model.Direction;
import exercise1.model.Grid;

import java.util.List;

import static java.lang.Double.min;

public class CostBasedStrategy {

    private double[][] costMatrix;
    private Grid grid;

    public CostBasedStrategy(Grid grid) {
        this.grid = grid;
        this.costMatrix = new double[grid.getColumnCount()][grid.getRowCount()];
        calculateCostMatrix();
    }

    private void calculateCostMatrix() {
        this.costMatrix = calculateEucledianCostMatrix();
    }

    private double[][] calculateEucledianCostMatrix() {
        List<Target> targets = grid.getTargets();
        double[][] costMatrix = new double[grid.getColumnCount()][grid.getRowCount()];
        for (int i = 0; i < targets.size(); ++i) {
            Target t = targets.get(i);
            for (int x = 0; x < costMatrix.length; x++) {
                for (int y = 0; y < costMatrix[0].length; y++) {
                    double dist = Math.sqrt(Math.pow(x - t.x, 2) + Math.pow(y - t.y, 2));
                    if (i == 0) costMatrix[x][y] = dist;
                    if (costMatrix[x][y] > dist) costMatrix[x][y] = dist;
                }
            }
        }
        return costMatrix;
    }

    /** Performs one step of the simulation. */
    public void nextStep() {
        grid.getPedestrians().forEach(p -> {
            double up = costMatrix[p.x][p.y - 1];
            double right = costMatrix[p.x + 1][p.y];
            double down = costMatrix[p.x][p.y +1];
            double left = costMatrix[p.x -1][p.y];
            double min = min(min(up, right), min(down, left));
            // Do nothing if distance increases when moving.
            //if (costMatrix[p.x][p.y] < min) return;
            if (min == up) grid.movePedestrian(p.x, p.y, Direction.UP);
            if (min == right) grid.movePedestrian(p.x, p.y, Direction.RIGHT);
            if (min == down) grid.movePedestrian(p.x, p.y, Direction.DOWN);
            if (min == left) grid.movePedestrian(p.x, p.y, Direction.LEFT);
        });
    }
}
