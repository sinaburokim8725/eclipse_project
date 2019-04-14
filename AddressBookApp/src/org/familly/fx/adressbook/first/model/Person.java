package org.familly.fx.adressbook.first.model;

import javafx.beans.property.*;
import org.familly.fx.adressbook.first.util.LocalDateAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.logging.Logger;

/**
 * 주소록 모델 클래스
 */
public class Person {
    private static final Logger logger = Logger.getLogger(Person.class.getSimpleName());

    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty street;
    private final IntegerProperty postalCode;
    private final StringProperty city;
    private final ObjectProperty<LocalDate> birthday;


    public Person() {
        this(null, null);
        logger.info("");

    }

    public Person(String firstName, String lastName) {
        logger.info("Person(String firstName, String lastName)");

        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        //더미
        this.street = new SimpleStringProperty("some street");
        this.postalCode = new SimpleIntegerProperty(1234);

        this.city = new SimpleStringProperty("some city");
        this.birthday = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, 2, 21));

    }

    //////////////////////////////////////////////////////
    //  getter setter
    /////////////////////////////////////////////////////

    public String getFirstName() {
        logger.info("");
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        logger.info("");
        return firstName;
    }

    public void setFirstName(String firstName) {
        logger.info("");
        this.firstName.set(firstName);
    }

    public String getLastName() {
        logger.info("");
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        logger.info("");
        return lastName;
    }

    public void setLastName(String lastName) {
        logger.info("");
        this.lastName.set(lastName);
    }

    public String getStreet() {
        logger.info("");
        return street.get();
    }

    public StringProperty streetProperty() {
        logger.info("");
        return street;
    }

    public void setStreet(String street) {
        logger.info("");
        this.street.set(street);
    }

    public int getPostalCode() {
        logger.info("");
        return postalCode.get();
    }

    public IntegerProperty postalCodeProperty() {
        logger.info("");
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        logger.info("");
        this.postalCode.set(postalCode);
    }

    public String getCity() {
        logger.info("");
        return city.get();
    }

    public StringProperty cityProperty() {
        logger.info("");
        return city;
    }

    public void setCity(String city) {
        logger.info("");
        this.city.set(city);
    }

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate getBirthday() {
        logger.info("");
        return birthday.get();
    }

    public ObjectProperty<LocalDate> birthdayProperty() {
        logger.info("");
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        logger.info("");
        this.birthday.set(birthday);
    }
}
