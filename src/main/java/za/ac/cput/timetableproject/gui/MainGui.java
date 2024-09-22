package za.ac.cput.timetableproject.gui;

import java.awt.*;
import javax.swing.*;

public class MainGui extends JFrame {

    private JButton generate, venue, subject, lecture, groups;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainGui() {
        // Initialize components
        generate = new JButton("Generate");
        lecture = new JButton("Lecture");
        venue = new JButton("Venue");
        subject = new JButton("Subject");
        groups = new JButton("Groups");

        // Set up the GUI
        setGui();
    }

    private void setGui() {
        // Set up the frame
        setTitle("Main GUI");
        setSize(800, 600); // Adjust size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel for buttons and set its layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1)); // Six rows for the buttons

        // Add buttons to the panel
        buttonPanel.add(generate);
        buttonPanel.add(lecture);
        buttonPanel.add(venue);
        buttonPanel.add(subject); // Add Subject button here
        buttonPanel.add(groups);

        // Add the button panel to the west side of the frame
        add(buttonPanel, BorderLayout.WEST);

        // Create a panel to hold the content
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        // Create a panel to hold VenueGui, LectureGui, GroupsGui, GenerateGui, and SubjectGui
        JPanel contentHolder = new JPanel(new CardLayout());

        // Initial image panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\hloni\\downloads\\cput.png"); // Ensure the path is correct
        JLabel imageLabel = new JLabel(imageIcon);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Ensure the image scales to fit the panel
        imageLabel.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(-1, 600, Image.SCALE_SMOOTH)));

        // Add imagePanel to contentHolder
        contentHolder.add(imagePanel, "Image");

        // Create instances of GUI panels
        VenueGui venueGui = new VenueGui();
        LectureGui lectureGui = new LectureGui();
        GroupsGui groupsGui = new GroupsGui();
        GenerateGui generateGui = new GenerateGui();
        SubjectGui subjectGui = new SubjectGui(); // Create instance of SubjectGui

        // Add panels to contentHolder
        contentHolder.add(venueGui, "Venue");
        contentHolder.add(lectureGui, "Lecture");
        contentHolder.add(groupsGui, "Groups");
        contentHolder.add(generateGui, "Generate");
        contentHolder.add(subjectGui, "Subject"); // Add SubjectGui to contentHolder

        contentPanel.add(contentHolder, BorderLayout.CENTER);

        // Add content panel to the center of the frame
        add(contentPanel, BorderLayout.CENTER);

        // Initialize CardLayout
        cardLayout = (CardLayout) contentHolder.getLayout();
        cardLayout.show(contentHolder, "Image");

        // Add action listeners for buttons
        lecture.addActionListener(e -> cardLayout.show(contentHolder, "Lecture"));
        venue.addActionListener(e -> cardLayout.show(contentHolder, "Venue"));
        groups.addActionListener(e -> cardLayout.show(contentHolder, "Groups"));
        generate.addActionListener(e -> cardLayout.show(contentHolder, "Generate"));
        subject.addActionListener(e -> cardLayout.show(contentHolder, "Subject")); // Add action listener for Subject button

        // Make the frame visible
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGui::new);
    }
}
