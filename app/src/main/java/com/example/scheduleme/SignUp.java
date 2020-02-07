package com.example.scheduleme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.StringUtils;

import me.ibrahimsn.lib.CirclesLoadingView;

public class SignUp extends AppCompatActivity {
    EditText inputName,inputEmail,inputPassword,inputConfirmPass;
    TextView gotoLogin;
    Button signup;
    CirclesLoadingView circlesLoadingView; //PROGRESS BAR DECLARATION
    String mName,mEmail,mPassword,mConfirmPassword;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        /*Calling method initTasks() to initialize the views , firebase objecrs etc... */
        initTasks();
        circlesLoadingView.setVisibility(View.GONE); // Setting the default visibilty of the progressbar to gone

        /* Button click listener for the signup button*/
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circlesLoadingView.setVisibility(View.VISIBLE); // Setting Pbar visible while clicked signup
                checkValidationOfData();// method foe checking validation of data
            }
        });

        /* Setting another event listener when user wants to go to Login Activity */
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUp.this, LoginActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    public void initTasks(){
        inputName=(EditText)findViewById(R.id.editTextName);
        inputEmail=(EditText)findViewById(R.id.editTextEmail);
        inputPassword=(EditText)findViewById(R.id.editTextPassword);
        inputPassword.setTransformationMethod(new PasswordTransformationMethod());
        inputConfirmPass=(EditText)findViewById(R.id.editTextConfirmPassword);
        inputConfirmPass.setTransformationMethod(new PasswordTransformationMethod());
        signup=(Button)findViewById(R.id.signupButton);
        mAuth=FirebaseAuth.getInstance();
        gotoLogin=(TextView)findViewById(R.id.gotoLogin);
        circlesLoadingView=(CirclesLoadingView)findViewById(R.id.progressBar);

    }

    public void checkValidationOfData(){
        mName= StringUtils.capitalize(inputName.getText().toString());
        mEmail=inputEmail.getText().toString();
        mPassword=inputPassword.getText().toString();
        mConfirmPassword=inputConfirmPass.getText().toString();
        if (mName.isEmpty()){
            inputName.setError("PLease enter your name");
            circlesLoadingView.setVisibility(View.GONE);
        }
        else if(mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
            inputEmail.setError("Enter a valid email");
            circlesLoadingView.setVisibility(View.GONE);
        }
        else if(mPassword.isEmpty() || mPassword.length()>16){
            inputPassword.setError("Password should not exceed 16 chars");
            circlesLoadingView.setVisibility(View.GONE);
        }

        else if(mConfirmPassword.isEmpty() || !mConfirmPassword.equals(mPassword)){
            inputConfirmPass.setError("Passwords don't match");
            circlesLoadingView.setVisibility(View.GONE);
        }
        else {
            registerUser();
        }

    }

    /* This method is called when validation is successful, it is saving the validated data to the firebase db */
    private void registerUser() {
            mAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();// getting the current user obj if signup is successfull

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(mName).build(); //Setting userprofilechangerequest so that we can set the display name


                        /* if displayname is set successfully we are creating a root node named "Users" in our db and also
                        creating a child node with the unique userID,each time a new user registers there will be a new child
                        with the unique id under the root node Users
                         */
                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    circlesLoadingView.setVisibility(View.GONE);
                                    databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                                    databaseReference.child("").setValue("");

                                    Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(SignUp.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

                    }
                    else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException){
                            inputEmail.setError("You are registered already");
                        }
                        else {
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }


}
