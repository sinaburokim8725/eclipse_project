package lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lambda01 {

    public static void printList(List<Integer> list) {
        for (Integer i : list) {
            System.out.println(i);
        }
    }

    public static void printRangeList(List<Integer> list) {
        for (Integer i : list) {
            if (i > 300) {
                System.out.println(i);
            }

        }
    }

    public static void main(String... agms) {
        /*
        List<Integer> list = new ArrayList<>();
        list.add(100);
        list.add(200);
        */
        List<Integer> list = Arrays.asList(100, 200, 500, 600, 900);
        printList(list);
        System.out.println();
        printRangeList(list);

    }
}
