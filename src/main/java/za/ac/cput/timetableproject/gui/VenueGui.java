package za.ac.cput.timetableproject.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import za.ac.cput.timetableproject.dao.VenueDao;
import za.ac.cput.timetableproject.domain.Venue;

public class VenueGui extends JPanel {

    private JButton addNew, change, delete;
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane pane;

    public VenueGui() {
        setLayout(new BorderLayout());

        // Initialize buttons
        addNew = new JButton("Add New");
        change = new JButton("Change");
        delete = new JButton("Delete");

        // Set button size
        Dimension buttonSize = new Dimension(100, 30);
        addNew.setPreferredSize(buttonSize);
        change.setPreferredSize(buttonSize);
        delete.setPreferredSize(buttonSize);

        // Initialize table and scroll pane
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        pane = new JScrollPane(table);
        setSize(450, 250);

        // Set up the GUI layout
        setGui();
    }

    private void setGui() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3)); // GridLayout to align buttons horizontally

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Add columns to table model
        tableModel.addColumn("VenueID");
        tableModel.addColumn("Venue Name");

        // Add buttons to panel
        panel.add(addNew);
        panel.add(change);
        panel.add(delete);

        // Add components to main panel
        mainPanel.add(pane, BorderLayout.CENTER);
        mainPanel.add(panel, BorderLayout.SOUTH);

        // Add main panel to the current panel
        this.add(mainPanel, BorderLayout.CENTER);

        // Add action listeners for buttons
        addNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add new venue logic
                JPanel innerPanel = new JPanel(new GridLayout(2, 2));
                JTextField venueId = new JTextField();
                JTextField venueName = new JTextField();

                JLabel lblVenueId = new JLabel("Venue ID");
                JLabel lblVenueName = new JLabel("Venue Name");

                innerPanel.add(lblVenueId);
                innerPanel.add(venueId);
                innerPanel.add(lblVenueName);
                innerPanel.add(venueName);

                int result = JOptionPane.showConfirmDialog(null, innerPanel,
                        "Enter Venue Details", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int id = Integer.parseInt(venueId.getText());
                        String name = venueName.getText();

                        Venue newVenue = new Venue(id, name);
                        VenueDao dao = new VenueDao();

                        if (dao.isVenueIdExists(id)) {
                            JOptionPane.showMessageDialog(null, "Venue ID already exists.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        dao.saveVenue(newVenue);
                        tableModel.addRow(new Object[]{id, name});
                        JOptionPane.showMessageDialog(null, "Venue successfully added");

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid number for Venue ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
                    }
                }
            }
        });

        change.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Change venue logic
                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {
                    int oldVenueId = (int) table.getValueAt(selectedRow, 0);

                    JPanel innerPanel = new JPanel(new GridLayout(1, 2));
                    JTextField newName = new JTextField();

                    JLabel labelNewName = new JLabel("New Venue Name:");

                    innerPanel.add(labelNewName);
                    innerPanel.add(newName);

                    int result = JOptionPane.showConfirmDialog(null, innerPanel, "Update Venue Details", JOptionPane.OK_CANCEL_OPTION);

                    if (result == JOptionPane.OK_OPTION) {
                        try {
                            String updatedName = newName.getText().trim();

                            VenueDao dao = new VenueDao();
                            dao.updateVenue(oldVenueId, updatedName);

                            tableModel.setValueAt(updatedName, selectedRow, 1);

                            JOptionPane.showMessageDialog(null, "Venue updated successfully!");

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
                        }
                    }
                }
            }
        });

        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Delete venue logic
                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {
                    int venueId = (int) table.getValueAt(selectedRow, 0);

                    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this venue?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            VenueDao dao = new VenueDao();
                            dao.deleteVenue(venueId);

                            tableModel.removeRow(selectedRow);

                            JOptionPane.showMessageDialog(null, "Venue deleted successfully!");

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a venue to delete.");
                }
            }
        });
    }
}
