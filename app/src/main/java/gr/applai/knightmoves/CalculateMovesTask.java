package gr.applai.knightmoves;

import android.app.Activity;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

public class CalculateMovesTask extends AsyncTask<Void, Integer, List> {
    private ChessBoard mBoard;
    private WeakReference<Activity> mWeakActivity;

    CalculateMovesTask(Activity activity,  ChessBoard board){
        this.mBoard = board;
        this.mWeakActivity = new WeakReference<Activity>(activity);
    }
    @Override
    protected List doInBackground(Void... parms) {
        if (mBoard ==null) return null;

        return mBoard.resolve();
    }
    protected void onProgressUpdate(Integer... progress) {
        Activity activity = mWeakActivity.get();

    }

    protected void onPostExecute(List solutions) {
        Activity activity = mWeakActivity.get();

        ((ChessboardActivity) activity).reportSolutions(solutions);

    }

}
