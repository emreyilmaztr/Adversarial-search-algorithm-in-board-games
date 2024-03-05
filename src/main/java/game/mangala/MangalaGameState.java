package game.mangala;

import searchAlgorithm.lib.GameState;
import searchAlgorithm.lib.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MangalaGameState extends GameState<MangalaAction> {

    private final int PIT_COUNT = 6;
    private final int PLAYER_START_INDEX = 0;
    private final int PLAYER_TREASURE_INDEX = PLAYER_START_INDEX + PIT_COUNT;
    private final int PLAYER_END_INDEX = PLAYER_TREASURE_INDEX - 1;
    private final int OPPONENT_START_INDEX = 7;
    private final int OPPONENT_TREASURE_INDEX = (OPPONENT_START_INDEX + PIT_COUNT);
    private final int INIT_STONE_COUNT = 4;
    private int[] playerRow;
    private int[] opponentRow;
    int playerTreasure;
    int opponentTreasure;
    private User turn;
    private boolean joker;
    private boolean terminal;
    private int prevIndex;
    public MangalaGameState() {
        initialize();
    }

    private MangalaGameState(MangalaGameState b)
    {
        this.playerTreasure   = b.playerTreasure;
        this.opponentTreasure = b.opponentTreasure;
        this.prevIndex = b.prevIndex;

        this.playerRow   = new int[PLAYER_TREASURE_INDEX];
        this.opponentRow = new int[PLAYER_TREASURE_INDEX];

        for (int row = 0; row < PLAYER_TREASURE_INDEX; row++){
            this.playerRow[row] = b.playerRow[row];
            this.opponentRow[row] = b.opponentRow[row];
        }
        // end of the for loop.

        this.turn = b.turn;
        this.terminal = b.terminal;
        this.joker = b.joker;
    }

    private void initialize()
    {
        this.playerTreasure = 0;
        this.opponentTreasure = 0;
        this.prevIndex = 0;

        this.playerRow = new int[PLAYER_TREASURE_INDEX];
        this.opponentRow = new int[PLAYER_TREASURE_INDEX];

        for (int row = 0; row < PLAYER_TREASURE_INDEX; row++){
            playerRow[row] = INIT_STONE_COUNT;
            opponentRow[row] = INIT_STONE_COUNT;
        }

        this.turn = User.ONE;
        this.terminal = false;
        this.joker = false;
    }

    public void reset()
    {
        initialize();
    }

    @Override
    public boolean getReplayStatus() {
        return this.joker;
    }

    @Override
    public int getBoardSize() {
        return 0;
    }

    @Override
    public GameState<MangalaAction> getDeepCopy() {
        return new MangalaGameState(this);
    }

    @Override
    public void process(MangalaAction action, User user)
    {
        int [] pRow , oRow;
        int treasure;

        // If user is null or have joker right, play with turn
        if (joker || user == User.NULL)
        {
            user = turn;
        }

        // User-1
        if ( user == User.ONE )
        {
            pRow = playerRow;
            oRow = opponentRow;
            treasure = playerTreasure;
        }
        // User-2
        else
        {
            pRow = opponentRow;
            oRow = playerRow;
            treasure = opponentTreasure;
        }

        int startIndex    = action.getIndex();
        int stonesInIndex = pRow[startIndex];

        int index = startIndex;

        pRow[startIndex] = 0;

        if (stonesInIndex == 0)
        {
            this.prevIndex = index;
        }
        else if (stonesInIndex == 1)
        {
            pRow[index] = 0;
            if (index < PLAYER_END_INDEX)
            {
                pRow[index + 1] = pRow[index + 1] + 1;
            }
            else
            {
                treasure = treasure + 1;
            }
            this.prevIndex = index + 1;

            updateStatus (pRow, oRow, treasure, user);
        }
        else
        {
            while ( stonesInIndex > 0 )
            {
                // 0 - 5         *       6          * 7 - 12        * 13
                // player area   * player treasure  * opponent area  opponent treasure

                this.prevIndex = index;
                if ( index < PLAYER_TREASURE_INDEX)
                {
                    // increase player stone count.
                    pRow[index]++;
                    index++;
                    stonesInIndex--;
                }
                else if ( index == PLAYER_TREASURE_INDEX)
                {
                    // just increase treasure.
                    treasure++;
                    index++;
                    stonesInIndex--;
                }
                else if ( index < OPPONENT_TREASURE_INDEX)
                {
                    // increase opponent stone.
                    oRow[index - OPPONENT_START_INDEX]++;
                    index++;
                    stonesInIndex--;
                }
                else
                {
                    //skip enemy treasure
                    index = index - OPPONENT_TREASURE_INDEX;
                }
            }

            updateStatus (pRow, oRow, treasure, user);
        }
    }
    private void updateStatus (int []pRow , int []oRow , int treasure, User user)
    {
        // Update status according to end point
        // If previous end point is in our space
        if (this.prevIndex < PLAYER_TREASURE_INDEX)
        {
            this.joker = false;

            if (pRow[this.prevIndex] == 1 && oRow[PLAYER_END_INDEX - this.prevIndex] > 0 )
            {
                treasure = treasure + pRow[this.prevIndex] + oRow[PLAYER_END_INDEX - this.prevIndex];

                // Reset both wells
                pRow[this.prevIndex] = 0;
                oRow[PLAYER_END_INDEX - this.prevIndex] = 0;
            }
        }
        // If previous end point is our treasure point, play again
        else if (this.prevIndex == PLAYER_TREASURE_INDEX)
        {
            if (!getLegalActions().isEmpty())
            {
                this.joker = true;
            }
            else
            {
                this.joker = false;
            }
        }
        // If previous end point is in opponent space
        else
        {
            this.joker = false;
            if (oRow[this.prevIndex - 7] % 2 == 0)
            {
                treasure = treasure + oRow[this.prevIndex - 7];

                // Reset opponent well
                oRow[this.prevIndex - 7] = 0;
            }
        }

        // Update treasure.
        if (user == User.ONE)
        {
            this.playerTreasure = treasure;
        }
        else
        {
            this.opponentTreasure = treasure;
        }

        if (!this.joker)
            this.turn = User.opposite(this.turn);

        // Evaluate.
        if (getLegalActions(User.ONE).isEmpty() || getLegalActions(User.TWO).isEmpty())
        {
            this.terminal = true;

            int playerStoneCount   = getStoneCount().get(User.ONE);
            int opponentStoneCount = getStoneCount().get(User.TWO);

            // if last player is player1(User.One) and no more stones in our space
            // take opponent stones and add our treasure.
            if (playerStoneCount == 0)
                this.playerTreasure += opponentStoneCount;

            if (opponentStoneCount == 0)
                this.opponentTreasure += playerStoneCount;
        }

    }
    @Override
    public boolean isTerminal() {
        return terminal;
    }

    @Override
    public List<MangalaAction> getLegalActions() {
        return getLegalActions(this.turn);
    }

    private List<MangalaAction> getLegalActions(User user) {

        int row[];

        if (user == User.ONE) {
            row = playerRow;
        } else {
            row = opponentRow;
        }

        List<MangalaAction> ans = new LinkedList<MangalaAction>();
        for (int i = 0; i < PLAYER_TREASURE_INDEX; i++)
        {
            if (row[i] != 0)
            {
                ans.add(new MangalaAction(i));
            }
        }

        return ans;
    }

    @Override
    public Map<User, Double> getUtilityMap() {

        double player1Score = normalize(playerTreasure, opponentTreasure);
        double player2Score = normalize(opponentTreasure, playerTreasure);

        Map<User, Double> score = new HashMap<User, Double>();

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

    @Override
    public String toString() {
        String ans = "";

        for (int i = PLAYER_END_INDEX; i >= 0 ; i--)
        {
            ans = ans + " " + i + "  ";
        }
        ans = ans + "\n";

        for (int i = PLAYER_END_INDEX; i >= 0 ; i--)
        {
            ans = ans +  "[" + opponentRow[i] + "] ";
        }

        ans = ans + "\n-----------------------\n";
        ans = ans + "[" + opponentTreasure + "] --------------- [" + playerTreasure + "]";
        ans = ans + "\n-----------------------\n";

        for (int i = 0; i <= PLAYER_END_INDEX; i++)
        {
            ans = ans +  "[" + playerRow[i] + "] ";
        }

        ans = ans + "\n";
        for (int i = 0; i <= PLAYER_END_INDEX; i++)
        {
            ans = ans + " " + i + "  ";
        }

        return ans;
    }
    private Map<User, Integer> getStoneCount()
    {
        Map<User, Integer> stoneCount = new HashMap<User, Integer>();
        int player1StoneCount = 0,  player2StoneCount = 0;

        for (int i = 0; i <= PLAYER_END_INDEX; i++)
        {
            player1StoneCount += playerRow[i];
            player2StoneCount += opponentRow[i];
        }

        stoneCount.put(User.ONE, player1StoneCount);
        stoneCount.put(User.TWO, player2StoneCount);

        return stoneCount;
    }
}