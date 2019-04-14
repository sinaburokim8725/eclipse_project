package org.familly.fx.adressbook.first.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.util.logging.Logger;

/**
 *LocalDate 와 ISO 8601 간 변환을 하는 JAXB어댑터
 * String은 '2012-12-03' 같은 날짜를 나타낸다.
 *
 * @author 김 영호
 */
public class LocalDateAdapter extends XmlAdapter<String,LocalDate> {

    private static final Logger logger = Logger.getLogger(LocalDateAdapter.class.getSimpleName());

    @Override
    public LocalDate unmarshal(String v) throws Exception {
        return LocalDate.parse(v);
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        return v.toString();
    }


}
