package BST;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Implemented binary search tree w/ Read-Write locking
 */

public class RWBST implements BST {
    private Node root;
    private ReentrantLock glock;

    public RWBST() {
        root = new Node(-1);
        // root = null;]
        glock = new ReentrantLock();
    }

    public void initialize() {
        root = new Node(-1);
    }

    public void insert(int data){
        root.writeLock.lock();
        root = insert(root, data);
        root.writeLock.unlock();
    }

    public Node insert(Node root, int toInsert) {
        Node cur;
        Node prev;
        Node newnode = new Node(toInsert);

        root.writeLock.lock();
        cur = root;
        if (cur.data == -1) {
            cur.data = toInsert;
            cur.writeLock.unlock();
            return root;
        }

        while (true) {
            if (newnode.data < cur.data) {
                if (cur.left != null) {
                    cur.left.writeLock.lock();
                    prev = cur;
                    cur = cur.left;
                    prev.writeLock.unlock();
                } else {
                    cur.left = newnode;
                    cur.writeLock.unlock();
                    break;
                }
            } else if (newnode.data > cur.data) {
                if (cur.right != null) {
                    cur.right.writeLock.lock();
                    prev = cur;
                    cur = cur.right;
                    prev.writeLock.unlock();
                } else {
                    cur.right = newnode;
                    cur.writeLock.unlock();
                    break;
                }
            } else {
                cur.writeLock.unlock();
//                cur.data = newnode.data;
//                cur.writeLock.unlock();
                break;
            }
        }

        return root;
    }

    /*****************************************************
     *
     * SEARCH
     *
     ******************************************************/

    // you don't need to implement hand-over-hand lock for this function.
    public int findMin() {
        if (root == null) {
            throw new RuntimeException("cannot findMin.");
        }
        Node n = root;
        while (n.left != null) {
            n = n.left;
        }
        return n.data;
    }

    public boolean search(int toSearch) {
        // String a = Thread.currentThread().getName();
        // System.out.println(a + " " + toSearch);
        return search(root, toSearch);
    }

    private boolean search(Node root, int toSearch) {
        Node cur;
        Node prev;

        // glock.lock();
        //
        // if (root == null) { // at first when tree is empty || at last
        // try {
        // return false;
        // } finally {
        // glock.unlock();
        // }
        // }

        root.readLock.lock();
        cur = root;
        // glock.unlock();

        if (cur.data == -1) {
            cur.readLock.unlock();
//			System.out.println("NO");
            return false;
        }

        while (true) {
            if (cur == null) {
//				System.out.println("NO");
                return false;
            } else if (toSearch == cur.data) {
                cur.readLock.unlock();
//				System.out.println("HIT");
                return true;
            } else if (toSearch < cur.data) {
                if (cur.left != null) {
                    cur.left.readLock.lock();
                    prev = cur;
                    cur = cur.left;
                    prev.readLock.unlock();
                } else {
                    cur.readLock.unlock();
//					System.out.println("NO");
                    return false;
                }
            } else if (toSearch > cur.data) {
                if (cur.right != null) {
                    cur.right.readLock.lock();
                    prev = cur;
                    cur = cur.right;
                    prev.readLock.unlock();
                } else {
                    cur.readLock.unlock();
//					System.out.println("NO");
                    return false;
                }
            }
        }
    }

    /*****************************************************
     *
     * DELETE
     *
     ******************************************************/
    public boolean delete(int toDelete) {
//        String a = Thread.currentThread().getName();
//        System.out.println(a + " " + toDelete);
        // System.out.println();

        Node parent = null;
        Node child = null;
        Node cur = null;

        if (root == null)
            return false;

        root.writeLock.lock();
        if (root.data == -1) {
            root.writeLock.unlock();
            return false;
        }
        cur = root;

        while (cur != null && cur.data != toDelete) {
            parent = cur;
            if (toDelete < cur.data) {
                if (cur.left != null) {
                    cur.left.writeLock.lock();
                    cur = cur.left; // Scan left
                    parent.writeLock.unlock();
                } else if (cur.left == null) {
                    cur.writeLock.unlock();
                    cur = null;
                    return false;
                }
            } else {
                if (cur.right != null) {
                    cur.right.writeLock.lock();
                    cur = cur.right; // Scan right
                    parent.writeLock.unlock();
                } else if (cur.right == null) {
                    cur.writeLock.unlock();
                    cur = null;
                    return false;
                }
            }
        }

        if (cur == null)
            return false;

        // cur lock on
        // key found, work with their children
        if (cur.left == null && cur.right == null) {
            if (parent == null) {
                cur.writeLock.unlock();
                root = null;
            } else {
                parent.writeLock.lock();
                if (toDelete < parent.data) {
                    parent.left = null;
                    cur.writeLock.unlock();
                    parent.writeLock.unlock();
                } else {
                    parent.right = null;
                    cur.writeLock.unlock();
                    parent.writeLock.unlock();
                }
            }
        } else if (cur.left == null) { // If left child is null, get right child
            child = cur.right;
            child.writeLock.lock();
            swapKeys(cur, child);
            cur.left = child.left;
            cur.right = child.right;
            cur.writeLock.unlock();
            child.writeLock.unlock();
        } else if (cur.right == null) { // If right child is null, get left
            // child
            child = cur.left;
            child.writeLock.lock();
            swapKeys(cur, child);
            cur.left = child.left;
            cur.right = child.right;
            cur.writeLock.unlock();
            child.writeLock.unlock();
        } else { // If two children
            child = cur.left;
            child.writeLock.lock();
            cur.writeLock.unlock();
            Node tmp = null;
            while (child.right != null) {
                child.right.writeLock.lock();
                tmp = child;
                child = child.right;
                tmp.writeLock.unlock();
            } // child lock on
            if (tmp == null) {
                cur.writeLock.lock();
                swapKeys(cur, child);
                cur.left = child.left;
                child.writeLock.unlock();
                cur.writeLock.unlock();
            } else {
                cur.writeLock.lock();
                swapKeys(cur, child);
                cur.writeLock.unlock();
                tmp.writeLock.lock();
                tmp.right = child.left;
                child.writeLock.unlock();
                tmp.writeLock.unlock();
            }
        }

        // this.preOrderTraversal();
        // System.out.println();
        if (root == null) {
            root = new Node(-1);
        }

        return true;
    }

    private void swapKeys(Node node, Node child) {
        int temp = node.data;
        node.data = child.data;
        child.data = temp;
    }

    /*************************************************
     *
     * TRAVERSAL
     *
     **************************************************/

    public void preOrderTraversal() {
        preOrderHelper(root);
    }

    private void preOrderHelper(Node r) {
        if (r != null) {
            System.out.print(r + " ");
            preOrderHelper(r.left);
            preOrderHelper(r.right);
        }
    }

    public void inOrderTraversal() {
        inOrderHelper(root);
    }

    private void inOrderHelper(Node r) {
        if (r != null) {
            inOrderHelper(r.left);
            System.out.print(r + " ");
            inOrderHelper(r.right);
        }
    }

    private class Node {
        private int data;
        private Node left, right;
        private Lock lock = new ReentrantLock();
        private ReadWriteLock rwlock = new ReentrantReadWriteLock();
        private Lock readLock = rwlock.readLock();
        private Lock writeLock = rwlock.writeLock();

        public Node(int data, Node l, Node r) {
            left = l;
            right = r;
            this.data = data;
        }

        public Node(int data) {
            this(data, null, null);
        }

        public String toString() {
            return "" + data;
        }
    }
}
