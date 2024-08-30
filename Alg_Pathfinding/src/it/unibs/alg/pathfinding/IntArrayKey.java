package it.unibs.alg.pathfinding;

import java.util.Arrays;

public class IntArrayKey {
	
	private final int[] key;

    public IntArrayKey(int[] key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntArrayKey that = (IntArrayKey) o;
        return Arrays.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(key);
    }
    
    public int[] getKey() {
    	return key;
    }
    
}
