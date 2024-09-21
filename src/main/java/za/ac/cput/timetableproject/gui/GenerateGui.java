package za.ac.cput.timetableproject.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.ac.cput.timetableproject.dao.*;
import za.ac.cput.timetableproject.domain.Group;
import za.ac.cput.timetableproject.domain.Lecture;
import za.ac.cput.timetableproject.domain.Slot;
import za.ac.cput.timetableproject.domain.Subject;
import za.ac.cput.timetableproject.domain.TimeTable;
import za.ac.cput.timetableproject.domain.Venue;

public class GenerateGui extends JPanel {

    private JComboBox<Group> groupComboBox;
    private JComboBox<Lecture> lecturerComboBox;
    private JComboBox<String> slotComboBox;
    private JComboBox<String> typeComboBox;
    private JComboBox<String> dayComboBox;
    private JComboBox<Subject> subjectComboBox;
    private JComboBox<Venue> venueComboBox;
    private JTable timetableTable;
    private DefaultTableModel tableModel;

    private Map<String, Map<String, String>> groupTimetables;
    private Map<String, String> lecturerAssignments;

    private LectureDao lDao;
    private GroupsDao gDao;
    private SubjectDao sDao;
    private VenueDao vDao;
    private TimeTableDao tDao;
    private SlotDao slotDao;
    private int slotId;

    public GenerateGui() {
        setLayout(new BorderLayout());
        tDao = new TimeTableDao();
        slotDao = new SlotDao();
        groupTimetables = new HashMap<>();
        lecturerAssignments = new HashMap<>();

        groupComboBox = new JComboBox<>();
        lecturerComboBox = new JComboBox<>();
        slotComboBox = new JComboBox<>(new String[]{
                "08:30 - 09:10", "09:15 - 09:55", "10:00 - 10:40", "10:45 - 11:25",
                "11:30 - 12:10", "12:15 - 12:55", "13:00 - 13:40", "13:45 - 14:25",
                "14:30 - 15:10", "15:15 - 15:55", "16:00 - 16:40", "16:45 - 17:25"
        });
        typeComboBox = new JComboBox<>(new String[]{"Lecture", "Tutorial", "Lab"});
        dayComboBox = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"});
        subjectComboBox = new JComboBox<>();
        venueComboBox = new JComboBox<>();

        populateLecture();
        populateGroups();
        populateSubject();
        populateVenue();

        JPanel inputPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        inputPanel.add(new JLabel("Select Group:"));
        inputPanel.add(groupComboBox);
        inputPanel.add(new JLabel("Select Lecturer:"));
        inputPanel.add(lecturerComboBox);
        inputPanel.add(new JLabel("Select Slot:"));
        inputPanel.add(slotComboBox);
        inputPanel.add(new JLabel("Select Type:"));
        inputPanel.add(typeComboBox);
        inputPanel.add(new JLabel("Select Day:"));
        inputPanel.add(dayComboBox);
        inputPanel.add(new JLabel("Select Subject:"));
        inputPanel.add(subjectComboBox);
        inputPanel.add(new JLabel("Select Venue:"));
        inputPanel.add(venueComboBox);

        JButton generateButton = new JButton("Generate Timetable");
        JButton saveButton = new JButton("Save Timetable");
        inputPanel.add(generateButton);
        inputPanel.add(saveButton);

        add(inputPanel, BorderLayout.NORTH);

        String[] columnNames = {"Day", "08:30 - 09:10", "09:15 - 09:55", "10:00 - 10:40", "10:45 - 11:25",
                "11:30 - 12:10", "12:15 - 12:55", "13:00 - 13:40", "13:45 - 14:25",
                "14:30 - 15:10", "15:15 - 15:55", "16:00 - 16:40", "16:45 - 17:25"};
        tableModel = new DefaultTableModel(columnNames, 5);
        timetableTable = new JTable(tableModel);

        tableModel.setValueAt("Monday", 0, 0);
        tableModel.setValueAt("Tuesday", 1, 0);
        tableModel.setValueAt("Wednesday", 2, 0);
        tableModel.setValueAt("Thursday", 3, 0);
        tableModel.setValueAt("Friday", 4, 0);

        for (int i = 0; i < 5; i++) {
            tableModel.setValueAt("Break", i, 7);
        }

        timetableTable.setRowHeight(80);
        TableColumnModel columnModel = timetableTable.getColumnModel();
        for (int i = 1; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(200);
        }

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setVerticalAlignment(SwingConstants.TOP);
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
        timetableTable.setDefaultRenderer(Object.class, renderer);

        add(new JScrollPane(timetableTable), BorderLayout.CENTER);

        generateButton.addActionListener(e -> generateTimetable());
        saveButton.addActionListener(e -> saveTimetable());
    }

