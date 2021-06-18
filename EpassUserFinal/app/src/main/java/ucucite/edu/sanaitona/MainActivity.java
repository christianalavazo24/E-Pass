package ucucite.edu.sanaitona;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView textViewdatashow;
    TextView textViewdatashow1;
    TextView textViewdatashow2;
    TextView textViewdatashow3;
    TextView textViewdatashow4;
    TextView textViewdatashow5;
    EditText editTextvalue;
    ImageView imageView;
    ImageButton buttonfetch;
    String url ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewdatashow = (TextView) findViewById(R.id.tvshowdata);
        textViewdatashow1 = (TextView) findViewById(R.id.tvshowdata1);
        textViewdatashow2 = (TextView) findViewById(R.id.tvshowdata2);
        textViewdatashow3 = (TextView) findViewById(R.id.tvshowdata3);
        textViewdatashow4 = (TextView) findViewById(R.id.tvshowdata4);
        textViewdatashow5 = (TextView) findViewById(R.id.tvshowdata5);
        editTextvalue = (EditText) findViewById(R.id.etvalue);
        imageView = (ImageView) findViewById(R.id.image);
        buttonfetch = (ImageButton) findViewById(R.id.buttonfetchdata);

        buttonfetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id  = editTextvalue.getText().toString();


                getData();
                url  = "https://electronic-pass.000webhostapp.com/epass/pets_picture/"+id+".jpeg";

            }
        });

    }

    private void getData() {


        String id = editTextvalue.getText().toString().trim();


        if (id.equals("")) {

            Toast.makeText(this, "Input HQPNO!", Toast.LENGTH_LONG).show();
            return;
        }

        String url = Config.DATA_URL + editTextvalue.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
                editTextvalue.setText("");
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        String name = "";
        String birth = "";
        String breed = "";
        String gapo = "";
        String species = "";
        String eta = "";


        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            name = collegeData.getString(Config.KEY_NAME);
            birth = collegeData.getString(Config.KEY_BIRTH);
            breed = collegeData.getString(Config.KEY_BREED);
            gapo = collegeData.getString(Config.KEY_GAPO);
            species = collegeData.getString(Config.KEY_SPECIES);
            eta = collegeData.getString(Config.KEY_ETA);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        textViewdatashow.setText("" + name);
        textViewdatashow1.setText("" + birth);
        textViewdatashow2.setText("" + breed);
        textViewdatashow3.setText("" + gapo);
        textViewdatashow4.setText("" + species);
        textViewdatashow5.setText("" + eta);
        ImageRetriveWithPicasso();
    }

    private void ImageRetriveWithPicasso() {


        Picasso.with(this)
                .load(url)
                .error(R.drawable.nodata)
                .placeholder(R.drawable.loadings)
                .into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.item1) {
            //Intent intent = new Intent(MainActivity.this,Login.class);
            //startActivity(intent);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to Log-Out?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            overridePendingTransition(R.anim.nothing, R.anim.nothing);
                            Intent intent = new Intent(MainActivity.this,Login.class);
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

            return true;
        }
        return super.onOptionsItemSelected(item);
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
                        Intent intent = new Intent(MainActivity.this,Login.class);
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