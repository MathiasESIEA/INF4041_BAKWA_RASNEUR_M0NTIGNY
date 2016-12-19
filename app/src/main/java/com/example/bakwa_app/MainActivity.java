package com.example.bakwa_app;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    DatePickerDialog d = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter inf = new IntentFilter(BIERS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BierUpdate(),inf);

        TextView tv_hw = (TextView) findViewById(R.id.tv_hello_world);
        Button btn_hw = (Button) findViewById(R.id.btn_hello_world);
        Button btn_n = (Button) findViewById(R.id.btn_notif);
        Button btn_sa = (Button) findViewById(R.id.btn_sa);
        Button btn_st = (Button) findViewById(R.id.btn_start);

        String aujourdhui = DateUtils.formatDateTime(getApplicationContext(), (new Date()).getTime(), DateFormat.FULL);

        tv_hw.setText(tv_hw.getText()+" : "+aujourdhui);

        btn_hw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.show();
            }
        });

        btn_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification_bakwa();
            }
        });

        btn_sa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(start);
            }
        });

        btn_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetBiersServices.startActionBiere(getApplicationContext());
            }
        });

        DatePickerDialog.OnDateSetListener ods = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                TextView tv_hw = (TextView) findViewById(R.id.tv_hello_world);
                tv_hw.setText(dayOfMonth+" / "+month+" / "+year);
            }
        };
        d = new DatePickerDialog(this, ods, 0,0,0 );
    }

    public void notification_bakwa(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notif)
                        .setContentTitle(" Qui aswaf ?")
                        .setContentText("Consulter la liste des bières disponibles");

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mBuilder.build());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.notif:
                Toast.makeText(getApplicationContext(),"Bienvenue sur l'application ONASWAF",Toast.LENGTH_LONG).show();
                GetBiersServices.startActionBiere(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static final String BIERS_UPDATE = "com.octip.cours.inf4042_11.BIERS_UPDATE";
    public class BierUpdate extends BroadcastReceiver {
        public void onReceive(Context c, Intent i){
            Log.d("Rasneur",i.getAction());
            Toast.makeText(getApplicationContext(),"Download",Toast.LENGTH_LONG).show();
        }
    }
}
