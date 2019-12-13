package gr.applai.chessknight;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class MyApp extends Application {

    SharedPreferences prefs;
    private static Context sContext;
    State state;


    @Override
    public void onCreate() {
        super.onCreate();
        sContext=   getApplicationContext();

        prefs = getSharedPreferences("userPrefs", MODE_PRIVATE);

        init();
        readState();
    }

    public static Context getContext() {
        return sContext;
    }

    protected SharedPreferences getPrefs() {
        return prefs;
    }

    protected State getState() {
        return state;
    }

    private void readState() {
        Gson gson = new Gson();
        try {
            String stateJson = prefs.getString("stateJson", null);
            State temp = gson.fromJson(stateJson, State.class);
            if (temp!=null) state =temp;
            if (state.solutions!=null && state.solutions.size()<1)
                state.solutions = null;
        } catch (Exception e){
        }

//        state.steps = prefs.getInt("steps", state.steps);
//        state.boardSize = prefs.getInt("boardSize", state.boardSize);
//
//        try {
//            String knightPosJson = prefs.getString("knightPosJson", null);
//            state.knightPos = gson.fromJson(knightPosJson, Point.class);
//        } catch (Exception e){
//        }
//        try {
//            String targetPosJson = prefs.getString("targetPosJson", null);
//            state.targetPos = gson.fromJson(targetPosJson, Point.class);
//        } catch (Exception e){
//        }
    }

    protected void setState(State state) {
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        if (state!=null) {
            if (state.solutions != null && state.solutions.size() < 1)
                state.solutions = null;

            editor.putString("stateJson", gson.toJson(state));

            editor.commit();
        }
        this.state = state;
    }

    private void init() {
        state = new State();
        state.steps = getResources().getInteger(R.integer.steps);
        state.boardSize = getResources().getInteger(R.integer.boardSize);
    }

}