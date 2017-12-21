package BST;

public interface BST {
    public void initialize();

    public void insert(int data);

    public int findMin();

    public boolean search(int toSearch);

    public boolean delete(int toDelete);

    public void preOrderTraversal();

    public void inOrderTraversal();

    }
