/*
 * GroovyXML1.java
 *
 * Created on August 24, 2004, 11:48 PM
 *
 * @author  Glenn Hummel
 */

package com.personalblueprint.helperClasses;

import java.util.Hashtable;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CF_InterpretationXMLParser
{ 
    // using SD and Means double result and matching that with ranges in xml file
    public static String GetSubscaleText( int currentSubscale, double currentSubscaleResult, String whichTest )
        throws Exception
    {
        // Read the XML 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        String choosenTest = whichTest;
        String testFileName = choosenTest + "_interpretations.xml";
        Document doc = builder.parse( new File( "personalblueprint_com/xml/" + testFileName));
        
        StringBuffer subscaleText = new StringBuffer();
        
        // Get the initial subscale elements
        NodeList topLevelElements = doc.getElementsByTagName("subscale");
        int size = topLevelElements.getLength();
        for(int i = 0; i < topLevelElements.getLength(); i++) {
            Element topLevelElement = (Element) topLevelElements.item(i);
            String subscaleNumText = topLevelElement.getAttribute("scaleNum");
            int subscaleNum = java.lang.Integer.parseInt(subscaleNumText);
            if(subscaleNum == currentSubscale){
                NodeList rangeElements = topLevelElement.getElementsByTagName("range");
                int lastRangeElement = (rangeElements.getLength() - 1);
                for(int k = 0; k < rangeElements.getLength(); k++) {
                    Element rangeElement = (Element) rangeElements.item(k);
                    String rangeMinText = rangeElement.getAttribute("min");
                    double rangeMin = java.lang.Double.parseDouble(rangeMinText);
                    Element foundRangeElement = (Element) rangeElements.item(0);
                    if(currentSubscaleResult < rangeMin) {
                        foundRangeElement = (Element) rangeElements.item(k - 1);
                        NodeList neededTextElements = foundRangeElement.getElementsByTagName("text");
                        Element neededTextElement = (Element) neededTextElements.item(0);
                        Node neededTextNode = neededTextElement.getFirstChild();
                        String completeText = neededTextNode.getNodeValue();
                        String neededText = "";
                        Pattern nlBr = Pattern.compile("(\r\n|\n\r|\n|\r)");
                        Matcher m = nlBr.matcher(completeText);
                        if(m.find()) {
                            neededText = m.replaceAll("<br />");
                        }else {
                            neededText = completeText;
                        }
                        subscaleText.append(neededText);
                        NodeList neededICPElements = foundRangeElement.getElementsByTagName("icp");
                        int icpElementSize = neededICPElements.getLength();
                        if(icpElementSize > 0) {
                            Element neededICPNums = (Element) neededICPElements.item(0);
                            Node neededICPNode = neededICPNums.getFirstChild();
                            String icp = neededICPNode.getNodeValue();
                            subscaleText.append("#");
                            subscaleText.append(icp);
                        }
                        break;
                    }else if("pbi".equals(choosenTest) && currentSubscaleResult > 2){
                        foundRangeElement = (Element) rangeElements.item(lastRangeElement);
                        NodeList neededTextElements = foundRangeElement.getElementsByTagName("text");
                        Element neededTextElement = (Element) neededTextElements.item(0);
                        Node neededTextNode = neededTextElement.getFirstChild();
                        String completeText = neededTextNode.getNodeValue();
                        String neededText = "";
                        Pattern nlBr = Pattern.compile("(\r\n|\n\r|\n|\r)");
                        Matcher m = nlBr.matcher(completeText);
                        if(m.find()) {
                            neededText = m.replaceAll("<br />");
                        }else {
                            neededText = completeText;
                        }
                        subscaleText.append(neededText);
                        NodeList neededICPElements = foundRangeElement.getElementsByTagName("icp");
                        int icpElementSize = neededICPElements.getLength();
                        if(icpElementSize > 0) {
                            Element neededICPNums = (Element) neededICPElements.item(0);
                            Node neededICPNode = neededICPNums.getFirstChild();
                            String icp = neededICPNode.getNodeValue();
                            subscaleText.append("#");
                            subscaleText.append(icp);
                        }
                        break;
                    }else if("ibt".equals(choosenTest) && currentSubscaleResult > 7) {
                        foundRangeElement = (Element) rangeElements.item(lastRangeElement);
                        NodeList neededTextElements = foundRangeElement.getElementsByTagName("text");
                        Element neededTextElement = (Element) neededTextElements.item(0);
                        Node neededTextNode = neededTextElement.getFirstChild();
                        String neededText = neededTextNode.getNodeValue();
                        subscaleText.append(neededText);
                        break;
                    }
                }
                break;
            }
        }
        String subscaleTextInfo = subscaleText.toString();
        return subscaleTextInfo;
    }
    
    // using percentage data to find category 
    public static String GetPbiSubscaleText( int currentSubscale, String whichCategory )
        throws Exception
    {
        // Read the XML 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        String testFileName = "pbi_interpretations.xml";
        Document doc = builder.parse( new File( "personalblueprint_com/xml/" + testFileName));
        
        StringBuffer subscaleText = new StringBuffer();
        
        // Get the initial subscale elements
        NodeList topLevelElements = doc.getElementsByTagName("subscale");
        int size = topLevelElements.getLength();
        for(int i = 0; i < topLevelElements.getLength(); i++) {
            Element topLevelElement = (Element) topLevelElements.item(i);
            String subscaleNumText = topLevelElement.getAttribute("scaleNum");
            int subscaleNum = java.lang.Integer.parseInt(subscaleNumText);
            if(subscaleNum == currentSubscale){
                NodeList scaleCategoryElements = topLevelElement.getElementsByTagName("category");
                int categoryListSize = scaleCategoryElements.getLength();
                for(int k = 0; k < categoryListSize; k++) {
                    Element categoryElement = (Element) scaleCategoryElements.item(k);
                    String categoryNameText = categoryElement.getAttribute("catNum");
                    if(categoryNameText.equals(whichCategory)) {
                        NodeList neededTextElements = categoryElement.getElementsByTagName("text");
                        Element neededTextElement = (Element) neededTextElements.item(0);
                        Node neededTextNode = neededTextElement.getFirstChild();
                        String completeText = neededTextNode.getNodeValue();
                        String neededText = "";
                        Pattern nlBr = Pattern.compile("(\r\n|\n\r|\n|\r)");
                        Matcher m = nlBr.matcher(completeText);
                        if(m.find()) {
                            neededText = m.replaceAll("<br />");
                        }else {
                            neededText = completeText;
                        }
                        subscaleText.append(neededText);
                        NodeList neededICPElements = categoryElement.getElementsByTagName("icp");
                        int icpElementSize = neededICPElements.getLength();
                        if(icpElementSize > 0) {
                            Element neededICPNums = (Element) neededICPElements.item(0);
                            Node neededICPNode = neededICPNums.getFirstChild();
                            String icp = neededICPNode.getNodeValue();
                            subscaleText.append("#");
                            subscaleText.append(icp);
                        }
                        break;
                    }
                }
                break;
            }
        }
        String subscaleTextInfo = subscaleText.toString();
        return subscaleTextInfo;
    }
}
