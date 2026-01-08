/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;
import dao.TenantDao;
import dao.ApartmentDao;
import java.awt.Color;
import java.awt.Font;
import model.Tenant;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.table.JTableHeader;
import model.Apartment;




/**
 *
 * @author User-104
 */
public class TenantPanel extends javax.swing.JPanel {

    int selectedId = -1;
    TenantDao tenantDao = new TenantDao();
    ApartmentDao apartmentDao = new ApartmentDao();

    /**
     * Creates new form TenantPanel
     */
    public TenantPanel() {
        initComponents();

        //jTable listener
        // jTable listener
jTable1.getSelectionModel().addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
        int row = jTable1.getSelectedRow();
        selectedId = (int) jTable1.getValueAt(row, 0);

        fullNameTxt.setText(jTable1.getValueAt(row, 1).toString());
        genderCombo.setSelectedItem(jTable1.getValueAt(row, 2).toString());
        phoneTxt.setText(jTable1.getValueAt(row, 3).toString());

        try {
            java.util.Date d = new SimpleDateFormat("yyyy-MM-dd")
                    .parse(jTable1.getValueAt(row, 4).toString());
            moveInDateChooser.setDate(d);
        } catch (Exception ex) {
            moveInDateChooser.setDate(null);
        }

        // ✅ Refresh apartment combo and select current tenant apartment
        loadApartmentNumbers();

        nationalIdTxt.setText(jTable1.getValueAt(row, 7).toString());
    }
});
    loadApartmentNumbers();
        loadTenantTable();
        
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
    }

    
private void loadApartmentNumbers() {
    apartmentCombo.removeAllItems();

    // 1️⃣ Add all available apartments
    List<Apartment> available = apartmentDao.getAllAvailableApartments();
    for (Apartment a : available) {
        apartmentCombo.addItem(a.getId() + " - " + a.getApartmentNumber());
    }

    // 2️⃣ Include current tenant apartment if editing
    if (selectedId != -1) {
        Tenant t = tenantDao.getTenantById(selectedId);
        if (t != null && t.getApartmentNumber() != null) {
            String current = t.getApartmentId() + " - " + t.getApartmentNumber();

            // Check if already in combo
            boolean exists = false;
            for (int i = 0; i < apartmentCombo.getItemCount(); i++) {
                if (apartmentCombo.getItemAt(i).equals(current)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                apartmentCombo.addItem(current); // ✅ now guaranteed to exist
            }

            // Select this apartment
            apartmentCombo.setSelectedItem(current);
        }
    }
}




    
private void loadTenantTable() {
    ArrayList<Tenant> list = tenantDao.getAllTenants();
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0);

    for (Tenant t : list) {
        model.addRow(new Object[]{
            t.getId(),
            t.getFullName(),
            t.getGender(),
            t.getPhone(),
            t.getMoveInDate(),
            t.getApartmentNumber(), // visible to user
            t.getApartmentId(),     // hidden column, actual ID
            t.getNationalId()
        });
    }

    // Hide apartmentId column (index 6)
    jTable1.getColumnModel().getColumn(6).setMinWidth(0);
    jTable1.getColumnModel().getColumn(6).setMaxWidth(0);
    jTable1.getColumnModel().getColumn(6).setWidth(0);
}



    // VALIDATION METHODS
 private boolean isValidFullName(String name) {
    return name != null && name.trim().length() >= 3 && name.matches("[a-zA-Z ]+");
}


private boolean isValidPhone(String phone) {
    return phone.matches("\\d{10}"); // 10 digits only
}

private boolean isValidNationalId(String nid) {
    return nid.matches("\\d{16}"); // 16 digits only
}

private boolean isApartmentSelected() {
    return apartmentCombo.getSelectedItem() != null;
}

