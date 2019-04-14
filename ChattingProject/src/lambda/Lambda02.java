package lambda;

import java.util.Arrays;
import java.util.List;

interface CheckData {
    boolean check(Integer i);
}

public class Lambda02 {
    public static void printList(List<Integer> list, CheckData checkData) {
        for (Integer i : list) {
            if (checkData.check(i)) {
                System.out.println(i);
            }
        }
    }

    public static void main(String... args) {
        List<Integer> list = Arrays.asList(100, 200, 300, 500, 600);
        CheckData c1 = new CheckData() {
            @Override
            public boolean check(Integer i) {
                return true;
            }
        };

        printList(list, c1);

        System.out.println();

        printList(list, new CheckData() {
            @Override
            public boolean check(Integer i) {
                return i > 300;
            }

        });

    }
}
