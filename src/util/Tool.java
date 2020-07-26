/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tool | Templates
 * and open the template in the editor.
 */
package util;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dirga
 */
public class Tool {
    
    public static String EMPTY = "";
    public static String LOGIN_URL = "https://ibank.klikbca.com/";
    public static String LOGIN_ACTION = "https://ibank.klikbca.com/authentication.do";
    public static String LOGOUT_ACTION = "https://ibank.klikbca.com/authentication.do?value(actions)=logout";
    public static String SALDO_URL = "https://ibank.klikbca.com/balanceinquiry.do";
    public static String MENU_SALDO_URL = "https://m.klikbca.com/accountstmt.do?value(actions)=menu";
    public static String GET_IPADDRESS_URL = "http://myjsonip.appspot.com/";
    
    public static String ACCOUNT_STATEMENT_MENU = "https://ibank.klikbca.com/accountstmt.do?value(actions)=menu";
    public static String ACCOUNT_STATEMENT_ACTION = "https://ibank.klikbca.com/accountstmt.do?value(actions)=acct_stmt";
    public static String ACCOUNT_STATEMENT_VIEW = "https://ibank.klikbca.com/accountstmt.do?value(actions)=acctstmtview";
    
    /**
     * @return 
     */
    public static String GetTime() {
        return (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(new Date());
    }
    
    /**
     * @param Header     
     * @param Log     
     */
    public static void Logger(String Header, Exception Log){
        System.out.println(MessageFormat.format("{0}{1}:{2}", Header, GetTime(), Log));
    }
    
    /**
     * @param Header     
     * @param Log     
     */
    public static void Logger(String Header, String Log){
        System.out.println(MessageFormat.format("{0}{1}:{2}", Header, GetTime(), Log));
    }
    
    /**
     * @param delimiter
     * @param map
     * @return 
     */
    public static String Implode(String delimiter, Map<String, String> map) {
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : map.entrySet()) {
            if (!first) {
                sb.append(delimiter);
            }
            sb.append(e.getKey()).append("=").append(e.getValue());
            first = false;
        }
        return sb.toString();
    }
    
    /**
     * @param string
     * @return 
     */
    public static Map<String, String> Explode(String string) {
        Map<String, String> head = new HashMap<String, String>(){};
        String arr[] = string.split(";");
        for (String arr1 : arr) {
            String[] arr2 = arr1.split("=");
            head.put(arr2[0], arr2[1]);
        }
        return head;
    }
    
    /**
     * @param text
     * @return 
     */
    public static String GetString(String text) {
        text = text.replaceAll("[^(\\x20-\\x7F)]*", "");
        text = text.replace("'", "");
        text = text.replace("\\", "");
        return text;
    }

    /**
     * @param text
     * @return 
     */
    public static String GetNominal(String text) {
        text = text.replace(".00", "");
        text = text.replace(",", "");
        return text;
    }
    
    /**
     * @return
     */
    public static Map<String, String> GetQueryParam() {
        LocalDateTime finishdate = LocalDateTime.now();
        LocalDateTime startdate = finishdate.minus(Period.ofDays(1));
        return new HashMap<String, String>() {{
                    put("r1", "1");
                    put("value(D1)", "0");
                    put("value(startDt)", String.valueOf(startdate.getDayOfMonth()));
                    put("value(startMt)", String.valueOf(startdate.getMonthValue()));
                    put("value(startYr)", String.valueOf(startdate.getYear()));
                    put("value(endDt)", String.valueOf(finishdate.getDayOfMonth()));
                    put("value(endMt)", String.valueOf(finishdate.getMonthValue()));
                    put("value(endYr)", String.valueOf(finishdate.getYear()));
                    put("value(fDt)", "");
                    put("value(tDt)", "");
                    put("value(submit1)", "Lihat Mutasi Rekening");
                }};
    }
    
}