/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pusdigg;
import java.awt.CardLayout;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;
import java.io.File;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Statement;


/**
 *
 * @author dppra
 */
public class buku extends javax.swing.JPanel {
    

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
    
   public buku() {
    initComponents();
    id_user.setText("SELAMAT DATANG"+id);
    username.setText("ANDA LOGIN SEBAGAI"+usernamee);
    
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

private String generateKodeBuku(Connection conn) throws SQLException {
    String kode = "BK-0001";
    String sql = "SELECT kode_buku FROM buku_item ORDER BY bukuitem_id DESC LIMIT 1";
    PreparedStatement ps = conn.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        String lastKode = rs.getString("kode_buku"); // BK-0009
        int num = Integer.parseInt(lastKode.substring(3));
        num++;
        kode = String.format("BK-%04d", num);
    }

    rs.close();
    ps.close();
    return kode;
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

    // text field
    txt_kategori.setText(model.getValueAt(i, 2).toString());
    judul.setText(model.getValueAt(i, 3).toString());
    penulis.setText(model.getValueAt(i, 4).toString());
    penerbit.setText(model.getValueAt(i, 5).toString());

    // tanggal
    java.util.Date tanggal = (java.util.Date) model.getValueAt(i, 6);
    tgl.setDate(tanggal);

    stok.setText(model.getValueAt(i, 7).toString());
    deskripsi.setText(model.getValueAt(i, 8).toString()); // ✅ benar
    rak.setText(model.getValueAt(i, 9).toString());       // ✅ benar

    // path gambar (hidden column)
    pathGambar = model.getValueAt(i, 11).toString();
    upload.setText(pathGambar);
}




    
    


        void bersih() {
            judul.setText("");
            penulis.setText("");
            penerbit.setText("");
            tgl.setDate(null);
            stok.setText("");
            rak.setText("");
            txt_kategori.setText("");
            deskripsi.setText("");
            upload.setText("");
            txt_cari.setText("");
        }
   
