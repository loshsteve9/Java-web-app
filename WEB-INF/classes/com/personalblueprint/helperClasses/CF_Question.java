/*
 * CF_Question.java
 *
 * Created on July 28, 2003, 12:31 PM
 */

package com.personalblueprint.helperClasses;

/**
 *
 * @author  bdunlop
 */
public class CF_Question extends Object implements java.io.Serializable {
    
    private Integer questionId = new Integer(0);
    
    private String question = "";
    
    private CF_Subscale subscale = null;
    
    /** Holds value of property answer. */
    private Integer answer = new Integer(0);
    
    /** Creates a new instance of CF_Question */
    public CF_Question() {
    }
    
    public CF_Question(java.lang.String question, Integer questionId, CF_Subscale subscale) {
        this.question = question;
        this.questionId = questionId;
        this.subscale = subscale;
    }
    public CF_Question(java.lang.String question, int questionId, CF_Subscale subscale) {
        this.question = question;
        this.questionId = new Integer(questionId);
        this.subscale = subscale;
    }
    public CF_Question(java.lang.String question, int questionId) {
        this.question = question;
        this.questionId = new Integer(questionId);
    }

    public Integer getQuestionId() {
        return this.questionId;
    }
    
    public int getSubscaleId() {
        return this.subscale.getSubscaleId();
    }
    
    public String getQuestion() {
        return this.question;
    }
    
    public String getSubscaleName() {
        return this.subscale.getSubscaleName();
    }
    
    public double getSubscaleMean() {
        return this.subscale.getSubscaleMean();
    }
    
    public double getSubscaleStandardDev() {
        return this.subscale.getSubscaleStandardDev();
    }
  
    /** Getter for property answer.
     * @return Value of property answer.
     *
     */
    public Integer getAnswer() {
        return this.answer;
    }
    
    /** Setter for property answer.
     * @param answer New value of property answer.
     *
     */
    public void setAnswer(Integer answer) {
        this.answer = answer;
    }
    
}
