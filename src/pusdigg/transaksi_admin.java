/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pusdigg;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.sql.Date;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;
import java.util.Calendar;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 *
 * @author ASUS vivobook
 */
public class transaksi_admin extends javax.swing.JPanel {

    /**
     * Creates new form transaksi_admin
     */
    Connection conn = null;
    ResultSet rs =null;
    PreparedStatement pst = null;
    
    private DefaultTableModel model;
    private String sql;
    private int peminjamanIdAktif = -1;

    
    String id = session.getU_id();
    String usernamee = session.getU_username();
  

    
    public transaksi_admin() {
        initComponents();
           
        id_user.setText("SELAMAT DATANG"+id);
        username.setText("ANDA LOGIN SEBAGAI"+usernamee);
        
        AutoCompleteDecorator.decorate(cmb_kd1);
        AutoCompleteDecorator.decorate(cmb_kd2);
        AutoCompleteDecorator.decorate(cmb_kd3);
        
        conn = koneksi.koneksi.koneksiDB();

        model = new DefaultTableModel();
        jTable1.setModel(model);

        // Tambahkan kolom tabel
       model.addColumn("ID"); // ⬅ peminjaman_id (hidden)
        model.addColumn("Kode Peminjaman");
        model.addColumn("buku_id");
        model.addColumn("Name");
        model.addColumn("Judul Buku");
        model.addColumn("Kategori Buku");
        model.addColumn("Jumlah Pinjam");
        model.addColumn("Tanggal Pinjam");
        model.addColumn("Tanggal Kembali");
        model.addColumn("Status");

        // sembunyikan kolom ID
        jTable1.getColumnModel().getColumn(0).setMinWidth(0);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(2).setMinWidth(2);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(2);
        getData();      
        loadKategori();
  
          cmb_kd1.addItemListener(e -> {
    if (e.getStateChange() == ItemEvent.SELECTED) {
        validasiKodeBuku(cmb_kd1);
    }
});

cmb_kd2.addItemListener(e -> {
    if (e.getStateChange() == ItemEvent.SELECTED) {
        validasiKodeBuku(cmb_kd2);
    }
});

cmb_kd3.addItemListener(e -> {
    if (e.getStateChange() == ItemEvent.SELECTED) {
        validasiKodeBuku(cmb_kd3);
    }
});

 }
   
void getData() {
    model.setRowCount(0);

    try {
        String sql =
            "SELECT p.peminjaman_id, p.kode_peminjaman, u.fullname, b.buku_id, b.judul, " +
            "k.name_kategori, p.jumlah_pinjam, p.tanggal_pinjam, " +
            "p.tanggal_kembali, p.status " +
            "FROM peminjaman p " +
            "JOIN user u ON p.user_id = u.user_id " +
            "JOIN buku b ON p.Buku_id = b.Buku_id " +
            "JOIN kategori k ON b.kategori_id = k.kategori_id "
            ;

        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();

        while (rs.next()) {
            Object[] row = {
                rs.getInt("peminjaman_id"), // 0
                rs.getString("kode_peminjaman"), // 1
                rs.getString("buku_id"), // 2 <- tambahkan buku_id
                rs.getString("fullname"), // 3
                rs.getString("judul"), // 4
                rs.getString("name_kategori"), // 5
                rs.getInt("jumlah_pinjam"), // 6
                rs.getDate("tanggal_pinjam"), // 7
                rs.getDate("tanggal_kembali"), // 8
                rs.getString("status") // 9
            };
            model.addRow(row);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}

    void loadKategori() {
        try {
            cmb_kd1.removeAllItems();
            cmb_kd2.removeAllItems();
            cmb_kd3.removeAllItems();

            cmb_kd1.addItem("-- Pilih Kode --");
            cmb_kd2.addItem("-- Pilih Kode --");
            cmb_kd3.addItem("-- Pilih Kode --");

            String sql = "SELECT kode_buku FROM buku_item ORDER BY kode_buku ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String kategori = rs.getString("kode_buku");

                cmb_kd1.addItem(kategori);
                cmb_kd2.addItem(kategori);
                cmb_kd3.addItem(kategori);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Load kategori gagal: " + e.getMessage());
        }
    }

    void pilihData() {
       int i = jTable1.getSelectedRow();
       if (i == -1) return;

       peminjamanIdAktif = Integer.parseInt(
       model.getValueAt(i, 0).toString()
   );


   kode_peminjaman.setText(
       model.getValueAt(i, 1).toString()


   );


       try {
           String sql =
               "SELECT kd_bk1, kd_bk2, kd_bk3, status, denda, tanggal_kembali, catatan_pengajuan " +
               "FROM peminjaman WHERE peminjaman_id=?";

           pst = conn.prepareStatement(sql);
           pst.setInt(1, peminjamanIdAktif);
           rs = pst.executeQuery();

           if (rs.next()) {
               cmb_kd1.setSelectedItem(rs.getString("kd_bk1"));
               cmb_kd2.setSelectedItem(rs.getString("kd_bk2"));
               cmb_kd3.setSelectedItem(rs.getString("kd_bk3"));
               cmbstatus.setSelectedItem(rs.getString("status"));
               denda.setText(String.valueOf(rs.getInt("denda")));

               Date tglKembali = rs.getDate("tanggal_kembali");
               denda.setText(String.valueOf(hitungDenda(tglKembali)));

               txtcatatan.setText(
       rs.getString("catatan_pengajuan")
   );

           }

       } catch (Exception e) {
           JOptionPane.showMessageDialog(this, e.getMessage());
       }
   }

    int hitungDenda(Date tanggalKembali) {
       int dendaPerHari = 1000;
       LocalDate hariIni = LocalDate.now();
       LocalDate kembali = tanggalKembali.toLocalDate();

       if (hariIni.isAfter(kembali)) {
           long hariTerlambat = ChronoUnit.DAYS.between(kembali, hariIni);
           return (int) hariTerlambat * dendaPerHari;
       }
       return 0;
   }

    void updateStatusBuku(String kodeBuku, String status) throws Exception {
       String sql = "UPDATE buku_item SET status=? WHERE kode_buku=?";
       pst = conn.prepareStatement(sql);
       pst.setString(1, status);
       pst.setString(2, kodeBuku);
       pst.executeUpdate();
   }
 
    void resetForm() {
       peminjamanIdAktif = -1;
       kode_peminjaman.setText("");
       denda.setText("0");
       cmbstatus.setSelectedIndex(0);
       cmb_kd1.setSelectedIndex(0);
       cmb_kd2.setSelectedIndex(0);
       cmb_kd3.setSelectedIndex(0);
       txtcatatan.setText("");


   }
 
    void validasiKodeBuku(JComboBox<String> sumber) {

        String v1 = getKode(cmb_kd1);
        String v2 = getKode(cmb_kd2);
        String v3 = getKode(cmb_kd3);

        // hitung berapa kode valid (bukan placeholder)
        int terisi = 0;
        if (!v1.isEmpty()) terisi++;
        if (!v2.isEmpty()) terisi++;
        if (!v3.isEmpty()) terisi++;

        // kalau baru isi 1 combo → JANGAN VALIDASI
        if (terisi < 2) return;

        // validasi dobel (abaikan N/A)
        if (!v1.equals("N/A") && v1.equals(v2)) {
            duplikat(sumber); return;
        }
        if (!v1.equals("N/A") && v1.equals(v3)) {
            duplikat(sumber); return;
        }
        if (!v2.equals("N/A") && v2.equals(v3)) {
            duplikat(sumber);
        }
    }

    private String getKode(JComboBox<String> cmb) {
        if (cmb.getSelectedItem() == null) return "";
        String val = cmb.getSelectedItem().toString();
        if (val.equals("-- Pilih Kode --")) return "";
        return val;
    }

    private void duplikat(JComboBox<String> sumber) {
        JOptionPane.showMessageDialog(this,
            "Kode buku tidak boleh sama (kecuali N/A)");
        sumber.setSelectedItem("N/A");
    }

    private void batalkanAtauUbahJumlah() {
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu!");
            return;
        }

        String status = model.getValueAt(row, 9).toString().toLowerCase();
        if (!status.equals("pending")) {
            JOptionPane.showMessageDialog(this,
                    "❌ Hanya peminjaman dengan status PENDING yang bisa dibatalkan.");
            return;
        }

        String kodePinjam = model.getValueAt(row, 1).toString(); // pastikan ini kolom kode_peminjaman
        String bukuId = model.getValueAt(row, 2).toString(); // pastikan ini kolom buku_id
        int jumlahLama = Integer.parseInt(model.getValueAt(row, 6).toString());

        JTextField txtJumlahBaru = new JTextField();

        Object[] pesan = {
            "Jumlah yang ingin dipertahankan:",
            txtJumlahBaru,
            "\nKosongkan jika ingin membatalkan seluruh peminjaman"
        };

        Object[] opsi = {"Ubah Jumlah", "Batalkan Semua", "Batal"};

        int pilih = JOptionPane.showOptionDialog(
                this,
                pesan,
                "Batalkan Peminjaman",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opsi,
                opsi[0]
        );

        if (pilih == 2 || pilih == JOptionPane.CLOSED_OPTION) return;

        // ================= DEBUG FULL CONNECTION =================
        try (Connection conn = koneksi.koneksi.koneksiDB()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "❌ Gagal koneksi ke database!");
                return;
            }
            conn.setAutoCommit(false); // supaya aman kalau ada error di tengah-tengah

            // ================= BATALKAN SEMUA =================
            if (pilih == 1) {
                try {
                    String sqlDelete = "DELETE FROM peminjaman WHERE kode_peminjaman = ?";
                    PreparedStatement ps = conn.prepareStatement(sqlDelete);
                    ps.setString(1, kodePinjam);
                    int deleted = ps.executeUpdate();

                    if (deleted == 0) {
                        JOptionPane.showMessageDialog(this, "❌ Data peminjaman tidak ditemukan!");
                        conn.rollback();
                        return;
                    }

                    String sqlStok = "UPDATE buku SET stok = stok + ? WHERE buku_id = ?";
                    ps = conn.prepareStatement(sqlStok);
                    ps.setInt(1, jumlahLama);
                    ps.setString(2, bukuId);
                    int updated = ps.executeUpdate();

                    if (updated == 0) {
                        JOptionPane.showMessageDialog(this, "❌ Data buku tidak ditemukan!");
                        conn.rollback();
                        return;
                    }

                    conn.commit();
                    model.removeRow(row);
                    JOptionPane.showMessageDialog(this,
                            "✅ Peminjaman berhasil dibatalkan sepenuhnya.");
                } catch (Exception ex) {
                    conn.rollback();
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            // ================= UBAH JUMLAH =================
            else if (pilih == 0) {
                if (txtJumlahBaru.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Jumlah tidak boleh kosong!");
                    return;
                }

                int jumlahBaru;
                try {
                    jumlahBaru = Integer.parseInt(txtJumlahBaru.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "❌ Jumlah harus angka!");
                    return;
                }

                if (jumlahBaru <= 0 || jumlahBaru >= jumlahLama) {
                    JOptionPane.showMessageDialog(this,
                            "Jumlah harus lebih kecil dari jumlah lama!");
                    return;
                }

                int selisih = jumlahLama - jumlahBaru;

                try {
                    String sqlUpdate = "UPDATE peminjaman SET jumlah_pinjam = ? WHERE kode_peminjaman = ?";
                    PreparedStatement ps = conn.prepareStatement(sqlUpdate);
                    ps.setInt(1, jumlahBaru);
                    ps.setString(2, kodePinjam);
                    int updatedPeminjaman = ps.executeUpdate();

                    if (updatedPeminjaman == 0) {
                        JOptionPane.showMessageDialog(this, "❌ Data peminjaman tidak ditemukan!");
                        conn.rollback();
                        return;
                    }

                    String sqlStok = "UPDATE buku SET stok = stok + ? WHERE buku_id = ?";
                    ps = conn.prepareStatement(sqlStok);
                    ps.setInt(1, selisih);
                    ps.setString(2, bukuId);
                    int updatedBuku = ps.executeUpdate();

                    if (updatedBuku == 0) {
                        JOptionPane.showMessageDialog(this, "❌ Data buku tidak ditemukan!");
                        conn.rollback();
                        return;
                    }

                    conn.commit();
                    model.setValueAt(jumlahBaru, row, 5);
                    JOptionPane.showMessageDialog(this, "✏ Jumlah peminjaman berhasil diperbarui.");
                } catch (Exception ex) {
                    conn.rollback();
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error koneksi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBtnPerpanjang() {
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            btnPerpanjang.setEnabled(false);
            return;
        }

        String status = model.getValueAt(row, 9).toString().toLowerCase();
        Date tglKembali = (Date) model.getValueAt(row, 8);

        // tanggal hari ini
        Date today = new Date(System.currentTimeMillis());

        // tombol aktif jika status = dipinjam AND belum telat
        boolean bisaPerpanjang = status.equals("dipinjam") && !today.after(tglKembali);
        btnPerpanjang.setEnabled(bisaPerpanjang);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txt_cari = new javax.swing.JTextField();
        cmb_transaksi = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        id_user = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        kode_peminjaman = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cmbstatus = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        denda = new javax.swing.JTextField();
        cmb_kd1 = new javax.swing.JComboBox<>();
        cmb_kd2 = new javax.swing.JComboBox<>();
        cmb_kd3 = new javax.swing.JComboBox<>();
        jButton7 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtcatatan = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        btnPerpanjang = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();

        jButton4.setText("jButton4");

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
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });

        cmb_transaksi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "pending ", "dipinjam", "selesai", "ditolak ", "diterima" }));

