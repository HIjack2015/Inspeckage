package mobi.acpm.inspeckage.webserver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by acpm on 17/11/15.
 */
public class InspeckageService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private WebServer ws;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Context context = getApplicationContext();

        String host = null;
        int port = 8008;
        if (intent != null && intent.getExtras() != null) {
            host = intent.getStringExtra("host");
            port = intent.getIntExtra("port", 8008);
        }

        try {

            ws = new WebServer(host, port, context);


        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Service started on port " + port, Toast.LENGTH_LONG).show();

        //android 8.0 need a ongoing notification for foreground service
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = null;
            channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ws!=null)
            ws.stop();

        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }
}
