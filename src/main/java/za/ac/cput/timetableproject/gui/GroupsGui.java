
package za.ac.cput.timetableproject.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
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
         JFrame frame = new JFrame();
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
          add_New.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel promt = new JPanel(new GridLayout(2, 2)); //3 rows and 2 columns
                JTextField groupID = new JTextField();
                JTextField groupCapacity = new JTextField();

                promt.add(new JLabel("Group ID: "));
                promt.add(groupID);
                promt.add(new JLabel("Group Name: "));
                promt.add(groupCapacity);

                int choiceResult = JOptionPane.showConfirmDialog(frame, promt, "Add New Group", JOptionPane.YES_NO_OPTION);

                if (choiceResult == JOptionPane.YES_OPTION) {

                    int group_id = Integer.parseInt(groupID.getText());
                    String group_name = groupCapacity.getText();

                    Group groups = new Group(group_id, group_name);

                    GroupsDao dao = new GroupsDao();
                    dao.addNew(groups);

                    ArrayList<Group> groupsList = dao.readFromDB();
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.setRowCount(0); // Clear the table

                    for (int i = 0; i < groupsList.size(); i++) {
                        model.addRow(new Object[]{groupsList.get(i).getGroupId(),
                            groupsList.get(i).getGroupName()});

                    }

//                    model.addColumn(new Objetc["Group ID" , "Group Capacity"]);
                    tableModel.addRow(new Object[]{group_id, group_name});

                    dao.populateTable(table);
                }

            }

        });

        change.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GroupsDao dao = new GroupsDao();
                
                JPanel promptChange = new JPanel(new GridLayout(2,2));
                JLabel changeID = new JLabel("Change Name of ID : ");
                JLabel changeCapacity = new JLabel("Change Name: ");
                JTextField changeIdTxt = new JTextField();
                JTextField changeCapacityTxt = new JTextField();
                
                promptChange.add(changeID);
                promptChange.add(changeIdTxt);
                promptChange.add(changeCapacity);
                promptChange.add(changeCapacityTxt);
                
                int choice = JOptionPane.showConfirmDialog(frame, promptChange,"Change Name", JOptionPane.YES_NO_OPTION);
                
               if(choice == JOptionPane.OK_OPTION){
                   int existingId = Integer.parseInt(changeIdTxt.getText());
                   String groupName = changeCapacityTxt.getText();
                   
                   Group groups = new Group(existingId,groupName);
                   dao.update(groups);
                   
                   //update the table
                   dao.populateTable(table);  
               }
            }
        });

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GroupsDao dao = new GroupsDao();
                
                JPanel promptDeleteById = new JPanel(new GridLayout(1,2)); //1 row and 2 columns
                JLabel deletebyId_label = new JLabel("Delete by ID: ");
                JTextField deleteByIdTxt = new JTextField();
                
                promptDeleteById.add(deletebyId_label);
                promptDeleteById.add(deleteByIdTxt);
                
                int choice = JOptionPane.showConfirmDialog(frame,promptDeleteById,"Delete",JOptionPane.YES_NO_CANCEL_OPTION);
//                int selectedRow = table.getSelectedRow();

                if (choice == JOptionPane.YES_OPTION) {
//                    tableModel.removeRow(selectedRow);
                      String deleteId = deleteByIdTxt.getText();
                      
                      if(!deleteId.isEmpty()){
                        dao.delete(deleteId);  // Pass the ID to the DAO delete method

                        dao.populateTable(table);
                      }else{
                          JOptionPane.showMessageDialog(null,"No matching ID found");
                      }
               }
            }
        });



     
    }
}
