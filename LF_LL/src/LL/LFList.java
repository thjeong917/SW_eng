package LL;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LFList<T> {
    public Node<T> head;
    public Node<T> tail;
    public int size;

    public LFList() {
        head = new Node<>(null);
        tail = new Node<>(null);
        head.key = Integer.MIN_VALUE;
        tail.key = Integer.MAX_VALUE;
        head.next.set(tail, false);
        tail.next.set(null, false);
        size = 0;
    }

    class Window<T> {
        Node<T> pred;
        Node<T> cur;

        Window(Node<T> p, Node<T> c) {
            this.pred = p;
            this.cur = c;
        }
    }

    public void traversal() {
        Node<T> cur;
        cur = head.next.getReference();

        while(cur.item != null){
            System.out.print(cur.item + " ");
            cur = cur.next.getReference();
        }
        System.out.println();
    }

    public Window find(int key) {
        Node<T> pred = null;
        Node<T> cur = null;
        Node<T> succ = null;
        boolean[] marked = {false};
        boolean snip;

        retry:
        while (true) {
            pred = head;
            cur = pred.next.getReference();
            while (true) {
                succ = cur.next.get(marked);
                while (marked[0]) {
                    snip = pred.next.compareAndSet(cur, succ, false, false);
                    if (!snip)
                        continue retry;
                    cur = succ;
                    succ = cur.next.get(marked);
                }
                if (cur.key >= key)
                    return new Window(pred, cur);
                pred = cur;
                cur = succ;
            }
        }
    }

    public boolean insert(T item) {
        Window<T> wnd;
        Node<T> pred;
        Node<T> cur;

        int key = item.hashCode();
        while (true) {
            wnd = find(key);
            pred = wnd.pred;
            cur = wnd.cur;

            if (cur.key == key)
                return false;
            else {
                Node<T> newnode = new Node<>(item);
                newnode.next = new AtomicMarkableReference<>(cur, false);
                if (pred.next.compareAndSet(cur, newnode, false, false))
                    return true;
            }
        }
    }

    public boolean delete(T item) {
        Window<T> wnd;
        Node<T> pred;
        Node<T> cur;
        Node<T> succ;

        int key = item.hashCode();
        boolean snip;
        while (true) {
            wnd = find(key);
            pred = wnd.pred;
            cur = wnd.cur;
            if (cur.key != key)
                return false;
            else {
                succ = cur.next.getReference();
                snip = cur.next.attemptMark(succ, true);
                if (!snip)
                    continue;
                pred.next.compareAndSet(cur, succ, false, false);
                return true;
            }
        }
    }

    public boolean search(T item) {
        Node<T> cur;
        Node<T> succ;

        int key = item.hashCode();
        boolean[] marked = {false};
        cur = head;
        while (cur.key < key) {
            cur = cur.next.getReference();
            succ = cur.next.get(marked);
        }

        if((cur.key == key) && !marked[0]) {
//            System.out.println("TRUE");
            return true;
        }
        else{
//            System.out.println("FALSE");
            return false;
        }
    }

}