private void clearFields() {
    fullNameTxt.setText("");
    phoneTxt.setText("");
    nationalIdTxt.setText("");
    moveInDateChooser.setDate(null);
    genderCombo.setSelectedIndex(0);
    
    // Refresh apartment combo with only available apartments
    apartmentCombo.removeAllItems();
    List<Apartment> available = apartmentDao.getAllAvailableApartments();
    for (Apartment a : available) {
        apartmentCombo.addItem(a.getId() + " - " + a.getApartmentNumber());
    }
    
    selectedId = -1;
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
        fullNameTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        genderCombo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        phoneTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        apartmentCombo = new javax.swing.JComboBox<>();
        addBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        refreshBtn = new javax.swing.JButton();
        moveInDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        nationalIdTxt = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        searchTxt = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Tenant Form");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setText("Full name");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel3.setText("Gender");

        genderCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female", "other" }));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel4.setText("Phone");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setText("Move in Date");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel6.setText("Apartment No");

        apartmentCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        addBtn.setBackground(new java.awt.Color(0, 153, 153));
        addBtn.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        addBtn.setForeground(new java.awt.Color(255, 255, 255));
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

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel8.setText("National ID");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel8))
                        .addGap(62, 62, 62)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fullNameTxt)
                            .addComponent(genderCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(phoneTxt)
                            .addComponent(nationalIdTxt)
                            .addComponent(moveInDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(apartmentCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(addBtn)
                        .addGap(26, 26, 26)
                        .addComponent(updateBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                        .addComponent(deleteBtn)
                        .addGap(30, 30, 30)
                        .addComponent(refreshBtn)))
                .addGap(44, 44, 44))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(fullNameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(genderCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(phoneTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nationalIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(moveInDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel5)))
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(apartmentCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addBtn)
                    .addComponent(updateBtn)
                    .addComponent(deleteBtn)
                    .addComponent(refreshBtn))
                .addGap(71, 71, 71))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        searchBtn.setText("search");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Full name", "Gender", "Phone", "Move in Date", "Apartment No", "Apartment ID", "National ID"
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchTxt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchBtn)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchBtn)
                    .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 153, 153));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Tenant Page");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(0, 153, 153));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 91, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
                                      
    try {
        String fullName = fullNameTxt.getText().trim();
        String phone = phoneTxt.getText().trim();
        String nid = nationalIdTxt.getText().trim();
        String gender = genderCombo.getSelectedItem().toString();

        if (!isValidFullName(fullName)) {
            JOptionPane.showMessageDialog(this, "Full name must be at least 3 letters and contain only letters!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, "Phone must be exactly 10 digits!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!isValidNationalId(nid)) {
            JOptionPane.showMessageDialog(this, "National ID must be exactly 16 digits!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (moveInDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select a move-in date!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!isApartmentSelected()) {
            JOptionPane.showMessageDialog(this, "Please select an apartment!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedApartment = apartmentCombo.getSelectedItem().toString();
        int apartmentId = Integer.parseInt(selectedApartment.split(" - ")[0]);

        Tenant t = new Tenant();
        t.setFullName(fullName);
        t.setGender(gender);
        t.setPhone(phone);
        t.setNationalId(nid);
        t.setMoveInDate(new SimpleDateFormat("yyyy-MM-dd").format(moveInDateChooser.getDate()));
        t.setApartmentId(apartmentId);

        if (tenantDao.insertTenant(t)) {
            JOptionPane.showMessageDialog(this, "Tenant added successfully!");
            clearFields();
            loadTenantTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add tenant. Apartment might be occupied.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_addBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
                                                                                
    if (selectedId == -1) {
        JOptionPane.showMessageDialog(this, "Please select a tenant from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        String fullName = fullNameTxt.getText().trim();
        String phone = phoneTxt.getText().trim();
        String nid = nationalIdTxt.getText().trim();
        String gender = genderCombo.getSelectedItem().toString();

        if (!isValidFullName(fullName)) {
            JOptionPane.showMessageDialog(this, "Full name must be at least 3 letters and contain only letters!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, "Phone must be exactly 10 digits!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!isValidNationalId(nid)) {
            JOptionPane.showMessageDialog(this, "National ID must be exactly 16 digits!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (moveInDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select a move-in date!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!isApartmentSelected()) {
            JOptionPane.showMessageDialog(this, "Please select an apartment!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedApartment = apartmentCombo.getSelectedItem().toString();
        int newApartmentId = Integer.parseInt(selectedApartment.split(" - ")[0]);

        int oldApartmentId = tenantDao.getTenantById(selectedId).getApartmentId();

        Tenant t = new Tenant();
        t.setId(selectedId);
        t.setFullName(fullName);
        t.setGender(gender);
        t.setPhone(phone);
        t.setNationalId(nid);
        t.setMoveInDate(new SimpleDateFormat("yyyy-MM-dd").format(moveInDateChooser.getDate()));
        t.setApartmentId(newApartmentId);

        if (tenantDao.updateTenant(t, oldApartmentId)) {
            JOptionPane.showMessageDialog(this, "Tenant updated successfully!");
            clearFields();
            loadTenantTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update tenant. Apartment might be occupied.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_updateBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
                                           
    if (selectedId == -1) {
        JOptionPane.showMessageDialog(this, "Please select a tenant to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this tenant?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) return;

    try {
        int apartmentId = tenantDao.getTenantById(selectedId).getApartmentId();

        if (tenantDao.deleteTenant(selectedId, apartmentId)) {
            JOptionPane.showMessageDialog(this, "Tenant deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadTenantTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete tenant.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
                                      
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
       loadTenantTable();
       clearFields();
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
                                             
    String keyword = searchTxt.getText().trim();
    ArrayList<Tenant> list = tenantDao.searchTenants(keyword);

    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0);

    for (Tenant t : list) {
        model.addRow(new Object[]{
            t.getId(),
            t.getFullName(),
            t.getGender(),
            t.getPhone(),
            t.getMoveInDate(),
            t.getApartmentNumber(), // show apartment number
            t.getNationalId()
        });
    }             
    }//GEN-LAST:event_searchBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JComboBox<String> apartmentCombo;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JTextField fullNameTxt;
    private javax.swing.JComboBox<String> genderCombo;
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
    private com.toedter.calendar.JDateChooser moveInDateChooser;
    private javax.swing.JTextField nationalIdTxt;
    private javax.swing.JTextField phoneTxt;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JButton searchBtn;
    private javax.swing.JTextField searchTxt;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
