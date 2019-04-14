package lambda;

public class CompareExam01 {

    public static void exec(Compare compare) {
        int x = 10;
        int y = 20;
        int value = compare.comareTo(x, y);
        System.out.println(value);

    }

    public static void main(String... args) {
        exec((i, j) -> {
            return j - i;
        });

    }


}
