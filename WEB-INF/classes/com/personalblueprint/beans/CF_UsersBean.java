/*
 * CF_UsersBean.java
 *
 * Created on June 17, 2003, 7:12 PM
 */

package com.personalblueprint.beans;

import com.personalblueprint.helperClasses.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.http.*;
import java.sql.*;

//import CF_UserInfo;

/**
 *
 * @author  bdunlop
 */
public class CF_UsersBean extends Object implements java.io.Serializable {
    
    public boolean isUserValid(String userid, String password,  HttpSession session) throws NoSuchAlgorithmException, InvalidKeySpecException {
        boolean isValid = false;
        CF_UserInfoBean user = new CF_UserInfoBean();
        Connection conn = null;        
        java.sql.Statement stmt = null; 
        ResultSet rs = null;
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            }
            catch (ClassNotFoundException ex) {
                System.out.println("ClassNotFoundException: " + ex.getMessage());
            }
            catch (InstantiationException ex) {
                System.out.println("InstantiationException: " + ex.getMessage());
            }
            catch (IllegalAccessException ex) {
                System.out.println("InstantiationException: " + ex.getMessage());
            }
            
            conn = DriverManager.getConnection("jdbc:mysql://dc2-mysql-05.kattare.com/pbitest?user=wcgypbp&password=m7%4wiZJdq3");
        }
        catch (SQLException ex) {
            System.out.println(new java.util.Date() + " LoginHandlerServlet validLogonUser initial connect. SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode()); 
        } 
        if(conn != null) {
            try {
                stmt = conn.createStatement(); 
                rs = stmt.executeQuery("SELECT * FROM t_users, t_persons WHERE t_users.userid = '" + userid + "' AND t_users.rowid = t_persons.personNum;");
            }
            catch (SQLException ex) {
                System.out.println(new java.util.Date() + " LoginHandlerServlet validLogonUser select users. SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode()); 
            }
            finally {
                try { 
                    if (rs.next()) {
                        CF_Person person = new CF_Person();
                        person.setDisplayName(rs.getString("t_persons.DisplayName"));
                        int personType = rs.getInt("personType");
                        person.setPersonType(personType);
                        person.setFirstName(rs.getString("t_persons.FirstName"));
                        person.setLastName(rs.getString("t_persons.LastName"));
                        person.setStreet(rs.getString("t_persons.Street"));
                        person.setCity(rs.getString("t_persons.City"));
                        person.setStateCode(rs.getString("t_persons.State"));
                        person.setZipOrPostalCode(rs.getString("t_persons.Zip"));
                        person.setAreaCode(rs.getString("t_persons.phoneAreaCode"));
                        person.setPrefix(rs.getString("t_persons.phonePrefix"));
                        person.setNumber(rs.getString("t_persons.phoneNumber"));
                        person.setEmail(rs.getString("t_persons.email"));
                        
                        user = new CF_UserInfoBean(rs.getString("t_users.userid"), rs.getString("pass"), rs.getInt("t_users.pbiTestComplete"), rs.getInt("t_users.ibtTestComplete"), rs.getInt("t_users.rowid"), person);
                        session.setAttribute("user", user);
                        
                        // now we have a user so compare the password
                        String dbPass = user.getPassword();
                        boolean passCheck = CF_PasswordHandler.validatePassword(password, dbPass);
                        if(passCheck) {
                            isValid = true;
                        }
                    }
                     // result set is empty not a valid user - isValid remains false
                    else {
                        session.setAttribute("user", user);
                    }
                }
                catch (SQLException ex) {
                    System.out.println(new java.util.Date() + " LoginHandlerServlet validLogonUser() assessing result set.SQLException: " + ex.getMessage()); 
                    System.out.println("SQLState: " + ex.getSQLState()); 
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
                 try {
                     rs.close(); 
                 }
                 catch (SQLException sqlEx) { // ignore }
                     rs = null;
                 }

                 if (stmt != null) { 
                     try { 
                         stmt.close(); 
                     }
                     catch (SQLException sqlEx) { // ignore }
                         stmt = null; 
                     }
                 }
             }
         }
        return isValid;
    }

    public boolean isUseridTaken(String userid) {
        
        boolean isUseridTaken = false;
        
        Connection conn = null;        
        java.sql.Statement stmt = null; 
        ResultSet rs = null;
        String pulledUserid = "";
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            }
            catch (ClassNotFoundException ex) {
                System.out.println("ClassNotFoundException: " + ex.getMessage());
            }
            catch (InstantiationException ex) {
                System.out.println("InstantiationException: " + ex.getMessage());
            }
            catch (IllegalAccessException ex) {
                System.out.println("InstantiationException: " + ex.getMessage());
            }
            
            conn = DriverManager.getConnection("jdbc:mysql://dc2-mysql-05.kattare.com/pbitest?user=wcgypbp&password=m7%4wiZJdq3");
        }
        catch (SQLException ex) {
            System.out.println(new java.util.Date());
            System.out.println("CF_UsersBean, isUserIdTaken, connecting to database. SQLException: " + ex.getMessage()); 
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode());
        } 
         if(conn != null) {
            try {
                stmt = conn.createStatement(); 
                rs = stmt.executeQuery("SELECT userid AS pulledId FROM t_users WHERE t_users.userid = '" + userid + "' ;");
            }
            catch (SQLException ex) {
                System.out.println(new java.util.Date());
                System.out.println("CF_UsersBean, isUseridTaken, executing query. SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode()); 
            }
            finally {
                if (rs != null) {
                    try {
                        if(rs.first()) {
                            pulledUserid = rs.getString("pulledId");
                        }
                    }
                    catch (SQLException ex) {
                            System.out.println(new java.util.Date());
                            System.out.println("CF_UsersBean, isUseridTaken, obtaining pulledId from result set. SQLException: " + ex.getMessage()); 
                            System.out.println("SQLState: " + ex.getSQLState()); 
                            System.out.println("VendorError: " + ex.getErrorCode());
                    }                    
                }
                if ("".equals(pulledUserid)) {
                    return isUseridTaken;
                }
                else {
                    isUseridTaken = true;
                }                
                try {
                    rs.close(); 
                }
                catch (SQLException sqlEx) { // ignore }
                    rs = null;
                }
                
                if (stmt != null) { 
                    try { 
                        stmt.close(); 
                    }
                    catch (SQLException sqlEx) { // ignore }
                        stmt = null; 
                    }
                }
            }
         }    
        return isUseridTaken;
    }
}
