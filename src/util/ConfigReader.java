/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Dirga
 */
public class ConfigReader {
    
    private static final String DBHOST = "localhost";
    private static final String DBPORT = "3306";
    private static final String DBUSER = "root";
    private static final String DBPASS = "admin123";
    private static final String DBNAME = "ibank_bca";
    private static final String USER = "BCA-USERNAME";
    private static final String PASS = "BCA-PASSWORD";
    private static final long LOOPWAIT = 10;
    
    /**
     */
    private static Properties LoadProperties(String sFile) throws FileNotFoundException, IOException {
        Properties p = new Properties();
        try {
            try (FileInputStream in = new FileInputStream(sFile)) {
                p.load(in);
            }
        } catch (IOException iOException) {
        }
        return p;
    }
    
    /**
     * @return 
     */
    public static String GetUser() {
        /*
        Properties p = new Properties();
        try {
            p = LoadProperties(PROP_FILE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        return USER;
    }
    
    /**
     * @return 
     */
    public static String GetPassword() {
        /*
        Properties p = new Properties();
        try {
            p = LoadProperties(PROP_FILE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        return PASS;
    }
    
    /**
     * @return 
     */
    public static long GetLoopWait() {
        /*
        Properties p = new Properties();
        try {
            p = LoadProperties(PROP_FILE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        return LOOPWAIT;
    }
    
    /**
     * @return 
     */
    public static String GetDbUser() {
        /*
        Properties p = new Properties();
        try {
            p = LoadProperties(PROP_FILE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        return DBUSER;
    }

    /**
     * @return 
     */
    public static String GetDbPass() {
        /*
        Properties p = new Properties();
        try {
            p = LoadProperties(PROP_FILE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        return DBPASS;
    }

    /**
     * @return 
     */
    public static String GetDbPort() {
        /*
        Properties p = new Properties();
        try {
            p = LoadProperties(PROP_FILE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        return DBPORT;
    }

    /**
     * @return 
     */
    public static String GetDbHost() {
        /*
        Properties p = new Properties();
        try {
            p = LoadProperties(PROP_FILE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        return DBHOST;
    }

    /**
     * @return 
     */
    public static String GetDbName() {
        /*
        Properties p = new Properties();
        try {
            p = LoadProperties(PROP_FILE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        return DBNAME;
    }
    
}