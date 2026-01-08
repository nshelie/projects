/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import model.Student;
import model.User;
import service.StudentService;
import service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.rmi.Naming;
import java.util.List;
import java.io.File;
import java.io.FileWriter;



/**
 *
 * @author elie
 */
public class StudentPage extends javax.swing.JPanel {

    private StudentService studentService;
    private UserService userService;

    /**
     * Creates new form StudentPage
     */
    public StudentPage() {
        initComponents();
        setupTableClickListener();
        loadStudentTable(null);  
        lookupServices();
        
    }
  

     public StudentPage(  UserService userService, StudentService studentService) {
        this.studentService = studentService;
        this.userService = userService;
        initComponents();
        setupTableClickListener();
        loadStudentTable(null);
    }

 

      // 🔹 Connect to RMI server
    private void lookupServices() {
        try {
            studentService = (StudentService) Naming.lookup("rmi://127.0.0.1:3700/student");
            userService = (UserService) Naming.lookup("rmi://127.0.0.1:3700/user");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to connect to server: " + ex.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportTableToCSV(JTable table) {
    JFileChooser chooser = new JFileChooser();
    chooser.setSelectedFile(new File("students_report.csv"));

    if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = chooser.getSelectedFile();

        try (FileWriter fw = new FileWriter(file)) {

            // Write column headers
            for (int i = 0; i < table.getColumnCount(); i++) {
                fw.write(table.getColumnName(i) + ",");
            }
            fw.write("\n");

            // Write table rows
            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    Object value = table.getValueAt(i, j);
                    fw.write((value != null ? value.toString() : "") + ",");
                }
                fw.write("\n");
            }

            JOptionPane.showMessageDialog(this,
                    "Student report exported successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to export report: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}


    // 🔹 Clear input fields
    private void clearFields() {
        fullNametxt.setText("");
        emailtxt.setText("");
        passwordtxt.setText("");
        departmenttxt.setText("");
        searchtxt.setText("");
    }

    // 🔹 Validate inputs
    private boolean validateInput() {
        String fullName = fullNametxt.getText().trim();
        String email = emailtxt.getText().trim();
        String password = new String(passwordtxt.getPassword()).trim();
        String department = departmenttxt.getText().trim();

        if (fullName.isEmpty() || !fullName.matches("[a-zA-Z\\s]+") || fullName.length() > 50) {
            JOptionPane.showMessageDialog(this, "Full Name invalid!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (email.isEmpty() || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            JOptionPane.showMessageDialog(this, "Email invalid!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (password.isEmpty() || password.length() < 8) {
            JOptionPane.showMessageDialog(this, "Password invalid!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (department.isEmpty() || !department.matches("[a-zA-Z\\s]+") || department.length() > 30) {
            JOptionPane.showMessageDialog(this, "Department invalid!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // 🔹 Load table from server
    private void loadStudentTable(String keyword) {
        try {
            DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
            model.setRowCount(0);

            List<Student> students = studentService.retrieveAllStudents(); // can filter later
            for (Student s : students) {
                if (keyword == null || keyword.isEmpty() || s.getFullName().toLowerCase().contains(keyword.toLowerCase())
                        || s.getEmail().toLowerCase().contains(keyword.toLowerCase())
                        || s.getDepartment().toLowerCase().contains(keyword.toLowerCase())) {

                    model.addRow(new Object[]{
                            s.getStudentId(),
                            s.getFullName(),
                            s.getEmail(),
                            s.getPassword(),
                            s.getDepartment()
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Server Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        leftPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        registrationPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        fullNametxt = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        emailtxt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        passwordtxt = new javax.swing.JPasswordField();
        jLabel12 = new javax.swing.JLabel();
        departmenttxt = new javax.swing.JTextField();
        registerBtn = new javax.swing.JButton();
        updateBtn1 = new javax.swing.JButton();
        deleteBtn1 = new javax.swing.JButton();
        refreshBtn = new javax.swing.JButton();
        csvBtn = new javax.swing.JButton();
        excelBtn = new javax.swing.JButton();
        pdfBtn = new javax.swing.JButton();
        tablePanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        studentTable = new javax.swing.JTable();
        searchtxt = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        leftPanel.setBackground(new java.awt.Color(0, 102, 153));
        leftPanel.setPreferredSize(new java.awt.Dimension(250, 370));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/careerStudent6.png"))); // NOI18N

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 250, Short.MAX_VALUE)
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 268, Short.MAX_VALUE))
        );

        add(leftPanel, java.awt.BorderLayout.LINE_START);

        mainPanel.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel1)
                .addContainerGap(789, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel3.setBackground(new java.awt.Color(0, 102, 153));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Student Page");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel8)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        registrationPanel.setBackground(new java.awt.Color(255, 255, 255));
        registrationPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        registrationPanel.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Student Registration Form");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Full Name");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Email");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Password");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Department");

        registerBtn.setBackground(new java.awt.Color(0, 0, 51));
        registerBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        registerBtn.setForeground(new java.awt.Color(255, 255, 255));
        registerBtn.setText("Register");
        registerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerBtnActionPerformed(evt);
            }
        });

        updateBtn1.setBackground(new java.awt.Color(0, 204, 204));
        updateBtn1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        updateBtn1.setForeground(new java.awt.Color(255, 255, 255));
        updateBtn1.setText("Update");
        updateBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtn1ActionPerformed(evt);
            }
        });

        deleteBtn1.setBackground(new java.awt.Color(0, 153, 153));
        deleteBtn1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        deleteBtn1.setForeground(new java.awt.Color(255, 255, 255));
        deleteBtn1.setText("Delete");
        deleteBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtn1ActionPerformed(evt);
            }
        });

        refreshBtn.setBackground(new java.awt.Color(0, 153, 153));
        refreshBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        refreshBtn.setForeground(new java.awt.Color(255, 255, 255));
        refreshBtn.setText("refresh");
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });

        csvBtn.setText("export csv");
        csvBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                csvBtnActionPerformed(evt);
            }
        });

        excelBtn.setText("export excel");
        excelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excelBtnActionPerformed(evt);
            }
        });

        pdfBtn.setText("export pdf");
        pdfBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pdfBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout registrationPanelLayout = new javax.swing.GroupLayout(registrationPanel);
        registrationPanel.setLayout(registrationPanelLayout);
        registrationPanelLayout.setHorizontalGroup(
            registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, registrationPanelLayout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addComponent(registerBtn)
                .addGap(18, 18, 18)
                .addComponent(updateBtn1)
                .addGap(18, 18, 18)
                .addComponent(deleteBtn1)
                .addGap(18, 18, 18)
                .addComponent(refreshBtn)
                .addGap(85, 85, 85))
            .addGroup(registrationPanelLayout.createSequentialGroup()
                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(registrationPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(38, 38, 38)
                        .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(emailtxt, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(fullNametxt)
                            .addComponent(passwordtxt)
                            .addComponent(departmenttxt)))
                    .addGroup(registrationPanelLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(registrationPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(csvBtn)
                        .addGap(39, 39, 39)
                        .addComponent(excelBtn)
                        .addGap(33, 33, 33)
                        .addComponent(pdfBtn)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        registrationPanelLayout.setVerticalGroup(
            registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registrationPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fullNametxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(departmenttxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(43, 43, 43)
                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refreshBtn)
                    .addComponent(deleteBtn1)
                    .addComponent(updateBtn1)
                    .addComponent(registerBtn))
                .addGap(18, 18, 18)
                .addGroup(registrationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(csvBtn)
                    .addComponent(excelBtn)
                    .addComponent(pdfBtn))
                .addGap(165, 165, 165))
        );

        tablePanel.setBackground(new java.awt.Color(255, 255, 255));
        tablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        studentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Full Name", "Email", "password", "Department"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(studentTable);

        searchBtn.setBackground(new java.awt.Color(0, 102, 153));
        searchBtn.setForeground(new java.awt.Color(255, 255, 255));
        searchBtn.setText("Search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tablePanelLayout = new javax.swing.GroupLayout(tablePanel);
        tablePanel.setLayout(tablePanelLayout);
        tablePanelLayout.setHorizontalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tablePanelLayout.createSequentialGroup()
                .addComponent(searchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(searchBtn)
                .addContainerGap(30, Short.MAX_VALUE))
            .addComponent(jScrollPane2)
        );
        tablePanelLayout.setVerticalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tablePanelLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(88, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 102, 153));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(registrationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(registrationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 436, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(tablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 24, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(203, 203, 203))
        );

        jPanel7.add(jPanel2, java.awt.BorderLayout.CENTER);

        mainPanel.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(mainPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void registerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerBtnActionPerformed
    if (!validateInput()) return;

        String fullName = fullNametxt.getText().trim();
        String email = emailtxt.getText().trim();
        String password = new String(passwordtxt.getPassword()).trim();
        String department = departmenttxt.getText().trim();

        try {
            if (userService.isEmailTaken(email)) {
                JOptionPane.showMessageDialog(this, "Email already registered!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

           User user = new User();
user.setFullName(fullName);
user.setEmail(email);
user.setPassword(password);
user.setRole("STUDENT");
user.setDepartment(department);

User savedUser = userService.saveUser(user); // returns User object with ID
if (savedUser == null) {
    JOptionPane.showMessageDialog(this, "Failed to create user account!", "Error", JOptionPane.ERROR_MESSAGE);
    return;
}
int newUserId = savedUser.getUserId();


            Student s = new Student();
            s.setFullName(fullName);
            s.setEmail(email);
            s.setPassword(password);
            s.setDepartment(department);
            s.setUserId(newUserId);

            Student savedStudent = studentService.saveStudent(s);

            if (savedStudent != null) {
                JOptionPane.showMessageDialog(this, "Student registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadStudentTable(null);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to register student.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Server Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_registerBtnActionPerformed

    private void updateBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtn1ActionPerformed

        int row = studentTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a student to update", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateInput()) return;

        try {
            int studentId = (int) studentTable.getValueAt(row, 0);

            // Preserve the userId link; the table doesn't show it.
            Student existing = studentService.findStudentById(studentId);
            if (existing == null) {
                JOptionPane.showMessageDialog(this, "Student not found on server.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student s = new Student();
            s.setStudentId(studentId);
            s.setFullName(fullNametxt.getText().trim());
            s.setEmail(emailtxt.getText().trim());
            s.setPassword(new String(passwordtxt.getPassword()).trim());
            s.setDepartment(departmenttxt.getText().trim());
            s.setUserId(existing.getUserId());

            Student updated = studentService.updateStudent(s);

            if (updated != null) {
                JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadStudentTable(null);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Server Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_updateBtn1ActionPerformed

    private void deleteBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtn1ActionPerformed
  int row = studentTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a student to delete", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int studentId = (int) studentTable.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            boolean success = studentService.deleteStudent(studentId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadStudentTable(null);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Server Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deleteBtn1ActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        clearFields();
        loadStudentTable(null);
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        loadStudentTable(searchtxt.getText().trim());
    }//GEN-LAST:event_searchBtnActionPerformed

    private void csvBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_csvBtnActionPerformed
          if (studentTable.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this,
                "No student data to export",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
        return;
    }
    exportTableToCSV(studentTable);
    }//GEN-LAST:event_csvBtnActionPerformed

    private void excelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excelBtnActionPerformed
          JOptionPane.showMessageDialog(this,
            "Excel export uses CSV format. Please use 'Export CSV' and open it with Excel.",
            "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_excelBtnActionPerformed

    private void pdfBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pdfBtnActionPerformed
         JOptionPane.showMessageDialog(this,
            "PDF export module is available and can be enabled using iText library.",
            "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_pdfBtnActionPerformed

    
    // 🔹 Populate fields when table row clicked
    private void setupTableClickListener() {
        studentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = studentTable.getSelectedRow();
                if (row >= 0) {
                    fullNametxt.setText(studentTable.getValueAt(row, 1).toString());
                    emailtxt.setText(studentTable.getValueAt(row, 2).toString());
                    passwordtxt.setText(studentTable.getValueAt(row, 3).toString());
                    departmenttxt.setText(studentTable.getValueAt(row, 4).toString());
                }
            }
        });
}



    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton csvBtn;
    private javax.swing.JButton deleteBtn1;
    private javax.swing.JTextField departmenttxt;
    private javax.swing.JTextField emailtxt;
    private javax.swing.JButton excelBtn;
    private javax.swing.JTextField fullNametxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPasswordField passwordtxt;
    private javax.swing.JButton pdfBtn;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JButton registerBtn;
    private javax.swing.JPanel registrationPanel;
    private javax.swing.JButton searchBtn;
    private javax.swing.JTextField searchtxt;
    private javax.swing.JTable studentTable;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JButton updateBtn1;
    // End of variables declaration//GEN-END:variables
}
