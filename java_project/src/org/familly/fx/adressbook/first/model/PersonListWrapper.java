package org.familly.fx.adressbook.first.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.logging.Logger;

/**
 * 연락처 리스트를 감싸는 헬퍼 클래스이다.
 * XML로 저장하는 데 사용된다.
 * @XmlRootElement(name = "persons") : 상위 엘리먼트 이름을 정의합니다.
 * @XmlElement(name = "person") : 엘리먼트에 선택적인 이름을 정의할
 * 수있습니다.
 * @author Kim Young Ho
 */
@XmlRootElement(name = "persons")
public class PersonListWrapper {
    private static final Logger logger = Logger.getLogger(PersonListWrapper.class.getSimpleName());


    private List<Person> personList;

    @XmlElement(name = "person")
    public List<Person> getPersonList() {
        logger.info("");

        return personList;
    }

    public void setPersonList(List<Person> persons) {
        logger.info("persons? > " + (persons != null ? "ok" :null));
        personList = persons;
    }

}
