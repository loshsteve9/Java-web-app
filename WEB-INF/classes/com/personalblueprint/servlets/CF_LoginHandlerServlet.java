/*
 * LBLoginHandlerServlet.java
 *
 * Created on June 16, 2003, 11:20 PM
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  bdunlop
 * @version
 */
public class CF_LoginHandlerServlet extends HttpServlet {
    
    /** Initializes the servlet.
     * @param config
     * @throws javax.servlet.ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
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
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.spec.InvalidKeySpecException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException , IOException, NoSuchAlgorithmException, InvalidKeySpecException/*, SQLException, ClassNotFoundException */{

        ServletContext context = super.getServletContext();
        HttpSession session = request.getSession();
/*
        String newRegister = request.getParameter("stint");
        if("newRegister".equals(newRegister)) {
            session.setAttribute("returnIndex", "logout");
            RequestDispatcher rd = request.getRequestDispatcher("CF_ManageUserServlet?stint=makeNewUser&newUserType=NewRegister");
            rd.forward(request, response);
            return;            
        }
*/
        String userid = request.getParameter("userid");
        if("".equals(userid) || userid == null) {
            RequestDispatcher rd = request.getRequestDispatcher("/jsps/loginDenied.jsp");
            rd.forward(request, response);
            return;
        }
        
        CF_UsersBean users = new CF_UsersBean();
        
        boolean isLogonUserValid = false;            
        String password = request.getParameter("password");
        isLogonUserValid = validLogonUser(userid, password, session, users);
       
        if (!isLogonUserValid) {
            String url = "";
            String newRegister = request.getParameter("stint");
            if("newRegister".equals(newRegister)) {
                session.setAttribute("returnIndex", "logout");
                url = "CF_ControllerServlet?action=bk_online_intro";
            }
            else {
                url = "/jsps/loginDenied.jsp";
            }
            RequestDispatcher rd = request.getRequestDispatcher(url);
            rd.forward(request, response);
        }
        else {
            CF_UserInfoBean user = (CF_UserInfoBean) session.getAttribute("user");
            user.setValidated(true);
            String act =  null;
            String isOnsite = null;
            int personType = user.getPersonType();

            switch (personType) {
                case 1:
                    act = "showSysAdminIndex";
                    break;
                case 2:
                    act = "showAdminIndex";
                    break;
                case 4:
                    act = "showUserIndex";
                    isOnsite = "yes";
                    break;
                case 6:
                    act = "showOnsiteLandingPage";
                    session.setMaxInactiveInterval(-1);
                    break;
                default:
                    break;
            }
            session.setAttribute("returnIndex", act);
            RequestDispatcher rd = request.getRequestDispatcher("CF_ControllerServlet?action=" + act + "&onsite=" + isOnsite);
            rd.forward(request, response);
        }
    }
    
    private boolean validLogonUser(String userid, String password, HttpSession session, CF_UsersBean users) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return users.isUserValid(userid, password, session);
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
        }
        catch (ServletException | IOException ex) {
            try (PrintWriter out = response.getWriter()) {
                ex.printStackTrace(out);
                out.println("An Exception occurred");
            }            
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(CF_LoginHandlerServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        }
        catch (ServletException | IOException ex) {
            //PrintWriter out = response.getWriter();
            //ex.printStackTrace(out);
            //out.println("An Exception occurred");
            //out.close();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(CF_LoginHandlerServlet.class.getName()).log(Level.SEVERE, null, ex);
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