        jButton1.setText("cari");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("kode peminjaman");

        jLabel2.setText("kode Buku 1");

        jButton2.setText("simpan");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("bayar denda");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setText("Slip peminjaman");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("slip pengembalian");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        id_user.setText("jLabel3");

        username.setText("jLabel3");

        jLabel3.setText("kode Buku 2");

        jLabel4.setText("kode Buku 3");

        kode_peminjaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kode_peminjamanActionPerformed(evt);
            }
        });

        jLabel5.setText("Status");

        cmbstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "pending", "dipinjam", "selesai", "ditolak", "diterima" }));

        jLabel6.setText("Denda");

        denda.setText("1000");

        jButton7.setText("Refresh");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        txtcatatan.setColumns(20);
        txtcatatan.setRows(5);
        jScrollPane2.setViewportView(txtcatatan);

        jLabel7.setText("Catatan");

        jLabel8.setText("Name user :");

        jButton8.setText("Batalkan Peminjaman");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        btnPerpanjang.setText("Ajukan Perpanjangan");
        btnPerpanjang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerpanjangActionPerformed(evt);
            }
        });

        jButton9.setText("Peminjaman Selesai");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(26, 26, 26)
                                .addComponent(jButton3)
                                .addGap(31, 31, 31)
                                .addComponent(jButton5)
                                .addGap(18, 18, 18)
                                .addComponent(jButton6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnPerpanjang, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(cmb_kd1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cmb_kd2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addGap(61, 61, 61)
                                                .addComponent(jLabel3)))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(cmb_kd3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel1)
                                    .addComponent(kode_peminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5)
                                    .addComponent(cmbstatus, 0, 190, Short.MAX_VALUE)
                                    .addComponent(jLabel6)
                                    .addComponent(denda))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7))
                                .addGap(39, 39, 39)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(username, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(id_user, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmb_transaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1099, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmb_transaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jLabel8)
                    .addComponent(jButton7))
                .addGap(9, 9, 9)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(username)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(id_user))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(kode_peminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbstatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel4))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(cmb_kd1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmb_kd2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmb_kd3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(denda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton3)
                            .addComponent(jButton5)
                            .addComponent(jButton6)
                            .addComponent(jButton8)
                            .addComponent(btnPerpanjang)
                            .addComponent(jButton9))))
                .addGap(25, 25, 25))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
