package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;

public class StudentDetails extends JFrame implements ActionListener {

    Choice crollno;
    JTable table;
    JButton search, print, update, add, cancel;
    JTextField searchNameField, searchRollNoField;

    StudentDetails() {

        // Frame settings
        setTitle("Student Details");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Background and styling
        getContentPane().setBackground(new Color(0xF0F0F0));

        // Main heading
        JLabel heading = new JLabel("Student Details");
        heading.setBounds(350, 20, 300, 40);
        heading.setFont(new Font("Serif", Font.BOLD, 30));
        heading.setForeground(new Color(0x333333)); // Dark gray
        add(heading);

        // Dropdown Label
        JLabel rollnoDropdownLabel = new JLabel("Search by Roll Number (Dropdown):");
        rollnoDropdownLabel.setBounds(20, 100, 250, 25);
        rollnoDropdownLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(rollnoDropdownLabel);

        // Choice dropdown for Roll Number
        crollno = new Choice();
        crollno.setBounds(280, 100, 200, 30); // Wider dropdown
        crollno.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(crollno);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("Select * from student");
            while (rs.next()) {
                crollno.add(rs.getString("rollno"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Search by Roll Number (Text field)
        JLabel searchRollNoLabel = new JLabel("Search by Roll Number (Text):");
        searchRollNoLabel.setBounds(20, 150, 250, 25);
        searchRollNoLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(searchRollNoLabel);

        searchRollNoField = new JTextField();
        searchRollNoField.setBounds(280, 150, 200, 30); // Wider field
        add(searchRollNoField);

        // Search by Name
        JLabel searchNameLabel = new JLabel("Search by Name:");
        searchNameLabel.setBounds(20, 200, 250, 25);
        searchNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(searchNameLabel);

        searchNameField = new JTextField();
        searchNameField.setBounds(280, 200, 200, 30); // Wider field
        add(searchNameField);

        // Table to display student details
        table = new JTable();
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(50, 300, 800, 300); // Increased height to show more rows
        add(jsp);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("Select * from student");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Buttons with custom styling and adjusted positioning similar to TeacherDetails
        search = new JButton("Search");
        styleButton(search, 30, 650);
        search.addActionListener(this);
        add(search);

        print = new JButton("Print");
        styleButton(print, 150, 650);
        print.addActionListener(this);
        add(print);

        add = new JButton("Add");
        styleButton(add, 270, 650);
        add.addActionListener(this);
        add(add);

        update = new JButton("Update");
        styleButton(update, 390, 650);
        update.addActionListener(this);
        add(update);

        cancel = new JButton("Cancel");
        styleButton(cancel, 510, 650);
        cancel.addActionListener(this);
        add(cancel);

        // Frame size and visibility
        setSize(900, 750); // Adjust height to accommodate buttons at the bottom
        setLocation(300, 100);
        setVisible(true);
    }

    // Method to style buttons
    private void styleButton(JButton button, int x, int y) {
        button.setBounds(x, y, 100, 30); // Button size and position
        button.setBackground(new Color(0x4CAF50)); // Green color
        button.setForeground(Color.WHITE); // White text
        button.setFont(new Font("Tahoma", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0x3E8E41)); // Darker green on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0x4CAF50)); // Normal color
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == search) {
            String dropdownQuery = "select * from student where rollno ='" + crollno.getSelectedItem() + "'";
            String rollnoQuery = "select * from student where rollno like '%" + searchRollNoField.getText() + "%'";
            String nameQuery = "select * from student where name like '%" + searchNameField.getText() + "%'";

            try {
                Conn c = new Conn();
                ResultSet rs;
                if (!searchNameField.getText().equals("")) {
                    // If the search bar for name is used
                    rs = c.s.executeQuery(nameQuery);
                } else if (!searchRollNoField.getText().equals("")) {
                    // If the search bar for roll number is used
                    rs = c.s.executeQuery(rollnoQuery);
                } else {
                    // If the dropdown is used
                    rs = c.s.executeQuery(dropdownQuery);
                }
                table.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == print) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == add) {
            setVisible(false);
            new AddStudent();
        } else if (ae.getSource() == update) {
            setVisible(false);
            new UpdateStudent(); // This will open the UpdateStudent page
        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new StudentDetails();
    }
}
