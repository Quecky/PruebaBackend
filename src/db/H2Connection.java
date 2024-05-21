package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Connection {
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        //cambia nombre base de datos para no tener errores
                return DriverManager.getConnection("jdbc:h2:~/parcial1","sa","sa");
    }
}
