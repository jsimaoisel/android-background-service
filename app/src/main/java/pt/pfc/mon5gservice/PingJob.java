package pt.pfc.mon5gservice;

import android.content.Context;
import android.util.Log;
import java.time.Instant;

public class PingJob implements Runnable {
    private final Context ctx;
    private boolean isRunning = false;

    public PingJob(Context ctx) {
        this.ctx = ctx;
    }
    
    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            Log.d("PingJob", "running " + Instant.now() + " in context " + ctx.toString());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("PingJob", "Ping finished " + Instant.now());
    }

    public void stop() { isRunning = false; }

    public boolean isRunning() { return isRunning; }
}
