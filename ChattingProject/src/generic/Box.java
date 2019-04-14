package generic;

public class Box<T> {
    private T obj;

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public static void main(String... argvs){
        //Box<String> b = new Box<>();
        Box<Integer> intBox = new Box<>();
        Box<String> stringBox = new Box<>();

        stringBox.setObj("아름다운 밤이양!◑");
        System.out.print(stringBox.getObj()+"\n");

        intBox.setObj(45);
        System.out.println(intBox.getObj());

    }
}
