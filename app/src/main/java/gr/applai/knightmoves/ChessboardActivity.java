package gr.applai.knightmoves;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class ChessboardActivity extends AppCompatActivity {
    private static final String TAG = ChessView.class.getSimpleName();

    private State state;

    private ChessView mChessboardView;
    private TextView mResultsView;
    private TextView mMessagesView;

    private CalculateMovesTask task;

    MyApp myApp = (MyApp) MyApp.getContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chessboard);

        mChessboardView = findViewById(R.id.chessboard);
        mResultsView = findViewById(R.id.results);
        mMessagesView = findViewById(R.id.messages);

        state = myApp.getState();
        mChessboardView.setDimensions(state.boardSize, state.boardSize);
        mChessboardView.setKnight(state.knightPos);
        mChessboardView.setTarget(state.targetPos);

        if (state.solutions!=null) {
            updateUI();
        } else if (state.boardSize>0 && state.steps>0 && state.knightPos!=null && state.targetPos!=null) {
            ChessBoard board = new ChessBoard(state.boardSize, state.knightPos, state.targetPos, state.steps);
            mMessagesView.setText("Please wait...");
            task = new CalculateMovesTask(this, board);
            task.execute();
        } else {
            mMessagesView.setText("Not enough input data provided");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (task!=null) {
            task.cancel(true);
            task = null;
            mMessagesView.setText("Calculation canceled!");
        }
    }

    protected void updateUI() {
        mChessboardView.drawSolutions(state.solutions);

        int n = 0;
        if (state.solutions!=null)
            n = state.solutions.size();
        if (n == 1)
            mMessagesView.setText("Found " + n + " solution");
        else if (n > 1)
            mMessagesView.setText("Found " + n + " different solutions");
        else
            mMessagesView.setText("No solutions found for this input");

    }

    protected void reportSolutions(List<List<Point>> solutions) {
        state.solutions = solutions;
        myApp.setState(state);
        task = null;
        updateUI();
    }
}