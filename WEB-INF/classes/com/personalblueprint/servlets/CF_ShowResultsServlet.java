/*
 * CF_ShowResultsServlet.java
 *
 * Created on July 21, 2003, 8:17 PM
 */

package com.personalblueprint.servlets;

import com.personalblueprint.beans.*;
import com.personalblueprint.helperClasses.*;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

import org.jfree.chart.*;
import org.jfree.chart.servlet.*;

/**
 *
 * @author  bdunlop
 * @version
 */
public class CF_ShowResultsServlet extends HttpServlet {
    
        private Hashtable pbiSubscaleStats = new Hashtable();
        private Hashtable ibtSubscaleStats = new Hashtable();
        private Hashtable threeCsStats = new Hashtable();
        private Hashtable sixCompStats = new Hashtable();
        
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
    throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        CF_TestsTaken profListTestTakers = new CF_TestsTaken();
        CF_UserInfoBean user = (CF_UserInfoBean) session.getAttribute("user");
        int profUserNum = user.getPersonNum();
        String userid = user.getUserid();
        
        String personNum = request.getParameter("personNum");
        int testUserNum = Integer.parseInt(personNum);
        
        // an array of pbi I's
        int pbiRevNums[] = { 1, 2, 3, 4, 6, 7, 8, 9, 10, 12, 17, 18, 20, 22, 23, 24, 25, 
                            27, 28, 30, 32, 35, 36, 37, 38, 39, 40, 41, 43, 49, 50, 
                            56, 57, 58, 61, 63, 64, 65, 66, 70, 71, 72, 73, 76, 78, 79,
                            81, 83, 85, 87, 88, 90, 91, 93, 94, 95, 97, 98, 99, 100, 
                            102, 104, 105, 107, 109, 110, 111, 112, 114, 115, 117, 118, 119,  
                            121, 122, 123, 127, 128, 130, 131, 135, 138, 139, 140, 
                            141, 143, 144, 145, 147, 148, 149, 150, 151, 154, 156, 157, 158, 160, 
                            161, 162, 163, 164, 165, 166, 168, 169, 171, 172, 174, 175, 177, 179, 180 };
        // an array of pbi R's
        /* int pbiRevNums[] = { 5, 11, 13, 14, 15, 16, 19, 21, 26, 29, 31, 33, 34, 42, 
                             44, 45, 46, 47, 48, 51, 52, 53, 54, 55, 59, 60, 62, 67, 
                             68, 69, 74, 75, 77, 80, 82, 84, 86, 89, 92, 96, 101, 103, 106, 
                             108, 113, 116, 120, 124, 125, 126, 129, 132, 133, 134, 136, 
                             137, 142, 146, 152, 153, 155, 159, 167, 170, 173, 176, 178 };
         */
        ArrayList pbiReverseValues = new ArrayList();
        for(int pr = 0; pr < pbiRevNums.length; pr ++) {
            int prnum = pbiRevNums[pr];
            Integer pra = prnum;
            pbiReverseValues.add(pra);
        }

        int ibtRevNums[] = { 4, 5, 11, 14, 15, 16, 17, 20, 22, 25, 29, 30, 31, 32, 
                             35, 36, 37, 39, 40, 41, 43, 44, 45, 48, 52, 54, 56, 
                             57, 58, 59, 60, 61, 63, 64, 65, 68, 70, 74, 77, 80, 83, 
                             85, 86, 87, 88, 91, 92, 93, 94, 95, 97, 98, 99, 100 };
        ArrayList ibtReverseValues = new ArrayList();
        for(int ir = 0; ir < ibtRevNums.length; ir ++) {
            int num = ibtRevNums[ir];
            Integer ira = num;
            ibtReverseValues.add(ira);
        }
        
        // an array of 6 CC I's
        // reversed questions not inccluded are reversed during result tallying
        int sixCCRevNums[] = { 7, 9, 23, 27, 36, 40, 43, 58, 65, 72, 97, 98, 99, 100, 
                                107, 109, 110, 114, 138, 144, 160, 161, 164, 172, 177, 179 };
        // an array of 6 CC R's
        /* int sixCCRevNums[] = { 5, 13, 14, 16, 19, 21, 23, 26, 29, 31, 33, 34, 44, 
                               45, 46, 47, 48, 51, 52, 55, 59, 62, 67, 68, 69, 75, 
                               77, 80, 84, 89, 92, 100, 101, 106, 108, 113, 114, 116, 
                               120, 124, 125, 134, 137, 142, 159, 167, 170, 176, 178 };
         */
        ArrayList sixCoreCompReverseValues = new ArrayList();
        for(int sxr = 0; sxr < sixCCRevNums.length; sxr ++) {
            int sxnum = sixCCRevNums[sxr];
            Integer sxa = sxnum;
            sixCoreCompReverseValues.add(sxa);
        }
        // an array of 3 CC I's
        int threeCCRevNums[] = { 8, 9, 27, 38, 56, 61, 66, 72, 78, 81, 84, 88, 98, 110, 121, 128, 149, 165, 172, 177, 179 };
        // an array of 3 CC R's
        /* int threeCCRevNums[] = { 132, 155 };
         */
        ArrayList threeCsCompReverseValues = new ArrayList();
        for(int txr = 0; txr < threeCCRevNums.length; txr ++) {
            int txnum = threeCCRevNums[txr];
            Integer tha = txnum;
            threeCsCompReverseValues.add(tha);
        }
        
