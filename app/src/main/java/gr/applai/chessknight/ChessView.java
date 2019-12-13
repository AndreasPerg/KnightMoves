package gr.applai.chessknight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Date;
import java.util.List;

public final class ChessView extends View {
    private static final String TAG = ChessView.class.getSimpleName();

    private int cols = 8;
    private int rows = 8;

    private List<List<Point>> solutions = null;

    private Tile[][] mTiles;

    private int from_x = 0;
    private int from_y = 0;
    private int squareSize = 0;

    private Point knightPosition = null;
    private Point targetPosition = null;

    /** 'true' if black is facing player. */
    private boolean flipped = false;
    private Date timeOfPreviousTouch;

    public ChessView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        setFocusable(true);

        setDimensions(8,8);
    }

    private void buildTiles() {
        Tile.TileType type = Tile.TileType.NONE;
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                type = whatTypeTileAt(c,r);

                mTiles[c][r] = new Tile(c, r, type);
            }
        }
    }

    private Tile.TileType whatTypeTileAt(int c, int r) {
        return (r % 2) == (c % 2) ? Tile.TileType.LIGHT : Tile.TileType.DARK;
    }

    private void resetTileAt(int c, int r) {
        mTiles[c][r] = new Tile(c,r,whatTypeTileAt(c,r));
    }

    public void setDimensions(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        this.mTiles = new Tile[cols][rows];

        buildTiles();
        this.invalidate();
    }

    protected void setKnight(Point pt) {

        // remove previous knight
        if (knightPosition!=null) {
            int kc = knightPosition.getX();
            int kr = knightPosition.getY();
            if (kc<cols && kr<rows)
                resetTileAt(kc, kr);
        }

        knightPosition = null;

        if (pt!=null) {
            int c = pt.getX();
            int r = pt.getY();
            if (c<cols && r<rows) {
                mTiles[c][r] = new Tile(c, r, Tile.TileType.KNIGHT);
                knightPosition = pt;
            }
        }

        this.invalidate();
    }

    protected void setTarget(Point pt) {

        // remove previous target
        if (targetPosition!=null) {
            int tc = targetPosition.getX();
            int tr = targetPosition.getY();
            if (tc<cols && tr<rows)
                resetTileAt(tc, tr);
        }

        targetPosition = null;

        if (pt!=null) {
            int c = pt.getX();
            int r = pt.getY();
            if (c<cols && r<rows) {
                mTiles[c][r] = new Tile(c, r, Tile.TileType.TARGET);
                targetPosition = pt;
            }
        }

        this.invalidate();
    }

    protected Point getKnight() {
        return this.knightPosition;
    }

    protected Point getTarget() {
        return this.targetPosition;
    }

    protected void drawSolutions(List<List<Point>> solutions) {
        Log.d(TAG, "found: " + solutions);
        this.solutions = solutions;
        this.invalidate();
    }


    @Override
    protected void onDraw(final Canvas canvas) {
        final int width = getWidth();
        final int height = getHeight();

        this.squareSize = Math.min(
                getSquareSizeWidth(width),
                getSquareSizeHeight(height)
        );

        // draw the board
        computeOrigins(width, height);

        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                final int xCoord = getXCoord(c);
                final int yCoord = getYCoord(r);

                final Rect tileRect = new Rect(
                        xCoord,               // left
                        yCoord,               // top
                        xCoord + squareSize,  // right
                        yCoord + squareSize   // bottom
                );

                mTiles[c][r].setTileRect(tileRect);
                mTiles[c][r].draw(canvas);
            }
        }

        // draw the solutions
        if (solutions!=null) {
            int n = solutions.size();
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);

//            for (int i=0; i<solutions.size(); i++) {
            for (int i=0; i<n; i++) {
                int gray = 255*i/n;
                int color = Color.argb(255, gray,gray,gray);
                paint.setColor(color);
                drawPath(canvas, solutions.get(i), paint);
            }

        }

    }

    private void drawPath(Canvas canvas, List<Point> path, Paint paint) {
        if (path.size()<2) return;

        int from_col = path.get(0).getX();
        int from_row = path.get(0).getY();
        int from_x = getXCoord(from_col) + squareSize/2;
        int from_y = getYCoord(from_row) + squareSize/2;

        for (int i=1;i<path.size();i++){
            int to_col = path.get(i).getX();
            int to_row = path.get(i).getY();
            int to_x = getXCoord(to_col) + squareSize/2;
            int to_y = getYCoord(to_row) + squareSize/2;

            canvas.drawLine(from_x, from_y, to_x, to_y, paint);

            from_x = to_x;
            from_y = to_y;
        }
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();

        Tile tile;
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                tile = mTiles[c][r];
                if (tile.isTouched(x, y)) {
//                    tile.handleTouch();
                    Date now = new Date();
                    if (timeOfPreviousTouch==null || (now.getTime() - timeOfPreviousTouch.getTime())>300) {
                        timeOfPreviousTouch = new Date();
                        handleTouch(c, r);
                    }
                    break;
                }

            }
        }

        return true;
    }

    private void handleTouch(int c, int r) {
        Point newPoint = new Point(c, r);
        if (knightPosition==null) {
            setKnight(newPoint);
            setTarget(null);
        } else if (targetPosition==null) {
            setTarget(newPoint);
        } else {
            setKnight(newPoint);
            setTarget(null);
        }
    }

    private int getSquareSizeWidth(final int width) {
        return width / cols;
    }

    private int getSquareSizeHeight(final int height) {
        return height / rows;
    }

    private int getXCoord(final int x) {
        return from_x + squareSize * (flipped ? cols -1 - x : x);
    }

    private int getYCoord(final int y) {
        return from_y + squareSize * (flipped ? y : rows -1 - y);
    }

    private void computeOrigins(final int width, final int height) {
        this.from_x = (width  - squareSize * cols) / 2;
        this.from_y = (height - squareSize * rows) / 2;
    }

}
