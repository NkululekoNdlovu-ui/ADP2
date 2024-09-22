package za.ac.cput.timetableproject.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import za.ac.cput.timetableproject.connection.DatabaseConnection;
import za.ac.cput.timetableproject.domain.Venue;

public class VenueDao {

    PreparedStatement ps;
    Connection con;

    public VenueDao() throws SQLException {
        try {
            this.con = DatabaseConnection.createConnection();
            createVenueTable();
            if (this.con != null && !this.con.isClosed()) {
                JOptionPane.showMessageDialog(null, "Connection Established");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to establish connection");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error establishing connection: " + e.getMessage());
        }
    }

    public void createVenueTable() {
        String sql = "CREATE TABLE Venue ("
                   + "VenueID INT PRIMARY KEY, "
                   + "VenueName VARCHAR(20))";

        try {
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Table created successfully or already exists.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error closing resources: " + e.getMessage());
            }
        }
    }

    public void updateVenue(int venueId, String newVenueName) {
        String sql = "UPDATE Venue SET VenueName = ? WHERE VenueID = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, newVenueName);
            ps.setInt(2, venueId);

            int row = ps.executeUpdate();

            if (row > 0) {
                JOptionPane.showMessageDialog(null, "Table Updated");
            } else {
                JOptionPane.showMessageDialog(null, "Not Updated");
            }

        } catch (SQLException k) {
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + k.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException k) {
                JOptionPane.showMessageDialog(null, "Error closing resources: " + k.getMessage());
            }
        }
    }

    public boolean saveVenue(Venue venue) {
        if (this.con == null) {
            JOptionPane.showMessageDialog(null, "No database connection.");
            return false;
        }

        String checkSql = "SELECT COUNT(*) FROM Venue WHERE VenueID = ?";
        String sql = "INSERT INTO Venue(VenueID, VenueName) VALUES(?, ?)";

        try {
            // Check if VenueID already exists
            ps = con.prepareStatement(checkSql);
            ps.setInt(1, venue.getVenueId());
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();

            if (count > 0) {
                JOptionPane.showMessageDialog(null, "Error: Venue ID already exists.");
                return false;
            } else {
                // Close the first PreparedStatement
                ps.close();

                // Insert the new venue
                ps = con.prepareStatement(sql);
                ps.setInt(1, venue.getVenueId());
                ps.setString(2, venue.getDescription());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Error inserting Venue.");
                }
            }
        } catch (SQLException k) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + k.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException k) {
                JOptionPane.showMessageDialog(null, "Error closing resources: " + k.getMessage());
            }
        }
        return false;
    }

    public ArrayList<Venue> readVenue() {
        ArrayList<Venue> list = new ArrayList<>();
        String sql = "SELECT * FROM Venue";

        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    int id = rs.getInt("VenueID");
                    String name = rs.getString("VenueName");

                    list.add(new Venue(id, name));
                }
            }
        } catch (SQLException k) {
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + k.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(VenueDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }

    public void deleteVenue(int id) {
        String sql = "DELETE FROM Venue WHERE VenueID = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Venue deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "No Venue with the provided id");
            }
        } catch (SQLException k) {
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + k.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException k) {
                JOptionPane.showMessageDialog(null, "Error closing resources: " + k.getMessage());
            }
        }
    }

    public boolean isVenueIdExists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Venue WHERE VenueID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}
