/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import dao.ApartmentDao;
import dao.DBConnection;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.PendingMaintenance;
import dao.PendingMaintenanceDAO;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author User-104
 */
public class MaintenancePanel extends javax.swing.JPanel {
  
    private PendingMaintenanceDAO maintenanceDao = new PendingMaintenanceDAO();

    /**
     * Creates new form MaintenancePanel
     */
    public MaintenancePanel() {
        initComponents();
        loadMaintenanceTable();
        loadApartmentCombo();
        //table
        JTableHeader header = jTable1.getTableHeader();
header.setFont(new Font("Segoe UI", Font.BOLD, 16));

header.setDefaultRenderer((table, value, isSelected, hasFocus, row, column) -> {
    JLabel lbl = new JLabel(value.toString());
    lbl.setOpaque(true);
    lbl.setBackground(new Color(0, 102, 102));   // Your background color
    lbl.setForeground(Color.WHITE);               // Text color
    //lbl.setHorizontalAlignment(JLabel.CENTER);    // Center text
    lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
    return lbl;
});
//table

 // **Row click listener**
jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                int workId = (int) jTable1.getValueAt(selectedRow, 0);
                String apartmentNumber = (String) jTable1.getValueAt(selectedRow, 1);
                String description = (String) jTable1.getValueAt(selectedRow, 2);
                String vendor = (String) jTable1.getValueAt(selectedRow, 3);
                double estimatedCost = (double) jTable1.getValueAt(selectedRow, 4);
                // Convert LocalDate to java.util.Date
                LocalDate scheduledDateLD = (LocalDate) jTable1.getValueAt(selectedRow, 5);
                java.util.Date scheduledDate = java.util.Date.from(
                        scheduledDateLD.atStartOfDay(ZoneId.systemDefault()).toInstant()
                );

                String status = (String) jTable1.getValueAt(selectedRow, 6);

                // Fill form fields
                descriptionTxt.setText(description);
                vendorTxt.setText(vendor);
                estimatedCostTxt.setText(String.valueOf(estimatedCost));
                scheduledtDateChooser.setDate(scheduledDate);
                statusCombo.setSelectedItem(status);

                // Select apartment in combo
                for (int i = 0; i < apartmentCombo.getItemCount(); i++) {
                    if (apartmentCombo.getItemAt(i).contains(apartmentNumber)) {
                        apartmentCombo.setSelectedIndex(i);
                        break;
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error filling form: " + ex.getMessage());
            }
        }
    }
});

      
    }

    private boolean validateMaintenanceForm() {
    String description = descriptionTxt.getText().trim();
    String vendor = vendorTxt.getText().trim();
    String estimatedCostStr = estimatedCostTxt.getText().trim();
    java.util.Date scheduledDate = scheduledtDateChooser.getDate();

    if (description.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Description cannot be empty!");
        return false;
    }
    if (vendor.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vendor cannot be empty!");
        return false;
    }
    double estimatedCost = 0;
    try {
        estimatedCost = Double.parseDouble(estimatedCostStr);
        if (estimatedCost <= 0) {
            JOptionPane.showMessageDialog(this, "Estimated cost must be positive!");
            return false;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid estimated cost!");
        return false;
    }
    if (scheduledDate == null) {
        JOptionPane.showMessageDialog(this, "Select a scheduled date!");
        return false;
    }
    LocalDate scheduledLocal = scheduledDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    if (scheduledLocal.isBefore(LocalDate.now())) {
        JOptionPane.showMessageDialog(this, "Scheduled date cannot be in the past!");
        return false;
    }
    return true;
}

    
private void loadMaintenanceTable() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); // clear existing rows

    try {
        for (PendingMaintenance m : maintenanceDao.getAllMaintenance()) {
            // Get apartment number using DAO
            String apartmentNumber = maintenanceDao.getApartmentNumberById(m.getApartmentId());

            Object[] row = new Object[] {
                m.getWorkId(),                // Work ID
                apartmentNumber,          // Apartment Number
                m.getDescription(),
                m.getVendor(),
                m.getEstimatedCost(),
                m.getScheduledDate(),
                m.getStatus(),
                m.getCreatedAt()
            };
            model.addRow(row);
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error loading maintenance table: " + ex.getMessage());
        ex.printStackTrace();
    }
}
   
  private void clearForm() {
    loadApartmentCombo();   // 🔥 Refresh updated apartment list

    apartmentCombo.setSelectedIndex(0);
    descriptionTxt.setText("");
    vendorTxt.setText("");
    estimatedCostTxt.setText("");
    scheduledtDateChooser.setDate(null);
    statusCombo.setSelectedIndex(0);
}



