/*
 * CF_ControllerServlet.java
 *
 * Created on July 21, 2003, 12:58 PM
 */

//####### Writing debug logfile ##################################
// try {
//   java.io.BufferedWriter transReceipt = 
//      new java.io.BufferedWriter(new java.io.FileWriter
//        (homeDirPath + "logs/debugLog.txt", true));
//   transReceipt.write(new java.util.Date() + " At someplace object is "  + object + "\n");
//   transReceipt.close();
// } catch (IOException e) {
//    System.out.println(new java.util.Date() + "BufferedWriter did not work");
//    System.out.println(e);
// } 
//################################################################

package com.personalblueprint.servlets;

import com.personalblueprint.beans.*;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.servlet.*;
import javax.servlet.http.*;/**/
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  bdunlop
 * @version
 */


public class CF_ControllerServlet extends HttpServlet {
    
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
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession(true);
try {
                    try (java.io.BufferedWriter transReceipt = new java.io.BufferedWriter(new java.io.FileWriter
                ("/home1/w/wcgypbp/logs/debugLog.txt", true))) {
                        transReceipt.write(new java.util.Date() + " At CF_Controller: action = " + action + "\n");
                    }
 } catch (IOException e) {
    System.out.println(new java.util.Date() + "BufferedWriter did not work");
    System.out.println(e);
 }
        
