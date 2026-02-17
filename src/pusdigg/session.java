/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pusdigg;

/**
 *
 * @author dppra
 */
public class session {
    private static String U_id;
    private static String U_username;
    private static String U_nomor;
    private static String U_status;

    public static String getU_id() {
        return U_id;
    }

    public static void setU_id(String U_id) {
        session.U_id = U_id;
    }

    public static String getU_username() {
        return U_username;
    }

    public static void setU_username(String U_username) {
        session.U_username = U_username;
    }
    
     public static String getU_nomor() {
        return U_nomor;
    }

    public static void setU_nomor(String U_nomor) {
        session.U_nomor = U_nomor;
    }
    
    public static String getU_status() {
        return U_status;
    }

    public static void setU_status(String U_status) {
        session.U_status = U_status;
    }

    public static void logout() {
        U_id = null;
        U_username = null;
        U_nomor = null;
        U_status = null;
    }
}
