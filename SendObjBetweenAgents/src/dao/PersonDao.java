package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class PersonDao {

    public static List<Person> getPersonsFromQuery(String query) { // Get Person from query passed in params
        List<Person> persons = new LinkedList<>();
        if(ConnectDB.con == null)
            return persons;
        try {
            Statement st = ConnectDB.con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Person person = new Person();
                person.setName(rs.getString("name"));
                person.setGender(rs.getBoolean("gender"));
                person.setBirthdate(rs.getDate("date_of_birth"));
                persons.add(person);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return persons;
    }
}
