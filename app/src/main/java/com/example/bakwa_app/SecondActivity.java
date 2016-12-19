package com.example.bakwa_app;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SecondActivity extends AppCompatActivity {

    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        rv = (RecyclerView) findViewById(R.id.rv_biere);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        rv.setAdapter(new BiersAdapter());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.notif:
                GetBiersServices.startActionBiere(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void notification_bakwa(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notif)
                        .setContentTitle("Bakwa Notif")
                        .setContentText("BAKWA - RASNEUR - MONTIGNY");

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mBuilder.build());
    }

    public JSONArray getBiersFromFile(){

        try{
            InputStream is = new FileInputStream(getCacheDir()+"/" + "bieres.json");
            byte[] buffer = new byte [is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8")); //Le tableau
        }catch(IOException e){
            e.printStackTrace();
            return new JSONArray();
        }catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private class BiersAdapter extends RecyclerView.Adapter< BiersAdapter.BierHolder>{

        public BiersAdapter(){

        }
        @Override
        public BiersAdapter.BierHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_second, null);
            BierHolder bh = new BierHolder(layoutView);
            return bh;
        }

        @Override
        public void onBindViewHolder( BiersAdapter.BierHolder holder, int position) {
            // Log.i("tag","coucou");
            JSONArray bieres = getBiersFromFile();
            try{

                JSONObject jo = bieres.getJSONObject(position);
                holder.modifierList(jo.getString("name"));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        public void setNewBiere(JSONArray ja){

            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return getBiersFromFile().length();
        }

        public class BierHolder extends RecyclerView.ViewHolder {
            private TextView itemView;

            public BierHolder(View itemView) {
                super(itemView);
                this.itemView = (TextView) itemView.findViewById(R.id.rv_biere_element);
            }
            public void modifierList(String s) {
                itemView.setText(s);
            }
        }
    }

    public static final String BIERS_UPDATE = "com.octip.cours.inf4042_11.BIERS_UPDATE";
    public class BierUpdate extends BroadcastReceiver {
        public void onReceive(Context c, Intent i){
            Log.d("Rasneur",i.getAction());
            BiersAdapter ba = (BiersAdapter) rv.getAdapter();
            ba.setNewBiere(getBiersFromFile());
            Toast.makeText(getApplicationContext(),"Download",Toast.LENGTH_LONG).show();
        }
    }
}
