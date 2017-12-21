package LL;

import util.Test;

public class Main {
    public static void main(String[] args) {
        LFList list = new LFList<>();
        Test test = new Test();

        Integer[] a = { 6,23,3,41,5,1,2,7,4 };

//        test.insertCheck(list, a);
//        test.deleteCheck(list, a);
//        test.searchCheck(list);

//        test.insertTimeTest(list);
        test.insertSearchTimeTest(list, 1, 1);

//        System.out.println("1 : 4");
//        test.insertSearchTimeTest(list, 1, 4);
//        test.insertSearchTimeTest(list, 1, 4);
//        test.insertSearchTimeTest(list, 1, 4);
//        test.insertSearchTimeTest(list, 1, 4);
//
//        System.out.println("1 : 9");y
//        test.insertSearchTimeTest(list, 1, 9);
//        test.insertSearchTimeTest(list, 1, 9);
//        test.insertSearchTimeTest(list, 1, 9);
//        test.insertSearchTimeTest(list, 1, 9);
    }
}
