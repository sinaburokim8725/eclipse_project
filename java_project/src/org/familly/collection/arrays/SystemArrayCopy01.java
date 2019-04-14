package org.familly.collection.arrays;

public class SystemArrayCopy01 {
    public static void main(String[] args) {

        char[] copyFrom = {'아', '름', '다', '운', '우', '리', '강', '산', '사', '랑', '해', '요'};

        char[] copyTo = new char[7];

        System.arraycopy(copyFrom, 2, copyTo, 0, copyTo.length);

        System.out.println(new String(copyTo));
    }
}
