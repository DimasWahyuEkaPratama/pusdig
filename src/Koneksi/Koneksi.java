/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Koneksi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;//memanggil mysql jdbc

/**
 *
 * @author dppra
 */
public class Koneksi {
     public static Connection KoneksiDB() {
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
