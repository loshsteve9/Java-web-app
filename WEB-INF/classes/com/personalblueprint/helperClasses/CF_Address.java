/*
 * CF_Address.java
 *
 * Created on June 29, 2003, 10:45 PM
 */

package com.personalblueprint.helperClasses;
import java.beans.*;
/** Class to hold a single address.  Includes set/get methods for
 * street, suite/apt, city, state code, zip, and the database key
 * @author bdunlop
 */
public class CF_Address extends java.lang.Object implements java.io.Serializable{
    
    /** street name */    
    private String street = "";
    
    /** this 2nd address line can be used for a
     * suite, apt, or ignored
     */    
    private String suiteOrApt = "";
    
    /** city name */    
    private String city = "";
    
    /** 2 character state code */    
    private String stateCode = "";
    
    /** the zip code in U.S., or postal code in Canada */    
    private String zipOrPostalCode = "";
    
    private String country = "";
    
    /** primary key in address table - allows lookup, update, etc. */    
    private int dbKey = 0;
    
    private String addrLine2 = "";
    
    private String addrLine1 ="";
    
    /** Creates a new instance of CF_Address */
    public CF_Address() {
    }
    
    /** Creates a new instance of CF_Address that's fully initialized
     * @param dbKey primary key for address table
     * @param street street name
     * @param suiteOrApt suite, apr, or null
     * @param city city name
     * @param stateCode 2 character state code
     * @param zipOrPostalCode zip code for U.S., postal code in Canada
     */
    public CF_Address(int dbKey, String street, String suiteOrApt, String city, String stateCode, String zipOrPostalCode, String country) {
        this.dbKey = dbKey;
        this.street = street;
        this.suiteOrApt = suiteOrApt;
        this.city = city;
        this.stateCode = stateCode;
        this.zipOrPostalCode = zipOrPostalCode;
        this.country = country;
    }
    
    /** sets the value for a street name
     * @param street street name
     */    
    public void setStreet(String street) {
        this.street = street.trim();
    }
    
    /** setter method for suite/apt
     * @param suiteOrApt suite or apt, or null
     */    
    public void setSuiteOrApt(String suiteOrApt) {
        this.suiteOrApt = suiteOrApt.trim();
    }
    
    /** setter method for city name
     * @param city city name
     */    
    public void setCity(String city) {
        this.city = city.trim();
    }
    
    /** setter method for state code
     * @param stateCode state code
     */    
    public void setStateCode(String stateCode) {
        this.stateCode = stateCode.trim();
    }
    
    /** setter method for zip or postal code
     * @param zipOrPostalCode zip or postal code
     */    
    public void setZipOrPostalCode(String zipOrPostalCode) {
        this.zipOrPostalCode = zipOrPostalCode.trim();
    }
    
    public void setCountry(String country) {
        this.country = country.trim();
    }
    
    /** setter method for db primary key - allows for update sql statement
     * @param dbKey database primary ket to address table
     */    
    public void setDbKey(int dbKey) {
    }
    
    /** getter method for the street name
     * @return returns the street name
     */    
    public String getStreet() {
        return street;
    }
    
    /** getter method for suite or apt
     * @return returns suite or apt, or null
     */    
    public String getSuiteOrApt() {
        return suiteOrApt;
    }
    
    /** getter method for city name
     * @return returns the city name
     */    
    public String getCity() {
        return city;
    }
    
    /** getter method for the 2 character state code
     * @return 2 character state code
     */    
    public String getStateCode() {
        return stateCode;
    }
    
    /** getter method for the zip or postal code
     * @return zip or postal code
     */    
    public String getZipOrPostalCode() {
        return zipOrPostalCode;
    }
    
    public String getCountry() {
        return country;
    }
    
    /** getter method for the database primary key in the address table
     * @return returns the lookup key
     */    
    public int getDbKey() {
        return dbKey;
    }
    
    public boolean isValid() {
        return true;
    }
    
    public String getAddrLine1() {
        return addrLine1;
    }
    
    
    public String getAddrLine2() {
        return addrLine2;
    }
    
    public void setAddrLine1(String address) {
        addrLine1 = address.trim();
        
    }
    
    public void setAddrLine2(String address) {
        addrLine2 = address.trim();
    }
    
    public boolean isEmpty() {
        if(street.length() == 0 &&
            suiteOrApt.length() == 0 &&
            city.length() == 0 &&
            stateCode.length() == 0 &&
            zipOrPostalCode.length() == 0 &&
            addrLine2.length() == 0 &&
            addrLine1.length() == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void fillObsoleteDataFields() {
        setAddrLine1(street);
        setAddrLine2(city.toString() + ", " + stateCode.toString() + "  " + zipOrPostalCode.toString());
    }
    
}
