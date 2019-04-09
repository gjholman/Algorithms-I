/* *****************************************************************************
 *  Name: Garrett Holman
 *  Date: 3/26/19
 *  Description: Permutation is a client class that utilizes the randomized queue
 *  class and prints out variables from a .txt file randomly (and at most once)
 *
 *  Uses:
 *
 *  % java-algs4 Permutation k < somefile.txt
 *
 *  Example:
 *  % java-algs4 Permutation 3 < distinct.txt
 *  E
 *  I
 *  G
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {

    /**
     * Display a random permutation of the strings in a text file
     *
     * @param args "k < *.txt" where 'k' is the length of the permutation, '<' is necessary, and
     *             '*.txt' is a file containing strings
     */
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            // StdOut.println(s);
            randomizedQueue.enqueue(s);
        }

        for (int i = 0; i < k; i++)
            StdOut.println(randomizedQueue.dequeue());

    }
}