        int pbiTestDone = 0;
        int ibtTestDone = 0;
        
        if(profUserNum == 2) {
            profListTestTakers = (CF_TestsTaken) session.getAttribute("names");
        }else {
            profListTestTakers = (CF_TestsTaken) session.getAttribute("testsTaken");
        }
        
        if(profListTestTakers != null) {
            Hashtable namesList = profListTestTakers.getUserTests();
            int size = namesList.size()+1;
            for(int i = 1; i < size; i++)
            {
                Integer key = i;
                String person = (String) namesList.get(key);
                int colonPos = person.indexOf(':');
                int dollarPos = person.indexOf('$');
                String personid = person.substring(colonPos + 1, dollarPos);
                int tempPersonNum = Integer.parseInt(personid);
                if(tempPersonNum == testUserNum) {
                    int asterikPos = person.indexOf('*');
                    int poundPos = person.indexOf('#');
                    String pbiStatus = person.substring(dollarPos + 1, asterikPos);
                    String ibtStatus = person.substring(asterikPos + 1, poundPos);
                    pbiTestDone = Integer.parseInt(pbiStatus);
                    ibtTestDone = Integer.parseInt(ibtStatus);
                    break;
                }
            }
        }else {
            pbiTestDone = user.getPbiTestComplete();
            ibtTestDone = user.getIbtTestComplete();
        }
        
        CF_TestManagerBean testBean = (CF_TestManagerBean) session.getAttribute("testBean");
        Hashtable pbiAnswers = new Hashtable();
        Hashtable ibtAnswers = new Hashtable();

