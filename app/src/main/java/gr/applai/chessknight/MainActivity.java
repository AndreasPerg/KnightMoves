package gr.applai.chessknight;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,View.OnClickListener {

//    private int steps;
//    private int boardSize;
//    private Point knightPos;
//    private Point targetPos;

    private State state;

    SeekBar mStepsView;
    SeekBar mSizeView;
    TextView mSizeLabelView;
    TextView mStepsLabelView;
    ChessView mBoardToSelectView;

    MyApp myApp = (MyApp) MyApp.getContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSizeView = findViewById(R.id.boardSize);
        mSizeLabelView = findViewById(R.id.boardSizeLabel);
        mStepsLabelView = findViewById(R.id.stepsLabel);
        mStepsView = findViewById(R.id.steps);
        mBoardToSelectView = findViewById(R.id.boardToSelect);

        state = myApp.getState();

        updateUI();

        if (state.solutions!=null) {
            Intent intent = new Intent(MainActivity.this, ChessboardActivity.class);
            startActivity(intent);
        }

        findViewById(R.id.startButton).setOnClickListener(this);

        mStepsView.setOnSeekBarChangeListener(this);
        mSizeView.setOnSeekBarChangeListener(this);

    }

//    private void setState() {
//        SharedPreferences.Editor editor = myApp.getPrefs().edit();
//
//        editor.putInt("steps", steps);
//        editor.putInt("boardSize", boardSize);
//
//        Gson gson = new Gson();
//        if (knightPos!=null)
//            editor.putString("knightPosJson", gson.toJson(knightPos));
//        if (targetPos!=null)
//            editor.putString("targetPosJson", gson.toJson(targetPos));
//
//        editor.commit();
//    }

    protected void updateUI() {
        state.knightPos = mBoardToSelectView.getKnight();
        state.targetPos = mBoardToSelectView.getTarget();

        mSizeLabelView.setText("Board size: " + state.boardSize);
        mBoardToSelectView.setDimensions(state.boardSize, state.boardSize);
        mStepsLabelView.setText("Steps: " + state.steps);

        mSizeView.setProgress(state.boardSize);
        mStepsView.setProgress(state.steps);

        mBoardToSelectView.setKnight(state.knightPos);
        mBoardToSelectView.setTarget(state.targetPos);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.boardSize:
                state.boardSize = progress;
                break;
            case R.id.steps:
                state.steps = progress;
                break;
        }
        updateUI();
//        myApp.setState(state);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton:
                state.knightPos = mBoardToSelectView.getKnight();
                state.targetPos = mBoardToSelectView.getTarget();


                state.solutions = null;

                myApp.setState(state);

                Intent intent = new Intent(MainActivity.this, ChessboardActivity.class);
//                int steps = mStepsView.getProgress();
//                int boardSize = mSizeView.getProgress();


//                intent.putExtra("steps", steps);
//                intent.putExtra("boardSize", boardSize);
//                intent.putExtra("knightPos", knightPos);
//                intent.putExtra("targetPos", targetPos);

                startActivity(intent);
                break;
        }

    }

}
