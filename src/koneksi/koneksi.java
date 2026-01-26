/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package koneksi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;//memanggil mysql jdbc

/**
 *
 * @author dppra
 */
public class koneksi {
     public static Connection koneksiDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");//memanggil driver JDBC
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/perpusdigital", "root", "");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    return null;
    }    
}
