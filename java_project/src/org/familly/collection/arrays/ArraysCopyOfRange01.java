package org.familly.collection.arrays;

import java.util.Arrays;

public class ArraysCopyOfRange01 {
    public static void main(String[] args) {

        char[] copyFrom = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O'};


        /**
         * copyOfRange method is the initial index of the
         * range to be copied, inclusively, while the third
         * parameter is the final index of the range to be
         * copied, exclusively. In this example, the range to
         * be copied does not include the array element at
         * index 9 (which contains the character a).
         * KOREAN
         * copyOfRange 메서드는 복사 할 범위의 초기 인덱스이며
         * 세 번째 매개 변수는 복사 할 범위의 최종 인덱스입니다
         * . 이 예제에서 복사 할 범위는 인덱스 9 (문자 a 포함)에
         * 배열 요소를 포함하지 않습니다.
         */
        char[] copyTo = Arrays.copyOfRange(copyFrom, 2, 9);

        System.out.println(copyTo);
    }
}
