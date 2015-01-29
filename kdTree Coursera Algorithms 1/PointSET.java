import java.util.TreeSet;
import java.util.Stack;

public class PointSET {
    
    private final TreeSet<Point2D> pointSet;
    public PointSET()                               // construct an empty set of points
    {
        pointSet = new TreeSet<Point2D>();
    }
    
    public boolean isEmpty()                        // is the set empty?
    {
        return pointSet.isEmpty();
    }
    
    public int size()                               // number of points in the set
    {
        return pointSet.size();
    }
    
    public void insert(Point2D p)                   // add the point p to the set (if it is not already in the set)
    {
        if(p == null) throw new java.lang.IllegalArgumentException();
        pointSet.add(p);
    }
    
    public boolean contains(Point2D p)              // does the set contain the point p?
    {
        if(p == null) throw new java.lang.IllegalArgumentException();
        return pointSet.contains(p);
    }
    
    public void draw()                              // draw all of the points to standard draw
    {
        for(Point2D points : pointSet){
           points.draw();
        }
    }
    
    public Iterable<Point2D> range(RectHV rect)     // all points in the set that are inside the rectangle
    {
        Stack<Point2D> mystack = new Stack<Point2D>();
        for(Point2D points : pointSet){
           if(rect.contains(points))
               mystack.push(points);
        }
        return mystack;
    }
    
    public Point2D nearest(Point2D p)               // a nearest neighbor in the set to p; null if set is empty
    {
        double maxDistance = Double.POSITIVE_INFINITY;
        Point2D nearestNeighbor = null;
        for(Point2D points : pointSet){
            double distanceToPoint = points.distanceSquaredTo(p);
            if(distanceToPoint < maxDistance){
               maxDistance = distanceToPoint;
               nearestNeighbor = points;
            }
        }
        return nearestNeighbor;
    }
}