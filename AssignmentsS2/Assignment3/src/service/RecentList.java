package AssignmentsS2.Assignment3.src.service;

import java.util.LinkedList;

public class RecentList<T> {
    private LinkedList<T> list = new LinkedList<>();
    private final int MAX_SIZE = 10;

    public int size() { return list.size(); }

    public boolean isEmpty() { return list.isEmpty(); }

    public void addRecent(T item) {
        if (item == null) 
            return;

        list.addFirst(item);

        if (list.size() > MAX_SIZE) 
            list.removeLast();
     }

    public void printRecent(int maxToShow) {
        if (list.isEmpty()) {
            System.out.println(">. No recent items.");
            return;
        }

        if (maxToShow <= 0) {
            System.out.println(">. Nothing to display.");
            return;
        }

        int limit = Math.min(maxToShow, list.size());

        for (int i = 0; i < limit; i++) {
            System.out.println(">. " + (i + 1) + ". " + list.get(i));
        }
    }
}