// 1️⃣ Buat komponen input manual
JTextField txtDenda = new JTextField(10);
txtDenda.setText(denda.getText()); // ambil nilai denda otomatis
txtDenda.setEditable(false);       // tidak bisa diubah

JTextField txtBayar = new JTextField(10);

// 2️⃣ Susun panel input
JPanel panelInput = new JPanel();
panelInput.setLayout(new BoxLayout(panelInput, BoxLayout.Y_AXIS));

panelInput.add(new JLabel("Denda yang Harus Dibayar:"));
panelInput.add(txtDenda);
panelInput.add(Box.createVerticalStrut(10));

panelInput.add(new JLabel("Jumlah Uang Bayar:"));
panelInput.add(txtBayar);

// 3️⃣ Tampilkan dialog input
int result = JOptionPane.showConfirmDialog(
        this,
        panelInput,
        "Input Transaksi Denda",
        JOptionPane.OK_CANCEL_OPTION
);

// 4️⃣ Proses setelah tombol OK diklik
if (result == JOptionPane.OK_OPTION) {
    try {
        conn.setAutoCommit(false);

        double totalDenda = Double.parseDouble(txtDenda.getText());
        double bayar = Double.parseDouble(txtBayar.getText());

        if (bayar < totalDenda) {
            JOptionPane.showMessageDialog(this, "Uang bayar kurang!");
            return;
        }

        double kembalian = bayar - totalDenda;

        // ===== UPDATE DATABASE =====
        String sql = "UPDATE peminjaman SET "
                   + "denda=?, bayar=?, kembali=?, total=?, status=? "
                   + "WHERE peminjaman_id=?";
        pst = conn.prepareStatement(sql);
        pst.setDouble(1, totalDenda);
        pst.setDouble(2, bayar);
        pst.setDouble(3, kembalian);
        pst.setDouble(4, totalDenda);
        pst.setString(5, "selesai");
        pst.setInt(6, peminjamanIdAktif);
        pst.executeUpdate();

        conn.commit();

        // ===== CETAK SLIP PENGEMBALIAN =====
        int row = jTable1.getSelectedRow();
        if (row != -1) {
            String kodePinjam = model.getValueAt(row, 1).toString();
            String bukuId = model.getValueAt(row, 2).toString();
            String fullname = model.getValueAt(row, 3).toString();
            String judul = model.getValueAt(row, 4).toString();
            String kategori = model.getValueAt(row, 5).toString();
            int jumlah = Integer.parseInt(model.getValueAt(row, 6).toString());
            String tglPinjam = model.getValueAt(row, 7).toString();
            String tglKembali = model.getValueAt(row, 8).toString();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Simpan Slip Denda");
           fileChooser.setSelectedFile(new File("Slip_Denda_" + kodePinjam + ".pdf"));
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);

                document.add(new Paragraph("SLIP DENDA PEMINJAMAN BUKU")
                        .setBold().setFontSize(16).setMarginBottom(10));


                Table tableInfo = new Table(2);
                tableInfo.addCell(new Cell().add(new Paragraph("Kode Peminjaman:")));
                tableInfo.addCell(new Cell().add(new Paragraph(kodePinjam)));
                tableInfo.addCell(new Cell().add(new Paragraph("Nama Peminjam:")));
                tableInfo.addCell(new Cell().add(new Paragraph(fullname)));
                tableInfo.addCell(new Cell().add(new Paragraph("Status:")));
                tableInfo.addCell(new Cell().add(new Paragraph("Selesai")));
                tableInfo.addCell(new Cell().add(new Paragraph("Denda:")));
                tableInfo.addCell(new Cell().add(new Paragraph(String.valueOf(totalDenda))));
                tableInfo.addCell(new Cell().add(new Paragraph("Bayar:")));
                tableInfo.addCell(new Cell().add(new Paragraph(String.valueOf(bayar))));
                tableInfo.addCell(new Cell().add(new Paragraph("Kembali:")));
                tableInfo.addCell(new Cell().add(new Paragraph(String.valueOf(kembalian))));

                document.add(tableInfo);

                document.add(new Paragraph("\nDetail Buku:").setBold());
                Table tableBuku = new Table(5);
                tableBuku.addHeaderCell("Buku ID");
                tableBuku.addHeaderCell("Judul");
                tableBuku.addHeaderCell("Kategori");
                tableBuku.addHeaderCell("Jumlah");
                tableBuku.addHeaderCell("Tanggal Kembali");

                tableBuku.addCell(bukuId);
                tableBuku.addCell(judul);
                tableBuku.addCell(kategori);
                tableBuku.addCell(String.valueOf(jumlah));
                tableBuku.addCell(tglKembali);

                document.add(tableBuku);
                document.add(new Paragraph("\nTanggal Pinjam: " + tglPinjam));
                document.add(new Paragraph("Tanggal Pengembalian: " + java.time.LocalDate.now()));

                document.close();

                JOptionPane.showMessageDialog(this, "✅ Slip pengembalian berhasil dibuat:\n" + file.getAbsolutePath());
            }
        }

        JOptionPane.showMessageDialog(
            this,
            "Pembayaran berhasil!\n" +
            "Denda     : Rp " + totalDenda + "\n" +
            "Bayar     : Rp " + bayar + "\n" +
            "Kembali   : Rp " + kembalian
        );


        getData();
        resetForm();

    } catch (Exception e) {
        try { conn.rollback(); } catch (Exception ex) {}
        JOptionPane.showMessageDialog(this, "Gagal simpan pembayaran: " + e.getMessage());
    } finally {
        try { conn.setAutoCommit(true); } catch (Exception e) {}
    }

}



    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        pilihData();
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (cmb_kd1.getSelectedItem().equals("N/A") &&
            cmb_kd2.getSelectedItem().equals("N/A") &&
            cmb_kd3.getSelectedItem().equals("N/A")) {

            JOptionPane.showMessageDialog(this,
                "Minimal 1 buku harus dipilih!");
            return;
        }

        
        if (kode_peminjaman.getText().isEmpty()) {
         JOptionPane.showMessageDialog(this, "Pilih transaksi terlebih dahulu!");
         return;
     }

     if (cmb_kd1.getSelectedIndex() == 0) {
         JOptionPane.showMessageDialog(this, "Kode buku minimal 1 harus diisi!");
         return;
     }

     try {
         conn.setAutoCommit(false);
         
         if (peminjamanIdAktif == -1) {
    JOptionPane.showMessageDialog(this, "Pilih transaksi terlebih dahulu!");
    return;
}


         String kd1 = cmb_kd1.getSelectedItem().toString();
         String kd2 = cmb_kd2.getSelectedIndex() == 0 ? null : cmb_kd2.getSelectedItem().toString();
         String kd3 = cmb_kd3.getSelectedIndex() == 0 ? null : cmb_kd3.getSelectedItem().toString();

         int totalDenda = Integer.parseInt(denda.getText());
         String status = cmbstatus.getSelectedItem().toString();

         // 2️⃣ UPDATE TRANSAKSI → BERDASARKAN peminjaman_id
         String sql =
             "UPDATE peminjaman SET kd_bk1=?, kd_bk2=?, kd_bk3=?, status=?, denda=?, catatan=?, update_by=?" +
             "WHERE peminjaman_id=?";

        pst = conn.prepareStatement(sql);
        pst.setString(1, kd1);
        pst.setString(2, kd2);
        pst.setString(3, kd3);
        pst.setString(4, status);
        pst.setInt(5, totalDenda);
        pst.setString(6, txtcatatan.getText());   // catatan
        pst.setString(7, session.getU_id());
        pst.setInt(8, peminjamanIdAktif); // WHERE
     
        
         pst.executeUpdate();

         conn.commit();

         JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan");
         getData();
         resetForm();

     } catch (Exception e) {
         try { conn.rollback(); } catch (Exception ex) {}
         JOptionPane.showMessageDialog(this, "Gagal menyimpan : " + e.getMessage());
     } finally {
         try { conn.setAutoCommit(true); } catch (Exception e) {}
     }

        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void kode_peminjamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kode_peminjamanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kode_peminjamanActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
   model.setRowCount(0);

