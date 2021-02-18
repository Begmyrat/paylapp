package com.mobiloby.paylapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    public static final int notify = 60000;  //interval between two services(Here Service run every 5 Minute)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling
    JSONObject jsonObject;
    JSONParser jsonParser;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mTimer != null) // Cancel if already existed
//            mTimer.cancel();
            Toast.makeText(this, "assa", Toast.LENGTH_SHORT).show();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task

//        createNotificationChannel();
//
//        Intent intent1 = new Intent(this, MainActivity.class);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
//
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
////        System.out.println(dtf.format(now));
//
//        Notification notification = new NotificationCompat.Builder(this, "ChannelID1")
//                .setContentTitle("MyApp Tutorial")
//                .setContentText("Our app is running \n" + now)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(pendingIntent)
//                .build();
//
//        startForeground(1, notification);

        return START_STICKY;
    }

    private void createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("ChannelID1", "ForegroundNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }

    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {



        return null;
    }

    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    // display toast

                    createNotificationChannel();

                    Intent intent1 = new Intent(MyService.this, MainActivity.class);

                    PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent1, 0);

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
//        System.out.println(dtf.format(now));

                    Notification notification = new NotificationCompat.Builder(MyService.this, "ChannelID1")
                            .setContentTitle("MyApp Tutorial")
                            .setContentText("Our app is running \n" + now)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pendingIntent)
                            .build();

                    startForeground(1, notification);

                    Toast.makeText(MyService.this, "Service is running : " + now, Toast.LENGTH_SHORT).show();

//                    getKonular();
                }
            });
        }
    }

    private void getKonular() {



        final String url = "https://mobiloby.com/KarincaProject/service_test.php";

        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {

                jsonParser = new JSONParser();

                HashMap<String, String> jsonData = new HashMap<>();

                int success = 0;
                try {

                    jsonObject = new JSONObject(jsonParser.sendPostRequestForImage(url, jsonData));

                    success = jsonObject.getInt("success");

                } catch (Exception ex) {
                    if (ex.getMessage() != null) {
                        Log.e("", ex.getMessage());
                    }
                }
                return String.valueOf(success);
            }


            @Override
            protected void onPostExecute(String res) {



                if (res.equals("1")) {

//                    Intent i = new Intent(MyService.this, MainActivity.class);
//                    PendingIntent pIntent = PendingIntent.getActivity(MyService.this, (int) System.currentTimeMillis(), i, 0);
//
//                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//                    Notification n = new Notification.Builder(MyService.this).setContentTitle("Gazi Ön Kayıt Bildirim Merkezi")
//                            .setContentText("Yeni bir mail var!").setSmallIcon(R.mipmap.ic_launcher)
//                            .setContentIntent(pIntent).setSound(alarmSound).setAutoCancel(true).build();
//
//                    NotificationManager notificationManager = (NotificationManager) MyService.this
//                            .getSystemService(MyService.this.NOTIFICATION_SERVICE);
//
//                    notificationManager.notify(0, n);
                }
                else{

                }

            }
        }.execute(null, null, null);

    }
}