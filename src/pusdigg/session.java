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

    public static void logout() {
        U_id = null;
        U_username = null;
    }
}
