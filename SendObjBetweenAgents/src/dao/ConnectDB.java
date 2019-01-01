package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    /* Start DB Information */
    public static final String DB_NAME = "persons_db";
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 3306;
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    /* End DB Information */

    public static Connection con = null;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
        }

        try {
            con = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME, USERNAME, PASSWORD);
            System.out.println("Connected !");
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

}
