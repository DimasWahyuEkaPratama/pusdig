/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pusdigg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dppra
 */
public class user extends javax.swing.JPanel {

    /**
     * Creates new form user
     */
     Connection conn = null;
    ResultSet rs =null;
    PreparedStatement pst = null;
    
    private int currentUserId = 1;
    private DefaultTableModel model;
    private String sql;
    
    public user() {
        initComponents();
      
        conn = koneksi.koneksi.koneksiDB();

        model = new DefaultTableModel();
        table.setModel(model);

        // Tambahkan kolom tabel
        model.addColumn("ID");
        model.addColumn("Username");
        model.addColumn("Password");
        model.addColumn("Nama Lengkap");
        model.addColumn("Role");
        model.addColumn("Telp");
        model.addColumn("Status");
        model.addColumn("Alamat");  
        getData();
    }
    
    void getData() {
    model.setRowCount(0);

    try {
        String sql = "SELECT user_id, username, password, fullname, role, telp, status, alamat FROM user ORDER BY user_id DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Object[] obj = new Object[8];
                
                obj[0] = rs.getInt("user_id");
                obj[1] = rs.getString("username");
                obj[2] = rs.getString("password");
                obj[3] = rs.getString("fullname");
                obj[4] = rs.getString("role");
                obj[5] = rs.getString("telp");
                obj[6] = rs.getString("status");
                obj[7] = rs.getString("alamat");
                
                model.addRow(obj);
            }
            
            // Sembunyikan kolom ID
            table.getColumnModel().getColumn(0).setMinWidth(0);
            table.getColumnModel().getColumn(0).setMaxWidth(0);
            table.getColumnModel().getColumn(0).setWidth(0);
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error getData: " + e.getMessage());
        }
    }

    void pilihData() {
        int i = table.getSelectedRow();
        
        if (i == -1) {
            JOptionPane.showMessageDialog(null, "Pilih data terlebih dahulu!");
            return;
        }

        txt_username.setText(model.getValueAt(i, 1).toString());
        txt_password.setText(model.getValueAt(i, 2).toString());
        txt_namalengkap.setText(model.getValueAt(i, 3).toString());
        cmb_role.setSelectedItem(model.getValueAt(i, 4).toString());
        txt_telp.setText(model.getValueAt(i, 5).toString());
        cmb_status.setSelectedItem(model.getValueAt(i, 6).toString());
        jTextArea1.setText(model.getValueAt(i, 7).toString());
    }

    void bersih() {
        txt_username.setText("");
        txt_password.setText("");
        txt_namalengkap.setText("");
        txt_telp.setText("");
        jTextArea1.setText("");

        cmb_role.setSelectedIndex(0);
        cmb_status.setSelectedIndex(0);

        table.clearSelection();
    }

    // ============================================
    // BUTTON SIMPAN
    // ============================================
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        // Validasi input kosong
        if (txt_username.getText().trim().isEmpty() || 
            txt_password.getText().trim().isEmpty() || 
            txt_namalengkap.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username, Password, dan Nama Lengkap harus diisi!");
            return;
        }
        
        // Validasi password maksimal 8 karakter
        if (txt_password.getText().length() > 8) {
            JOptionPane.showMessageDialog(null, "Password maksimal 8 karakter!");
            return;
        }
        
        try {
            String sql = "INSERT INTO user (username, password, fullname, role, telp, status, alamat, created_by, created_at) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURDATE())";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txt_username.getText().trim());
            pst.setString(2, txt_password.getText());
            pst.setString(3, txt_namalengkap.getText().trim());
            pst.setString(4, (String) cmb_role.getSelectedItem());
            pst.setString(5, txt_telp.getText().trim());
            pst.setString(6, (String) cmb_status.getSelectedItem());
            pst.setString(7, jTextArea1.getText().trim());
            pst.setInt(8, currentUserId); // user yang membuat
            
            int result = pst.executeUpdate();
            
            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
                bersih();
                getData();
            }
            
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(null, "Username sudah digunakan!");
            } else if (e.getMessage().contains("Data too long")) {
                JOptionPane.showMessageDialog(null, "Password terlalu panjang! Maksimal 8 karakter.");
            } else {
                JOptionPane.showMessageDialog(null, "Error Simpan: " + e.getMessage());
            }
        }
    }

    // ============================================
    // BUTTON UPDATE
    // ============================================
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        int i = table.getSelectedRow();
        
        if (i == -1) {
            JOptionPane.showMessageDialog(null, "Pilih data yang ingin diupdate!");
            return;
        }
        
        // Validasi input kosong
        if (txt_username.getText().trim().isEmpty() || 
            txt_password.getText().trim().isEmpty() || 
            txt_namalengkap.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username, Password, dan Nama Lengkap harus diisi!");
            return;
        }
        
        // Validasi password maksimal 8 karakter
        if (txt_password.getText().length() > 8) {
            JOptionPane.showMessageDialog(null, "Password maksimal 8 karakter!");
            return;
        }
        
        int id = Integer.parseInt(model.getValueAt(i, 0).toString());

        try {
            String sql = "UPDATE user SET username=?, password=?, fullname=?, role=?, telp=?, status=?, alamat=?, " +
                         "update_by=?, update_at=CURDATE() WHERE user_id=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txt_username.getText().trim());
            pst.setString(2, txt_password.getText());
            pst.setString(3, txt_namalengkap.getText().trim());
            pst.setString(4, (String) cmb_role.getSelectedItem());
            pst.setString(5, txt_telp.getText().trim());
            pst.setString(6, (String) cmb_status.getSelectedItem());
            pst.setString(7, jTextArea1.getText().trim());
            pst.setInt(8, currentUserId); // user yang mengupdate
            pst.setInt(9, id);
            
            int result = pst.executeUpdate();
            
            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");
                bersih();
                getData();
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(null, "Username sudah digunakan!");
            } else if (e.getMessage().contains("Data too long")) {
                JOptionPane.showMessageDialog(null, "Password terlalu panjang! Maksimal 8 karakter.");
            } else {
                JOptionPane.showMessageDialog(null, "Error Update: " + e.getMessage());
            }
        }
    }

    // ============================================
    // BUTTON REFRESH
    // ============================================
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        bersih();
        getData();
        jTextField5.setText(""); // Clear search box
        JOptionPane.showMessageDialog(null, "Data berhasil di-refresh!");
    }

    // ============================================
    // BUTTON DELETE
    // ============================================
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        int i = table.getSelectedRow();
        
        if (i == -1) {
            JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus!");
            return;
        }
        
        int id = Integer.parseInt(model.getValueAt(i, 0).toString());
        String username = model.getValueAt(i, 1).toString();
         int currentUserId = 0;
        
        // Cegah hapus user sendiri (opsional)
        if (id == currentUserId) {
            JOptionPane.showMessageDialog(null, "Tidak dapat menghapus akun Anda sendiri!");
            return;
        }
        
        // Konfirmasi hapus
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Apakah Anda yakin ingin menghapus user '" + username + "'?\n" +
            "PERINGATAN: Semua data peminjaman terkait user ini akan ikut terhapus!",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM user WHERE user_id=?";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, id);
                
                int result = pst.executeUpdate();
                
                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                    bersih();
                    getData();
                }
                
            } catch (SQLException e) {
                if (e.getMessage().contains("foreign key constraint")) {
                    JOptionPane.showMessageDialog(null, 
                        "Tidak dapat menghapus! User ini masih memiliki data peminjaman aktif.\n" +
                        "Hapus data peminjaman terlebih dahulu.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error Delete: " + e.getMessage());
                }
            }
        }
    }

    // ============================================
    // BUTTON CARI
    // ============================================
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        String keyword = jTextField5.getText().trim();
        
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Masukkan kata kunci pencarian!");
            jTextField5.requestFocus();
            return;
        }
        
        model.setRowCount(0);
        
        try {
            String sql = "SELECT user_id, username, password, fullname, role, telp, status, alamat FROM user WHERE " +
                         "username LIKE ? OR " +
                         "fullname LIKE ? OR " +
                         "telp LIKE ? OR " +
                         "alamat LIKE ? OR " +
                         "role LIKE ? OR " +
                         "status LIKE ? " +
                         "ORDER BY user_id DESC";
            pst = conn.prepareStatement(sql);
            
            String searchPattern = "%" + keyword + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            pst.setString(3, searchPattern);
            pst.setString(4, searchPattern);
            pst.setString(5, searchPattern);
            pst.setString(6, searchPattern);
            
            rs = pst.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                Object[] obj = new Object[8];
                
                obj[0] = rs.getInt("user_id");
                obj[1] = rs.getString("username");
                obj[2] = rs.getString("password");
                obj[3] = rs.getString("fullname");
                obj[4] = rs.getString("role");
                obj[5] = rs.getString("telp");
                obj[6] = rs.getString("status");
                obj[7] = rs.getString("alamat");
                
                model.addRow(obj);
                count++;
            }
            
            if (count == 0) {
                JOptionPane.showMessageDialog(null, "Data tidak ditemukan!");
                getData(); // Tampilkan semua data kembali
                jTextField5.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Ditemukan " + count + " data");
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error Cari: " + e.getMessage());
        }
    }

    // ============================================
    // MOUSE CLICK TABLE
    // ============================================
    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        pilihData();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txt_username = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_password = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_namalengkap = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmb_role = new javax.swing.JComboBox<String>();
        jLabel5 = new javax.swing.JLabel();
        txt_telp = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cmb_status = new javax.swing.JComboBox<String>();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        tombolsimpan = new javax.swing.JButton();
        tombolupdate = new javax.swing.JButton();
        tombolrefresh = new javax.swing.JButton();
        tomboldelete = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jTextField5 = new javax.swing.JTextField();
        cari = new javax.swing.JButton();

        jLabel1.setText("username");

        jLabel2.setText("password");

        jLabel3.setText("nama lengkap");

        jLabel4.setText("role");

        cmb_role.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "admin", "user" }));

        jLabel5.setText("telp");

        jLabel6.setText("status");

        cmb_status.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "guru", "siswa", "lainnya" }));

        jLabel7.setText("alamat");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        tombolsimpan.setText("Simpan");
        tombolsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolsimpanActionPerformed(evt);
            }
        });

        tombolupdate.setText("Update");
        tombolupdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolupdateActionPerformed(evt);
            }
        });

        tombolrefresh.setText("Refresh");
        tombolrefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolrefreshActionPerformed(evt);
            }
        });

        tomboldelete.setText("Delete");
        tomboldelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tomboldeleteActionPerformed(evt);
            }
        });

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table);

        cari.setText("CARI");
        cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tombolsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tombolupdate)
                        .addGap(22, 22, 22)
                        .addComponent(tombolrefresh)
                        .addGap(18, 18, 18)
                        .addComponent(tomboldelete)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel6)
                                .addComponent(txt_telp, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                                .addComponent(jLabel5)
                                .addComponent(jLabel4)
                                .addComponent(txt_namalengkap, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addComponent(txt_password, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addComponent(jLabel1)
                                .addComponent(txt_username)
                                .addComponent(cmb_role, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmb_status, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cari))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(168, 168, 168))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cari, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(txt_username))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txt_password, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_namalengkap, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmb_role, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_telp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmb_status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tombolsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tombolupdate, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tombolrefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tomboldelete, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tombolsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolsimpanActionPerformed
 // Validasi input kosong
        if (txt_username.getText().trim().isEmpty() || 
            txt_password.getText().trim().isEmpty() || 
            txt_namalengkap.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username, Password, dan Nama Lengkap harus diisi!");
            return;
        }
        
        // Validasi password maksimal 8 karakter
        if (txt_password.getText().length() > 8) {
            JOptionPane.showMessageDialog(null, "Password maksimal 8 karakter!");
            return;
        }
        
        try {
            String sql = "INSERT INTO user (username, password, fullname, role, telp, status, alamat, created_by, created_at) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURDATE())";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txt_username.getText().trim());
            pst.setString(2, txt_password.getText());
            pst.setString(3, txt_namalengkap.getText().trim());
            pst.setString(4, (String) cmb_role.getSelectedItem());
            pst.setString(5, txt_telp.getText().trim());
            pst.setString(6, (String) cmb_status.getSelectedItem());
            pst.setString(7, jTextArea1.getText().trim());
            pst.setInt(8, currentUserId);
            
            int result = pst.executeUpdate();
            
            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
                bersih();
                getData();
            }
            
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(null, "Username sudah digunakan!");
            } else if (e.getMessage().contains("Data too long")) {
                JOptionPane.showMessageDialog(null, "Password terlalu panjang! Maksimal 8 karakter.");
            } else {
                JOptionPane.showMessageDialog(null, "Error Simpan: " + e.getMessage());
            }
        }
    
        // TODO add your handling code here:
    }//GEN-LAST:event_tombolsimpanActionPerformed

    private void tombolrefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolrefreshActionPerformed
        // TODO add your handling code here:
        bersih();
        getData();
        jTextField5.setText("");
        JOptionPane.showMessageDialog(null, "Data berhasil di-refresh!");
  
    }//GEN-LAST:event_tombolrefreshActionPerformed

    private void tombolupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolupdateActionPerformed
        // TODO add your handling code here:
       int i = table.getSelectedRow();
        
        if (i == -1) {
            JOptionPane.showMessageDialog(null, "Pilih data yang ingin diupdate!");
            return;
        }
        
        // Validasi input kosong
        if (txt_username.getText().trim().isEmpty() || 
            txt_password.getText().trim().isEmpty() || 
            txt_namalengkap.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username, Password, dan Nama Lengkap harus diisi!");
            return;
        }
        
        // Validasi password maksimal 8 karakter
        if (txt_password.getText().length() > 8) {
            JOptionPane.showMessageDialog(null, "Password maksimal 8 karakter!");
            return;
        }
        
        int id = Integer.parseInt(model.getValueAt(i, 0).toString());

        try {
            String sql = "UPDATE user SET username=?, password=?, fullname=?, role=?, telp=?, status=?, alamat=?, " +
                         "update_by=?, update_at=CURDATE() WHERE user_id=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, txt_username.getText().trim());
            pst.setString(2, txt_password.getText());
            pst.setString(3, txt_namalengkap.getText().trim());
            pst.setString(4, (String) cmb_role.getSelectedItem());
            pst.setString(5, txt_telp.getText().trim());
            pst.setString(6, (String) cmb_status.getSelectedItem());
            pst.setString(7, jTextArea1.getText().trim());
            pst.setInt(8, currentUserId);
            pst.setInt(9, id);
            
            int result = pst.executeUpdate();
            
            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");
                bersih();
                getData();
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(null, "Username sudah digunakan!");
            } else if (e.getMessage().contains("Data too long")) {
                JOptionPane.showMessageDialog(null, "Password terlalu panjang! Maksimal 8 karakter.");
            } else {
                JOptionPane.showMessageDialog(null, "Error Update: " + e.getMessage());
            }
        }

    }//GEN-LAST:event_tombolupdateActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        pilihData();
        
        if (evt.getClickCount() == 2) {
        txt_username.requestFocus();
        }    
        table.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        jTable1MouseClicked(evt);
    }
});        
// TODO add your handling code here:
    }//GEN-LAST:event_tableMouseClicked

    private void cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariActionPerformed
        String keyword = jTextField5.getText().trim();
        
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Masukkan kata kunci pencarian!");
            jTextField5.requestFocus();
            return;
        }
        
        model.setRowCount(0);
        
        try {
            String sql = "SELECT user_id, username, password, fullname, role, telp, status, alamat FROM user WHERE " +
                         "username LIKE ? OR " +
                         "fullname LIKE ? OR " +
                         "telp LIKE ? OR " +
                         "alamat LIKE ? OR " +
                         "role LIKE ? OR " +
                         "status LIKE ? " +
                         "ORDER BY user_id DESC";
            pst = conn.prepareStatement(sql);
            
            String searchPattern = "%" + keyword + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            pst.setString(3, searchPattern);
            pst.setString(4, searchPattern);
            pst.setString(5, searchPattern);
            pst.setString(6, searchPattern);
            
            rs = pst.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                Object[] obj = new Object[8];
                
                obj[0] = rs.getInt("user_id");
                obj[1] = rs.getString("username");
                obj[2] = rs.getString("password");
                obj[3] = rs.getString("fullname");
                obj[4] = rs.getString("role");
                obj[5] = rs.getString("telp");
                obj[6] = rs.getString("status");
                obj[7] = rs.getString("alamat");
                
                model.addRow(obj);
                count++;
            }
            
            if (count == 0) {
                JOptionPane.showMessageDialog(null, "Data tidak ditemukan!");
                getData();
                jTextField5.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Ditemukan " + count + " data");
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error Cari: " + e.getMessage());
        }
    
    
        
        cari.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        cariActionPerformed(evt);
    }
});        // TODO add your handling code here:
    }//GEN-LAST:event_cariActionPerformed

    private void tomboldeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tomboldeleteActionPerformed
        int i = table.getSelectedRow();
        
        if (i == -1) {
            JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus!");
            return;
        }
        
        int id = Integer.parseInt(model.getValueAt(i, 0).toString());
        String username = model.getValueAt(i, 1).toString();
        
        // Cegah hapus user sendiri
        if (id == currentUserId) {
            JOptionPane.showMessageDialog(null, "Tidak dapat menghapus akun Anda sendiri!");
            return;
        }
        
        // Konfirmasi hapus
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Apakah Anda yakin ingin menghapus user '" + username + "'?\n" +
            "PERINGATAN: Semua data peminjaman terkait user ini akan ikut terhapus!",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM user WHERE user_id=?";
                pst = conn.prepareStatement(sql);
                pst.setInt(1, id);
                
                int result = pst.executeUpdate();
                
                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
                    bersih();
                    getData();
                }
                
            } catch (SQLException e) {
                if (e.getMessage().contains("foreign key constraint")) {
                    JOptionPane.showMessageDialog(null, 
                        "Tidak dapat menghapus! User ini masih memiliki data peminjaman aktif.\n" +
                        "Hapus data peminjaman terlebih dahulu.");
                } else {
                    JOptionPane.showMessageDialog(null, "Error Delete: " + e.getMessage());
                }
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_tomboldeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cari;
    private javax.swing.JComboBox<String> cmb_role;
    private javax.swing.JComboBox<String> cmb_status;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTable table;
    private javax.swing.JButton tomboldelete;
    private javax.swing.JButton tombolrefresh;
    private javax.swing.JButton tombolsimpan;
    private javax.swing.JButton tombolupdate;
    private javax.swing.JTextField txt_namalengkap;
    private javax.swing.JTextField txt_password;
    private javax.swing.JTextField txt_telp;
    private javax.swing.JTextField txt_username;
    // End of variables declaration//GEN-END:variables
}
