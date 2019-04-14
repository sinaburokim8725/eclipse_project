package lambda;

import java.util.Arrays;
import java.util.List;

public class Lambda03 {

    public static void printList(List<Integer> list, CheckData checkData) {
        for (Integer i : list) {
            if (checkData.check(i)) {
                System.out.println(i);
            }
        }
    }

    public static void main(String... args) {
        List<Integer> list = Arrays.asList(100, 200, 300, 500, 600);
        //람다식으로 호출한다.즉 우리가 여태껏 익명클래스를 생성해서 참조타입의로
        //인자를 넘겼으나 메쏘드 자체를 인자로 넘길수는 없었다
        //람다식은 익명의 클래스를 넘겨서 참조하는것을 없애고 마치 메쏘드가 인자가 넘어가는 것처럼 구현한 식이다.
        printList(list, (i) -> {
            return true;
        });

        System.out.println();

        printList(list, (i) -> {
            return i >= 300;
        });
    }
}