        if(null != action) switch (action) {
            case "generatePasswordHash":{
                String pw2bHashed = request.getParameter("password");
                String hashedPW = CF_PasswordHandler.createHash(pw2bHashed);
                request.setAttribute("hashedPW", hashedPW);
                RequestDispatcher rd = request.getRequestDispatcher("/jsps/displayHashedPassword.jsp");
                rd.forward(request, response);
                    break;                
                }            
            case "authenticate":{
                RequestDispatcher rd = request.getRequestDispatcher("CF_LoginHandlerServlet");
                rd.forward(request, response);
                    break;
                }
            case "showSysAdminIndex":{
                RequestDispatcher rd = request.getRequestDispatcher("/jsps/sysadminindex.jsp");
                rd.forward(request, response);
                    break;
                }
            case "showAdminIndex":{
                RequestDispatcher rd = request.getRequestDispatcher("/jsps/adminindex.jsp");
                rd.forward(request, response);
                    break;
                }
            case "showUserIndex":{
                String url = "";
                CF_UserInfoBean user = (CF_UserInfoBean) session.getAttribute("user");
                CF_UserInfoBean tempUser = (CF_UserInfoBean) session.getAttribute("tempUser");
                int personType = user.getPersonType();
                String isOnSite = request.getParameter("onsite");
                if("yes".equals(isOnSite)) {
                    if(tempUser == null && personType == 4){
                        session.setAttribute("tempUser", user);
                    }
                    url = "/jsps/onSiteUserIndex.jsp";
                }
                else {
                    url = "/jsps/userIndex.jsp";
                }      
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
                    break;
                }
            case "showWolfCGnames":{
                String listUrl = "";
                String rank = request.getParameter("rank");
                CF_TestsTaken names = new CF_TestsTaken(rank);
                session.setAttribute("names", names);
                listUrl = "/jsps/listwcgnames.jsp";
                RequestDispatcher rd = request.getRequestDispatcher(listUrl);
                rd.forward(request, response);
                    break;
                }
            case "showOnsiteLandingPage":{
                RequestDispatcher rd = request.getRequestDispatcher("/jsps/onSiteLandingPage.jsp");
                rd.forward(request, response);
                    break;
                }
            case "register":{
                RequestDispatcher rd = request.getRequestDispatcher("CF_ManageUserServlet");
                rd.forward(request, response);
                    break;
            }
            case "createNewUser":{
                RequestDispatcher rd = request.getRequestDispatcher("CF_ManageUserServlet");
                rd.forward(request, response);
                    break;
            }
            case "saveUserToDatabase":{
                RequestDispatcher rd = request.getRequestDispatcher("CF_StoreUserInfo");
                rd.forward(request, response);
                    break;
            }
            case "runNewTest":{
                String whichTest = request.getParameter("whichTest");
                String state = request.getParameter("state");
                if("start".equals(state)) {
                    String url = "/jsps/ibtTestpageIntro.jsp";
                    RequestDispatcher rd = request.getRequestDispatcher(url);
                    rd.forward(request, response);
                }
                else {
                    String url = "/jsps/" + whichTest + "Testpage.jsp";
                    CF_TestManagerBean testBean = new CF_TestManagerBean(whichTest);
                    session.setAttribute("testBean", testBean);
                    RequestDispatcher rd = request.getRequestDispatcher(url);
                    rd.forward(request, response);
                }
                    break;
            }
            case "runTestAnswersOnly":{
                String errorMsg = request.getParameter("errorMsg");
                if("".equals(errorMsg)|| errorMsg == null) {
                    String whichTest = request.getParameter("whichTest");
                    CF_TestManagerBean testBean = new CF_TestManagerBean(whichTest);
                    session.setAttribute("testBean", testBean);
                    request.setAttribute("whichTest", whichTest);
                    String pNum = request.getParameter("personNum");
                    int personNum = Integer.parseInt(pNum);
                    CF_UserInfoBean tempUser = new CF_UserInfoBean();
                    tempUser.setPersonNum(personNum);
                    session.setAttribute("tempUser", tempUser);
                    session.setAttribute("answersOnly", "yes");
                }
                RequestDispatcher rd = request.getRequestDispatcher("/jsps/answersOnlyTestpage.jsp");
                rd.forward(request, response);
                    break;
            }
            case "processTestPage":{
                String whichTest = request.getParameter("whichTest");
                request.setAttribute("whichTest", whichTest);
                RequestDispatcher rd = request.getRequestDispatcher("CF_TestServlet");
                rd.forward(request, response);
                    break;
            }
            case "finishedQuestions":{
                // contToQuestions is tag for admin when only registering a person and filling their inventory answers
                String answersOnly = (String) session.getAttribute("answersOnly");
                CF_TestManagerBean testBean = (CF_TestManagerBean) session.getAttribute("testBean");
                CF_UserInfoBean tempUser = (CF_UserInfoBean) session.getAttribute("tempUser");
                CF_UserInfoBean user = (CF_UserInfoBean) session.getAttribute("user");
                CF_UsersBean users = new CF_UsersBean();
                String newUserType = (String) session.getAttribute("newUserType");
                int personNum = user.getPersonNum();
                int personType = user.getPersonType();
                String whichTest = request.getParameter("whichTest");
                String url = "";
                // wolf office entering answers for participant
                if("yes".equals(answersOnly)) {
                    testBean.writeTestResultsToDB(tempUser, whichTest);
                    session.setAttribute("answersOnly", null);
                    url = "CF_ControllerServlet?action=showWolfCGnames";
                }
                // onSite participate in office testing process (original login user: wolf office - personType:6)
                else if(personType == 6) {
                    testBean.writeTestResultsToDB(tempUser, whichTest);
                    url = "CF_ControllerServlet?action=showUserIndex&onsite=yes";
                }
                // an onsite participant (personType:4) but logged in outside of office testing process - no tempUser
                else if (personType == 4){
                    testBean.writeTestResultsToDB(user, whichTest);
                    url = "CF_ControllerServlet?action=showUserIndex&onsite=yes";
                }
                else {
                    testBean.writeTestResultsToDB(user, whichTest);
                    url = "/jsps/thankyou.jsp";
                }
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
                    break;
            }
            case "createPbiTestResultList":{
                String stint = request.getParameter("stint");
                String resultType = request.getParameter("resultType");
                String listUrl = "";
                if("start".equals(stint)) {
                    listUrl = "CF_BuildPbiTestResultList?resultType=" + resultType;
                }
                else if("printout".equals(stint)) {
                    listUrl = "/jsps/showPbiTestResultList.jsp";
                }       RequestDispatcher rd = request.getRequestDispatcher(listUrl);
                rd.forward(request, response);
                    break;
            }
            case "showtests":{
                String rank = request.getParameter("rank");
                CF_UserInfoBean user = (CF_UserInfoBean) session.getAttribute("user");
                int personNum = user.getPersonNum();
                CF_TestsTaken testsTaken = new CF_TestsTaken(rank);
                session.setAttribute("testsTaken", testsTaken);
                RequestDispatcher rd = request.getRequestDispatcher("/jsps/listusertests.jsp");
                rd.forward(request, response);
                    break;
            }
            case "showResults":{
                String personFullName = request.getParameter("personFullName");
                session.setAttribute("personFullName", personFullName);
                RequestDispatcher rd = request.getRequestDispatcher("CF_ShowResultsServlet");
                rd.forward(request, response);
                    break;
            }
            case "displayChart":{
                String url = "/jsps/userChartDisplay.jsp";
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
                    break;
        }   
            case "showInterpretations":{
                String url = "";
                String introState = request.getParameter("introState");
                if("start".equals(introState)) {
                    url = "/jsps/bkClientInterpIntro.jsp";
                }
                else {
                    url = "/jsps/userInterpretationsPage.jsp";
                }       RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
                    break;
            }
            case "modifyTexts":{
                String whichText = request.getParameter("whichText");
                if("questions".equals(whichText)) {
                    RequestDispatcher rd = request.getRequestDispatcher("CF_ModifyQuestions");
                    rd.forward(request, response);
                }
                else if("interpretations".equals(whichText)) {
                    RequestDispatcher rd = request.getRequestDispatcher("CF_ModifyInterpretations");
                    rd.forward(request, response);
                }   
                break;
            }
            case "modifySubscales":{
                RequestDispatcher rd = request.getRequestDispatcher("CF_ManageSubscalesServlet");
                rd.forward(request, response);
                    break;
            }
            case "thankyou":{
                RequestDispatcher rd = request.getRequestDispatcher("/jsps/thankyou.jsp");
                rd.forward(request, response);
                    break;
            }
            case "logout":{
                String url = "";
                String userLoggingOut = request.getParameter("newUserType");
                String isOnSite = request.getParameter("onsite");
                CF_UserInfoBean user = (CF_UserInfoBean) session.getAttribute("user");
                CF_UserInfoBean tempUser = (CF_UserInfoBean) session.getAttribute("tempUser");
try {
                    try (java.io.BufferedWriter transReceipt = new java.io.BufferedWriter(new java.io.FileWriter
                ("/home1/w/wcgypbp/logs/debugLog.txt", true))) {
                        transReceipt.write(new java.util.Date() + " At logout: isOnSite = "  + isOnSite + " and userLoggingOut = " + userLoggingOut + "\n" + "tempUser is " + tempUser + "\n");
                    }
 } catch (IOException e) {
    System.out.println(new java.util.Date() + "BufferedWriter did not work");
    System.out.println(e);
 }
                if("yes".equals(isOnSite)) {
                    session.setAttribute("tempUser", null);
                    url = "/jsps/onSiteLandingPage.jsp";
                    RequestDispatcher rd = request.getRequestDispatcher(url);
                    rd.forward(request, response);
                }
                else if("BookstorePurchase".equals(userLoggingOut)) {
                    session.invalidate();
                    url = "http://www.personalblueprint.com/index.html";
                    response.sendRedirect(url);
                }
                else {
                    if(user != null) {
                        session.setAttribute("user", null);
                    }
                    if(tempUser != null) {
                        session.setAttribute("tempUser", null);
                    }
                    session.invalidate();
                    url = "http://www.personalblueprint.com/index.html";
                    response.sendRedirect(url);
                }
                    break;
            }
            case "databaseFailure":{
                String url = "/jsps/errorPage.jsp";
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.forward(request, response);
                    break;
            }
            default:{
                //we should never get here, but just in case
                CF_UserInfoBean user = (CF_UserInfoBean) session.getAttribute("user");
                CF_UserInfoBean tempUser = (CF_UserInfoBean) session.getAttribute("tempUser");
                if(user != null) {
                    session.setAttribute("user", null);
                }       if(tempUser != null){
                    session.setAttribute("tempUser", null);
                }       session.invalidate();
                response.sendRedirect("http://www.personalblueprint.com/index.html");
                    break;
            }
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
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CF_ControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(CF_ControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CF_ControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(CF_ControllerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** Returns a short description of the servlet.
     * @return 
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