// In your MaintenancePanel
private void loadApartmentCombo() {
    apartmentCombo.removeAllItems();
    ApartmentDao apartmentDao = new ApartmentDao();
    apartmentDao.getAllApartments().forEach(a -> {
        apartmentCombo.addItem(a.getId() + " - " + a.getApartmentNumber());
    });
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
        scheduledtDateChooser = new com.toedter.calendar.JDateChooser();
        estimatedCostTxt = new javax.swing.JTextField();
        statusCombo = new javax.swing.JComboBox<>();
        apartmentCombo = new javax.swing.JComboBox<>();
        addBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        refreshBtn = new javax.swing.JButton();
        descriptionTxt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        vendorTxt = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        searchTxt = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("Maintenance Form");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setText("Apartment");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel3.setText("Description");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel4.setText("status");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setText("Estimated cost");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel6.setText("Sheduled Date");

        statusCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pending", "In-Progress", "completed" }));

        apartmentCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        addBtn.setText("add");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

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

        refreshBtn.setText("refresh");
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel9.setText("Vendor");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(statusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(scheduledtDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                        .addComponent(estimatedCostTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(apartmentCombo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(descriptionTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vendorTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(56, 56, 56))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(addBtn)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(updateBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteBtn)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refreshBtn)
                        .addGap(42, 42, 42))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(124, 124, 124)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(apartmentCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(descriptionTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(vendorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(estimatedCostTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addComponent(jLabel6))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(scheduledtDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(43, 43, 43)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(statusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(144, 156, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(refreshBtn)
                            .addComponent(updateBtn)
                            .addComponent(deleteBtn)
                            .addComponent(addBtn))
                        .addGap(58, 58, 58))))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Work ID", "Apartment number", "Description", "Vendor", "Estimated cost", "Scheduled Date", "Status", "Creaated At"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
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
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 985, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(searchTxt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchBtn)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Maintenance Page");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel7)
                .addContainerGap(40, Short.MAX_VALUE))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
                                     
    if (!validateMaintenanceForm()) return;

    try {
        int apartmentId = Integer.parseInt(apartmentCombo.getSelectedItem().toString().split(" - ")[0]);
        String description = descriptionTxt.getText().trim();
        String vendor = vendorTxt.getText().trim();
        double estimatedCost = Double.parseDouble(estimatedCostTxt.getText().trim());
        LocalDate scheduledDate = scheduledtDateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String status = statusCombo.getSelectedItem().toString();

        PendingMaintenance m = new PendingMaintenance(apartmentId, description, vendor, estimatedCost, scheduledDate, status);

        if (maintenanceDao.insertMaintenance(m)) {
            JOptionPane.showMessageDialog(this, "Maintenance record added successfully!");
            loadMaintenanceTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add maintenance record!");
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
    }//GEN-LAST:event_addBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
                                         
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(this, "Select a record to update.");
        return;
    }

    if (!validateMaintenanceForm()) return;

    int maintenanceId = (int) jTable1.getValueAt(selectedRow, 0);
    int apartmentId = Integer.parseInt(apartmentCombo.getSelectedItem().toString().split(" - ")[0]);
    String description = descriptionTxt.getText().trim();
    String vendor = vendorTxt.getText().trim();
    double estimatedCost = Double.parseDouble(estimatedCostTxt.getText().trim());
    LocalDate scheduledDate = scheduledtDateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    String status = statusCombo.getSelectedItem().toString();

    PendingMaintenance m = new PendingMaintenance(apartmentId, description, vendor, estimatedCost, scheduledDate, status);
    m.setWorkId(maintenanceId);

    if (JOptionPane.showConfirmDialog(this, "Are you sure to update this record?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        if (maintenanceDao.updateMaintenance(m)) {
            JOptionPane.showMessageDialog(this, "Maintenance record updated!");
            loadMaintenanceTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update maintenance record!");
        }
    }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
                                          
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow >= 0) {
        int maintenanceId = (int) jTable1.getValueAt(selectedRow, 0);
        if (JOptionPane.showConfirmDialog(this, "Are you sure to delete?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (maintenanceDao.deleteMaintenance(maintenanceId)) {
                JOptionPane.showMessageDialog(this, "Record deleted!");
                loadMaintenanceTable();
                clearForm();
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Select a record to delete.");
    }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
       loadMaintenanceTable();
       clearForm();
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
                                                
    String keyword = searchTxt.getText().trim();
    if (keyword.isEmpty()) {
        loadMaintenanceTable(); // If search is empty, load all
        return;
    }

    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); // Clear table

    List<PendingMaintenance> list = new ArrayList<>();

    try {
        // Try to search by Work ID if keyword is numeric
        int id = Integer.parseInt(keyword);
        PendingMaintenance m = maintenanceDao.searchMaintenanceById(id);
        if (m != null) list.add(m);
    } catch (NumberFormatException e) {
        // Not numeric, search by apartment number, vendor, or description
        list = maintenanceDao.searchMaintenanceByKeyword(keyword);
    }

    if (list.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No maintenance records found for: " + keyword);
    }

    for (PendingMaintenance m : list) {
        String apartmentNumber = maintenanceDao.getApartmentNumberById(m.getApartmentId());
        model.addRow(new Object[]{
            m.getWorkId(),
            apartmentNumber,
            m.getDescription(),
            m.getVendor(),
            m.getEstimatedCost(),
            m.getScheduledDate(),
            m.getStatus(),
            m.getCreatedAt()
        });
    }
    }//GEN-LAST:event_searchBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JComboBox<String> apartmentCombo;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JTextField descriptionTxt;
    private javax.swing.JTextField estimatedCostTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton refreshBtn;
    private com.toedter.calendar.JDateChooser scheduledtDateChooser;
    private javax.swing.JButton searchBtn;
    private javax.swing.JTextField searchTxt;
    private javax.swing.JComboBox<String> statusCombo;
    private javax.swing.JButton updateBtn;
    private javax.swing.JTextField vendorTxt;
    // End of variables declaration//GEN-END:variables
}
