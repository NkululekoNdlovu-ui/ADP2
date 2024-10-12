package za.ac.cput.timetableproject.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import za.ac.cput.timetableproject.connection.DatabaseConnection;
import za.ac.cput.timetableproject.domain.TimeTable;

public class TimeTableDao {
    private PreparedStatement ps;
    private Connection con;

    public TimeTableDao() {
        try {
            this.con = DatabaseConnection.createConnection();
            createTable();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + e.getMessage());
        }
    }

    // Ensure the connection is active
    private void ensureConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            con = DatabaseConnection.createConnection();
        }
    }

    // Create TimeTable table
    public void createTable() throws SQLException {
        ensureConnection();  // Ensure connection before creating table
        String createTableSQL = "CREATE TABLE TimeTable ("
                + "timeTableId INT GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY, "
                + "lectureId INT, "
                + "venueId INT, "
                + "slotId INT, "
                + "groupId INT, "
                + "subjectCode INT, "
                + "type VARCHAR(20), "
                + "FOREIGN KEY (lectureId) REFERENCES Lecture(lectureId), "
                + "FOREIGN KEY (venueId) REFERENCES Venue(venueId), "
                + "FOREIGN KEY (slotId) REFERENCES Slot(slotId), "
                + "FOREIGN KEY (groupId) REFERENCES \"Group\"(GroupID), "
                + "FOREIGN KEY (subjectCode) REFERENCES Subject(subjectCode) )";

        try (PreparedStatement createPs = con.prepareStatement(createTableSQL)) {
            createPs.execute();
            JOptionPane.showMessageDialog(null, "Table 'TimeTable' created successfully.");
        } catch (SQLException e) {
            if (e.getSQLState().equals("42X05")) { // Table already exists
                JOptionPane.showMessageDialog(null, "Table 'TimeTable' already exists.");
            } else {
                JOptionPane.showMessageDialog(null, "SQL error occurred: " + e.getMessage());
            }
        }
    }

    // Insert new TimeTable record
    public void insert(TimeTable table) throws SQLException {
        ensureConnection();  // Ensure connection before inserting
        String insertSQL = "INSERT INTO TimeTable (lectureId, venueId, slotId, groupId, subjectCode, type) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, table.getLectureId());
            ps.setInt(2, table.getVenueId());
            ps.setInt(3, table.getSlotId());
            ps.setInt(4, table.getGroupId());
            ps.setInt(5, table.getSubjectCode());
            ps.setString(6, table.getType());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    table.setTimeTableId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL error occurred during insertion: " + e.getMessage());
        }
    }

    // Retrieve all TimeTable records
    public List<TimeTable> getAll() throws SQLException {
        ensureConnection();  // Ensure connection before retrieving
        List<TimeTable> timeTables = new ArrayList<>();
        String selectSQL = "SELECT * FROM TimeTable";

        try (PreparedStatement ps = con.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TimeTable timeTable = new TimeTable();
                timeTable.setTimeTableId(rs.getInt("timeTableId"));
                timeTable.setLectureId(rs.getInt("lectureId"));
                timeTable.setVenueId(rs.getInt("venueId"));
                timeTable.setSlotId(rs.getInt("slotId"));
                timeTable.setGroupId(rs.getInt("groupId"));
                timeTable.setSubjectCode(rs.getInt("subjectCode"));
                timeTable.setType(rs.getString("type"));
                timeTables.add(timeTable);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL error occurred during retrieval: " + e.getMessage());
        }
        return timeTables;
    }

    // Retrieve TimeTables by groupId
    public ArrayList<TimeTable> retrieveGroups(int groupsId) throws SQLException {
        ensureConnection();  // Ensure connection before retrieving by group
        ArrayList<TimeTable> timeTables = new ArrayList<>();
        String sql = "SELECT * FROM TimeTable WHERE groupId = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, groupsId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TimeTable timeTable = new TimeTable();
                    timeTable.setTimeTableId(rs.getInt("timeTableId"));
                    timeTable.setLectureId(rs.getInt("lectureId"));
                    timeTable.setVenueId(rs.getInt("venueId"));
                    timeTable.setSlotId(rs.getInt("slotId"));
                    timeTable.setGroupId(rs.getInt("groupId"));
                    timeTable.setSubjectCode(rs.getInt("subjectCode"));
                    timeTable.setType(rs.getString("type"));
                    timeTables.add(timeTable);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + e.getMessage());
        }
        return timeTables;
    }

    // Retrieve selected TimeTables by groupId
    public ArrayList<TimeTable> selectedGroups(int groupsId) throws SQLException {
        ensureConnection();  // Ensure connection before retrieving selected groups
        ArrayList<TimeTable> timeTables = new ArrayList<>();
        String sql = "SELECT * FROM TimeTable WHERE groupId = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, groupsId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TimeTable timeTable = new TimeTable();
                    timeTable.setTimeTableId(rs.getInt("timeTableId"));
                    timeTable.setLectureId(rs.getInt("lectureId"));
                    timeTable.setVenueId(rs.getInt("venueId"));
                    timeTable.setSlotId(rs.getInt("slotId"));
                    timeTable.setGroupId(rs.getInt("groupId"));
                    timeTable.setSubjectCode(rs.getInt("subjectCode"));
                    timeTable.setType(rs.getString("type"));
                    timeTables.add(timeTable);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL error occurred during retrieval: " + e.getMessage());
        }
        return timeTables;
    }
}
