/*
 * CF_Subscale.java
 *
 * Created on July 28, 2003, 12:40 PM
 */

package com.personalblueprint.helperClasses;

/**
 *
 * @author  The Digital Medium - Glenn Hummel
 */
public class CF_Subscale extends java.lang.Object implements java.io.Serializable {
    
    private int subscaleId = 0;
    
    private String subscaleName = "";
    
    private double subscaleMean = 0.0;
    
    private double subscaleStandardDev = 0.0;
    
    private double questionsPerScale = 0.0;
    
    private double[] scaleCutoffs;
    
    private int subscaleCount = 0;
    
    public CF_Subscale(java.lang.String subscaleName, int subscaleId, double[] scaleCutoffs, int questionsPerScale) {
        this.subscaleName = subscaleName;
        this.subscaleId = subscaleId;
        this.scaleCutoffs = scaleCutoffs;
        this.questionsPerScale = questionsPerScale;
    }
    
    public CF_Subscale(java.lang.String subscaleName, double subscaleMean, double subscaleStandardDev, int subscaleId) {
        this.subscaleName = subscaleName;
        this.subscaleMean = subscaleMean;
        this.subscaleStandardDev = subscaleStandardDev;
        this.subscaleId = subscaleId;
    }
    
    public CF_Subscale(java.lang.String subscaleName, int subscaleId) {
        this.subscaleName = subscaleName;
        this.subscaleId = subscaleId;
    }
    
    public CF_Subscale(int subscaleId) {
        this.subscaleId = subscaleId;
    }
    
    public String toString() {
        String retValue;
        
        retValue = super.toString();
        return retValue;
    }
    
    public String getSubscaleName() {
        return this.subscaleName;
    }
    
    public double getSubscaleMean() {
        return this.subscaleMean;
    }
    
    public double getSubscaleStandardDev() {
        return this.subscaleStandardDev;
    }
    
    public int getSubscaleId() {
        return this.subscaleId;
    }
    
    public double[] getScaleCutoffs() {
        return this.scaleCutoffs;
    }
    
    public double getQuestionsPerScale() {
        return this.questionsPerScale;
    }
}
