import java.util.Stack;
public class KdTree {
    
    private Node root;
    private int size;
    
    public KdTree()                               // construct an empty set of points
    {
        root = null;
        size = 0;
    }
    
    public boolean isEmpty()                        // is the set empty?
    {
        return (size == 0);
    }
    
    public int size()                               // number of points in the set
    {
        return size;
    }
    
    public void insert(Point2D p)                   // add the point p to the set (if it is not already in the set)
    {
        if(p == null) throw new java.lang.IllegalArgumentException();
        root = insertRemainingNodes(root, p, true, new RectHV(0,0,10,10));
    }
    
    private Node insertRemainingNodes(Node node, Point2D p, boolean vertical, RectHV rect){
        
        if(node == null){     
            size++;
            return new Node(p,vertical,rect);  
        }
        
        //don't allow for duplicates
        if(node.getPoint().equals(p)) return node;
        
        if(node.vertical){
            //vertical node and right child
            if(p.x() > node.getPoint().x()){
                node.rt = insertRemainingNodes(node.rt,p,!node.vertical,new RectHV(node.getPoint().x(),node.rect.ymin(),node.rect.xmax(),node.rect.ymax()));
            }
            //vertical node and left child
            else{
                node.lb = insertRemainingNodes(node.lb,p,!node.vertical,new RectHV(node.rect.xmin(),node.rect.ymin(),node.getPoint().x(),node.rect.ymax()));
            }
        }
        
        else{     
            //horizontal node and right child
            if(p.y() > node.getPoint().y()){
                node.rt = insertRemainingNodes(node.rt,p,!node.vertical, new RectHV(node.rect.xmin(),node.getPoint().y(),node.rect.xmax(),node.rect.ymax()));
            }   
            //horizontal and left child
            else{
                node.lb = insertRemainingNodes(node.lb,p,!node.vertical, new RectHV(node.rect.xmin(),node.rect.ymin(),node.rect.xmax(),node.getPoint().y()));
            }
        }
        return node;
    }
    
    public boolean contains(Point2D p)              // does the set contain the point p?
    {
        if(p == null) throw new java.lang.IllegalArgumentException();
        return searchNode(root,p);
    }
    
    private boolean searchNode(Node node, Point2D p){
        
        if(node == null) return false;
        
        if(node.getPoint().equals(p))
            return true;
        
        if(node.vertical){
            if(p.x() > node.getPoint().x()){
                return searchNode(node.rt,p);
            }
            else{
                return searchNode(node.lb,p);
            }
        }
        
        else{
            if(p.y() > node.getPoint().y()){
                return searchNode(node.rt,p);
            }
            else{
                return searchNode(node.lb,p);
            }
        }
    }
    
    public void draw()                              // draw all of the points to standard draw
    {
        StdDraw.line(0,0,10,0);
        StdDraw.line(10,0,10,10);
        StdDraw.line(10,10,0,10);
        StdDraw.line(0,10,0,0);
        drawNodes(root);
    }
    
    private void drawNodes(Node node){
        if(node == null) return;
        
        drawNodes(node.lb);
        StdDraw.setPenRadius(.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        node.getPoint().draw();
        if(node.vertical){
            StdDraw.setPenRadius(.005);  
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.getPoint().x(),node.rect.ymin(),node.getPoint().x(),node.rect.ymax());
        }
        else{
            StdDraw.setPenRadius(.005);  
            StdDraw.setPenColor(StdDraw.BLUE);  
            StdDraw.line(node.rect.xmin(),node.getPoint().y(),node.rect.xmax(),node.getPoint().y());
        }
        drawNodes(node.rt);
    }
    
    public Iterable<Point2D> range(RectHV rect)     // all points in the set that are inside the rectangle
    {
        Stack<Point2D> mystack = new Stack<Point2D>();
        searchRange(root,mystack,rect);
        return mystack;
    }
    