      // ****  Obtain PBI results  ****
        if(pbiTestDone > 0) {
            loadAnswers(pbiAnswers, testUserNum, "pbi");
            
            int pbiTotalSubscales = pbiSubscaleStats.size();
            
            int pbiAnswersSize = pbiAnswers.size() + 1;
            
            double[] pbiSubscaleSums;
            pbiSubscaleSums = new double[pbiTotalSubscales];
            
            double[] pbiScaleCutoffResults;
            pbiScaleCutoffResults = new double[pbiTotalSubscales];

            String[] pbiSubscaleNames;
            pbiSubscaleNames = new String[pbiTotalSubscales];
            
            String pbiInterpretationsOnly = "";

            Hashtable pbiSubscaleInterpretations = new Hashtable();            
            
            int icp1 = 0;
            int icp2 = 0;
            int icp3 = 0;
            int icp4 = 0;
            int icp5 = 0;
            int icp6 = 0;

            int sixCC1[] = { 12, 13, 21, 23, 29, 33, 45, 47, 48, 49, 59, 60, 67, 
                             68, 69, 75, 77, 80, 82, 85, 88, 92, 101, 106, 108, 113, 
                             114, 120, 125, 126, 129, 134, 142, 159, 167, 170, 180 };
            ArrayList sixCoreComp1 = new ArrayList();
            for(int cc1 = 0; cc1 < sixCC1.length; cc1 ++) {
                int cc1num = sixCC1[cc1];
                Integer cc1N = cc1num;
                sixCoreComp1.add(cc1N);
            }
            
            int sixCC2[] = { 4, 8, 9, 12, 17, 26, 27, 30, 34, 36, 38, 41, 56, 58, 60, 
                             61, 66, 69, 72, 78, 84, 99, 121, 154, 177, 179 };
            ArrayList sixCoreComp2 = new ArrayList();
            for(int cc2 = 0; cc2 < sixCC2.length; cc2 ++) {
                int cc2num = sixCC2[cc2];
                Integer cc2N = cc2num;
                sixCoreComp2.add(cc2N);
            }
            
            int sixCC3[] = { 21, 31, 33, 47, 48, 54, 59, 67, 69, 75, 77, 80, 84, 89, 
                             96, 106, 113, 120, 125, 134, 137, 142, 158, 167, 170 };
            ArrayList sixCoreComp3 = new ArrayList();
            for(int cc3 = 0; cc3 < sixCC3.length; cc3 ++) {
                int cc3num = sixCC3[cc3];
                Integer cc3N = cc3num;
                sixCoreComp3.add(cc3N);
            }
            
            int sixCC4[] = { 13, 14, 16, 21, 24, 29, 31, 33, 46, 47, 48, 54, 59, 67, 69, 75, 
                             77, 80, 84, 89, 96, 100, 106, 108, 113, 134, 137, 142, 167 };
            ArrayList sixCoreComp4 = new ArrayList();
            for(int cc4 = 0; cc4 < sixCC4.length; cc4 ++) {
                int cc4num = sixCC4[cc4];
                Integer cc4N = cc4num;
                sixCoreComp4.add(cc4N);
            }
            
            int sixCC5[] = { 13, 19, 31, 33, 34, 44, 45, 47, 48, 52, 55, 59, 62, 67, 68, 69, 
                             77, 80, 92, 96, 101, 113, 114, 120, 125, 137, 159, 167, 170 };
            ArrayList sixCoreComp5 = new ArrayList();
            for(int cc5 = 0; cc5 < sixCC5.length; cc5 ++) {
                int cc5num = sixCC5[cc5];
                Integer cc5N = cc5num;
                sixCoreComp5.add(cc5N);
            }
            
            int sixCC6[] = { 5, 7, 9, 26, 27, 36, 40, 43, 51, 58, 65, 72, 97, 98, 99, 100, 107, 
                             109, 110, 116, 124, 138, 144, 160, 161, 164, 172, 176, 177, 178, 179 };
            ArrayList sixCoreComp6 = new ArrayList();
            for(int cc6 = 0; cc6 < sixCC6.length; cc6 ++) {
                int cc6num = sixCC6[cc6];
                Integer cc6N = cc6num;
                sixCoreComp6.add(cc6N);
            }
            
            int threeCs1[] = { 9, 27, 72, 81, 84, 98, 110, 155, 179 };
            int threeCs2[] = { 8, 9, 27, 38, 56, 61, 66, 72, 78, 81, 88, 98, 172, 177, 179 };
            int threeCs3[] = { 110, 121, 128, 132, 149, 155, 165 };

            int[] sixCCSums;
            sixCCSums = new int[6];
            
            int[] threeCsSums;
            threeCsSums = new int[3];
            
            for (int i = 1; i < pbiAnswersSize; i++) {
                Integer key = i;
                CF_Question currentQuestion = (CF_Question) pbiAnswers.get(key);
                int currentSubscaleId = currentQuestion.getSubscaleId();
                Integer questionNum = currentQuestion.getQuestionId();
                Integer origAnswer = currentQuestion.getAnswer();
                int pbiAdjustedAnswer = origAnswer;
                
                // add answer to associated subscale 
                if(currentSubscaleId != 0) {
                    if(pbiReverseValues.contains(questionNum)) {
                        pbiAdjustedAnswer = reverseAnswer(pbiAdjustedAnswer);
                    }
                    double subscaleSum = pbiSubscaleSums[currentSubscaleId - 1];
                    subscaleSum += pbiAdjustedAnswer;
                    pbiSubscaleSums[currentSubscaleId - 1] = subscaleSum;
                }
                
                // add up 6 CC's result
                int sixCompAdjustedAnswer = origAnswer;
                
                if(sixCoreComp1.contains(questionNum)) {
                    int tempint1 = sixCompAdjustedAnswer;
                    if(sixCoreCompReverseValues.contains(questionNum)) {
                        tempint1 = reverseAnswer(sixCompAdjustedAnswer);
                    }
                    sixCCSums[0] += tempint1;
                }
                if(sixCoreComp2.contains(questionNum)) {
                    int tempint2 = sixCompAdjustedAnswer;
                    if(questionNum != 26) {
                        tempint2 = reverseAnswer(sixCompAdjustedAnswer);
                    }
                    sixCCSums[1] += tempint2;
                }
                if(sixCoreComp3.contains(questionNum)) {
                    int tempint3 = sixCompAdjustedAnswer;
                    if(questionNum == 158) {
                        tempint3 = reverseAnswer(sixCompAdjustedAnswer);
                    }
                    sixCCSums[2] += tempint3;
                }
                if(sixCoreComp4.contains(questionNum)) {
                    int tempint4 = sixCompAdjustedAnswer;
                    if(questionNum == 24) {
                        tempint4 = reverseAnswer(sixCompAdjustedAnswer);
                    }
                    sixCCSums[3] += tempint4;
                }
                if(sixCoreComp5.contains(questionNum)) {
                    int tempint5 = sixCompAdjustedAnswer;
                    if(questionNum == 114) {
                        tempint5 = reverseAnswer(sixCompAdjustedAnswer);
                    }
                    sixCCSums[4] += tempint5;
                }
                if(sixCoreComp6.contains(questionNum)) {
                    int tempint6 = sixCompAdjustedAnswer;
                    if(sixCoreCompReverseValues.contains(questionNum)) {
                        tempint6 = reverseAnswer(sixCompAdjustedAnswer);
                    }
                    sixCCSums[5] += tempint6;
                }

                // add up 3 CC's result
                int threeCsAdjustedAnswer = origAnswer;
                if(threeCsCompReverseValues.contains(questionNum)) {
                    threeCsAdjustedAnswer = reverseAnswer(threeCsAdjustedAnswer);
                }
                for(int c1 = 0; c1 < threeCs1.length; c1++) {
                    int c1temp = threeCs1[c1];
                    if(c1temp == questionNum){
                        threeCsSums[0] += threeCsAdjustedAnswer;
                    }
                }
                for(int c2 = 0; c2 < threeCs2.length; c2++) {
                    int c2temp = threeCs2[c2];
                    if(c2temp == questionNum){
                        threeCsSums[1] += threeCsAdjustedAnswer;
                    }
                }
                for(int c3 = 0; c3 < threeCs3.length; c3++) {
                    int c3temp = threeCs3[c3];
                    if(c3temp == questionNum){
                        threeCsSums[2] += threeCsAdjustedAnswer;
                    }
                }
            }
            
            // go through each subscale to find the cut off point to find the related interpretation
            for(int ss = 0; ss < pbiTotalSubscales; ss ++) {
                Integer scalekey = ss + 1;
                CF_Subscale currentSubscaleObj = (CF_Subscale) pbiSubscaleStats.get(scalekey);
                pbiSubscaleNames[ss] = currentSubscaleObj.getSubscaleName();
                double[] scaleCutoffs = currentSubscaleObj.getScaleCutoffs();
                double qsPerScale = currentSubscaleObj.getQuestionsPerScale();
                double scaleMeanSum = pbiSubscaleSums[ss]/qsPerScale;
                double finalCutoffNum = findCutoffNum(scaleCutoffs, scaleMeanSum);
                pbiScaleCutoffResults[ss] = finalCutoffNum - 5;                
                
                String theCategory = "cat" + Math.round(finalCutoffNum);
                
                try {
                    String subscaleResultsInterpretation = CF_InterpretationXMLParser.GetPbiSubscaleText(ss + 1, theCategory);

                    ArrayList allICPs = new ArrayList();
                    int pndIndex = subscaleResultsInterpretation.indexOf("#");
                    pbiInterpretationsOnly = subscaleResultsInterpretation.substring(0, pndIndex);
                    Integer interpKey = ss + 1;
                    pbiSubscaleInterpretations.put(interpKey, pbiInterpretationsOnly);
                    String icps = subscaleResultsInterpretation.substring(pndIndex + 1);
                    int firstComma = icps.indexOf(",");
                    if(firstComma == -1) {
                        allICPs.add(icps);
                    }else {
                        int secondComma = icps.indexOf(",", firstComma + 1);
                        if(secondComma == -1) {
                            String firstNum = icps.substring(0, firstComma);
                            String secondNum = icps.substring(firstComma + 1);
                            allICPs.add(firstNum);
                            allICPs.add(secondNum);
                        }else {
                            String firstNum = icps.substring(0, firstComma);
                            String secondNum = icps.substring(firstComma + 1, secondComma);
                            allICPs.add(firstNum);
                            allICPs.add(secondNum);
                            int thirdComma = icps.indexOf(",", secondComma +1);
                            if(thirdComma == -1) {
                                String thirdNum = icps.substring(secondComma + 1);
                                allICPs.add(thirdNum);
                            }else {
                                String thirdNum = icps.substring(secondComma + 1, thirdComma);
                                allICPs.add(thirdNum);
                                int fourthComma = icps.indexOf(",", thirdComma +1);
                                if(fourthComma == -1) {
                                    String fourthNum = icps.substring(thirdComma + 1);
                                    allICPs.add(fourthNum);
                                }else {
                                    String fourthNum = icps.substring(thirdComma + 1, fourthComma);
                                    allICPs.add(fourthNum);
                                    int fifthComma = icps.indexOf(",", fourthComma +1);
                                    if(fifthComma == -1) {
                                        String fifthNum = icps.substring(fourthComma + 1);
                                        allICPs.add(fifthNum);
                                    }else {
                                        String fifthNum = icps.substring(fourthComma + 1, fifthComma);
                                        allICPs.add(fifthNum);
                                        String sixthNum = icps.substring(fifthComma + 1);
                                        allICPs.add(sixthNum);
                                    }
                                }
                            }
                        }
                    }
                    for(int al = 0; al < allICPs.size(); al++) {
                        String num = (String) allICPs.get(al);
                        if(null != num) switch (num) {
                            case "1":
                                icp1++;
                                break;
                            case "2":
                                icp2++;
                                break;
                            case "3":
                                icp3++;
                                break;
                            case "4":
                                icp4++;
                                break;
                            case "5":
                                icp5++;
                                break;
                            case "6":
                                icp6++;
                                break;
                            default:
                                break;
                        }
                    }
                }catch (Exception e) {
                    System.out.println(new java.util.Date());
                    System.out.println("CF_ShowResultsServlet, loadAnswers, pbiInterpretationsXML File not Found");
                    System.out.println(e + "\n");
                }
            }
            
            int[] icpTotals;
            icpTotals = new int[6];
            
            icpTotals[0] = icp1;
            icpTotals[1] = icp2;
            icpTotals[2] = icp3;
            icpTotals[3] = icp4;
            icpTotals[4] = icp5;
            icpTotals[5] = icp6;
            
//            load up the names, means and SD's for the 3 C's and Six Core Comps
            loadThreeCs_sixCompStats();
            
//            Six Core Competencies calculations
            int sixCompTotalSubscales = sixCompStats.size();            
            String[] sixCompNames;
            sixCompNames = new String[sixCompTotalSubscales];
            
            double [] sixCompCutoffResults;
            sixCompCutoffResults = new double[sixCompTotalSubscales];
            
            for(int sx = 0; sx < sixCompTotalSubscales; sx ++) {
                Integer m = sx + 1;
                CF_Subscale sixCompSubscaleData = (CF_Subscale) sixCompStats.get(m);
                sixCompNames[sx] = sixCompSubscaleData.getSubscaleName();
                double[] sixCsCutoffs = sixCompSubscaleData.getScaleCutoffs();
                double sixCsQsPerScale = sixCompSubscaleData.getQuestionsPerScale();
                double sixCsScaleMeanSum = sixCCSums[sx]/sixCsQsPerScale;
                double sixCsFinalCutoffNum = findCutoffNum(sixCsCutoffs, sixCsScaleMeanSum);
                sixCompCutoffResults[sx] = sixCsFinalCutoffNum - 5;
            }
            
//            an array of just the two highest (absolute) sixComp scores
            int [] sortedSixComps = new int[6];
            sortedSixComps = new int[sixCompTotalSubscales];
            System.arraycopy(sixCCSums, 0, sortedSixComps, 0, sixCCSums.length);
            Arrays.sort(sortedSixComps);
            int firstHighest = sortedSixComps[5];
            int secondHighest = sortedSixComps[4];
            int firstHighestIndex = 0;
            int secondHighestIndex = 0;
            for(int s = 0; s < sixCCSums.length; s ++) {
                if(sixCCSums[s] == firstHighest) {
                    firstHighestIndex = s;
                }
                if(sixCCSums[s] == secondHighest) {
                    secondHighestIndex = s;
                }
            }
            String [] bkClientSixCompResults;
            bkClientSixCompResults = new String[2];
            bkClientSixCompResults[0] = sixCompNames[firstHighestIndex];
            bkClientSixCompResults[1] = sixCompNames[secondHighestIndex];
            session.setAttribute("bkClientSixCompResults", bkClientSixCompResults);
            
//            Three C's calculations
            int threeCsTotalSubscales = threeCsStats.size();            
            String[] threeCsNames;
            threeCsNames = new String[threeCsTotalSubscales];
            
            double [] threeCsCutoffResults;
            threeCsCutoffResults = new double[threeCsTotalSubscales];
            
            for(int cs = 0; cs < threeCsTotalSubscales; cs ++) {
                Integer y = cs + 1;
                CF_Subscale threeCsSubscaleData = (CF_Subscale) threeCsStats.get(y);
                threeCsNames[cs] = threeCsSubscaleData.getSubscaleName();
                double[] threeCsCutoffs = threeCsSubscaleData.getScaleCutoffs();
                double threeCsQsPerScale = threeCsSubscaleData.getQuestionsPerScale();
                double threeCsScaleMeanSum = threeCsSums[cs]/threeCsQsPerScale;
                double threeCsFinalCutoffNum = findCutoffNum(threeCsCutoffs, threeCsScaleMeanSum);
                threeCsCutoffResults[cs] = threeCsFinalCutoffNum - 5;
            }

            session.setAttribute("icpTotals", icpTotals);
            session.setAttribute("pbiSubscaleNames", pbiSubscaleNames);
            session.setAttribute("pbiSubscaleInterpretations", pbiSubscaleInterpretations);

            JFreeChart pbiChart = new CF_ChartGenerator().buildChart(pbiSubscaleNames, pbiScaleCutoffResults, "pbi");
            JFreeChart sixCompChart = new CF_ChartGenerator().buildChart(sixCompNames, sixCompCutoffResults, "sixComp");
            JFreeChart threeCsChart = new CF_ChartGenerator().buildChart(threeCsNames, threeCsCutoffResults, "threeCs");
            ServletUtilities.setTempFilePrefix(userid);
            String pbiFilename = ServletUtilities.saveChartAsPNG(pbiChart, 1000, 330, session);
            String sixCompFilename = ServletUtilities.saveChartAsPNG(sixCompChart, 1000, 200, session);
            String threeCsFilename = ServletUtilities.saveChartAsPNG(threeCsChart, 1000, 100, session);
            session.setAttribute("pbiFilename", pbiFilename);
            session.setAttribute("sixCompFilename", sixCompFilename);
            session.setAttribute("threeCsFilename", threeCsFilename);
            
        }else {
            session.setAttribute("pbiFilename", null);
            session.setAttribute("sixCompFilename", null);
            session.setAttribute("threeCsFilename", null);
        }
        
