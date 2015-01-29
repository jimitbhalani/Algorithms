public class Percolation{
    
    private boolean[][] siteGrid; 
    private static final boolean   BLOCKED = false;
    private static final boolean   OPEN = true;
    private int   VIRTUAL_TOP_SITE;
    private int   VIRTUAL_BOTTOM_SITE;
    private int   GRID_SIZE;
    private WeightedQuickUnionUF WeightQU;
    private WeightedQuickUnionUF SecondQU;
    
    public Percolation(int N)        // create N-by-N grid, with all sites blocked
    {
        if(N <= 0)
            throw new IllegalArgumentException();      
        siteGrid = new boolean[N][N];
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                siteGrid[i][j] = BLOCKED;
            }
        }
        GRID_SIZE = N;
        WeightQU = new WeightedQuickUnionUF((GRID_SIZE*GRID_SIZE)+2);
        SecondQU = new WeightedQuickUnionUF((GRID_SIZE*GRID_SIZE)+1);
        VIRTUAL_TOP_SITE = GRID_SIZE*GRID_SIZE;
        VIRTUAL_BOTTOM_SITE = GRID_SIZE*GRID_SIZE + 1;
    }
    
    public void open(int i, int j)     // open site (row i, column j) if it is not already
    {
        //validate the incides first
        checkIndex(i,j);
        if(isOpen(i,j))return;
        //Open the site at row and column
        siteGrid[i-1][j-1] = OPEN;
        //if its not the first row, connect site to the adjacent cell        
        if(i-1 != 0 && isOpen(i-1,j))
        {   
            WeightQU.union(map2Dto1D(i-2,j-1),map2Dto1D(i-1,j-1));  
            SecondQU.union(map2Dto1D(i-2,j-1),map2Dto1D(i-1,j-1));       
        }      
        //else connect the site to the virtual top site
        else if(i-1 == 0)
        {          
            WeightQU.union(map2Dto1D(i-1,j-1),VIRTUAL_TOP_SITE);  
            SecondQU.union(map2Dto1D(i-1,j-1),VIRTUAL_TOP_SITE);           
        }
        //check if the site is bottom row
        if(i-1 != GRID_SIZE-1 && isOpen(i+1,j))
        {                           
            WeightQU.union(map2Dto1D(i,j-1),map2Dto1D(i-1,j-1));
            SecondQU.union(map2Dto1D(i,j-1),map2Dto1D(i-1,j-1));            
        }
        //else if it is the bottom row, connect to virtual bottom site
        else if(i-1 == GRID_SIZE-1)
        {  
            WeightQU.union(map2Dto1D(i-1,j-1),VIRTUAL_BOTTOM_SITE);             
        }
        //check if the column is in the left border
        if(j-1 != 0 && isOpen(i,j-1))
        {   
            WeightQU.union(map2Dto1D(i-1,j-2),map2Dto1D(i-1,j-1));
            SecondQU.union(map2Dto1D(i-1,j-2),map2Dto1D(i-1,j-1));            
        }
        //check if the column is on the right border
        if(j-1 != GRID_SIZE-1 && isOpen(i,j+1))
        {    
            WeightQU.union(map2Dto1D(i-1,j),map2Dto1D(i-1,j-1));
            SecondQU.union(map2Dto1D(i-1,j),map2Dto1D(i-1,j-1));           
        }
    }
    
    public boolean isOpen(int i, int j)    // is site (row i, column j) open?
    {   
        checkIndex(i,j);
        return (siteGrid[i-1][j-1] == OPEN);
    }
    
    public boolean isFull(int i, int j)    // is site (row i, column j) full?
    {
        checkIndex(i,j);
        return (SecondQU.connected(VIRTUAL_TOP_SITE,map2Dto1D(i-1,j-1)));
    }
    
    public boolean percolates()            // does the system percolate?
    {
        return (WeightQU.connected(VIRTUAL_TOP_SITE,VIRTUAL_BOTTOM_SITE));
    }
    
    //map the 2d grid to 1d array for the purpose of using the WeightedUF data structure
    private int map2Dto1D(int i,int j){
        return (j + (GRID_SIZE*i));
    }
    
    private void checkIndex(int i,int j){
        if( i <= 0 || j <= 0 || i > GRID_SIZE || j > GRID_SIZE)throw new java.lang.IndexOutOfBoundsException();
    }
}

