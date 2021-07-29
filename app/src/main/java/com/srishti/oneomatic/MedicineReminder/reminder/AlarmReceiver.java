package com.srishti.oneomatic.MedicineReminder.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

            // Set the alarm here.
            Toast.makeText(context,"Time to take Medicine",Toast.LENGTH_SHORT).show();
            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if (uri==null){
                uri =RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            }
            Ringtone ringtone=RingtoneManager.getRingtone(context,uri);
            ringtone.play();


    }
}
