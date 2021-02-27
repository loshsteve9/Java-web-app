/*
 * CF_TestManagerBean.java
 *
 * Created on July 21, 2003, 6:02 PM
 */

package com.personalblueprint.beans;

import com.personalblueprint.helperClasses.*;

import javax.servlet.*;
import java.beans.*;
import java.util.*;
import java.sql.*;
import java.io.*;

public class CF_TestManagerBean extends Object implements Serializable {

    /** Holds value of property question. */
    private CF_Question question = null;    
    
    private int totalSubscales;
    
    private int totalQuestions = 0;
    
    private int currentSubscale = 0;
    
    private int currentQuestion = 0;
    
    private Hashtable questionList = null;
    
    private int lastQuestion = 1;
    
    private int startQuestion = 1;
    
    /** Holds value of property answers. */
    private Hashtable answers = null;
    
    /** Holds value of property testId. */
    private Integer testId;
    
    private String dbConnection = "";
    
    public CF_TestManagerBean() {
    }
        
    public CF_TestManagerBean(String selectedTest) {
        init(selectedTest);        
        this.totalSubscales = totalSubscales;
        this.totalQuestions = totalQuestions;
    }

    /** Getter for property question.
     * @return Value of property question.
     * Increments question number value
     *
     */
    public String getQuestion() {
        Integer key = new Integer(this.lastQuestion);
        CF_Question aQuestion = (CF_Question) questionList.get(key);
        this.question = aQuestion;
        this.lastQuestion++;        
        return question.getQuestion();       
    }    
    
