package it.unibs.alg.pathfinding;

import java.util.Arrays;

public class IntArrayState {
	
	private int[] state;

    public IntArrayState(int[] state) {
        this.state = state;
    }

    /*
     * Override array int equals method, making two int arrays equal 
     * when they contain the same elements
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntArrayState that = (IntArrayState) o;
        return Arrays.equals(state, that.state);
    }

    /*
     * Override array int hashCode method
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(state);
    }
    
    public int getCell() {
    	return state[0];
    }
    
    public int getIstant() {
    	return state[1];
    }
    
}
