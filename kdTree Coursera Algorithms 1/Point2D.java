public class Point2D implements Comparable<Point2D> {
    private final double x,y;
    public Point2D(double x, double y)              // construct the point (x, y)
    {
        if (Double.isInfinite(x) || Double.isInfinite(y))
            throw new IllegalArgumentException("Coordinates must be finite");
        if (Double.isNaN(x) || Double.isNaN(y))
            throw new IllegalArgumentException("Coordinates cannot be NaN");
        if (x == 0.0) x = 0.0;  // convert -0.0 to +0.0
        if (y == 0.0) y = 0.0;  // convert -0.0 to +0.0
        this.x = x;
        this.y = y;
    }
    public double x()                               // x-coordinate
    {
        return x;
    }
    public double y()                               // y-coordinate
    {
        return y;
    }
    public double distanceTo(Point2D that)          // Euclidean distance between two points
    {
        double dx = this.x - that.x;
        double dy = this.y - that.y;
        return Math.sqrt(dx*dx + dy*dy);
    }
    
    public double distanceSquaredTo(Point2D that)   // square of Euclidean distance between two points
    {
        double dx = this.x - that.x;
        double dy = this.y - that.y;
        return dx*dx + dy*dy;
    }
    public int compareTo(Point2D that)              // for use in an ordered symbol table
    {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return +1;
        if (this.x < that.x) return -1;
        if (this.x > that.x) return +1;
        return 0;
    }
    public boolean equals(Object other)              // does this point equal that?
    {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Point2D that = (Point2D) other;
        return this.x == that.x && this.y == that.y;
    }
    public void draw()                              // draw to standard draw
    {
        StdDraw.point(x, y);
    }
    public String toString()                        // string representation
    {
        return "(" + x + ", " + y + ")";
    }
    
    public static void main(String[] args) {
        double x0 = 3;
        double y0 = 5;
        int N = 10;
        
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0, 10);
        StdDraw.setYscale(0, 10);
        StdDraw.setPenRadius(.005);
        Point2D[] points = new Point2D[N];
        for (int i = 0; i < N; i++) {
            double x = StdRandom.uniform(10);
            double y = StdRandom.uniform(10);
            points[i] = new Point2D(x, y);
            points[i].draw();
        }
        
        // draw p = (x0, x1) in red
        Point2D p = new Point2D(x0, y0);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(.02);
        p.draw();
        
    }
}