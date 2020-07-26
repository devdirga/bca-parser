/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tool | Templates
 * and open the template in the editor.
 */
package util;

import model.Mutation;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Bca;

/**
 *
 * @author Dirga
 */
public class Connect {

    Connection cn = null;
    Statement st = null;
    ResultSet hasilResultSet;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy // HH-mm-ss");
    String GET_MUTATION = "SELECT IDX FROM BANKMUTASI WHERE KODEBANK=''{0}'' AND KETERANGAN=''{1}'' AND NOMINAL={2,number,#.##} AND DBKR=''{3}'' AND SALDO={4,number,#.##} AND TANGGAL BETWEEN (DATE_SUB(CURDATE(), INTERVAL 3 DAY)) AND CURDATE()";
    String GET_DATA = "SELECT KETERANGAN,NOMINAL,DBKR,SALDO FROM BANKMUTASI WHERE KODEBANK='BCA' AND TANGGAL BETWEEN (DATE_SUB(CURDATE(), INTERVAL 3 DAY)) AND CURDATE()";
    static String GET_MAP_INSERTED = "SELECT KETERANGAN,NOMINAL,DBKR,SALDO FROM BANKMUTASI WHERE KODEBANK='BCA' AND TANGGAL BETWEEN (DATE_SUB(CURDATE(), INTERVAL 3 DAY)) AND CURDATE()";
    String INSERT_QUERY = "INSERT INTO BANKMUTASI (KODEBANK,TANGGAL,KETERANGAN,NOMINAL,DBKR,SALDO,TGLSYS,WAKTUSYS,CATATAN1,CATATAN2) VALUES(''{0}'',CURDATE(),''{1}'',{2,number,#.##},''{3}'',{4,number,#.##}, CURDATE(),CURTIME(), ''{5}'',''{6}'' )";
    
    public void ConnectDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            cn = (Connection) DriverManager.getConnection(
                    MessageFormat.format("jdbc:mysql://{0}:{1}/{2}", Conf.GetDbHost(), Conf.GetDbPort(), Conf.GetDbName()), 
                    Conf.GetDbUser(), 
                    Conf.GetDbPass());
            st = cn.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            Tool.Logger("[ERROR]" + Bca.class.getName() + " line-41 ", e.getMessage());
        }
    }

    /**
     * @param mutation
     * @return 
     */
    public Boolean GetMutation(Mutation mutation) {
        try {
            ResultSet results = st.executeQuery(MessageFormat.format(GET_MUTATION
                    ,"BCA",mutation.getKeterangan(),Double.parseDouble(mutation.getNominal()) ,mutation.getDbkr(),Double.parseDouble(mutation.getSaldo())));
            return results.next();
        } catch (SQLException e) {
            Tool.Logger("[ERROR]" + Bca.class.getName() + " line-59 ", e.getMessage());
            return false;
        }
    }

    public Boolean GetData() {
        ResultSet results;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try (Connection con = (Connection) DriverManager.getConnection(MessageFormat.format("jdbc:mysql://{0}:{1}/{2}", Conf.GetDbHost(), Conf.GetDbPort(), Conf.GetDbName())
                    , Conf.GetDbUser(), Conf.GetDbPass()); Statement stmt = con.createStatement()) {
                results = stmt.executeQuery(GET_DATA);
                if (results != null) {
                    while (results.next()) {
                    }
                }
                if(results != null){
                    results.close();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            Tool.Logger("[ERROR]" + Bca.class.getName() + " line-87 ", e.getMessage());
        }
        return false;
    }

    /**
     * @return 
     */
    public static Map<String, String> GetMapWasInserted() {
        ResultSet results;
        Map<String, String> res = new HashMap();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try (Connection con = (Connection) DriverManager.getConnection(MessageFormat.format("jdbc:mysql://{0}:{1}/{2}", Conf.GetDbHost(), Conf.GetDbPort(), Conf.GetDbName()), 
                    Conf.GetDbUser(), 
                    Conf.GetDbPass()); Statement stmt = con.createStatement()) {
                results = stmt.executeQuery(GET_MAP_INSERTED);
                if (results != null) {
                    while (results.next()) {
                        res.put(MessageFormat.format("{0}:{1}:{2}:{3}", results.getString("KETERANGAN"),results.getString("NOMINAL"),results.getString("DBKR"),results.getString("SALDO")),results.getString("KETERANGAN"));
                    }
                }
                if(results!=null){
                    results.close();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            Tool.Logger("[ERROR]" + Connect.class.getName() + " line-115 ", e.getMessage());
        }
        return res;
    }

    /**
     * @param mutations
     * @return 
     */
    public String InsertMutation(List<Mutation> mutations) {
        ConnectDB();
        String error = "Success";
        try {
            for (Mutation mutation : mutations) {
                if (!GetMutation(mutation)) {
                    Mutation mut = mutation;
                    if (GetData() == false) {
                        st.execute(MessageFormat.format(INSERT_QUERY,
                                mut.getKodebank(),mut.getKeterangan(),Double.parseDouble(mut.getNominal()),mut.getDbkr(),Double.parseDouble(mut.getSaldo()),mut.getCatatan1(),mut.getCatatan2()));
                        Tool.Logger("[INFO]" + Connect.class.getName() , MessageFormat.format(" SUCCESS INSERT DATA :\nBANK : {0}\nKETERANGAN : {1}\nNOMINAL : {2}\n---------------------------------------------", mut.getKodebank(), mut.getKeterangan(), mut.getNominal()) );  
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
            Tool.Logger("[ERROR]" + Connect.class.getName() + " line-156 ", e.getMessage());
        }
    }

}