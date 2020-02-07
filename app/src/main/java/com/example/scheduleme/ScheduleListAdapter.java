package com.example.scheduleme;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ScheduleListAdapter extends ArrayAdapter<ScheduleModel> {

    Activity context;
    List<ScheduleModel> scheduleModelList;
    ViewSchedule viewSchedule=new ViewSchedule();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mDatabaseReference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

    public ScheduleListAdapter(Activity context,List<ScheduleModel> scheduleModelList){
        super(context,R.layout.schedule_list,scheduleModelList);
        this.context=context;
        this.scheduleModelList=scheduleModelList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=context.getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.schedule_list,null);
        Button editBtn=(Button)view.findViewById(R.id.editSchedule);
        TextView sTitle=(TextView)view.findViewById(R.id.s_title);
        TextView sPlace=(TextView)view.findViewById(R.id.s_place);
        TextView sDate=(TextView)view.findViewById(R.id.s_date);
        TextView sTime=(TextView)view.findViewById(R.id.s_time);
        ImageView deleteView=(ImageView)view.findViewById(R.id.deleteSchedule);
        final ScheduleModel scheduleModel=scheduleModelList.get(position);
        sTitle.setText(StringUtils.capitalize(scheduleModel.getScheduleTitle()));
        sPlace.setText(StringUtils.capitalize(scheduleModel.getSchedulePlace()));
        sDate.setText(scheduleModel.scheduleDate);
        sTime.setText(StringUtils.capitalize(scheduleModel.getScheduleTime()));

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 showUpdateDialog(scheduleModel.getScheduleId(),scheduleModel.getScheduleTitle(),scheduleModel.getSchedulePlace(),scheduleModel.getScheduleDate(),scheduleModel.getScheduleTime());
            }
        });

        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseReference.child(scheduleModel.scheduleId).removeValue();
            }
        });
        return view;
    }
    public void showUpdateDialog(final String sid, final String sName, final String sPlace, final String sDate, final String sTime){
        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(context);
        LayoutInflater layoutInflater=context.getLayoutInflater();
        View dialogView=layoutInflater.inflate(R.layout.update_dialog,null);
        dialogBuilder.setView(dialogView);
        TextView updateTitleText=(TextView)dialogView.findViewById(R.id.updateTitleText);

        final EditText updateTitle=(EditText)dialogView.findViewById(R.id.updateTitle);
        final EditText updatePLace=(EditText)dialogView.findViewById(R.id.updatePlace);
        final EditText updateDate=(EditText) dialogView.findViewById(R.id.updateDate);
        final EditText updateTime=(EditText)dialogView.findViewById(R.id.updateTime);


        Button updateBtn=(Button)dialogView.findViewById(R.id.updateBtn);

        updateTitleText.setText("Edit "+sName);
        updateTitle.setText(sName);
        updatePLace.setText(sPlace);
        updateDate.setText(sDate);
        updateTime.setText(sTime);


        final AlertDialog alertDialog=dialogBuilder.show();
        alertDialog.show();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String updatedTitle,updatedPlace,updatedDate,updatedTime;
                 updatedTitle=StringUtils.capitalize(updateTitle.getText().toString());
                 updatedPlace=StringUtils.capitalize(updatePLace.getText().toString());
                 updatedDate=updateDate.getText().toString();
                 updatedTime=updateTime.getText().toString();



                ScheduleModel updatedModel=new ScheduleModel(sid,updatedTitle,updatedPlace,updatedDate,updatedTime);
                mDatabaseReference.child(sid).setValue(updatedModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context,"Schedules updated",Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }
                        else {
                            Toast.makeText(context,"Error Encountered",Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }
                    }
                });

            }
        });


    }
}
