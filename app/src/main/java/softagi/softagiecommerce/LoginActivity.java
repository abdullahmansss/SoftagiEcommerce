package softagi.softagiecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    private EditText emailField,passwrodField;
    private String email,password;

    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initFB();
        initViews();
        initDialog();
    }

    private void initDialog()
    {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("please wait ...");
        progressDialog.setCancelable(false);
    }

    private void initFB()
    {
        auth = FirebaseAuth.getInstance();
    }

    private void initViews()
    {
        emailField = findViewById(R.id.email_field);
        passwrodField = findViewById(R.id.password_field);
    }

    public void regisetr(View view)
    {
    }

    public void login(View view)
    {
        email = emailField.getText().toString();
        password = passwrodField.getText().toString();

        if (email.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "enter your email", Toast.LENGTH_SHORT).show();
            emailField.requestFocus();
            return;
        }

        if (password.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "enter your correct password", Toast.LENGTH_SHORT).show();
            passwrodField.requestFocus();
            return;
        }

        progressDialog.show();

        loginFB(email,password);
    }

    private void loginFB(String email, String password)
    {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        progressDialog.dismiss();

                        if (task.isSuccessful())
                        {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else
                            {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    }
                });
    }
}
