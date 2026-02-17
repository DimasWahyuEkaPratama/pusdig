/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pusdigg;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jdesktop.swingx.prompt.PromptSupport;
/**
 *
 * @author dppra
 */
public class riwayat_transaksi_admin extends javax.swing.JPanel {
    private DefaultTableModel model; // global
    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;

    /**
     * Creates new form riwayat_transaksi_admin
     */
    
    public riwayat_transaksi_admin() {
        initComponents();
        conn = koneksi.koneksi.koneksiDB();
        
        AutoCompleteDecorator.decorate(cmbKategori);
        
        // TABLE
        loadRiwayatPeminjaman(); 
        
        // Panggil function Kategori
        loadKategori();
        
        // PLACEHOLDER
        PromptSupport.setPrompt("Masukkan kode peminjaman", txtKode);
        PromptSupport.setPrompt("Masukkan username", txtNama);
        PromptSupport.setPrompt("Masukkan judul buku", txtJudul);
    }
    
    void loadKategori() {
        try {
            cmbKategori.removeAllItems();

            cmbKategori.addItem("Semua Kategori");

            String sql = "SELECT name_kategori FROM kategori ORDER BY name_kategori ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                cmbKategori.addItem(rs.getString("name_kategori"));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Load kategori gagal: " + e.getMessage());
        }
    }

    public void exportRiwayatPenting() {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String defaultFileName = "RiwayatPeminjaman_" + timeStamp + ".xls";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan File Excel");
        fileChooser.setSelectedFile(new java.io.File(defaultFileName));

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".xls")) filePath += ".xls";

            try (Connection conn = koneksi.koneksi.koneksiDB();
                 PreparedStatement pst = conn.prepareStatement(
                    "SELECT p.kode_peminjaman, u.fullname, b.judul, k.name_kategori, " +
                    "p.jumlah_pinjam, p.tanggal_pinjam, p.tanggal_kembali, " +
                    "p.status, p.denda, p.bayar, p.total " +
                    "FROM riwayat_peminjaman p " +
                    "JOIN user u ON p.user_id = u.user_id " +
                    "JOIN buku b ON p.buku_id = b.buku_id " +
                    "JOIN kategori k ON b.kategori_id = k.kategori_id " +
                    "ORDER BY p.tanggal_pinjam DESC");
                 ResultSet rs = pst.executeQuery()) {

                Workbook workbook = new HSSFWorkbook();
                Sheet sheet = workbook.createSheet("Riwayat Peminjaman");

                // Header
                String[] headers = {
                    "Kode Peminjaman", "User", "Judul Buku", "Kategori",
                    "Jumlah Pinjam", "Tanggal Pinjam", "Tanggal Kembali",
                    "Status", "Denda", "Bayar", "Total"
                };

                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                }

                int rowIndex = 1;
                while (rs.next()) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(rs.getString("kode_peminjaman"));
                    row.createCell(1).setCellValue(rs.getString("fullname"));
                    row.createCell(2).setCellValue(rs.getString("judul"));
                    row.createCell(3).setCellValue(rs.getString("name_kategori"));
                    row.createCell(4).setCellValue(rs.getInt("jumlah_pinjam"));
                    row.createCell(5).setCellValue(rs.getDate("tanggal_pinjam").toString());
                    // tanggal kembali
                    Date tglKembali = rs.getDate("tanggal_kembali");
                    row.createCell(6).setCellValue(tglKembali != null ? tglKembali.toString() : "-");

                    // denda
                    BigDecimal denda = rs.getBigDecimal("denda");
                    row.createCell(8).setCellValue(denda != null ? denda.toString() : "0");

                    // bayar
                    BigDecimal bayar = rs.getBigDecimal("bayar");
                    row.createCell(9).setCellValue(bayar != null ? bayar.toString() : "0");

                    // total
                    BigDecimal total = rs.getBigDecimal("total");
                    row.createCell(10).setCellValue(total != null ? total.toString() : "0");

                }

                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    workbook.write(fos);
                }
                workbook.close();

                JOptionPane.showMessageDialog(null, "Export sukses!\nFile: " + filePath);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error export: " + e.getMessage());
                e.printStackTrace();
            }
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

        txtKode = new javax.swing.JTextField();
        dcPinjam = new com.toedter.calendar.JDateChooser();
        dcKembali = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        cmbKategori = new javax.swing.JComboBox<>();
        txtJudul = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();

        txtKode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKodeActionPerformed(evt);
            }
        });

        jButton1.setText("cari");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Export");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable1);

        jButton3.setText("Refresh");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1206, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtJudul, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNama)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dcPinjam, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(dcKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton2)
                        .addComponent(jButton3))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtJudul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dcKembali, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dcPinjam, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                .addGap(25, 25, 25))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    model.setRowCount(0);

    try {
        String kode = txtKode.getText().trim();
        String judul =txtJudul.getText().trim();

        String sql =
            "SELECT p.kode_peminjaman, u.fullname, b.judul, k.name_kategori, " +
            "p.jumlah_pinjam, p.tanggal_pinjam, p.tanggal_kembali, " +
            "p.status, p.denda, p.bayar, p.total " +
            "FROM riwayat_peminjaman p " +
            "JOIN user u ON p.user_id = u.user_id " +
            "JOIN buku b ON p.buku_id = b.buku_id " +
            "JOIN kategori k ON b.kategori_id = k.kategori_id " +
            "WHERE p.kode_peminjaman LIKE ? " +
            "ORDER BY p.tanggal_pinjam DESC";

        pst = conn.prepareStatement(sql);
        pst.setString(1, "%" + kode + "%");
        pst.setString(2, "%" + judul + "%");

        rs = pst.executeQuery();

        while (rs.next()) {
            Object[] row = {
                rs.getString("kode_peminjaman"),
                rs.getString("fullname"),
                rs.getString("judul"),
                rs.getString("name_kategori"),
                rs.getInt("jumlah_pinjam"),
                rs.getDate("tanggal_pinjam"),
                rs.getDate("tanggal_kembali"),
                rs.getString("status"),
                rs.getBigDecimal("denda"),
                rs.getBigDecimal("bayar"),
                rs.getBigDecimal("total")
            };
            model.addRow(row);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        loadRiwayatPeminjaman();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    exportRiwayatPenting();        // TODO add your handling code here:
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtKodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKodeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmbKategori;
    private com.toedter.calendar.JDateChooser dcKembali;
    private com.toedter.calendar.JDateChooser dcPinjam;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtJudul;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtNama;
    // End of variables declaration//GEN-END:variables

    
    // AMBIL DATA UNTUK DITMPILKAN DI TABLE
private void loadRiwayatPeminjaman() {
    model = new DefaultTableModel();
    model.addColumn("Kode Peminjaman");
    model.addColumn("User");
    model.addColumn("Judul Buku");
    model.addColumn("Kategori");
    model.addColumn("Jumlah Pinjam");
    model.addColumn("Tanggal Pinjam");
    model.addColumn("Tanggal Kembali");
    model.addColumn("Status");
    model.addColumn("Denda");
    model.addColumn("Bayar");
    model.addColumn("Total");

    try {
        Connection conn = koneksi.koneksi.koneksiDB();

        String sql =
            "SELECT p.kode_peminjaman, u.fullname, b.judul, k.name_kategori, " +
            "p.jumlah_pinjam, p.tanggal_pinjam, p.tanggal_kembali, " +
            "p.status, p.denda, p.bayar, p.total " +
            "FROM riwayat_peminjaman p " +
            "JOIN user u ON p.user_id = u.user_id " +
            "JOIN buku b ON p.buku_id = b.buku_id " +
            "JOIN kategori k ON b.kategori_id = k.kategori_id " +
            "ORDER BY p.tanggal_pinjam DESC";

        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("kode_peminjaman"),
                rs.getString("fullname"),
                rs.getString("judul"),
                rs.getString("name_kategori"),
                rs.getInt("jumlah_pinjam"),
                rs.getDate("tanggal_pinjam"),
                rs.getDate("tanggal_kembali"),
                rs.getString("status"),
                rs.getBigDecimal("denda"),
                rs.getBigDecimal("bayar"),
                rs.getBigDecimal("total")
            });
        }

        jTable1.setModel(model);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        e.printStackTrace();
    }
}

}
