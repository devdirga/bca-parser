/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bcaparser;

import bca.Bca;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.UncheckedIOException;
import util.ConfigReader;
import util.Tools;

/**
 *
 * @author Dirga
 */
public class BCAParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String IP = GetIPAddress();
        new Thread(() -> {
            while (true) {
                try {
                    Bca bca = new Bca(ConfigReader.GetUser(), ConfigReader.GetPassword(), IP);
                    bca.GetMutation();
                    bca.Logout();
                    Thread.sleep(ConfigReader.GetLoopWait() * 60 * 1000);
                } catch (InterruptedException e) {
                    System.out.println("[ERROR] " + Tools.GetTime() + " : " + e.getMessage());
                }
            }
        }).start();
    }

    public static String GetIPAddress() {
        String IP = Tools.EMPTY;
        try {
            String data = Jsoup.connect(Tools.GET_IPADDRESS_URL)
                    .ignoreContentType(true)
                    .execute()
                    .body();
            JSONObject obj = new JSONObject(data);
            IP = obj.getString("ip");
        } catch (IOException | JSONException | UncheckedIOException ex) {
            System.out.println("[ERROR] " + Tools.GetTime() + " : " + ex);
        }
        return IP;
    }
}
