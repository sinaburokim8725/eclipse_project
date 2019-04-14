package lambda;

import java.util.Arrays;
import java.util.List;

public class Lambda04 {
    public static void display(Integer i) {
        System.out.println("출력값 > " + i);
    }

    public static void main(String... args) {
        List<Integer> list = Arrays.asList(100, 200, 300, 500, 700);

        list.stream().forEach(Lambda04::display);

        System.out.println();

        list.stream().filter(i -> {
            return i > 300;
        }).forEach(Lambda04::display);
    }
}
