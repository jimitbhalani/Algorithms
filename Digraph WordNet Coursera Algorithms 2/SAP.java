import java.lang.*;
import java.util.*;

public class SAP {
    
    private Digraph digraph;
    private WordNet wordnet;
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G)
    {
       digraph = G;
    }
    
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w)
    {
        int shortestDist = 100000;
        boolean changed = false;
        BreadthFirstDirectedPaths bfPath_v = new BreadthFirstDirectedPaths(digraph,v);
        BreadthFirstDirectedPaths bfPath_w = new BreadthFirstDirectedPaths(digraph,w);
        for(int i = 0; i < digraph.V(); i++)
        {
            for(Integer adjvertex : digraph.adj(i))
            {                
                if(bfPath_v.hasPathTo(adjvertex) && bfPath_w.hasPathTo(adjvertex))
                {
                    if((bfPath_v.distTo(adjvertex) + bfPath_w.distTo(adjvertex)) < shortestDist)
                    {
                        shortestDist = bfPath_v.distTo(adjvertex) + bfPath_w.distTo(adjvertex);
                        if(!changed)
                            changed = true;
                    }
                    if(changed && (bfPath_v.distTo(adjvertex) + bfPath_w.distTo(adjvertex)) > shortestDist)
                    {
                        return shortestDist;
                    }
                }
            }
        }
        if(!changed)
            return -1;
        else
            return shortestDist;
    }
    
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w)
    {
        int commonAncestor = -1;
        BreadthFirstDirectedPaths bfPath_v = new BreadthFirstDirectedPaths(digraph,v);
        BreadthFirstDirectedPaths bfPath_w = new BreadthFirstDirectedPaths(digraph,w);
        int shortestPathLength = length(v,w);
        
        for(int i = 0; i < digraph.V(); i++)
        {
            for(Integer adjvertex : digraph.adj(i))
            {                
                if(bfPath_v.hasPathTo(adjvertex) && bfPath_w.hasPathTo(adjvertex))
                {
                    if((bfPath_v.distTo(adjvertex) + bfPath_w.distTo(adjvertex)) == shortestPathLength)
                    {
                        commonAncestor = adjvertex;
                    }
                }
            }
        }
        return commonAncestor;  
    }
    
    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
        int shortestDist = 100000;
        boolean changed = false;
        ArrayList<BreadthFirstDirectedPaths> bfPathsVList = new ArrayList<BreadthFirstDirectedPaths>();
        ArrayList<BreadthFirstDirectedPaths> bfPathsWList = new ArrayList<BreadthFirstDirectedPaths>();
        for(Integer vVertex : v)
        {
            BreadthFirstDirectedPaths bfPath_v = new BreadthFirstDirectedPaths(digraph,vVertex);
            bfPathsVList.add(bfPath_v);
        }
        for(Integer wVertex : w)
        {
            BreadthFirstDirectedPaths bfPath_w = new BreadthFirstDirectedPaths(digraph,wVertex);
            bfPathsWList.add(bfPath_w);
        }
        
        for(int i = 0; i < digraph.V(); i++)
        {
            for(Integer adjvertex : digraph.adj(i))
            {        
                for(BreadthFirstDirectedPaths vPath : bfPathsVList)
                {
                    for(BreadthFirstDirectedPaths wPath : bfPathsWList)
                    {
                        if(vPath.hasPathTo(adjvertex) && wPath.hasPathTo(adjvertex))
                        {
                            if((vPath.distTo(adjvertex) + wPath.distTo(adjvertex)) < shortestDist)
                            {
                                shortestDist = vPath.distTo(adjvertex) + wPath.distTo(adjvertex);
                                if(!changed)
                                    changed = true;
                            }
                        }      
                    }
                }
            }
        }
        if(!changed)
            return -1;
        else
            return shortestDist;
    }
    
    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        int commonAncestor = -1;
        ArrayList<BreadthFirstDirectedPaths> bfPathsVList = new ArrayList<BreadthFirstDirectedPaths>();
        ArrayList<BreadthFirstDirectedPaths> bfPathsWList = new ArrayList<BreadthFirstDirectedPaths>();
        for(Integer vVertex : v)
        {
            BreadthFirstDirectedPaths bfPath_v = new BreadthFirstDirectedPaths(digraph,vVertex);
            bfPathsVList.add(bfPath_v);
        }
        for(Integer wVertex : w)
        {
            BreadthFirstDirectedPaths bfPath_w = new BreadthFirstDirectedPaths(digraph,wVertex);
            bfPathsWList.add(bfPath_w);
        }
        int shortestPathLength = length(v,w);
        
        for(int i = 0; i < digraph.V(); i++)
        {
            for(Integer adjvertex : digraph.adj(i))
            {
                for(BreadthFirstDirectedPaths vPath : bfPathsVList)
                {
                    for(BreadthFirstDirectedPaths wPath : bfPathsWList)
                    {
                        if(vPath.hasPathTo(adjvertex) && wPath.hasPathTo(adjvertex))
                        {
                            if((vPath.distTo(adjvertex) + wPath.distTo(adjvertex)) == shortestPathLength)
                            {
                                commonAncestor = adjvertex;
                            }
                        }
                    }
                }
            }
            
        }
        return commonAncestor;
    }
    
    // do unit testing of this class
    public static void main(String[] args)
    {
    }
}