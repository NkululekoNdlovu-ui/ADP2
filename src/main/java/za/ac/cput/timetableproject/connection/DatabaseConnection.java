package za.ac.cput.timetableproject.connection;

import java.sql.*;
import javax.swing.*;

public class DatabaseConnection {
    private static Connection con;
    private static boolean connectionEstablished = false;

    public static Connection createConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            String url = "jdbc:derby://localhost:1527/Lecture5";
            String username = JOptionPane.showInputDialog("Enter the username for the database:");
            String password = JOptionPane.showInputDialog("Enter the password:");

            con = DriverManager.getConnection(url, username, password);
            
            if (!connectionEstablished) {
                JOptionPane.showMessageDialog(null, "Connection Established");
                connectionEstablished = true;
            }
        }
        return con;
    }

    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error closing connection: " + e.getMessage());
        }
    }
}
