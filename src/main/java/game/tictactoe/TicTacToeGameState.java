package game.tictactoe;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

enum VisitType
{
    horizontalVisit,
    verticalVisit,
    diagonalL2RVisit,
    diagonalR2LVisit;
};

class BoardVisit
{
    private int boardSize;
    private boolean [][]horizontalVisit;
    private boolean [][]verticalVisit;
    private boolean [][]diagonalL2RVisit;
    private boolean [][]diagonalR2LVisit;

    BoardVisit(int boardSize)
    {
        this.boardSize = boardSize;
        this.horizontalVisit  = new boolean[boardSize][boardSize];
        this.verticalVisit    = new boolean[boardSize][boardSize];
        this.diagonalL2RVisit = new boolean[boardSize][boardSize];
        this.diagonalR2LVisit = new boolean[boardSize][boardSize];

        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                verticalVisit[i][j]    = false;
                horizontalVisit[i][j]  = false;
                diagonalL2RVisit[i][j] = false;
                diagonalR2LVisit[i][j] = false;
            }
        }
    }

    void clear()
    {
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                horizontalVisit[i][j] = false;
                verticalVisit[i][j] = false;
                diagonalL2RVisit[i][j] = false;
                diagonalR2LVisit[i][j] = false;
            }
        }
    }

    // Getter.
    public boolean getHorizontalVisit(int x, int y) {
        return horizontalVisit[x][y];
    }
    public boolean getVerticalVisit(int x, int y) {
        return verticalVisit[x][y];
    }
    public boolean getDiagonalL2RVisit(int x, int y) {
        return diagonalL2RVisit[x][y];
    }
    public boolean getDiagonalR2LVisit(int x, int y) {
        return diagonalR2LVisit[x][y];
    }
    // Setter.
    public void setHorizontalVisit(int x, int y) {
        this.horizontalVisit[x][y] = true;
    }
    public void setVerticalVisit(int x, int y){
        this.verticalVisit[x][y] = true;
    }
    public void setDiagonalR2LVisit(int x, int y){
        this.diagonalR2LVisit[x][y] = true;
    }
    public void setDiagonalL2RVisit(int x, int y) {
        this.diagonalL2RVisit[x][y] = true;
    }

}
public class TicTacToeGameState extends GameState<TicTacToeAction> {

    private int boardSize;
    private TicTacToeStone[][] board;
    private User turn;
    private boolean terminal;
    private final int MIN_BLOCK_SIZE = 3;
    private double MAX_POINT;

