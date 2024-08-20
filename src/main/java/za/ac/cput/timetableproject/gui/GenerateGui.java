package za.ac.cput.timetableproject.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class GenerateGui extends JPanel {

    private JComboBox<String> groupComboBox;
    private JComboBox<String> lecturerComboBox;
    private JComboBox<String> slotComboBox;
    private JComboBox<String> typeComboBox;
    private JComboBox<String> dayComboBox;
    private JComboBox<String> subjectComboBox;
    private JComboBox<String> venueComboBox;
    private JTable timetableTable;
    private DefaultTableModel tableModel;

    private Map<String, Map<String, String>> groupTimetables;
    private Map<String, String> lecturerAssignments;

    public GenerateGui() {
        setLayout(new BorderLayout());

        groupTimetables = new HashMap<>();
        lecturerAssignments = new HashMap<>();

        groupComboBox = new JComboBox<>(new String[]{"Group A", "Group B", "Group C"});
        lecturerComboBox = new JComboBox<>(new String[]{"Lecturer 1", "Lecturer 2", "Lecturer 3"});
        slotComboBox = new JComboBox<>(new String[]{
            "08:30 - 09:10", "09:15 - 09:55", "10:00 - 10:40", "10:45 - 11:25",
            "11:30 - 12:10", "12:15 - 12:55", "13:00 - 13:40", "13:45 - 14:25",
            "14:30 - 15:10", "15:15 - 15:55", "16:00 - 16:40", "16:45 - 17:25"
        });
        typeComboBox = new JComboBox<>(new String[]{"Lecture", "Tutorial", "Lab"});
        dayComboBox = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"});
        subjectComboBox = new JComboBox<>(new String[]{"Math", "Physics", "Computer Science"});
        venueComboBox = new JComboBox<>(new String[]{"Venue 1", "Venue 2", "Venue 3"});

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
        String group = (String) groupComboBox.getSelectedItem();
        String lecturer = (String) lecturerComboBox.getSelectedItem();
        String slot = (String) slotComboBox.getSelectedItem();
        String type = (String) typeComboBox.getSelectedItem();
        String day = (String) dayComboBox.getSelectedItem();
        String subject = (String) subjectComboBox.getSelectedItem();
        String venue = (String) venueComboBox.getSelectedItem();

        if (slot.equals("13:00 - 13:40")) {
            JOptionPane.showMessageDialog(this, "13:00 to 13:40 is a break. Please select a different time slot.", "Break Time", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String daySlotKey = day + "-" + slot;

        groupTimetables.putIfAbsent(group, new HashMap<>());

        Map<String, String> timetable = groupTimetables.get(group);

        if (timetable.containsKey(daySlotKey)) {
            JOptionPane.showMessageDialog(this, "This slot is already booked for " + group + "!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String lecturerKey = lecturer + "-" + daySlotKey;
        if (lecturerAssignments.containsKey(lecturerKey)) {
            JOptionPane.showMessageDialog(this, "Lecturer " + lecturer + " is already assigned to " + lecturerAssignments.get(lecturerKey) + " at this time!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cellValue = "<html>" + lecturer + "<br><b>" + subject + "</b><br>" + venue + " (" + type + ")" + "</html>";
        timetable.put(daySlotKey, cellValue);

        lecturerAssignments.put(lecturerKey, venue);

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

        tableModel.setValueAt(cellValue, rowIndex, columnIndex);
    }

    private void saveTimetable() {
        String group = (String) groupComboBox.getSelectedItem();

        int response = JOptionPane.showConfirmDialog(this, "Do you want to save the timetable for " + group + "?", "Save Timetable", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Timetable for " + group + " has been saved successfully!", "Save Success", JOptionPane.INFORMATION_MESSAGE);

            groupComboBox.setSelectedIndex(0);
            lecturerComboBox.setSelectedIndex(0);
            slotComboBox.setSelectedIndex(0);
            typeComboBox.setSelectedIndex(0);
            dayComboBox.setSelectedIndex(0);
            subjectComboBox.setSelectedIndex(0);
            venueComboBox.setSelectedIndex(0);

            tableModel.setRowCount(0);
            tableModel.setRowCount(5);
            tableModel.setValueAt("Monday", 0, 0);
            tableModel.setValueAt("Tuesday", 1, 0);
            tableModel.setValueAt("Wednesday", 2, 0);
            tableModel.setValueAt("Thursday", 3, 0);
            tableModel.setValueAt("Friday", 4, 0);

            for (int i = 0; i < 5; i++) {
                tableModel.setValueAt("Break", i, 7);
            }
        }
    }
}

