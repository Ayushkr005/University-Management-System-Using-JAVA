package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;

public class TeacherDetails extends JFrame implements ActionListener {
    
    Choice cEmpId;
    JTable table;
    JButton search, print, update, add, cancel;
    JTextField searchNameField, searchEmpIdField;

    TeacherDetails() {
        
        setTitle("Teacher Details");
        setLayout(null);

        // Set a plain background color instead of an image
        getContentPane().setBackground(new Color(0xF0F0F0));  // Light gray

        // Title with custom font and color
        JLabel heading = new JLabel("Teacher Details");
        heading.setBounds(350, 20, 300, 50);
        heading.setFont(new Font("Serif", Font.BOLD, 30));
        heading.setForeground(new Color(0x333333)); // Dark gray text
        add(heading);

        // Dropdown Label
        JLabel empIdLabel = new JLabel("Search by Employee ID (Dropdown):");
        empIdLabel.setBounds(20, 100, 250, 25);
        empIdLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        empIdLabel.setForeground(Color.BLACK);
        add(empIdLabel);
        
        // Choice dropdown for Employee ID
        cEmpId = new Choice();
        cEmpId.setBounds(280, 100, 150, 25);
        cEmpId.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(cEmpId);
        
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("Select * from teacher");
            while (rs.next()) {
                cEmpId.add(rs.getString("empId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Search by Employee ID (Text field)
        JLabel searchEmpIdLabel = new JLabel("Search by Employee ID (Text):");
        searchEmpIdLabel.setBounds(20, 150, 250, 25);
        searchEmpIdLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        searchEmpIdLabel.setForeground(Color.BLACK);
        add(searchEmpIdLabel);

        searchEmpIdField = new JTextField();
        searchEmpIdField.setBounds(280, 150, 150, 25);
        add(searchEmpIdField);

        // Search by Name
        JLabel searchNameLabel = new JLabel("Search by Name:");
        searchNameLabel.setBounds(20, 200, 150, 25);
        searchNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        searchNameLabel.setForeground(Color.BLACK);
        add(searchNameLabel);
        
        searchNameField = new JTextField();
        searchNameField.setBounds(280, 200, 150, 25);
        add(searchNameField);
        
        // Table to display teacher details
        table = new JTable();
        
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("Select * from teacher");
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(50, 300, 800, 300);
        add(jsp);

        // Custom button styling
        search = new JButton("Search");
        styleButton(search, 50, 250);
        search.addActionListener(this);
        add(search);

        print = new JButton("Print");
        styleButton(print, 150, 250);
        print.addActionListener(this);
        add(print);

        add = new JButton("Add");
        styleButton(add, 250, 250);
        add.addActionListener(this);
        add(add);

        update = new JButton("Update");
        styleButton(update, 350, 250);
        update.addActionListener(this);
        add(update);

        cancel = new JButton("Cancel");
        styleButton(cancel, 450, 250);
        cancel.addActionListener(this);
        add(cancel);

        setSize(900, 700);
        setLocation(300, 100);
        setVisible(true);
    }

    // Button styling function
    private void styleButton(JButton button, int x, int y) {
        button.setBounds(x, y, 80, 30);
        button.setBackground(new Color(0x4CAF50));  // Green color
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Tahoma", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0x3E8E41)); // Darker green on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0x4CAF50)); // Reset color after hover
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == search) {
            String dropdownQuery = "select * from teacher where empID ='" + cEmpId.getSelectedItem() + "'";
            String empIdQuery = "select * from teacher where empID like '%" + searchEmpIdField.getText() + "%'";
            String nameQuery = "select * from teacher where name like '%" + searchNameField.getText() + "%'";
            
            try {
                Conn c = new Conn();
                ResultSet rs;
                if (!searchNameField.getText().equals("")) {
                    rs = c.s.executeQuery(nameQuery);
                } else if (!searchEmpIdField.getText().equals("")) {
                    rs = c.s.executeQuery(empIdQuery);
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
        } else if (ae.getSource() == add) {
            setVisible(false);
            new AddTeacher();
        } else if (ae.getSource() == update) {
            setVisible(false);
            new UpdateTeacher();  // Open UpdateTeacher window
        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new TeacherDetails(); 
    }
}
