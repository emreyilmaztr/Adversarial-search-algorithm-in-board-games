package game.checkers;
import searchAlgorithm.lib.UtilityEnum;
import searchAlgorithm.lib.User;
import searchAlgorithm.lib.GameState;

import java.util.*;

public class CheckersGameState extends GameState<CheckersAction> {

    private int BOARD_WIDTH, BOARD_HEIGHT;
    private CheckersStone[][] board;
    private User turn;
    private boolean terminal;
    private int blackStoneCount;
    private int whiteStoneCount;
    final private int STONE_COUNT = 12;
    private UtilityEnum utility;

    public CheckersGameState(UtilityEnum utility)
    {
        initializeData(utility);
        initializeBoard();
    }

    private void initializeData(UtilityEnum utility)
    {
        this.utility = utility;
        this.blackStoneCount = STONE_COUNT;
        this.whiteStoneCount = STONE_COUNT;

        this.turn = User.ONE;

        this.BOARD_WIDTH = 8;
        this.BOARD_HEIGHT = 8;
        this.board = new CheckersStone[BOARD_HEIGHT][BOARD_WIDTH];

        this.terminal = false;
    }
    private void initializeBoard()
    {
        for (int row = 0; row < BOARD_HEIGHT; row++)
        {
            for (int col = 0; col < BOARD_WIDTH; col++)
            {
                if ( (row <= 2) && ( (row % 2 == 0 && col % 2 == 1) || (row % 2 == 1 && col % 2 == 0) ) )
                {
                    board[row][col] = new CheckersStone(row ,col, StoneType.BLACK);
                }
                else if ( (row >= 5) && ( (row % 2 == 1 && col % 2 == 0) || (row % 2 == 0 && col % 2 == 1) ) )
                {
                    board[row][col] =  new CheckersStone(row ,col, StoneType.WHITE);
                }
                else
                {
                    board[row][col] = new CheckersStone(row ,col, StoneType.NULL);
                }
            }
            // end for.
        }
        // end for.
    }
    // copy constructor.
    private CheckersGameState(CheckersGameState b)
    {
        this.utility = b.utility;
        this.whiteStoneCount = b.whiteStoneCount;
        this.blackStoneCount = b.blackStoneCount;

        this.turn = b.turn;

        this.BOARD_WIDTH  = b.BOARD_WIDTH;
        this.BOARD_HEIGHT = b.BOARD_HEIGHT;

        this.board = new CheckersStone[this.BOARD_HEIGHT][this.BOARD_WIDTH];

        for (int row = 0; row < this.BOARD_HEIGHT; row++)
        {
            for (int col = 0; col < this.BOARD_WIDTH; col++) {

                this.board[row][col] = new CheckersStone(b.board[row][col].checkerPoint.getRow(),
                        b.board[row][col].checkerPoint.getCol(), b.board[row][col].type);

                this.board[row][col].isKing = b.board[row][col].isKing;
            }
        }

        this.terminal = b.terminal;
    }

    @Override
    public GameState<CheckersAction> getDeepCopy() {
        return new CheckersGameState(this);
    }

    private void executeMove(CheckersAction action)
    {
        CheckerPoint srcPoint = action.getSourceStone().checkerPoint, dstPoint = action.getDestPoint();

        // Empty current field.
        if (board[srcPoint.getRow()][srcPoint.getCol()].type != StoneType.NULL)
        {
            // Move stone.
            board[dstPoint.getRow()][dstPoint.getCol()].type = board[srcPoint.getRow()][srcPoint.getCol()].type;
            board[dstPoint.getRow()][dstPoint.getCol()].isKing = board[srcPoint.getRow()][srcPoint.getCol()].isKing;

            board[srcPoint.getRow()][srcPoint.getCol()].type = StoneType.NULL;
            board[srcPoint.getRow()][srcPoint.getCol()].isKing = false;

            // Check if it becomes king or not.
            convertToKing(board[dstPoint.getRow()][dstPoint.getCol()]);
        }
        // else invalid move.
    }

