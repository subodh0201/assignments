import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    
    private Node first, last;
    private int size;
    
    private class Node {
        Item item;
        Node next, prev;
    }
    

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }
    
    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
        return size;
    }
 
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null is not a valid argument!");
        }
        
        if (isEmpty()) {
            first = new Node();
            first.item = item;
            first.prev = null;
            first.next = null;
            last = first;
        } else {
            Node oldFirst = first;
            first = new Node();
            first.item = item;
            first.next = oldFirst;
            first.prev = null;
            oldFirst.prev = first;
        }
        size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null is not a valid argument!");
        }
        
        if (isEmpty()) {
            first = new Node();
            first.item = item;
            first.prev = null;
            first.next = null;
            last = first;
        } else {
            Node oldLast = last;
            last = new Node();
            last.item = item;
            last.next = null;
            last.prev = oldLast;
            oldLast.next = last;
        }
        size++;
    }
    
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Can not remove from an empty deque!");
        }
        
        Item item = first.item;
        first = first.next;
        size--;
        if (isEmpty()) {
            last = null;
            first = null;
        }
        if(!isEmpty()) {
            first.prev = null;
        }
        return item;
    }
 
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Can not remove from an empty deque!");
        }
    
        Item item = last.item;
        last = last.prev;
        size--;
        if (isEmpty()) {
            first = null;
            last = null;
        }
        if(!isEmpty()) {
            last.next = null;
        }
        return item;
    }
    
    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    private class DequeIterator implements Iterator<Item> {
        
        Node current;
        
        public DequeIterator() {
            current = first;
        }
    
        @Override
        public boolean hasNext() {
            return current != null;
        }
    
        @Override
        public Item next() {
            
            if(!hasNext()) {
                throw new NoSuchElementException("No more elements in iterator!");
            }
            
            Item item = current.item;
            current = current.next;
            return item;
        }
    
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported!");
        }
    }
    
    public static void main(String[] args) {
        Deque<Integer> d = new Deque<Integer>();
        StdOut.println(d.isEmpty());
        d.addFirst(1);
        d.addLast(2);
        StdOut.println(d.size());
        StdOut.println(d.removeLast());
        StdOut.println(d.removeFirst());
        StdOut.println(d.iterator());
        d.iterator().hasNext();
        d.iterator().next();
    }
}
