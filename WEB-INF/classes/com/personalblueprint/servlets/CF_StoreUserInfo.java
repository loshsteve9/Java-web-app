/*
 * CF_StoreUserInfo.java
 *
 * Created on February 19, 2004, 7:31 AM
 */

package com.personalblueprint.servlets;
import com.personalblueprint.beans.*;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  gchrt
 * @version
 */
public class CF_StoreUserInfo extends HttpServlet {
    
    /** Initializes the servlet.
     */
    
    private String error = null;
    private ServletContext context = null;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        context = config.getServletContext();
    }
    
    /** Destroys the servlet.
     */
    @Override
    public void destroy() {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        HttpSession session = request.getSession();
        if(session == null)
            return;
        
        error = null;
        boolean addTo = true;
        
        CF_UserInfoBean user = (CF_UserInfoBean) session.getAttribute("user");
        CF_UserInfoBean tempUser = (CF_UserInfoBean) session.getAttribute("tempUser");
        int referringPersonNum = 0;
        int referringPersonType = 0;
        String userid = "";
        
        if(tempUser == null) {
            tempUser = (CF_UserInfoBean) session.getAttribute("user");
            addTo = false;
        }
        if(tempUser == null) {
            return;
        }
        
        if(user != null) {
            referringPersonNum = user.getPersonNum();
            referringPersonType = user.getPersonType();
        }
        
        String tempUserid = tempUser.getUserid();
        String tempUserType = (String) session.getAttribute("newUserType");
        
        Connection conn = null;
        java.sql.PreparedStatement stmt = null;
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            }
            catch (ClassNotFoundException ex) {
                System.out.println(new java.util.Date());
                System.out.println("ClassNotFoundException: " + ex.getMessage());
                error = "ClassNotFoundException: " + ex.getMessage()  + "\n" + "CF_WriteUserInfo - 78";
                String url = "CF_ControllerServlet?action=databaseFailure&error=" + error;
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
            }
            catch (InstantiationException ex) {
                System.out.println(new java.util.Date());
                System.out.println("InstantiationException: " + ex.getMessage());
                error = "InstantiationException: " + ex.getMessage()  + "\n" + "CF_WriteUserInfo - 78";
                String url = "CF_ControllerServlet?action=databaseFailure&error=" + error;
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
            }
            catch (IllegalAccessException ex) {
                System.out.println(new java.util.Date());
                System.out.println("IllegalAccessException: " + ex.getMessage());
                error = "IllegalAccessException: " + ex.getMessage()  + "\n" + "CF_WriteUserInfo - 78";
                String url = "CF_ControllerServlet?action=databaseFailure&error=" + error;
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
            }
            
        conn = DriverManager.getConnection("jdbc:mysql://dc2-mysql-05.kattare.com/pbitest?user=wcgypbp&password=m7%4wiZJdq3");
        conn.setAutoCommit(false);
        }
        catch (SQLException ex) {
             System.out.println(new java.util.Date());
             System.out.println("StoreUserInfo, setAutoCommit. SQLException: " + ex.getMessage());
             System.out.println("SQLState: " + ex.getSQLState());
             System.out.println("VendorError: " + ex.getErrorCode());
             String url = "CF_ControllerServlet?action=databaseFailure&error=" + error;
             RequestDispatcher rd = request.getRequestDispatcher(url);
               rd.forward(request, response);
        }

        if(conn != null){
            if(writeUser(tempUser, conn, stmt, tempUserType, user)) {
                try {
                    conn.commit();
                }
                catch(SQLException ex) {
                    System.out.println(new java.util.Date());
                    System.out.println("StoreUserInfo, commit. SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
                // a client of Wolf Consulting
                if(referringPersonNum == 2) {
                    RequestDispatcher rd = request.getRequestDispatcher("CF_ControllerServlet?action=showWolfCGnames");
                    rd.forward(request, response);
                }
                else if("NewRegister".equals(tempUserType)) {
                    session.setAttribute("user", tempUser);
                    session.setAttribute("tempUser", null);
                    session.setAttribute("returnIndex", "processBk_OnlineClient");
                    
                    RequestDispatcher rd = request.getRequestDispatcher("CF_ControllerServlet?action=runNewTest&whichTest=pbi");
                    rd.forward(request, response);
                }
                // this is an onSite participant
                else if(referringPersonType == 6) {
                    RequestDispatcher rd = request.getRequestDispatcher("CF_ControllerServlet?action=showUserIndex&onsite=yes");
                    rd.forward(request, response);
                }
                else {
                    if(addTo) {
                        tempUser = null;
                        session.setAttribute("tempUser", null);
                    }
                    RequestDispatcher rd = request.getRequestDispatcher("CF_ControllerServlet?action=thankyou");
                    rd.forward(request, response);
                }
            }
            else {
                try {
                    conn.rollback();
                }
                catch(SQLException ex) {
                    System.out.println(new java.util.Date());
                    System.out.println("StoreUserInfo, conn.rollback try. SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
                
                String url = "CF_ControllerServlet?action=databaseFailure&error=writeUser failed";
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
            }             
        }
        else {
           RequestDispatcher rd = request.getRequestDispatcher("CF_ControllerServlet?action=logout");
             rd.forward(request, response);
        }
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(CF_StoreUserInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CF_StoreUserInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(CF_StoreUserInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** Returns a short description of the servlet.
     * @return 
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    private boolean writeUser(CF_UserInfoBean tempUser, Connection conn, java.sql.PreparedStatement stmt, String userType, CF_UserInfoBean user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        boolean bRet = false;
        String tempUserid = tempUser.getUserid();
        String tempUserPW = tempUser.getPassword();
        String hashedTempUserPW = CF_PasswordHandler.createHash(tempUserPW);
        
        try {
            stmt = conn.prepareStatement("INSERT INTO t_users (userid, pass, ibtTestComplete, pbiTestComplete) VALUES (?,?,?,?)");
        }
        catch (SQLException ex) {
            System.out.println(new java.util.Date());
            System.out.println("StoreUserInfo, writeUser, prepareStatement for setting userid. SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode());
            return bRet;
        }

        try {
            stmt.clearParameters();
            stmt.setString(1, tempUserid);
            stmt.setString(2, hashedTempUserPW);
            stmt.setInt(3, 0);
            stmt.setInt(4, 0);
            stmt.executeUpdate();
        }
        catch (SQLException ex) {
            System.out.println(new java.util.Date());
            System.out.println("StoreUserInfo, writeUser, execute update on userid. SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return bRet;
        }

        java.sql.Statement stmt1 = null;
        ResultSet rs = null;

        try {
            stmt1 = conn.createStatement();
            rs = stmt1.executeQuery("SELECT MAX(rowid) AS rowid FROM t_users");
        }
        catch (SQLException ex) {
            System.out.println(new java.util.Date());
            System.out.println("StoreUserInfo, writeUser, pulling MAX(rowid). SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return bRet;

        }
        finally {
            if (rs != null) {
                try {
                    rs.first();
                    tempUser.setPersonNum(rs.getInt("rowid"));
                }
                catch  (SQLException ex) {
                    System.out.println(new java.util.Date());
                    System.out.println("StoreUserInfo, writeUser, tempUser.setPersonNum. SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState()); 
                    System.out.println("VendorError: " + ex.getErrorCode());
                      return bRet;
                }
                try {
                    rs.close(); 
                }
                catch (SQLException sqlEx) { // ignore }
                    rs = null; 
                }
                if (stmt1 != null) { 
                    try { 
                        stmt1.close(); 
                    }
                    catch (SQLException sqlEx) { // ignore } 
                        stmt1 = null;
                    }
                }
            }
        }

        try {
            stmt = conn.prepareStatement("INSERT INTO t_persons (personNum, personType, lastName, firstName, middleInitial, displayName, presentingIssue, hoursWorked, isMarried, yearsMarried, gender, dateofBirth, education, occupation, employPosition, howLongEmployed, ethnicity, native, yearsInUSA, email, street, suite, city, state, zip, country, phoneAreaCode, phonePrefix, phoneNumber, phoneExtension, addedToSystemDate) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        }
        catch (SQLException ex) {
            System.out.println(new java.util.Date());
            System.out.println("StoreUserInfo, writeUser, prepareStatement for storing new user. SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return bRet;
        }

        try {
            stmt.clearParameters();
            stmt.setInt(       1, tempUser.getPersonNum());
            stmt.setInt(       2, tempUser.getPersonType());
            stmt.setString(    3, tempUser.getPerson().getLastName());
            stmt.setString(    4, tempUser.getPerson().getFirstName());
            stmt.setString(    5, tempUser.getPerson().getMiddleInitial());
            stmt.setString(    6, tempUser.getPerson().getFirstName() + " " + tempUser.getPerson().getMiddleInitial() + " " + tempUser.getPerson().getLastName());
            stmt.setString(    7, tempUser.getPerson().getPresentingIssue());
            stmt.setString(    8, tempUser.getPerson().getHoursWorked());
            stmt.setString(    9, tempUser.getPerson().getIsMarried());
            stmt.setString(   10, tempUser.getPerson().getYearsMarried());
            stmt.setString(   11, tempUser.getPerson().getGender());
            String iDate = tempUser.getPerson().getBirthDate(); 
            
            if("".equals(iDate)) {
                stmt.setDate( 12, null);                    
            }
            else {
                java.sql.Date birthDate = java.sql.Date.valueOf(iDate); 
                stmt.setDate( 12, birthDate);
            }
            stmt.setString(   13, tempUser.getPerson().getEducation());
            stmt.setString(   14, tempUser.getPerson().getOccupation());
            stmt.setString(   15, tempUser.getPerson().getEmployPosition());
            stmt.setString(   16, tempUser.getPerson().getHowLongEmployed());
            stmt.setString(   17, tempUser.getPerson().getEthnicity());
            stmt.setString(   18, tempUser.getPerson().getIsNative());
            stmt.setString(   19, tempUser.getPerson().getYearsInUSA());
            stmt.setString(   20, tempUser.getPerson().getEmail());
            stmt.setString(   21, tempUser.getPerson().getStreet());
            stmt.setString(   22, tempUser.getPerson().getSuiteOrApt());
            stmt.setString(   23, tempUser.getPerson().getCity());
            stmt.setString(   24, tempUser.getPerson().getStateCode());
            stmt.setString(   25, tempUser.getPerson().getZipOrPostalCode());
            stmt.setString(   26, tempUser.getPerson().getCountry());
            stmt.setString(   27, tempUser.getPerson().getAreaCode());
            stmt.setString(   28, tempUser.getPerson().getPrefix());
            stmt.setString(   29, tempUser.getPerson().getNumber());
            stmt.setString(   30, tempUser.getPerson().getExtension());
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(31, ts);
            stmt.executeUpdate();
        }
        catch (SQLException ex) {
            System.out.println(new java.util.Date());
            System.out.println("StoreUserInfo, writeUser, update database with new user. SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode());
            return bRet;

        }
        finally {
            bRet = true;
            if (stmt != null) {
                try {
                    stmt.close();
                }
                catch (SQLException sqlEx) { // ignore }
                    stmt = null; 
                }
            }
        }
        return bRet;
    }
}
