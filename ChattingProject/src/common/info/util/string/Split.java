package common.info.util.string;

public class Split {


    public static void main(String[] args) {
        String msg = "김영호;";
        System.out.println(">> " + msg.split(";")[0]);
        //System.out.println(">> " + msg.split(";")[1]);
        //System.out.println(">> " + msg.split(";")[2]);
        //boolean a = (msg.split(";")[1].equals(""))?true:false;
        //System.out.println("?  " + a);
        System.out.println("김영호; 배열길이>" + msg.split(";").length);

    }
}
