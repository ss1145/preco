package com.example.lenovo.preco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    Button b1, b2, b3, b4;
    EditText editText, editText2;
    private DbHelper db;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DbHelper(this);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        session = new Session(this);
        if (session.loggedin()) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
            }
        });
        b3 = findViewById(R.id.b3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });
    }

    private void login() {
        String email = editText.getText().toString();
        String pass = editText2.getText().toString();

        if (db.getUser(email, pass)) {
            session.setLoggedin(true);
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Wrong email/password", Toast.LENGTH_SHORT).show();
        }
    }
}
