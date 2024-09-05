package it.unibs.alg.pathfinding;

import java.util.Arrays;

public class IntArrayState {
	
	private int[] state;

    public IntArrayState(int[] state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntArrayState that = (IntArrayState) o;
        return Arrays.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(state);
    }
    
    public void setState(int[] newState) {
    	state = newState;
    }
    
    public int getCell() {
    	return state[0];
    }
    
    public int getIstant() {
    	return state[1];
    }
    
}
