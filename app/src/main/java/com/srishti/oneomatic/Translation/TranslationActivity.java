package com.srishti.oneomatic.Translation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.srishti.oneomatic.Database.CheckInternet;
import com.srishti.oneomatic.R;
import com.srishti.oneomatic.Translation.TranslateFragment;

public class TranslationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        CheckInternet checkInternet=new CheckInternet();
        if(!checkInternet.isOnline(this)){
            showCustommDialog();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, TranslateFragment.newInstance())
                    .commitNow();

        }
    }
    private void showCustommDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builder.setTitle("Alert !");
        builder.setMessage("Please Connect to Internet to Proceed").setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(getApplicationContext(),"You may not be able to Translate if not connected to Internet ",Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {

    }

}