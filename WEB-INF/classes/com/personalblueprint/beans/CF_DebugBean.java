/*
 * LB_DebugBean.java
 *
 * Created on July 6, 2003, 6:59 PM
 */

package com.personalblueprint.beans;

import java.beans.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

/**
 * This class is a bean that can be used to extract debug
 * information from a JSP PageContext. The debug info is
 * sent to the browser, System.out, and the servlet log
 * file, depending on the value of the "debug" request
 * parameter sent with the request for the JSP page:
 * "resp", "stdout" and "log". The values can be combined to
 * get the information directed to multiple targets.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0.1
 */

public class CF_DebugBean extends Object implements java.io.Serializable {
    static private final String LINE_FEED = System.getProperty("line.separator");
    private PageContext pageContext;
    private long startTime;
    private String debugType;
    private ServletContext context;
    
    /**
     * Creates an instance and initializes the timer.
     */
    public CF_DebugBean() {
        startTime = System.currentTimeMillis();
    }
    
    /**
     * Sets the pageContext property.
     */
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }
    
    /**
     * Returns a String with the number of milliseconds that
     * has passed since the bean was created or the last time
     * this method was called.
     */
    public String getElapsedTime() {
        String elapsedTime = 
            new Long(System.currentTimeMillis() - startTime).toString() + 
                " ms";
        startTime = System.currentTimeMillis();
        return handleMsg("elapsedTime", elapsedTime);
    }
    
    /**
     * Returns a String with all basic request information.
     */
    public String getRequestInfo() {
        if (pageContext == null) {
            throw new IllegalStateException("The pageContext property is not set");
        }
        
        Hashtable info = new Hashtable();
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        info.put("authType", nullToString(req.getAuthType()));
        info.put("characterEncoding", nullToString(req.getCharacterEncoding()));
        info.put("contentLength", new Integer(req.getContentLength()).toString());
        info.put("contentType", nullToString(req.getContentType()));
        info.put("contextPath", nullToString(req.getContextPath()));
        info.put("pathInfo", nullToString(req.getPathInfo()));
        info.put("protocol", nullToString(req.getProtocol()));
        info.put("queryString", nullToString(req.getQueryString()));
        info.put("remoteAddr", nullToString(req.getRemoteAddr()));
        info.put("remoteHost", nullToString(req.getRemoteHost()));
        info.put("remoteUser", nullToString(req.getRemoteUser()));
        info.put("requestURI", nullToString(req.getRequestURI()));
        info.put("scheme", nullToString(req.getScheme()));
        info.put("serverName", nullToString(req.getServerName()));
        info.put("serverPort", new Integer(req.getServerPort()).toString());
        info.put("servletPath", nullToString(req.getServletPath()));
        return handleMsg("requestInfo", info);
    }
    
    /**
     * Returns a String with all header information.
     */
    public String getHeaders() {
        if (pageContext == null) {
            throw new IllegalStateException("The pageContext property is not set");
        }
        
        Hashtable info = new Hashtable();
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        Enumeration names = req.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Enumeration values = req.getHeaders(name);
            StringBuffer sb = new StringBuffer();
            boolean first = true;
            while (values.hasMoreElements()) {
                if (!first) {
                    sb.append(" | ");
                }
                first = false;
                sb.append(values.nextElement());
            }
            info.put(name, sb.toString());
        }
        return handleMsg("headers", info);
    }
    
    /**
     * Returns a String with all cookie information.
     */
    public String getCookies() {
        if (pageContext == null) {
            throw new IllegalStateException("The pageContext property is not set");
        }
        
        Hashtable info = new Hashtable();
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        Cookie[] cookies = req.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            info.put(cookies[i].getName(), cookies[i].getValue());
        }
        return handleMsg("cookies", info);
    }
    
    /**
     * Returns a String with all request parameter information.
     */
    public String getParameters() {
        if (pageContext == null) {
            throw new IllegalStateException("The pageContext property is not set");
        }
        
        Hashtable info = new Hashtable();
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        Enumeration names = req.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String[] values = req.getParameterValues(name);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < values.length; i++) {
                if (i != 0) {
                    sb.append(" | ");
                }
                sb.append(values[i]);
            }
            info.put(name, sb.toString());
        }
        return handleMsg("parameters", info);
    }
    
    /**
     * Returns a String with all request scope variables.
     */
    public String getRequestScope() {
        if (pageContext == null) {
            throw new IllegalStateException("The pageContext property is not set");
        }
        
        Hashtable info = new Hashtable();
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        Enumeration names = req.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Object value = req.getAttribute(name);
            info.put(name, toStringValue(value));
        }
        return handleMsg("requestScope", info);
    }
    
    /**
     * Returns a String with all page scope variables.
     */
    public String getPageScope() {
        if (pageContext == null) {
            throw new IllegalStateException("The pageContext property is not set");
        }
        
        Hashtable info = new Hashtable();
        Enumeration names = 
            pageContext.getAttributeNamesInScope(PageContext.PAGE_SCOPE);
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Object value = pageContext.getAttribute(name);
            info.put(name, toStringValue(value));
        }
        return handleMsg("pageScope", info);
    }
    
    /**
     * Returns a String with all session scope variables.
     */
    public String getSessionScope() {
        if (pageContext == null) {
            throw new IllegalStateException("The pageContext property is not set");
        }
        
        Hashtable info = new Hashtable();
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        HttpSession session = req.getSession();
        Enumeration names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Object value = session.getAttribute(name);
            info.put(name, toStringValue(value));
        }
        return handleMsg("sessionScope", info);
    }
    
    /**
     * Returns a String with all application scope variables.
     */
    public String getApplicationScope() {
        if (pageContext == null) {
            throw new IllegalStateException("The pageContext property is not set");
        }
        
        Hashtable info = new Hashtable();
        ServletContext context = pageContext.getServletContext();
        Enumeration names = context.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Object value = context.getAttribute(name);
            info.put(name, toStringValue(value));
        }
        return handleMsg("applicationScope", info);
    }
    
    /**
     * Returns the String "null" if the value is null,
     * otherwise the value itself.
     */
    private String nullToString(String value) {
        if (value == null) {
            return "null";
        }
        else {
            return value;
        }
    }
    
    /**
     * Returns true if the pageContext attribute has been set
     * and a valid "debug" request parameter is present in the request.
     */
    private boolean isDebugActive() {
        if (pageContext == null) {
            return false;
        }
        
        if (debugType == null) {
            String debugParam = pageContext.getRequest().getParameter("debug");
            if (debugParam != null) {
                debugType = debugParam;
            }
            else {
                debugType = "off";
            }
        }
        return "resp stdout log".indexOf(debugType) != -1;
    }
    
    /**
     * Returns a String suitable for browser debug display
     * and sends the debug message to System.out and/or
     * the servlet log file, depending on the value of
     * the "debug" request parameter. If debug is not active,
     * an empty String is returned.
     */
    private String handleMsg(String propName, String msg) {
        String returnVal = "";
        if (isDebugActive()) {
            log(propName, msg);
            if (debugType.indexOf("resp") != -1) {
                returnVal = propName + ": " + msg;
            }
        }
        return returnVal;
    }
    
    /**
     * If debug is activated, writes the specified property
     * value to the log file and returns it as an HTML table,
     * depending on the requested debug type.
     */
    private String handleMsg(String propName, Hashtable values) {
        String returnVal = "";
        if (isDebugActive()) {
            log(propName, values);
            if (debugType.indexOf("resp") != -1) {
                returnVal = toHTMLTable(propName, values);
            }
        }
        return returnVal;
    }
        
    /**
     * Writes the specified property value to the System.out or
     * the log file, depending on the requested debug type.
     */
    private void log(String propName, String msg) {
        HttpServletRequest request = 
            (HttpServletRequest) pageContext.getRequest();
        msg = "[DebugBean] " + request.getRequestURI() + " : " + 
            propName + " : " + msg;
        if (debugType.indexOf("stdout") != -1) {
            System.out.println(msg);
        }
        if (debugType.indexOf("log") != -1) {
            if (context == null) {
                context = pageContext.getServletContext();
            }
            context.log(msg);
        }
    }
        
    private void log(String propName, Hashtable values) {
        log(propName, toTabbedTable(values));
    }
    
    /**
     * Returns an HTML table with all the values of the
     * specified property.
     */
    private String toHTMLTable(String propName, Hashtable values) {
        StringBuffer tableSB = new StringBuffer("<table border=\"1\">");
        tableSB.append("<caption align=\"top\"><b>").
            append(propName).
            append("</b></caption>");
        Enumeration keys = values.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            tableSB.append("<tr><td>").
                append(key).
                append("</td><td>").
                append(values.get(key)).
                append("</td></tr>");
        }
        tableSB.append("</table>");
        return tableSB.toString();
    }
    
    /**
     * Returns an simple ASCII table with all the values of the
     * specified property, used for log output.
     */
    private String toTabbedTable(Hashtable values) {
        StringBuffer tableSB = new StringBuffer();
        Enumeration keys = values.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            tableSB.append(LINE_FEED).
                append(key).append("\t\t").
                append(values.get(key));
        }
        return tableSB.toString();
    }
    
    /**
     * Returns a String representation of the specified
     * Object, in a format suitable for debug output.
     */
    private String toStringValue(Object value) {
        StringBuffer sb = new StringBuffer();
        Class type = value.getClass();
        if (type.isArray()) {
            Class componentType = type.getComponentType();
            sb.append(componentType.getName());
            sb.append("[]: {");
            if (!componentType.isPrimitive()) {
                Object[] arr = (Object[]) value;
                for (int i = 0; i < arr.length; i++) {
                    if (i != 0) {
                        sb.append(", ");
                    }
                    sb.append(arr[i]);
                }
            }
            else if (componentType == Boolean.TYPE) {
                boolean[] arr = (boolean[]) value;
                for (int i = 0; i < arr.length; i++) {
                    if (i != 0) {
                        sb.append(", ");
                    }
                    sb.append(arr[i]);
                }
            }
            else if (componentType == Byte.TYPE) {
                byte[] arr = (byte[]) value;
                for (int i = 0; i < arr.length; i++) {
                    if (i != 0) {
                        sb.append(", ");
                    }
                    sb.append(arr[i]);
                }
            }
            else if (componentType == Character.TYPE) {
                char[] arr = (char[]) value;
                for (int i = 0; i < arr.length; i++) {
                    if (i != 0) {
                        sb.append(", ");
                    }
                    sb.append(arr[i]);
                }
            }
            else if (componentType == Double.TYPE) {
                double[] arr = (double[]) value;
                for (int i = 0; i < arr.length; i++) {
                    if (i != 0) {
                        sb.append(", ");
                    }
                    sb.append(arr[i]);
                }
            }
            else if (componentType == Float.TYPE) {
                float[] arr = (float[]) value;
                for (int i = 0; i < arr.length; i++) {
                    if (i != 0) {
                        sb.append(", ");
                    }
                    sb.append(arr[i]);
                }
            }
            else if (componentType == Integer.TYPE) {
                int[] arr = (int[]) value;
                for (int i = 0; i < arr.length; i++) {
                    if (i != 0) {
                        sb.append(", ");
                    }
                    sb.append(arr[i]);
                }
            }
            else if (componentType == Long.TYPE) {
                long[] arr = (long[]) value;
                for (int i = 0; i < arr.length; i++) {
                    if (i != 0) {
                        sb.append(", ");
                    }
                    sb.append(arr[i]);
                }
            }
            else if (componentType == Short.TYPE) {
                short[] arr = (short[]) value;
                for (int i = 0; i < arr.length; i++) {
                    if (i != 0) {
                        sb.append(", ");
                    }
                    sb.append(arr[i]);
                }
            }
            sb.append("}");
        }
        else {
            sb.append(value.getClass().getName()).
                append(": ").
                append(value.toString());
        }
        return sb.toString();
    }    
}
