/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tool | Templates
 * and open the template in the editor.
 */
package model;

import util.Connect;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Tool;

/**
 *
 * @author Dirga
 */
public class Bca {

    Connect DB;
    Map<String, String> Inserted = new HashMap();
    private final String USER;
    private final String PASS;
    protected boolean isLoggedIn = false;
    public String IP;

    public Map<String, String> Headers = new HashMap<String, String>() {
        {
            put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            put("Accept-Encoding", "gzip, deflate, br");
            put("Accept-Language", "en-US,en;q=0.9");
            put("Cache-Control", "max-age=0");
            put("Connection", "keep-alive");
            put("Content-Length", "321");
            put("Content-Type", "application/x-www-form-urlencoded");
            put("Referer", Tool.LOGIN_ACTION);
            put("Sec-Fetch-Dest", "document");
            put("Sec-Fetch-Mode", "navigate");
            put("Sec-Fetch-Site", "same-origin");
            put("Sec-Fetch-User", "1");
            put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
            put("Origin", "https://ibank.klikbca.com");
            put("Upgrade-Insecure-Requests", "1");
        }
    };

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
//        DB = new Connect();
    }

    /**
     * @param USER
     * @param PASS
     */
    private void Login(String USER, String PASS) {
        try {
            Response res = Jsoup.connect(Tool.LOGIN_URL)
                    .headers(Headers)
                    .execute();
            this.Headers.put("Cookie", Tool.Implode(";", res.cookies()));
            this.Cookies = res.cookies();
        } catch (IOException e) {
            Tool.Logger("[ERROR]" + Bca.class.getName() + " line 83 ", e.getMessage());
        }
        Map<String, String> data = new HashMap<String, String>() {
            {
                put("value(user_id)", USER);
                put("value(pswd)", PASS);
                put("value(Submit)", "LOGIN");
                put("value(actions)", "login");
                put("value(user_ip)", IP);
                put("user_ip", IP);
                put("value(mobile)", "true");
                put("mobile", "true");
            }
        };
        try {
            Jsoup.connect(Tool.LOGIN_ACTION)
                    .referrer(Tool.LOGIN_URL)
                    .cookies(this.Cookies)
                    .headers(this.Headers)
                    .data(data)
                    .post();
            this.isLoggedIn = true;
        } catch (IOException e) {
            Tool.Logger("[ERROR]" + Bca.class.getName() + " line 104 ", e.getMessage());
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
            Document doc = Jsoup.connect(Tool.SALDO_URL)
                    .referrer(Tool.MENU_SALDO_URL)
                    .headers(this.Headers)
                    .cookies(this.Cookies)
                    .post();
            Elements tables = doc.select("table");
            Elements tr = tables.get(2).select("tr");
            Elements td = tr.get(1).select("td");
            saldo = td.get(3).text();
        } catch (IOException e) {
            Tool.Logger("[ERROR]" + Bca.class.getName() + " line 123 ", e.getMessage());
        }
    }

    /**
     * Get Mutation
     */
    public void GetMutation() {
        List<Mutation> mutations = new ArrayList<>();
//        Inserted = Connect.GetMapWasInserted();
        if (!this.isLoggedIn) {
            this.Login(USER, PASS);
        }
        try {
            Jsoup.connect(Tool.ACCOUNT_STATEMENT_ACTION)
                    .referrer(Tool.ACCOUNT_STATEMENT_MENU)
                    .headers(this.Headers)
                    .cookies(this.Cookies)
                    .post();

            Document docs = Jsoup.connect(Tool.ACCOUNT_STATEMENT_VIEW)
                    .referrer(Tool.ACCOUNT_STATEMENT_ACTION)
                    .headers(this.Headers)
                    .cookies(this.Cookies)
                    .data(Tool.GetQueryParam())
                    .post();

            Elements tables = docs.select("table");
            Elements tr = tables.get(4).select("tr");
            int i = 0;
            for (Element elm : tr) {
                if (i > 0) {
                    Elements td = elm.select("td");
                    Tool.Logger("[INFO]", MessageFormat.format("{0}:{1}:{2}:{3}",
                            Tool.GetString(td.get(1).text()),
                            Tool.GetNominal(td.get(3).text()),
                            Tool.GetString(td.get(4).text()).equals("CR") ? "CR" : "DB",
                            Tool.GetNominal(td.get(5).text())));
                    /*
                    if (!Inserted.containsKey(MessageFormat.format("{0}:{1}:{2}:{3}",
                            Tool.GetString(td.get(1).text()),
                            Tool.GetNominal(td.get(3).text()),
                            Tool.GetString(td.get(4).text()).equals("CR") ? "CR" : "DB",
                            Tool.GetNominal(td.get(5).text())))) {
                        mutations.add(new Mutation(Tool.GetString(td.get(1).text()),
                                "BCA",
                                Tool.GetString(td.get(4).text()).equals("CR") ? "CR" : "DB",
                                Tool.GetNominal(td.get(3).text()),
                                Tool.GetNominal(td.get(5).text())));
                    } else {
                        Tool.Logger("[INFO]" + Bca.class.getName() + " line 167 ", " X Data already exist...");
                    }
                    */
                }
                i++;
            }
//            DB.InsertMutation(mutations);
        } catch (IOException e) {
            Tool.Logger("[ERROR]" + Bca.class.getName() + " line 174 ", e.getMessage());
        }
    }

    /**
     * Logout
     */
    public void Logout() {
        try {
            Jsoup.connect(Tool.LOGIN_ACTION)
                    .headers(this.Headers)
                    .cookies(this.Cookies)
                    .referrer(Tool.LOGIN_URL)
                    .execute();
        } catch (IOException e) {
            Tool.Logger("[ERROR]" + Bca.class.getName() + " line 189 ", e.getMessage());
        }
    }

}