String namaUser = txt_cari.getText().trim();
String status = cmb_transaksi.getSelectedItem().toString();

String sql =
    "SELECT p.peminjaman_id, p.kode_peminjaman, u.fullname, b.judul, " +
    "k.name_kategori, p.jumlah_pinjam, p.tanggal_pinjam, " +
    "p.tanggal_kembali, p.status " +
    "FROM peminjaman p " +
    "JOIN user u ON p.user_id = u.user_id " +
    "JOIN buku b ON p.buku_id = b.buku_id " +
    "JOIN kategori k ON b.kategori_id = k.kategori_id " +
    "WHERE 1=1 ";

if (!namaUser.isEmpty()) {
    sql += "AND u.fullname LIKE ? ";
}

if (!status.equals("Semua")) {
    sql += "AND p.status = ? ";
}

try {
    pst = conn.prepareStatement(sql);

    int index = 1;

    if (!namaUser.isEmpty()) {
        pst.setString(index++, "%" + namaUser + "%");
    }

    if (!status.equals("Semua")) {
        pst.setString(index++, status);
    }

    rs = pst.executeQuery();

    while (rs.next()) {
        Object[] row = {
            rs.getInt("peminjaman_id"),
            rs.getString("kode_peminjaman"),
            rs.getString("fullname"),
            rs.getString("judul"),
            rs.getString("name_kategori"),
            rs.getInt("jumlah_pinjam"),
            rs.getDate("tanggal_pinjam"),
            rs.getDate("tanggal_kembali"),
            rs.getString("status")
        };
        model.addRow(row);
    }

    if (model.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "Data tidak ditemukan");
    }

} catch (Exception e) {
    JOptionPane.showMessageDialog(this, e.getMessage());
}



        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        resetForm();
        getData();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_cariActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        batalkanAtauUbahJumlah();       // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void btnPerpanjangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerpanjangActionPerformed
