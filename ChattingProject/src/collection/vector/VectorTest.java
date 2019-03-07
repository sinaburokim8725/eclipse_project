package collection.vector;

import common.info.UserInfo;

import java.util.Vector;

public class VectorTest {
    private Vector vc;
    private UserInfo info;

    public VectorTest() {
        init();
     }

    private void init(){
        vc = new Vector(3);

        System.out.println("사이즈 > " + vc.size());
        System.out.println("용량 >" + vc.capacity());

        for (int i = 0; i < 4; i++) {
            info = new UserInfo();
            info.setName("USER" + i);
            info.setNickName("딸랑이" + i);
            info.setAge(i + 30);
            vc.addElement(info);
        }

        //추가 후의 사이즈랑 용량크기
        System.out.println("사이즈 > " + vc.size());
        System.out.println("용량 >" + vc.capacity());
        System.out.println("첫벗째요소 > " + ((UserInfo)vc.elementAt(0)).getName());

        //요소 추가
        vc.add("사랑이");
        vc.add("아이유");

        System.out.println("마지막요소 > " + vc.elementAt(vc.size()-1));
        //요소 삭제
        String lastElement = (String)vc.remove(vc.size() - 1);
        System.out.println("삭제된 마지막요소 > " + lastElement);
        System.out.println("마지막요소 > " + vc.elementAt(vc.size()-1));

    }

    public static void main(String[] args){
        new VectorTest();
    }
}
