package pt.pfc.a5gmonservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BindingActivity extends Activity {
    LocalService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button rnd = findViewById(R.id.random);
        rnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBound) {
                    // Call a method from the LocalService.
                    // However, if this call were something that might hang, then this request should
                    // occur in a separate thread to avoid slowing down the activity performance.
                    int num = mService.getRandomNumber();
                    Toast.makeText(BindingActivity.this, "number: " + num, Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button ping = findViewById(R.id.ping);
        ping.setOnClickListener(new View.OnClickListener() {
            boolean start = true;
            @Override
            public void onClick(View view) {
                if (mBound) {
                    if (start)
                        mService.startPing();
                    else
                        mService.stopPing();
                    start = !start;
                }
            }
        });
        TextView txt = findViewById(R.id.hello);
        txt.setText("Binding Activity");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start the service
        Intent intent = new Intent(this, LocalService.class);
        startService(intent);
        // Bind to the service (to directly call operations on service)
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        Log.i("BindingActivity", "START");
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
        Log.i("BindingActivity", "STOP");
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}