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
            this.con = DatabaseConnection.createConnection();
            createTable();
            JOptionPane.showMessageDialog(null, "Connection Established");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + e.getMessage());
        }
    }

    // Ensures the connection is active
    private void ensureConnection() throws SQLException {
        // Check if the connection is closed or null and create a new one if necessary
        if (con == null || con.isClosed()) {
            con = DatabaseConnection.createConnection();
        }
    }

    public void createTable() throws SQLException {
        ensureConnection();  // Ensure connection before creating the table
        String createTableSQL = "CREATE TABLE Slot (" +
                "slotId INT GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY, " +
                "startTime VARCHAR(10), " +
                "endTime VARCHAR(10), " +
                "dayOfWeek VARCHAR(10))";

        try {
            ps = con.prepareStatement(createTableSQL);
            ps.execute();
            JOptionPane.showMessageDialog(null, "Table 'Slot' created successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + e.getMessage());
        } finally {
            closeResources(ps);
        }
    }

    // Method to insert a Slot record
    public int insertSlot(Slot slot) throws SQLException {
        ensureConnection();  // Ensure connection before inserting
        String sql = "INSERT INTO Slot (startTime, endTime, dayOfWeek) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
    public ArrayList<Slot> getAllSlots() throws SQLException {
        ensureConnection();  // Ensure connection before retrieving slots
        String selectSQL = "SELECT * FROM Slot";
        ArrayList<Slot> list = new ArrayList<>();

        try (PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Slot slot = new Slot();
                slot.setSlotId(rs.getInt("slotId"));
                slot.setStartTime(rs.getString("startTime"));
                slot.setEndTime(rs.getString("endTime"));
                slot.setDayOfWeek(rs.getString("dayOfWeek"));
                list.add(slot);
            }
        }
        return list;
    }

    // Method to retrieve a specific Slot by slotId
    public Slot getSlotById(int slotId) throws SQLException {
        ensureConnection();  // Ensure connection before retrieving slot by ID
        Slot slot = null; // Initialize slot to null in case it doesn't exist
        String selectSQL = "SELECT * FROM Slot WHERE slotId = ?";

        try (PreparedStatement pstmt = con.prepareStatement(selectSQL)) {
            pstmt.setInt(1, slotId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    slot = new Slot();
                    slot.setSlotId(rs.getInt("slotId"));
                    slot.setStartTime(rs.getString("startTime"));
                    slot.setEndTime(rs.getString("endTime"));
                    slot.setDayOfWeek(rs.getString("dayOfWeek"));
                } else {
                    JOptionPane.showMessageDialog(null, "No Slot found with the provided ID.");
                }
            }
        }
        return slot; // Return the found slot or null if not found
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
