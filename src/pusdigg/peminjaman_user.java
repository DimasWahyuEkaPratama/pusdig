/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pusdigg;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Image;
import javax.swing.ImageIcon;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.SQLException;


/**
 *
 * @author dppra
 */
public class peminjaman_user extends javax.swing.JPanel {
    

    /**
     * Creates new form buku
     */
    
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    private DefaultTableModel  model;
    private String sql;
    private final DateFormat dfi = new SimpleDateFormat ("dd/MM/yyyy");
    Calendar hariini ;
    String pathGambar;
   private int kategoriId;
   private int bukuId;




    String id = session.getU_id();
    String usernamee = session.getU_username();    
    
   public peminjaman_user() {
    initComponents();

    conn = koneksi.koneksi.koneksiDB();   // ✅ koneksi dulu!

   

    id_user.setText("SELAMAT DATANG " + id);
    username.setText("ANDA LOGIN SEBAGAI " + usernamee);
    nama.setText(usernamee);

    
    AutoCompleteDecorator.decorate(cmb_kategori);
    
    conn = koneksi.koneksi.koneksiDB();

    model = new DefaultTableModel() {
   @Override
public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex == 10) { 
        return ImageIcon.class;
    }
    return Object.class;
}

};

jTable1.setModel(model);
jTable1.setRowHeight(80);



 // 0

model.addColumn("Buku_Id");
model.addColumn("Kategori_ID");
model.addColumn("Kategori");
model.addColumn("Judul");
model.addColumn("Penulis");
model.addColumn("Penerbit");
model.addColumn("Tahun Terbit");
model.addColumn("Stok");
model.addColumn("Deskripsi");
model.addColumn("Rak Buku");
model.addColumn("Gambar");
model.addColumn("PathGambar");
model.addColumn("Aksi"); // ⬅ kolom button




    getData();
    
    // Buku_ID
jTable1.getColumnModel().getColumn(0).setMinWidth(0);
jTable1.getColumnModel().getColumn(0).setMaxWidth(0);

// Kategori_ID
jTable1.getColumnModel().getColumn(1).setMinWidth(0);
jTable1.getColumnModel().getColumn(1).setMaxWidth(0);

jTable1.getColumnModel().getColumn(11).setMinWidth(0);
jTable1.getColumnModel().getColumn(11).setMaxWidth(0);

loadKategori();


}

        
    void getData() {
    model.setRowCount(0); // bersihkan tabel

    try {
        String sql =
            "SELECT b.*, k.name_kategori " +
            "FROM buku b " +
            "JOIN kategori k ON b.kategori_id = k.kategori_id";

        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();

        while (rs.next()) {
            Object[] obj = new Object[13];

            obj[0]  = rs.getInt("buku_id");
            obj[1]  = rs.getInt("kategori_id");
            obj[2]  = rs.getString("name_kategori");
            obj[3]  = rs.getString("judul");
            obj[4]  = rs.getString("penulis");
            obj[5]  = rs.getString("penerbit");
            obj[6]  = rs.getDate("tahun_terbit");
            obj[7]  = rs.getInt("stok");
            obj[8]  = rs.getString("deskripsi");
            obj[9]  = rs.getString("rak_buku");

            // gambar
            String path = rs.getString("imgsampul");
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(60, 80, Image.SCALE_SMOOTH);
            obj[10] = new ImageIcon(img);
            
            // path gambar hidden
            obj[11] = path;
            obj[12] = "Detail";


            model.addRow(obj);
        }

        jTable1.setModel(model);
        jTable1.getColumnModel().getColumn(12)
        .setCellRenderer(new ButtonRenderer());

        jTable1.getColumnModel().getColumn(12)
                .setCellEditor(new ButtonEditor(new JCheckBox(), jTable1));

        jTable1.getColumnModel().getColumn(12).setPreferredWidth(80);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    }
}



    
    public class ButtonRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        JButton btn = new JButton("Detail");
        btn.setFocusPainted(false);
        return btn;
    }
}
 
public class ButtonEditor extends DefaultCellEditor {

