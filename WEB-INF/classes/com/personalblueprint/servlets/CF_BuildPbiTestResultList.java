/*
 * CF_BuildPbiTestResultList.java
 *
 * Created on September 14, 2007
 * @author  Glenn Hummel
 */

package com.personalblueprint.servlets;

import com.personalblueprint.beans.*;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class CF_BuildPbiTestResultList extends HttpServlet {
    
    /** Destroys the servlet.
     */
    public void destroy() {
    }    
        
    String testResultsFilePath = "d:/clientWork/wolfConsult/ROOT/pbiTestTakerList.txt";
    int currentNumQuestions = 180;
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        HttpSession session = request.getSession();        
        
        ArrayList takersArrayList = new ArrayList();
        Hashtable testTakerAnswers = new Hashtable();
        ArrayList pbiTestTakerList = new ArrayList();
        
        String pNum = new String();
        
        Connection conn = null;        
        Statement stmt1 = null;
        ResultSet allTestTakersRS = null;
        ResultSet pbiAnswersRS = null;
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
            System.out.println("CF_BuildPbiTestResultList, contacting database. SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode() + "\n");
        }
        if(conn != null) {
            try {
                String selectTestTakers = new String("SELECT personNum, DisplayName, addedToSystemDate FROM t_persons WHERE t_persons.personType = 4;");
                stmt1 = conn.createStatement();
                allTestTakersRS = stmt1.executeQuery(selectTestTakers);
            }
            catch (SQLException ex) {
                System.out.println(new java.util.Date());
                System.out.println("CF_BuildPbiTestResultList, executing query to get all pbi takers. SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode() + "\n");
            }
            finally {
                if (allTestTakersRS != null) {
                    try {
                        allTestTakersRS.first();
                        while (!allTestTakersRS.isAfterLast()) {
                            Hashtable currentPbiTaker = new Hashtable();
                            String testTakerPersonNum = new String(allTestTakersRS.getString("personNum"));
                            Integer key0 = new Integer(1);
                            currentPbiTaker.put(key0, testTakerPersonNum);
                            String testTakerName = new String(allTestTakersRS.getString("DisplayName"));
                            Integer key1 = new Integer(2);
                            currentPbiTaker.put(key1, testTakerName);
                            String testTakerDate = allTestTakersRS.getString("addedToSystemDate");
                            testTakerDate = testTakerDate.substring(0, 10);
                            Integer key2 = new Integer(3);
                            currentPbiTaker.put(key2, testTakerDate);
                            testTakerAnswers = getTestTakerAnswers(testTakerPersonNum);
                            int takersHashKey = 4; // start in 4th position of hashtable
                            int numCurrentTestAnswers = testTakerAnswers.size();
                            if(numCurrentTestAnswers > 0) {
                                for(int alist = 0; alist < numCurrentTestAnswers; alist ++) {
                                    Integer allTakersKey = new Integer(takersHashKey);
                                    Integer answersKey = new Integer(alist);
                                    String htItem = (String) testTakerAnswers.get(answersKey);
                                    currentPbiTaker.put(allTakersKey, htItem);
                                    takersHashKey ++;
                                }
                                takersArrayList.add(currentPbiTaker);
                                allTestTakersRS.next();
                            }
                            else {
                                allTestTakersRS.next();
                            }
                        }
                    }
                    catch (SQLException ex) {
                        System.out.println(new java.util.Date());
                        System.out.println("CF_BuildPbiTestResultList, parsing result set for person info. SQLException: " + ex.getMessage()); 
                        System.out.println("SQLState: " + ex.getSQLState()); 
                        System.out.println("VendorError: " + ex.getErrorCode() + "\n");
                    }
                    try {
                        allTestTakersRS.close(); 
                    }
                    catch (SQLException sqlEx) { // ignore } 
                        allTestTakersRS = null;
                    }                    
                    if (stmt1 != null) {
                        try { 
                            stmt1.close();
                            conn.close();
                        }
                        catch (SQLException sqlEx) { // ignore } 
                            stmt1 = null; 
                        }
                    }
                }
            }
         }
        session.setAttribute("takersArrayList", takersArrayList);
        String numQs = String.valueOf(currentNumQuestions);
        session.setAttribute("numPbiQuestions", numQs);
        writeTxtFile(takersArrayList);
        RequestDispatcher rd = request.getRequestDispatcher("CF_ControllerServlet?action=createPbiTestResultList&stint=printout");
        rd.forward(request, response);
          return;  
    }
    
    private Hashtable getTestTakerAnswers(String personNum) {
        
        Hashtable testTakerAnswers = new Hashtable();
        
        Connection connAns = null;
        Statement stmtAns = null;
        ResultSet pbiAnswersRS = null;
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
            
            connAns = DriverManager.getConnection("jdbc:mysql://dc2-mysql-05.kattare.com/pbitest?user=wcgypbp&password=m7%4wiZJdq3");
        }
        catch (SQLException ex) {
            System.out.println(new java.util.Date());
            System.out.println("CF_BuildPbiSurveyList::getTestTakerAnswers, contacting database. SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode() + "\n");
        }
        if(connAns != null) {
            try {
                String selectPbiAnswers = new String("SELECT testid, answer FROM t_pbi_tests WHERE userid =" + personNum + ";");
                stmtAns = connAns.createStatement();
                pbiAnswersRS = stmtAns.executeQuery(selectPbiAnswers);
            }
            catch (SQLException ex) {
                System.out.println(new java.util.Date());
                System.out.println("CF_BuildPbiTestResultList::getTestTakerAnswers, executing query for person info and pbi answers. SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode() + "\n");
            }
            finally {
                int answerHashKey = 0;
                try {
                    if(pbiAnswersRS.next()) {
                        pbiAnswersRS.first();
                        Integer akey = new Integer(answerHashKey);
                        String testid = (String) pbiAnswersRS.getString(1); // testid#
                        testTakerAnswers.put(akey, testid);
                        answerHashKey ++;
                        while (!pbiAnswersRS.isAfterLast()) {
                            String currentAnswer = (String) pbiAnswersRS.getString(2); // the result is composed of paired id and answer sets
                            akey = new Integer(answerHashKey);
                            testTakerAnswers.put(akey, currentAnswer);
                            answerHashKey ++;
                            pbiAnswersRS.next();
                        }
                    }
                }
                catch (SQLException ex) {
                    System.out.println(new java.util.Date());
                    System.out.println("CF_BuildPbiTestResultList::getTestTakerAnswers, parsing result set for pbi answers. SQLException: " + ex.getMessage()); 
                    System.out.println("SQLState: " + ex.getSQLState()); 
                    System.out.println("VendorError: " + ex.getErrorCode() + "\n");
                 }
                 try {
                     pbiAnswersRS.close(); 
                 }
                 catch (SQLException sqlEx) { // ignore } 
                     pbiAnswersRS = null; 
                 }
                 if (stmtAns != null) {
                     try {
                         stmtAns.close();
                         connAns.close();
                     }
                     catch (SQLException sqlEx) { // ignore } 
                         stmtAns = null;
                    }
                }
            }
        }
        return testTakerAnswers;
    }
    
    private void writeTxtFile(ArrayList list) {
        try {
            java.io.BufferedWriter writeTestResults = new java.io.BufferedWriter(new java.io.FileWriter(testResultsFilePath));

            StringBuffer lineOfInfo = new StringBuffer();
            int amtQs = currentNumQuestions;
            // write spreadsheet header
            lineOfInfo.append("PersonId#, Name, Date, TestId#, ");
            for(int a = 1; a <= currentNumQuestions; a ++) {
                lineOfInfo.append("Q" + a);
                if(a != currentNumQuestions) {
                    lineOfInfo.append(", ");
                }
                else {
                    lineOfInfo.append("\n");
                }
            }

            // fill in the rest of info
            for(int il = 0; il < list.size(); il ++) {
                Hashtable currLn = (Hashtable) list.get(il);            
                int infoSize = currLn.size();
                for(int wi = 1; wi <= infoSize; wi ++) {
                    Integer ikey = new Integer(wi);
                    lineOfInfo.append((String) currLn.get(ikey));
                    if(wi != infoSize) {
                        lineOfInfo.append(", ");
                    }
                    else {
                        lineOfInfo.append("\n");
                    }            
                }
            }

            writeTestResults.write(lineOfInfo.toString());
            writeTestResults.close();

         }
        catch (IOException e) {
            System.out.println(new java.util.Date());
            System.out.println("CF_BuildPbiTestResultList::writeTxt() BufferedWriter failed.\n");
            System.out.println(e);
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
