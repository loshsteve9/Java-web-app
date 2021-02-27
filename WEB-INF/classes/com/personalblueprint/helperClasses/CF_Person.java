/*
 * CF_Person.java
 *
 * Created on June 29, 2003, 12:01 AM
 */

package com.personalblueprint.helperClasses;

import java.util.*;
import java.sql.*;
import java.beans.*;
import java.text.*;

/**
 *
 * @author  bdunlop
 */
public class CF_Person extends java.lang.Object implements java.io.Serializable{
    
    public static final int TYPE_BLUEPRINT_ONSITE_LOGIN_USER = 6;
    
    public static final int TYPE_BLUEPRINT_ONSITE_PARTICIPANT_USER = 4;
    
    public static final int TYPE_BLUEPRINT_PARTICIPANT_USER = 3;
    
    public static final int TYPE_BLUEPRINT_ADMIN_USER = 2;
    
    public static final int TYPE_BLUEPRINT_SYSADMIN_USER = 1;
    
    private int personType = 0;

    private String lastName = "";
    
    private String firstName = "";
        
    private String middleInitial = "";
    
    private String displayName = "";
        
    /** Holds value of property name. */
    private String name = "";

    private String presentingIssue = "";
    
    private String hoursWorked = "";
    
    /** Holds value of property isMarried. */
    private String isMarried = "";
    
    /** Holds value of property yearsMarried. */
    private String yearsMarried = "";
     
    private String gender = "";
    
    private String dobMonth = "";
    
    private String dobDay = "";
    
    private String dobYear = "";
   
    private String birthDate = "";

    /** Holds value of property age. */
    private int age = 0;
    
    private String education = "";
    
    private String occupation = "";
    
    /** Holds value of property employPosition. */
    private String employPosition = "";
    
    private String howLongEmployed = "";
    
    private String ethnicity = "";
   
    /** Holds value of property nativeLanguage. */
    private String isNative = "";

    private String yearsInUSA = "";
    
    private String email ="";
    
    private CF_Address address = new CF_Address();
    
    private CF_Phone phone = new CF_Phone();
   
    
    /** Creates a new instance of CF_Person */
    public CF_Person() {
    }
    
    public CF_Person(int type) {
        setPersonType(type);
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }
    
