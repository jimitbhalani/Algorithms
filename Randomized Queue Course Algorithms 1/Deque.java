import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    
    private Node first, last;  
    private int size;
    
    private class Node{
        public Item item;
        public Node next;
        public Node prev;
    }
    
    public Deque()                           // construct an empty deque
    {
        first = null;
        last =  null;
        size = 0;
    }
    public boolean isEmpty()                 // is the deque empty?
    {
        return (last == null && first == null);
    }
    public int size()                        // return the number of items on the deque
    {
        return size;
    }
    public void addFirst(Item item)          // insert the item at the front
    {
        if(item == null)
            throw new NullPointerException();
        
        size++;
        if(isEmpty()){
            first = new Node();
            first.item = item;
            first.next = null;
            first.prev = null;
            last = first;
        }else{
            Node tempFirst = first;
            Node newNode = new Node();
            newNode.item = item;
            newNode.next = tempFirst;
            newNode.prev = null;
            tempFirst.prev = newNode;
            first = newNode;
        }
    }
    public void addLast(Item item)           // insert the item at the end
    {
        if(item == null)
            throw new NullPointerException();
        
        size++;
        if(isEmpty()){
            last = new Node();
            last.item = item;
            last.next = null;
            last.prev = null;
            first = last;
        }else{
            Node oldLast = last;
            last = new Node();
            last.item = item;
            last.next = null;
            last.prev = oldLast;
            oldLast.next = last;
        }
    }
    public Item removeFirst()                // delete and return the item at the front
    {
        size--;
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        if(first.next != null){
            Node oldFirst = first;
            Item item = oldFirst.item;
            first = first.next;
            first.prev = null;
            oldFirst.next = null;
            return item;
        }else{
            Item item = first.item;
            last = null;
            first = null;
            return item;
        }       
    }
    public Item removeLast()                 // delete and return the item at the end
    {
        size--;
        if(isEmpty()){
            throw new NoSuchElementException();
        }   
        if(last.prev != null){
            Node oldLast = last;
            Item item = oldLast.item;
            last = last.prev;
            last.next = null;
            oldLast.prev = null;
            return item;
        }else{
            Item item = last.item;
            last = null;
            first = null;
            return item;
        }
    }
    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new ListIterator();
    }
    private class ListIterator implements Iterator<Item>{
        
        private Node current = first;
        public boolean hasNext(){
            return current != null;
        }
        
        public void remove(){
            throw new UnsupportedOperationException();
        }
        
        public Item next(){
            if(current == null)
                throw new NoSuchElementException();
            
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}