/*
 * CF_UserInfoBean.java
 *
 * Created on July 6, 2003, 3:35 PM
 */

package com.personalblueprint.beans;
import  com.personalblueprint.helperClasses.*;
/**
 *
 * @author  bdunlop
 */
public class CF_UserInfoBean extends java.lang.Object implements java.io.Serializable {
    
    /** Holds value of property validated. */
    private boolean validated = false;
    
    /** Holds value of property person. */
    private CF_Person person = new CF_Person();     
    
    private String userid = "";
    
    private String password = "";
    
    /** Holds value of property confirmPass. */
    private String confirmPass = "";
    
    /** personNum, primary key in user table */
    private int personNum = 0;
    
    private int pbiTestComplete = 0;
    
    private int ibtTestComplete = 0;
    
    /** Creates new CF_UserInfoBean */
    public CF_UserInfoBean() {
    }
    
    public CF_UserInfoBean(String userid) {
        setUserid(userid);
    }
    
    public CF_UserInfoBean(String userid, String password, int pbiTestComplete, int ibtTestComplete, int personNum, CF_Person person) {
        setUserid(userid);
        setPassword(password);
        setPbiTestComplete(pbiTestComplete);
        setIbtTestComplete(ibtTestComplete);
        setPersonNum(personNum);
        setPerson(person);
        
    }
    /** Getter for property userid.
     * @return Value of property userid.
     */
    public String getUserid() {
        return this.userid;
    }    
    