int row = jTable1.getSelectedRow();
if (row == -1) {
    JOptionPane.showMessageDialog(this, "Pilih data peminjaman terlebih dahulu!");
    return;
}

// ambil data dari JTable
String kode = model.getValueAt(row, 1).toString();   // kode_peminjaman
String bukuId = model.getValueAt(row, 2).toString(); // buku_id
Date tglKembali = (Date) model.getValueAt(row, 8);   // tanggal_kembali saat ini
String status = model.getValueAt(row, 9).toString().toLowerCase();

// validasi status
if (!status.equals("dipinjam")) {
    JOptionPane.showMessageDialog(this, "❌ Tidak bisa diajukan perpanjangan!");
    return;
}

// dialog alasan
JTextArea txtAlasan = new JTextArea(3, 20);
int result = JOptionPane.showConfirmDialog(
    this,
    new JScrollPane(txtAlasan),
    "Ajukan Perpanjangan",
    JOptionPane.OK_CANCEL_OPTION
);
if (result != JOptionPane.OK_OPTION) return;

String alasan = txtAlasan.getText().trim();
if (alasan.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Alasan wajib diisi!");
    return;
}

// hitung tanggal_kembali baru (+5 hari)
Calendar cal = Calendar.getInstance();
cal.setTime(tglKembali);
cal.add(Calendar.DAY_OF_MONTH, 5);
Date tglKembaliBaru = new Date(cal.getTimeInMillis());

