/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Dirga
 */
public class Conf {
    
    private static final String DBHOST = "localhost";
    private static final String DBPORT = "3306";
    private static final String DBUSER = "root";
    private static final String DBPASS = "admin123";
    private static final String DBNAME = "ibank_bca";
    private static final String USER = "user";
    private static final String PASS = "pass";
    private static final long LOOPWAIT = 1;
   
    
    /**
     * @return 
     */
    public static String GetUser() {
        return USER;
    }
    
    /**
     * @return 
     */
    public static String GetPassword() {
        return PASS;
    }
    
    /**
     * @return 
     */
    public static long GetLoopWait() {
        return LOOPWAIT;
    }
    
    /**
     * @return 
     */
    public static String GetDbUser() {
        return DBUSER;
    }

    /**
     * @return 
     */
    public static String GetDbPass() {
        return DBPASS;
    }

    /**
     * @return 
     */
    public static String GetDbPort() {
        return DBPORT;
    }

    /**
     * @return 
     */
    public static String GetDbHost() {
        return DBHOST;
    }

    /**
     * @return 
     */
    public static String GetDbName() {
        return DBNAME;
    }
    
}