    public void setKategori(int id, String nama) {
    this.kategoriId = id;      // untuk DB
    txt_kategori.setText(nama); // untuk tampilan
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
        jLabel2 = new javax.swing.JLabel();
        judul = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        penulis = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        upload = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tgl = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        penerbit = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        deskripsi = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        stok = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        txt_cari = new javax.swing.JTextField();
        cmb_cari = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_kategori = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        rak = new javax.swing.JTextField();
        id_user = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        cmb_kategori = new javax.swing.JComboBox<>();

        jLabel2.setText("Judul Buku");

        judul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                judulActionPerformed(evt);
            }
        });

        jLabel3.setText("Penulis");

        jLabel4.setText("Penerbit");

        jLabel5.setText("Tahun Terbit");

        jLabel6.setText("Stok Buku");

        penerbit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penerbitActionPerformed(evt);
            }
        });

        jLabel7.setText("Rak Buku");

        deskripsi.setColumns(20);
        deskripsi.setRows(5);
        jScrollPane1.setViewportView(deskripsi);

        jLabel8.setText("Kategori ");

        jButton2.setText("Upload");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Simpan");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Update");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Refresh");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Delete");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        cmb_cari.setText("CARI");
        cmb_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_cariActionPerformed(evt);
            }
        });

        jLabel1.setText("Judul:");

        jLabel9.setText("Kategori:");

        txt_kategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kategoriActionPerformed(evt);
            }
        });

        jButton1.setText("pilih");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel10.setText("Deskripsi");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel8)
                        .addComponent(jLabel6)
                        .addComponent(jLabel5)
                        .addComponent(jLabel4)
                        .addComponent(jLabel3)
                        .addComponent(jLabel2)
                        .addComponent(penulis)
                        .addComponent(tgl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(penerbit)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addComponent(stok))
                    .addComponent(judul, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rak, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(upload, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4)
                                .addGap(18, 18, 18)
                                .addComponent(jButton5)
                                .addGap(18, 18, 18)
                                .addComponent(jButton6)))
                        .addGap(0, 343, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(34, 34, 34)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(cmb_kategori, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(0, 0, Short.MAX_VALUE)))))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(username)
                    .addComponent(id_user))
                .addGap(47, 47, 47))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(59, 59, 59)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(jLabel1)
                                .addComponent(jLabel9))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton1)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmb_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(id_user)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(username)
                        .addGap(36, 36, 36)
                        .addComponent(cmb_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(judul, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(penulis, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(penerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tgl, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stok, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rak, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(upload, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(jButton4)
                            .addComponent(jButton5)
                            .addComponent(jButton6))
                        .addGap(127, 127, 127))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void judulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_judulActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_judulActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
String format = "yyyy-MM-dd";
SimpleDateFormat fm = new SimpleDateFormat(format);
String Tanggal = fm.format(tgl.getDate());

Connection conn = null;
PreparedStatement pstBuku = null;
PreparedStatement pstEksemplar = null;
ResultSet rs = null;

try {
    conn = koneksi.koneksi.koneksiDB();
    conn.setAutoCommit(false); // TRANSACTION START

    // =======================
    // 1. INSERT BUKU
    // =======================
    String sqlBuku = "INSERT INTO buku (Judul, Penulis, Penerbit, Tahun_Terbit, stok, kategori_id, rak_buku, deskripsi, imgsampul, created_by, update_by) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    pstBuku = conn.prepareStatement(sqlBuku, Statement.RETURN_GENERATED_KEYS);

    pstBuku.setString(1, judul.getText());
    pstBuku.setString(2, penulis.getText());
    pstBuku.setString(3, penerbit.getText());
    pstBuku.setString(4, Tanggal);
    pstBuku.setInt(5, Integer.parseInt(stok.getText()));
    pstBuku.setInt(6, kategoriId);
    pstBuku.setString(7, rak.getText());
    pstBuku.setString(8, deskripsi.getText());
    pstBuku.setString(9, pathGambar);
    pstBuku.setString(10, session.getU_id());
    pstBuku.setString(11, session.getU_id());

    pstBuku.executeUpdate();

    // =======================
    // 2. AMBIL ID BUKU
    // =======================
    rs = pstBuku.getGeneratedKeys();
    int bukuId = 0;
    if (rs.next()) {
        bukuId = rs.getInt(1);
    }

    // =======================
    // 3. INSERT EKSEMPLAR
    // =======================
    int jumlahStok = Integer.parseInt(stok.getText());

    String sqlEksemplar = "INSERT INTO buku_item (buku_id, kode_buku, status, created_at) "
                        + "VALUES (?, ?, ?, ?)";

    pstEksemplar = conn.prepareStatement(sqlEksemplar);

    for (int i = 0; i < jumlahStok; i++) {
        String kodeBuku = generateKodeBuku(conn);

        pstEksemplar.setInt(1, bukuId);
        pstEksemplar.setString(2, kodeBuku);
        pstEksemplar.setString(3, "tersedia");
        pstEksemplar.setString(4, Tanggal);
        pstEksemplar.executeUpdate();
    }

    conn.commit(); // TRANSACTION SUCCESS
    JOptionPane.showMessageDialog(null, "Data buku & kode buku berhasil disimpan!");

} catch (Exception e) {
    try {
        if (conn != null) conn.rollback();
    } catch (SQLException ex) {}

    JOptionPane.showMessageDialog(null, "Gagal menyimpan data: " + e);

} finally {
    try {
        if (rs != null) rs.close();
        if (pstBuku != null) pstBuku.close();
        if (pstEksemplar != null) pstEksemplar.close();
        if (conn != null) conn.close();
    } catch (SQLException e) {}
}

// refresh
getData();
bersih();

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void penerbitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penerbitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_penerbitActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
    String format = "yyyy-MM-dd";
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    String tahunTerbit = sdf.format(tgl.getDate());

    String sql = "UPDATE buku SET " +
            "judul=?, penulis=?, penerbit=?, tahun_terbit=?, " +
            "stok=?, kategori_id=?, rak_buku=?, deskripsi=?, imgsampul=?, update_by=? " +
            "WHERE buku_id=?";

    pst = conn.prepareStatement(sql);

    pst.setString(1, judul.getText());
    pst.setString(2, penulis.getText());
    pst.setString(3, penerbit.getText());
    pst.setString(4, tahunTerbit);
    pst.setInt(5, Integer.parseInt(stok.getText()));
    pst.setInt(6, kategoriId);
    pst.setString(7, rak.getText());
    pst.setString(8, deskripsi.getText());
    pst.setString(9, pathGambar);   // gambar baru / lama
    pst.setString(10, session.getU_id());
    pst.setInt(11, bukuId);         // PK

    pst.executeUpdate();
    JOptionPane.showMessageDialog(null, "Data berhasil diupdate");

} catch (Exception e) {
    JOptionPane.showMessageDialog(null, "Gagal update: " + e.getMessage());
}

getData();
bersih();


    
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        bersih();
        getData();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

    int i = jTable1.getSelectedRow();
    int id = Integer.parseInt(model.getValueAt(i, 0).toString());

    int a = JOptionPane.showConfirmDialog(null,
            "Yakin ingin menghapus?",
            "Delete",
            JOptionPane.YES_NO_OPTION);

    if (a == JOptionPane.YES_OPTION) {
        try {
            String sql = "DELETE FROM buku WHERE Buku_id=?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Deleted");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    getData();
    bersih();

    
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
                                         
    JFileChooser fc = new JFileChooser();
fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
fc.setAcceptAllFileFilterUsed(false);
fc.addChoosableFileFilter(
    new javax.swing.filechooser.FileNameExtensionFilter(
        "Image Files", "jpg", "jpeg", "png"
    )
);

int result = fc.showOpenDialog(this);

if (result == JFileChooser.APPROVE_OPTION) {
    File file = fc.getSelectedFile();

    // cek ukuran file (maksimal 5 MB)
    long maxSize = 5 * 1024 * 1024; // 5 MB dalam byte

    if (file.length() > maxSize) {
        JOptionPane.showMessageDialog(
            this,
            "Ukuran file terlalu besar! Maksimal 5 MB.",
            "Error Upload",
            JOptionPane.ERROR_MESSAGE
        );
        return; // hentikan proses upload
    }

    // tampilkan nama file
    upload.setText(file.getName());

    // simpan path gambar
    pathGambar = file.getAbsolutePath();
}


        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

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

    private void txt_kategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_kategoriActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    JDialog dialog = new JDialog((java.awt.Frame) null, true);
    daftar_kategori panel = new daftar_kategori(this, dialog);

    dialog.setContentPane(panel);
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmb_kategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_kategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmb_kategoriActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmb_cari;
    private javax.swing.JComboBox<String> cmb_kategori;
    private javax.swing.JTextArea deskripsi;
    private javax.swing.JLabel id_user;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField judul;
    private javax.swing.JTextField penerbit;
    private javax.swing.JTextField penulis;
    private javax.swing.JTextField rak;
    private javax.swing.JTextField stok;
    private com.toedter.calendar.JDateChooser tgl;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_kategori;
    private javax.swing.JTextField upload;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables

}