    public int getPersonType() {
        return this.personType;
    }   
    
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getMiddleInitial() {
        return this.middleInitial;
    }
    
    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }
    
    public String getDisplayName() {
        return this.displayName;        
    }
    
    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     */
    public String getName() {
        return this.name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }
     
    public String getPresentingIssue() {
        return this.presentingIssue;
    }
    
    public void setPresentingIssue(String presentingIssue) {
        this.presentingIssue = presentingIssue;
    }

    public String getHoursWorked() {
        return this.hoursWorked;
    }

    public void setHoursWorked(String hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
    
    /** Getter for property isMarried.
     * @return Value of property isMarried.
     *
     */
    public String getIsMarried() {
        return this.isMarried;
    }
    
    public void setIsMarried(String isMarried) {
        this.isMarried = isMarried;
    }
    
    /** Getter for property yearsMarried.
     * @return Value of property yearsMarried.
     */
    public String getYearsMarried() {
        return this.yearsMarried;
    }
    
    /** Setter for property yearsMarried.
     * @param yearsMarried New value of property yearsMarried.
     */
    public void setYearsMarried(String yearsMarried) {
        this.yearsMarried = yearsMarried;
    }
    
    public String getGender(){
        return this.gender;
    }
     
    public void setGender(String gender){
        this.gender = gender;
    }
     
    public String getDobDay(){
        return this.dobDay;
    }
    
    public void setDobDay(String dobDay){
        this.dobDay = dobDay;
    }    
          
    public String getDobMonth(){
        return this.dobMonth;
    }
    
    public void setDobMonth(String dobMonth){
        this.dobMonth = dobMonth;
    }    
          
    public String getDobYear(){
        return this.dobYear;
    }
    
    public void setDobYear(String dobYear){
        this.dobYear = dobYear;
    }    
     
    public String getBirthDate() {
        return this.birthDate;
    }
    
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }     

    /** Getter for property age.
     * @return Value of property age.
     */
    public int getAge() {
        return this.age;
    }
    
    /** Setter for property age.
     * @param age New value of property age.
     */
    public void setAge(int age) {
        this.age = age;
    }
        
    public String getEducation(){
        return this.education;
    }
    
    public void setEducation(String education){
        this.education = education;
    }
        
    public String getOccupation(){
        return this.occupation;
    }
    
    public void setOccupation(String occupation){
        this.occupation = occupation;
    }
    
    /** Getter for property employPosition.
     * @return Value of property employPosition.
     */
    public String getEmployPosition() {
        return this.employPosition;
    }

    public void setEmployPosition(String employPosition) {
        this.employPosition = employPosition;        
    }
    
    public String getHowLongEmployed() {
        return this.howLongEmployed;
    }

    public void setHowLongEmployed(String howLongEmployed) {
        this.howLongEmployed = howLongEmployed;
    }

    public String getEthnicity() {
        return this.ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }
    
    public String getIsNative() {
        return this.isNative;
    }

    public void setIsNative(String isNative) {
        this.isNative = isNative;
    }
    
    public String getYearsInUSA() {
        return this.yearsInUSA;
    }

    public void setYearsInUSA(String yearsInUSA) {
        this.yearsInUSA = yearsInUSA;
    }

    /** Getter for property email.
     * @return Value of property email.
     */
    public String getEmail() {
        return this.email;
    }
    
    /** Setter for property email.
     * @param email New value of property email.
     */
    public void setEmail(String email) {
        this.email = email;
    }    
               
    public CF_Address getAddress() {
        return this.address;
    }
   
    /** Getter for property street.
     * @return Value of property street.
     */
    public String getStreet() {
        return this.address.getStreet();
    }
    
    /** Setter for property street.
     * @param street New value of property street.
     */
    public void setStreet(String street) {
        this.address.setStreet(street);
    }
    
    /** Getter for property aptOrSuite.
     * @return Value of property aptOrSuite.
     */
    public String getSuiteOrApt() {
        return this.address.getSuiteOrApt();
    }
    
    /** Setter for property aptOrSuite.
     * @param aptOrSuite New value of property aptOrSuite.
     */
    public void setSuiteOrApt(String suiteOrApt) {
        this.address.setSuiteOrApt(suiteOrApt);
    }
    
    /** Getter for property city.
     * @return Value of property city.
     */
    public String getCity() {
        return this.address.getCity();
    }
    
    /** Setter for property city.
     * @param city New value of property city.
     */
    public void setCity(String city) {
        this.address.setCity(city);
    }
    
    /** Getter for property state.
     * @return Value of property state.
     */
    public String getStateCode() {
        return this.address.getStateCode();
    }
    
    /** Setter for property state.
     * @param state New value of property state.
     *
     */
    public void setStateCode(String stateCode) {
        this.address.setStateCode(stateCode);
    }
    
    /** Getter for property zipOrPostal.
     * @return Value of property zipOrPostal.
     */
    public String getZipOrPostalCode() {
        return this.address.getZipOrPostalCode();
    }
    
    /** Setter for property zipOrPostal.
     * @param zipOrPostal New value of property zipOrPostal.
     */
    public void setZipOrPostalCode(String zipOrPostalCode) {
        this.address.setZipOrPostalCode(zipOrPostalCode);
    }
    
    /** Getter for property country.
     * @return Value of property country.
     */
    public String getCountry() {
        return this.address.getCountry();
    }
    
    /** Setter for property country.
     * @param country New value of property country.
     */
    public void setCountry(String country) {
        this.address.setCountry(country);
    }

    public CF_Phone getPhone() {
        return this.phone;
    }
    
    public String getAreaCode() {
        return this.phone.getAreaCode();
    }
    
    /** Setter for property city.
     * @param city New value of property city.
     */
    public void setAreaCode(String areaCode) {
        this.phone.setAreaCode(areaCode);
    }
    
    public String getPrefix() {
        return this.phone.getPrefix();
    }
    
    /** Setter for property city.
     * @param city New value of property city.
     */
    public void setPrefix(String prefix) {
        this.phone.setPrefix(prefix);
    }
    
    public String getNumber() {
        return this.phone.getNumber();
    }
    
    /** Setter for property city.
     * @param city New value of property city.
     */
    public void setNumber(String number) {
        this.phone.setNumber(number);
    }
    
    public String getExtension() {
        return this.phone.getExtension();
    }
    
    /** Setter for property city.
     * @param city New value of property city.
     */
    public void setExtension(String extension) {
        this.phone.setExtension(extension);
    }
    
    public boolean isValid(String newUserType)
    {
        boolean valid = false;
        boolean keepOn = true;
        String errorMsg = "";

        if("Participant".equals(newUserType))
        {
            if(firstName.length() == 0)
            {
                keepOn = false;
                errorMsg = "Please supply a First Name";
            }

            if(keepOn)
            {
                if(lastName.length() == 0)
                {
                    keepOn = false;
                    errorMsg = "Please supply a Last Name";
                }
            }

            if(keepOn)
            {
                if(occupation.length() == 0)
                {
                    keepOn = false;
                    errorMsg = "Please supply an Occupation";
                }
            }

            if(keepOn)
            {
                if(employPosition.length() == 0)
                {
                    keepOn = false;
                    errorMsg = "Please supply an Employ";
                }
            }

            if(keepOn)
            {
                if(isNative.length() == 0)
                {
                    keepOn = false;
                    errorMsg = "Please state if you are Native to the USA";
                }
            }

            if(keepOn)
            {
                if(gender.length() == 0)
                {
                    keepOn = false;
                    errorMsg = "Please supply your Gender";
                }
            }

            if(keepOn)
            {
                if(isMarried.length() == 0)
                {
                    keepOn = false;
                    errorMsg = "Please provide Marital Status";
                }
            }

            if(keepOn) // did all tests pass
            {
                valid = true;
            }
        }
        else
        {
            if(firstName.length() == 0)
            {
                keepOn = false;
                errorMsg = "Please supply a First Name";
            }

            if(keepOn)
            {
                if(lastName.length() == 0)
                {
                    keepOn = false;
                    errorMsg = "Please supply a Last Name";
                }
            }

            if(keepOn)
            {
                if(employPosition.length() == 0)
                {
                    keepOn = false;
                    errorMsg = "Please supply an Employ";
                }
            }

            if(keepOn) // did all tests pass
            {
                valid = true;
            }            
        }
        return valid;
    }  
}
