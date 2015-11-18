import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/*************************************************************************
 * Name:antonio saco
 * <p/>
 * Compilation: javac Solver.java
 * <p/>
 * Execution: java Solver filename1.txt
 * <p/>
 * Dependencies: In.java MinPQ.java Queue.java Board.java
 * <p/>
 * Description: solve the 8-puzzle problem (and its natural generalizations)
 * using the A* search algorithm.
 * <p/>
 * https://www.cs.princeton.edu/courses/archive/fall12/cos226/assignments/8puzzle.html
 *************************************************************************/
public class Solver {
    private boolean solvable;
    private Stack<Board> solution = null;
    private SearchNode searchNode;

    private static class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private SearchNode previous;

        public SearchNode(Board board, SearchNode previous) {
            this.board = board;
            if (previous != null) {
                this.moves = previous.moves++;
                this.previous = previous;
            } else
                this.moves++;
        }

        @Override
        public int compareTo(SearchNode o) {
            return (this.board.manhattan() + this.moves)
                    - (o.board.manhattan() + o.moves);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        MinPQ<SearchNode> nodes = new MinPQ<>();
        MinPQ<SearchNode> twinNodes = new MinPQ<>();
        Set<Integer> hashes = new HashSet<>();
        Set<Integer> twinHashes = new HashSet<>();
        SearchNode searchTwinNode;
        Stopwatch watch = new Stopwatch();

        // insert the first initial node into the priority queue
        searchNode = new SearchNode(initial, null);
        nodes.insert(searchNode);

        // insert the first twin node into the twin priority queue
        searchTwinNode = new SearchNode(initial.twin(), null);
        twinNodes.insert(searchTwinNode);

        // main loop
        while (true) {

            // delete MIN node
            if (!nodes.isEmpty())
                searchNode = nodes.delMin();
            else
                break;

            // is goal?
            if (searchNode.board.isGoal()) {
                solvable = true;

                break;
            }

            // parallel search to determine whether this puzzle is solvable
            if (!twinNodes.isEmpty())
                searchTwinNode = twinNodes.delMin();
            else
                break;

            if (searchTwinNode.board.isGoal()) {
                break;
            }

            // insert onto the priority queue all neighboring search nodes
            for (Board neighbor : searchNode.board.neighbors()) {
                // critical optimization
                insertNeighboringSearchNodes(nodes, hashes, neighbor);
            }

            // insert onto twin priority queue all neighboring search nodes
            for (Board neighbor : searchTwinNode.board.neighbors()) {
                // critical optimization
                insertNeighboringSearchNodes(twinNodes, twinHashes, neighbor);
            }

            // time limit
            if (watch.elapsedTime() > 7) {
                break;
            }

        }
    }

    private void insertNeighboringSearchNodes(MinPQ<SearchNode> nodes, Set<Integer> hashes, Board neighbor) {
        int hashCode;
        hashCode = neighbor.toString().hashCode();
        if (!hashes.contains(hashCode)) {
            nodes.insert(new SearchNode(neighbor, searchNode));
            hashes.add(hashCode);
            // inserts++;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if no solution
    public int moves() {
        if (solvable) {
            return createStackSolution().size() - 1;
            // return searchNode.moves;
        } else
            return -1;
    }

    // sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution() {
        if (isSolvable()) {
            return createStackSolution();
        }
        return null;
    }

    private Stack<Board> createStackSolution() {
        if (solution == null) {
            solution = new Stack<>();

            while (searchNode != null) {
                solution.push(searchNode.board);
                searchNode = searchNode.previous;
            }
        }
        return solution;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        URL testFile;
        if (args != null && args.length > 0) {
            testFile = Solver.class.getResource(args[0]);
        } else {
            testFile = Solver.class.getResource("8puzzle/puzzle4x4-hard2.txt");
        }
        In in = new In(testFile);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
