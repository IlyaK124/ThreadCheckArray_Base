# ThreadCheckArray

ThreadCheckArray is a Java command-line project that searches for a subset of integer input values whose sum equals a target. It uses two worker threads to explore separate branches of a recursive subset-sum search.

## How the algorithm works

`TestThreadCheckArray` reads the number of input values, their values, and the target sum. It creates a `SharedData` instance, starts two `ThreadCheckArray` workers, and calls `join()` on both threads before displaying the result.

The recursive method `rec(n, b)` considers two possibilities for each value: include it (subtracting that value from the remaining target) or exclude it (leaving the target unchanged). The workers split the initial search around the last input element:

- **`thread1`** starts with the last element included.
- **`thread2`** starts with the last element excluded.

Each worker keeps a primitive `boolean[] winArray` marking which input positions are selected. The shared `SharedData` object holds the input values, target sum, a match-found flag, and the published result array. The workers use `synchronized (sd)` blocks when checking or updating shared result state. After both threads finish, the main thread prints the selected positions as a `C` row of `1` and `0` values, or prints `Sorry` if no solution was found.

**Implementation note:** The algorithm assumes at least one input value. When multiple valid subsets exist, the particular result may depend on thread scheduling. The original recursive search and synchronization approach was preserved during the requested data-structure refactoring; the refactoring was not intended to redesign the concurrency logic.

## Data-structure refactoring

The assignment replaces the input variable or field named `array`, originally an `int[]`, with an `ArrayList<Integer>`:

- Input values are added using `array.add(value)` instead of assignment by index.
- Indexed access uses `array.get(index)` instead of `array[index]`.
- Collection size is obtained using `array.size()` instead of `array.length`.
- Result arrays, including `winArray`, remain primitive `boolean[]` arrays. Where initialized from the input size, they use `new boolean[array.size()]`.

The completed refactor requires compatible `ArrayList<Integer>` types across `TestThreadCheckArray`, `SharedData`, and `ThreadCheckArray`.

## Javadoc documentation

The refactored code includes Javadoc describing classes, constructors, methods, parameters, and return values. Generic types are written safely in Javadoc, for example `{@code ArrayList<Integer>}`, to avoid parsing errors.

After the branches are integrated and the project builds, Javadoc can be generated from the project root:

```powershell
javadoc -d docs src/SharedData.java src/ThreadCheckArray.java src/TestThreadCheckArray.java
```

Open `docs/index.html` in a browser to view the generated documentation.

## Requirements and running

The course environment uses **BellSoft Liberica Full JDK 25 LTS**. Configure that JDK as the Project SDK in IntelliJ IDEA.

**Integration status:** The assignment intentionally separates refactoring work across branches. On `master`, `SharedData.java` and `ThreadCheckArray.java` have been refactored, while the matching changes to `TestThreadCheckArray.java` are on Student B's feature branch. Consequently, compilation may fail before the branches are merged and type mismatches are resolved. The following instructions apply to the **integrated, buildable project**, not necessarily to either branch before merging.

From the project root, compile and run:

```powershell
javac -d out src/SharedData.java src/ThreadCheckArray.java src/TestThreadCheckArray.java
java -cp out TestThreadCheckArray
```

The program prompts for the array size, input integers, and target sum. For example, enter size `4`, values `2`, `3`, `7`, `8`, and target `10` to search for a matching subset.

## Authors and Git collaboration

- **Ilya Kaganovski — Student A:** Refactored `SharedData.java` and `ThreadCheckArray.java` on the `master` branch, and prepared the project README.
- **Oriyan Eliguev — Student B:** Refactored `TestThreadCheckArray.java` and `ThreadCheckArray.java` on the `feature/arraylist-refactor-b` branch.

Both students intentionally edited `ThreadCheckArray.java` on separate branches to practice collaboration and merge-conflict resolution. Student A's changes are pushed to `master`, and Student B's changes are pushed to `feature/arraylist-refactor-b`. The branches are to be integrated in a later assignment step; this README does **not** claim that the merge has already happened.

**Repository:** [IlyaK124/ThreadCheckArray_Base](https://github.com/IlyaK124/ThreadCheckArray_Base)

## License

**Braude College Academic License**

This license name is specified by the course assignment. The full license text and its permissions or restrictions have not been provided here; consult the applicable Braude College or course-provided terms before assuming specific usage rights.
