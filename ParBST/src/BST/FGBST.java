package BST;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implemented binary search tree w/ Fine-Grained locking
 */

public class FGBST implements BST {
    private Node root;
    private ReentrantLock glock;

    public FGBST() {
        root = new Node(-1);
        // root = null;]
        glock = new ReentrantLock();
    }

    public void initialize() {
        root = new Node(-1);
    }

    public void insert(int data){
        root.lock.lock();
        root = insert(root, data);
        root.lock.unlock();
    }

    public Node insert(Node root, int toInsert) {
        Node cur;
        Node prev;
        Node newnode = new Node(toInsert);

         root.lock.lock();
         if (root.data == -1) { // at first when tree is empty || at last
             try {
                 root.data = toInsert;
                 return root;
             } finally {
                 root.lock.unlock();
             }
         }
         cur = root;

//        root.lock.lock();
//        cur = root;
//        if (cur.data == -1) {
//            cur.data = toInsert;
//            cur.lock.unlock();
//            return true;
//        }

        while (true) {
            if (newnode.data < cur.data) {
                if (cur.left != null) {
                    cur.left.lock.lock();
                    prev = cur;
                    cur = cur.left;
                    prev.lock.unlock();
                } else {
                    cur.left = newnode;
                    cur.lock.unlock();
                    break;
                }
            } else if (newnode.data > cur.data) {
                if (cur.right != null) {
                    cur.right.lock.lock();
                    prev = cur;
                    cur = cur.right;
                    prev.lock.unlock();
                } else {
                    cur.right = newnode;
                    cur.lock.unlock();
                    break;
                }
            } else {
//                cur.data = newnode.data;
                cur.lock.unlock();
                break;
            }
        }
        // System.out.println(toInsert + " INSERT DONE");
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
        // if (p == null)
        // return false;
        // else if (toSearch == p.data)
        // return true;
        // else if (toSearch < p.data)
        // return search(p.left, toSearch);
        // else
        // return search(p.right, toSearch);
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

        root.lock.lock();
        cur = root;
        // glock.unlock();

        if (cur.data == -1) {
            cur.lock.unlock();
//			System.out.println("NO");
            return false;
        }

        while (true) {
            if (cur == null) {
//				System.out.println("NO");
                return false;
            } else if (toSearch == cur.data) {
                cur.lock.unlock();
//				System.out.println("HIT");
                return true;
            } else if (toSearch < cur.data) {
                if (cur.left != null) {
                    cur.left.lock.lock();
                    prev = cur;
                    cur = cur.left;
                    prev.lock.unlock();
                } else {
                    cur.lock.unlock();
//					System.out.println("NO");
                    return false;
                }
            } else if (toSearch > cur.data) {
                if (cur.right != null) {
                    cur.right.lock.lock();
                    prev = cur;
                    cur = cur.right;
                    prev.lock.unlock();
                } else {
                    cur.lock.unlock();
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

        root.lock.lock();
        if(root.data == -1){
            root.lock.unlock();
            return false;
        }
        cur = root;

        while (cur != null && cur.data != toDelete) {
            parent = cur;
            if (toDelete < cur.data) {
                if (cur.left != null) {
                    cur.left.lock.lock();
                    cur = cur.left; // Scan left
                    parent.lock.unlock();
                } else if (cur.left == null) {
                    cur.lock.unlock();
                    cur = null;
                    return false;
                }
            } else {
                if (cur.right != null) {
                    cur.right.lock.lock();
                    cur = cur.right; // Scan right
                    parent.lock.unlock();
                } else if (cur.right == null) {
                    cur.lock.unlock();
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
                cur.lock.unlock();
                root = null;
            } else {
                parent.lock.lock();
                if (toDelete < parent.data) {
                    parent.left = null;
                    cur.lock.unlock();
                    parent.lock.unlock();
                } else {
                    parent.right = null;
                    cur.lock.unlock();
                    parent.lock.unlock();
                }
            }
        } else if (cur.left == null) { // If left child is null, get right child
            child = cur.right;
            child.lock.lock();
            swapKeys(cur, child);
            cur.left = child.left;
            cur.right = child.right;
            cur.lock.unlock();
            child.lock.unlock();
        } else if (cur.right == null) { // If right child is null, get left
            // child
            child = cur.left;
            child.lock.lock();
            swapKeys(cur, child);
            cur.left = child.left;
            cur.right = child.right;
            cur.lock.unlock();
            child.lock.unlock();
        } else { // If two children
            child = cur.left;
            child.lock.lock();
            cur.lock.unlock();
            Node tmp = null;
            while (child.right != null) {
                child.right.lock.lock();
                tmp = child;
                child = child.right;
                tmp.lock.unlock();
            } // child lock on
            if (tmp == null) {
                cur.lock.lock();
                swapKeys(cur, child);
                cur.left = child.left;
                child.lock.unlock();
                cur.lock.unlock();
            } else {
                cur.lock.lock();
                swapKeys(cur, child);
                cur.lock.unlock();
                tmp.lock.lock();
                tmp.right = child.left;
                child.lock.unlock();
                tmp.lock.unlock();
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
        Lock lock = new ReentrantLock();

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
