package util;

import java.util.Random;
import BST.*;

public class Test {

    public void insertCheck(BST bst, Integer[] a){

        Pool pool = new Pool(8);

        for (Integer n : a) {
            pool.push(() -> bst.insert(n));
        }
        pool.join();

        System.out.print("Multithreading : ");
        bst.preOrderTraversal();
        System.out.println();
        System.out.println("DONE");
    }

    public void deleteCheck(BST bst, Integer[] a){

         for (Integer n : a) {
             bst.insert(n);
         }

        bst.preOrderTraversal();
        System.out.println();

        Pool pool = new Pool(8);

        Integer[] b = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        for (Integer n : b) {
            pool.push(() -> bst.delete(n));
        }
        pool.join();

        bst.preOrderTraversal();
        System.out.println();
        System.out.println("DONE");
    }

    public void searchCheck(BST bst) {
        Random random = new Random();

        bst.preOrderTraversal();
        System.out.println();

        Pool pool = new Pool(8);

        Integer[] b = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        for (int n=1; n<10000; n++) {
            int tmp = n;
            if(n%2 == 0) {
                pool.push(() -> bst.insert(tmp));
                pool.push(() -> bst.search(tmp));
                pool.push(() -> bst.delete(tmp));
            }
            else {
                pool.push(() -> bst.insert(tmp));
                pool.push(() -> bst.delete(tmp));
                pool.push(() -> bst.search(tmp));
            }
        }

        pool.join();

        bst.preOrderTraversal();
        System.out.println();
        System.out.println("DONE");
    }

    public void insertTimeTest(BST bst) {
        long t1 = getInsertTime(1, 1000000, bst);
        System.out.println("1 thread " + t1);
        long t2 = getInsertTime(2, 1000000, bst);
        System.out.println("2 thread " + t2);
        long t3 = getInsertTime(4, 1000000, bst);
        System.out.println("4 thread " + t3);
        long t4 = getInsertTime(8, 1000000, bst);
        System.out.println("8 thread " + t4);
        System.out.println();
    }

    public void insertSearchTimeTest(BST bst, int r1, int r2) {
        Random random = new Random();
        for (int i = 0; i < 1000000; i++) {
            bst.insert(random.nextInt(1000000));
        }

        long t1 = getSearchTime(1, 1000000, bst, r1, r2);
        System.out.println("1 thread " + t1);
        long t2 = getSearchTime(2, 1000000, bst, r1, r2);
        System.out.println("2 thread " + t2);
        long t3 = getSearchTime(4, 1000000, bst, r1, r2);
        System.out.println("4 thread " + t3);
        long t4 = getSearchTime(8, 1000000, bst, r1, r2);
        System.out.println("8 thread " + t4);
        System.out.println();
    }

    public void deleteTimeTest(BST bst) {
        Random random = new Random();
        for (int i = 0; i < 1000000; i++) {
            bst.insert(random.nextInt(1000000));
        }

        long t1 = getDeleteTime(1, 1000000, bst);
        System.out.println("1 thread " + t1);
        long t2 = getDeleteTime(2, 1000000, bst);
        System.out.println("2 thread " + t2);
        long t3 = getDeleteTime(4, 1000000, bst);
        System.out.println("4 thread " + t3);
        long t4 = getDeleteTime(8, 1000000, bst);
        System.out.println("8 thread " + t4);
        System.out.println();
    }


    public long getInsertTime(int nThread, int num, BST exbst) {
        Random random = new Random();
        Pool pool = new Pool(nThread);

        long starttime = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            pool.push(() -> exbst.insert(random.nextInt(num)));
        }
        pool.join();

        long finishtime = System.currentTimeMillis();
        long total = finishtime - starttime;
        return total;
    }

    public long getSearchTime(int nThread, int num, BST exbst, int r1, int r2) {
        Random random = new Random();
        Pool pool = new Pool(nThread);


        long starttime = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            if (i % (r1+r2) == 0) // set ratio
                pool.push(() -> exbst.insert(random.nextInt(2000000)));
            else
                pool.push(() -> exbst.search(random.nextInt(2000000)));
        }
        pool.join();

        long finishtime = System.currentTimeMillis();
        long total = finishtime - starttime;
        return total;
    }

    public long getDeleteTime(int nThread, int num, BST exbst) {
        Random random = new Random();
        Pool pool = new Pool(nThread);

        long starttime = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            pool.push(() -> exbst.delete(random.nextInt(num)));
        }
        pool.join();

        long finishtime = System.currentTimeMillis();
        long total = finishtime - starttime;
        return total;
    }

}
