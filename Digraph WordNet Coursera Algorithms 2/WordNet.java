import java.util.*;

public class WordNet {
    
    private HashMap<Integer,String> synsetStringMap = new HashMap<Integer,String>(); //map for synset id and its correspoding nouns string
    private HashMap<String,ArrayList<Integer>> nounsToSynsetMap = new HashMap<String,ArrayList<Integer>>(); //map for common nouns to list of its synset ids
    private Digraph digraph;
    private SAP sap;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)
    {        
        if(synsets == null || hypernyms == null)
        {
            throw new java.lang.NullPointerException();    
        }
        
        In in = new In(synsets);     
        In hypin = new In(hypernyms);
        
        //for synsets
        ConstructSynsetMap(in);
        
        //initialize the digraph
        digraph = new Digraph(synsetStringMap.size());
        
        //for hypernyms
        ConstructHypernyms(hypin);
        
        //initialize sap ds
        sap = new SAP(digraph);
    }
    
    private void ConstructSynsetMap(In in)
    {
        while(in.hasNextLine())
        {
            String line = in.readLine();
            String[] linesArray = line.split(",");
            String id = linesArray[0];
            int idd = Integer.parseInt(id);      
            if(!synsetStringMap.containsKey(idd))
            {
                synsetStringMap.put(idd,linesArray[1]);
            }
            String[] nouns = linesArray[1].split(" "); 
            for(int i = 0; i < nouns.length; i++)
            {
                //this map creates a list of synset ids to one common key noun ie if the same noun is associated with different synset ids
                if(!nounsToSynsetMap.containsKey(nouns[i]))
                {
                    nounsToSynsetMap.put(nouns[i],new ArrayList<Integer>());
                    nounsToSynsetMap.get(nouns[i]).add(idd);
                }
                else if(nounsToSynsetMap.containsKey(nouns[i]))
                {
                    nounsToSynsetMap.get(nouns[i]).add(idd);
                }
            }    
        }
    }
    
    private void ConstructHypernyms(In hypin)
    {
        while(hypin.hasNextLine())
        {
            String line = hypin.readLine();
            String[] linesArray = line.split(",");
            String id = linesArray[0];
            int idd = Integer.parseInt(id); //synset id
            if(linesArray.length > 1)
            {
                for(int i = 1; i < linesArray.length; i++)
                {
                    String hid = linesArray[i];
                    int hidd = Integer.parseInt(hid);
                    digraph.addEdge(idd,hidd);                   
                }
            }
        }     
    }
    
    // returns all WordNet nouns
    public Iterable<String> nouns()
    {
        //iterate through all the keys in the synsets map and fetch the nouns. add them to the set
        SET<String> nounsSet = new SET<String>();
        for(String noun : nounsToSynsetMap.keySet())
        {
            nounsSet.add(noun);
        }
        return nounsSet;
    }
    
    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        for(String noun : nouns())
        {
            if(noun.equals(word))
                return true;
        }
        return false;
    }
    
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        //first get the vertex int of nounA and nounB
        ArrayList<Integer> nounAsynsets = nounsToSynsetMap.get(nounA);
        ArrayList<Integer> nounBsynsets = nounsToSynsetMap.get(nounB);
        int length = -1;
        
        //get the shortest distance of nounA and nounB
        if(nounAsynsets.size() == 1 && nounBsynsets.size() == 1)
        {
            length = sap.length(nounAsynsets.get(0),nounBsynsets.get(0));
        }
        else
        {
            length = sap.length(nounAsynsets,nounBsynsets);
        }
        //System.out.println(length);
        return length;
    }
    
    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        ArrayList<Integer> nounAsynsets = nounsToSynsetMap.get(nounA);
        ArrayList<Integer> nounBsynsets = nounsToSynsetMap.get(nounB);
        int commonAncestor = -1;
        if(nounAsynsets.size() == 1 && nounBsynsets.size() == 1)
        {
            commonAncestor = sap.ancestor(nounAsynsets.get(0),nounBsynsets.get(0));
        }
        else
        {
            commonAncestor = sap.ancestor(nounAsynsets,nounBsynsets);
        }
        //System.out.println("common ancestor" +commonAncestor+ " name " +synsetStringMap.get(commonAncestor));
        return synsetStringMap.get(commonAncestor);
    }
    
    // do unit testing of this class
    public static void main(String[] args)
    {
    }
}