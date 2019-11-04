package exercise1.simulation;

import exercise1.model.CellStateObjects.Obstacle;
import exercise1.model.CellStateObjects.StateSpace;
import exercise1.model.CellStateObjects.Target;
import exercise1.model.Grid;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class DijkstraDistanceCalculationStrategy implements IDistanceCalculationStrategy {
    @Override
    public double[][] calculateCostMatrix(Grid grid) {
        List<Target> targets = grid.getTargets();
        double[][] costMatrix = new double[grid.getColumnCount()][grid.getRowCount()];
        for (int i = 0; i < targets.size(); ++i) {
            Target t = targets.get(i);
            double[][] targetCostMatrix = new double[grid.getColumnCount()][grid.getRowCount()];
            boolean[][] trackingMatrix = new boolean[grid.getColumnCount()][grid.getRowCount()];
            targetCostMatrix[t.x][t.y] = 0;
            trackingMatrix[t.x][t.y] = true;
            List<Pair<Integer,Integer>> queue = new LinkedList<>();
            queue.add(new Pair<Integer, Integer>(t.x,t.y));
            while(queue.size()>0){
                Pair<Integer,Integer> element = queue.get(0);
                queue.remove(0);
                int x = element.getKey();
                int y = element.getValue();
                double value = targetCostMatrix[x][y];
                StateSpace[][] state = grid.getState();
                if (x+1<targetCostMatrix.length && !trackingMatrix[x+1][y] && state[x+1][y] != StateSpace.O){
                    targetCostMatrix[x+1][y] = value + 1;
                    trackingMatrix[x+1][y] = true;
                    queue.add(new Pair<Integer, Integer>(x+1,y));
                }
                if (x-1>=0 && !trackingMatrix[x-1][y] && state[x-1][y] != StateSpace.O){
                    targetCostMatrix[x-1][y] = value + 1;
                    trackingMatrix[x-1][y] = true;
                    queue.add(new Pair<Integer, Integer>(x-1,y));
                }
                if (y+1<targetCostMatrix[0].length && !trackingMatrix[x][y+1] && state[x][y+1] != StateSpace.O){
                    targetCostMatrix[x][y+1] = value + 1;
                    trackingMatrix[x][y+1] = true;
                    queue.add(new Pair<Integer, Integer>(x,y+1));
                }
                if (y-1>=0 && !trackingMatrix[x][y-1] && state[x][y-1] != StateSpace.O){
                    targetCostMatrix[x][y-1] = value + 1;
                    trackingMatrix[x][y-1] = true;
                    queue.add(new Pair<Integer, Integer>(x,y-1));
                }
            }
            if (i == 0) {
                costMatrix = targetCostMatrix.clone();
                continue;
            }
            for (int j=0;j<costMatrix.length;j++){
                for (int k=0;k<costMatrix[0].length;k++){
                    costMatrix[j][k] = Math.min(costMatrix[j][k], targetCostMatrix[j][k]);
                }
            }
        }
        for (Obstacle o : grid.getObstacles()){
            costMatrix[o.x][o.y] = Double.MAX_VALUE;
        }
        return costMatrix;
    }
}