    private void executeAttack(CheckersAction action)
    {
        // Source point, Destination point, Deleted point
        CheckerPoint srcPoint, dstPoint, delPoint;
        StoneType stoneType;

        CheckersAction activeAction = action;
        Random rand = new Random();

        while(activeAction!= null)
        {
            srcPoint  = activeAction.getSourceStone().checkerPoint;
            delPoint  = activeAction.getDeletedPoint();
            dstPoint  = activeAction.getDestPoint();
            stoneType = activeAction.getSourceStone().type;

            // Apply attack.

            StoneType deletedStoneType = board[delPoint.getRow()][delPoint.getCol()].type;

            boolean isAttacked = applyAttack(board, srcPoint, dstPoint, delPoint);

            if (deletedStoneType == StoneType.BLACK && isAttacked == true)
                blackStoneCount--;
            else if (deletedStoneType == StoneType.WHITE && isAttacked == true)
                whiteStoneCount--;

            // Check if there is another attack avaliable in destination point.
            CheckersStone cs = new CheckersStone(dstPoint, stoneType, activeAction.getSourceStone().isKing);
            List<CheckersAction> attacks = getLegalAttacks(cs);

            if (attacks.size() > 0 )
            {
                int index = rand.nextInt(attacks.size());
                activeAction = attacks.get(index);
            }
            else
            {
                activeAction = null;
            }
        }
    }
    private boolean applyAttack(CheckersStone[][] myBoard, CheckerPoint srcPoint, CheckerPoint dstPoint, CheckerPoint delPoint)
    {
        boolean isAttackApplied = false;

        if (myBoard[srcPoint.getRow()][srcPoint.getCol()].type != StoneType.NULL)
        {
            // Move stone.
            myBoard[dstPoint.getRow()][dstPoint.getCol()].type   = myBoard[srcPoint.getRow()][srcPoint.getCol()].type;
            myBoard[dstPoint.getRow()][dstPoint.getCol()].isKing = myBoard[srcPoint.getRow()][srcPoint.getCol()].isKing;

            myBoard[srcPoint.getRow()][srcPoint.getCol()].type = StoneType.NULL;
            myBoard[srcPoint.getRow()][srcPoint.getCol()].isKing = false;

            // Delete point.
            isAttackApplied = true;

            myBoard[delPoint.getRow()][delPoint.getCol()].type = StoneType.NULL;
            myBoard[delPoint.getRow()][delPoint.getCol()].isKing = false;

            // Check if it becomes king or not.
            convertToKing(myBoard[dstPoint.getRow()][dstPoint.getCol()]);
        }
        // else invalid attack.

        return isAttackApplied;
    }
    private void convertToKing(CheckersStone stone)
    {
        if(stone.type == StoneType.BLACK && stone.checkerPoint.getRow() == 7)
            stone.isKing = true;
        else if(stone.type == StoneType.WHITE && stone.checkerPoint.getRow() == 0)
            stone.isKing = true;
    }
    @Override
    public void process(CheckersAction action, User user)
    {
        if (action.getActionType() == ActionType.MOVE)
        {
            executeMove(action);
        }
        else if (action.getActionType() == ActionType.ATTACK)
        {
            executeAttack(action);
        }
        // else invalid action.

        StoneType oppositeType = (turn == User.ONE ? StoneType.WHITE : StoneType.BLACK);
        int size = getLegalActions(oppositeType).size();

        boolean isThereOneStoneLeft = ( (whiteStoneCount == 1) && (blackStoneCount == 1) );

        if (isThereOneStoneLeft == true || whiteStoneCount == 0 || blackStoneCount == 0 || size == 0)
            terminal = true;
        else
            turn = User.opposite(turn);
    }

    @Override
    public boolean isTerminal() {
        return terminal;
    }

