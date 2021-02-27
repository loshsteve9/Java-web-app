/*
 * CF_ManageStatsServlet.java
 *
 * Created on July 10, 2004, 10:27 AM
 */

package com.personalblueprint.servlets;

import java.io.*;
import java.util.*;
import java.sql.*;
import java.net.*;

import com.personalblueprint.helperClasses.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *
 * @author  Glenn Hummel
 * @version
 */
public class CF_ManageSubscalesServlet_RedoAnswers extends HttpServlet {
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }    
    /** Creates new CF_DisplayQuestionsBean */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {        
        
        String whichTest = request.getParameter("whichTest");
        String cancel = request.getParameter("cancel");
        
        if(cancel != null) { 
            RequestDispatcher rd = request.getRequestDispatcher("CF_ControllerServlet?action=showAdminIndex");
            rd.forward(request, response);
              return;              
        }

        String update = request.getParameter("change");

        Hashtable subscaleData = new Hashtable();
        loadSubscales(subscaleData, whichTest);

        if("nothing".equals(update)) {            
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>" + whichTest + " Subscale Info</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div align='center'>");
            out.println("<table width='800' cellspacing='0' cellpadding='5'>");
            out.println("<tr><td><b>Subscale No</b></td><td><b>Subscale Label</b></td><td><b>Subscale Mean</b></td><td><b>Subscale SD</b></td></tr>");
            out.println("<form name='modifySubscales' method='post' action='/servlet/com/wolfconsultinggroup/personalblueprint/servlets/CF_ControllerServlet?action=modifySubscales&whichTest=" + whichTest + "&change=something'>");

            for (int i = 1; i <= subscaleData.size(); i++)
            {
                Integer key = new Integer(i);
                CF_Subscale subscale = (CF_Subscale) subscaleData.get(key);
                int subscaleid = subscale.getSubscaleId();
                String subscaleName = subscale.getSubscaleName();
                double subscaleMean = subscale.getSubscaleMean();
                double subscaleSD = subscale.getSubscaleStandardDev();
                StringBuffer href = new StringBuffer();
                href.append("<tr><td bgcolor='#D3D3D3'>" + subscaleid + "</td><td>" + subscaleName + "</td><td>" + subscaleMean + "</td><td>" + subscaleSD + "</td></tr>");
                href.append("<tr><td bgcolor='#BBFFFF' align='right'>&rarr;</td>");
                href.append("<td><textarea name='" + subscaleid + "Name' rows='1' cols='60' wrap='virtual'></textarea></td>");
                href.append("<td><input type='text' name='" + subscaleid + "Mean' value='' size='6' maxlength='6' /></td>");
                href.append("<td><input type='text' name='" + subscaleid + "SD' value='' size='6' maxlength='6' /></td></tr>");
                out.println(href.toString());
            }
            out.println("</table>");
            out.println("<table>");
            out.println("<tr><td class='submitButton'><input class='submitButton' type='submit' name='submit' value='Submit Changes' /></td>");
            out.println("<td class='submitButton' colspan='3'><input class='submitButton' type='submit' name='cancel' value='No Changes - Cancel' /></td></tr>");
            out.println("<tr><td></td></tr>");
            out.println("</table>");
            out.println("</form>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            out.close();
        }else {
            
            Hashtable editedData = new Hashtable();
            int subscales = subscaleData.size();
            getEditedData(subscales, editedData, request);
            
             if(editedData.size() > 0) {
                updateSubscaleData(subscales, editedData, whichTest);
             }            
            
            RequestDispatcher rd = request.getRequestDispatcher("CF_ControllerServlet?action=modifySubscales&whichTest=" + whichTest + "&change=nothing");
            rd.forward(request, response);
              return;
        }
    }

    public void loadSubscales(Hashtable subscaleData, String whichTest)
    {
        Connection conn = null;
        java.sql.Statement stmt = null;
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
            
          conn = DriverManager.getConnection("jdbc:mysql://dc2-mysql-05.kattare.com/pbitest?user=wcgypbp&password=m7%4wiZJdq3");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage()); 
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode()); 
        }
        
         if(conn != null)
        {
            try {
                String select = new String("SELECT t_" + whichTest + "_subscales.subscaleID, subscaleMean, subscaleStandardDev, subscaleName from t_" + whichTest + "_subscales");
                stmt = conn.createStatement(); 
                rs = stmt.executeQuery(select); 
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode());
            } finally { 
                if (rs != null) {                    
                    try {
                        if(rs.first())
                        {
                            int i = 0;                            
                            while (!rs.isAfterLast())
                            {
                              CF_Subscale subscale = new CF_Subscale(rs.getString("subscaleName"), rs.getDouble("subscaleMean"), rs.getDouble("subscaleStandardDev"), rs.getInt("subscaleId"));
                              Integer key = new Integer(++i);
                              subscaleData.put(key, subscale);
                              rs.next();
                            }
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
                        } catch (SQLException sqlEx) { // ignore 
                        } 
                        stmt = null; 
                    } 
                } // end if (rs!=null)    
            } // end finally
        }// end if (conn != null)                    
    }
    
    public void getEditedData(int numSubscales, Hashtable editedFields, HttpServletRequest request)
    {
        for(int i = 1; i <= numSubscales; i++) {
            String nameValue = String.valueOf(i) + "Name";
            String meanValue = String.valueOf(i) + "Mean";
            String SDValue = String.valueOf(i) + "SD";
            String newSubscaleName = request.getParameter(nameValue);
            String newSubscaleMean = request.getParameter(meanValue);
            String newSubscaleSD = request.getParameter(SDValue);
            if(!newSubscaleName.equals("")|| !newSubscaleMean.equals("") || !newSubscaleSD.equals(""))
            {
                Integer key = new Integer(i);
                String newSubscaleData = (newSubscaleName + "," + newSubscaleMean + "," + newSubscaleSD);
                editedFields.put(key, newSubscaleData);
            }
        }
    }
    
    private void updateSubscaleData(int subscaleLength, Hashtable editedData, String whichTest)
    {
       ArrayList preparedStatement = new ArrayList();
       for(int i = 0; i < subscaleLength; i++) {
           String statement = "UPDATE t_" + whichTest + "_subscales SET ";
           Integer key = new Integer(i + 1);
           String edits = (String) editedData.get(key);
           if(edits != null) {
                int firstComma = edits.indexOf(",");
                int secondComma = edits.indexOf(",", firstComma + 1);
                String name = edits.substring(0, firstComma);
                String mean = edits.substring(firstComma + 1, secondComma);
                String sd = edits.substring(secondComma + 1);
                if(!"".equals(name) && !"".equals(mean) && !"".equals(sd)) {
                    statement += "subscaleName='" + name + "', subscaleMean='" + mean + "', subscaleStandardDev='" + sd + "'";
                }else if(!"".equals(name) && !"".equals(mean) && "".equals(sd)) {
                    statement += "subscaleName='" + name + "', subscaleMean='" + mean + "'";
                }else if(!"".equals(name) && "".equals(mean) && !"".equals(sd)) {
                    statement += "subscaleName='" + name + "', subscaleStandardDev='" + sd + "'";
                }else if(!"".equals(name) && "".equals(mean) && "".equals(sd)) {
                    statement += "subscaleName='" + name + "'";
                }else if("".equals(name) && !"".equals(mean) && !"".equals(sd)) {
                    statement += "subscaleMean='" + mean + "', subscaleStandardDev='" + sd + "'";
                }else if("".equals(name) && !"".equals(mean) && "".equals(sd)) {
                    statement += "subscaleMean='" + mean + "'";
                }else if("".equals(name) && "".equals(mean) && !"".equals(sd)) {
                    statement += "subscaleStandardDev='" + sd + "'";
                }
                statement += " WHERE subscaleid=" + key;               
                preparedStatement.add(statement);
           }
       }
        Connection conn = null;
        java.sql.PreparedStatement stmt = null;
        String error = null;
        
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
            conn = DriverManager.getConnection("jdbc:mysql://dc2-mysql-05.kattare.com/pbitest?user=wcgypbp&password=m7%4wiZJdq3");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage()); 
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode()); 
        }
        
        if(conn != null)
        {
            for(int m = 0; m < preparedStatement.size(); m++) {
                String parameters = (String) preparedStatement.get(m);
                try {
                    stmt = conn.prepareStatement(parameters);
                } catch (SQLException ex) {
                    System.out.println("SQLException: " + ex.getMessage()); 
                    System.out.println("SQLState: " + ex.getSQLState()); 
                    System.out.println("VendorError: " + ex.getErrorCode());
                }

                try {
                    stmt.executeUpdate();

                } catch (SQLException ex) {
                    System.out.println("SQLException: " + ex.getMessage()); 
                    System.out.println("SQLState: " + ex.getSQLState()); 
                    System.out.println("VendorError: " + ex.getErrorCode()); 

                }finally {
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
    
    
     public int pbireverseValues[] = {1, 2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 15, 16, 
                        17, 18, 19, 20, 23, 24, 26, 27, 29, 30, 32, 
                        35, 36, 37, 38, 40, 41, 42, 43, 46, 48, 49, 
                        51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 
                        64, 65, 66, 68, 69, 72, 74, 75, 77, 78, 79, 
                        81, 82, 83, 84, 87, 89, 90, 91, 92, 93, 94, 
                        95, 96, 97, 98, 100, 101, 103, 105, 107, 108, 
                        109, 110, 112, 113, 115, 116, 117, 119, 120, 121, 
                        122, 123, 124, 125, 126, 127, 129, 130, 133, 
                        134, 135, 136, 137, 139};
                        
    public int ibtreverseValues[] = { 4, 5, 11, 14, 15, 16, 17, 20, 22, 25, 29, 30, 31, 32, 
                      35, 36, 37, 39, 40, 41, 43, 44, 45, 48, 52, 54, 56, 
                      57, 58, 59, 60, 61, 63, 64, 65, 68, 70, 74, 77, 80, 83, 
                      85, 86, 87, 88, 91, 92, 93, 94, 95, 97, 98, 99, 100 };                        
                        
    public void changeAnswers(String whichTest)
    {
        Connection conn = null;
        java.sql.PreparedStatement stmt = null;
        String error = null;
        
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
            conn = DriverManager.getConnection("jdbc:mysql://dc2-mysql-05.kattare.com/pbitest?user=wcgypbp&password=m7%4wiZJdq3");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage()); 
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode()); 
        }
        
        if(conn != null) {
            
            try {
                stmt = conn.prepareStatement("ALTER TABLE t_" + whichTest + "_tests ADD newAnswer INTEGER NOT NULL");
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode());
            }

            try {
                stmt.executeUpdate();

            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode()); 

            }finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException sqlEx) { // ignore }
                        stmt = null; 
                    }
                }
            }

            try {
                stmt = conn.prepareStatement("UPDATE t_" + whichTest + "_tests SET newAnswer=? WHERE questionid=? AND answer=?");
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode());
            }

            try {
                if("ibt".equals(whichTest)) {
                    for(int i = 0; i < ibtreverseValues.length; i ++) {
                        int questNum = ibtreverseValues[i];
                        String questId = Integer.toString(questNum);
                        for(int k = 1; k <= 5; k ++) {
                            String newAnswers[] = {"5", "4", "3", "2", "1"};
                            String oldAnswer = Integer.toString(k);
                            String newAnswer = newAnswers[k -1];
                            stmt.clearParameters();
                            stmt.setString(1, newAnswer);
                            stmt.setString(2, questId);
                            stmt.setString(3, oldAnswer);
                            stmt.executeUpdate();
                        }
                    }
                }else if("pbi".equals(whichTest)) {
                    for(int i = 0; i < pbireverseValues.length; i ++) {
                        int questNum = pbireverseValues[i];
                        String questId = Integer.toString(questNum);
                        for(int k = 1; k <= 5; k ++) {
                            String newAnswers[] = {"5", "4", "3", "2", "1"};
                            String oldAnswer = Integer.toString(k);
                            String newAnswer = newAnswers[k -1];
                            stmt.clearParameters();
                            stmt.setString(1, newAnswer);
                            stmt.setString(2, questId);
                            stmt.setString(3, oldAnswer);
                            stmt.executeUpdate();
                        }
                    }
                }


            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode()); 

            }finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException sqlEx) {
                        stmt = null; 
                    }
                }
            }
        }
    }

    public void replaceAnswers(String whichTest)
    {
        Connection conn = null;
        java.sql.PreparedStatement stmt = null;
        String error = null;
        
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
            conn = DriverManager.getConnection("jdbc:mysql://dc2-mysql-05.kattare.com/pbitest?user=wcgypbp&password=m7%4wiZJdq3");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage()); 
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode()); 
        }
        
        if(conn != null) {

            try {
                stmt = conn.prepareStatement("UPDATE t_" + whichTest + "_tests SET answer = newAnswer WHERE newAnswer > 0");
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode());
            }

            try {
                stmt.executeUpdate();

            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode()); 

            }finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException sqlEx) {
                        stmt = null; 
                    }
                }
            }

            try {
                stmt = conn.prepareStatement("ALTER TABLE t_" + whichTest + "_tests DROP COLUMN newAnswer");
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode());
            }

            try {
                stmt.executeUpdate();

            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode()); 

            }finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException sqlEx) {
                        stmt = null; 
                    }
                }
            }
        }
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
}
