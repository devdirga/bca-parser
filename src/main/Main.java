/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tool | Templates
 * and open the template in the editor.
 */
package main;

import model.Bca;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.UncheckedIOException;
import util.Conf;
import util.Tool;

/**
 *
 * @author Dirga
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String IP = GetIPAddress();
        new Thread(() -> {
            while (true) {
                try {
                    Bca bca = new Bca(Conf.GetUser(), Conf.GetPassword(), IP);
                    bca.GetMutation();
                    bca.Logout();
                    Thread.sleep(Conf.GetLoopWait() * 60 * 1000);
                } catch (InterruptedException e) {
                    Tool.Logger("[ERROR] " + Main.class.getName() + "[LINE-36]", e);
                }
            }
        }).start();
    }

    /**
     * @return 
     */
    public static String GetIPAddress() {
        String IP = Tool.EMPTY;
        try {
            String data = Jsoup.connect(Tool.GET_IPADDRESS_URL)
                    .ignoreContentType(true)
                    .execute()
                    .body();
            JSONObject obj = new JSONObject(data);
            IP = obj.getString("ip");
        } catch (IOException | JSONException | UncheckedIOException e) {
            Tool.Logger("[ERROR] " + Main.class.getName(), e);
        }
        return IP;
    }
}