    private JButton button;
    private int row;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox, JTable table) {
        super(checkBox);
        this.table = table;

        button = new JButton("Detail");
        button.setFocusPainted(false);

        button.addActionListener(e -> showDetail());
    }

    private void showDetail() {
        int i = table.getSelectedRow();
        if (i == -1) return;

        // ambil data dari tabel
        String judul = table.getValueAt(i, 3).toString();
        String penulis = table.getValueAt(i, 4).toString();
        String penerbit = table.getValueAt(i, 5).toString();
        String kategori = table.getValueAt(i, 2).toString();
        String stok = table.getValueAt(i, 7).toString();
        String deskripsi = table.getValueAt(i, 8).toString();
        String rak = table.getValueAt(i, 9).toString();
        String path = table.getValueAt(i, 11).toString();

        // gambar
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(180, 240, Image.SCALE_SMOOTH);
        JLabel lblImg = new JLabel(new ImageIcon(img));

        JTextArea txt = new JTextArea(
                "Judul     : " + judul +
                "\nPenulis  : " + penulis +
                "\nPenerbit : " + penerbit +
                "\nKategori : " + kategori +
                "\nStok     : " + stok +
                "\nRak      : " + rak +
                "\n\nDeskripsi:\n" + deskripsi
        );
        txt.setEditable(false);
        txt.setWrapStyleWord(true);
        txt.setLineWrap(true);

        JScrollPane sp = new JScrollPane(txt);
        sp.setPreferredSize(new Dimension(300, 200));

        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.add(lblImg, BorderLayout.WEST);
        panel.add(sp, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
                null,
                panel,
                "Detail Buku",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {
        this.row = row;
        return button;
    }
}
    
    void loadKategori() {
    try {
        cmb_kategori.removeAllItems();

        String sql = "SELECT name_kategori FROM kategori ORDER BY name_kategori ASC";
        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();

        while (rs.next()) {
            cmb_kategori.addItem(rs.getString("name_kategori"));
        }

        System.out.println("Kategori loaded: " + cmb_kategori.getItemCount());

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
            "Load kategori gagal: " + e.getMessage());
    }
}
    
   private String generateKodePeminjaman(Connection conn) throws SQLException {
    String kode = "KD_BUKU_0001";

    String sql = "SELECT kode_peminjaman FROM peminjaman " +
                 "ORDER BY peminjaman_id DESC LIMIT 1";

    PreparedStatement ps = conn.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        String lastKode = rs.getString("kode_peminjaman");

        if (lastKode != null && lastKode.startsWith("KD_BUKU_")) {
            int num = Integer.parseInt(lastKode.substring(8));
            num++;
            kode = String.format("KD_BUKU_%04d", num);
        }
    }

    rs.close();
    ps.close();
    return kode;
}









      
//     void pilihData() {
//    int i = jTable1.getSelectedRow();
//    if (i == -1) return;
//
//    txt_kategori.setText(model.getValueAt(i, 1).toString());
//    judul.setText(model.getValueAt(i, 2).toString());
//    penulis.setText(model.getValueAt(i, 3).toString());
//    penerbit.setText(model.getValueAt(i, 4).toString());
//
//    // ⬇ JDateChooser
//    java.util.Date tanggal = (java.util.Date) model.getValueAt(i, 5);
//    tgl.setDate(tanggal);
//
//    stok.setText(model.getValueAt(i, 6).toString());
//    deskripsi.setText(model.getValueAt(i, 8).toString());
//    upload.setText(model.getValueAt(i, 9).toString());
//    
//}
   void pilihData() {
    int i = jTable1.getSelectedRow();
    if (i == -1) return;

    // PK & FK
    bukuId = Integer.parseInt(model.getValueAt(i, 0).toString());
    kategoriId = Integer.parseInt(model.getValueAt(i, 1).toString());
    judul.setText(model.getValueAt(i, 3).toString());
    // text field
}



    


        void bersih() {
            txt_cari.setText("");
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
        txt_cari = new javax.swing.JTextField();
        cmb_cari = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        id_user = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        cmb_kategori = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        nama = new javax.swing.JTextField();
        judul = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jumlah_dipinjam = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        cmb_cari.setText("CARI");
        cmb_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_cariActionPerformed(evt);
            }
        });

        jLabel1.setText("Judul:");

        jLabel9.setText("Kategori:");

        id_user.setText("jLabel11");

        username.setText("jLabel11");

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
        jScrollPane3.setViewportView(jTable1);

        cmb_kategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_kategoriActionPerformed(evt);
            }
        });

        jButton1.setText("Refresh");

        jLabel2.setText("Name Peminjam");

        jLabel3.setText("Judul Buku");

        jLabel4.setText("Jumlah Pinjam");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(username)
                    .addComponent(id_user))
                .addGap(47, 47, 47))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(cmb_kategori, 0, 352, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(nama))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(57, 57, 57)
                                .addComponent(judul, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(30, 30, 30)
                                .addComponent(jumlah_dipinjam, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(id_user)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(username))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel9))
                                .addGap(35, 35, 35))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmb_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton1)))))
                .addGap(13, 13, 13)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(judul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jumlah_dipinjam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(55, Short.MAX_VALUE))
        );

        jButton2.setText("Pinjam");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        pilihData();
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void cmb_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_cariActionPerformed
     // TODO add your handling code here:
    model.setRowCount(0);

    try {
        String judulCari = txt_cari.getText().trim();
        String kategori = cmb_kategori.getSelectedItem().toString();

        String sql =
            "SELECT b.*, k.name_kategori " +
            "FROM buku b " +
            "JOIN kategori k ON b.kategori_id = k.kategori_id " +
            "WHERE k.name_kategori = ? " +
            "AND b.judul LIKE ?";

        pst = conn.prepareStatement(sql);
        pst.setString(1, kategori);
        pst.setString(2, "%" + judulCari + "%");

        rs = pst.executeQuery();

        while (rs.next()) {
            Object[] obj = new Object[12];

            obj[0] = rs.getInt("buku_id");
            obj[1] = rs.getInt("kategori_id");
            obj[2] = rs.getString("name_kategori");
            obj[3] = rs.getString("judul");
            obj[4] = rs.getString("penulis");
            obj[5] = rs.getString("penerbit");
            obj[6] = rs.getDate("tahun_terbit");
            obj[7] = rs.getInt("stok");
            obj[8] = rs.getString("deskripsi");
            obj[9] = rs.getString("rak_buku");

            String path = rs.getString("imgsampul");
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(60, 80, Image.SCALE_SMOOTH);
            obj[10] = new ImageIcon(img);
            obj[11] = path;

            model.addRow(obj);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e);
    }



    }//GEN-LAST:event_cmb_cariActionPerformed

    private void cmb_kategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_kategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmb_kategoriActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
