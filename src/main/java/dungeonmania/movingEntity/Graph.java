package dungeonmania.movingEntity;

import java.util.ArrayList;

public class Graph {
    ArrayList<ArrayList<Integer>> grid;
    int[] movementFactor;

    public Graph(ArrayList<ArrayList<Integer>> grid, int[] movementFactor) {
        this.grid = grid;
        this.movementFactor = movementFactor;
    }

    public ArrayList<ArrayList<Integer>> getGrid() {
        return grid;
    }

    // public void setGrid(ArrayList<ArrayList<Integer>> grid) {
    //     this.grid = grid;
    // }

    public int[] getMovementFactorList() {
        return movementFactor;
    }

    // public void setMovementFactor(int[] movementFactor) {
    //     this.movementFactor = movementFactor;
    // }

    public int getMovementFactor(int i) {
        return movementFactor[i];
    }
}
