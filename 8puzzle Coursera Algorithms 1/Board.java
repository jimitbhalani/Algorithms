import java.lang.Math;
import java.util.Stack;

public class Board {
    
    private final int[][] myboard;
    
    public Board(int[][] blocks)           // construct a board from an N-by-N array of blocks
    {
        myboard = blocks;
    }
    
    // (where blocks[i][j] = block in row i, column j)
    public int dimension()                 // board dimension N
    {
        return myboard.length;
    }
    
    public int hamming()                   // number of blocks out of place
    {
        int count = 0;
        for(int i =0; i < dimension(); i++){
            for(int j =0; j < dimension(); j++){
                if(i == dimension()-1 && j == dimension()-1){
                    if(!(myboard[i][j] == 0))
                        count++;
                }
                else if(!(myboard[i][j] == 0) && !(myboard[i][j] == j+(i*dimension()+1)))
                    count++;
            }
        }
        return count;
    }
    
    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        int count = 0;
        for(int i =0; i < dimension(); i++){
            for(int j =0; j < dimension(); j++){
                if(!(myboard[i][j] == 0) && !(myboard[i][j] == j+(i*dimension()+1))){
                    int boardNo = myboard[i][j]; 
                    for(int k = 0; k < dimension(); k++){
                        for(int l =0; l < dimension(); l++){
                            if( (l+(k*dimension()+1)) == boardNo )
                            {
                                int distance = Math.abs(i-k) + Math.abs(j-l);
                                count += distance;
                            }
                        }
                    }
                }
            }
        }
        return count;
    }
    
    public boolean isGoal()                // is this board the goal board?
    {
        for(int i =0; i < dimension(); i++){
            for(int j =0; j < dimension(); j++){
                if(i == dimension()-1 && j == dimension()-1){
                    if(!(myboard[i][j] == 0))
                        return false;
                }
                else if(!(myboard[i][j] == j+(i*dimension()+1)))
                    return false;         
            }
        }
        return true;
    }
    
    public Board twin()                    // a board obtained by exchanging two adjacent blocks in the same row
    {
        int[][] twinBlocks = new int[dimension()][dimension()];    
        deepCopy(myboard,twinBlocks,dimension());       
        if(dimension() <= 1)
            return new Board(twinBlocks);
        
        int row = 0;
        int col = 0;
        int lastValue = myboard[0][0];
        int value = 0;
        
        outerloop:
            for(row = 0; row < dimension(); row++){
            for(col = 0; col < dimension(); col++){
                value = myboard[row][col];
                if(value != 0 && lastValue != 0 && col > 0){
                    break outerloop;
                }
                lastValue = value;
            }
        } 
            twinBlocks[row][col] = lastValue;
            twinBlocks[row][col-1] = value;
            return new Board(twinBlocks);
    }
    
    public boolean equals(Object y)        // does this board equal y?
    {
        if(y == this) return true;
        if( y == null) return false;
        if( y.getClass() != this.getClass()) return false;
        
        Board thatBoard = (Board)y;
        if(this.dimension() != thatBoard.dimension())
            return false;
        
        for(int i = 0; i < dimension(); i++){
            for(int j =0; j < dimension(); j++){            
                if(!(this.myboard[i][j] == thatBoard.myboard[i][j]))
                    return false;
            }
        }
        return true;
    }
    
    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Stack<Board> boardStack = new Stack<Board>();
        for(int i =0; i < dimension(); i++){
            for(int j =0; j < dimension(); j++){
                if(myboard[i][j] == 0){
                    //find top element top row
                    boolean done = false;
                    while(!done){
                        if( i - 1 >= 0 ){
                            //swap top item with 0
                            int[][] twinBoard = new int[dimension()][dimension()];
                            deepCopy(myboard,twinBoard,dimension());  
                            Board neighbor = new Board(twinBoard);    
                            int temp = twinBoard[i][j];
                            twinBoard[i][j] = twinBoard[i-1][j];
                            twinBoard[i-1][j] = temp;
                            boardStack.push(neighbor);
                        }                       
                        //find bottom element bottom row
                        if( i + 1 < dimension()){
                            int[][] twinBoard = new int[dimension()][dimension()];
                            deepCopy(myboard,twinBoard,dimension());  
                            Board neighbor = new Board(twinBoard);    
                            int temp = twinBoard[i][j];
                            twinBoard[i][j] = twinBoard[i+1][j];
                            twinBoard[i+1][j] = temp;
                            boardStack.push(neighbor);
                        }                        
                        //left element left column
                        if( j-1 >= 0){
                            int[][] twinBoard = new int[dimension()][dimension()];
                            deepCopy(myboard,twinBoard,dimension());  
                            Board neighbor = new Board(twinBoard);    
                            int temp = twinBoard[i][j];
                            twinBoard[i][j] = twinBoard[i][j-1];
                            twinBoard[i][j-1] = temp;
                            boardStack.push(neighbor);
                        }                        
                        if( j+1 < dimension()){
                            int[][] twinBoard = new int[dimension()][dimension()];
                            deepCopy(myboard,twinBoard,dimension());  
                            Board neighbor = new Board(twinBoard);    
                            int temp = twinBoard[i][j];
                            twinBoard[i][j] = twinBoard[i][j+1];
                            twinBoard[i][j+1] = temp;
                            boardStack.push(neighbor);
                        }
                        done = true;
                    }
                }
            }
        }  
        return boardStack;
    }
    
    public String toString()               // string representation of the board (in the output format specified below)
    {
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                s.append(String.format("%2d ", myboard[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    private void deepCopy(int[][] ogArray,int[][] newArray,int dimension){
        for(int m = 0; m < dimension; m++){
            for(int n = 0; n < dimension; n++){
                newArray[m][n] = ogArray[m][n];
            }
        }
    }
    
    public static void main(String[] args) {
        
    }
}