package LL;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class Node<T>{
    T item;
    int key;
    AtomicMarkableReference<Node<T>> next;

    public Node(T item){
        this.item = item;
        if(item != null)
            this.key = item.hashCode();

        next = new AtomicMarkableReference<>(null, false);
    }
}