    private List<CheckersAction> getLegalMoves(CheckersStone checkersStone)
    {
        int row = checkersStone.checkerPoint.getRow();
        int col = checkersStone.checkerPoint.getCol();
        ActionType actionType = ActionType.MOVE;

        List<CheckersAction> validMovesList = new LinkedList<>();

        if(!checkersStone.isKing)
        {
            if(checkersStone.type == StoneType.WHITE)
            {
                if((row - 1 >= 0) && (col + 1 < 8) && (board[row - 1][col + 1].isEmpty()))
                    validMovesList.add(new CheckersAction(checkersStone, new CheckerPoint(row - 1, col + 1), actionType));

                if((row - 1 >= 0) && (col - 1 >= 0) && (board[row - 1][col - 1].isEmpty()))
                    validMovesList.add(new CheckersAction(checkersStone,new CheckerPoint(row - 1,col - 1), actionType));
            }
            else
            {
                if((row + 1 < 8) && (col + 1 < 8) && (board[row + 1][col + 1].isEmpty()))
                    validMovesList.add(new CheckersAction(checkersStone,new CheckerPoint(row + 1,col + 1), actionType));

                if( (row + 1 < 8) && (col - 1 >= 0) && (board[row + 1][col - 1].isEmpty()) )
                    validMovesList.add(new CheckersAction(checkersStone,new CheckerPoint(row + 1,col - 1), actionType));
            }
        }
        else
        {
            if ((row - 1 >= 0) && (col - 1 >= 0) && (board[row - 1][col - 1].isEmpty()))
                validMovesList.add(new CheckersAction(checkersStone, new CheckerPoint(row - 1, col - 1), actionType));

            if ((row - 1 >= 0) && (col + 1 < 8) && (board[row - 1][col + 1].isEmpty()))
                validMovesList.add(new CheckersAction(checkersStone, new CheckerPoint(row - 1, col + 1), actionType));

            if ((row + 1 < 8) && (col - 1 >= 0) && (board[row + 1][col - 1].isEmpty()))
                validMovesList.add(new CheckersAction(checkersStone, new CheckerPoint(row + 1, col - 1), actionType));

            if ((row + 1 < 8) && (col + 1 < 8) && (board[row + 1][col + 1].isEmpty()))
                validMovesList.add(new CheckersAction(checkersStone, new CheckerPoint(row + 1, col + 1), actionType));
        }

        return validMovesList;
    }