int row = jTable1.getSelectedRow();
if (row == -1) {
    JOptionPane.showMessageDialog(this, "Pilih buku terlebih dahulu!");
    return;
}

if (jumlah_dipinjam.getText().isEmpty()) {
    JOptionPane.showMessageDialog(this, "Masukkan jumlah pinjam!");
    return;
}

Connection conn = null;
PreparedStatement ps = null;
ResultSet rs = null;

try {
    conn = koneksi.koneksi.koneksiDB();
    conn.setAutoCommit(false); // 🔐 TRANSACTION

    int userId = Integer.parseInt(session.getU_id());
    int bukuId = Integer.parseInt(jTable1.getValueAt(row, 0).toString());
    int jumlah = Integer.parseInt(jumlah_dipinjam.getText());

    /* ===============================
       KODE PEMINJAMAN (1 USER = 1 KODE)
    =============================== */
    String today = new java.text.SimpleDateFormat("yyMMdd")
            .format(new java.util.Date());

    PreparedStatement psKode = conn.prepareStatement(
        "SELECT kode_peminjaman FROM peminjaman " +
        "WHERE kode_peminjaman LIKE ? " +
        "ORDER BY peminjaman_id DESC LIMIT 1"
    );
    psKode.setString(1, "PJM-" + today + "%");
    ResultSet rsKode = psKode.executeQuery();

    int lastNumber = 0;
    if (rsKode.next()) {
        String lastKode = rsKode.getString("kode_peminjaman");
        // ambil 3 digit urutan
        lastNumber = Integer.parseInt(
            lastKode.substring(11, 14)
        );
    }

    String kodePeminjaman =
        "PJM-" + today + "-" +
        String.format("%03d", lastNumber) +
        "-" + userId;

    /* ===============================
       CEK TOTAL BUKU YANG MASIH DIPINJAM
    =============================== */
    ps = conn.prepareStatement(
        "SELECT IFNULL(SUM(jumlah_pinjam),0) AS total " +
        "FROM peminjaman " +
        "WHERE user_id = ? AND status IN ('pending','dipinjam')"
    );
    ps.setInt(1, userId);
    rs = ps.executeQuery();

    int totalDipinjam = 0;
    if (rs.next()) {
        totalDipinjam = rs.getInt("total");
    }

    if (totalDipinjam >= 3) {
        JOptionPane.showMessageDialog(this,
            "❌ Batas peminjaman Anda sudah habis.\n\n" +
            "Anda telah meminjam 3 buku.\n" +
            "Silakan kembalikan buku yang Anda pinjam terlebih dahulu\n" +
            "sebelum melakukan peminjaman kembali.",
            "Peminjaman Ditolak",
            JOptionPane.WARNING_MESSAGE
        );
        conn.rollback();
        return;
    }

    if (totalDipinjam + jumlah > 3) {
        JOptionPane.showMessageDialog(this,
            "❌ Jumlah peminjaman melebihi batas.\n\n" +
            "Maksimal peminjaman: 3 buku\n" +
            "Saat ini dipinjam: " + totalDipinjam,
            "Peminjaman Ditolak",
            JOptionPane.WARNING_MESSAGE
        );
        conn.rollback();
        return;
    }


    /* ===============================
       CEK STOK BUKU (LOCK)
    =============================== */
    ps = conn.prepareStatement(
        "SELECT stok FROM buku WHERE buku_id = ? FOR UPDATE"
    );
    ps.setInt(1, bukuId);
    rs = ps.executeQuery();

    if (!rs.next()) {
        JOptionPane.showMessageDialog(this, "❌ Buku tidak ditemukan!");
        conn.rollback();
        return;
    }

    int stok = rs.getInt("stok");
    if (stok < jumlah) {
        JOptionPane.showMessageDialog(this,
            "❌ Stok tidak mencukupi!\nStok tersedia: " + stok);
        conn.rollback();
        return;
    }

    /* ===============================
       INSERT PEMINJAMAN
    =============================== */
    ps = conn.prepareStatement(
        "INSERT INTO peminjaman " +
        "(kode_peminjaman, user_id, buku_id, jumlah_pinjam, " +
        "tanggal_pinjam, tanggal_kembali, status, denda, created_at, created_by) " +
        "VALUES (?, ?, ?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 'pending', 0, NOW(), ?)"
    );
    ps.setString(1, kodePeminjaman);
    ps.setInt(2, userId);
    ps.setInt(3, bukuId);
    ps.setInt(4, jumlah);
    ps.setInt(5, userId);
    ps.executeUpdate();

    /* ===============================
       UPDATE STOK
    =============================== */
    ps = conn.prepareStatement(
        "UPDATE buku SET stok = stok - ? WHERE buku_id = ?"
    );
    ps.setInt(1, jumlah);
    ps.setInt(2, bukuId);
    ps.executeUpdate();

    conn.commit(); // ✅ SIMPAN

    getData(); // refresh tabel

    JOptionPane.showMessageDialog(this,
        "✅ Peminjaman berhasil\n" +
        "Kode Peminjaman: " + kodePeminjaman +
        "\nTotal dipinjam sekarang: " + (totalDipinjam + jumlah));

    jumlah_dipinjam.setText("");

} catch (Exception e) {
    try {
        if (conn != null) conn.rollback();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    JOptionPane.showMessageDialog(this, "❌ Error: " + e.getMessage());
} finally {
    try {
        if (rs != null) rs.close();
        if (ps != null) ps.close();
        if (conn != null) conn.setAutoCommit(true);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmb_cari;
    private javax.swing.JComboBox<String> cmb_kategori;
    private javax.swing.JLabel id_user;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField judul;
    private javax.swing.JTextField jumlah_dipinjam;
    private javax.swing.JTextField nama;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables

}
