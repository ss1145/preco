package com.example.lenovo.preco;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Register extends AppCompatActivity {
    EditText editText, editText2, editText3, editText4, editText5, editText6;
    Button b1;
    DatePicker datePicker;
    String strDate;
    int year, month, day;
    Calendar calendar = Calendar.getInstance();
    private DbHelper db;
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                EditText editText6;
                int year, month, day;


                public void onDateSet(
                        DatePicker view, int y, int m, int d) {
                    year = y;
                    month = m;
                    day = d;
                   /* DatePickerDialog b =new DatePickerDialog(getApplicationContext(),this,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                    b.show();*/
                    // updateDisplay();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                    Date date = new Date(year, month, day);
                    strDate = dateFormat.format(date);
                    editText6 = findViewById(R.id.editText6);
                    editText6.setText(new StringBuilder()
                            // Month is 0 based so add 1
                            .append(d).append("/").append(m + 1).append("/").append(y).append(" "));
                    // Toast.makeText(getBaseContext(),"You have selected " + strDate,
                    //Toast.LENGTH_SHORT).show();
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new DbHelper(this);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        editText6 = findViewById(R.id.editText6);
        b1 = findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
            }
        });

    }

    private void register() {
        String email = editText.getText().toString();
        String pass = editText2.getText().toString();
        String cpass = editText3.getText().toString();
        String fname = editText4.getText().toString();
        String lname = editText5.getText().toString();
        String dob = editText6.getText().toString();
        if (email.isEmpty() && pass.isEmpty()) {
            displayToast("Username/password field empty");
        } else {
            db.addUser(email, pass, cpass, fname, lname, dob);
            displayToast("User registered");
            finish();
        }
    }

    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(
                this, mDateSetListener, year, month, day);
    }

    public void onClickDate(View view) {
        showDialog(1);
    }
   /* public class MyEditTextDatePicker  implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
        EditText _editText;
        private int _day;
        private int _month;
        private int _birthYear;
        private Context _context;

        public MyEditTextDatePicker(Context context, int editTextViewID)
        {
            Activity act = (Activity)context;
            this._editText = (EditText)act.findViewById(R.id.editText6);
            this._editText.setOnClickListener(this);
            this._context = context;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _birthYear = year;
            _month = monthOfYear;
            _day = dayOfMonth;
            updateDisplay();
        }
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

            DatePickerDialog dialog = new DatePickerDialog(_context, this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();

        }

        // updates the date in the birth date EditText
        private void updateDisplay() {

            _editText.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(_day).append("/").append(_month + 1).append("/").append(_birthYear).append(" "));
        }
    }*/


}