    private List<CheckersAction> getLegalAttacks(CheckersStone checkersStone)
    {
        return getLegalAttacks(checkersStone, this.board);
    }
    private List<CheckersAction> getLegalAttacks(CheckersStone checkersStone, CheckersStone[][] board)
    {
        ActionType actionType = ActionType.ATTACK;
        int row = checkersStone.checkerPoint.getRow(), col = checkersStone.checkerPoint.getCol();

        List<CheckersAction> validAttacksList = new LinkedList<>();

        if (!checkersStone.isKing)
        {
            // White to black.
            if (checkersStone.type == StoneType.WHITE)
            {
                // Diagonal right attack.
                if ( (row - 1 >= 0) && (row - 2 >= 0) && (col + 1 < 8) && (col + 2 < 8) && board[row - 2][col + 2].isEmpty() && !board[row - 1][col + 1].isEmpty())
                {
                    if (board[row - 1][col + 1].type == StoneType.BLACK)
                    {
                        CheckersAction action = new CheckersAction(checkersStone, board[row - 2][col + 2].checkerPoint, actionType);
                        action.addDeletedPoint(board[row - 1][col + 1].checkerPoint);

                        validAttacksList.add(action);
                    }
                }

                // Diagonal left attack.
                if ((row - 1 >= 0) && (row - 2 >= 0) && (col - 1 >= 0) && (col - 2 >= 0) && board[row - 2][col - 2].isEmpty() && !board[row - 1][col - 1].isEmpty())
                {
                    if (board[row - 1][col - 1].type == StoneType.BLACK)
                    {
                        CheckersAction action = new CheckersAction(checkersStone, board[row - 2][col - 2].checkerPoint, actionType);
                        action.addDeletedPoint(board[row - 1][col - 1].checkerPoint);

                        validAttacksList.add(action);
                    }
                }
            }
            // Black to white
            else
            {
                if ((row + 1 < 8) && (row + 2 < 8) && (col + 1 < 8) && (col + 2 < 8) && board[row + 2][col + 2].isEmpty() && !board[row + 1][col + 1].isEmpty())
                {
                    if (board[row + 1][col + 1].type == StoneType.WHITE)
                    {
                        CheckersAction action = new CheckersAction(checkersStone, board[row + 2][col + 2].checkerPoint, actionType);
                        action.addDeletedPoint(board[row + 1][col + 1].checkerPoint);

                        validAttacksList.add(action);
                    }
                }

                if ((row + 1 < 8) && (row + 2 < 8) && (col - 1 >= 0) && (col - 2 >= 0) && board[row + 2][col - 2].isEmpty() && !board[row + 1][col - 1].isEmpty())
                {
                    if (board[row + 1][col - 1].type == StoneType.WHITE) {

                        CheckersAction action = new CheckersAction(checkersStone, board[row + 2][col - 2].checkerPoint, actionType);
                        action.addDeletedPoint(board[row + 1][col - 1].checkerPoint);

                        validAttacksList.add(action);
                    }
                }
            }
        }
        else
        {
            // Up - Right.
            if ( (row - 1 >= 0) && (row - 2 >= 0) && (col + 1 < 8) && (col + 2 < 8) && board[row - 2][col + 2].isEmpty() && isValidAttack(checkersStone, board[row - 1][col + 1] ) )
            {
                CheckersAction action = new CheckersAction(checkersStone, board[row - 2][col + 2].checkerPoint, actionType);
                action.addDeletedPoint(board[row - 1][col + 1].checkerPoint);

                validAttacksList.add(action);
            }

            // Up - Left.
            if ( (row - 1 >= 0) && (row - 2 >= 0) && (col - 1 >= 0)  && (col - 2 >= 0) && board[row - 2][col - 2].isEmpty() && isValidAttack(checkersStone, board[row - 1][col - 1] ) )
            {
                CheckersAction action = new CheckersAction(checkersStone, board[row - 2][col - 2].checkerPoint, actionType);
                action.addDeletedPoint(board[row - 1][col - 1].checkerPoint);

                validAttacksList.add(action);
            }

            // Down Right
            if ( (row + 1 < 8) && (row + 2 < 8) && (col + 1 < 8)  && (col + 2 < 8) && board[row + 2][col + 2].isEmpty() && isValidAttack(checkersStone, board[row + 1][col + 1] ) )
            {
                CheckersAction action = new CheckersAction(checkersStone, board[row + 2][col + 2].checkerPoint, actionType);
                action.addDeletedPoint(board[row + 1][col + 1].checkerPoint);

                validAttacksList.add(action);
            }

            // Down Left.
            if ( (row + 1 < 8) && (row + 2 < 8) && (col - 1 >= 0)  && (col - 2 >= 0) && board[row + 2][col - 2].isEmpty() && isValidAttack(checkersStone, board[row + 1][col - 1] ) )
            {
                CheckersAction action = new CheckersAction(checkersStone, board[row + 2][col - 2].checkerPoint, actionType);
                action.addDeletedPoint(board[row + 1][col - 1].checkerPoint);

                validAttacksList.add(action);
            }
        }

        return validAttacksList;
    }

