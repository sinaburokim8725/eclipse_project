package org.familly.fx.adressbook.first.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * 날짜를 제어하는 헬퍼 메소드들
 */
public class DateUtil {
    private static final Logger logger = Logger.getLogger(DateUtil.class.getSimpleName());

    //변환에 사용되는 날짜 패턴
    private static final String DATE_PATTERN = "yyyy.MM.dd";
    //날짜 변환기
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * 주어진 날짜를 String 타입으로 반환한다.
     * 위에서 정의한
     * {@link DateUtil#DATE_PATTERN}이 사용된다.
     *
     * @param date
     * @return
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        logger.info("DATE_FORMATTER.format(date) > " + DATE_FORMATTER.format(date));

        return DATE_FORMATTER.format(date);
    }

    /**
     * String을 {@link DateUtil#DATE_PATTERN}에 정의한 대로
     * {@link LocalDate} 객체로 변환한다.
     *
     *
     * This method matches the signature of the functional interface
     * TemporalQuery allowing it to be used as a query via method
     * reference, LocalDate::from.
     *
     * KOREAN
     * 이 메서드는 함수 인터페이스 TemporalQuery의 서명과 일치하여
     * 메서드 참조 인 LocalDate :: from을 통해 쿼리로 사용할 수
     * 있도록합니다.
     *
     * @param dateString
     * @return
     */

    public static LocalDate parse(String dateString) {
        logger.info("dateString > " + dateString);

        try {

            return DATE_FORMATTER.parse(dateString, LocalDate::from);

        } catch (DateTimeParseException e) {

            return null;
        }

    }

    public static boolean validDate(String dateString) {
        logger.info("날짜유효성 검사 > " + (DateUtil.parse(dateString) != null));

        //Try to parse the String
        return DateUtil.parse(dateString) != null;
    }

}
