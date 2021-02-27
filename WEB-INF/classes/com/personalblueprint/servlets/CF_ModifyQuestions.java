/*
 * CF_DisplayQuestionsBean.java
 *
 * Created on February 24, 2004, 5:45 PM
 */

package com.personalblueprint.servlets;

import com.personalblueprint.helperClasses.*;

import java.net.*;
import java.util.*;
import java.sql.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  gchrt
 */
public class CF_ModifyQuestions extends HttpServlet {
    
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
        String keepGoing = request.getParameter("keepGoing");
        
        if(cancel != null || "stop".equals(keepGoing))
        { 
            RequestDispatcher rd = request.getRequestDispatcher("CF_ControllerServlet?action=showAdminIndex");
            rd.forward(request, response);
              return;
        }
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        Hashtable allQuestions = new Hashtable();
        Hashtable editedQuestions = new Hashtable();
        
        loadQuestions(allQuestions, whichTest);
        
        int amtAllQuestions = allQuestions.size();
        int lastQuestion;
        int iStart = 0;
        
        if(request.getParameter("lastQuestion") != null)
        {
            lastQuestion = Integer.parseInt(request.getParameter("lastQuestion"));
            loadEditedQuestions(lastQuestion, editedQuestions, request);
            
        }else
        {
            lastQuestion = 0;
        }
        
        if(editedQuestions.size() > 0)
        {
            writeQuestions(editedQuestions);
        }

        if("onward".equals(keepGoing))
        {
            if(lastQuestion == 0)
            {
                iStart = 1;
                lastQuestion = 20;
                keepGoing = "onward";
            }
            else if((lastQuestion + 20) < amtAllQuestions)
            {
                iStart = lastQuestion + 1;
                lastQuestion += 20;
                keepGoing = "onward";
            }
            else if((lastQuestion + 20) >= amtAllQuestions)
            {
                iStart = lastQuestion + 1;
                lastQuestion += (amtAllQuestions - lastQuestion);
                keepGoing = "stop";
            }
            
            out.println("<html>");
            out.println("<head>");
            out.println("<title>" + whichTest + " Questions</title>");
            out.println("<link rel='stylesheet' type='text/css' href='/pbi_stylesheet.css' />");
            out.println("</head>");
            out.println("<body>");
            out.println("<div align='center'>");
            out.println("<table width='800' cellspacing='0' cellpadding='0'>");
            out.println("<tr><td><img src='/images/spacer.gif' width='20' height='1' border='0' /></td><td><img src='/images/spacer.gif' width='170' height='1' border='0' /></td><td><img src='/images/spacer.gif' width='15' height='1' border='0' /></td><td><img src='/images/spacer.gif' width='595' height='1' border='0' /></td></tr>");
            out.println("<tr><td colspan='2'><b>Subscale</b></td><td colspan='2'><b>Questions " + iStart + " through " + lastQuestion + " of " + amtAllQuestions + "</b></td></tr>");
            out.println("<tr><td>ID</td><td align='center'>Name</td><td align='center'>Num</td><td>Text</td></tr>");
            out.println("<form name='modifyQuestions' method='post' action='" + request.getContextPath() + "/servlet/com/personalblueprint/servlets/CF_ControllerServlet?action=modifyTexts&whichText=questions&whichTest=" + whichTest + "&lastQuestion=" + lastQuestion + "&keepGoing=" + keepGoing + "'>");
            
            for (int i = iStart; i <= lastQuestion; i++)
            {
                Integer key = new Integer(i);
                CF_Question question = (CF_Question) allQuestions.get(key);
                int subscaleid = question.getSubscaleId();
                String subscale = question.getSubscaleName();
                String currentQuestion = question.getQuestion();
                Integer questionid = question.getQuestionId();
                StringBuffer href = new StringBuffer();
                href.append("<tr><td bgcolor='#D3D3D3' align='center'>" + subscaleid + "</td><td bgcolor='#00FFCC'>" + subscale + "</td>");
                href.append("<td bgcolor='#FFFAFA' align='center'><b>" + questionid + "</b></td><td bgcolor='#F5F5F5'>" + currentQuestion + "</td></tr>");
                href.append("<tr><td><img src='/images/spacer.gif' border='0' /></td>");
                href.append("<td bgcolor='#BBFFFF' colspan='2' align='right'>Write new question here &rarr;</td>");
                href.append("<td><textarea name='" + questionid + "' rows='1' cols='80' wrap='virtual'></textarea></td></tr>");
                out.println(href.toString());
            }
            out.println("<tr><td class='submitButton' colspan='4' align='center'><input class='submitButton' type='submit' name='submit' value='Submit Changes and/or Move to Next Group of Questions' /></td></tr>");
            out.println("<tr><td colspan='4'><img src='/images/spacer.gif' height='5' border='0' /></td></tr>");
            out.println("<tr><td class='submitButton' colspan='4' align='center'><input class='submitButton' type='submit' name='cancel' value='No Changes - Cancel Operation' /></td></tr>");
            out.println("<tr><td colspan='4'><img src='/images/spacer.gif' height='5' border='0' /></td></tr>");
            out.println("</form>");
            out.println("</table>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
            out.close();
        }
    }
    
    public void loadEditedQuestions(int lastQuestion, Hashtable editedQuestions, HttpServletRequest request)
    {
        int loopCount = 0;
        for(int i = (lastQuestion - 19); i <= (lastQuestion); i++) {
            String questid = String.valueOf(i);
            String quest = request.getParameter(questid);
            if(!quest.equals(""))
            {
                Integer key = new Integer(++loopCount);
                String question = (questid + ":" + quest);
                editedQuestions.put(key, question);
            }
        }
    }
    
    public void loadQuestions(Hashtable allQuestions, String whichTest)
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
                String select = new String("SELECT t_" + whichTest + "_subscales.subscaleId, subscaleName, questionId, question from t_" + whichTest + "_subscales, t_" + whichTest + "_questions where t_" + whichTest + "_subscales.subscaleId and t_" + whichTest + "_questions.subscaleId = t_" + whichTest + "_subscales.subscaleId;");
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
                              CF_Subscale subscale = new CF_Subscale(rs.getString("subscaleName"), rs.getInt("t_" + whichTest + "_subscales.subscaleId"));
                              String string = subscale.toString();                                
                              CF_Question question = new CF_Question(rs.getString("question"), rs.getInt("questionId"), subscale);                              
                              Integer key = new Integer(++i);
                              allQuestions.put(key, question);
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
    
    private void writeQuestions(Hashtable editedQuestions)
    {
        Connection conn = null;
        java.sql.PreparedStatement stmt = null;
        String error = null;
        int amtQuests = editedQuestions.size();
        int id = 0;
        
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
                stmt = conn.prepareStatement("UPDATE t_pbi_questions SET question = ? WHERE questionid = ?"); 
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode());
            }
            
            try
            {
                for(int i = 1; i <= amtQuests; i++)
                {
                    Integer key = new Integer(i);
                    String quest_id = (String) editedQuestions.get(key);
                    int colonpos = quest_id.indexOf(":");
                    String newQuest = quest_id.substring(colonpos + 1);
                    id = Integer.parseInt(quest_id.substring(0, colonpos));
                    stmt.clearParameters();
                    stmt.setString(1, newQuest);
                    stmt.setInt(2, id);                
                    stmt.executeUpdate();
                }
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
