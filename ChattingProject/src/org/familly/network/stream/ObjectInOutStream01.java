package org.familly.network.stream;

import common.info.User;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Arrays;


public class ObjectInOutStream01 {

    private URL url;
    private User user;

    public static void main(String[] args) {
        ObjectInOutStream01 main = new ObjectInOutStream01();


        try {

            main.init();
            main.doSerializable();
            User user = (User) main.undoSerializable();

            System.out.println(user.getName());
            System.out.println(user.getAge());
            System.out.println(user.getPhoneNumber());
            //파일생성하기
            //main.createFile();
            //디렉토리 생성하기
            main.createDirectory();
            main.createDirectory02();

            //시스템에서 사용할수있는 폰트명 출력
            main.getAvailabeFontFamilyNames();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        //filePath = "c:\\user.ser";
        //소스패키지 내에 file 폴더를 만든다.
        String path = "/file/user.ser";
        url = getClass().getResource(path);

        user = new User();
        user.setName("KIM YOUNG HO");
        user.setAge(50);
        user.setPhoneNumber("010-7630-9837");

    }

    //마샬링(marshalling)수행,직렬화
    public void doSerializable() throws IOException {

        FileOutputStream fos = new FileOutputStream(String.valueOf(url));
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(user);
        oos.close();

    }

    /**
     * 언마샬링(unmarshalling)수행, 역직렬화
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object undoSerializable() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(String.valueOf(url));
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object user = (User) ois.readObject();
        ois.close();

        return user;
    }

    //////////////////////////////////////////////////////////////////////////////////
    //추가메소드
    //////////////////////////////////////////////////////////////////////////////////
    private void getAvailabeFontFamilyNames() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        //현재 사용할수있는 폰트 출력하기
        System.out.println("현재 사용할수있는 폰트 출력하기 :\n ");

        Arrays.stream(ge.getAvailableFontFamilyNames()).forEach(System.out::println);

    }

    private void createDirectory02() throws IOException {
        //해당 경로에 있는 파일에 대한 정보를 담는다.
        //객체의 인자인 문자열은 디렉토리로 파일을 생성하거나,
        //원하는 위치의 디렉토리를 설정하면 된다.
        //C:\Users\kyh\Documents\12sky2classic
        File fileDir = new File("C://Users//kyh//Documents//12sky2classic");
        //위의 folder는 파일이 아닌 디렉토리이기 때문에
        //.list() 함수를 사용하면 해당 디렉토리의 파일, 디렉토리 정보를 모두 볼 수 있다.
        String fileList[] = fileDir.list();
        Arrays.stream(fileList).forEach(System.out::println);

    }

    /**
     *
     * @throws IOException
     */
    private void createDirectory() throws IOException {
        String path = "D://create//file//";

        File file = new File(path);
        //파일존재여부 체크
        if (!file.exists()) {
            //파일디렉토리생성
            file.mkdirs();
            //이것은 흠 디렉토리 생성할때는 위에것을.
            //file.mkdir();

            JOptionPane.showMessageDialog(null,
                    "디렉토리가 생성되었습니다. ♥",
                    "알림", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "디렉토리가 이미 존재합니다.",
                    "알림", JOptionPane.INFORMATION_MESSAGE);

        }
    }



    private void createFile() throws IOException {
        int data = 0;
        String fName = "test01.txt";
        URL fileURL = getClass().getResource(fName);
        //System.out.println("fileURL.getPath() > " +fileURL.getPath());

        File file = new File(fName);//파일명을 줄때 파일경로는 아직도 모르겠다
        FileOutputStream fos = new FileOutputStream(file);
        while ((data = System.in.read()) != 'q') {
            //키보드로 입력한 내용을 지정한 파일에 내용으로 기록한다.
            fos.write(data);
        }
        fos.close();//파일을 닫는다.
        //FileInputStream fis = new FileInputStream(file);
    }
}