    /** Setter for property userid.
     * @param userid New value of property id
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }
    
    /** Setter for property password.
     * @param password New value of property pass.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    /** Setter for property confirmPass.
     * @param confirmPass New value of property confirmPass.
     */
    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }
    
    public String getConfirmPass(){
        return this.confirmPass;
    }

    public int getPbiTestComplete() {
        return this.pbiTestComplete;
    }
    
    public void setPbiTestComplete(int which) {
        this.pbiTestComplete = which;
    }

    public int getIbtTestComplete() {
        return this.ibtTestComplete;
    }
    
    public void setIbtTestComplete(int which) {
        this.ibtTestComplete = which;
    }
    
    /** Setter for property validated.
     * @param validated New value of property validated.
     */
    public void setValidated(boolean validated) {
        this.validated = validated;
    }
    
    /** Getter for property personType.
     * @return Value of property personType.
     */
    public int getPersonType() {
        return this.getPerson().getPersonType();
    }
    
    /** Setter for property personType.
     * @param personType New value of property personType.
     */
    public void setPersonType(int personType) {
        this.getPerson().setPersonType(personType);
    }
     
    public CF_Person getPerson() {
        return this.person;
    }
    
    /** Setter for property person.
     * @param person New value of property person.
     *
     */
    public void setPerson(CF_Person person) {
        CF_Person oldPerson = this.person;
        this.person = person;
    }    
    
    /** Getter for property lastName.
     * @return Value of property lastName.
     */
    public String getLastName() {
        return this.getPerson().getLastName();
    }
    
    /** Setter for property lastName.
     * @param lastName New value of property lastName.
     */
    public void setLastName(String lastName) {
        this.getPerson().setLastName(lastName);
    }
   
    /** Getter for property firstName.
     * @return Value of property firstName.
     */
    public String getFirstName() {
        return this.getPerson().getFirstName();
    }
     
    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     */
    public void setFirstName(String firstName) {
        this.getPerson().setFirstName(firstName);
    }
    
    public String getDisplayName() {
        return this.getPerson().getDisplayName();
    }
     
    /** Setter for property displayName.
     * @param displayName New value of property firstName.
     */
    public void setDisplayName(String displayName) {
        this.getPerson().setDisplayName(displayName);
    }
    
    public String getMiddleInitial() {
        return this.getPerson().getMiddleInitial();
    }
    
    public void setMiddleInitial(String middleInitial) {
        this.getPerson().setMiddleInitial(middleInitial);
    }
     
    public String getPresentingIssue() {
        return this.getPerson().getPresentingIssue();
    }
    
    public void setPresentingIssue(String presentingIssue) {
        this.getPerson().setPresentingIssue(presentingIssue);
    }

    public String getHoursWorked() {
        return this.getPerson().getHoursWorked();
    }

    public void setHoursWorked(String hoursWorked) {
        this.getPerson().setHoursWorked(hoursWorked);
    }
    
    public String getIsMarried(){
        return this.getPerson().getIsMarried();
    }
    
    public void setIsMarried(String isMarried){
        this.getPerson().setIsMarried(isMarried);
    }

    public String getYearsMarried() {
        return this.getPerson().getYearsMarried();
    }

    public void setYearsMarried(String yearsMarried) {
        this.getPerson().setYearsMarried(yearsMarried);
    }
            
    public String getGender(){
        return this.getPerson().getGender();
    }
    
    public void setGender(String gender){
        this.getPerson().setGender(gender);
    }

    public String getDobDay(){
        return this.getPerson().getDobDay();
    }

    public void setDobDay(String dobDay){
        this.getPerson().setDobDay(dobDay);
    }    

    public String getDobMonth(){
        return this.getPerson().getDobMonth();
    }

    public void setDobMonth(String dobMonth){
        this.getPerson().setDobMonth(dobMonth);
    }    

    public String getDobYear(){
        return this.getPerson().getDobYear();
    }

    public void setDobYear(String dobYear){
        this.getPerson().setDobYear(dobYear);
    }

    public String getBirthDate(){
        return this.getPerson().getBirthDate();
    }
    
    public void setBirthDate(String birthDate){
        this.getPerson().setBirthDate(birthDate);
    }    
     
    public String getOccupation(){
        return this.getPerson().getOccupation();
    }
        
    public String getEducation(){
        return this.getPerson().getEducation();
    }
    
    public void setEducation(String education){
        this.getPerson().setEducation(education);
    }
    
    public void setOccupation(String occupation){
        this.getPerson().setOccupation(occupation);
    }
 
    public String getEmployPosition() {
        return this.getPerson().getEmployPosition();
    }
    
    public void setEmployPosition(String employPosition) {
        this.getPerson().setEmployPosition(employPosition);
    }
    
    public String getHowLongEmployed() {
        return this.getPerson().getHowLongEmployed();
    }

    public void setHowLongEmployed(String howLongEmployed) {
        this.getPerson().setHowLongEmployed(howLongEmployed);
    }

    public String getEthnicity() {
        return this.getPerson().getEthnicity();
    }

    public void setEthnicity(String ethnicity) {
        this.getPerson().setEthnicity(ethnicity);
    }
    
    public String getIsNative() {
        return this.getPerson().getIsNative();
    }

    public void setIsNative(String isNative) {
        this.getPerson().setIsNative(isNative);
    }
    
    public String getYearsInUSA() {
        return this.getPerson().getYearsInUSA();
    }

    public void setYearsInUSA(String yearsInUSA) {
        this.getPerson().setYearsInUSA(yearsInUSA);
    }
     
    /** Getter for property email.
     * @return Value of property email.
     */
    public String getEmail() {
        return this.getPerson().getEmail();
    }
    
    /** Setter for property email.
     * @param email New value of property email.
     */
    public void setEmail(String email) {
        this.getPerson().setEmail(email);
    }    
     
    /** Getter for property street.
     * @return Value of property street.
     */
    public String getStreet() {
        return this.getPerson().getStreet();
    }
    
    /** Setter for property street.
     * @param street New value of property street.
     */
    public void setStreet(String street) {
        this.getPerson().setStreet(street);
    }
    
    /** Getter for property aptOrSuite.
     * @return Value of property aptOrSuite.
     */
    public String getSuiteOrApt() {
        return this.getPerson().getSuiteOrApt();
    }
    
    /** Setter for property suiteOrApt.
     * @param suiteOrApt New value of property aptOrSuite.
     */
    public void setSuiteOrApt(String suiteOrApt) {
        this.getPerson().setSuiteOrApt(suiteOrApt);
    }
    
    /** Getter for property city.
     * @return Value of property city.
     */
    public String getCity() {
        return this.getPerson().getCity();
    }
    
    /** Setter for property city.
     * @param city New value of property city.
     */
    public void setCity(String city) {
        this.getPerson().setCity(city);
    }
    
    /** Getter for property state.
     * @return Value of property state.
     */
    public String getStateCode() {
        return this.getPerson().getStateCode();
    }
    
    /** Setter for property stateCode.
     * @param stateCode New value of property state.
     */
    public void setStateCode(String stateCode) {
        this.getPerson().setStateCode(stateCode);
    }
    
    /** Getter for property zipOrPostal.
     * @return Value of property zipOrPostal.
     */
    public String getZipOrPostalCode() {
        return this.getPerson().getZipOrPostalCode();
    }
    
    /** Setter for property zipOrPostalCode.
     * @param zipOrPostalCode New value of property zipOrPostal.
     */
    public void setZipOrPostalCode(String zipOrPostalCode) {
        this.getPerson().setZipOrPostalCode(zipOrPostalCode);
    }

    /** Getter for property country.
     * @return Value of property country.
     */    
    public String getCountry() {
        return this.getPerson().getCountry();
    }
    
    /** Setter for property country.
     * @param country New value of property country.
     */
    public void setCountry(String country) {
        this.getPerson().setCountry(country);
    }
    
    /** Getter for property phone.
     * @return Value of property phone.
     */
    public String getPhone() {
        return this.getPerson().getPhone().getPhoneNumber();
    }

    public String getAreaCode() {
        return this.getPerson().getAreaCode();
    }
    
    /** Setter for property areaCode.
     * @param areaCode New value of property areaCode.
     */
    public void setAreaCode(String areaCode) {
        this.getPerson().setAreaCode(areaCode);
    }
    
    /** Getter for property prefix.
     * @return Value of property prefix.
     */
    public String getPrefix() {
        return this.getPerson().getPrefix();
    }
    
    /** Setter for property prefix.
     * @param prefix New value of property prefix.
     */
    public void setPrefix(String prefix) {
        this.getPerson().setPrefix(prefix);
    }
    
    /** Getter for property number.
     * @return Value of property number.
     */
    public String getNumber() {
        return this.getPerson().getNumber();
    }
    
    /** Setter for property number.
     * @param number New value of property number.
     */
    public void setNumber(String number) {
        this.getPerson().setNumber(number);
    }
    
    public String getExtension() {
        return this.getPerson().getExtension();
    }
    
    /** Setter for property number.
     * @param extension New value of property number.
     */
    public void setExtension(String extension) {
        this.getPerson().setExtension(extension);
    }
  
   /** setter method for personNum
    * @param personNum New value of property personNum.
    *
    */
    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }
    
    /** getter method for personNum 
     *@return Value of property personNum. 
     *
     */
    public int getPersonNum() {
        return this.personNum;
    }
}
