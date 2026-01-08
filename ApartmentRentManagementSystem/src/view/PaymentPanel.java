/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import dao.PaymentDao;
import dao.DBConnection;
import java.awt.Color;
import java.awt.Font;
import model.Payment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import service.NotificationService;

/**
 *
 * @author User-104
 */
public class PaymentPanel extends javax.swing.JPanel {

    private PaymentDao paymentDAO = new PaymentDao();
    /**
     * Creates new form PaymentPanel
     */
    public PaymentPanel() {
        initComponents();
        tenantCombo.addActionListener(e -> {
    String tenantSelected = (String) tenantCombo.getSelectedItem();
    if (tenantSelected != null && !tenantSelected.isEmpty()) {
        int tenantId = Integer.parseInt(tenantSelected.split(" - ")[0]);
        loadApartmentsByTenant(tenantId);
    }
});
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        jTable1MouseClicked(evt);
    }
});


        loadTenants();
        loadAllPayments();
    }
    
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) return; // nothing selected

    // Tenant
    String tenantName = (String) jTable1.getValueAt(selectedRow, 1);
    for (int i = 0; i < tenantCombo.getItemCount(); i++) {
        if (tenantCombo.getItemAt(i).contains(tenantName)) {
            tenantCombo.setSelectedIndex(i);
            break;
        }
    }

    // Apartment
    String apartmentNumber = (String) jTable1.getValueAt(selectedRow, 2);
    for (int i = 0; i < ApartmentCombo.getItemCount(); i++) {
        if (ApartmentCombo.getItemAt(i).contains(apartmentNumber)) {
            ApartmentCombo.setSelectedIndex(i);
            break;
        }
    }

    // Rent type & amount
    rentTypeField.setText((String) jTable1.getValueAt(selectedRow, 3));
    rentAmountField.setText(String.valueOf(jTable1.getValueAt(selectedRow, 4)));

    // Amount paid
    amountPaidTxt.setText(String.valueOf(jTable1.getValueAt(selectedRow, 5)));

    // Date paid
    LocalDate datePaid = (LocalDate) jTable1.getValueAt(selectedRow, 7);
    datePaidChooser.setDate(java.sql.Date.valueOf(datePaid));
}

    //validations
    
    private boolean validatePaymentForm() {
    if (tenantCombo.getSelectedIndex() == -1) {
        System.out.println("Select a tenant!");
        return false;
    }
    if (ApartmentCombo.getSelectedIndex() == -1) {
        System.out.println("Select an apartment!");
        return false;
    }
    if (amountPaidTxt.getText().isEmpty()) {
        System.out.println("Enter amount paid!");
        return false;
    }
    try {
        double amount = Double.parseDouble(amountPaidTxt.getText());
        if (amount <= 0) {
            System.out.println("Amount must be greater than 0!");
            return false;
        }
    } catch (NumberFormatException e) {
        System.out.println("Amount must be numeric!");
        return false;
    }
    if (datePaidChooser.getDate() == null) {
        System.out.println("Select date paid!");
        return false;
    }
    return true;
}


 private void loadTenants() {
    try (Connection conn = DBConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT id, full_name FROM tenant")) {

        DefaultComboBoxModel<String> tenantModel = new DefaultComboBoxModel<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("full_name");
            tenantModel.addElement(id + " - " + name);
        }
        tenantCombo.setModel(tenantModel);

    } catch (SQLException ex) {
        System.out.println("Load Tenants Error: " + ex.getMessage());
    }
}

 private void loadApartmentsByTenant(int tenantId) {
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(
             "SELECT a.id, a.apartment_number, a.rent_type, a.rent_amount " +
             "FROM apartment a JOIN tenant t ON a.id = t.apartment_id " +
             "WHERE t.id = ?")) {

        ps.setInt(1, tenantId);
        ResultSet rs = ps.executeQuery();

        DefaultComboBoxModel<String> apartmentModel = new DefaultComboBoxModel<>();
        if (rs.next()) {
            int aptId = rs.getInt("id");
            String aptNumber = rs.getString("apartment_number");
            String rentType = rs.getString("rent_type");
            double rentAmount = rs.getDouble("rent_amount");

            apartmentModel.addElement(aptId + " - " + aptNumber);
            ApartmentCombo.setModel(apartmentModel);

            // Auto-fill text fields
            rentTypeField.setText(rentType);
            rentAmountField.setText(String.valueOf(rentAmount));
        } else {
            ApartmentCombo.setModel(new DefaultComboBoxModel<>());
            rentTypeField.setText("");
            rentAmountField.setText("");
        }

    } catch (SQLException ex) {
        System.out.println("Load Apartments Error: " + ex.getMessage());
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        ApartmentCombo = new javax.swing.JComboBox<>();
        tenantCombo = new javax.swing.JComboBox<>();
        amountPaidTxt = new javax.swing.JTextField();
        datePaidChooser = new com.toedter.calendar.JDateChooser();
        saveBtn = new javax.swing.JButton();
        refreshBtn = new javax.swing.JButton();
        rentTypeField = new javax.swing.JTextField();
        rentAmountField = new javax.swing.JTextField();
        updateBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        searchTxt = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("Payment Form");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setText("Tenant");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel3.setText("Apartment No");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel4.setText("Rent Type");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setText("Rent Amount");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel6.setText("Amount Paid");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel7.setText("Date Paid");

        ApartmentCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tenantCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        saveBtn.setText("save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        refreshBtn.setText("refresh");
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });

        rentTypeField.setEditable(false);

        rentAmountField.setEditable(false);

        updateBtn.setText("update");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });

        deleteBtn.setText("delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(saveBtn)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(updateBtn)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(datePaidChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                            .addComponent(amountPaidTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(rentAmountField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(rentTypeField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ApartmentCombo, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tenantCombo, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(45, 45, 45))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(deleteBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addComponent(refreshBtn)
                        .addGap(65, 65, 65))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(187, 187, 187)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addGap(52, 52, 52)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tenantCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(ApartmentCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(rentTypeField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(rentAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(amountPaidTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(datePaidChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refreshBtn)
                    .addComponent(saveBtn)
                    .addComponent(updateBtn)
                    .addComponent(deleteBtn))
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Tenant name", "Apartement Number", "Rent type", "Rent Amount ", "Amount Paid", "Payment for duration", "Date Paid", "Next due date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        searchBtn.setText("search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(searchTxt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchBtn))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));
        jPanel4.setForeground(new java.awt.Color(0, 102, 102));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Payment Page");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(0, 102, 102));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
                                      
    try {
        // Tenant
        String tenantSelected = (String) tenantCombo.getSelectedItem();
        if (tenantSelected == null) {
            JOptionPane.showMessageDialog(this, "Please select a tenant.");
            return;
        }
        int tenantId = Integer.parseInt(tenantSelected.split(" - ")[0]);

        // Apartment
        String apartmentSelected = (String) ApartmentCombo.getSelectedItem();
        if (apartmentSelected == null) {
            JOptionPane.showMessageDialog(this, "Please select an apartment.");
            return;
        }
        int apartmentId = Integer.parseInt(apartmentSelected.split(" - ")[0]);

        // Rent type and amount
        String rentType = rentTypeField.getText();
        double rentAmount = Double.parseDouble(rentAmountField.getText());

        // Amount paid
        double amountPaid = Double.parseDouble(amountPaidTxt.getText());
        if (amountPaid <= 0) {
            JOptionPane.showMessageDialog(this, "Amount paid must be positive!");
            return;
        }

        // Date paid
        if (datePaidChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select a payment date.");
            return;
        }
        LocalDate datePaid = datePaidChooser.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        if (datePaid.isAfter(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "Payment date cannot be in the future!");
            return;
        }

        Payment payment = new Payment();
        payment.setTenantId(tenantId);
        payment.setApartmentId(apartmentId);
        payment.setRentType(rentType);
        payment.setRentAmount(rentAmount);
        payment.setAmountPaid(amountPaid);
        payment.setDatePaid(datePaid);

        payment.calculatePaymentDuration();
        payment.calculateNextDueDate();

        if (paymentDAO.insertPayment(payment)) {
            JOptionPane.showMessageDialog(this, "Payment saved successfully!");
            loadAllPayments();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save payment!");
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
      clearForm();
      loadAllPayments();
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed

  int selectedRow = jTable1.getSelectedRow();
    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(this, "Please select a payment to update!");
        return;
    }

    int paymentId = (int) jTable1.getValueAt(selectedRow, 0);

    int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to update this payment?",
            "Confirm Update", JOptionPane.YES_NO_OPTION);

    if (confirm != JOptionPane.YES_OPTION) return;

    try {
        String tenantSelected = (String) tenantCombo.getSelectedItem();
        int tenantId = Integer.parseInt(tenantSelected.split(" - ")[0]);
        String apartmentSelected = (String) ApartmentCombo.getSelectedItem();
        int apartmentId = Integer.parseInt(apartmentSelected.split(" - ")[0]);

        String rentType = rentTypeField.getText();
        double rentAmount = Double.parseDouble(rentAmountField.getText());
        double amountPaid = Double.parseDouble(amountPaidTxt.getText());

        if (amountPaid <= 0) {
            JOptionPane.showMessageDialog(this, "Amount paid must be positive!");
            return;
        }

        LocalDate datePaid = datePaidChooser.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        if (datePaid.isAfter(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "Payment date cannot be in the future!");
            return;
        }

        Payment payment = new Payment();
        payment.setPaymentId(paymentId);
        payment.setTenantId(tenantId);
        payment.setApartmentId(apartmentId);
        payment.setRentType(rentType);
        payment.setRentAmount(rentAmount);
        payment.setAmountPaid(amountPaid);
        payment.setDatePaid(datePaid);
        payment.calculatePaymentDuration();
        payment.calculateNextDueDate();

        if (paymentDAO.updatePayment(payment)) {
            JOptionPane.showMessageDialog(this, "Payment updated successfully!");
            loadAllPayments();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update payment!");
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
     
   int selectedRow = jTable1.getSelectedRow();
    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(this, "Please select a payment to delete!");
        return;
    }

    int paymentId = (int) jTable1.getValueAt(selectedRow, 0);

    int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this payment?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

    if (confirm != JOptionPane.YES_OPTION) return;

    if (paymentDAO.deletePayment(paymentId)) {
        JOptionPane.showMessageDialog(this, "Payment deleted successfully!");
        loadAllPayments();
        clearForm();
    } else {
        JOptionPane.showMessageDialog(this, "Failed to delete payment!");
    }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed

    String keyword = searchTxt.getText().trim();
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0);

    if (keyword.isEmpty()) {
        loadAllPayments();
        return;
    }

    List<Payment> list = new ArrayList<>();

    try {
        // Try numeric search by payment ID
        int id = Integer.parseInt(keyword);
        Payment p = paymentDAO.searchPayment(id);
        if (p != null) list.add(p);
    } catch (NumberFormatException e) {
        // If not numeric, search by tenant name or apartment number
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT p.* FROM payment p " +
                 "JOIN tenant t ON p.tenant_id = t.id " +
                 "JOIN apartment a ON p.apartment_id = a.id " +
                 "WHERE t.full_name LIKE ? OR a.apartment_number LIKE ?"
             )) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new PaymentDao().mapResultSetToPayment(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Search Payment Error: " + ex.getMessage());
        }
    }

    // Fill table
    for (Payment p : list) {
        model.addRow(new Object[]{
            p.getPaymentId(),
            getTenantNameById(p.getTenantId()),
            getApartmentNumberById(p.getApartmentId()),
            p.getRentType(),
            p.getRentAmount(),
            p.getAmountPaid(),
            p.getPaymentForDuration(),
            p.getDatePaid(),
            p.getNextDueDate()
        });
    }
    }//GEN-LAST:event_searchBtnActionPerformed

 private void clearForm() {
    tenantCombo.setSelectedIndex(-1);
    ApartmentCombo.setModel(new DefaultComboBoxModel<>());
    
    rentTypeField.setText("");
    rentAmountField.setText("");
    amountPaidTxt.setText("");
    datePaidChooser.setDate(null);

    loadTenants(); // reload full tenant list
}



    
   // -------------------------------
    // Load all payments into JTable
    // -------------------------------
    private void loadAllPayments() {
        List<Payment> list = paymentDAO.getAllPayments();

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        for (Payment p : list) {
            Object[] row = new Object[]{
                    p.getPaymentId(),
                    getTenantNameById(p.getTenantId()),
                    getApartmentNumberById(p.getApartmentId()),
                    p.getRentType(),
                    p.getRentAmount(),
                    p.getAmountPaid(),
                    p.getPaymentForDuration(),
                    p.getDatePaid(),
                    p.getNextDueDate()
            };
            model.addRow(row);
        }
        //table
        JTableHeader header = jTable1.getTableHeader();
header.setFont(new Font("Segoe UI", Font.BOLD, 16));

header.setDefaultRenderer((table, value, isSelected, hasFocus, row, column) -> {
    JLabel lbl = new JLabel(value.toString());
    lbl.setOpaque(true);
    lbl.setBackground(new Color(0, 102, 102));   // Your background color
    lbl.setForeground(Color.WHITE);               // Text color
    //lbl.setHorizontalAlignment(JLabel.CENTER);    // Center text
    lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
    return lbl;
});
//table
    }
    
     // -------------------------------
    // Helper: Get tenant name by ID
    // -------------------------------
    private String getTenantNameById(int tenantId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT full_name FROM tenant WHERE id = ?")) {

            ps.setInt(1, tenantId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("full_name");

        } catch (SQLException ex) {
            System.out.println("Get Tenant Name Error: " + ex.getMessage());
        }
        return "";
    }

    // -------------------------------
    // Helper: Get apartment number by ID
    // -------------------------------
    private String getApartmentNumberById(int apartmentId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT apartment_number FROM apartment WHERE id = ?")) {

            ps.setInt(1, apartmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("apartment_number");

        } catch (SQLException ex) {
            System.out.println("Get Apartment Number Error: " + ex.getMessage());
        }
        return "";
    }
    
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ApartmentCombo;
    private javax.swing.JTextField amountPaidTxt;
    private com.toedter.calendar.JDateChooser datePaidChooser;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JTextField rentAmountField;
    private javax.swing.JTextField rentTypeField;
    private javax.swing.JButton saveBtn;
    private javax.swing.JButton searchBtn;
    private javax.swing.JTextField searchTxt;
    private javax.swing.JComboBox<String> tenantCombo;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
