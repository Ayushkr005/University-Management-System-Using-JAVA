package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.*;

public class StudentLeaveDetails extends JFrame implements ActionListener {
    
    Choice crollno;
    JTable table;
    JButton search, print, cancel;
    JTextField searchRollnoField;

    StudentLeaveDetails() {

        setTitle("Student Leave Details");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Background Color and basic styling
        getContentPane().setBackground(new Color(0xF0F0F0)); // Light gray

        // Main heading
        JLabel heading = new JLabel("Student Leave Details");
        heading.setBounds(350, 20, 300, 40);
        heading.setFont(new Font("Serif", Font.BOLD, 30));
        heading.setForeground(new Color(0x333333)); // Dark gray text
        add(heading);

        // Dropdown Label
        JLabel rollnoLabel = new JLabel("Search by Roll Number (Dropdown):");
        rollnoLabel.setBounds(20, 100, 250, 25);
        rollnoLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        rollnoLabel.setForeground(Color.BLACK);
        add(rollnoLabel);

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
        JLabel searchRollnoLabel = new JLabel("Search by Roll Number (Text):");
        searchRollnoLabel.setBounds(20, 150, 250, 25);
        searchRollnoLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        searchRollnoLabel.setForeground(Color.BLACK);
        add(searchRollnoLabel);

        searchRollnoField = new JTextField();
        searchRollnoField.setBounds(280, 150, 200, 30); // Wider field
        add(searchRollnoField);

        // Table to display student leave details
        table = new JTable();
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(50, 250, 800, 300); // Increased height to show more rows
        add(jsp);

        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("Select * from studentleave");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Buttons with custom styling
        search = new JButton("Search");
        styleButton(search, 550, 100);
        search.addActionListener(this);
        add(search);

        print = new JButton("Print");
        styleButton(print, 650, 100);
        print.addActionListener(this);
        add(print);

        cancel = new JButton("Cancel");
        styleButton(cancel, 600, 150);
        cancel.addActionListener(this);
        add(cancel);

        setSize(900, 700);
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
            String dropdownQuery = "select * from studentleave where rollno ='" + crollno.getSelectedItem() + "'";
            String rollnoQuery = "select * from studentleave where rollno like '%" + searchRollnoField.getText() + "%'";
            
            try {
                Conn c = new Conn();
                ResultSet rs;
                if (!searchRollnoField.getText().equals("")) {
                    rs = c.s.executeQuery(rollnoQuery);
                } else {
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
        } else if (ae.getSource() == cancel) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new StudentLeaveDetails();
    }
}
