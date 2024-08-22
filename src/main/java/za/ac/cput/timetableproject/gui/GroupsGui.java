
package za.ac.cput.timetableproject.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import za.ac.cput.timetableproject.dao.GroupsDao;
import za.ac.cput.timetableproject.domain.Group;

public class GroupsGui extends JPanel {

    private JButton add_New, change, delete;
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane pane;

    public GroupsGui() {
        setLayout(new BorderLayout());

        // Initialize buttons
        add_New = new JButton("Add New");
        change = new JButton("Change");
        delete = new JButton("Delete");

        // Initialize table model and table
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        pane = new JScrollPane(table);
        setSize(450, 250);

        // Set up the GUI layout
        setGui();
    }

    private void setGui() {
        // Set up table model columns
        tableModel.addColumn("GroupID");
        tableModel.addColumn("Group Name");

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3)); // Three buttons in one row
        buttonPanel.add(add_New);
        buttonPanel.add(change);
        buttonPanel.add(delete);

        // Create main panel and add components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(pane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(mainPanel, BorderLayout.CENTER);

        // Add action listeners for buttons
        //add new function for btn 

        add_New.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        JPanel innerPanel = new JPanel(new GridLayout(2, 2));
        JTextField groupIdField = new JTextField();
        JTextField groupNameField = new JTextField();

        JLabel groupIdLabel = new JLabel("Group ID:");
        JLabel groupNameLabel = new JLabel("Group Name:");

        innerPanel.add(groupIdLabel);
        innerPanel.add(groupIdField);
        innerPanel.add(groupNameLabel);
        innerPanel.add(groupNameField);

        int result = JOptionPane.showConfirmDialog(null, innerPanel,
                "Enter Group Details", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int groupId = Integer.parseInt(groupIdField.getText());
                String groupName = groupNameField.getText();

                if (groupName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Group Name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                GroupsDao dao = new GroupsDao();

                if (dao.isGroupIdExists(groupId)) {
                    JOptionPane.showMessageDialog(null, "Group ID already exists.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Group newGroup = new Group(groupId, groupName);
                if (dao.save(newGroup)) {
                    tableModel.addRow(new Object[]{groupId, groupName});
                    JOptionPane.showMessageDialog(null, "Group successfully added.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error occurred while saving the group.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Group ID must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
});


//change function for btn 
       change.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        // Change group logic
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            String groupId = table.getValueAt(selectedRow, 0).toString();
            int groupId2 = Integer.parseInt(groupId);

            JPanel innerPanel = new JPanel(new GridLayout(1, 2));
            JTextField groupNameField = new JTextField();

            JLabel labelNewName = new JLabel("New Group Name:");

            innerPanel.add(labelNewName);
            innerPanel.add(groupNameField);

            int result = JOptionPane.showConfirmDialog(null, innerPanel, "Update Group Details", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String newGroupName = groupNameField.getText().trim();

                    GroupsDao dao = new GroupsDao();
                    dao.updateGroup(groupId2, newGroupName);

                    DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                    tableModel.setValueAt(newGroupName, selectedRow, 1);

                    JOptionPane.showMessageDialog(null, "Group updated successfully!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a group to update.");
        }
    }
});
//delete function for btn 

        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Delete group logic
                 int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {
                    int groupId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());

                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this group?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            GroupsDao dao = new GroupsDao();
                            dao.deleteGroup(groupId);

                            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                            tableModel.removeRow(selectedRow);

                            JOptionPane.showMessageDialog(null, "Group deleted successfully!");

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a group to delete.");
                }
            
            }
        });
    }
}
