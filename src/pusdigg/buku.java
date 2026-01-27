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
    private final DateFormat dfi = new SimpleDateFormat ("dd/mm/yyyy");
    Calendar hariini ;
    String pathGambar;
   private int kategoriId;
   private int bukuId;




    
    
   public buku() {
    initComponents();

    conn = koneksi.koneksi.koneksiDB();

    model = new DefaultTableModel() {
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 9) {
            return ImageIcon.class;
        }
        return Object.class;
    }
};

jTable1.setModel(model);
jTable1.setRowHeight(80);



   model.addColumn("Buku_Id");
model.addColumn("Kategori"); // ⬅ nama kategori
model.addColumn("Judul");
model.addColumn("Penulis");
model.addColumn("Penerbit");
model.addColumn("Tanggal_Terbit");
model.addColumn("Stok");
model.addColumn("Created_At");
model.addColumn("Deskripsi");
model.addColumn("Gambar");


    getData();
}

        
    void getData() {
    model.getDataVector().removeAllElements();
    model.fireTableDataChanged();

    try {
        String sql =
            "SELECT b.*, k.name_kategori " +
            "FROM buku b " +
            "JOIN kategori k ON b.kategori_id = k.kategori_id";

        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();

        while (rs.next()) {
            Object[] obj = new Object[11]; // ⬅ TAMBAH 1

            obj[0] = rs.getInt("buku_id");
            obj[1] = rs.getString("name_kategori"); // tampil nama
            obj[2] = rs.getString("judul");
            obj[3] = rs.getString("penulis");
            obj[4] = rs.getString("penerbit");
            obj[5] = rs.getDate("tahun_terbit");
            obj[6] = rs.getInt("stok");
            obj[7] = rs.getDate("created_at");
            obj[8] = rs.getString("deskripsi");

            String path = rs.getString("imgsampul");

            // gambar (buat tampilan)
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(60, 80, Image.SCALE_SMOOTH);
            obj[9] = new ImageIcon(img);

            // path asli (buat logic)
            obj[10] = path;

            model.addRow(obj);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
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

    // ⬅ SIMPAN ID UNTUK UPDATE & DELETE
    bukuId = Integer.parseInt(model.getValueAt(i, 0).toString());

    txt_kategori.setText(model.getValueAt(i, 1).toString());
    judul.setText(model.getValueAt(i, 2).toString());
    penulis.setText(model.getValueAt(i, 3).toString());
    penerbit.setText(model.getValueAt(i, 4).toString());

    java.util.Date tanggal = (java.util.Date) model.getValueAt(i, 5);
    tgl.setDate(tanggal);

    stok.setText(model.getValueAt(i, 6).toString());
    deskripsi.setText(model.getValueAt(i, 8).toString());

    // ⬅ AMBIL PATH ASLI
    pathGambar = model.getValueAt(i, 10).toString();
    upload.setText(new File(pathGambar).getName());
}

    
    


        void bersih() {
    judul.setText("");
    penulis.setText("");
    penerbit.setText("");
    tgl.setDate(null);
    stok.setText("");
    txt_kategori.setText("");
    deskripsi.setText("");
    upload.setText("");
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
        txt_kategori = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        stok = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txt_cari = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();

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

        jLabel7.setText("Deskripsi");

        deskripsi.setColumns(20);
        deskripsi.setRows(5);
        jScrollPane1.setViewportView(deskripsi);

        txt_kategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kategoriActionPerformed(evt);
            }
        });

        jLabel8.setText("Kategori ");

        jButton1.setText("CEK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

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
        jScrollPane2.setViewportView(jTable1);

        jButton7.setText("CARI");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel8)
                                .addComponent(jLabel6)
                                .addComponent(jLabel5)
                                .addComponent(jLabel4)
                                .addComponent(jLabel3)
                                .addComponent(jLabel2)
                                .addComponent(penulis)
                                .addComponent(upload)
                                .addComponent(tgl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addComponent(penerbit)
                                .addComponent(txt_kategori)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                                .addComponent(stok))
                            .addComponent(judul, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(61, 61, 61)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 23, Short.MAX_VALUE))
                                    .addComponent(jScrollPane2)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton2)
                                    .addComponent(jButton1))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(judul, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(penulis, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(penerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tgl, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(stok, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(upload, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void judulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_judulActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_judulActionPerformed

    private void txt_kategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_kategoriActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
 String pusdig = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat(pusdig);
        String Tanggal = String.valueOf(fm.format(tgl.getDate()));
        String Created_at = String.valueOf(fm.format(tgl.getDate()));
        
         try {
    String sql = "INSERT INTO buku (Judul, Penulis, Penerbit, Tahun_Terbit, stok, kategori_id, deskripsi, imgsampul) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    pst = conn.prepareStatement(sql);

    pst.setString(1, judul.getText()); 
    pst.setString(2, penulis.getText()); 
    pst.setString(3, penerbit.getText()); 
    pst.setString(4, Tanggal); 
    pst.setInt(5, Integer.parseInt(stok.getText())); 
    pst.setInt(6, kategoriId); 
    pst.setString(7, deskripsi.getText()); 
    pst.setString(8, pathGambar);
    pst.execute();
    JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
} catch (Exception e) { 
    JOptionPane.showMessageDialog(null, "Gagal menyimpan Data: " + e);
}
       
    
    getData();
    bersih();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void penerbitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penerbitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_penerbitActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            String sql = "UPDATE buku SET judul=?, penulis=?, penerbit=?, stok=?, kategori_id=? WHERE buku_id=?";
            pst = conn.prepareStatement(sql);

            pst.setString(1, judul.getText());
            pst.setString(2, penulis.getText());
            pst.setString(3, penerbit.getText());
            pst.setInt(4, Integer.parseInt(stok.getText()));
            pst.setInt(5, kategoriId);
            pst.setInt(6, bukuId); // ⬅ INI KUNCI UTAMA

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "UPDATE SUKSES");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    JDialog dialog = new JDialog((java.awt.Frame) null, true);
    daftar_kategori panel = new daftar_kategori(this, dialog);

    dialog.setContentPane(panel);
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);


        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

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

    // tampilkan nama file
    upload.setText(file.getName());

    // SIMPAN NAMA FILE KE DATABASE
    pathGambar = file.getAbsolutePath();
}

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        pilihData();
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
       
    
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea deskripsi;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField judul;
    private javax.swing.JTextField penerbit;
    private javax.swing.JTextField penulis;
    private javax.swing.JTextField stok;
    private com.toedter.calendar.JDateChooser tgl;
    private javax.swing.JTextField txt_cari;
    private javax.swing.JTextField txt_kategori;
    private javax.swing.JTextField upload;
    // End of variables declaration//GEN-END:variables
}
