import java.util.ArrayList;

/**
 * A worker thread responsible for searching for a subset of integers
 * whose sum matches a specified target.
 *
 * <p>The input collection is represented by an
 * {@code ArrayList<Integer>}, while the result is maintained
 * using a primitive {@code boolean[]} array.
 *
 * <p>Synchronization through {@link SharedData} allows multiple
 * threads to communicate their search results.
 */
public class ThreadCheckArray implements Runnable {

    /** Indicates whether this worker has found a matching subset. */
    private boolean flag;

    /** Marks the elements that belong to the matching subset. */
    private boolean[] winArray;

    /** Shared object used to communicate between worker threads. */
    SharedData sd;

    /** Collection containing the integer values to be examined. */
    ArrayList<Integer> array;

    /** Target sum for the subset search. */
    int b;

    /**
     * Initializes a worker thread using the supplied shared data.
     *
     * <p>The input collection and target are retrieved within
     * a synchronized block.
     *
     * <p>The boolean result array is dynamically allocated using
     * the size of the {@code ArrayList<Integer>}.
     *
     * @param sd shared data containing the input collection
     *           and target value
     */
    public ThreadCheckArray(SharedData sd) {
        this.sd = sd;

        synchronized (sd) {
            array = sd.getArray();
            b = sd.getB();
        }

        winArray = new boolean[array.size()];
    }

    /**
     * Recursively checks whether a subset of the first n elements
     * can satisfy the remaining target sum.
     *
     * <p>Each recursive step examines the possibilities of
     * including or excluding the current element.
     *
     * <p>The search checks the shared flag to detect whether
     * another worker has already found a solution.
     *
     * @param n number of elements currently considered
     * @param b remaining sum to be matched
     */
    void rec(int n, int b) {

        synchronized (sd) {
            if (sd.getFlag()) return;
        }

        if (n == 1) {
            if (b == 0 || b == array.get(n - 1)) {
                flag = true;

                synchronized (sd) {
                    sd.setFlag(true);
                }
            }

            if (b == array.get(n - 1))
                winArray[n - 1] = true;

            return;
        }

        rec(n - 1, b - array.get(n - 1));

        if (flag)
            winArray[n - 1] = true;

        synchronized (sd) {
            if (sd.getFlag()) return;
        }

        rec(n - 1, b);
    }

    /**
     * Executes the subset search for the current worker.
     *
     * <p>The thread named {@code thread1} starts by considering
     * the last element as included in the subset, while the
     * other worker starts without that element.
     *
     * <p>When a matching subset is found, the worker updates
     * the shared result through {@link SharedData}.
     *
     * <p>The input is accessed through {@code ArrayList<Integer>}
     * operations, while subset membership is stored in
     * a primitive {@code boolean[]} array.
     */
    @Override
    public void run() {

        if (array.size() != 1) {
            if (Thread.currentThread().getName().equals("thread1")) {
                rec(array.size() - 1,
                        b - array.get(array.size() - 1));
            } else {
                rec(array.size() - 1, b);
            }
        }

        if (array.size() == 1) {
            if (b == array.get(0) && !flag) {
                winArray[0] = true;
                flag = true;

                synchronized (sd) {
                    sd.setFlag(true);
                }
            }
        }

        if (flag) {
            if (Thread.currentThread().getName().equals("thread1"))
                winArray[array.size() - 1] = true;

            synchronized (sd) {
                sd.setWinArray(winArray);
            }
        }
    }
}