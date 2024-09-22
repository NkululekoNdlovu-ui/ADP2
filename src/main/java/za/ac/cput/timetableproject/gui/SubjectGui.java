/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.cput.timetableproject.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import za.ac.cput.timetableproject.dao.SubjectDao;
import za.ac.cput.timetableproject.domain.Subject;

/**
 *
 * @author hloni
 */
public class SubjectGui extends JPanel{
     private JButton addNew, change, delete;
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane pane;
    private ArrayList<Subject> list;

    public SubjectGui() {
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

        list = new ArrayList<>();

        // Set up the GUI layout
        setGui();
    }

    public void setGui() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3)); // Align buttons horizontally

        JPanel tablePanel = new JPanel(new BorderLayout());

        // Add columns to table model
        tableModel.addColumn("Subject Code");
        tableModel.addColumn("Description");

        // Add buttons to button panel
        buttonPanel.add(addNew);
        buttonPanel.add(change);
        buttonPanel.add(delete);

        // Add table and button panel to tablePanel
        tablePanel.add(pane, BorderLayout.CENTER);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add tablePanel to current panel
        this.add(tablePanel, BorderLayout.CENTER);

        // Action listeners for buttons
        addNew.addActionListener(e -> handleAddNew());
        change.addActionListener(e -> handleChange());
        delete.addActionListener(e -> handleDelete());
    }

    private void handleAddNew() {
        JPanel innerPanel = new JPanel(new GridLayout(2, 2));
        JTextField subjectCode = new JTextField();
        JTextField description = new JTextField();

        JLabel codeLabel = new JLabel("Subject Code");
        JLabel descriptionLabel = new JLabel("Description");

        innerPanel.add(codeLabel);
        innerPanel.add(subjectCode);
        innerPanel.add(descriptionLabel);
        innerPanel.add(description);

        int result = JOptionPane.showConfirmDialog(null, innerPanel, "Enter Subject Details", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String code1 = subjectCode.getText();
                String desc = description.getText();

                if (code1.isEmpty() || desc.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Subject Code and Description cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int code = Integer.parseInt(code1);
                SubjectDao dao = new SubjectDao();

                if (dao.isSubjectCodeExists(code)) {
                    JOptionPane.showMessageDialog(null, "Subject Code already exists.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Subject newSubject = new Subject(code, desc);
                dao.save(newSubject);

                list = dao.readSubjects();
                tableModel.setRowCount(0); // Clear existing rows
                for (Subject sub : list) {
                    tableModel.addRow(new Object[]{sub.getSubjectCode(), sub.getDescription()});
                }

                JOptionPane.showMessageDialog(null, "Subject successfully added");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
            }
        }
    }

    private void handleChange() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String oldCode = table.getValueAt(selectedRow, 0).toString();
            String oldDescription = table.getValueAt(selectedRow, 1).toString();

            JPanel innerPanel = new JPanel(new GridLayout(1, 2));
            JTextField newDescription = new JTextField();

            JLabel labelNewDescription = new JLabel("New Description:");

            innerPanel.add(labelNewDescription);
            innerPanel.add(newDescription);

            int result = JOptionPane.showConfirmDialog(null, innerPanel, "Update Subject Details", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String updatedDescription = newDescription.getText().trim();

                    if (updatedDescription.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Description cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    SubjectDao dao = new SubjectDao();
                    int old = Integer.parseInt(oldCode);

                    dao.updateSubject(old, updatedDescription);

                    tableModel.setValueAt(updatedDescription, selectedRow, 1);

                    JOptionPane.showMessageDialog(null, "Subject updated successfully!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
                }
            }
        }
    }

    private void handleDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String subjectCode1 = table.getValueAt(selectedRow, 0).toString();

            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this subject?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            int subjectCode = Integer.parseInt(subjectCode1);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    SubjectDao dao = new SubjectDao();
                    dao.delete(subjectCode);

                    tableModel.removeRow(selectedRow);

                    JOptionPane.showMessageDialog(null, "Subject deleted successfully!");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error occurred: " + ex.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a subject to delete.");
        }
    }
}