    public TicTacToeGameState(int boardSize)
    {
        setUpState(boardSize);
    }
    private void setUpState(int boardSize)
    {
        this.boardSize = boardSize;
        this.board = new TicTacToeStone[boardSize][boardSize];

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                board[row][col] = TicTacToeStone.NULL;
            }
        }

        this.turn = User.ONE;
        this.terminal = false;
        this.MAX_POINT = getMaxPoint(boardSize);
    }

    // copy constructor.
    private TicTacToeGameState(TicTacToeGameState b)
    {
        this.boardSize = b.boardSize;
        this.board = new TicTacToeStone[boardSize][boardSize];

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                board[row][col] = b.board[row][col];
            }
        }
        // end of the for loop.

        this.turn = b.turn;
        this.terminal = b.terminal;
        this.MAX_POINT = b.MAX_POINT;
    }
    @Override
    public GameState<TicTacToeAction> getDeepCopy() {
        return new TicTacToeGameState(this);
    }

    private TicTacToeStone getPlayerStone(User user)
    {
        TicTacToeStone ans = TicTacToeStone.NULL;

        if (user == User.ONE)
        {
            ans = TicTacToeStone.X;
        }
        else if (user == User.TWO)
        {
            ans = TicTacToeStone.O;
        }
        else
        {
            ans = TicTacToeStone.NULL;
        }

        return ans;
    }
    private void play(User user, int row, int col)
    {
        TicTacToeStone stone = getPlayerStone(turn);
        if (user != User.NULL)
        {
            stone = (user == User.ONE ? TicTacToeStone.X : TicTacToeStone.O);
        }

        board[row][col] = stone;
    }

    @Override
    public void process(TicTacToeAction action, User user)
    {
        int row = action.getRow();
        int col = action.getCol();

        play(user, row, col);

        if (getLegalActions().size() == 0)
            terminal = true;
        else
            turn = User.opposite(turn);
    }

    @Override
    public boolean isTerminal() {
        return terminal;
    }

    @Override
    public List<TicTacToeAction> getLegalActions()
    {
        List<TicTacToeAction> ans = new LinkedList<TicTacToeAction>();
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (board[row][col] == TicTacToeStone.NULL)
                    ans.add(new TicTacToeAction(row, col));
            }
        }
        return ans;
    }

    private Map<User, Double> getScoreMap(int minBlockSize)
    {
        // Player1 : X, Player2 : O
        Map<User, Double> score = new HashMap<User, Double>();
        Map<User, Double> temp = new HashMap<User, Double>();
        double player1Utility = 0, player2Utility = 0;
        BoardVisit checkVisit = new BoardVisit(this.boardSize);

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {

                // Vertical
                if (!checkVisit.getVerticalVisit(row, col) && (boardSize - row) >= minBlockSize) {
                    temp = calculateUtility(row, col, 0, 1, boardSize - row, minBlockSize, checkVisit, VisitType.verticalVisit);

                    player1Utility += temp.get(User.ONE);
                    player2Utility += temp.get(User.TWO);
                }

                // Horizontal
                if (!checkVisit.getHorizontalVisit(row, col) && (boardSize - col) >= minBlockSize) {
                    temp = calculateUtility(row, col, 1, 0, boardSize - col, minBlockSize, checkVisit, VisitType.horizontalVisit);

                    player1Utility += temp.get(User.ONE);
                    player2Utility += temp.get(User.TWO);
                }

                // Diagonal Left to Right
                if (!checkVisit.getDiagonalL2RVisit(row, col) && (boardSize - row) >= minBlockSize && (boardSize - col) >= minBlockSize)
                {
                    int bSize = Math.min(boardSize - col, boardSize - row);
                    temp = calculateUtility(row, col, 1, 1, bSize, minBlockSize, checkVisit, VisitType.diagonalL2RVisit);

                    player1Utility += temp.get(User.ONE);
                    player2Utility += temp.get(User.TWO);
                }

                // Diagonal Right to Left
                if (!checkVisit.getDiagonalR2LVisit(row, col) && (boardSize - row) >= minBlockSize && (boardSize - col) <= minBlockSize)
                {
                    int bSize = Math.min(col + 1, boardSize - row);
                    temp = calculateUtility(row, col, -1, 1, bSize, minBlockSize, checkVisit, VisitType.diagonalR2LVisit);

                    player1Utility += temp.get(User.ONE);
                    player2Utility += temp.get(User.TWO);
                }
            }
        }

        score.put(User.ONE, player1Utility);
        score.put(User.TWO, player2Utility);

        return score;
    }

    @Override
    public Map<User, Double> getUtilityMap()
    {
        Map<User, Double> score = new HashMap<User, Double>();
        Map<User, Double> scoreMap = getScoreMap(3);

        double player1Score = normalize(scoreMap.get(User.ONE), scoreMap.get(User.TWO) );
        double player2Score = normalize(scoreMap.get(User.TWO), scoreMap.get(User.ONE) );

        if (this.terminal == true)
        {
            score = getScoreMap(player1Score, player2Score);
        }
        else
        {
            score.put(User.ONE, player1Score);
            score.put(User.TWO, player2Score);
        }

        return score;
    }

    private double normalize(double score1, double score2) {
        return (score1 - score2) / (score1 + score2 + 0.0001);
    }
    @Override
    public double getUtility(User user) {
        return getUtilityMap().get(user);
    }

    public Map<User, Double> getScoreMap(double player1Score, double player2Score)
    {
        Map<User, Double> score = new HashMap<User, Double>();
        if (player1Score > player2Score )
        {
            score.put(User.ONE, 1.0);
            score.put(User.TWO, 0.0);
        }
        else if (player1Score < player2Score )
        {
            score.put(User.ONE, 0.0);
            score.put(User.TWO, 1.0);
        }
        else
        {
            score.put(User.ONE, 0.5);
            score.put(User.TWO, 0.5);
        }

        return score;
    }

    @Override
    public User getTurn() {
        return turn;
    }
    public void reset()
    {
        setUpState(this.boardSize);
    }

    public String toString()
    {
        String ans = "";

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                ans = ans + " " + board[row][col].toString();
            }
            ans = ans + "\n";
        }

        return ans;
    }

    private Map<User, Double> calculateUtility(int row, int col, int deltaX, int deltaY, int blockSize, final int minBlockSize, BoardVisit boardVisit, VisitType visitType)
    {
        Map<User, Double> utility = new HashMap<User, Double>();

        double player1Utility = calculateUtility(TicTacToeStone.X, row, col, deltaX, deltaY, blockSize, minBlockSize);
        double player2Utility = calculateUtility(TicTacToeStone.O, row, col, deltaX, deltaY, blockSize, minBlockSize);
        fillVisit(row, col, deltaX, deltaY, blockSize, boardVisit, visitType);

        utility.put(User.ONE, player1Utility);
        utility.put(User.TWO, player2Utility);

        return utility;
    }

    private double calculateUtility(TicTacToeStone player, int row, int col, int deltaX, int deltaY, int blockSize, int minBlockSize)
    {
        int playerSeqLength = 0, emptySeqLength = 0;
        double tempUtility = 0, utility = 0;
        final double PLAYER_POINT = 1.0, EMPTY_POINT = 0.25;

        for (int i = 0; i < blockSize; i++) {
            int nextCol = col + i * deltaX;
            int nextRow = row + i * deltaY;

            if (board[nextRow][nextCol] == player)
            {
                tempUtility += PLAYER_POINT;
                playerSeqLength++;
            }
            else if (board[nextRow][nextCol] == TicTacToeStone.NULL)
            {
                tempUtility += EMPTY_POINT;
                emptySeqLength++;
            }
            else
            {
                if ( playerSeqLength > 0 && (playerSeqLength + emptySeqLength) >= minBlockSize) {
                    utility += tempUtility;
                }

                playerSeqLength = 0;
                emptySeqLength = 0;
                tempUtility = 0;
            }
        }

        // check last series
        if ( playerSeqLength > 0 && (playerSeqLength + emptySeqLength) >= minBlockSize) {
            utility += tempUtility;
        }

        return utility;
    }

    private void fillVisit(int row, int col, int deltaX, int deltaY, int blockSize,BoardVisit boardVisit, VisitType visitType)
    {
        doVisit(row, col, boardVisit, visitType);

        for (int i = 1; i < blockSize; i++) {
            int nextCol = col + i * deltaX;
            int nextRow = row + i * deltaY;

            doVisit(nextRow, nextCol, boardVisit, visitType);
        }
    }

    private void doVisit(int row, int col, BoardVisit boardVisit, VisitType visitType)
    {
        switch (visitType)
        {
            case horizontalVisit:
            {
                boardVisit.setHorizontalVisit(row, col);
                break;
            }
            case verticalVisit:
            {
                boardVisit.setVerticalVisit(row, col);
                break;
            }
            case diagonalL2RVisit:
            {
                boardVisit.setDiagonalL2RVisit(row, col);
                break;
            }
            case diagonalR2LVisit:
            {
                boardVisit.setDiagonalR2LVisit(row, col);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public boolean getReplayStatus() {
        return false;
    }

    @Override
    public int getBoardSize() {
        return this.boardSize;
    }
    private double getMaxPoint(int boardSize)
    {
        if (boardSize == 3)      // Approved
            return 3;
        else if (boardSize == 4) // Approved
            return 52.0;
        else if (boardSize == 5) // Approved
            return 88.0;
        else if (boardSize == 6) // Approved
            return 132.0;
        else if (boardSize == 7) // Approved
            return 86;
        else if (boardSize == 8)
            return 100;
        else if (boardSize == 9)
            return 120;
        else if (boardSize == 10)
            return 130;
        else
            return 1000;
    }
}
