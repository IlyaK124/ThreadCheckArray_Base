import java.util.ArrayList;

/**
 * Implements a worker thread that searches for a subset of integers
 * whose sum equals a specified target value.
 *
 * <p>The input values are stored in an {@code ArrayList<Integer>},
 * while the selected subset is represented by a {@code boolean[]}.
 *
 * <p>The worker communicates with other threads through a shared
 * {@link SharedData} object. Synchronization is used when accessing
 * the shared result state.
 */
public class ThreadCheckArray implements Runnable {

    /** Indicates whether this worker has found a matching subset. */
    private boolean flag;

    /** Indicates which input elements belong to the matching subset. */
    private boolean[] winArray;

    /** Shared data used for communication between worker threads. */
    SharedData sd;

    /** Input values used in the subset search. */
    ArrayList<Integer> array;

    /** Target sum that the worker attempts to find. */
    int b;

    /**
     * Constructs a worker using the provided shared data.
     *
     * <p>The input values and target are retrieved from the shared
     * object inside a synchronized block.
     *
     * <p>The result array is initialized using the size of the
     * {@code ArrayList<Integer>}.
     *
     * @param sd the shared data containing input values and target
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
     * Recursively searches for a subset whose sum equals the target.
     *
     * <p>For each element, the algorithm explores two possibilities:
     * including the element or excluding it from the subset.
     *
     * <p>The search may terminate early if another worker has
     * already found a matching subset.
     *
     * @param n the number of elements currently considered
     * @param b the remaining target sum
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

            if (b == array.get(n - 1)) {
                winArray[n - 1] = true;
            }

            return;
        }

        rec(n - 1, b - array.get(n - 1));

        if (flag) {
            winArray[n - 1] = true;
        }

        synchronized (sd) {
            if (sd.getFlag()) return;
        }

        rec(n - 1, b);
    }

    /**
     * Executes the subset search for the current worker thread.
     *
     * <p>When more than one input element exists, the thread named
     * {@code thread1} searches using the last element as part of
     * the initial subset, while the other thread searches without it.
     *
     * <p>If a matching subset is found, the result is stored in
     * the shared {@link SharedData} object.
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
            if (Thread.currentThread().getName().equals("thread1")) {
                winArray[array.size() - 1] = true;
            }

            synchronized (sd) {
                sd.setWinArray(winArray);
            }
        }
    }
}
