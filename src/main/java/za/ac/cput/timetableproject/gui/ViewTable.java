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
import java.util.logging.Level;
import java.util.logging.Logger;
import za.ac.cput.timetableproject.dao.*;
import za.ac.cput.timetableproject.domain.Group;
import za.ac.cput.timetableproject.domain.*;
import za.ac.cput.timetableproject.dao.*;

public class ViewTable extends JPanel {

    private JTable timetableTable;
    private DefaultTableModel tableModel;
    private JComboBox<Group> groupComboBox; // Combo box for groups
    GroupsDao gDao = new GroupsDao();
    SubjectDao sDao;
    VenueDao vDao ;
    LectureDao lDao;
    SlotDao slotDao;
    
    TimeTableDao tDao = new TimeTableDao();
    public ViewTable() throws SQLException {
        setLayout(new BorderLayout());
        sDao = new SubjectDao();
        vDao = new VenueDao();
        lDao = new LectureDao();
        slotDao = new SlotDao();
        // Add a combo box for group selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel groupLabel = new JLabel("Select Group:");
        groupComboBox = new JComboBox<>();
        topPanel.add(groupLabel);
        topPanel.add(groupComboBox);
                // Add top panel to the north of the BorderLayout
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Day", "08:30 - 09:10", "09:15 - 09:55", "10:00 - 10:40", "10:45 - 11:25",
                "11:30 - 12:10", "12:15 - 12:55", "13:00 - 13:40", "13:45 - 14:25",
                "14:30 - 15:10", "15:15 - 15:55", "16:00 - 16:40", "16:45 - 17:25"};
        populateGroups();

        tableModel = new DefaultTableModel(columnNames, 5);
        timetableTable = new JTable(tableModel);

        // Set day labels
        tableModel.setValueAt("Monday", 0, 0);
        tableModel.setValueAt("Tuesday", 1, 0);
        tableModel.setValueAt("Wednesday", 2, 0);
        tableModel.setValueAt("Thursday", 3, 0);
        tableModel.setValueAt("Friday", 4, 0);

        // Set break times
        for (int i = 0; i < 5; i++) {
            tableModel.setValueAt("Break", i, 7);
        }

        // Adjust table layout
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
        
       groupComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Group selectedGroups = (Group)groupComboBox.getSelectedItem();
                ArrayList<TimeTable> list = null;
                try {
                    list = tDao.selectedGroups(selectedGroups.getGroupId());
                } catch (SQLException ex) {
                    Logger.getLogger(ViewTable.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                int sId = list.get(0).getSubjectCode();
                Subject subject = null;
                try {
                    subject = sDao.selectedSubject(sId);
                } catch (SQLException ex) {
                    Logger.getLogger(ViewTable.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                int vId = list.get(0).getVenueId();
                Venue venue = null;
                try {
                    venue = vDao.selectedVenue(vId);
                } catch (SQLException ex) {
                    Logger.getLogger(ViewTable.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                int lId = list.get(0).getLectureId();
                Lecture lecture = null;
                try {
                    lecture = lDao.getLectureById(lId);
                } catch (SQLException ex) {
                    Logger.getLogger(ViewTable.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                int slotId = list.get(0).getSlotId();
                Slot slot = null;
                try {
                     slot = slotDao.getSlotById(slotId);
                } catch (SQLException ex) {
                    
                }
                
                System.out.println(subject+""+""+ venue+""+lecture+""+slot);
                
            }
        });
    }
    

    public JTable getTimetableTable() {
        return timetableTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

  
     private void populateGroups() throws SQLException {
        gDao = new GroupsDao();
        ArrayList<Group> groups = gDao.readGroups();
        for (Group group : groups) {
            groupComboBox.addItem(group);
        }
    }
     
  // ArrayList<TimeTable> list = tDao.selectedGroups(PROPERTIES)
     
     
     
}