    private boolean isValidAttack(CheckersStone src, CheckersStone dest)
    {
        if (!dest.isEmpty() && src.type != dest.type)
            return true;

        return false;
    }
    private List<CheckersAction> getLegalActions(StoneType turn)
    {
        List<CheckersAction> ans = new LinkedList<>();
        List<CheckersAction> moves = new LinkedList<>();
        List<CheckersAction> attacks = new LinkedList<>();

        for (int row = 0; row < BOARD_HEIGHT; row++)
        {
            for (int col = 0; col < BOARD_WIDTH; col++)
            {
                if (board[row][col].type == turn)
                {
                    List<CheckersAction> stoneMoves = getLegalMoves(board[row][col]);
                    List<CheckersAction> stoneAttacks = getLegalAttacks(board[row][col]);

                    // Add attacks.
                    if (stoneAttacks.size() > 0) {
                        attacks.addAll(stoneAttacks);
                    }
                    // else do nothing.

                    // Add moves.
                    if (stoneMoves.size() > 0){
                        moves.addAll(stoneMoves);
                    }
                    // else do nothing.
                }
                // else do nothing.
            }
            // End for.
        }
        // End for.

        if (attacks.size() > 0)
            ans.addAll(attacks);
        else
            ans.addAll(moves);

        return ans;
    }
    @Override
    public List<CheckersAction> getLegalActions()
    {
        StoneType type = (turn == User.ONE ? StoneType.BLACK : StoneType.WHITE);
        return getLegalActions(type);
    }

