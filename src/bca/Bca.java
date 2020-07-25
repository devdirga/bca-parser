/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bca;

import db.DBAdapter;
import db.Mutation;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Tools;

/**
 *
 * @author Dirga
 */
public class Bca {
    
    DBAdapter DB;
    Map<String, String> mapHasInserted = new HashMap();
    private final String USER;
    private final String PASS;
    protected boolean isLoggedIn = false;
    public String IP;
    
    public Map<String, String> Headers = new HashMap<String, String>() {{
            put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            put("Accept-Encoding", "gzip, deflate, br");
            put("Accept-Language", "en-US,en;q=0.9");
            put("Cache-Control", "max-age=0");
            put("Connection", "keep-alive");
            put("Content-Length", "321");
            put("Content-Type", "application/x-www-form-urlencoded");
            put("Referer", Tools.LOGIN_ACTION);
            put("Sec-Fetch-Dest", "document");
            put("Sec-Fetch-Mode", "navigate");
            put("Sec-Fetch-Site", "same-origin");
            put("Sec-Fetch-User", "1");
            put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
            put("Origin", "https://ibank.klikbca.com");
            put("Upgrade-Insecure-Requests", "1");
    }};

    public Map<String, String> Cookies = new HashMap<>();

    /**
     * @param USER
     * @param PASS
     * @param IP
     */
    public Bca(String USER, String PASS, String IP) {
        this.USER = USER;
        this.PASS = PASS;
        this.IP = IP;
        DB = new DBAdapter();
    }

    /**
     * @param USER
     * @param PASS
     */
    private void Login(String USER, String PASS) {
        try {
            Response res = Jsoup.connect(Tools.LOGIN_URL)
                    .headers(Headers)
                    .execute();
            this.Headers.put("Cookie", Tools.Implode(";", res.cookies()));
            this.Cookies = res.cookies();
        } catch (IOException ex) {
            System.out.println("[IOException] " + Tools.GetTime() + ex.getMessage());
        }
        Map<String, String> data = new HashMap<String, String>() {{
                put("value(user_id)", USER);
                put("value(pswd)", PASS);
                put("value(Submit)", "LOGIN");
                put("value(actions)", "login");
                put("value(user_ip)", IP);
                put("user_ip", IP);
                put("value(mobile)", "true");
                put("mobile", "true");
        }};
        try {
            Jsoup.connect(Tools.LOGIN_ACTION)
                    .referrer(Tools.LOGIN_URL)
                    .cookies(this.Cookies)
                    .headers(this.Headers)
                    .data(data)
                    .post();
            this.isLoggedIn = true;
        } catch (IOException ex) {
            System.out.println("[ERROR] " + Tools.GetTime() + " : " + ex);
        }
    }

    /**
     * Get Balance
     */
    public void GetBalance() {
        String saldo;
        if (!this.isLoggedIn) {
            this.Login(USER, PASS);
        }
        try {
            Document doc = Jsoup.connect(Tools.SALDO_URL)
                    .referrer(Tools.MENU_SALDO_URL)
                    .headers(this.Headers)
                    .cookies(this.Cookies)
                    .post();
            Elements tables = doc.select("table");
            Elements tr = tables.get(2).select("tr");
            Elements td = tr.get(1).select("td");
            saldo = td.get(3).text();
            System.out.println("[SUCCESS] " + Tools.GetTime() + " : " + saldo);
        } catch (IOException ex) {
            Logger.getLogger(Bca.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get Mutation
     */
    public void GetMutation() {
        List<Mutation> mutationList = new ArrayList<>();
        mapHasInserted = DBAdapter.GetMapWasInserted();
        if (!this.isLoggedIn) {
            this.Login(USER, PASS);
        }
        try {
            Jsoup.connect(Tools.ACCOUNT_STATEMENT_ACTION)
                    .referrer(Tools.ACCOUNT_STATEMENT_MENU)
                    .headers(this.Headers)
                    .cookies(this.Cookies)
                    .post();

            Document docs = Jsoup.connect(Tools.ACCOUNT_STATEMENT_VIEW)
                    .referrer(Tools.ACCOUNT_STATEMENT_ACTION)
                    .headers(this.Headers)
                    .cookies(this.Cookies)
                    .data(Tools.GetQueryParam())
                    .post();

            Elements tables = docs.select("table");
            Elements tr = tables.get(4).select("tr");
            int i = 0;
            for (Element elm : tr) {
                if (i > 0) {
                    Elements td = elm.select("td");                    
                    String Dbkr = Tools.GetString(td.get(4).text()).equals("CR") ? "CR" : "DB";
                    if (!mapHasInserted.containsKey(Tools.GetString(td.get(1).text()) + ":" + Tools.GetNominal(td.get(3).text()) + ":" + Dbkr + ":" + Tools.GetNominal(td.get(5).text()))){
                        if( mutationList.add(new Mutation(Tools.GetString(td.get(1).text()), "BCA", Dbkr, Tools.GetNominal(td.get(3).text()), Tools.GetNominal(td.get(5).text())))) {
                            System.out.println("[SUCCESS] " + Tools.GetTime() + " New Data inserted"  );
                        }
                    } else {
                        System.out.println("[INFO] " + Tools.GetTime() + " Data already exist"  );
                    }
                }
                i++;
            }
            DB.InsertMutation(mutationList);
        } catch (IOException e) {
            System.out.println("[ERROR] " + Tools.GetTime() + " : " + e.getMessage());
        }
    }

    /**
     * Logout
     */
    public void Logout() {
        try {
            Jsoup.connect(Tools.LOGIN_ACTION)
                    .headers(this.Headers)
                    .cookies(this.Cookies)
                    .referrer(Tools.LOGIN_URL)
                    .execute();
        } catch (IOException ex) {
            System.out.println("[ERROR] " + Tools.GetTime() + " : " + ex);
        }
    }
    
}
