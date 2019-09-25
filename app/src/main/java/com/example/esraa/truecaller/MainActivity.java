package com.example.esraa.truecaller;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //callHistory
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CALL_LOG)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
            }
        } else {
            TextView textView = (TextView) findViewById(R.id.text);
            textView.setText(GetCallDetails());
        }

        //truecaller





        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WAKE_LOCK

                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}


            @Override public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

    }




    //callhistory
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(this, "permission granted !", Toast.LENGTH_SHORT).show();
                        TextView textView = (TextView) findViewById(R.id.text);
                        textView.setText(GetCallDetails());


                    }
                }else {
                    Toast.makeText(this, " no permission granted !", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private String GetCallDetails()
    {
        StringBuffer sb = new StringBuffer();
        Cursor manegedcursor= getContentResolver().query(CallLog.Calls.CONTENT_URI , null ,
                null , null , CallLog.Calls._ID + " DESC");

        int number = manegedcursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = manegedcursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = manegedcursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = manegedcursor.getColumnIndex(CallLog.Calls.DURATION);

        sb.append("call details : \n\n");
        while (manegedcursor.moveToNext())
        {
            String phnumber = manegedcursor.getString(number);

            String calltype = manegedcursor.getString(type);
            String calldate = manegedcursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(calldate));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy  HH:mm") ;
            String dateString = formatter.format(callDayTime);
            String callDuratin = manegedcursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(calltype);
            switch (dircode)
            {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "Missed";
                    break;
            }
            sb.append("\n phone number : " + phnumber + "\n call type : " + dir + "\n call date : " + dateString +
                    "\n call duration in sec : " + callDuratin);
            sb.append("\n------------------------------------------");
        }
        manegedcursor.close();
        return sb.toString();

    }





    //truecaller

    public void onclick(View view) {
        context=this;
        ExitActivity.exitApplication(context);
    }

}