// SQL update
String sql = "UPDATE peminjaman SET status = ?, catatan_pengajuan = ?, tanggal_kembali = ? " +
             "WHERE kode_peminjaman = ? " +
             "AND buku_id = ? " +
             "AND tanggal_kembali = ? " +
             "AND status = 'dipinjam'";

try (Connection conn = koneksi.koneksi.koneksiDB();
     PreparedStatement ps = conn.prepareStatement(sql)) {

    ps.setString(1, "diperpanjang");
    ps.setString(2, alasan);
    ps.setDate(3, tglKembaliBaru);
    ps.setString(4, kode);
    ps.setString(5, bukuId);
    ps.setDate(6, tglKembali); // kondisi WHERE pakai tanggal_kembali lama

    int affected = ps.executeUpdate();

    if (affected == 0) {
        JOptionPane.showMessageDialog(this, "⚠ Data tidak ditemukan atau sudah berubah.");
        return;
    }

    // update JTable
    model.setValueAt("diperpanjang", row, 9);
    model.setValueAt(tglKembaliBaru, row, 8); // update tanggal_kembali
    updateBtnPerpanjang();

    JOptionPane.showMessageDialog(this, "📨 Perpanjangan berhasil diajukan. Tanggal kembali bertambah 5 hari.");

} catch (Exception e) {
    JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
}

        // TODO add your handling code here:
    }//GEN-LAST:event_btnPerpanjangActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
    int row = jTable1.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu!");
        return;
    }

    try {
        // Ambil data dari JTable
        String kodePinjam = model.getValueAt(row, 1).toString();
        String bukuId = model.getValueAt(row, 2).toString();
        String fullname = model.getValueAt(row, 3).toString();
        String judul = model.getValueAt(row, 4).toString();
        String kategori = model.getValueAt(row, 5).toString();
        int jumlah = Integer.parseInt(model.getValueAt(row, 6).toString());
        String tglPinjam = model.getValueAt(row, 7).toString();
        String tglKembali = model.getValueAt(row, 8).toString();
        String status = model.getValueAt(row, 9).toString();

        // Folder simpan PDF
        // Pilih lokasi simpan PDF
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Slip Peminjaman");
        fileChooser.setSelectedFile(new File("Slip_Peminjaman_" + kodePinjam + ".pdf"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            // User membatalkan
            return;
        }

        File file = fileChooser.getSelectedFile();

        PdfWriter writer = new PdfWriter(file.getAbsolutePath());
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Judul
        document.add(new Paragraph("SLIP PEMINJAMAN BUKU")
                .setBold()
                .setFontSize(16)
                .setMarginBottom(10));

        // Info peminjam
        Table tableInfo = new Table(2); // 2 kolom

        tableInfo.addCell(new Cell().add(new Paragraph("Kode Peminjaman:")));
        tableInfo.addCell(new Cell().add(new Paragraph(kodePinjam)));

        tableInfo.addCell(new Cell().add(new Paragraph("Nama Peminjam:")));
        tableInfo.addCell(new Cell().add(new Paragraph(fullname)));

        tableInfo.addCell(new Cell().add(new Paragraph("Status:")));
        tableInfo.addCell(new Cell().add(new Paragraph(status)));


        document.add(tableInfo);

        document.add(new Paragraph("\nDetail Buku:").setBold());

        // Table Buku
        Table tableBuku = new Table(5);
        tableBuku.addHeaderCell("Buku ID");
        tableBuku.addHeaderCell("Judul");
        tableBuku.addHeaderCell("Kategori");
        tableBuku.addHeaderCell("Jumlah");
        tableBuku.addHeaderCell("Tanggal Kembali");

        tableBuku.addCell(bukuId);
        tableBuku.addCell(judul);
        tableBuku.addCell(kategori);
        tableBuku.addCell(String.valueOf(jumlah));
        tableBuku.addCell(tglKembali);

        document.add(tableBuku);

        document.add(new Paragraph("\nTanggal Pinjam: " + tglPinjam));

        document.close();
        JOptionPane.showMessageDialog(this, "✅ Slip peminjaman berhasil dibuat:\n" + file.getAbsolutePath());

    } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(this, "Jumlah buku tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal membuat slip PDF:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
int row = jTable1.getSelectedRow();
if (row == -1) {
    JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu!");
    return;
}

// cek status
String status = model.getValueAt(row, 9).toString().trim().toLowerCase();
if (!status.equals("selesai")) {
    JOptionPane.showMessageDialog(this,
        "Tombol hanya aktif jika status selesai.\nStatus saat ini: " + status.toUpperCase());
    return;
}

int confirm = JOptionPane.showConfirmDialog(this,
        "Buku sudah selesai dikembalikan.\nKirim ke Riwayat Transaksi?",
        "Konfirmasi",
        JOptionPane.YES_NO_OPTION);
if (confirm != JOptionPane.YES_OPTION) return;

Connection conn = null;
PreparedStatement pstSelect = null;
PreparedStatement pstInsert = null;
PreparedStatement pstDelete = null;
ResultSet rs = null;

try {
    conn = koneksi.koneksi.koneksiDB();
    conn.setAutoCommit(false);

    // Ambil data lengkap dari DB berdasarkan kode_peminjaman
    String kodePeminjaman = model.getValueAt(row, 1).toString(); // kolom kode_peminjaman
    String sqlSelect = "SELECT * FROM peminjaman WHERE kode_peminjaman = ?";
    pstSelect = conn.prepareStatement(sqlSelect);
    pstSelect.setString(1, kodePeminjaman);
    rs = pstSelect.executeQuery();

    if (!rs.next()) {
        JOptionPane.showMessageDialog(this, "Data peminjaman tidak ditemukan di database!");
        return;
    }

    // ambil semua field yang dibutuhkan termasuk user_id
    int peminjaman_id      = rs.getInt("peminjaman_id");
    String user_id         = rs.getString("user_id"); // WAJIB ADA
    String buku_id         = rs.getString("buku_id");
    String kd_bk1          = rs.getString("kd_bk1");
    String kd_bk2          = rs.getString("kd_bk2");
    String kd_bk3          = rs.getString("kd_bk3");
    int jumlah_pinjam      = rs.getInt("jumlah_pinjam");
    java.sql.Date tanggal_pinjam = rs.getDate("tanggal_pinjam");
    java.sql.Date tanggal_kembali = rs.getDate("tanggal_kembali");
    String stat            = rs.getString("status");
    int denda              = rs.getInt("denda");
    int bayar              = rs.getInt("bayar");
    int kembali            = rs.getInt("kembali");
    int total              = rs.getInt("total");
    String catatan         = rs.getString("catatan");
    String catatan_pengajuan = rs.getString("catatan_pengajuan");
    String update_by       = rs.getString("update_by");
    java.sql.Timestamp update_at = rs.getTimestamp("update_at");
    String created_by      = rs.getString("created_by");
    java.sql.Timestamp created_at = rs.getTimestamp("created_at");

    // insert ke riwayat_peminjaman
    String sqlInsert = "INSERT INTO riwayat_peminjaman(" +
            "peminjaman_id,kode_peminjaman,user_id,buku_id,kd_bk1,kd_bk2,kd_bk3," +
            "jumlah_pinjam,tanggal_pinjam,tanggal_kembali,status,denda,bayar,kembali,total," +
            "catatan,catatan_pengajuan,update_by,update_at,created_by,created_at) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    pstInsert = conn.prepareStatement(sqlInsert);
    pstInsert.setInt(1, peminjaman_id);
    pstInsert.setString(2, kodePeminjaman);
    pstInsert.setString(3, user_id); // jangan null!
    pstInsert.setString(4, buku_id);
    pstInsert.setString(5, kd_bk1);
    pstInsert.setString(6, kd_bk2);
    pstInsert.setString(7, kd_bk3);
    pstInsert.setInt(8, jumlah_pinjam);
    pstInsert.setDate(9, tanggal_pinjam);
    pstInsert.setDate(10, tanggal_kembali);
    pstInsert.setString(11, stat);
    pstInsert.setInt(12, denda);
    pstInsert.setInt(13, bayar);
    pstInsert.setInt(14, kembali);
    pstInsert.setInt(15, total);
    pstInsert.setString(16, catatan);
    pstInsert.setString(17, catatan_pengajuan);
    pstInsert.setString(18, update_by);
    pstInsert.setTimestamp(19, update_at);
    pstInsert.setString(20, created_by);
    pstInsert.setTimestamp(21, created_at);

    pstInsert.executeUpdate();

    // hapus dari peminjaman
    String sqlDelete = "DELETE FROM peminjaman WHERE peminjaman_id = ?";
    pstDelete = conn.prepareStatement(sqlDelete);
    pstDelete.setInt(1, peminjaman_id);
    pstDelete.executeUpdate();

    conn.commit();

    model.removeRow(row);
    JOptionPane.showMessageDialog(this, "📚 Data berhasil dipindahkan ke Riwayat Transaksi.");

} catch (Exception e) {
    try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "Terjadi error: " + e.getMessage());
} finally {
    try { if (rs != null) rs.close(); } catch (Exception ex) {}
    try { if (pstSelect != null) pstSelect.close(); } catch (Exception ex) {}
    try { if (pstInsert != null) pstInsert.close(); } catch (Exception ex) {}
    try { if (pstDelete != null) pstDelete.close(); } catch (Exception ex) {}
    try { if (conn != null) conn.close(); } catch (Exception ex) {}
}

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
int row = jTable1.getSelectedRow();
if (row == -1) {
    JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu!");
    return;
}

// Cek status
String status = model.getValueAt(row, 9).toString().trim().toLowerCase();
if (!status.equals("selesai")) {
    JOptionPane.showMessageDialog(this, 
        "Slip pengembalian hanya bisa dibuat jika status selesai.\nStatus saat ini: " + status.toUpperCase());
    return;
}

try {
    // Ambil data dari JTable
    String kodePinjam = model.getValueAt(row, 1).toString();
    String bukuId = model.getValueAt(row, 2).toString();
    String fullname = model.getValueAt(row, 3).toString();
    String judul = model.getValueAt(row, 4).toString();
    String kategori = model.getValueAt(row, 5).toString();
    int jumlah = Integer.parseInt(model.getValueAt(row, 6).toString());
    String tglPinjam = model.getValueAt(row, 7).toString();
    String tglKembali = model.getValueAt(row, 8).toString();

    // Pilih lokasi simpan PDF
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Simpan Slip Pengembalian");
    fileChooser.setSelectedFile(new File("Slip_Pengembalian_" + kodePinjam + ".pdf"));
    int userSelection = fileChooser.showSaveDialog(this);
    if (userSelection != JFileChooser.APPROVE_OPTION) return;

    File file = fileChooser.getSelectedFile();

    // Buat PDF
    PdfWriter writer = new PdfWriter(file.getAbsolutePath());
    PdfDocument pdfDoc = new PdfDocument(writer);
    Document document = new Document(pdfDoc);

    // Judul
    document.add(new Paragraph("SLIP PENGEMBALIAN BUKU")
            .setBold()
            .setFontSize(16)
            .setMarginBottom(10));

    // Info Peminjam
    Table tableInfo = new Table(2);
    tableInfo.addCell(new Cell().add(new Paragraph("Kode Peminjaman:")));
    tableInfo.addCell(new Cell().add(new Paragraph(kodePinjam)));

    tableInfo.addCell(new Cell().add(new Paragraph("Nama Peminjam:")));
    tableInfo.addCell(new Cell().add(new Paragraph(fullname)));

    tableInfo.addCell(new Cell().add(new Paragraph("Status:")));
    tableInfo.addCell(new Cell().add(new Paragraph("Selesai")));

    document.add(tableInfo);

    // Detail Buku
    document.add(new Paragraph("\nDetail Buku:").setBold());

    Table tableBuku = new Table(5);
    tableBuku.addHeaderCell("Buku ID");
    tableBuku.addHeaderCell("Judul");
    tableBuku.addHeaderCell("Kategori");
    tableBuku.addHeaderCell("Jumlah");
    tableBuku.addHeaderCell("Tanggal Kembali");

    tableBuku.addCell(bukuId);
    tableBuku.addCell(judul);
    tableBuku.addCell(kategori);
    tableBuku.addCell(String.valueOf(jumlah));
    tableBuku.addCell(tglKembali);

    document.add(tableBuku);

    document.add(new Paragraph("\nTanggal Pinjam: " + tglPinjam));
    document.add(new Paragraph("Tanggal Pengembalian: " + java.time.LocalDate.now()));

    document.close();

    JOptionPane.showMessageDialog(this, "✅ Slip pengembalian berhasil dibuat:\n" + file.getAbsolutePath());

} catch (NumberFormatException nfe) {
    JOptionPane.showMessageDialog(this, "Jumlah buku tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
} catch (Exception e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "Gagal membuat slip PDF:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
}
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPerpanjang;
    private javax.swing.JComboBox<String> cmb_kd1;
    private javax.swing.JComboBox<String> cmb_kd2;
    private javax.swing.JComboBox<String> cmb_kd3;
    private javax.swing.JComboBox<String> cmb_transaksi;
    private javax.swing.JComboBox<String> cmbstatus;
    private javax.swing.JTextField denda;
    private javax.swing.JLabel id_user;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField kode_peminjaman;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextArea txtcatatan;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables
}
