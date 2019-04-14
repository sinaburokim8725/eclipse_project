package org.familly.collection.arrays;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ArraysAsList01 {

    public static void main(String[] args) {
        List<Integer> intList = Arrays.asList(20, 40, 10, 50, 30);

        System.out.println("미정렬 > " + intList);

        Collections.sort(intList);

        System.out.println("정렬 > "+ intList);
    }
}
