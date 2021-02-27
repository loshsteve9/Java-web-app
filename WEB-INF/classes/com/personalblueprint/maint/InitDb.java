/*
 * InitDb.java
 *
 * Created on July 27, 2003, 11:00 PM
 */

package com.wolfconsultinggroup.personalblueprint.maint;

import java.sql.*;
/**
 *
 * @author  bdunlop
 */
public class InitDb {
    
    /** Creates a new instance of InitDb */
    public InitDb() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InitDb db = new InitDb();
        db.init();
    }
    public void init() {
        Connection conn = null;        
        java.sql.PreparedStatement stmt = null; 
        ResultSet rs = null;
        try {
            try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                } catch (ClassNotFoundException ex) {
                    System.out.println("ClassNotFoundException: " + ex.getMessage());
                } catch (InstantiationException ex) {
                    System.out.println("InstantiationException: " + ex.getMessage());
                } catch (IllegalAccessException ex) {
                    System.out.println("InstantiationException: " + ex.getMessage());
                }
            
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/clay?user=clay");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage()); 
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode()); 
        }
        if(conn != null)
        {
            try {
                    stmt = conn.prepareStatement("INSERT INTO t_pbi_questions (question) VALUES (?)"); 
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode()); 
            }
            
                for(int i = 1; i < 141; i++)
                {
                    try {
                        Integer q = new Integer(i);
                        String s = "This is question: " + q.toString();
                        stmt.clearParameters();
                        stmt.setString(1, s);
                        stmt.executeUpdate();
                    }catch (SQLException ex) {
                        System.out.println("SQLException: " + ex.getMessage()); 
                        System.out.println("SQLState: " + ex.getSQLState()); 
                        System.out.println("VendorError: " + ex.getErrorCode()); 
                        }
                }
            }
        }
}
