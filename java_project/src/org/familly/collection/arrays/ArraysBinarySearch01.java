package org.familly.collection.arrays;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArraysBinarySearch01 {
    public static void main(String[] args) {
        //intializing unsorted byte array
        byte[] byteArr = {30, 20, 10, 60, 5};

        //List byteList = Arrays.asList(byteArr);

        //sorting list 검색시 반드드 배열은 정력이 되어있어야 한다.
        //그렇지 않을경우 예상치 못한 결과가 나온다.
        Arrays.sort(byteArr);

        for (byte number : byteArr) {
            System.out.println(number);
        }

        byte byteSearchValue = 30;

        int retVal = Arrays.binarySearch(byteArr, byteSearchValue);

        System.out.println("The Index of 35 > " + retVal);



    }
}
