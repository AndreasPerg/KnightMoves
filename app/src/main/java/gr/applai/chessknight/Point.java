package gr.applai.chessknight;

import java.io.Serializable;

public class Point  implements Serializable {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point newPointMovedBy(int dx, int dy) {
        return new Point(x + dx, y+dy);
    }

    public boolean equals(Point pt) {
        return pt!=null && pt.getX()==this.getX() && pt.getY()==this.getY();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String toString() {
        return "<Point " + x + ", " + y + ">";
    }
}