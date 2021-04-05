import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private Item[] queue;
    private int size;
    
    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
        size = 0;
    }
    
    private void resize(int newSize) {
        Item[] newQueue = (Item[]) new Object[newSize];
        for (int i = 0 ; i < size ; i++ ) {
            newQueue[i] = queue[i];
        }
        queue = newQueue;
    }
    
    private void swap(int i , int j) {
        Item temp = queue[i];
        queue[i] = queue[j];
        queue[j] = temp;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
        return size;
    }
    
    public void enqueue(Item item) {
        if(item == null) {
            throw new IllegalArgumentException("Null is an Illegal Argument!");
        }
        
        if(size == queue.length){
            resize(2*size);
        }
        
        queue[++size - 1] = item;
    }
    
    public Item dequeue() {
        if(isEmpty()) {
            throw new NoSuchElementException("Randomised Dequeue is empty");
        }
        
        int x = StdRandom.uniform(size);
        swap(x , size-1);
        Item item = queue[size-- - 1];
        queue[size] = null;
        
        if (size < queue.length / 4){
            resize(queue.length/2);
        }
        return item;
    }
    
    public Item sample() {
        if(isEmpty()) {
            throw new NoSuchElementException("Randomised Dequeue is empty");
        }
        
        int x = StdRandom.uniform(size);
        return queue[x];
    }
    
    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }
    
    private class RandomizedQueueIterator implements Iterator<Item> {
        
        Item[] toIterate;
        int current;
        int iteratorSize;
        
        public RandomizedQueueIterator() {
            current = 0;
            iteratorSize = size;
            toIterate = (Item[]) new Object[iteratorSize];
            for (int i = 0; i < iteratorSize; i++) {
                toIterate[i] = queue[i];
            }
            StdRandom.shuffle(toIterate);
        }
        
        @Override
        public boolean hasNext() {
            return current < size;
        }
    
        @Override
        public Item next() {
            if(!hasNext()) {
                throw new NoSuchElementException("No more element in iterator!");
            }
            Item item = toIterate[current];
            current++;
            return item;
        }
    
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported!");
        }
    }
    
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        rq.isEmpty();
        rq.size();
        rq.enqueue(1);
        rq.enqueue(2);
        rq.sample();
        rq.iterator();
        rq.iterator().hasNext();
        rq.iterator().next();
        rq.dequeue();
    }
}
