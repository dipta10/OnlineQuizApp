package semester2_2project.onlinequizapp2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import semester2_2project.onlinequizapp2.BroadcastReceiver.AlarmReceiver;
import semester2_2project.onlinequizapp2.Model.User;
import semester2_2project.onlinequizapp2.common.Common;

public class MainActivity extends AppCompatActivity {
    MaterialEditText edtNewUser, edtNewPassword, edtNewEmail; // for sign up
    MaterialEditText edtUser, edtPassword; // for sign in

    Button btnSignUp, btnSignIn;
    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

//        registerAlarm();

        edtUser = findViewById(R.id.edtUser);
        edtPassword = findViewById(R.id.edtPassword);

        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(edtUser.getText().toString(), edtPassword.getText().toString());
            }
        });
    }

    private void signIn(final String user, final String pass) {

        if (!checkValidity(user, pass)) {
            return;
        }

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user).exists()) {
                    if (!user.isEmpty()) {
                        User login = dataSnapshot.child(user).getValue(User.class);
                        if (login.getPassword().equals(pass)) {
//                             Toast.makeText(MainActivity.this, "Login Done!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = login;
                            startActivity(i);
                            finish();
                        } else if (login.getPassword().length() == 0) {
                            Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Wrong pass", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter you user name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog() {


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout, null);
        sign_up_layout.setElevation(20);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        edtNewUser = sign_up_layout.findViewById(R.id.edtNewUserName);
        edtNewEmail = sign_up_layout.findViewById(R.id.edtNewEmail);
        edtNewPassword = sign_up_layout.findViewById(R.id.edtNewPassword);


        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("Create Account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });

//        alertDialog.show();

        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtNewUser.getText().toString();
                String password = edtNewPassword.getText().toString();
                String email = edtNewEmail.getText().toString();


                if (!checkValidity(username, password, email)) {
                    return;
                }


                final User user = new User(edtNewUser.getText().toString(),
                        edtNewPassword.getText().toString(),
                        edtNewEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUserName()).exists()) {
                            Toast.makeText(MainActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            users.child(user.getUserName())
                                    .setValue(user);

                            Toast.makeText(MainActivity.this, "User registration done", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        dialog.setCancelable(false);

    }

    private void registerAlarm() {


        String x = database.getReference("time").child("hour").toString();

        Toast.makeText(this, "time: " + x, Toast.LENGTH_SHORT).show();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 7);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

//        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    boolean checkValidity(String username, String password) {
        if (username.isEmpty()) {
            Toast.makeText(MainActivity.this, "Username can not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

//                if (username.trim().length() != username.length()) {
//                    Toast.makeText(MainActivity.this, "Username can not have white spaces", Toast.LENGTH_SHORT).show();
//                    return false;
//                }

        if (username.contains(" ")) {
            Toast.makeText(MainActivity.this, "Username can not contain spaces", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty()) {
            Toast.makeText(MainActivity.this, "password can not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        boolean onlySpace = true;
        for (int i = 0; i < password.length(); ++i) {
            if (password.charAt(i) != ' ') {
                onlySpace = false;
                break;
            }
        }
        if (onlySpace) {
            Toast.makeText(MainActivity.this, "Password can not contain only spaces", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    boolean checkValidity(String username, String password, String email) {
        if (!checkValidity(username, password)) return false;

        if (email.isEmpty()) {
            Toast.makeText(MainActivity.this, "Email can not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.contains(" ")) {
            Toast.makeText(MainActivity.this, "Email can not contain spaces", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}















