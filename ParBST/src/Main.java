import BST.*;
import util.Pool;
import util.Test;

public class Main {
    public static void main(String[] args) {

        Integer[] a = { 13, 5, 2, 7, 4, 10, 15, 11, 13, 20, 9 };

        BST fgbst = new FGBST();
        BST rwbst = new RWBST();
        Test test = new Test();

//        test.insertCheck(fgbst, a);
//        System.out.println();
//        test.insertCheck(rwbst, a);



//        test.insertTimeTest(fgbst);
//        test.insertTimeTest(rwbst);

        test.insertSearchTimeTest(fgbst,1,4);
        test.insertSearchTimeTest(rwbst,1,4);
    }
}