    private void searchRange(Node node, Stack<Point2D> stck, RectHV rect){
        if(node == null) return;
        
        if( rect.contains(node.getPoint())){
            stck.push(node.getPoint());
        }
        
        if(rect.intersects(node.rect)){
            searchRange(node.lb,stck,rect);
            searchRange(node.rt,stck,rect);
        }
    }
    
    public Point2D nearest(Point2D p)               // a nearest neighbor in the set to p; null if set is empty
    {
        double distQueryToNode  = 0.0;
        double distQueryToRect = 0.0;
        double distQueryToNearestPoint = 0.0;
     
        return searchNearest(root,p,distQueryToNode,distQueryToRect,distQueryToNearestPoint,null);
    }
    
    private Point2D searchNearest(Node node,Point2D queryPoint,double distQueryToNode,double distQueryToRect,double distQueryToNearestPoint,Point2D nearestPoint){
        if(node == null) return nearestPoint;
        
        if(nearestPoint == null){
            nearestPoint = node.getPoint();
            //System.out.println( " NEAREST POINT IS " +nearestPoint);
        }
        
        if(nearestPoint != null){
            distQueryToNode = queryPoint.distanceSquaredTo(node.getPoint());
           // System.out.println( " DISTANCE QUERY TO NODE " +node.getPoint().toString()+ " IS " +distQueryToNode);
            distQueryToRect  = node.rect.distanceSquaredTo(queryPoint);
          //  System.out.println( " DISTANCE QUERY TO RECT " +node.rect.toString()+ " IS " +distQueryToRect);
            distQueryToNearestPoint = queryPoint.distanceSquaredTo(nearestPoint);
          //  System.out.println( " DISTANCE OF QUERY POINT TO NEAREST POINT " +nearestPoint+ " IS " +queryPoint.distanceSquaredTo(nearestPoint));
        }
        
        if(distQueryToNode <= distQueryToNearestPoint){
            nearestPoint = node.getPoint();
            //System.out.println( " NEW NEAREST POINT IS " +nearestPoint.toString());
        }
        
        if(distQueryToNode > distQueryToRect){
            
            if(node.vertical){
                //if query point x < node point x, explore left
                if( queryPoint.x() < node.getPoint().x()){
                    //System.out.println( " PREV LEVEL IS VERTICAL , GO TO LEFT CHILD OF POINT " +node.getPoint().toString());
                    nearestPoint = searchNearest(node.lb,queryPoint,distQueryToNode,distQueryToRect,distQueryToNearestPoint,nearestPoint);
                }
                else{
                    //System.out.println( " PREV LEVEL IS VERTICAL , GO TO RIGHT CHILD OF POINT " +node.getPoint().toString()); 
                    nearestPoint = searchNearest(node.rt,queryPoint,distQueryToNode,distQueryToRect,distQueryToNearestPoint,nearestPoint);
                }
            }
            
            else{
                //when line horizontal, check query point y < node point y, go left
                if( queryPoint.y() < node.getPoint().y()){
                   // System.out.println( " PREV LEVEL IS HORIZONTAL , GO TO LEFT CHILD OF POINT " +node.getPoint().toString());
                    nearestPoint = searchNearest(node.lb,queryPoint,distQueryToNode,distQueryToRect,distQueryToNearestPoint,nearestPoint);
                }
                else{
                   // System.out.println( " PREV LEVEL IS HORIZONTAL , GO TO RIGHT CHILD OF POINT " +node.getPoint().toString()); 
                    nearestPoint = searchNearest(node.rt,queryPoint,distQueryToNode,distQueryToRect,distQueryToNearestPoint,nearestPoint);
                }
            }
        }
        
        return nearestPoint;
    }
    
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean vertical;
        
        public Node(Point2D point,boolean v,RectHV rctHV){
            p = point;
            lb = null;
            rt = null;
            rect = rctHV;
            vertical = v;
        }
        
        private Point2D getPoint(){
            return p;
        } 
    }
}