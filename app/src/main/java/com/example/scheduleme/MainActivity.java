package com.example.scheduleme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.mtp.MtpDeviceInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;

import me.ibrahimsn.lib.CirclesLoadingView;

public class MainActivity extends AppCompatActivity {

    EditText scheduleTitle,schedulePlace,scheduleDate,scheduleTime;
    TextView welcomeTitle;
    ImageView logoutBtn;
    Button submitData,viewSchedule;
    DatabaseReference mDatabaseReference;
    String mTitle,mPlace,mDate,mTime;
    ScheduleModel scheduleModel;
    FirebaseAuth mAuth;
    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
    CirclesLoadingView circlesLoadingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTasks();
        showDisplayName();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnectivity();

            }
        });

    }




        /* For showing the display name in MainActivity */
    public void showDisplayName(){
        if (mAuth.getCurrentUser()!=null)
        welcomeTitle.setText(user.getDisplayName());
    }

    public void initTasks(){
        scheduleTitle=(EditText)findViewById(R.id.scheduleTitle);
        schedulePlace=(EditText)findViewById(R.id.schedulePlace);
        scheduleDate=(EditText)findViewById(R.id.scheduleDate);
        scheduleTime=(EditText)findViewById(R.id.scheduleTime);
        welcomeTitle=(TextView)findViewById(R.id.welcomeTitle);
        logoutBtn=(ImageView)findViewById(R.id.logoutButton);
         circlesLoadingView=(CirclesLoadingView)findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();


        submitData=(Button)findViewById(R.id.submitSchedule);
        viewSchedule=(Button)findViewById(R.id.viewSchedule);
        mDatabaseReference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());/* Getting the database node */


    }

    public void checkDataValidation(View view){
                mTitle= StringUtils.capitalize(scheduleTitle.getText().toString());
                mPlace=StringUtils.capitalize(schedulePlace.getText().toString());
                mDate=StringUtils.capitalize(scheduleDate.getText().toString());
                mTime=StringUtils.capitalize(scheduleTime.getText().toString());


                if(mTitle.isEmpty()){
                    scheduleTitle.setError("Please provide schedule name");
                }

                else if(mDate.isEmpty()){
                    scheduleDate.setError("Please provide date");
                }
                else if(mTime.isEmpty()){
                    scheduleTime.setError("Pleaae provide time");
                }
                else{
                    if (mPlace.isEmpty()) {
                        mPlace = "Unknown";
                    }
                        submitData();
                }





    }
    /* Method will be invoked when validation is passed , it will store the schedule data into a newly generated key(Unique each time) under the unique id of the user*/
     public void submitData(){
           String key=mDatabaseReference.push().getKey();
          scheduleModel=new ScheduleModel(key,mTitle,mPlace,mDate,mTime);
          mDatabaseReference.child(key).setValue(scheduleModel).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                  if (task.isSuccessful()){
                      Toast.makeText(getApplicationContext(),"Submitted successfully",Toast.LENGTH_SHORT).show();
                  }
                  else{
                      Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_SHORT).show();
                  }
              }
          });
     }

     public void viewSchedules(View view){
         /*Setting editexts to null so that when user presses back from viewschedule Activty, the mainactivity editexts will
         not contain the previously submitted data
          */
        scheduleTitle.setText(null);
        schedulePlace.setText(null);
        scheduleDate.setText(null);
        scheduleTime.setText(null);
        Intent intent=new Intent(MainActivity.this,ViewSchedule.class);
        startActivity(intent);


     }
     /* This method checks the connectivty, user can only logout if internet is there */
    public void checkConnectivity(){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);
                if (connected){
                    circlesLoadingView.setVisibility(View.GONE);
                    mAuth.signOut();
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    circlesLoadingView.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }






}
