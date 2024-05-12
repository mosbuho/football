package controller.db;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class connectDB {
    public static Connection getConnection() {
        Properties pt = new Properties();
        Connection con = null;
        try (FileReader fr = new FileReader("src/controller/db/db.properties")) {
            pt.load(fr);
            String url = pt.getProperty("url");
            String user = pt.getProperty("user");
            String pw = pt.getProperty("pw");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(url, user, pw);
        } catch (IOException e) {
            System.out.println("FileReader Error");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Error");
        } catch (SQLException e) {
            System.out.println("DataBase Connection Error");
        }
        return con;
    }
}