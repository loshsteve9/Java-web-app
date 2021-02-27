/*
 * CF_LoadProps.java
 *
 * Created on May 24, 2007, 2:31 PM
 */

package com.personalblueprint.helperClasses;

import java.util.*;
import java.io.*;

/**
 *
 * @author  Glenn Hummel
 */
public class CF_Props {
    
    private String jdbcConnection = "";
    private String indexPage = "";
    
    private String path = "";
    private Properties wcgProps = new Properties();
    
    public CF_Props() {
        findPath();
        loadProperties();
    }    
    public void loadProperties() {
        try {
            FileInputStream fis = new FileInputStream(path + "wcgProps.props");
            wcgProps.load(fis);
        }catch (IOException e) {
        }
    }    
    private void findPath() {
        String fullPath = getClass().getProtectionDomain().getCodeSource().getLocation().toString().substring(5);
        int webinfLoc = fullPath.indexOf("WEB");
        path = fullPath.substring(0, webinfLoc);
    }    
    public String getDBPath() {
        jdbcConnection = wcgProps.getProperty("jdbcConnection");
        return this.jdbcConnection;
    }
    public String getDirPath() {
         return this.path;
    }
}
