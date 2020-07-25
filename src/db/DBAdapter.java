/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.ConfigReader;
import util.Tools;

/**
 *
 * @author Dirga
 */
public class DBAdapter {

    Connection cn = null;
    Statement st = null;
    ResultSet hasilResultSet;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy // HH-mm-ss");

    public void ConnectDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + ConfigReader.GetDbHost() + ":" + ConfigReader.GetDbPort() + "/" + ConfigReader.GetDbName();
            String user = ConfigReader.GetDbUser();
            String password = ConfigReader.GetDbPass();
            cn = (Connection) DriverManager.getConnection(url, user, password);
            st = cn.createStatement();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("[ERROR] " + Tools.GetTime() + " : " + e.getMessage());
        }
    }

    public Boolean GetMutation(Mutation mutation) {
        ResultSet results;
        String query;
        try {
            query = "SELECT * from BANKMUTASI WHERE KODEBANK = 'BCA' AND KETERANGAN = '" 
                    + mutation.getKeterangan() + "' AND NOMINAL = '" 
                    + mutation.getNominal() + "' AND DBKR = '" 
                    + mutation.getDbkr() + "' AND SALDO ='" 
                    + mutation.getSaldo() + "' AND TANGGAL BETWEEN (DATE_SUB(CURDATE(), INTERVAL 3 DAY)) AND CURDATE()";
            results = st.executeQuery(query);
            return results.next();
        } catch (SQLException e) {
            System.out.println("[ERROR] " + Tools.GetTime() + " : " + e.getMessage());
            return false;
        }
    }

    public Boolean GetData() {
        ResultSet results;
        try {
            String query = "SELECT KETERANGAN,NOMINAL,DBKR,SALDO FROM BANKMUTASI WHERE KODEBANK='BCA' AND TANGGAL BETWEEN (DATE_SUB(CURDATE(), INTERVAL 3 DAY)) AND CURDATE()";
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + ConfigReader.GetDbHost() + ":" + ConfigReader.GetDbPort() + "/" + ConfigReader.GetDbName();
            String user = ConfigReader.GetDbUser();
            String password = ConfigReader.GetDbPass();
            try (Connection con = (Connection) DriverManager.getConnection(url, user, password); Statement stmt = con.createStatement()) {
                results = stmt.executeQuery(query);
                if (results != null) {
                    while (results.next()) {
                        System.out.println(results.getString("keterangan"));
                        System.out.println(results.getString("nominal"));
                        System.out.println(results.getString("dbkr"));
                        System.out.println(results.getString("saldo"));
                        System.out.println(results.getString("keterangan"));
                    }
                }
                if(results != null){
                    results.close();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("[ERROR] " + Tools.GetTime() + " : " + e.getMessage());
        }
        return false;
    }

    public static Map<String, String> GetMapWasInserted() {
        ResultSet results;
        Map<String, String> res = new HashMap();
        try {
            String query = "SELECT KETERANGAN,NOMINAL,DBKR,SALDO FROM BANKMUTASI WHERE KODEBANK='BCA' AND TANGGAL BETWEEN (DATE_SUB(CURDATE(), INTERVAL 3 DAY)) AND CURDATE()";
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + ConfigReader.GetDbHost() + ":" + ConfigReader.GetDbPort() + "/" + ConfigReader.GetDbName();
            String user = ConfigReader.GetDbUser();
            String password = ConfigReader.GetDbPass();
            try (Connection con = (Connection) DriverManager.getConnection(url, user, password); Statement stmt = con.createStatement()) {
                results = stmt.executeQuery(query);
                if (results != null) {
                    while (results.next()) {
                        res.put(results.getString("keterangan") + ":" + results.getString("nominal") + ":" + results.getString("dbkr") + ":" + results.getString("saldo"), results.getString("keterangan"));
                    }
                }
                if(results!=null){
                    results.close();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("[ERROR] " + Tools.GetTime() + " : " + e.getMessage());
        }
        return res;
    }

    public String InsertMutation(List<Mutation> mutations) {
        ConnectDB();
        String error = "SUCCESS";
        try {
            for (Mutation mutation : mutations) {
                if (!GetMutation(mutation)) {
                    Mutation mut = mutation;
                    if (GetData() == false) {
                        st.execute("INSERT INTO BANKMUTASI (KODEBANK,TANGGAL,KETERANGAN,IDOUTLET,NOMINAL,DBKR,SALDO,TGLSYS,WAKTUSYS,CATATAN1,CATATAN2) VALUES "
                                + "('" + mut.getKodebank() + "',curdate(),'" 
                                + mut.getKeterangan() + "','','" + mut.getNominal() + "','" 
                                + mut.getDbkr() + "','" 
                                + mut.getSaldo() + "',CURDATE(),CURTIME(),'" 
                                + mut.getCatatan1() + "','" 
                                + mut.getCatatan2() + "')");
                        error = "SUCCESS";
                        System.out.println("[SUCCESS] " + Tools.GetTime() + " : " + "\n BANK : " 
                                + mut.getKodebank() + "\n KETERANGAN : " 
                                + mut.getKeterangan() + "\n Nominal : " 
                                + mut.getNominal() + " \n ---------------------------------------------");
                    } else {
                        System.out.println("[ERROR] " + Tools.GetTime() + " GET DATA CHECK " );
                    }
                }
            }
        } catch (SQLException ex) {
            error = ex.getLocalizedMessage();
        } finally {
            CloseDB();
        }
        return error;
    }

    public void CloseDB() {
        try {
            if (!cn.isClosed()) {
                cn.close();
            }
            if (!st.isClosed()) {
                st.close();
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] " + Tools.GetTime() + " : " + e.getMessage());
        }
    }

}
