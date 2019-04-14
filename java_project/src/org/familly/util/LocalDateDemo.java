package org.familly.util;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class LocalDateDemo {
    public static void main(String[] args) {

        LocalDate date = LocalDate.from(ZonedDateTime.now());
        System.out.println(date);
    }
}
