package dao;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//serializable pour divise objet a des entites
public class Person implements java.io.Serializable {

    String name;
    boolean gender;
    Date birthdate;

    Person() {

    }

    Person(String name, boolean gender, Date birthdate) {
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isMale() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String dateToString(Object date) { //to convert Date to String, use format method of
        return new SimpleDateFormat("dd-mm-yyyy").format(date);
    }

    @Override
    public String toString() {
        return name + " " + gender + " born on " + dateToString(birthdate);
    }

}