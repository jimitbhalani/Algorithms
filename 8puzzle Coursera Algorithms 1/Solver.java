import java.util.Iterator;
import java.lang.Math;
import java.lang.Comparable;

public class Solver {
    
    private MinPQ<Node> minPQ;
    private MinPQ<Node> twinPQ;
    private Queue<Board> solutionQ = new Queue<Board>();
    private Node initialSearchNode;
    private Node initialTwinSearchNode;
    private Node searchNode;
    private Node twinSearchNode;
    private Node resultNode;
    
    public Solver(Board initial)            // find a solution to the initial board (using the A* algorithm)
    {
        //step 1
        //insert the initial search node into minPQ
        
        if(initial.isGoal()){
            searchNode = new Node(initial,null);
            resultNode = searchNode;
        }else{
            
            //insert the initial board into pq
            minPQ = new MinPQ<Node>();
            twinPQ = new MinPQ<Node>();
            searchNode = new Node(initial,null);
            minPQ.insert(searchNode);
            twinSearchNode = new Node(initial.twin(),null);
            twinPQ.insert(twinSearchNode);
            while(true){               
                
                //delete this min pri search node     
                Node minNode = null;
                Node twinMinNode = null;
                if(!minPQ.isEmpty()){
                    minNode = minPQ.delMin();
                    solutionQ.enqueue(minNode.currentBoard);
                }    
                if(!twinPQ.isEmpty()){
                    twinMinNode = twinPQ.delMin();
                }      
                //check if the goal is reached
                if(minNode.currentBoard.isGoal()){
                    resultNode = minNode;
                    break;
                }
                
                if(twinMinNode.currentBoard.isGoal()){
                    resultNode = null;
                    break;
                }     
                //set the prev search nodes
                if(minNode.prevSearchNode != null)
                    initialSearchNode = minNode.prevSearchNode; 
                else
                    initialSearchNode = searchNode;
                
                if(twinMinNode.prevSearchNode != null)
                    initialTwinSearchNode = twinMinNode.prevSearchNode; 
                else
                    initialTwinSearchNode = twinSearchNode;
                
                //iterate through all the neighbors
                Iterator<Board> stackIter = minNode.currentBoard.neighbors().iterator();           
                while(stackIter.hasNext()){
                    Board firstBoard = stackIter.next();
                    searchNode = new Node(firstBoard,minNode);                    
                    if(!(searchNode.currentBoard.equals(initialSearchNode.currentBoard))){
                        minPQ.insert(searchNode);
                    }
                } 
                
                Iterator<Board> stackItertwin = twinMinNode.currentBoard.neighbors().iterator();           
                while(stackItertwin.hasNext()){
                    Board firstBoard = stackItertwin.next();
                    twinSearchNode = new Node(firstBoard,twinMinNode);                    
                    if(!(twinSearchNode.currentBoard.equals(initialTwinSearchNode.currentBoard))){     
                        twinPQ.insert(twinSearchNode);
                    }
                } 
            }
            
        }
    }
    public boolean isSolvable()             // is the initial board solvable?
    {
        return resultNode != null;
    }
    
    public int moves()                      // min number of moves to solve initial board; -1 if no solution
    {
        if(resultNode != null && isSolvable())
            return resultNode.moves;
        else return -1;
    }
    
    public Iterable<Board> solution()       // sequence of boards in a shortest solution; null if no solution
    {
        if(!isSolvable())
            return null;
        
        return solutionQ;
    } 
    
    private class Node implements Comparable<Node>{
        private final Board currentBoard;
        private final Node prevSearchNode;
        private final int moves;
        private final int priority;
        
        public Node(Board currBoard,Node prevNode){
            currentBoard = currBoard;
            prevSearchNode = prevNode;
            if(prevSearchNode == null ) moves = 0;
            else moves = prevSearchNode.moves + 1;
            priority = moves + currentBoard.manhattan();
        }  
        
        public int compareTo(Node other){
            return this.priority - other.priority;
        }
    }
    
    public static void main(String[] args)  // solve a slider puzzle (given below)
    {
      // create initial board from file
        // In in = new In(args[0]);
        //int N = in.readInt();
        int N = 3;
        int[][] blocks = new int[N][N];
        /*for (int i = 0; i < N; i++){
         for (int j = 0; j < N; j++){
         blocks[i][j] = j+(i*N+1);
         }
         }
         blocks[N-1][N-1] = 0;*/
        
        blocks[0][0] = 1;
        blocks[0][1] = 3;
        blocks[0][2] = 2;
        blocks[1][0] = 5;
        blocks[1][1] = 7;
        blocks[1][2] = 8;
        blocks[2][0] = 6;
        blocks[2][1] = 4;
        blocks[2][2] = 0;
        
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
      //   print solution to standard output
         if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
        StdOut.println("Minimum number of moves = " + solver.moves());       
        for (Board board : solver.solution())
            StdOut.println(board);
        }
    }
}