import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MCTSNode {
    private TicTacToe state;
    private MCTSNode parent;
    private List<MCTSNode> children;
    private int visits;
    private int wins;
    private int[] untriedMoves;
    private Random rand = new Random();

    public MCTSNode(TicTacToe state) {
        this.state = state;
        this.children = new ArrayList<>();
        this.untriedMoves = state.getLegalMoves();
        this.visits = 0;
        this.wins = 0;
    }

    public MCTSNode selectChild() {
        MCTSNode selected = null;
        double bestValue = Double.MIN_VALUE;
        for (MCTSNode child : children) {
            double ucb1 = child.wins / (child.visits + 1) +
                    Math.sqrt(2 * Math.log(visits + 1) / (child.visits + 1));
            if (ucb1 > bestValue) {
                selected = child;
                bestValue = ucb1;
            }
        }
        return selected;
    }

    public void expand() {
        int move = untriedMoves[rand.nextInt(untriedMoves.length)];
        TicTacToe newState = state.clone();
        newState.makeMove(move);
        MCTSNode child = new MCTSNode(newState);
        child.parent = this;
        children.add(child);
    }

    public void updateStats(char result) {
        visits++;
        char lastPlayer = (state.getCurrentPlayer() == 'X') ? 'O' : 'X';
        if (result == lastPlayer) {
            wins++;
        }
    }

    public boolean isFullyExpanded() {
        return children.size() == untriedMoves.length;
    }

    public boolean hasUntriedMoves() {
        return untriedMoves.length > 0;
    }
}
