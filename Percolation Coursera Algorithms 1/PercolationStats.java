public class PercolationStats{
    
    private int GRID_SIZE;
    private int TOTAL_COMPUTATIONS;
    private double[] thresholdList;
    
    public PercolationStats(int N,int T){
        if(N <= 0 || T <= 0)
            throw new java.lang.IllegalArgumentException();        
        thresholdList = new double[T];
        GRID_SIZE = N;
        TOTAL_COMPUTATIONS = T;      
        for(int i = 0; i < T; i++){           
            double counter = 0;
            Percolation p = new Percolation(N);
            while(!p.percolates()){             
                int topRowRandom = StdRandom.uniform(N)+1;
                int bottomRowRandom = StdRandom.uniform(N)+1;
                if(!p.isOpen(topRowRandom,bottomRowRandom)){
                    p.open(topRowRandom,bottomRowRandom);  
                    counter++;                   
                }
            }
            thresholdList[i] = (counter)/(N*N);
        }
    }
    
    public double mean(){
        return StdStats.mean(thresholdList);
    }
    
    public double stddev(){
        return StdStats.stddev(thresholdList);
    }
    
    public double confidenceLo()             // returns lower bound of the 95% confidence interval
    {
        return (mean() - (1.96 * stddev())/(Math.sqrt(TOTAL_COMPUTATIONS)));
    }
    public double confidenceHi()             // returns upper bound of the 95% confidence interval
    {
        return (mean() + (1.96 * stddev())/(Math.sqrt(TOTAL_COMPUTATIONS)));
    }
    
    public static void main(String[] args){
        int SIZE = Integer.parseInt(args[0]);
        int COMPUTATIONS = Integer.parseInt(args[1]); 
        PercolationStats ps = new PercolationStats(SIZE, COMPUTATIONS);   
        double conLo = ps.confidenceLo();
        double conHi = ps.confidenceHi();
        System.out.println("mean = " + ps.mean());
        System.out.println("stddev = " + ps.stddev());
        System.out.println("95% confidence interval = " + conLo + ", " + conHi);
    }
}