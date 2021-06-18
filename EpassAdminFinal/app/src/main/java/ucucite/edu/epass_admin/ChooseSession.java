package ucucite.edu.epass_admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ChooseSession extends AppCompatActivity {

    ImageButton button2;
    ImageButton button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_session);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        getSupportActionBar().setTitle("Admin Choose Session " .toString());

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoadminhome();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showusers();
            }
        });
    }

    private void showusers() {
        Intent intent = new Intent(this, UsersMainActivity.class);
        startActivity(intent);
    }

    private void gotoadminhome() {
        Intent intent = new Intent(this, AdminMainActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to Log-Out?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        overridePendingTransition(R.anim.nothing, R.anim.nothing);
                        Intent intent = new Intent(ChooseSession.this,Login.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}