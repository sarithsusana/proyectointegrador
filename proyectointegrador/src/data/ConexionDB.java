package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "system"; // Cambia a tu usuario
    private static final String PASSWORD = "system"; // Cambia a tu contraseña

    public static Connection conectar() {
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos Oracle.");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Driver de Oracle no encontrado.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
        return connection; 
    }
}
