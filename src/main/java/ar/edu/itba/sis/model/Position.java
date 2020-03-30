package ar.edu.itba.sis.model;

public class Position extends Vector {
    private double x;
    private double y;

    public Position(double x, double y) {
        super(x, y);
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
