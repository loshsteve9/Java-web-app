/*
 * CF_TestsTaken.java
 *
 * Created on July 29, 2003, 10:50 AM
 */

package com.personalblueprint.beans;


import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 *
 * @author  bdunlop
 */
public class CF_TestsTaken extends Object implements java.io.Serializable {
    
    /** Holds value of property userTests. */
    private Hashtable userTests;
    
    /** Creates new CF_TestsTaken */
    public CF_TestsTaken(){
    }
        
    public CF_TestsTaken(String rank) {
        Init(rank);
    }
    
    private void Init(String rank) {
        String order = "";
        if(null != rank)switch (rank) {
            case "":
                order = "t_persons.addedToSystemDate DESC";
                break;
            case "lastName_desc":
                order = "t_persons.lastname DESC";
                break;
            case "lastName_asc":
                order = "t_persons.lastname";
                break;
            case "date_desc":
                order = "t_persons.addedToSystemDate DESC";
                break;
            case "date_asc":
                order = "t_persons.addedToSystemDate";
                break;
            default:
                order = "t_persons.addedToSystemDate DESC";
                break;
        }
        Connection conn = null;        
        java.sql.Statement stmt = null;
        ResultSet rs = null;
        try {
            try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                } catch (ClassNotFoundException ex) {
                    System.out.println("ClassNotFoundException: " + ex.getMessage());
                } catch (InstantiationException | IllegalAccessException ex) {
                    System.out.println("InstantiationException: " + ex.getMessage());
                }
            
            conn = DriverManager.getConnection("jdbc:mysql://dc2-mysql-05.kattare.com/pbitest?user=wcgypbp&password=m7%4wiZJdq3");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage()); 
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode()); 
        } 
         if(conn != null) {
            try {
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery("SELECT DISTINCT personNum, displayname, lastname, firstname, middleinitial, pbiTestComplete, ibtTestComplete, addedToSystemDate FROM t_users, t_persons WHERE t_persons.personType = '4' AND t_persons.personNum = t_users.rowId AND t_persons.addedtosystemdate >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) ORDER BY '" + order + "';");
            } catch (SQLException ex) {
                System.out.println("Selecting userInfo: SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode()); 
            } finally {
                userTests = new Hashtable();
                if (rs != null) {                                        
                    try {
                        rs.first();
                        int testCount = 0;
                        while (!rs.isAfterLast())
                        {
                            StringBuilder result = new StringBuilder();
                            String personLastName = rs.getString("lastname");
                            String firstname = rs.getString("firstname");
                            String middleinitial = rs.getString("middleinitial");
                            String personFirstName = "";
                            if("".equals(middleinitial)) {
                                personFirstName = firstname;
                            }else {
                                personFirstName = firstname + " " + middleinitial;
                            }
                            String personFullName = rs.getString("displayname");
                            Integer personId = rs.getInt("personNum");
                            String pNum = personId.toString();
                            Integer pbiComplete = rs.getInt("pbiTestComplete");
                            String pbiStatus = pbiComplete.toString();
                            Integer ibtComplete = rs.getInt("ibtTestComplete");
                            String ibtStatus = ibtComplete.toString();
                            java.sql.Date tempDate = rs.getDate("addedToSystemDate");
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy");
                            String dateEntered = sdf.format(tempDate);
                            result.append(personLastName);
                            result.append("%").append(personFirstName);
                            result.append("&").append(personFullName);
                            result.append(":").append(pNum);
                            result.append("$").append(pbiStatus);
                            result.append("*").append(ibtStatus);
                            result.append("#").append(dateEntered);
                            Integer key = ++testCount;
                            userTests.put(key, result.toString());
                            rs.next();
                        }
                    } catch (SQLException ex) {
                        System.out.println("SQLException: " + ex.getMessage()); 
                        System.out.println("SQLState: " + ex.getSQLState()); 
                        System.out.println("VendorError: " + ex.getErrorCode()); 
                    }
                    try {
                        rs.close(); 
                    } catch (SQLException sqlEx) { // ignore }                        
                        rs = null; 
                    }
                    
                    if (stmt != null) { 
                        try { 
                            stmt.close(); 
                        } catch (SQLException sqlEx) { // ignore }                            
                            stmt = null; 
                        } 
                    }    
                }
            }
         }
    }
        
    /** Getter for property userTests.
     * @return Value of property userTests.
     *
     */
    public Hashtable getUserTests() {
        return this.userTests;
    }
 
}
