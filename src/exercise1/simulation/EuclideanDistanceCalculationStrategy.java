package exercise1.simulation;

import exercise1.model.CellStateObjects.Target;
import exercise1.model.Grid;

import java.util.List;

public class EuclideanDistanceCalculationStrategy implements IDistanceCalculationStrategy {
    @Override
    public double[][] calculateCostMatrix(Grid grid) {
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
}
