package gr.applai.chessknight;

import java.util.ArrayList;
import java.util.List;

public class ChessBoard {

    private final int sideLength;
    private final int steps;
    private final Point start;
    private final Point target;
    private final static int DEFAULT_STEPS = 3;

    private final int DX[] = { 2, 1, -1, -2, -2, -1, 1, 2 };
    private final int DY[] = { 1, 2, 2, 1, -1, -2, -2, -1 };


    public ChessBoard(int sideLength, Point start, Point target) {
        this(sideLength, start, target, DEFAULT_STEPS);
    }

    public ChessBoard(int sideLength, Point start, Point target, int steps) {

        this.sideLength = sideLength;
        this.steps = steps;
        this.start = start;
        this.target = target;
    }

    public List<List<Point>> resolve() {
        List solutions = new ArrayList();

        findTarget(solutions);

        return solutions;
    }

    private void findTarget(final List solutions) {
        findTarget(solutions, start);
    }

    private void findTarget(final List solutions, Point from) {
        findTarget(solutions, from, null);
    }

    private void findTarget(final List solutions, Point from, List<Point> path) {

        if (path == null) {
            path = new ArrayList();
            path.add(start);
        }

        int count = path.size();
        if (count > steps)
            return;

        for (int i = 0; i < 8; i++) {
            Point newPoint = from.newPointMovedBy(DX[i], DY[i]);

            if (isValidPoint(newPoint)) {
                List<Point> newPath = new ArrayList();

                newPath.addAll(path);
                newPath.add(newPoint);

                if (newPoint.equals(target)) {
                    if (count==steps) // don't count the target point as a step, we need it as a point
                        solutions.add(newPath);
                } else {
                    findTarget(solutions, newPoint, newPath);
                }
            }
        }

        return;

    }

    boolean isValidPoint(Point pt) {
        int x = pt.getX();
        int y = pt.getY();

        return (x >= 0 && y >= 0 && x < this.sideLength && y < this.sideLength);
    }
}
