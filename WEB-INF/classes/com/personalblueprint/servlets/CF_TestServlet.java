/*
 * CF_TestServlet.java
 *
 * Created on July 27, 2003, 11:56 PM
 */

package com.personalblueprint.servlets;

import com.personalblueprint.beans.*;

import java.io.*;
import java.net.*;
import java.util.regex.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  bdunlop
 * @version
 */
public class CF_TestServlet extends HttpServlet {
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
            HttpSession session = request.getSession();
            CF_TestManagerBean testBean = (CF_TestManagerBean) session.getAttribute("testBean");
            // null pointer error debug
            if(testBean == null) {
                int debugPersonNum = 0;
                System.out.println(new java.util.Date());
                System.out.println("TestServlet failure");
                System.out.println("testBean is " + testBean);
                CF_UserInfoBean debugUser = (CF_UserInfoBean) session.getAttribute("user");
                if(debugUser == null) {
                    System.out.println("session.person is = " + debugPersonNum);
                }
                System.out.println("\n");
            }
            String selectedTest = (String) request.getAttribute("whichTest");
            String answersOnly = (String) session.getAttribute("answersOnly");
            String answer = "";
            StringBuffer badAnswers = new StringBuffer();
                              
            if("yes".equals(answersOnly)) {
                int totalQuestions = 0;
                Pattern wrongAnswers = Pattern.compile("[^1-5]");
                
                if("pbi".equals(selectedTest)) {
                    totalQuestions = 180;
                }
                else if("ibt".equals(selectedTest)) {
                    totalQuestions = 100;
                }
                
                for(int i = 1; i <= totalQuestions; i++) {
                    String rb = String.valueOf(i);
                    answer = request.getParameter(rb);
                    Matcher m = wrongAnswers.matcher(answer);
                    boolean foundMatch = m.find();
                    if("".equals(answer) || foundMatch){
                        String thisQuestion = String.valueOf(i);
                        badAnswers.append(thisQuestion + " ");
                        answer = "0";
                    }
                    testBean.setAnswers(answer,i);
                }

                String ba = badAnswers.toString();
                String url = "";
                if(!"".equals(ba)) {
                    String errorMsg = "The following questions had incorrect entries:<br />" + ba + "<br />Please change the answer to an answer between 1 and 5";
                    request.setAttribute("errorMsg", errorMsg);
                    url = "/jsps/answersOnlyTestpage.jsp";
                }
                else {
                    url = "CF_ControllerServlet?action=finishedQuestions&whichTest=" + selectedTest;
                }
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
                
            }
            else {
                int currQ = testBean.getCurrentQuestion();
                int startQ = testBean.getStartQuestion();
                boolean onlineUserFinished = false;
                if(!testBean.isAnotherQuestion()) {
                    onlineUserFinished = true;
                }
                String url = "";
                                
                for(int i = startQ; i < currQ; i++) {
                    String rb = String.valueOf(i);
                    answer = request.getParameter(rb);
                    if(answer == null) {
                        answer = "";
                    }
                    if("".equals(answer)){
                        String thisQuestion = String.valueOf(i);
                        badAnswers.append(thisQuestion + " ");
                        answer = "0";
                    }
                    testBean.setAnswers(answer,i);
                    }
                String ba = badAnswers.toString();
                if("".equals(ba)) {
                    if(!onlineUserFinished) {
                        testBean.setStartQuestion(currQ);
                        url = "/jsps/" + selectedTest + "Testpage.jsp";
                    }
                    else {
                        url = "CF_ControllerServlet?action=finishedQuestions";
                    }
                }
                else {
                    int newLastQuestion = currQ - 20;
                    testBean.setCurrentQuestion(newLastQuestion);
                    String errorMsg = "The following questions were unanswered:<br />" + ba + "<br />Please answer them.";
                    request.setAttribute("errorMsg", errorMsg);
                    url = "/jsps/" + selectedTest + "Testpage.jsp";
                }
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
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