        if(ibtTestDone > 0) {
            loadAnswers(ibtAnswers, testUserNum, "ibt");
            
            int ibtAnswersSize = ibtAnswers.size()+1;
            
            int ibtTotalSubscales = ibtSubscaleStats.size();
            
            int[] ibtSubscaleTotals;
            ibtSubscaleTotals = new int[ibtTotalSubscales];
            
            int[] hiIbtScores;
            hiIbtScores = new int[ibtTotalSubscales];
            
            String[] ibtSubscaleNames;
            ibtSubscaleNames = new String[ibtTotalSubscales];
            for(int sn = 0; sn < ibtTotalSubscales; sn ++) {
                Integer snkey = sn + 1;
                CF_Subscale ibtSubscaleData = (CF_Subscale) ibtSubscaleStats.get(snkey);
                ibtSubscaleNames[sn] = ibtSubscaleData.getSubscaleName();
            }
            
            Hashtable ibtSubscaleInterpretations = new Hashtable();
            
            for (int i = 1; i < ibtTotalSubscales + 1; i++) {                
                int talleySubscaleSum = 0;
                int hiIbt = 0;
                for (int k = 1; k < ibtAnswersSize; k++)
                {
                    Integer key = k;
                    CF_Question currentQuestion = (CF_Question) ibtAnswers.get(key);
                    int currentSubscaleNum = currentQuestion.getSubscaleId();
                    Integer questionNum = currentQuestion.getQuestionId();
                    if (currentSubscaleNum == i) {
                        Integer origAns = currentQuestion.getAnswer();                        
                        int currentAnswer = origAns;
                        if(ibtReverseValues.contains(questionNum)){
                            currentAnswer= reverseAnswer(currentAnswer);
                        }
                        talleySubscaleSum += currentAnswer;
                        if(currentAnswer >= 4 ){
                         hiIbt++;   
                        }
                    }
                }
                int index = (i - 1);
                ibtSubscaleTotals[index] = talleySubscaleSum;
                hiIbtScores[index] = hiIbt;
                try {
                    String ibtSubscaleResultsInterpretation = CF_InterpretationXMLParser.GetSubscaleText(i, hiIbt, "ibt");
                    Integer key = i;
                    ibtSubscaleInterpretations.put(key, ibtSubscaleResultsInterpretation);
                }catch (Exception e) {
                    System.out.println(new java.util.Date());
                    System.out.println("CF_ShowResultsServlet, loadAnswers, ibtInterpretationsXML File not Found");
                    System.out.println(e + "\n");
                }                
            }
            session.setAttribute("ibtSubscaleNames", ibtSubscaleNames);
            session.setAttribute("ibtSubscaleInterpretations", ibtSubscaleInterpretations);
            session.setAttribute("ibtSubscaleTotals", ibtSubscaleTotals);
            session.setAttribute("hiIbtScores", hiIbtScores);
        }else {
            session.setAttribute("ibtSubscaleNames", null);
            session.setAttribute("ibtSubscaleTotals", null);
        }
        
