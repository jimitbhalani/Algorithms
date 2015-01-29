import java.util.Iterator;
import java.util.NoSuchElementException;
public class RandomizedQueue<Item> implements Iterable<Item> { 
    
    private int capacity = 1;
    private Item[] queue;
    private int back;
    private int size;
    
    public RandomizedQueue()                 // construct an empty randomized queue
    {
        queue = (Item[])new Object[capacity];
        back = capacity-1;
        size = 0;
    }
    public boolean isEmpty()                 // is the queue empty?
    {
        return (size == 0);
    }
    public int size()                        // return the number of items on the queue
    {
        return size;
    }
    public void enqueue(Item item)           // add the item
    {   
        if(item == null)
            throw new NullPointerException();
        
        if(size() == capacity){
            capacity = 2*capacity;
            resize(capacity);
        }
        back = (back+1)%queue.length;
        queue[back] = item;
        ++size; 
    }
    public Item dequeue()                    // delete and return a random item
    {
        if(isEmpty())
            throw new NoSuchElementException();
        
        int randomIndex = StdRandom.uniform(size);
        Item item = queue[randomIndex];
        if(randomIndex != size - 1){
            Item lastItem = queue[size-1];
            queue[randomIndex] = lastItem;
            queue[size-1] = null;
            back--;
        }else{
            queue[randomIndex] = null;
            back--;
        }   
        --size;
        return item;
    }
    
    public Item sample()                     // return (but do not delete) a random item
    {
        if(isEmpty())
            throw new NoSuchElementException();
        
        int randomIndex = StdRandom.uniform(size);
        Item item = queue[randomIndex];
        return item;
    }
    
    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new QueueIterator();
    }
    
    private class QueueIterator implements Iterator<Item>{
        private int i = 0;
        
        public boolean hasNext(){
            return (i <= back);
        }
        
        public void remove(){
            throw new UnsupportedOperationException();
        }
        
        public Item next(){ 
            if(queue[i] == null)
                throw new NoSuchElementException();
            
            Item item = queue[i];
            i++;
            return item;
        }
    }
    
    private void resize(int newCapacity){
        Item[] copy = (Item[]) new Object[newCapacity];
        for(int i = 0; i < size; i++){
            copy[i] = queue[i];
        }
        back = size - 1;
        queue = copy;
    }
}