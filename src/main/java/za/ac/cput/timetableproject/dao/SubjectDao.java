/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author hloni
 */
public class SubjectDao {
    private Connection con;
    private PreparedStatement ps;

    public SubjectDao() {
        try {
            if (this.con == null || this.con.isClosed()) {  // Check if the connection is null or closed
                this.con = DatabaseConnection.createConnection();
                createSubjectTable();
                JOptionPane.showMessageDialog(null, "Connection Established");
            }
        } catch (SQLException k) {
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + k.getMessage());
        }
    }

    // Create Subject table
    public void createSubjectTable() {
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
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException k) {
                JOptionPane.showMessageDialog(null, k.getMessage());
            }
        }
    }

    // Update Subject details
    public void updateSubject(int oldCode, String updatedSubject) {
        String sql = "UPDATE Subject SET description = ? WHERE subjectCode = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1,updatedSubject );
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
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException k) {
                JOptionPane.showMessageDialog(null, k.getMessage());
            }
        }
    }

    // Read all subjects
    public ArrayList<Subject> readSubjects() {
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
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SubjectDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list;
    }

    // Save a new subject
    public boolean save(Subject subject) {
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
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException k) {
                JOptionPane.showMessageDialog(null, k.getMessage());
            }
        }
        return false;
    }

    // Delete a subject by code
    public void delete(int code) {
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
        }
    }

    // Check if a subject code exists
    public boolean isSubjectCodeExists(int code) throws SQLException {
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
    public ArrayList<String> Subjects() {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT * FROM Subject";  // Adjust the SQL if needed

        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    int sId = rs.getInt(1);  // Assuming ID is the first column
                    String code = rs.getString(2);  // Assuming code is the second column
                    list.add(code);
                }
            }
        } catch (SQLException k) {
            k.printStackTrace();  // Print the exception for debugging
        }
        return list;
    }
}