    private void init(String selectedTest) {
        String theSubscaleTable = "t_" + selectedTest + "_subscales";
        String theQuestionTable = "t_" + selectedTest + "_questions";
        String theTestidTable = "t_" + selectedTest + "_testid";
        questionList = new Hashtable();
        Connection conn = null;
        java.sql.Statement stmt = null;
        java.sql.Statement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
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
            System.out.println(new java.util.Date() + " TestManagerBean init initial connect. SQLException: " + ex.getMessage()); 
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode()); 
        } 
        if(conn != null) {
            if(!"pbi_survey".equals(selectedTest)) { // pull out standard test questions and subscales
                try {
                    String select = new String("SELECT count(subscaleid) AS sumOfSubscales FROM " + theSubscaleTable + ";");
                    String select2 = new String("SELECT count(question) AS sumOfQuestions FROM " + theQuestionTable + ";");
                    stmt = conn.createStatement();
                    stmt2 = conn.createStatement();
                    rs = stmt.executeQuery(select);
                    rs2 = stmt2.executeQuery(select2);
                }
                catch (SQLException ex) {
                    System.out.println(new java.util.Date() + " TestManagerBean init finding subscale or question count SQLException: " + ex.getMessage()); 
                    System.out.println("SQLState: " + ex.getSQLState()); 
                    System.out.println("VendorError: " + ex.getErrorCode()); 
                } 
                finally {
                    if (rs != null) {
                        try {
                            if(rs.first()) {
                                totalSubscales = rs.getInt("sumOfSubscales");
                            }
                        }
                        catch (SQLException ex) {
                            System.out.println(new java.util.Date() + " TestManagerBean init totalSubscales rs. SQLException: " + ex.getMessage());
                            System.out.println("SQLState: " + ex.getSQLState()); 
                            System.out.println("VendorError: " + ex.getErrorCode()); 
                        }
                        try {
                            rs.close(); 
                        }
                        catch (SQLException sqlEx) { // ignore 
                        } 
                        rs = null; 
                        if (stmt != null) { 
                            try { 
                                stmt.close(); 
                            }
                            catch (SQLException sqlEx) { // ignore 
                            }
                            stmt = null; 
                        }                    
                    }// end rs if
                    if (rs2 != null) {
                        try {
                            if(rs2.first()) {
                                totalQuestions = rs2.getInt("sumOfQuestions");
                            }
                        }
                        catch (SQLException ex) {
                            System.out.println(new java.util.Date() + " TestManagerBean init totalQuestions rs. SQLException: " + ex.getMessage());
                            System.out.println("SQLState: " + ex.getSQLState()); 
                            System.out.println("VendorError: " + ex.getErrorCode()); 
                        }
                        try {
                            rs2.close(); 
                        }
                        catch (SQLException sqlEx) { // ignore 
                        } 
                        rs2 = null; 
                        if (stmt2 != null) { 
                            try { 
                                stmt2.close(); 
                            }
                            catch (SQLException sqlEx) { // ignore 
                            }
                            stmt2 = null; 
                        }                    
                    }// end rs2 if 
                }// end finally

                try {
                    String select = new String("SELECT " + theSubscaleTable + ".subscaleId, subscaleName, questionId, question FROM " + theSubscaleTable + ", " + theQuestionTable + " WHERE " + theQuestionTable + ".subscaleId = " + theSubscaleTable + ".subscaleId order by questionId;");
                    stmt = conn.createStatement(); 
                    rs = stmt.executeQuery(select); 
                }
                catch (SQLException ex) {
                    System.out.println(new java.util.Date() + " TestManagerBean init pull questions statement. SQLException: " + ex.getMessage()); 
                    System.out.println("SQLState: " + ex.getSQLState()); 
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
                finally { 
                    if (rs != null) {                    
                        try {
                            if(rs.first()) {
                                int i = 0;                            
                                while (!rs.isAfterLast()) {                             
                                    CF_Subscale subscale = new CF_Subscale(rs.getString("subscaleName"), rs.getInt(theSubscaleTable + ".subscaleId"));
                                    String string = subscale.toString();
                                    CF_Question question = new CF_Question(rs.getString("question"), rs.getInt("questionId"), subscale);                              
                                    Integer key = new Integer(++i);
                                    questionList.put(key, question);
                                    rs.next();
                                }
                                //initialize this.question
                                Integer key = new Integer(1);
                                CF_Question q = (CF_Question) questionList.get(key);
                                this.question = q;
                            }
                        } 
                        catch (SQLException ex) {
                            System.out.println(new java.util.Date() + " TestManagerBean init pulled questions rs. SQLException: " + ex.getMessage());
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
                            catch (SQLException sqlEx) { // ignore 
                            } 
                            stmt = null; 
                        } 
                    } // end if (rs!=null)    
                } // end finally
            }
            else { // pull out just survey questions
                try {
                    String select = new String("SELECT * FROM " + theQuestionTable + " ORDER BY questionId;");
                    String select2 = new String("SELECT count(question) AS sumOfQuestions FROM " + theQuestionTable + ";");
                    stmt = conn.createStatement();
                    stmt2 = conn.createStatement();
                    rs = stmt.executeQuery(select);
                    rs2 = stmt2.executeQuery(select2);
                }
                catch (SQLException ex) {
                    System.out.println(new java.util.Date() + " TestManagerBean init pull survey questions statement. SQLException: " + ex.getMessage()); 
                    System.out.println("SQLState: " + ex.getSQLState()); 
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
                finally { 
                    if (rs != null) {                  
                        try {
                            if(rs.first()) {
                                int i = 0;                            
                                while (!rs.isAfterLast()) {    
                                    CF_Question question = new CF_Question(rs.getString("question"), rs.getInt("questionId"));
                                    Integer key = new Integer(++i);
                                    questionList.put(key, question);
                                    rs.next();
                                }
                                //initialize this.question
                                Integer key = new Integer(1);
                                CF_Question q = (CF_Question) questionList.get(key);
                                this.question = q;
                            }
                        }
                        catch (SQLException ex) {
                            System.out.println(new java.util.Date() + " TestManagerBean init pulled questions rs. SQLException: " + ex.getMessage());
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
                            catch (SQLException sqlEx) { // ignore 
                            } 
                            stmt = null; 
                        } 
                    } // end rs if
                    if (rs2 != null) {
                        try {
                            if(rs2.first()) {
                                totalQuestions = rs2.getInt("sumOfQuestions");
                            }
                        }
                        catch (SQLException ex) {
                            System.out.println(new java.util.Date() + " TestManagerBean init totalQuestions rs. SQLException: " + ex.getMessage());
                            System.out.println("SQLState: " + ex.getSQLState()); 
                            System.out.println("VendorError: " + ex.getErrorCode()); 
                        }
                        try {
                            rs2.close(); 
                        }
                        catch (SQLException sqlEx) { // ignore 
                        } 
                        rs2 = null; 
                        if (stmt2 != null) { 
                            try { 
                                stmt2.close(); 
                            }
                            catch (SQLException sqlEx) { // ignore 
                            }
                            stmt2 = null; 
                        }                    
                    }// end rs2 if    
                } // end finally
            }

          try {
               String select = new String("SELECT testId from " + theTestidTable + ";");
               stmt = conn.createStatement(); 
               rs = stmt.executeQuery(select); 
          }
          catch (SQLException ex) {
               System.out.println(new java.util.Date() + " TestManagerBean init pull testid statement. SQLException: " + ex.getMessage());
               System.out.println("SQLState: " + ex.getSQLState()); 
               System.out.println("VendorError: " + ex.getErrorCode()); 
          } 
          finally {
               if (rs != null) {
                   try {
                      if(rs.first()) {
                            this.testId = new Integer(rs.getInt("testid"));
                      }
                  }
                   catch (SQLException ex) {
                      System.out.println(new java.util.Date() + " TestManagerBean init pulled testid rs. SQLException: " + ex.getMessage());
                      System.out.println("SQLState: " + ex.getSQLState()); 
                      System.out.println("VendorError: " + ex.getErrorCode()); 
                  }
                  try {
                      rs.close(); 
                  }
                  catch (SQLException sqlEx) { // ignore 
                  } 
                  rs = null; 
                  if (stmt != null) { 
                      try { 
                          stmt.close(); 
                      }
                      catch (SQLException sqlEx) { // ignore 
                      }
                      stmt = null; 
                  }                    
              }// end if (rs != null)
          }// end finally
          
          // increment testid by one
          java.sql.PreparedStatement stmtW = null; 
          try {
              stmtW = conn.prepareStatement("Update " + theTestidTable + " set testId = ?;");
          } 
          catch (SQLException ex) {
              System.out.println(new java.util.Date() + " TestManagerBean init update testid statement. SQLException: " + ex.getMessage()); 
              System.out.println("SQLState: " + ex.getSQLState()); 
              System.out.println("VendorError: " + ex.getErrorCode()); 
          }
          try {
              if (stmtW != null) {
                stmtW.clearParameters();
                stmtW.setInt(1, this.testId + 1);
                int executeUpdate = stmtW.executeUpdate();
              }
          }
          catch (SQLException ex) {
              System.out.println(new java.util.Date() + " TestManagerBean init update testid. SQLException: " + ex.getMessage());
              System.out.println("SQLState: " + ex.getSQLState()); 
              System.out.println("VendorError: " + ex.getErrorCode()); 
          }
          
        }// end if (conn != null)          
    }// end init    
    

    public Hashtable getAnswers() {
        return this.answers;
    }
    
    public void setAnswers(String answer, int questNum) {
        if(this.answers == null) {
            this.answers = new Hashtable();
        }
        Integer key = new Integer(questNum);
        CF_Question q = (CF_Question) questionList.get(key);
        q.setAnswer(new Integer(answer));
        this.answers.put(key, q);
    }
    
    public boolean isAnotherQuestion() {
        if(this.lastQuestion >= this.totalQuestions) {
            return false;
        }
        else {
            return true;
        }
    }
    
    public String getQuestionID() {
        return this.question.getQuestionId().toString();
    }
    
    public void setStartQuestion(int newStartQuestion) {
        this.startQuestion = newStartQuestion;
    }
    
    public int getStartQuestion() {
        return this.startQuestion;
    }
    
    public int getSubscaleCount() {
        return totalSubscales;
    }
    public int getQuestionCount() {
        return this.totalQuestions;
    }
    
    public int getCurrentQuestion() {
        return this.lastQuestion;
    }
    
    public void setCurrentQuestion(int newLastQuestion) {
        this.lastQuestion = newLastQuestion;
    }

    public Integer getTestId() {
        return this.testId;
    }
    
    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public void writeTestResultsToDB(CF_UserInfoBean testTaker, String selectedTest) {
        String theTestTable = "t_" + selectedTest + "_tests";
        String whichCompletedTest = selectedTest + "TestComplete";
        int userNum = testTaker.getPersonNum();
        Connection conn = null;        
        java.sql.PreparedStatement stmt = null; 
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
            System.out.println(new java.util.Date() + " TestManagerBean writeTestResultsToDB connect. SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode()); 
        }
        if(conn != null) {
            try {
                stmt = conn.prepareStatement("INSERT INTO " + theTestTable + " (userid, testid, questionid, subscaleId, answer) VALUES (?,?,?,?,?)"); 
            }
            catch (SQLException ex) {
                System.out.println(new java.util.Date() + " TestManagerBean writeTestResultsToDB insert statement for questions. SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode()); 
            }
            for(int i = 1; i < questionList.size() + 1; i++) {
                try {
                    Integer key = new Integer(i);
                    CF_Question question = (CF_Question) questionList.get(key);

                    stmt.clearParameters();
                    stmt.setInt(1, userNum);
                    stmt.setInt(2, this.testId.intValue());
                    stmt.setInt(3, question.getQuestionId().intValue());
                    stmt.setInt(4, question.getSubscaleId());
                    stmt.setInt(5, question.getAnswer().intValue());
                    stmt.executeUpdate();
                }
                catch (SQLException ex) {
                    System.out.println(new java.util.Date() + " TestManagerBean writeTestResultsToDB inserting questions into db. SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState()); 
                    System.out.println("VendorError: " + ex.getErrorCode()); 
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                }
                catch (SQLException sqlEx) { // ignore }                        
                    stmt = null;
                }
            }       

            try {
                stmt = conn.prepareStatement("UPDATE t_users SET " + whichCompletedTest + " = ? WHERE rowid = ?"); 
            }
            catch (SQLException ex) {
                System.out.println(new java.util.Date() + " TestManagerBean update users statement. SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode()); 
            }
            try {
                stmt.clearParameters();
                stmt.setInt(1, 1);
                stmt.setInt(2, userNum);
                stmt.executeUpdate();
            }
            catch (SQLException ex) {
                System.out.println(new java.util.Date() + " TestManagerBean writeTestResultsToDB update completed " + whichCompletedTest + ". SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode()); 
            }
            finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    }
                    catch (SQLException sqlEx) { // ignore }                        
                        stmt = null;
                    }
                }
                if("pbi".equals(selectedTest)) {
                    testTaker.setPbiTestComplete(1);
                }
                else if("ibt".equals(selectedTest)) {
                    testTaker.setIbtTestComplete(1);
                }
            }
        }
    }
}
