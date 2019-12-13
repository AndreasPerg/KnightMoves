package gr.applai.knightmoves;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.Log;

public final class Tile {
    private static final String TAG = Tile.class.getSimpleName();

    protected enum TileType { LIGHT, DARK, KNIGHT, TARGET, NONE}

    private final int col;
    private final int row;

    private Paint squareColor;
    private Rect tileRect;

    private TileType type = TileType.NONE;

    public Tile(final int col, final int row, TileType type) {
        this.col = col;
        this.row = row;

        this.type = type;

    }

    public void draw(final Canvas canvas) {
        squareColor = getPaint(tileRect, type);
        canvas.drawRect(tileRect, squareColor);
    }

    public String getColumnString() {
        switch (col) {
            case 0: return "A";
            case 1: return "B";
            case 2: return "C";
            case 3: return "D";
            case 4: return "E";
            case 5: return "F";
            case 6: return "G";
            case 7: return "H";
            default: return null;
        }
    }

    public String getRowString() {
        // To get the actual row, add 1 since 'row' is 0 indexed.
        return String.valueOf(row + 1);
    }

    public void handleTouch() {
        Log.d(TAG, "handleTouch(): col: " + col);
        Log.d(TAG, "handleTouch(): row: " + row);

    }

    public boolean isDark() {
        return (col + row) % 2 == 0;
    }

    public boolean isTouched(final int x, final int y) {
        return tileRect.contains(x, y);
    }

    public void setTileRect(final Rect tileRect) {
        this.tileRect = tileRect;
//        squareColor.setColor(isDark() ? Color.LTGRAY : Color.WHITE);
    }

    public String toString() {
        final String col = getColumnString();
        final String row    = getRowString();
        return "<Tile " + col + row + ">";
    }

    private Paint getPaint(Rect tileRect, TileType type) {
        int fromColor, toColor;

        switch (type) {
            case DARK:
                fromColor = Color.parseColor("#FF7255");
                toColor = Color.parseColor("#D84B2E");
            break;
            case LIGHT:
                fromColor = Color.parseColor("#54C2F9");
                toColor = Color.parseColor("#2D9BD1");
            break;
            case KNIGHT:
                fromColor = Color.parseColor("#FF00FF");
                toColor = Color.parseColor("#FF00FF");
            break;
            case TARGET:
                fromColor = Color.parseColor("#A6E036");
                toColor = Color.parseColor("#A6E036");
            break;
            default:
                fromColor = Color.parseColor("#FFFFFF");
                toColor = Color.parseColor("#999999");
                break;
        }

        Shader shader = new LinearGradient(tileRect.left, tileRect.top, tileRect.right, tileRect.bottom, fromColor, toColor, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        return paint;
    }
//
//    private Paint getPaint(boolean isBlack) {
//        int fromColor, toColor;
//        if (isBlack) {
//            fromColor = Color.parseColor("#FF7255");
//            toColor = Color.parseColor("#D84B2E");
//        } else {
//            fromColor = Color.parseColor("#54C2F9");
//            toColor = Color.parseColor("#2D9BD1");
//
//        }
////        Shader shader = new LinearGradient(tileRect.left, tileRect.top, tileRect.right, tileRect.bottom, Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP);
//        Shader shader = new LinearGradient(tileRect.left, tileRect.top, tileRect.right, tileRect.bottom, fromColor, toColor, Shader.TileMode.CLAMP);
//        Paint paint = new Paint();
//        paint.setShader(shader);
//        return paint;
//    }
}