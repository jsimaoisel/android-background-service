package pt.pfc.mon5gservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocalService extends Service {
    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("LocalService", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Log.i("LocalService", "startId " + startId);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("LocalService", "onDestroy()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /** methods for clients */
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }

    public void startPing() {
        if (!ping.isRunning())
            executor.submit(ping);
    }

    public void stopPing() {
        if (ping.isRunning())
            ping.stop();
    }

    PingJob ping = new PingJob(this);
    ExecutorService executor = Executors.newFixedThreadPool(4);
}