        RequestDispatcher rd = request.getRequestDispatcher("CF_ControllerServlet?action=displayChart");
        rd.forward(request, response);
          return;        
    }

    private int reverseAnswer(int currentAnswer) {
            switch (currentAnswer) {
                case 1:
                    currentAnswer = 5;
                    break;
                case 2:
                    currentAnswer = 4;
                    break;
                case 4:
                    currentAnswer = 2;
                    break;
                case 5:
                    currentAnswer = 1;
                    break;
                default:
                    break;
            }
        return currentAnswer;
    }
    
    private double findCutoffNum(double[] scaleCutoffs, double scaleMeanSum) {
        double cutoffNum;
        int intNum = 0;
        for(int sc = 0; sc < scaleCutoffs.length; sc ++) {
            if(scaleMeanSum <= scaleCutoffs[sc]) {
                intNum = (sc + 1);
                    break;
            }
         }
         if(intNum == 0) {
             intNum = 10;
         }
        cutoffNum = intNum;
        return cutoffNum;
    }
    
    private void loadAnswers(Hashtable answers, int personNum, String whichTest) {
        Connection conn = null;
        java.sql.Statement stmt1 = null;
        java.sql.Statement stmt2 = null;
        java.sql.Statement stmt3 = null;
        ResultSet rsAmtQs = null;
        ResultSet rsSubscale = null;
        ResultSet rsAnswer = null;
        int totalNumQuestions = 0;
        try {
            try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                } catch (ClassNotFoundException ex) {
                    System.out.println("ClassNotFoundException: " + ex.getMessage());
                } catch (InstantiationException | IllegalAccessException ex) {
                    System.out.println("InstantiationException: " + ex.getMessage());
                }
            
            conn = DriverManager.getConnection("jdbc:mysql://dc2-mysql-05.kattare.com/pbitest?user=wcgypbp&password=m7%4wiZJdq3");
        } catch (SQLException ex) {
            System.out.println(new java.util.Date());
            System.out.println("CF_ShowResultsServlet, loadAnswers, contacting database. SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode() + "\n");
        } 
        if(conn != null)
        {
            try {
                String getTotalAmtQs = "SELECT COUNT(*) FROM t_" + whichTest + "_questions;";
                String selectSubscale = "";
                if("pbi".equals(whichTest)) {
                    selectSubscale = "SELECT * FROM t_pbi_scales_cutoffs;";
                }else {
                    selectSubscale = "SELECT * FROM t_ibt_subscales;";
                }
                String selectAnswer = "SELECT answer, t_" + whichTest + "_tests.subscaleid, questionid FROM t_" + whichTest + "_subscales, t_" + whichTest + "_tests WHERE userid =" + personNum + " AND t_" + whichTest + "_subscales.subscaleid = t_" + whichTest + "_tests.subscaleid ORDER BY t_" + whichTest + "_tests.questionid;";
                stmt1 = conn.createStatement();
                stmt2 = conn.createStatement();
                stmt3 = conn.createStatement();
                rsAmtQs = stmt1.executeQuery(getTotalAmtQs);
                rsSubscale = stmt2.executeQuery(selectSubscale);
                rsAnswer = stmt3.executeQuery(selectAnswer);
            } catch (SQLException ex) {
                System.out.println(new java.util.Date());
                System.out.println("CF_ShowResultsServlet, loadAnswers, executing queries: amt questions, pulling subscales and answers. SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode() + "\n"); 
            } finally {
                if(rsAmtQs != null) {
                    try {
                        rsAmtQs.first();
                        totalNumQuestions = rsAmtQs.getInt("COUNT(*)");
                    } catch (SQLException ex) {
                        System.out.println(new java.util.Date());
                        System.out.println("CF_ShowResultsServlet, loadAnswers, parsing result set for amount of questions. SQLException: " + ex.getMessage()); 
                        System.out.println("SQLState: " + ex.getSQLState()); 
                        System.out.println("VendorError: " + ex.getErrorCode() + "\n");
                    }
                    try {
                        rsAmtQs.close(); 
                    } catch (SQLException sqlEx) { // ignore }
                        rsAmtQs = null; 
                    }
                    try {
                        stmt1.close(); 
                    } catch (SQLException sqlEx) { // ignore } 
                        stmt1 = null; 
                    }
                }
                if (rsSubscale != null) {
                    try {
                        rsSubscale.first();
                        int countSS = 0;
                        while (!rsSubscale.isAfterLast()) {
                            Integer key = ++countSS;
                            if("ibt".equals(whichTest)) {
                                CF_Subscale subscale = new CF_Subscale(rsSubscale.getString("subscaleName"), rsSubscale.getDouble("subscaleMean"), rsSubscale.getDouble("subscaleStandardDev"), rsSubscale.getInt("subscaleid"));
                                ibtSubscaleStats.put(key, subscale);
                            }else {
                                double[] cutoffs = {rsSubscale.getDouble("cutoff_01"), rsSubscale.getDouble("cutoff_02"), rsSubscale.getDouble("cutoff_03"), rsSubscale.getDouble("cutoff_04"), rsSubscale.getDouble("cutoff_05"), rsSubscale.getDouble("cutoff_06"), rsSubscale.getDouble("cutoff_07"), rsSubscale.getDouble("cutoff_08"), rsSubscale.getDouble("cutoff_09")};
                                CF_Subscale scale = new CF_Subscale(rsSubscale.getString("subscaleName"), rsSubscale.getInt("subscaleid"), cutoffs, rsSubscale.getInt("questionsPerScale"));                              
                                pbiSubscaleStats.put(key, scale);
                            }
                            rsSubscale.next();
                        }
                    } catch (SQLException ex) {
                        System.out.println(new java.util.Date());
                        System.out.println("CF_ShowResultsServlet, loadAnswers, parsing result set for subscales. SQLException: " + ex.getMessage()); 
                        System.out.println("SQLState: " + ex.getSQLState()); 
                        System.out.println("VendorError: " + ex.getErrorCode() + "\n");
                    }
                    try {
                        rsSubscale.close(); 
                    } catch (SQLException sqlEx) { // ignore } 
                        rsSubscale = null; 
                    }
                    if (stmt2 != null) { 
                        try { 
                            stmt2.close(); 
                        } catch (SQLException sqlEx) { // ignore } 
                            stmt2 = null;
                        } 
                    }    
                }
                
                if (rsAnswer != null) {
                    try {
                        rsAnswer.first();
                        int countA = 0;
                        while (!rsAnswer.isAfterLast()) {
                            CF_Subscale subscale = new CF_Subscale(rsAnswer.getInt("t_" + whichTest + "_tests.subscaleid"));
                            int questionid = rsAnswer.getInt("questionid");
                            if(questionid <= totalNumQuestions) {
                                CF_Question question = new CF_Question("",questionid,subscale);
                                question.setAnswer(rsAnswer.getInt("answer"));
                                Integer key = ++countA;
                                answers.put(key, question);
                            }
                            rsAnswer.next();
                        }
                    } catch (SQLException ex) {
                        System.out.println(new java.util.Date());
                        System.out.println("CF_ShowResultsServlet, loadAnswers, parsing result set for answers. SQLException: " + ex.getMessage()); 
                        System.out.println("SQLState: " + ex.getSQLState()); 
                        System.out.println("VendorError: " + ex.getErrorCode() + "\n");
                    }
                    try {
                        rsAnswer.close();
                    } catch (SQLException sqlEx) { // ignore }
                        rsAnswer = null;
                    }
                    if (stmt3 != null) {
                        try {
                            stmt3.close();
                        } catch (SQLException sqlEx) { // ignore }
                            stmt3 = null;
                        } 
                    }    
                }
            }
         }
    }    
    private void loadThreeCs_sixCompStats()
    {
        Connection conn = null;        
        java.sql.Statement stmt1 = null;
        java.sql.Statement stmt2 = null;
        ResultSet rsThreeCs = null;
        ResultSet rsSixComps = null;
        try {
            try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                } catch (ClassNotFoundException ex) {
                    System.out.println("ClassNotFoundException: " + ex.getMessage());
                } catch (InstantiationException | IllegalAccessException ex) {
                    System.out.println("InstantiationException: " + ex.getMessage());
                }
            
            conn = DriverManager.getConnection("jdbc:mysql://dc2-mysql-05.kattare.com/pbitest?user=wcgypbp&password=m7%4wiZJdq3");
        } catch (SQLException ex) {
            System.out.println(new java.util.Date());
            System.out.println("CF_ShowResultsServlet, loadThreeCs_sixCompStats, contacting database. SQLException: " + ex.getMessage()); 
            System.out.println("SQLState: " + ex.getSQLState()); 
            System.out.println("VendorError: " + ex.getErrorCode() + "\n");
        }
        if(conn != null)
        {
            try {
                String selectThreeCs = "SELECT * FROM t_threecs_cutoffs";
                String selectSixComp = "SELECT * FROM t_sixcomp_cutoffs";
                stmt1 = conn.createStatement();
                stmt2 = conn.createStatement();
                rsThreeCs = stmt1.executeQuery(selectThreeCs);
                rsSixComps = stmt2.executeQuery(selectSixComp);
            } catch (SQLException ex) {
                System.out.println(new java.util.Date());
                System.out.println("CF_ShowResultsServlet, loadThreeCs_sixCompStats, executing query for three C's and 6 comps. SQLException: " + ex.getMessage()); 
                System.out.println("SQLState: " + ex.getSQLState()); 
                System.out.println("VendorError: " + ex.getErrorCode() + "\n");
            } finally {
                if (rsThreeCs != null) {
                    try {
                        rsThreeCs.first();
                        int countThree = 0;
                        while (!rsThreeCs.isAfterLast()) {
                            double[] cutoffs3cs = {rsThreeCs.getDouble("cutoff3c_01"), rsThreeCs.getDouble("cutoff3c_02"), rsThreeCs.getDouble("cutoff3c_03"), rsThreeCs.getDouble("cutoff3c_04"), rsThreeCs.getDouble("cutoff3c_05"), rsThreeCs.getDouble("cutoff3c_06"), rsThreeCs.getDouble("cutoff3c_07"), rsThreeCs.getDouble("cutoff3c_08"), rsThreeCs.getDouble("cutoff3c_09")};                            
                            CF_Subscale cScales = new CF_Subscale(rsThreeCs.getString("subscaleName"), rsThreeCs.getInt("subscaleid"), cutoffs3cs, rsThreeCs.getInt("questionsPer3c"));
                            Integer ckey = ++countThree;
                            threeCsStats.put(ckey, cScales);
                            rsThreeCs.next();
                        }
                    } catch (SQLException ex) {
                        System.out.println(new java.util.Date());
                        System.out.println("CF_ShowResultsServlet, loadThreeCs_sixCompStats, parsing result set for three C's subscales. SQLException: " + ex.getMessage()); 
                        System.out.println("SQLState: " + ex.getSQLState()); 
                        System.out.println("VendorError: " + ex.getErrorCode() + "\n");
                    }
                    try {
                        rsThreeCs.close(); 
                    } catch (SQLException sqlEx) { // ignore } 
                        rsThreeCs = null; 
                    }
                    
                    if (stmt1 != null) { 
                        try { 
                            stmt1.close(); 
                        } catch (SQLException sqlEx) { // ignore } 
                            stmt1 = null; 
                        } 
                    }    
                }
                
                if (rsSixComps != null) {
                    try {
                        rsSixComps.first();
                        int countSix = 0;
                        while (!rsSixComps.isAfterLast()) {
                            double[] cutoffs6cs = {rsSixComps.getDouble("cutoff6c_01"), rsSixComps.getDouble("cutoff6c_02"), rsSixComps.getDouble("cutoff6c_03"), rsSixComps.getDouble("cutoff6c_04"), rsSixComps.getDouble("cutoff6c_05"), rsSixComps.getDouble("cutoff6c_06"), rsSixComps.getDouble("cutoff6c_07"), rsSixComps.getDouble("cutoff6c_08"), rsSixComps.getDouble("cutoff6c_09")};                            
                            CF_Subscale ccScales = new CF_Subscale(rsSixComps.getString("subscaleName"), rsSixComps.getInt("subscaleid"), cutoffs6cs, rsSixComps.getInt("questionsPer6cic"));
                            Integer cckey = ++countSix;
                            sixCompStats.put(cckey, ccScales);
                            rsSixComps.next();
                        }
                    } catch (SQLException ex) {
                        System.out.println(new java.util.Date());
                        System.out.println("CF_ShowResultsServlet, loadThreeCs_sixCompStats, parsing result set for 6 comps subscales. SQLException: " + ex.getMessage()); 
                        System.out.println("SQLState: " + ex.getSQLState()); 
                        System.out.println("VendorError: " + ex.getErrorCode() + "\n");
                    }
                    try {
                        rsSixComps.close(); 
                    } catch (SQLException sqlEx) { // ignore } 
                        rsSixComps = null; 
                    }
                    
                    if (stmt2 != null) { 
                        try { 
                            stmt2.close(); 
                        } catch (SQLException sqlEx) { // ignore } 
                            stmt2 = null; 
                        } 
                    }    
                }
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
        processRequest(request, response);
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
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     * @return 
     */
        @Override
    public String getServletInfo() {
        return "Short description";
    }
}
