/*
 * CF_Phone.java
 *
 * Created on June 28, 2003, 11:05 PM
 */

package com.personalblueprint.helperClasses;
import java.beans.*;
/**
 *
 * @author  bdunlop
 */
public class CF_Phone extends Object implements java.io.Serializable {
    
    
    /** Holds value of property countryCode. */
    private String countryCode = "";
 
    /** Holds value of property areaCode. */
    private String areaCode = "";
    
    /** Holds value of property prefix. */
    private String prefix = "";
    
    /** Holds value of property number. */
    private String number = "";
    
    /** Holds value of property extension. */
    private String extension = "";
    
    /** Holds value of property phoneNumber. */
    private String phoneNumber = "";
    
    /** Holds value of property type. */
    private int type = 0;
    
    
    /** Creates new CF_Phone */
    public CF_Phone() {
    }
    
    
    /** Getter for property countryCode.
     * @return Value of property countryCode.
     *
     */
    public String getCountryCode() {
        return this.countryCode;
    }
    
    /** Setter for property countryCode.
     * @param countryCode New value of property countryCode.
     *
     * @throws PropertyVetoException
     *
     */
    public void setCountryCode(String countryCode) {
        String oldCountryCode = this.countryCode;
        this.countryCode = countryCode.trim();
    }
    
    /** Getter for property areaCode.
     * @return Value of property areaCode.
     *
     */
    public String getAreaCode() {
        return this.areaCode;
    }
    
    /** Setter for property areaCode.
     * @param areaCode New value of property areaCode.
     *
     * @throws PropertyVetoException
     *
     */
    public void setAreaCode(String areaCode){
        String oldAreaCode = this.areaCode;
        this.areaCode = areaCode.trim();
    }
    
    /** Getter for property prefix.
     * @return Value of property prefix.
     *
     */
    public String getPrefix() {
        return this.prefix;
    }
    
    /** Setter for property prefix.
     * @param prefix New value of property prefix.
     *
     * @throws PropertyVetoException
     *
     */
    public void setPrefix(String prefix){
        String oldPrefix = this.prefix;
        this.prefix = prefix.trim();
    }
    
    /** Getter for property number.
     * @return Value of property number.
     *
     */
    public String getNumber() {
        return this.number;
    }
    
    /** Setter for property number.
     * @param number New value of property number.
     *
     * @throws PropertyVetoException
     *
     */
    public void setNumber(String number){
        String oldNumber = this.number;
        this.number = number.trim();
     }
    
    /** Getter for property extension.
     * @return Value of property extension.
     *
     */
    public String getExtension() {
        return this.extension;
    }
    
    /** Setter for property extension.
     * @param extension New value of property extension.
     *
     * @throws PropertyVetoException
     *
     */
    public void setExtension(String extension){
        String oldExtension = this.extension;
        this.extension = extension.trim();
    }
    
    /** Getter for property type.
     * @return Value of property type.
     *
     */
    public int getType() {
        return this.type;
    }
    
    /** Setter for property type.
     * @param type New value of property type.
     *
     */
    public void setType(int type) {
        int oldType = this.type;
        this.type = type;
    }
    
    public boolean isValid() {
        String test = phoneNumber.trim();
        if(test.length() >0)
            return true;
        else
            return false;
    }
    
    /** Getter for property phoneNumber.
     * @return Value of property phoneNumber.
     *
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    
    /** Setter for property phoneNumber.
     * @param phoneNumber New value of property phoneNumber.
     *
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.trim();
        
    }
    
    public boolean isEmpty() {
    
        if( countryCode.length() == 0 &&
            areaCode.length() == 0 && 
            prefix.length() == 0 &&
            number.length() == 0 &&
            extension.length() == 0 &&
            phoneNumber.length() == 0)
        { 
            return true;
        }
        else
            return false;
    }
    
    public void fillObsoleteDataFields() {
        
        if(phoneNumber.length() == 0)
        {
            StringBuffer s = new StringBuffer();
            s.append(this.areaCode + this.prefix + this.number);
            phoneNumber = s.toString();
        }
    }
    
    
}
