/* *****************************************************************************
 *  Name: Garrett Holman
 *  Date: 3/25/19
 *  Description: Deque is an implementation of a queue and stack in that you can
 *  remove from the front and back. Also implements an iterator for client code.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    private class Node {
        public Item value;
        public Node next;
        public Node previous;

        public Node(Item val) {
            value = val;
            next = null;
            previous = null;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("New item cannot be null");

        Node newNode = new Node(item);

        if (isEmpty()) {
            first = newNode;
            last = newNode;
        }
        else {
            newNode.next = first;
            first.previous = newNode;
            first = newNode;
        }

        size++;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("New item cannot be null");

        Node newNode = new Node(item);

        if (isEmpty()) {
            first = newNode;
            last = newNode;
        }
        else {
            last.next = newNode;
            newNode.previous = last;
            last = newNode;
        }

        size++;
    }

    public Item removeFirst() {
        if (size == 0)
            throw new NoSuchElementException("Deque is empty");

        // store return value, change front of queue
        Item returnVal = first.value;
        first = first.next;
        if (first != null)
            first.previous = null;

        size--;

        if (isEmpty())
            last = null;

        return returnVal;
    }

    public Item removeLast() {
        if (size == 0)
            throw new NoSuchElementException("Deque is empty");

        // store return value, change front of queue
        Item returnVal = last.value;
        last = last.previous;
        if (last != null)
            last.next = null;

        size--;

        if (isEmpty())
            last = null;

        return returnVal;
    }

    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException("Iterator does not supprt remove()");
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = current.value;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        // DEBUG AND TEST DEQUE

        Deque<Integer> deque = new Deque<Integer>();

        deque.addFirst(1);
        // StdOut.print(deque.removeFirst());
        // deque.addFirst(4);
        // deque.addFirst(2);
        deque.addLast(3);
        // StdOut.print(deque.removeFirst());
        StdOut.print(deque.removeLast());
        deque.addLast(5);
        deque.addLast(6);

        for (int i : deque) {
            StdOut.print(i);
        }


        while (!deque.isEmpty()) {
            StdOut.print(deque.removeLast());
        }

    }
}
