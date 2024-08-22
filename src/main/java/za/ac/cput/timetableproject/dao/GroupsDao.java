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

import javax.swing.JOptionPane;
import za.ac.cput.timetableproject.connection.DatabaseConnection;
import za.ac.cput.timetableproject.domain.Group;


public class GroupsDao {

    private PreparedStatement ps;
    private Connection con;

    public GroupsDao() {
        try {
            if (this.con == null || this.con.isClosed()) {
                this.con = DatabaseConnection.createConnection();
                createGroup();
                JOptionPane.showMessageDialog(null, "Connection Established");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + e.getMessage());
        }
    }
    public void createGroup(){
        String sql = "CREATE TABLE Group(GroupID INT PRIMARY KEY ,GroupName VARCHAR(50))";
        
        try{
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
             JOptionPane.showMessageDialog(null, "You have sucessfully created a table");
            
        }catch(SQLException k){
             JOptionPane.showMessageDialog(null, "SQL error occured " + k);
        }
        
    }

    public boolean save(Group group) {
        if (this.con == null) {
            JOptionPane.showMessageDialog(null, "No database connection.");
            return false;
        }

        String checkSql = "SELECT COUNT(*) FROM `Group` WHERE GroupID = ?";
        String sql = "INSERT INTO `Group` (GroupID, GroupName) VALUES (?, ?)";

        try {
            ps = con.prepareStatement(checkSql);
            ps.setInt(1, group.getGroupId());
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();

            if (count > 0) {
                JOptionPane.showMessageDialog(null, "Error: GroupID already exists.");
                return false;
            } else {
                ps.close();

                ps = con.prepareStatement(sql);
                ps.setInt(1, group.getGroupId());
                ps.setString(2, group.getGroupName());

                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            closePreparedStatement();
        }
        return false;
    }

    public void updateGroup(int groupId, String newGroupName) {
        String sql = "UPDATE `Group` SET GroupName = ? WHERE GroupID = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, newGroupName);
            ps.setInt(2, groupId);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Group updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "No group found with the provided GroupID.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            closePreparedStatement();
        }
    }

    public void deleteGroup(int groupId) {
        String sql = "DELETE FROM `Group` WHERE GroupID = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, groupId);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Group deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "No group found with the provided GroupID.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            closePreparedStatement();
        }
    }

    public boolean isGroupIdExists(int groupId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM `Group` WHERE GroupID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public ArrayList<Group> readGroups() {
        ArrayList<Group> list = new ArrayList<>();
        String sql = "SELECT * FROM `Group`";

        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    int id = rs.getInt("GroupID");
                    String groupName = rs.getString("GroupName");
                    list.add(new Group(id, groupName));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            closePreparedStatement();
        }
        return list;
    }

    private void closePreparedStatement() {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error closing resources: " + e.getMessage());
        }
    }
}
