package com.anatoly.chugaev.VKAPP;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.anatoly.chugaev.VKAPP.app.Account;
import com.anatoly.chugaev.VKAPP.app.Constants;
import com.anatoly.chugaev.VKAPP.network.ApiClient;
import com.anatoly.chugaev.VKAPP.network.Model.LongPollServer.DataLongPollServerResponse;
import com.anatoly.chugaev.VKAPP.network.VkApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InterruptedIOException;

public class MyService extends Service {

    private static final String TAG = "my_service";
    private Thread mThread;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        MyRun myRun = new MyRun();
        mThread = new Thread(myRun);
        mThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThread.interrupt();
        Log.d(TAG, "onDestroy");
    }

    class MyRun implements Runnable {
        @Override
        public void run() {
            Intent intent = new Intent(Constants.BROADCAST_ACTION);
            VkApi vkApi = ApiClient.getClient().create(VkApi.class);
            try {
                DataLongPollServerResponse response = vkApi.getLongPollServer(1, 2,
                            Constants.API_VERSION,
                            Account.get(getApplicationContext()).access_token)
                            .execute().body().getResponse();
                Log.d(TAG, response.getKey());
                Log.d(TAG, response.getServer());
                Log.d(TAG, String.valueOf(response.getTs()));
                long ts = response.getTs();
                int count = 0;
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                while (true) {
                    String json = Jsoup
                            .connect(getUrl(response.getServer(), response.getKey(), ts))
                            .timeout(1000000)
                            .ignoreContentType(true).execute().body();
                    JSONObject jsonObject = new JSONObject(json);
                    ts = jsonObject.getLong("ts");
                    JSONArray updates = jsonObject.getJSONArray("updates");
                    for (int i = 0; i < updates.length(); i++) {
                        if (updates.getJSONArray(i).getString(0).equals("4")) {
                            count++;
                            NotificationCompat.Builder builder =
                                    new NotificationCompat.Builder(getApplicationContext())
                                            .setSmallIcon(R.mipmap.vk_logo)
                                            .setLargeIcon(BitmapFactory.decodeResource(
                                                    getApplicationContext().getResources(),
                                                    R.mipmap.vk_logo))
                                            .setWhen(System.currentTimeMillis())
                                            .setContentTitle(count + " new messages");
                            notificationManager.notify(1, builder.build());
                            intent.putExtra(Constants.PARAM_SERVICE, updates.getJSONArray(i).toString());
                            sendBroadcast(intent);
                            Log.d(TAG, updates.getJSONArray(i).toString());
                        }
                    }
                }
            } catch (InterruptedIOException e) {
                Log.d(TAG, "interrupt");
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }

    String getUrl(String server, String key, long ts) {
        Uri builder = Uri.parse("https://" + server)
                .buildUpon()
                .appendQueryParameter("act", "a_check")
                .appendQueryParameter("key", key)
                .appendQueryParameter("ts", String.valueOf(ts))
                .appendQueryParameter("wait", "25")
                .appendQueryParameter("mode", "2")
                .appendQueryParameter("version", "2")
                .build();
        return builder.toString();
    }
}
