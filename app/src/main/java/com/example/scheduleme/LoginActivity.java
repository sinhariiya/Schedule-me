package com.example.scheduleme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import me.ibrahimsn.lib.CirclesLoadingView;

public class LoginActivity extends AppCompatActivity {
    EditText inputEmail,inputPass;
    TextView notRegisteredClick;
    Button loginBtn;
    CirclesLoadingView circlesLoadingView;
    String mEmail,mPassword;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initTasks();


       /* Checking if user if loggedin or not. if loggedin than go to MainActivity */
        if (mAuth.getCurrentUser()!=null){
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }


         /*Listener on login button */
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circlesLoadingView.setVisibility(CirclesLoadingView.VISIBLE);
                checkValidationOfData();
            }
        });

        /* Another listner to goto Signup activity */
        notRegisteredClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,SignUp.class);
                startActivity(intent);
            }
        });
    }

    public void initTasks(){

        inputEmail=(EditText)findViewById(R.id.editTextEmail);
        inputPass=(EditText)findViewById(R.id.editTextPass);
        inputPass.setTransformationMethod(new PasswordTransformationMethod());
        mAuth=FirebaseAuth.getInstance();
        loginBtn=(Button)findViewById(R.id.loginButton);
        notRegisteredClick=(TextView)findViewById(R.id.notRegistered);
        circlesLoadingView=(CirclesLoadingView) findViewById(R.id.progressBar);
        circlesLoadingView.setVisibility(CirclesLoadingView.GONE);

    }

    public void checkValidationOfData(){
        mEmail=inputEmail.getText().toString();
        mPassword=inputPass.getText().toString();
        if(mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
            inputEmail.setError("Enter a valid email");
            circlesLoadingView.setVisibility(CirclesLoadingView.GONE);
        }
        else if(mPassword.length()>16){
            inputPass.setError("Password should not be more than 16");
            circlesLoadingView.setVisibility(CirclesLoadingView.GONE);

        }
        else if(mPassword.isEmpty()){
            inputPass.setError("Please enter password");
            circlesLoadingView.setVisibility(CirclesLoadingView.GONE);
        }

        else {
            loginUser();
        }

    }
        /* If validation is checked than this method will be invoked */
    private void loginUser() {

        mAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    circlesLoadingView.setVisibility(CirclesLoadingView.GONE);
                    Toast.makeText(LoginActivity.this,"Login Successfull",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    circlesLoadingView.setVisibility(CirclesLoadingView.GONE);
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
