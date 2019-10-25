package exercise1.simulation;

import exercise1.model.CellStateObjects.Obstacle;
import exercise1.model.CellStateObjects.Pedestrian;
import exercise1.model.CellStateObjects.StateSpace;
import exercise1.model.CellStateObjects.Target;
import javafx.util.Pair;
import exercise1.model.Direction;
import exercise1.model.Grid;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.lang.Double.min;

public class CostBasedStrategy {

    private double[][] costMatrix;
    private Grid grid;

    public CostBasedStrategy(Grid grid) {
        this.grid = grid;
        this.costMatrix = new double[grid.getColumnCount()][grid.getRowCount()];
        updateCostMatrix();
    }

    public void updateCostMatrix() {
        this.costMatrix = calculateDijkstraCostMatrix();
        this.addPedestrianCost();
        this.addObstacleCost();
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
    
    
    private double[][] calculateDijkstraCostMatrix() {
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
            for (int j=0;j<costMatrix.length;j++){
            	for (int k=0;k<costMatrix[0].length;k++){
            		costMatrix[j][k] += targetCostMatrix[j][k];
            	}
            }
        }
        for (Obstacle o : grid.getObstacles()){
        	costMatrix[o.x][o.y] = Double.MAX_VALUE;
        }
        return costMatrix;
    }
    

    private void addObstacleCost() {
    	List<Pedestrian> pedestrians = grid.getPedestrians();
    	for (Pedestrian p : pedestrians){
			costMatrix[p.x][p.y] += Double.MAX_VALUE;
    	}
    }

    private void addPedestrianCost() {
    	List<Pedestrian> pedestrians = grid.getPedestrians();
    	for (Pedestrian p : pedestrians){
			costMatrix[p.x][p.y] += 100;
    	}
    }

    /** Performs one step of the simulation. */
    public void nextStep() {
        grid.getPedestrians().forEach(p -> {
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
}
