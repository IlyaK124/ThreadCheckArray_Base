import java.util.ArrayList;

/**
 * Stores the input values and shared state used by worker threads
 * during the subset search.
 *
 * <p>The input values are stored in an {@code ArrayList<Integer>},
 * while the result array remains a {@code boolean[]}.
 */
public class SharedData {

    /** The input values used by the worker threads. */
    private final ArrayList<Integer> array;

    /** Indicates which input positions belong to the matching subset. */
    private boolean[] winArray;

    /** Indicates whether a worker thread has found a matching subset. */
    private boolean flag;

    /** The target value for the subset search. */
    private final int b;

    /**
     * Initializes the shared data.
     *
     * @param array the input values as an {@code ArrayList<Integer>}
     * @param b the target value
     */
    public SharedData(ArrayList<Integer> array, int b) {
        this.array = array;
        this.b = b;
        this.winArray = new boolean[array.size()];
        this.flag = false;
    }

    /**
     * Returns the result array.
     *
     * @return the boolean array indicating the selected input positions
     */
    public boolean[] getWinArray() {
        return winArray;
    }

    /**
     * Updates the result array.
     *
     * @param winArray the result array to store
     */
    public void setWinArray(boolean[] winArray) {
        this.winArray = winArray;
    }

    /**
     * Returns the input values.
     *
     * @return the input values as an {@code ArrayList<Integer>}
     */
    public ArrayList<Integer> getArray() {
        return array;
    }

    /**
     * Returns the target value.
     *
     * @return the target value
     */
    public int getB() {
        return b;
    }

    /**
     * Checks whether a matching subset has been found.
     *
     * @return {@code true} if a matching subset was found,
     *         {@code false} otherwise
     */
    public boolean getFlag() {
        return flag;
    }

    /**
     * Updates the search result flag.
     *
     * @param flag {@code true} if a matching subset was found,
     *             {@code false} otherwise
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
