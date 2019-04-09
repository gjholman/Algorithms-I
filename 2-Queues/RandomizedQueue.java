/* *****************************************************************************
 *  Name: Garrett Holman
 *  Date: 3/26/19
 *  Description: Randomized queue, where the dequeue removes an element at random
 *  and returns, and sample returns a random element, but does not remove. This
 *  is achieved by implementing a resizing array, in order to achieve a constant
 *  lookup time for elements at a given index, but keeping all functions a
 *  constant amortized time, due to the resizing aspect
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;   // Array of items
    private int size;   // number of elements in queue (and tracks last element

    /**
     * Initializes empty array
     */
    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        size = 0;
    }

    /**
     * Is the stack empty?
     *
     * @return if size is 0; otherwise false
     */
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Returns number of items in array
     *
     * @return number of items in array
     */
    public int size() {
        return size;
    }

    /**
     * Add an item to the array
     *
     * @param item is the item to add
     */
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("New item cannot be null");
        if (size == a.length) resize(2 * a.length);
        a[size++] = item;
    }

    /**
     * Returns a random item from the array and removes from the array
     *
     * @return random item from array
     */
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        int randomIndex = StdRandom.uniform(size);
        Item randomItem = a[randomIndex];
        a[randomIndex] = a[size - 1];     // To fill holes
        a[size - 1] = null;              // To prevent loitering
        size--;
        // resize if storage is at 25% capacity
        if (size > 0 && size == a.length / 4) resize(a.length / 2);
        return randomItem;
    }

    /**
     * Returns a random item from the array   * does not remove *
     *
     * @return random item from the array
     */
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        int randomIndex = StdRandom.uniform(size);
        Item randomItem = a[randomIndex];
        return randomItem;
    }

    /**
     * Implement iterator after shuffling so that each iterator is independent
     *
     * @return new list iterator (see below)
     */
    public Iterator<Item> iterator() {
        // Shuffle so that each iterator is different!
        StdRandom.shuffle(a, 0, size);
        return new ListIterator();
    }

    /**
     * List Iterator class to iterate through all elements
     */
    private class ListIterator implements Iterator<Item> {
        private int i;

        public ListIterator() {
            i = 0;
        }

        public boolean hasNext() {
            return i < size;
        }

        public void remove() {
            throw new UnsupportedOperationException("Iterator does not supprt remove()");
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return a[i++];

        }
    }

    /**
     * resize array to prevent overflow and manage memory more efficiently Also allow the array
     * appending and removing to take constant amortized time.
     *
     * @param capacity is the new size of the array
     */
    private void resize(int capacity) {
        // assert capacity >= size;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<Integer>();
        for (int i = 0; i < 6; i++) {
            randomizedQueue.enqueue(i);
        }
        while (!randomizedQueue.isEmpty()) {
            StdOut.print(randomizedQueue.dequeue());
        }
        StdOut.print("\n");
        for (int i = 0; i < 10; i++) {
            randomizedQueue.enqueue(i);
        }
        for (int i : randomizedQueue) {
            StdOut.print(i);
        }
        StdOut.print("\n");
        for (int i : randomizedQueue) {
            StdOut.print(i);
        }
        StdOut.print("\n");
        for (int i : randomizedQueue) {
            StdOut.print(i);
        }

    }
}