    private void generateTimetable() {
        String day = (String) dayComboBox.getSelectedItem();
        String slot = (String) slotComboBox.getSelectedItem();

        String[] times = slot.split(" - ");
        String startTime = times[0];
        String endTime = times[1];

        Slot slot2 = new Slot(startTime, endTime, day);
        slotId = -1;

        try {
            slotId = slotDao.insert(slot2);
        } catch (SQLException ex) {
            Logger.getLogger(GenerateGui.class.getName()).log(Level.SEVERE, null, ex);
        }

        Group selectedGroup = (Group) groupComboBox.getSelectedItem();
        int groupId = selectedGroup != null ? selectedGroup.getGroupId() : -1;

        Lecture selectedLecture = (Lecture) lecturerComboBox.getSelectedItem();
        int lectureId = selectedLecture != null ? selectedLecture.getLectureID() : -1;

        String type = (String) typeComboBox.getSelectedItem();

        Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
        int subjectId = selectedSubject != null ? selectedSubject.getSubjectCode() : -1;

        Venue selectedVenue = (Venue) venueComboBox.getSelectedItem();
        int venueId = selectedVenue != null ? selectedVenue.getVenueId() : -1;

        if (slot.equals("13:00 - 13:40")) {
            JOptionPane.showMessageDialog(this, "13:00 to 13:40 is a break. Please select a different time slot.", "Break Time", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String daySlotKey = day + "-" + slot;

        groupTimetables.putIfAbsent(selectedGroup.toString(), new HashMap<>());

        Map<String, String> timetable = groupTimetables.get(selectedGroup.toString());

        if (timetable.containsKey(daySlotKey)) {
            JOptionPane.showMessageDialog(this, "This slot is already booked for " + selectedGroup.toString() + "!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String lecturerKey = selectedLecture.toString() + "-" + daySlotKey;
        if (lecturerAssignments.containsKey(lecturerKey)) {
            JOptionPane.showMessageDialog(this, "Lecturer " + selectedLecture.toString() + " is already assigned to " + lecturerAssignments.get(lecturerKey) + " at this time!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cellValue = "<html>" + selectedLecture + "<br><b>" + selectedSubject + "</b><br>" + selectedVenue + " (" + type + ")" + "</html>";
        timetable.put(daySlotKey, cellValue);

        lecturerAssignments.put(lecturerKey, selectedVenue.toString());

        int rowIndex = 0;
        int columnIndex = 0;

        switch (day) {
            case "Monday":
                rowIndex = 0;
                break;
            case "Tuesday":
                rowIndex = 1;
                break;
            case "Wednesday":
                rowIndex = 2;
                break;
            case "Thursday":
                rowIndex = 3;
                break;
            case "Friday":
                rowIndex = 4;
                break;
        }

        switch (slot) {
            case "08:30 - 09:10":
                columnIndex = 1;
                break;
            case "09:15 - 09:55":
                columnIndex = 2;
                break;
            case "10:00 - 10:40":
                columnIndex = 3;
                break;
            case "10:45 - 11:25":
                columnIndex = 4;
                break;
            case "11:30 - 12:10":
                columnIndex = 5;
                break;
            case "12:15 - 12:55":
                columnIndex = 6;
                break;
            case "13:00 - 13:40":
                columnIndex = 7; 
                break;
            case "13:45 - 14:25":
                columnIndex = 8;
                break;
            case "14:30 - 15:10":
                columnIndex = 9;
                break;
            case "15:15 - 15:55":
                columnIndex = 10;
                break;
            case "16:00 - 16:40":
                columnIndex = 11;
                break;
            case "16:45 - 17:25":
                columnIndex = 12;
                break;
        }

        TimeTable tt = new TimeTable(lectureId, venueId, slotId, groupId, subjectId, type);
        try {
            tDao.insert(tt);
            JOptionPane.showMessageDialog(null, "Added the timetable to db");
            tableModel.setValueAt(cellValue, rowIndex, columnIndex); 
        } catch (Exception k) {
            k.printStackTrace(); 
            JOptionPane.showMessageDialog(null, "Error adding timetable: " + k.getMessage());
        }
    }

    private void saveTimetable() {
       
        JOptionPane.showMessageDialog(this, "Timetable saved successfully.");
    }

    private void populateGroups() {
        gDao = new GroupsDao();
        List<Group> groups = gDao.readGroups();
        for (Group group : groups) {
            groupComboBox.addItem(group);
        }
    }

    private void populateLecture() {
        try {
            lDao = new LectureDao();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Lecture> lectures = lDao.readLectures();
        for (Lecture lecture : lectures) {
            lecturerComboBox.addItem(lecture);
        }
    }

    private void populateSubject() {
        sDao = new SubjectDao();
        List<Subject> subjects = sDao.readSubjects();
        for (Subject subject : subjects) {
            subjectComboBox.addItem(subject);
        }
    }

    private void populateVenue() {
        try {
            vDao = new VenueDao();
        } catch (SQLException ex) {
            Logger.getLogger(GenerateGui.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Venue> venues = vDao.readVenue();
        for (Venue venue : venues) {
            venueComboBox.addItem(venue);
        }
    }
}
