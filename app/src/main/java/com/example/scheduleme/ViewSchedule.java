package com.example.scheduleme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.le.AdvertiseData;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import me.ibrahimsn.lib.CirclesLoadingView;

public class ViewSchedule extends AppCompatActivity {

    ListView listView;
    CirclesLoadingView circlesLoadingView;
    DatabaseReference databaseReference;
    List<ScheduleModel> scheduleList=new ArrayList<>();
    TextView noSchedule;
    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);
        initTasks();
    }

    public void initTasks(){
        listView=(ListView)findViewById(R.id.listView);
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        noSchedule=(TextView)findViewById(R.id.noSchedule);
        circlesLoadingView=(CirclesLoadingView)findViewById(R.id.progressBar);

    }



    @Override
    protected void onStart() {
        super.onStart();
       /* Adding value event listener to get the values childrens of root Users

        */
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                scheduleList.clear(); //Clearing the list so that the arrayAdapter gets fresh children nodes each time
                 /* Looping through the childrens*/
                for(DataSnapshot scheduleSnapshot : dataSnapshot.getChildren()){
                    ScheduleModel scheduleModel=scheduleSnapshot.getValue(ScheduleModel.class);
                    scheduleList.add(scheduleModel);
                }

                ScheduleListAdapter scheduleListAdapter=new ScheduleListAdapter(ViewSchedule.this,scheduleList);
                listView.setAdapter(scheduleListAdapter);
                if (!scheduleListAdapter.isEmpty()){
                    noSchedule.setVisibility(View.GONE);
                }
                else {
                    noSchedule.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        checkConnectivity();


    }
    public void checkConnectivity(){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
       connectedRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               boolean connected = dataSnapshot.getValue(Boolean.class);
               if (connected){
                   listView.setVisibility(View.VISIBLE);
                   circlesLoadingView.setVisibility(View.GONE);
               }
               else {
                   listView.setVisibility(View.GONE);
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
