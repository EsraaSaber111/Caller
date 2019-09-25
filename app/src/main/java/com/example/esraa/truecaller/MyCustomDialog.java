package com.example.esraa.truecaller;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import static android.support.constraint.Constraints.TAG;


public class MyCustomDialog extends Activity
{
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    private String phone;
    private String name;

    float x1, x2;
    static final int MIN_DISTANCE = 150;
    TextView Cname, Cnumber;
    String ContactName = "";
    String ContactNumber = "";

    String id = "";
    long cid;
    ImageView Cphoto;
    public static Activity mActivity;
    RelativeLayout card;
    Animation rslide, lslide;
    private Window wind;

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {


            firebaseAuth=FirebaseAuth.getInstance();




        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.setFinishOnTouchOutside(true);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog);
            this.mActivity = this;
            initializeContent();
            lslide = AnimationUtils.loadAnimation(this, R.anim.lslide);
            rslide = AnimationUtils.loadAnimation(this, R.anim.rslide);
            ContactName = getIntent().getExtras().getString("contact_name");
            ContactNumber = getIntent().getExtras().getString("phone_no");
            id = getIntent().getExtras().getString("id");
            try {
                if (id != null) {
                    cid = Long.parseLong(id);
                    retrieveContactPhoto(cid);
                }

            } catch (NumberFormatException e) {
                System.out.println("nfe");
            }
          //  Toast.makeText(mActivity,ContactName , Toast.LENGTH_SHORT).show();
          //  Toast.makeText(mActivity, ContactNumber, Toast.LENGTH_SHORT).show();


            if(ContactName != null && !ContactName.isEmpty() && !ContactName.equals("null"))
          {
              //Toast.makeText(mActivity, "Done", Toast.LENGTH_SHORT).show();
              Cname.setText("" + ContactName + " is calling you");
              Cnumber.setText(ContactNumber);
          }

          else if(ContactName==null) {

              SearchData();
            }




        } catch (Exception e) {
            Log.d("Exception", e.toString());
            e.printStackTrace();
        }
        card = (RelativeLayout) findViewById(R.id.card);



    }

private void SearchData() {

    final String userpath="KvWemDXD1e1knkjwrEd2";
    firebaseFirestore = FirebaseFirestore.getInstance();

    DocumentReference user = firebaseFirestore.collection("Callers").document(userpath);
    user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                phone = doc.getString("phone");
                name = doc.getString("name");


                if (ContactName == null){
                    Cname.setText("Unknown number");
                    Cnumber.setText(ContactNumber);
                }


                if (phone.equals(ContactNumber)) {
                    Cname.setText("" + name + " is calling you");
                    Cnumber.setText(ContactNumber);
                }




            }
            else {
                Toast.makeText(MyCustomDialog.this,"error", Toast.LENGTH_SHORT).show();
            }
        }
    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });

//---------------------------------------------------------

  /* firebaseFirestore.collection("Callers").document(userpath).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                firebaseFirestore.collection("Callers").whereEqualTo("phone",ContactNumber).get().addOnCompleteListener(
                        new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(MyCustomDialog.this, phone, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );





            }
        }
    });



//---------------------------------------------------------
firebaseFirestore.collection("users")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });


    //--------------------------------------------------------------

    DocumentReference user = firebaseFirestore.collection("Callers").document(userpath);
    user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

            if (task.isSuccessful()) {
          Toast.makeText(MyCustomDialog.this, phone, Toast.LENGTH_SHORT).show();



                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                Toast.makeText(MyCustomDialog.this, fields.append("Name: ").append(doc.get("name")), Toast.LENGTH_SHORT).show();
                Toast.makeText(MyCustomDialog.this, fields.append("\ntype: ").append(doc.get("type")), Toast.LENGTH_SHORT).show();
                Toast.makeText(MyCustomDialog.this, fields.append("\nPhone: ").append(doc.get("phone")), Toast.LENGTH_SHORT).show();


            }
        }
    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
            */
}





    private void initializeContent()
    {
        Cname  = (TextView) findViewById(R.id.Cname);
        Cnumber  = (TextView) findViewById(R.id.Cnumber);
        Cphoto = (ImageView) findViewById(R.id.contactPhoto);
    }
    private void retrieveContactPhoto(long contactID) {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactID));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                Cphoto.setImageBitmap(photo);
            }

            if(inputStream != null)
                inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE)
                {

                    if (x2 > x1)
                    {
                        card.startAnimation(rslide);
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                MyCustomDialog.this.finish();
                                System.exit(0);
                                ExitActivity.exitApplication(getApplicationContext());
                            }
                        },500);


                    }


                    else
                    {
                        card.startAnimation(lslide);
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try {
                                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

                                    Method m1 = tm.getClass().getDeclaredMethod("getITelephony");
                                    m1.setAccessible(true);
                                    Object iTelephony = m1.invoke(tm);

                                    Method m2 = iTelephony.getClass().getDeclaredMethod("silenceRinger");
                                    Method m3 = iTelephony.getClass().getDeclaredMethod("endCall");

                                    m2.invoke(iTelephony);
                                    m3.invoke(iTelephony);
                                }catch (Exception e){

                                }
                                MyCustomDialog.this.finish();
                                System.exit(0);
                                ExitActivity.exitApplication(getApplicationContext());
                            }
                        },500);
                    }

                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        wind = this.getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        wind.addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        wind.addFlags(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);

    }

}
