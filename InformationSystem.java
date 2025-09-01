import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class InformationSystem extends JFrame {
    private JTextField txtFirstName, txtLastName, txtEmail, txtRegNo, txtCourse;
    private JButton btnInsert, btnUpdate, btnDelete;
    private JTable table;
    private DefaultTableModel model;

    Connection con;
    PreparedStatement pst;

    public InformationSystem() {
        setTitle("Student Information System");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblTitle = new JLabel("Student Information System");
        lblTitle.setBounds(300, 10, 300, 30);
        add(lblTitle);

        addLabels();
        addTextFields();
        addButtons();
        addTable();

        connect();
        loadData();

        setVisible(true);
    }

    private void addLabels() {
        String[] labels = {"firstname", "lastname", "email", "Reg.no", "course"};
        int y = 50;
        for (String label : labels) {
            JLabel lbl = new JLabel(label);
            lbl.setBounds(30, y, 100, 25);
            add(lbl);
            y += 35;
        }
    }

    private void addTextFields() {
        txtFirstName = new JTextField(); txtFirstName.setBounds(130, 50, 150, 25);
        txtLastName = new JTextField(); txtLastName.setBounds(130, 85, 150, 25);
        txtEmail = new JTextField(); txtEmail.setBounds(130, 120, 150, 25);
        txtRegNo = new JTextField(); txtRegNo.setBounds(130, 155, 150, 25);
        txtCourse = new JTextField(); txtCourse.setBounds(130, 190, 150, 25);

        add(txtFirstName);
        add(txtLastName);
        add(txtEmail);
        add(txtRegNo);
        add(txtCourse);
    }

    private void addButtons() {
        btnInsert = new JButton("Insert");
        btnInsert.setBounds(50, 240, 200, 30);
        btnInsert.setBackground(new java.awt.Color(51, 255, 51));
        btnInsert.setFont(new java.awt.Font("Segoe UI", 1, 14));
        add(btnInsert);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(50, 280, 200, 30);
        btnUpdate.setBackground(new java.awt.Color(0, 102, 204));
        btnUpdate.setForeground(java.awt.Color.white);
        add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(50, 320, 200, 30);
        btnDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnDelete.setForeground(java.awt.Color.white);
        add(btnDelete);

        btnInsert.addActionListener(e -> insert());
        btnUpdate.addActionListener(e -> update());
        btnDelete.addActionListener(e -> delete());
    }

    private void addTable() {
        model = new DefaultTableModel(new String[]{"ID", "firstname", "lastname", "email", "Reg.no", "course"}, 0);
        table = new JTable(model);
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(300, 50, 470, 300);
        add(pane);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = table.getSelectedRow();
                txtFirstName.setText(model.getValueAt(i, 1).toString());
                txtLastName.setText(model.getValueAt(i, 2).toString());
                txtEmail.setText(model.getValueAt(i, 3).toString());
                txtRegNo.setText(model.getValueAt(i, 4).toString());
                txtCourse.setText(model.getValueAt(i, 5).toString());
            }
        });
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/studentdb", "root", "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadData() {
        try {
            model.setRowCount(0);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getString("regno"),
                    rs.getString("course")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void insert() {
        try {
            pst = con.prepareStatement("INSERT INTO students (firstname, lastname, email, regno, course) VALUES (?, ?, ?, ?, ?)");
            pst.setString(1, txtFirstName.getText());
            pst.setString(2, txtLastName.getText());
            pst.setString(3, txtEmail.getText());
            pst.setString(4, txtRegNo.getText());
            pst.setString(5, txtCourse.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Record Inserted");
            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void update() {
        int i = table.getSelectedRow();
        if (i == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to update");
            return;
        }
        try {
            int id = Integer.parseInt(model.getValueAt(i, 0).toString());
            pst = con.prepareStatement("UPDATE students SET firstname=?, lastname=?, email=?, regno=?, course=? WHERE id=?");
            pst.setString(1, txtFirstName.getText());
            pst.setString(2, txtLastName.getText());
            pst.setString(3, txtEmail.getText());
            pst.setString(4, txtRegNo.getText());
            pst.setString(5, txtCourse.getText());
            pst.setInt(6, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Record Updated");
            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void delete() {
        int i = table.getSelectedRow();
        if (i == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete");
            return;
        }
        try {
            int id = Integer.parseInt(model.getValueAt(i, 0).toString());
            pst = con.prepareStatement("DELETE FROM students WHERE id=?");
            pst.setInt(1, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Record Deleted");
            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new InformationSystem();
    }
}
