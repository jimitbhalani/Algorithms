public class RectHV {
    private final double xmin,ymin,xmax,ymax;
    public RectHV(double xmin, double ymin,         // construct the rectangle [xmin, xmax] x [ymin, ymax]
                  double xmax, double ymax)         // throw a java.lang.IllegalArgumentException if (xmin > xmax) or (ymin > ymax)
    {
        if (xmax < xmin || ymax < ymin) {
            throw new IllegalArgumentException("Invalid rectangle");
        }
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }
    
    public double xmin()                            // minimum x-coordinate of rectangle
    {
        return xmin;
    }
    
    public double ymin()                            // minimum y-coordinate of rectangle
    {
        return ymin;
    }
    
    public double xmax()                            // maximum x-coordinate of rectangle
    {
        return xmax;
    }
    
    public double ymax()                            // maximum y-coordinate of rectangle
    {
        return ymax;
    }
    
    public boolean contains(Point2D p)              // does this rectangle contain the point p (either inside or on boundary)?
    {
        return (p.x() >= xmin) && (p.x() <= xmax)
            && (p.y() >= ymin) && (p.y() <= ymax);
    }
    
    public boolean intersects(RectHV that)          // does this rectangle intersect that rectangle (at one or more points)?
    {
        return this.xmax >= that.xmin && this.ymax >= that.ymin
            && that.xmax >= this.xmin && that.ymax >= this.ymin;
    }
    
    public double distanceTo(Point2D p)             // Euclidean distance from point p to the closest point in rectangle
    {
        return Math.sqrt(this.distanceSquaredTo(p));
    }
    
    public double distanceSquaredTo(Point2D p)      // square of Euclidean distance from point p to closest point in rectangle
    {
        double dx = 0.0, dy = 0.0;
        if      (p.x() < xmin) dx = p.x() - xmin;
        else if (p.x() > xmax) dx = p.x() - xmax;
        if      (p.y() < ymin) dy = p.y() - ymin;
        else if (p.y() > ymax) dy = p.y() - ymax;
        return dx*dx + dy*dy;
    }
    
    public boolean equals(Object y)              // does this rectangle equal that?
    {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        RectHV that = (RectHV) y;
        if (this.xmin != that.xmin) return false;
        if (this.ymin != that.ymin) return false;
        if (this.xmax != that.xmax) return false;
        if (this.ymax != that.ymax) return false;
        return true;
    }
    
    public void draw()                              // draw to standard draw
    {
        StdDraw.line(xmin, ymin, xmax, ymin);
        StdDraw.line(xmax, ymin, xmax, ymax);
        StdDraw.line(xmax, ymax, xmin, ymax);
        StdDraw.line(xmin, ymax, xmin, ymin);
    }
    
    public String toString()                        // string representation
    {
        return "[" + xmin + ", " + ymin + " , " + xmax +  ", " + ymax + "]";
    }
    
    public static void main(String[] args) {
        double x0 = 3;
        double y0 = 5;
        int N = 10;
        
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0, 10);
        StdDraw.setYscale(0, 10);
        StdDraw.setPenRadius(.005);
        RectHV[] rects = new RectHV[N];
        for (int i = 0; i < N; i++) {
            double xmin = StdRandom.uniform(10);
            double ymin = StdRandom.uniform(10);
            double xmax = xmin + StdRandom.uniform(5);
            double ymax = ymin + StdRandom.uniform(5);
            rects[i] = new RectHV(xmin, ymin, xmax, ymax);
            rects[i].draw();
        }
    }
}