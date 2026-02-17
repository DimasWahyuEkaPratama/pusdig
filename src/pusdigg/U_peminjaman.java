/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pusdigg;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.prompt.PromptSupport;


/**
 *
 * @author HP
 */
public class U_peminjaman extends javax.swing.JFrame {

    /**
     * Creates new form U_peminjaman
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
    String nomor = session.getU_nomor();
    String usernamee = session.getU_username();
    
    public U_peminjaman() {
        initComponents();
        conn = koneksi.koneksi.koneksiDB();   // ✅ koneksi dulu!

        // Maximize frame
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);  

                // PLACEHOLDER
        PromptSupport.setPrompt("Masukkan Judul", txt_cari);
        id_user.setText(id);
        no.setText(nomor);
        name.setText(usernamee);


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
                obj[12] = "";


                model.addRow(obj);
            }

            jTable1.setModel(model);
            jTable1.getColumnModel().getColumn(12)
                    .setCellRenderer(new ButtonRendererPanel());
            jTable1.getColumnModel().getColumn(12)
                    .setCellEditor(new ButtonEditorPanel(new JCheckBox(), jTable1));
            jTable1.getColumnModel().getColumn(12).setPreferredWidth(160);


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
// Renderer untuk menampilkan 2 tombol di kolom aksi
public class ButtonRendererPanel extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        JPanel panel = new JPanel(new java.awt.FlowLayout(FlowLayout.CENTER, 5, 0));
        JButton btnDetail = new JButton("Detail");
        JButton btnPinjam = new JButton("Pinjam");

        btnDetail.setFocusable(false);
        btnPinjam.setFocusable(false);

        panel.add(btnDetail);
        panel.add(btnPinjam);

        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
        }

        return panel;
    }
}

// Editor untuk tombol interaktif
public class ButtonEditorPanel extends DefaultCellEditor {
    private JPanel panel;
    private JButton btnDetail, btnPinjam;
    private JTable table;

    public ButtonEditorPanel(JCheckBox checkBox, JTable table) {
        super(checkBox);
        this.table = table;

        panel = new JPanel(new java.awt.FlowLayout(FlowLayout.CENTER, 5, 0));
        btnDetail = new JButton("Detail");
        btnPinjam = new JButton("Pinjam");

        btnDetail.setFocusable(false);
        btnPinjam.setFocusable(false);

        panel.add(btnDetail);
        panel.add(btnPinjam);

        // Aksi tombol Detail
        btnDetail.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) showDetail(row);
        });

        // Aksi tombol Pinjam
        btnPinjam.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) pinjamBuku(row);
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    // ================= Detail Buku =================
    private void showDetail(int i) {
        String judul = table.getValueAt(i, 3).toString();
        String penulis = table.getValueAt(i, 4).toString();
        String penerbit = table.getValueAt(i, 5).toString();
        String kategori = table.getValueAt(i, 2).toString();
        String stok = table.getValueAt(i, 7).toString();
        String deskripsi = table.getValueAt(i, 8).toString();
        String rak = table.getValueAt(i, 9).toString();
        String path = table.getValueAt(i, 11).toString();

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

        JPanel panelDetail = new JPanel(new BorderLayout(10,10));
        panelDetail.add(lblImg, BorderLayout.WEST);
        panelDetail.add(sp, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(
                null,
                panelDetail,
                "Detail Buku",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    // ================= Pinjam Buku =================
    private void pinjamBuku(int row) {
        try {
            int userId = Integer.parseInt(session.getU_id());
            int bukuId = Integer.parseInt(table.getValueAt(row, 0).toString());
            int stok = Integer.parseInt(table.getValueAt(row, 7).toString());

            // Input jumlah pinjam
            String jumlahStr = JOptionPane.showInputDialog(
                    null,
                    "Masukkan jumlah yang ingin dipinjam\nStok tersedia: " + stok,
                    "Pinjam Buku",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (jumlahStr == null || jumlahStr.isEmpty()) return;

            int jumlah;
            try {
                jumlah = Integer.parseInt(jumlahStr);
                if (jumlah > stok) {
                    JOptionPane.showMessageDialog(null, "❌ Stok tidak cukup!");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "❌ Jumlah tidak valid!");
                return;
            }

            Connection conn = koneksi.koneksi.koneksiDB();
            conn.setAutoCommit(false); // TRANSACTION

            // ================= KODE PEMINJAMAN =================
            String today = new java.text.SimpleDateFormat("yyMMdd").format(new java.util.Date());
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
                lastNumber = Integer.parseInt(lastKode.substring(11, 14));
            }

            String kodePeminjaman = "PJM-" + today + "-" + String.format("%03d", lastNumber + 1) + "-" + userId;

            // ================= CEK TOTAL BUKU DIPINJAM =================
            PreparedStatement psCek = conn.prepareStatement(
                    "SELECT IFNULL(SUM(jumlah_pinjam),0) AS total " +
                            "FROM peminjaman " +
                            "WHERE user_id = ? AND status IN ('pending','dipinjam')"
            );
            psCek.setInt(1, userId);
            ResultSet rsCek = psCek.executeQuery();

            int totalDipinjam = 0;
            if (rsCek.next()) totalDipinjam = rsCek.getInt("total");

            if (totalDipinjam >= 3 || totalDipinjam + jumlah > 3) {
                JOptionPane.showMessageDialog(null,
                        "❌ Jumlah peminjaman melebihi batas.\nSaat ini dipinjam: " + totalDipinjam,
                        "Peminjaman Ditolak",
                        JOptionPane.WARNING_MESSAGE
                );
                conn.rollback();
                return;
            }

            // ================= TANGGAL KEMBALI =================
            java.sql.Date tglPinjam = new java.sql.Date(System.currentTimeMillis());
            java.sql.Date tglKembali;

            // Cek jika user pengunjung, kembali hari yang sama
            if (session.getU_status().equalsIgnoreCase("pengunjung")) {
                tglKembali = tglPinjam;
            } else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(tglPinjam);
                cal.add(Calendar.DAY_OF_MONTH, 7); // misal maksimal 7 hari untuk member biasa
                tglKembali = new java.sql.Date(cal.getTimeInMillis());
            }

            // ================= INSERT PEMINJAMAN =================
            PreparedStatement psInsert = conn.prepareStatement(
                    "INSERT INTO peminjaman " +
                            "(kode_peminjaman, user_id, buku_id, jumlah_pinjam, status, tanggal_pinjam, tanggal_kembali, created_by) " +
                            "VALUES (?, ?, ?, ?, 'pending', ?, ?, ?)"
            );
            psInsert.setString(1, kodePeminjaman);
            psInsert.setInt(2, userId);
            psInsert.setInt(3, bukuId);
            psInsert.setInt(4, jumlah);
            psInsert.setDate(5, tglPinjam);
            psInsert.setDate(6, tglKembali);
            psInsert.setString(7, session.getU_id());

            psInsert.executeUpdate();

            // ================= UPDATE STOK BUKU DI DATABASE =================
            PreparedStatement psStok = conn.prepareStatement(
                    "UPDATE buku SET stok = stok - ? WHERE buku_id = ?"
            );
            psStok.setInt(1, jumlah);
            psStok.setInt(2, bukuId);
            psStok.executeUpdate();

            conn.commit();

            JOptionPane.showMessageDialog(null,
                    "✅ Berhasil meminjam buku: " + table.getValueAt(row, 3).toString(),
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Update stok di JTable
            ((DefaultTableModel) table.getModel()).setValueAt(stok - jumlah, row, 7);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}

//    public class ButtonRenderer extends DefaultTableCellRenderer {
//    @Override
//    public Component getTableCellRendererComponent(
//            JTable table, Object value, boolean isSelected,
//            boolean hasFocus, int row, int column) {
//
//        JButton btn = new JButton("Detail");
//        btn.setFocusPainted(false);
//        return btn;
//    }
//}
// 
//    public class ButtonEditor extends DefaultCellEditor {
//
//        private JButton button;
//        private int row;
//        private JTable table;
//
//        public ButtonEditor(JCheckBox checkBox, JTable table) {
//            super(checkBox);
//            this.table = table;
//
//            button = new JButton("Detail");
//            button.setFocusPainted(false);
//
//            button.addActionListener(e -> showDetail());
//        }
//
//        private void showDetail() {
//            int i = table.getSelectedRow();
//            if (i == -1) return;
//
//            // ambil data dari tabel
//            String judul = table.getValueAt(i, 3).toString();
//            String penulis = table.getValueAt(i, 4).toString();
//            String penerbit = table.getValueAt(i, 5).toString();
//            String kategori = table.getValueAt(i, 2).toString();
//            String stok = table.getValueAt(i, 7).toString();
//            String deskripsi = table.getValueAt(i, 8).toString();
//            String rak = table.getValueAt(i, 9).toString();
//            String path = table.getValueAt(i, 11).toString();
//
//            // gambar
//            ImageIcon icon = new ImageIcon(path);
//            Image img = icon.getImage().getScaledInstance(180, 240, Image.SCALE_SMOOTH);
//            JLabel lblImg = new JLabel(new ImageIcon(img));
//
//            JTextArea txt = new JTextArea(
//                    "Judul     : " + judul +
//                    "\nPenulis  : " + penulis +
//                    "\nPenerbit : " + penerbit +
//                    "\nKategori : " + kategori +
//                    "\nStok     : " + stok +
//                    "\nRak      : " + rak +
//                    "\n\nDeskripsi:\n" + deskripsi
//            );
//            txt.setEditable(false);
//            txt.setWrapStyleWord(true);
//            txt.setLineWrap(true);
//
//            JScrollPane sp = new JScrollPane(txt);
//            sp.setPreferredSize(new Dimension(300, 200));
//
//            JPanel panel = new JPanel(new BorderLayout(10,10));
//            panel.add(lblImg, BorderLayout.WEST);
//            panel.add(sp, BorderLayout.CENTER);
//
//            JOptionPane.showMessageDialog(
//                    null,
//                    panel,
//                    "Detail Buku",
//                    JOptionPane.PLAIN_MESSAGE
//            );
//        }
//
//        @Override
//        public Component getTableCellEditorComponent(
//                JTable table, Object value, boolean isSelected, int row, int column) {
//            this.row = row;
//            return button;
//        }
//    }

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

    void pilihData() {
        int i = jTable1.getSelectedRow();
        if (i == -1) return;

        // PK & FK
        bukuId = Integer.parseInt(model.getValueAt(i, 0).toString());
        kategoriId = Integer.parseInt(model.getValueAt(i, 1).toString());
        //judul.setText(model.getValueAt(i, 3).toString());
        // text field
    }
    
    void bersih() {
        txt_cari.setText("");
        //judul.setText("");
        //jumlah_dipinjam.setText("");
        cmb_kategori.setSelectedItem(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        name = new javax.swing.JLabel();
        no = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        txt_cari = new javax.swing.JTextField();
        cmb_kategori = new javax.swing.JComboBox<>();
        cmb_cari = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        id_user = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        name.setText("FullName");
        name.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                nameComponentResized(evt);
            }
        });

        no.setText("jLabel2");

        jButton1.setText("Selesai & Keluar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });

        cmb_cari.setText("Cari");
        cmb_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_cariActionPerformed(evt);
            }
        });

        jButton3.setText("Refresh");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        id_user.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(no, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_user)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txt_cari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmb_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cmb_cari)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(name)
                            .addComponent(id_user))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(no))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cmb_cari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmb_kategori, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 3, Short.MAX_VALUE))))
                .addGap(4, 4, 4)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nameComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_nameComponentResized
        // TODO add your handling code here:
    }//GEN-LAST:event_nameComponentResized

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        getData();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_cariActionPerformed

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
        }        // TODO add your handling code here:
    }//GEN-LAST:event_cmb_cariActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
            // 1️⃣ Konfirmasi logout
    int confirm = JOptionPane.showConfirmDialog(null, 
        "Apakah Anda Sudah Selesai?", "Selesai", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        // 2️⃣ Bersihkan session (jika pakai session)
        session.logout(); // contoh jika ada method clear() di session class

        // 3️⃣ Tutup window saat ini
        this.dispose(); // menutup JFrame saat ini

        // 4️⃣ Buka window login
        login Login = new login();
        Login.setVisible(true);
    }         // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(U_peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(U_peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(U_peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(U_peminjaman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new U_peminjaman().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmb_cari;
    private javax.swing.JComboBox<String> cmb_kategori;
    private javax.swing.JLabel id_user;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel name;
    private javax.swing.JLabel no;
    private javax.swing.JTextField txt_cari;
    // End of variables declaration//GEN-END:variables
}
