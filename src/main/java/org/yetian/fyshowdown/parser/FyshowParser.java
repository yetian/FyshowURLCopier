package org.yetian.fyshowdown.parser;

/**
 * Created by solaris_navi on 23.08.15.
 */

import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class FyshowParser {

    // test
    /*
    public static void main(String args[])
    {
        String url = "http://ezfm.china-plus.net/index.php?m=index&a=cat_list&cid=224";
        FyshowParser fp = new FyshowParser(url);

        System.out.println(fp.getFyshowDS().size());
    }
    */

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString().replace('\r', ' ');
    }

    private static JSONObject readJsonFromUrl(String url) {
        try {
            InputStream is = new URL(url).openStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject j = new JSONObject(jsonText);
            is.close();
            return j;
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    private JSONObject fyurl;

    public FyshowParser(String url)
    {
        try {
            this.fyurl = this.readJsonFromUrl(url);
        }
        catch(Exception ex)
        {
            this.fyurl = null;
        }
    }

    public ArrayList<FyshowDS> getFyshowDS()
    {
        if (this.fyurl == null){
            return null;
        }

        ArrayList<FyshowDS> list = new ArrayList<FyshowDS>();

        try {
            JSONArray array = (JSONArray) this.fyurl.get("data");
            for (int i=0; i < array.length(); i++)
            {
                JSONObject j = (JSONObject)array.get(i);
                int id = Integer.parseInt(j.get("id").toString());
                int cid = Integer.parseInt(j.get("cid").toString());
                String title = j.get("title").toString();
                String lpic = j.get("lpic").toString();
                String url = j.get("url").toString();
                String update_time = j.get("update_time").toString();
                String f_time = j.get("f_time").toString();
                String size = j.get("size").toString();

                FyshowDS d = new FyshowDS();
                d.setId(id);
                d.setCid(cid);
                d.setTitle(title);
                d.setLpic(lpic);
                d.setUrl(url);
                d.setUpdate_time(update_time);
                d.setF_time(f_time);
                d.setSize(size);

                list.add(d);
            }
        }
        catch(Exception ex)
        {
            return null;
        }
        return list;
    }
}
