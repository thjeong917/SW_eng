package util;

import java.util.Random;

import LL.*;

public class Test {

    public void insertCheck(LFList list, Integer[] a) {

        Pool pool = new Pool(8);

        for (Integer n : a) {
            pool.push(() -> list.insert(n));
        }
        pool.join();

        System.out.print("Multithreading : ");
        list.traversal();
        System.out.println("DONE");
    }

    public void deleteCheck(LFList list, Integer[] a) {

        for (Integer n : a) {
            list.insert(n);
        }

        list.traversal();
        System.out.println();

        Pool pool = new Pool(8);

        Integer[] b = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        for (Integer n : b) {
            pool.push(() -> list.delete(n));
        }
        pool.join();

        list.traversal();
        System.out.println();
        System.out.println("DONE");
    }

    public void searchCheck(LFList list) {
        Random random = new Random();

        list.traversal();
        System.out.println();

        Pool pool = new Pool(8);

        Integer[] b = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        for (int n=1; n<10000; n++) {
            int tmp = n;
            if(n%2 == 0) {
                pool.push(() -> list.insert(tmp));
                pool.push(() -> list.search(tmp));
                pool.push(() -> list.delete(tmp));
            }
            else {
                pool.push(() -> list.insert(tmp));
                pool.push(() -> list.delete(tmp));
                pool.push(() -> list.search(tmp));
            }
        }

        pool.join();

        list.traversal();
        System.out.println();
        System.out.println("DONE");
    }

    public void insertTimeTest(LFList list) {
//        LFList list = new LFList();
        long t1 = getInsertTime(1, 100000, list);
        System.out.println("1 thread " + t1);
        long t2 = getInsertTime(2, 100000, list);
        System.out.println("2 thread " + t2);
        long t3 = getInsertTime(4, 100000, list);
        System.out.println("4 thread " + t3);
        long t4 = getInsertTime(8, 100000, list);
        System.out.println("8 thread " + t4);
        System.out.println();
    }

    public void insertSearchTimeTest(LFList li, int r1, int r2) {
        LFList list = new LFList();
        Random random = new Random();
        Pool pool = new Pool(4);

        for (int i = 0; i < 100000; i++) {
            pool.push(() -> list.insert(random.nextInt(100000)));
        }
        pool.join();

//        for (int i = 0; i < 100000; i++) {
//            list.insert(random.nextInt(100000));
//            list.traversal();
//        }
        System.out.println("INSERT DONE");
        System.out.println("TEST STARTS");

        long t1 = getSearchTime(1, 100000, list, r1, r2);
        System.out.println("1 thread " + t1);
        long t2 = getSearchTime(2, 100000, list, r1, r2);
        System.out.println("2 thread " + t2);
        long t3 = getSearchTime(4, 100000, list, r1, r2);
        System.out.println("4 thread " + t3);
        long t4 = getSearchTime(8, 100000, list, r1, r2);
        System.out.println("8 thread " + t4);
        System.out.println();
    }

    public void deleteTimeTest(LFList list) {
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            list.insert(random.nextInt(100000));
        }

        long t1 = getDeleteTime(1, 100000, list);
        System.out.println("1 thread " + t1);
        long t2 = getDeleteTime(2, 100000, list);
        System.out.println("2 thread " + t2);
        long t3 = getDeleteTime(4, 100000, list);
        System.out.println("4 thread " + t3);
        long t4 = getDeleteTime(8, 100000, list);
        System.out.println("8 thread " + t4);
        System.out.println();
    }


    public long getInsertTime(int nThread, int num, LFList li) {
        LFList list = new LFList();
        Random random = new Random();
        Pool pool = new Pool(nThread);

        long starttime = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            pool.push(() -> list.insert(random.nextInt(num)));
        }
        pool.join();

        long finishtime = System.currentTimeMillis();
        long total = finishtime - starttime;
        return total;
    }

    public long getSearchTime(int nThread, int num, LFList list, int r1, int r2) {
        Random random = new Random();
        Pool pool = new Pool(nThread);

        long starttime = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            if (i % (r1 + r2) == 0) // set ratio
                pool.push(() -> list.insert(random.nextInt(200000)));
            else
                pool.push(() -> list.search(random.nextInt(200000)));
        }
        pool.join();

        long finishtime = System.currentTimeMillis();
        long total = finishtime - starttime;
        return total;
    }

    public long getDeleteTime(int nThread, int num, LFList list) {
        Random random = new Random();
        Pool pool = new Pool(nThread);

        long starttime = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            pool.push(() -> list.delete(random.nextInt(num)));
        }
        pool.join();

        long finishtime = System.currentTimeMillis();
        long total = finishtime - starttime;
        return total;
    }

}
