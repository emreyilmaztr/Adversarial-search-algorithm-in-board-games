package gameform;

import ZeroSumGame.AlgorithmEnum;
import ZeroSumGame.GameEnum;
import ZeroSumGame.PlayerSetting;
import game.checkers.CheckersGameState;
import game.mangala.MangalaGameState;
import game.tictactoe.TicTacToeGameState;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import searchAlgorithm.lib.IPlayer;
import searchAlgorithm.lib.GameState;
import searchAlgorithm.lib.User;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class GameFormController implements Initializable {
    @FXML
    private ChoiceBox<GameEnum> gameCb;
    @FXML
    private ChoiceBox<Integer> boardSizeCb;
    @FXML
    private Label boardSizeLbl;
    @FXML
    private ChoiceBox<AlgorithmEnum> player1AlgorithmCb;
    @FXML
    private Label player1AlgInfoLbl;
    @FXML
    private TextField player1AlgInfoTf;
    @FXML
    private ChoiceBox<AlgorithmEnum> player2AlgorithmCb;
    @FXML
    private Label player2AlgInfoLbl;
    @FXML
    private TextField player2AlgInfoTf;
    @FXML
    private TextArea logTxt;
    @FXML
    private TextField gameCountTf;
    @FXML
    private CheckBox verboseChck;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressLbl;
    @FXML
    private TextField numOfRandActionTf;
    private final Integer defaultDepth = 5, defaultIteration = 300;
    private final Integer defaultEpochCount = 1;

    // Game attributes
    private GameState gameState;
    private IPlayer player1, player2, randomPlayer1, randomPlayer2;
    private Thread th;

    private boolean isGameStarted;

    private Integer boardSizeArr[] = {3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.isGameStarted = false;

        // set up board size label
        boardSizeLbl.setVisible(false);

        // set up board size choice box
        boardSizeCb.setVisible(false);
        boardSizeCb.getItems().addAll(boardSizeArr);
        boardSizeCb.setValue(boardSizeArr[0]);

        // set up game selection choice box
        gameCb.getItems().addAll(GameEnum.values());
        gameCb.setValue(GameEnum.mangala);
        gameCb.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    // manage board size
                    if (new_val == (Number) GameEnum.tictactoe.toInt())
                    {
                        boardSizeCb.setVisible(true);
                        boardSizeLbl.setVisible(true);
                    }
                    else
                    {
                        boardSizeCb.setVisible(false);
                        boardSizeLbl.setVisible(false);
                    }
                });

        // set up player 1 algorithm selection choice box
        player1AlgorithmCb.getItems().addAll(AlgorithmEnum.values());
        player1AlgorithmCb.setValue(AlgorithmEnum.minimax);
        player1AlgInfoTf.setText(defaultDepth.toString());
        player1AlgorithmCb.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, old_Val, new_Val) -> {
                    if (new_Val == AlgorithmEnum.alphaBeta || new_Val == AlgorithmEnum.minimax || new_Val == AlgorithmEnum.expectimax )
                    {
                        player1AlgInfoLbl.setText("Max Depth:");
                        player1AlgInfoTf.setText(defaultDepth.toString());
                    }
                    else if (new_Val == AlgorithmEnum.mcts || new_Val == AlgorithmEnum.mctsWithWu)
                    {
                        player1AlgInfoLbl.setText("Game Played:");
                        player1AlgInfoTf.setText(defaultIteration.toString());
                    }
                    else
                    {
                        player1AlgInfoLbl.setVisible(false);
                        player1AlgInfoTf.setVisible(false);
                    }
                });
        // set up player 1 algorithm selection choice box
        player2AlgorithmCb.getItems().addAll(AlgorithmEnum.values());
        player2AlgorithmCb.setValue(AlgorithmEnum.minimax);
        player2AlgInfoTf.setText(defaultDepth.toString());
        player2AlgorithmCb.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, old_Val, new_Val) -> {
                    if (new_Val == AlgorithmEnum.alphaBeta || new_Val == AlgorithmEnum.minimax || new_Val == AlgorithmEnum.expectimax )
                    {
                        player2AlgInfoLbl.setText("Max Depth:");
                        player2AlgInfoTf.setText(defaultDepth.toString());
                    }
                    else if (new_Val == AlgorithmEnum.mcts || new_Val == AlgorithmEnum.mctsWithWu)
                    {
                        player2AlgInfoLbl.setText("Game Played:");
                        player2AlgInfoTf.setText(defaultIteration.toString());
                    }
                    else
                    {
                        player2AlgInfoLbl.setVisible(false);
                        player2AlgInfoTf.setVisible(false);
                    }
                });

        gameCountTf.setText(defaultEpochCount.toString());
        numOfRandActionTf.setText("0");
        verboseChck.setSelected(false);
    }

    private void setGameParameter()
    {
        int player1InputVal = Integer.parseInt(player1AlgInfoTf.getText());
        int player2InputVal = Integer.parseInt(player2AlgInfoTf.getText());

        Random random = new Random();

        switch (gameCb.getValue())
        {
            case tictactoe:
            {
                gameState = new TicTacToeGameState(boardSizeCb.getValue().intValue());
                player1 = PlayerSetting.initializePlayer(GameEnum.tictactoe, player1AlgorithmCb.getValue(), User.ONE, random,1, player1InputVal);
                player2 = PlayerSetting.initializePlayer(GameEnum.tictactoe, player2AlgorithmCb.getValue(), User.TWO, random,2, player2InputVal);

                // Uses to take random action before actual game if k is not equal to 0
                randomPlayer1 = PlayerSetting.initializePlayer(GameEnum.tictactoe, AlgorithmEnum.random, User.ONE, random,98, 0);
                randomPlayer2 = PlayerSetting.initializePlayer(GameEnum.tictactoe, AlgorithmEnum.random, User.TWO, random,99, 0);
                break;
            }
            case checkers:
            {
                gameState = new CheckersGameState();
                player1 = PlayerSetting.initializePlayer(GameEnum.checkers, player1AlgorithmCb.getValue(), User.ONE, random,1, player1InputVal);
                player2 = PlayerSetting.initializePlayer(GameEnum.checkers, player2AlgorithmCb.getValue(), User.TWO, random,2, player2InputVal);

                // Uses to take random action before actual game if k is not equal to 0
                randomPlayer1 = PlayerSetting.initializePlayer(GameEnum.checkers, AlgorithmEnum.random, User.ONE, random,98, 0);
                randomPlayer2 = PlayerSetting.initializePlayer(GameEnum.checkers, AlgorithmEnum.random, User.TWO, random,99, 0);
                break;
            }
            case mangala:
            {
                gameState = new MangalaGameState();
                player1 = PlayerSetting.initializePlayer(GameEnum.mangala, player1AlgorithmCb.getValue(), User.ONE, random,1, player1InputVal);
                player2 = PlayerSetting.initializePlayer(GameEnum.mangala, player2AlgorithmCb.getValue(), User.TWO, random,2, player2InputVal);

                // Uses to take random action before actual game if k is not equal to 0
                randomPlayer1 = PlayerSetting.initializePlayer(GameEnum.mangala, AlgorithmEnum.random, User.ONE, random,98, 0);
                randomPlayer2 = PlayerSetting.initializePlayer(GameEnum.mangala, AlgorithmEnum.random, User.TWO, random,99, 0);
                break;
            }
            default:
            {
                break;
            }
        }
    }
    @FXML
    public void startGame()
    {
        this.isGameStarted = true;

        logTxt.setText("");

        setGameParameter();

        int gameCount = 1;
        if (!gameCountTf.getText().isEmpty())
            gameCount = Integer.parseInt(gameCountTf.getText().toString());

        int numOfRandAction = 0;
        if (!numOfRandActionTf.getText().isEmpty())
            numOfRandAction = Integer.parseInt(numOfRandActionTf.getText().toString());

        GameTask gameTask = new GameTask(gameCount, player1, player2, randomPlayer1, randomPlayer2, gameState, numOfRandAction, verboseChck.isSelected());
        gameTask.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                logTxt.setText(t1);
            }
        });

        progressBar.progressProperty().bind(gameTask.progressProperty());

        th = new Thread(gameTask);
        th.setDaemon(true);
        th.start();

    }

    @FXML
    public void stopGame()
    {
        if (this.isGameStarted)
            th.stop();
    }
}