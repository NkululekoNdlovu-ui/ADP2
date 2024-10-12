package za.ac.cput.timetableproject.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import za.ac.cput.timetableproject.connection.DatabaseConnection;
import za.ac.cput.timetableproject.domain.Subject;

public class SubjectDao {
    private Connection con;
    private PreparedStatement ps;

    public SubjectDao() {
        try {
            this.con = DatabaseConnection.createConnection();
            createSubjectTable();
            JOptionPane.showMessageDialog(null, "Connection Established");
        } catch (SQLException k) {
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + k.getMessage());
        }
    }

    // Ensures the connection is active
    private void ensureConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            con = DatabaseConnection.createConnection();
        }
    }

    // Create Subject table
    public void createSubjectTable() throws SQLException {
        ensureConnection(); // Ensure connection before using it
        String sql = "CREATE TABLE Subject ("
                   + "subjectCode INT PRIMARY KEY, "
                   + "description VARCHAR(255))";

        try {
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Subject table successfully created");
        } catch (SQLException k) {
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + k.getMessage());
        } finally {
            closeResources(ps);
        }
    }

    // Update Subject details
    public void updateSubject(int oldCode, String updatedSubject) throws SQLException {
        ensureConnection(); // Ensure connection before updating
        String sql = "UPDATE Subject SET description = ? WHERE subjectCode = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, updatedSubject);
            ps.setInt(2, oldCode);
            int rowsUpdated = ps.executeUpdate();
            
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Subject updated successfully");
            } else {
                JOptionPane.showMessageDialog(null, "No subject found with the provided code");
            }
        } catch (SQLException k) {
            JOptionPane.showMessageDialog(null, k.getMessage());
        } finally {
            closeResources(ps);
        }
    }

    // Read all subjects
    public ArrayList<Subject> readSubjects() throws SQLException {
        ensureConnection(); // Ensure connection before reading
        ArrayList<Subject> list = new ArrayList<>();
        String sql = "SELECT * FROM Subject";

        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int code = rs.getInt("subjectCode");
                String description = rs.getString("description");
                list.add(new Subject(code, description));
            }
        } catch (SQLException k) {
            JOptionPane.showMessageDialog(null, k.getMessage());
        } finally {
            closeResources(ps);
        }
        return list;
    }

    // Save a new subject
    public boolean save(Subject subject) throws SQLException {
        ensureConnection(); // Ensure connection before saving
        String sql = "INSERT INTO Subject(subjectCode, description) VALUES(?, ?)";

        String checkSql = "SELECT COUNT(*) FROM Subject WHERE subjectCode = ?";
        try {
            ps = this.con.prepareStatement(checkSql);
            ps.setInt(1, subject.getSubjectCode());
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                JOptionPane.showMessageDialog(null, "Error: Subject code already exists.");
                return false;
            } else {
                ps.close();

                ps = con.prepareStatement(sql);
                ps.setInt(1, subject.getSubjectCode());
                ps.setString(2, subject.getDescription());

                int ok = ps.executeUpdate();

                if (ok > 0) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Error inserting Subject.");
                }
            }
        } catch (SQLException k) {
            JOptionPane.showMessageDialog(null, k.getMessage());
        } finally {
            closeResources(ps);
        }
        return false;
    }

    // Delete a subject by code
    public void delete(int code) throws SQLException {
        ensureConnection(); // Ensure connection before deleting
        String sql = "DELETE FROM Subject WHERE subjectCode = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, code);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Subject deleted successfully");
            } else {
                JOptionPane.showMessageDialog(null, "No subject found with the provided code");
            }
        } catch (SQLException k) {
            JOptionPane.showMessageDialog(null, k.getMessage());
        } finally {
            closeResources(ps);
        }
    }

    // Check if a subject code exists
    public boolean isSubjectCodeExists(int code) throws SQLException {
        ensureConnection(); // Ensure connection before checking existence
        String sql = "SELECT COUNT(*) FROM Subject WHERE subjectCode = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // Retrieve list of subject codes
    public ArrayList<String> Subjects() throws SQLException {
        ensureConnection(); // Ensure connection before retrieving subject codes
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT * FROM Subject";  // Adjust the SQL if needed

        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    String code = rs.getString("description");  // Assuming code is in the second column
                    list.add(code);
                }
            }
        } catch (SQLException k) {
            k.printStackTrace();  // Print the exception for debugging
        } finally {
            closeResources(ps);
        }
        return list;
    }

    // Retrieve specific subject by code
    public Subject selectedSubject(int subjectCode) throws SQLException {
        ensureConnection(); // Ensure connection before retrieving
        Subject subject = null; // Initialize subject to null in case it doesn't exist
        String selectSql = "SELECT * FROM Subject WHERE subjectCode = ?";

        try {
            ps = con.prepareStatement(selectSql);
            ps.setInt(1, subjectCode); // Set the subjectCode parameter
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) { // Check if there is a result
                int code = rs.getInt("subjectCode");
                String description = rs.getString("description");
                subject = new Subject(code, description); // Create a new Subject object
            } else {
                JOptionPane.showMessageDialog(null, "No subject found with the provided subject code.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            closeResources(ps);
        }
        return subject; // Return the found subject or null if not found
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
