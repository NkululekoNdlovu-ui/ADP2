package za.ac.cput.timetableproject.dao;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import za.ac.cput.timetableproject.connection.DatabaseConnection;
import za.ac.cput.timetableproject.domain.Slot;

public class SlotDao {
    private Connection con;
    private PreparedStatement ps;

    public SlotDao() {
        try {
            if (this.con == null || this.con.isClosed()) {
                this.con = DatabaseConnection.createConnection();
                createTable();
                JOptionPane.showMessageDialog(null, "Connection Established");
            }
        } catch (SQLException k) {
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + k.getMessage());
        }
    }

    public void createTable() {
        String createTableSQL = "CREATE TABLE Slot (" +
                "slotId INT GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY, " +
                "startTime VARCHAR(10), " +
                "endTime VARCHAR(10), " +
                "dayOfWeek VARCHAR(10))";

        try {
            ps = con.prepareStatement(createTableSQL);
            ps.execute();
            JOptionPane.showMessageDialog(null, "Table 'Slot' created successfully.");
        } catch (SQLException k) {
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + k.getMessage());
        } finally {
            closeResources(ps);
        }
    }

    // Method to insert a Slot record
    public int insert(Slot slot) throws SQLException {
        String sql = "INSERT INTO Slot (startTime, endTime, dayOfWeek) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.createConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, slot.getStartTime());
            pstmt.setString(2, slot.getEndTime());
            pstmt.setString(3, slot.getDayOfWeek());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the generated slotId
                }
            }
        }
        return -1; // Return -1 if insertion fails
    }

    // Method to retrieve all Slot records
    public ArrayList<Slot> getAll() throws SQLException {
        String selectSQL = "SELECT * FROM Slot";
        PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
        ResultSet rs = preparedStatement.executeQuery();

        ArrayList<Slot> list = new ArrayList<>();
        while (rs.next()) {
            Slot slot = new Slot();
            slot.setSlotId(rs.getInt("slotId"));
            slot.setStartTime(rs.getString("startTime"));
            slot.setEndTime(rs.getString("endTime"));
            slot.setDayOfWeek(rs.getString("dayOfWeek"));
            list.add(slot);
        }
        return list;
    }

    // Utility method to close resources
    private void closeResources(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error closing resource: " + ex.getMessage());
            }
        }
    }
}
