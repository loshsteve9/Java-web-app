/*
 * CF_ManageUserServlet.java
 *
 * Created on February 4, 2004, 9:49 PM
 *
 * @author  gchrt
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
import com.personalblueprint.helperClasses.*;

import java.io.*;
import java.util.*;
import java.text.*;
import java.util.regex.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class CF_ManageUserServlet extends HttpServlet {
    
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
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException , IOException {
        
        HttpSession session = request.getSession(false);
        {
            session = request.getSession(true);
        }
        String cancel = request.getParameter("cancel");
        String stint = request.getParameter("stint");
        String newUserType = request.getParameter("newUserType");
        StringBuilder errorMsgSB = new StringBuilder();
        String newLine = "<br />";
        String url = "";
        RequestDispatcher rd = null;

        if(cancel != null) {
            session.setAttribute("answersOnly", null);
            String returnIndex = (String) session.getAttribute("returnIndex");
            url = "CF_ControllerServlet?action=" + returnIndex;
            rd = request.getRequestDispatcher(url);            
        }

        else if ("makeNewUser".equals(stint)) {
            session.setAttribute("newUserType", newUserType);
            if(null != newUserType) switch (newUserType) {
                case "Participant":{
                    CF_UserInfoBean tempUser = new CF_UserInfoBean();
                    tempUser.setPersonType(CF_Person.TYPE_BLUEPRINT_PARTICIPANT_USER);
                    session.setAttribute("tempUser", tempUser);
                    url = "/jsps/addParticipantUser.jsp";
                        break;
                    }
                case "onSiteParticipant":{
                    CF_UserInfoBean tempUser = new CF_UserInfoBean();
                    tempUser.setPersonType(CF_Person.TYPE_BLUEPRINT_ONSITE_PARTICIPANT_USER);
                    session.setAttribute("tempUser", tempUser);
                    url = "/jsps/addOnSiteParticipantUser.jsp";
                        break;
                    }
                case "Admin":{
                    CF_UserInfoBean tempUser = new CF_UserInfoBean();
                    tempUser.setPersonType(CF_Person.TYPE_BLUEPRINT_ADMIN_USER);
                    session.setAttribute("tempUser", tempUser);
                    url = "/jsps/addUser_other.jsp";
                        break;
                    }
                case "Sysadmin":{
                    CF_UserInfoBean tempUser = new CF_UserInfoBean();
                    tempUser.setPersonType(CF_Person.TYPE_BLUEPRINT_SYSADMIN_USER);
                    session.setAttribute("tempUser", tempUser);
                    url = "/jsps/addUser_other.jsp";
                        break;
                    }
                default:
                    break;
            }
            rd = request.getRequestDispatcher(url);  
        }
          
        else if("processPage".equals(stint)) {
            int loginPersonType = 0;
            CF_UserInfoBean tempUser = (CF_UserInfoBean) session.getAttribute("tempUser");
            CF_UserInfoBean user = (CF_UserInfoBean) session.getAttribute("user");
            newUserType = (String) session.getAttribute("newUserType");
            Pattern notNumbers = Pattern.compile("[^0-9]");
            int formErrors = 0;
            StringBuffer formErrorMsg = new StringBuffer();
            
            if(user != null){
                loginPersonType = user.getPersonType();
            }
            
            ServletContext context = super.getServletContext();
            
            CF_UsersBean users = new CF_UsersBean();
            
            // process book and online participant
            if("NewRegister".equals(newUserType)) {
                url = "/jsps/onlineRegister.jsp";
                
                // check form for empty fields build error msg
                String firstName = request.getParameter("firstName");
                if("".equals(firstName)) {
                    formErrors++;
                    formErrorMsg.append("No First Name was entered");
                    formErrorMsg.append(newLine);
                    firstName = "";
                }
                tempUser.setFirstName(firstName.trim());
                
                String middleInitial = request.getParameter("middleInitial");
                if("".equals(middleInitial)) {
                    middleInitial = "";
                }
                tempUser.setMiddleInitial(middleInitial.trim());
                                
                String lastName = request.getParameter("lastName");
                if("".equals(lastName)) {
                    formErrors++;
                    formErrorMsg.append("No Last Name was entered");
                    formErrorMsg.append(newLine);
                    lastName = "";
                }
                tempUser.setLastName(lastName.trim());
                
                tempUser.setDisplayName(firstName + " " + lastName);

                String gender = request.getParameter("gender");
                if(gender == null) {
                    formErrors++;
                    formErrorMsg.append("Please enter person's Gender");
                    formErrorMsg.append(newLine);
                    gender = "";
                }
                tempUser.setGender(gender.trim());

                String email = request.getParameter("email");
                if("".equals(email)) {
                    formErrors++;
                    formErrorMsg.append("Please enter an Email Address");
                    formErrorMsg.append(newLine);
                    email = "";
                }
                tempUser.setEmail(email.trim());

                // Set birthdate
                tempUser.setBirthDate("");
                
                // Validate password
                String userid = request.getParameter("userid");
                String password = request.getParameter("password");
                String confirmPass = request.getParameter("confirmPass");

                if("NewRegister".equals(newUserType)) {
                    String restrictedUseridChars = "";
                    if("".equals(userid)) {
                        formErrors++;
                        formErrorMsg.append("Please enter a Userid");
                        formErrorMsg.append(newLine);
                    }
                    else {
                        int numCharsUserid = userid.length();
                        if(numCharsUserid < 4) {
                            formErrors++;
                            formErrorMsg.append("Userid must be more than four letters");
                            formErrorMsg.append(newLine);
                        }
                        else {
                            restrictedUseridChars = userid.substring(0, 4);
                        }
                    }
                    tempUser.setUserid(userid.trim());
                }

                if("".equals(password)) {
                    formErrors++;
                    formErrorMsg.append("Please enter a Password");
                    formErrorMsg.append(newLine);
                }
                else {
                    int numCharsPassword = password.length();
                    if(numCharsPassword < 6) {
                        formErrors++;
                        formErrorMsg.append("Passwords must be longer than 5 characters");
                        formErrorMsg.append(newLine);
                    }
                }
                tempUser.setPassword(password.trim());

                if("".equals(confirmPass)) {
                    formErrors++;
                    formErrorMsg.append("Please retype the Password in Confirm Password");
                    formErrorMsg.append(newLine);
                }
                tempUser.setConfirmPass(confirmPass.trim());

                if(formErrors > 0) {
                    errorMsgSB.append("This form is incomplete. Please make sure the following fields are filled in:");
                    errorMsgSB.append(newLine);
                    errorMsgSB.append(formErrorMsg);
                    String errorMsg = errorMsgSB.toString();
                    request.setAttribute("errorMsg", errorMsg);
                }
                else {
                    if("NewRegister".equals(newUserType) && users.isUseridTaken(userid)) {
                        tempUser.setUserid("");
                        tempUser.setPassword("");
                        tempUser.setConfirmPass("");
                        StringBuilder StringBuilder; /*append*/
                        StringBuilder = errorMsgSB.append("The userid \"").append(userid).append("\" has been taken. Please choose another userID. Thank you.");
                        String errorMsg = errorMsgSB.toString();
                        request.setAttribute("errorMsg", errorMsg);
                    }
                    else if(!password.equals(confirmPass)) {
                        tempUser.setPassword("");
                        tempUser.setConfirmPass("");
                        errorMsgSB.append("The Passwords did not match. Please be sure both fields contain the same password. Thank you.");
                        String errorMsg = errorMsgSB.toString();
                        request.setAttribute("errorMsg", errorMsg);
                    }
                    else {
                        url = "CF_ControllerServlet?action=onlinePurchase";
                    }
                }
            }
            
            // create a participant or an onSite participant(loginPersonType #6 = wolf office login)
            else if("Participant".equals(newUserType) || loginPersonType == 6) {
                
                // check form for empty fields build error msg
                String firstName = request.getParameter("firstName");
                if("".equals(firstName)) {
                    formErrors++;
                    formErrorMsg.append("No First Name was entered");
                    formErrorMsg.append(newLine);
                    firstName = "";
                }
                tempUser.setFirstName(firstName.trim());
                
                String middleInitial = request.getParameter("middleInitial");
                if("".equals(middleInitial)) {
                    middleInitial = "";
                }
                tempUser.setMiddleInitial(middleInitial.trim());
                                
                String lastName = request.getParameter("lastName");
                if("".equals(lastName)) {
                    formErrors++;
                    formErrorMsg.append("No Last Name was entered");
                    formErrorMsg.append(newLine);
                    lastName = "";
                }
                tempUser.setLastName(lastName.trim());
                
                String presentingIssue = request.getParameter("presentingIssue");
                if("".equals(presentingIssue)) {
                    formErrors++;
                    formErrorMsg.append("No Presenting Issue was entered");
                    formErrorMsg.append(newLine);
                    presentingIssue = "";
                }
                
                tempUser.setPresentingIssue(presentingIssue.trim());
                
                if("Participant".equals(newUserType)) {
                    String hoursWorked = request.getParameter("hoursWorked");
                    Matcher hw = notNumbers.matcher(hoursWorked);
                    boolean noNumHours = hw.find();                 
                    if("".equals(hoursWorked) || "0".equals(hoursWorked) || noNumHours) {
                        formErrors++;
                        formErrorMsg.append("Number of Hours with patient needs to be entered");
                        formErrorMsg.append(newLine);
                        hoursWorked = "";
                    }
                    tempUser.setHoursWorked(hoursWorked.trim());
                }
                
                String isMarried = request.getParameter("isMarried");         
                if(isMarried == null) {
                    formErrors++;
                    formErrorMsg.append("No Marriage Status was entered");
                    formErrorMsg.append(newLine);
                    isMarried = "";
                }
                tempUser.setIsMarried(isMarried.trim());
                
                String yearsMarried = request.getParameter("yearsMarried");
                Matcher ym = notNumbers.matcher(yearsMarried);
                boolean noNumYears = ym.find();
                if("yes".equals(isMarried) && ("".equals(yearsMarried) || "0".equals(yearsMarried) || noNumYears)) {
                    formErrors++;
                    formErrorMsg.append("Please enter number of years married");
                    formErrorMsg.append(newLine);
                    yearsMarried = "";
                }
                if("no".equals(isMarried)) {
                    yearsMarried = "";
                }
                tempUser.setYearsMarried(yearsMarried.trim());

                String gender = request.getParameter("gender");
                if(gender == null) {
                    formErrors++;
                    formErrorMsg.append("Please enter person's Gender");
                    formErrorMsg.append(newLine);
                    gender = "";
                }
                tempUser.setGender(gender.trim());

                String education = request.getParameter("education");
                if("".equals(education)) {
                    formErrors++;
                    formErrorMsg.append("Please enter Education Level");
                    formErrorMsg.append(newLine);
                    education = "";
                }
                tempUser.setEducation(education.trim());
                
                String occupation = request.getParameter("occupation");
                if("".equals(occupation)) {
                    formErrors++;
                    formErrorMsg.append("Please enter an Occupation");
                    formErrorMsg.append(newLine);
                    occupation = "";
                }
                tempUser.setOccupation(occupation.trim());
                
                String employPosition = request.getParameter("employPosition");
                if("".equals(employPosition)) {
                    formErrors++;
                    formErrorMsg.append("Please enter Employment Position");
                    formErrorMsg.append(newLine);
                    employPosition = "";
                }
                tempUser.setEmployPosition(employPosition.trim());
                
                String howLongEmployed = request.getParameter("howLongEmployed");
                Matcher hle = notNumbers.matcher(howLongEmployed);
                boolean noHoursEmp = hle.find();
                if("".equals(howLongEmployed) || "0".equals(howLongEmployed) || noHoursEmp) {
                    formErrors++;
                    formErrorMsg.append("Please enter how long Employed");
                    formErrorMsg.append(newLine);
                    howLongEmployed = "";
                }
                tempUser.setHowLongEmployed(howLongEmployed.trim());
                
                String ethnicity = request.getParameter("ethnicity");
                if("".equals(ethnicity)) {
                    formErrors++;
                    formErrorMsg.append("Please enter person's Ethnicity");
                    formErrorMsg.append(newLine);
                    ethnicity = "";
                }
                tempUser.setEthnicity(ethnicity.trim());
                
                String isNative = request.getParameter("isNative");
                if(isNative == null) {
                    formErrors++;
                    formErrorMsg.append("Please indicate if Native to United States");
                    formErrorMsg.append(newLine);
                    isNative = "";
                }
                tempUser.setIsNative(isNative.trim());
                
                String yearsInUSA = request.getParameter("yearsInUSA");
                Matcher yUS = notNumbers.matcher(yearsInUSA);
                boolean noYearsUS = yUS.find();
                if("no".equals(isNative) && ("".equals(yearsInUSA) || "0".equals(yearsInUSA) || noYearsUS)) {
                    formErrors++;
                    formErrorMsg.append("Please enter number of years in United States");
                    formErrorMsg.append(newLine);
                    yearsInUSA = "";
                }
                tempUser.setYearsInUSA(yearsInUSA.trim());
                
                // build string birthdate in y-d-m for database
                String dobMonth = request.getParameter("dobMonth");
                if("".equals(dobMonth)) {
                    formErrors++;
                    formErrorMsg.append("Please enter a Birth Month");
                    formErrorMsg.append(newLine);
                    dobMonth = "";
                }
                tempUser.setDobMonth(dobMonth.trim());
                
                String dobDay = request.getParameter("dobDay");
                if("".equals(dobDay)) {
                    formErrors++;
                    formErrorMsg.append("Please enter a Birth Day");
                    formErrorMsg.append(newLine);
                    dobDay = "";
                }
                tempUser.setDobDay(dobDay.trim());
                
                String dobYear = request.getParameter("dobYear");
                if("".equals(dobYear)) {
                    formErrors++;
                    formErrorMsg.append("Please enter Birth Year");
                    formErrorMsg.append(newLine);
                    dobYear = "";
                }
                tempUser.setDobYear(dobYear.trim());
                
                String birthDate = (dobYear + "-" + dobMonth + "-" + dobDay);
                tempUser.setBirthDate(birthDate.trim()); 
                
                // If not an onSite participant Check userid for compliance and against database
                String userid = request.getParameter("userid");
                String password = request.getParameter("password");
                String confirmPass = request.getParameter("confirmPass");
                
                // referringPersonType 6 are onSite participants
                if(loginPersonType == 6) {
                    String fnInitial = firstName.substring(0,1);
                    String lnInitial = lastName.substring(0,1);
                    Date now = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("MMddyyHHss");
                    String formattedDate = sdf.format(now);
                    userid = fnInitial + lnInitial + formattedDate;
                    password = fnInitial + lnInitial + "wcgLogon";
                    confirmPass = fnInitial + lnInitial + "wcgLogon";
                }

                String restrictedUseridChars = "";
                if("".equals(userid)) {
                    formErrors++;
                    formErrorMsg.append("Please enter a Userid");
                    formErrorMsg.append(newLine);
                }
                else {
                    int numCharsUserid = userid.length();
                    if(numCharsUserid < 4) {
                        formErrors++;
                        formErrorMsg.append("Userid must be more than four letters");
                        formErrorMsg.append(newLine);
                    }
                    else {
                        restrictedUseridChars = userid.substring(0, 4);
                    }
                }
                tempUser.setUserid(userid.trim());

                // Validate password
                if("".equals(password)) {
                    formErrors++;
                    formErrorMsg.append("Please enter a Password");
                    formErrorMsg.append(newLine);
                }
                else {
                    int numCharsPassword = password.length();
                    if(numCharsPassword < 6) {
                        formErrors++;
                        formErrorMsg.append("Passwords must be longer than 5 characters");
                        formErrorMsg.append(newLine);
                    }
                }
                tempUser.setPassword(password.trim());

                if("".equals(confirmPass)) {
                    formErrors++;
                    formErrorMsg.append("Please retype the Password in Confirm Password");
                    formErrorMsg.append(newLine);
                }
                tempUser.setConfirmPass(confirmPass.trim());

                if(formErrors > 0) {
                    errorMsgSB.append("This form is incomplete. Please make sure the following fields are filled in:");
                    errorMsgSB.append(newLine);
                    errorMsgSB.append(formErrorMsg);
                    String errorMsg = errorMsgSB.toString();
                    request.setAttribute("errorMsg", errorMsg);
                    if("Participant".equals(newUserType)) {
                        url = "/jsps/addUser_Participant.jsp";
                    }
                    else if("onSiteParticipant".equals(newUserType)) {
                        url = "/jsps/addUser_onSiteParticipant.jsp";
                    }
                }
                else {
                    if(users.isUseridTaken(userid)) {
                        tempUser.setUserid("");
                        tempUser.setPassword("");
                        tempUser.setConfirmPass("");
                        StringBuilder StringBuilder; /*append*/
                        StringBuilder = errorMsgSB.append("The userid \"").append(userid).append("\" has been taken. Please choose another userID. Thank you.");
                        String errorMsg = errorMsgSB.toString();
                        request.setAttribute("errorMsg", errorMsg);
                        if("Participant".equals(newUserType)) {
                            url = "/jsps/addUser_Participant.jsp";
                        }
                    }
                    else if(!password.equals(confirmPass)) {
                        tempUser.setPassword("");
                        tempUser.setConfirmPass("");
                        errorMsgSB.append("The Passwords did not match. Please be sure both fields contain the same password. Thank you.");
                        String errorMsg = errorMsgSB.toString();
                        request.setAttribute("errorMsg", errorMsg);
                        if("Participant".equals(newUserType)) {
                            url = "/jsps/addUser_Participant.jsp";
                        }
                    }
                    else {
                        url = "CF_ControllerServlet?action=saveUserToDatabase";
                    }
                }                
            }
            
            // create other users - professional, administrator, system administrator
            else {
                String firstName = request.getParameter("firstName");
                if("".equals(firstName)) {
                    formErrors++;
                    formErrorMsg.append("No First Name was entered");
                    formErrorMsg.append(newLine);
                    firstName = "";
                }
                tempUser.setFirstName(firstName.trim());
                
                String middleInitial = request.getParameter("middleInitial");
                if("".equals(middleInitial)) {
                    middleInitial = "";
                }
                tempUser.setMiddleInitial(middleInitial.trim());
                                
                String lastName = request.getParameter("lastName");
                if("".equals(lastName)) {
                    formErrors++;
                    formErrorMsg.append("No Last Name was entered");
                    formErrorMsg.append(newLine);
                    lastName = "";
                }
                tempUser.setLastName(lastName.trim());  
                
                String education = request.getParameter("education");
                if("".equals(education)) {
                    formErrors++;
                    formErrorMsg.append("Please enter Education Level");
                    formErrorMsg.append(newLine);
                    education = "";
                }
                tempUser.setEducation(education.trim());

                String employPosition = request.getParameter("employPosition");
                if("".equals(employPosition)) {
                    formErrors++;
                    formErrorMsg.append("Please enter Employment Position");
                    formErrorMsg.append(newLine);
                    employPosition = "";
                }
                tempUser.setEmployPosition(employPosition.trim());

                String email = request.getParameter("email");
                if("".equals(email)) {
                    formErrors++;
                    formErrorMsg.append("Please enter an Email Address");
                    formErrorMsg.append(newLine);
                    email = "";
                }
                tempUser.setEmail(email.trim());

                String street = request.getParameter("street");
                if("".equals(street)) {
                    formErrors++;
                    formErrorMsg.append("Please enter a Street Address");
                    formErrorMsg.append(newLine);
                    street = "";
                }
                tempUser.setStreet(street.trim());

                String suiteOrApt = request.getParameter("suiteOrApt");
                if("".equals(suiteOrApt)) {
                    formErrors++;
                    formErrorMsg.append("Please enter a Suite or Apt");
                    formErrorMsg.append(newLine);
                    suiteOrApt = "";
                }
                tempUser.setSuiteOrApt(suiteOrApt.trim());

                String city = request.getParameter("city");
                if("".equals(city)) {
                    formErrors++;
                    formErrorMsg.append("Please enter a City");
                    formErrorMsg.append(newLine);
                    city = "";
                }
                tempUser.setCity(city.trim());

                String stateCode = request.getParameter("stateCode");
                if("".equals(stateCode)) {
                    formErrors++;
                    formErrorMsg.append("Please enter a State Code");
                    formErrorMsg.append(newLine);
                    stateCode = "";
                }
                tempUser.setStateCode(stateCode.trim());

                String zipOrPostalCode = request.getParameter("zipOrPostalCode");
                if("".equals(zipOrPostalCode)) {
                    formErrors++;
                    formErrorMsg.append("Please enter a ZipCode");
                    formErrorMsg.append(newLine);
                    zipOrPostalCode = "";
                }
                tempUser.setZipOrPostalCode(zipOrPostalCode.trim());
                
                String country = request.getParameter("country");
                if("".equals(country)) {
                    formErrors++;
                    formErrorMsg.append("Please enter a Country Code");
                    formErrorMsg.append(newLine);
                    country = "";
                }
                tempUser.setCountry(country.trim());

                String areaCode = request.getParameter("areaCode");
                if("".equals(areaCode)) {
                    formErrors++;
                    formErrorMsg.append("Please enter a Phone Area Code");
                    formErrorMsg.append(newLine);
                    areaCode = "";
                }
                tempUser.setAreaCode(areaCode.trim());

                String prefix = request.getParameter("prefix");
                if("".equals(prefix)) {
                    formErrors++;
                    formErrorMsg.append("Please enter a Phone Prefix");
                    formErrorMsg.append(newLine);
                    prefix = "";
                }
                tempUser.setPrefix(prefix.trim());

                String number = request.getParameter("number");
                if("".equals(number)) {
                    formErrors++;
                    formErrorMsg.append("Please enter the Phone Number");
                    formErrorMsg.append(newLine);
                    number = "";
                }
                tempUser.setNumber(number.trim());

                String extension = request.getParameter("extension");
                if("".equals(extension)) {
                    extension = "";
                }
                tempUser.setExtension(extension.trim());
                
                // Check userid against database, validate password
                String userid = request.getParameter("userid");
                String restrictedUseridChars = userid.substring(0, 4);
                if("".equals(userid)) {
                        formErrors++;
                        formErrorMsg.append("Please enter a Userid");
                        formErrorMsg.append(newLine);
                }
                if("bk1".equals(restrictedUseridChars)) {
                        formErrors++;
                    StringBuffer StringBuffer; /*append*/
                    StringBuffer = formErrorMsg.append("Userid cannot begin with ").append(restrictedUseridChars);
                        formErrorMsg.append(newLine);  
                }
                tempUser.setUserid(userid.trim());
                
                String password = request.getParameter("password");
                if("".equals(password)) {
                        formErrors++;
                        formErrorMsg.append("Please enter a Password");
                        formErrorMsg.append(newLine);
                }
                else {
                    int numCharsPassword = password.length();
                    if(numCharsPassword < 6) {
                        formErrors++;
                        formErrorMsg.append("Passwords must be longer than 5 characters");
                        formErrorMsg.append(newLine);
                    }
                }
                tempUser.setPassword(password.trim());
                
                String confirmPass = request.getParameter("confirmPass");
                if("".equals(confirmPass)) {
                        formErrors++;
                        formErrorMsg.append("Please retype the Password in Confirm Password");
                        formErrorMsg.append(newLine);
                }
                tempUser.setConfirmPass(confirmPass.trim());
                
                if(formErrors > 0) {
                    errorMsgSB.append("This form is incomplete. Please make sure the following fields are filled in:");
                    errorMsgSB.append(newLine);
                    errorMsgSB.append(formErrorMsg);
                    String errorMsg = errorMsgSB.toString();
                    request.setAttribute("errorMsg", errorMsg);
                    url = "/jsps/addUser_Participant.jsp";
                }              
                else {
                    if(users.isUseridTaken(userid)) {
                        tempUser.setUserid("");
                        tempUser.setPassword("");
                        tempUser.setConfirmPass("");
                        StringBuilder StringBuilder; /*append*/
                        StringBuilder = errorMsgSB.append("The userid \"").append(userid).append("\" has been taken. Please choose another userID. Thank you.");
                        String errorMsg = errorMsgSB.toString();
                        request.setAttribute("errorMsg", errorMsg);
                        url = "/jsps/addUser_other.jsp";
                    }
                    else if(!password.equals(confirmPass)) {
                        tempUser.setPassword("");
                        tempUser.setConfirmPass("");
                        errorMsgSB.append("The Passwords did not match. Please be sure both fields contain the same password. Thank you.");
                        String errorMsg = errorMsgSB.toString();
                        request.setAttribute("errorMsg", errorMsg);
                        url = "/jsps/addUser_other.jsp";
                    }
                    else {
                        url = "CF_ControllerServlet?action=saveUserToDatabase";             
                    }
                }                
            }
            rd = request.getRequestDispatcher(url);                            
        }
        rd.forward(request, response);
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
