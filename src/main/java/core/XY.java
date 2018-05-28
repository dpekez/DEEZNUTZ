package core;

public final class XY {
    public static final XY ZERO_ZERO = new XY(0, 0);
    public static final XY RIGHT = new XY(1, 0);
    public static final XY LEFT = new XY(-1, 0);
    public static final XY UP = new XY(0, -1);
    public static final XY DOWN = new XY(0, 1);
    public static final XY RIGHT_UP = new XY(1, -1);
    public static final XY RIGHT_DOWN = new XY(1, 1);
    public static final XY LEFT_UP = new XY(-1, -1);
    public static final XY LEFT_DOWN = new XY(-1, 1);
    private final int x;
    private final int y;

    public XY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public XY addVector(XY vector) {
        return new XY(x + vector.getX(), y + vector.getY());
    }

    public XY reduceVector(XY vector) {
        return new XY(x - vector.getX(), y - vector.getY());
    }

    public XY times(int factor) {
        return new XY(x * factor, y * factor);
    }

    public double length() {
        return 0;
    }

    /**
     * Calculates the distance between two locations.
     *
     * @param xy Position of Comparision
     * @return the length of the XY Object to the other one
     */

    public double distanceFrom(XY xy) {
        return Math.sqrt(Math.pow(xy.getX() - x, 2) + Math.pow(xy.getY() - y, 2));
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof XY)) {
            return false;
        }
        try {
            if (((XY) obj).getX() == x && ((XY) obj).getY() == y)
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
