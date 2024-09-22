package za.ac.cput.timetableproject.dao;

import java.sql.*;
import java.util.ArrayList;
import za.ac.cput.timetableproject.connection.DatabaseConnection;
import za.ac.cput.timetableproject.domain.Lecture;

public class LectureDao {

    private Connection con;

    public LectureDao() throws SQLException {
        this.con = DatabaseConnection.createConnection();
        createTable(); // Ensure the table is created when the DAO is initialized
    }

    public void createTable() {
        String sql = "CREATE TABLE Lecture (" +
                     "LectureID INT PRIMARY KEY, " +
                     "LectureName VARCHAR(255) NOT NULL, " +
                     "LectureSurname VARCHAR(255) NOT NULL, " +
                     "LectureIntials CHAR(2) NOT NULL" +
                     ")";
        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql);
            System.out.println("Lecture table created or already exists.");
        } catch (SQLException e) {
            // Derby will throw an error if the table already exists. You can ignore this error.
            if (!e.getSQLState().equals("X0Y32")) { // SQLState for table already exists
                e.printStackTrace();
            }
        }
    }

    public void save(Lecture lecture) {
        String sql = "INSERT INTO Lecture (LectureID, LectureName, LectureSurname, LectureIntials) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, lecture.getLectureID());
            ps.setString(2, lecture.getLectureName());
            ps.setString(3, lecture.getLectureSurname());
            ps.setString(4, lecture.getLectureIntials());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLecture(int lectureID, String name, String surname, String initials) {
        String sql = "UPDATE Lecture SET LectureName = ?, LectureSurname = ?, LectureIntials = ? WHERE LectureID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, surname);
            ps.setString(3, initials);
            ps.setInt(4, lectureID);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Lecture updated successfully.");
            } else {
                System.out.println("No lecture found with the provided ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int lectureID) {
        String sql = "DELETE FROM Lecture WHERE LectureID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, lectureID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Lecture> readLectures() {
        ArrayList<Lecture> lectures = new ArrayList<>();
        String sql = "SELECT * FROM Lecture";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("LectureID");
                String name = rs.getString("LectureName");
                String surname = rs.getString("LectureSurname");
                String initials = rs.getString("LectureIntials");
                Lecture lecture = new Lecture(id, name, surname, initials);
                lectures.add(lecture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lectures;
    }

    public boolean isLectureIdExists(int lectureID) {
        String sql = "SELECT 1 FROM Lecture WHERE LectureID = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, lectureID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
