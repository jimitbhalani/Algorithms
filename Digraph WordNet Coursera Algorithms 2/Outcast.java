import java.util.*;

public class Outcast {
   
   private WordNet wordnet;
   
   public Outcast(WordNet wordnet)         // constructor takes a WordNet object
   {
       this.wordnet = wordnet;
   }
   
   public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
   {
       int distance = -1;
       String noun = null;
       
       for(String noun1 : wordnet.nouns())
       {
           int sum = 0;
           for(String noun2 : nouns)
           {              
               sum += wordnet.distance(noun1,noun2);
               if(sum > distance)
               {
                   distance = sum;
                   noun = noun2;
               }
           }
       }
       return noun;
   }
   
   public static void main(String[] args)  // see test client below
   {
   }
}