    public Map<User, Double> getEasyUtilityMap() {

        Map<User, Double> score = new HashMap<>();

        double blckStoneCount = 0, whtStoneCount = 0;

        for (int row = 0; row < BOARD_HEIGHT; row++)
        {
            for (int col = 0; col < BOARD_WIDTH; col++)
            {
                if (board[row][col].type == StoneType.BLACK)
                {
                    blckStoneCount++;
                }
                else if (board[row][col].type == StoneType.WHITE)
                {
                    whtStoneCount++;
                }
            }
        }

        double player1Score = normalize(blckStoneCount, whtStoneCount);
        double player2Score = normalize(whtStoneCount, blckStoneCount);

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

    public Map<User, Double> getMediumUtilityMap() {

        Map<User, Double> score = new HashMap<>();

        double blckKingStoneCount = 0, whtKingStoneCount = 0;
        double blckNonKingStoneCount = 0, whtNonKingStoneCount = 0;

        for (int row = 0; row < BOARD_HEIGHT; row++)
        {
            for (int col = 0; col < BOARD_WIDTH; col++)
            {
                if (board[row][col].type == StoneType.BLACK)
                {
                    if (board[row][col].isKing)
                    {
                        blckKingStoneCount++;
                    }
                    else
                    {
                        blckNonKingStoneCount++;
                    }
                }
                else if (board[row][col].type == StoneType.WHITE)
                {
                    if (board[row][col].isKing)
                    {
                        whtKingStoneCount++;
                    }
                    else
                    {
                        whtNonKingStoneCount++;
                    }
                }
            }
        }

        double blckStoneCount = (blckNonKingStoneCount + 1.5 * blckKingStoneCount);
        double whtStoneCount  = (whtNonKingStoneCount  + 1.5 * whtKingStoneCount);

        double player1Score = normalize(blckStoneCount, whtStoneCount);
        double player2Score = normalize(whtStoneCount, blckStoneCount);

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

    public Map<User, Double> getHardUtilityMap() {

        Map<User, Double> score = new HashMap<>();

        double blckKingStoneCount = 0, whtKingStoneCount = 0;
        double blckNonKingStoneCount = 0, whtNonKingStoneCount = 0;

        // Distance to king position
        double blckDist2King = 0, whtDist2King = 0;
        double MAX_DIST = this.BOARD_HEIGHT - 1;

        for (int row = 0; row < BOARD_HEIGHT; row++)
        {
            for (int col = 0; col < BOARD_WIDTH; col++)
            {
                if (board[row][col].type == StoneType.BLACK)
                {
                    if (board[row][col].isKing)
                    {
                        blckKingStoneCount++;
                    }
                    else
                    {
                        blckNonKingStoneCount++;
                        blckDist2King += board[row][col].checkerPoint.getRow();
                    }
                }
                else if (board[row][col].type == StoneType.WHITE)
                {
                    if (board[row][col].isKing)
                    {
                        whtKingStoneCount++;
                    }
                    else
                    {
                        whtNonKingStoneCount++;
                        whtDist2King += MAX_DIST - board[row][col].checkerPoint.getRow();
                    }
                }
            }
        }

        double ESP = 0.0001;
        double blckAvgDistToKing = blckDist2King / (blckNonKingStoneCount + ESP);
        double whtAvgDistToKing  = whtDist2King /  (whtNonKingStoneCount + ESP);

        double blckDistToKingPnt = (blckAvgDistToKing < ESP) ? 0 : (MAX_DIST - blckAvgDistToKing) / MAX_DIST;
        double whtDistToKingPnt = (whtAvgDistToKing < ESP) ? 0 : (MAX_DIST - whtAvgDistToKing) / MAX_DIST;

        double blckStoneCount = (blckNonKingStoneCount + blckDistToKingPnt) + (1.5 * blckKingStoneCount);
        double whtStoneCount  = (whtNonKingStoneCount + whtDistToKingPnt)  + (1.5 * whtKingStoneCount);

        double player1Score = normalize(blckStoneCount, whtStoneCount);
        double player2Score = normalize(whtStoneCount, blckStoneCount);

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

    @Override
    public Map<User, Double> getUtilityMap() {

        if (this.utility == UtilityEnum.UTILITY_1)
        {
            return getEasyUtilityMap();
        }
        else if (this.utility == UtilityEnum.UTILITY_2)
        {
            return getMediumUtilityMap();
        }
        else if (this.utility == UtilityEnum.UTILITY_3)
        {
            return getHardUtilityMap();
        }
        else
        {
            return getEasyUtilityMap();
        }
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

    private String[][] getFormattedBoard()
    {
        final int boardOffset = 2;
        int width = BOARD_WIDTH + boardOffset, height = BOARD_HEIGHT + boardOffset;

        String[][] formattedBoard = new String[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                formattedBoard[i][j] = "";

                if ( (i >= 1 && i <= BOARD_HEIGHT) && (j >= 1 && j <= BOARD_WIDTH))
                    formattedBoard[i][j] = board[i - 1][j - 1].toString();
            }
        }
        // end for.

        for (int i = 1; i < height - 1; i++) {
            formattedBoard[0][i] = Character.toString('A' + i - 1);
            formattedBoard[width - 1][i] = Character.toString('A' + i - 1);
        }
        // end for.
        for (int i = 1; i < width - 1; i++) {
            formattedBoard[i][0] = Integer.toString(width -i - 1);
            formattedBoard[i][height - 1] = Integer.toString(width -i - 1);
        }
        // end for.
        return formattedBoard;
    }

    public void reset()
    {
        initializeData(this.utility);
        initializeBoard();
    }

    public String toString()
    {
        String ans = "";
        String boardStr[][] = getFormattedBoard();
        for (int i = 0; i < boardStr.length; i++) {
            for (int j = 0; j < boardStr[0].length; j++) {
                ans = ans + " " + boardStr[i][j];
            }
            ans = ans + "\n";
        }
        return ans;
    }

    @Override
    public boolean getReplayStatus() {
        return false;
    }

    @Override
    public int getBoardSize() {
        return this.BOARD_WIDTH;
    }

    private void initializeDataBoard(int blackStoneCount, int whiteStoneCount, CheckersStone[][] board)
    {
        this.blackStoneCount = blackStoneCount;
        this.whiteStoneCount = blackStoneCount;

        this.turn = User.ONE;

        this.BOARD_WIDTH = 8;
        this.BOARD_HEIGHT = 8;
        this.board = new CheckersStone[BOARD_HEIGHT][BOARD_WIDTH];


        for (int row = 0; row < BOARD_HEIGHT; row++)
        {
            for (int col = 0; col < BOARD_WIDTH; col++)
            {
                this.board[row][col] = board[row][col];
            }
            // end for.
        }
        // end for.

        this.terminal = false;
    }

    private void copyBoard(CheckersStone[][] cp, CheckersStone[][] mv)
    {
        for (int row = 0; row < this.BOARD_HEIGHT; row++)
        {
            for (int col = 0; col < this.BOARD_WIDTH; col++)
            {
                mv[row][col] = new CheckersStone(row, col, cp[row][col].type, cp[row][col].isKing);
            }
            // end for.
        }
    }
}