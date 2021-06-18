package ucucite.edu.sanaitona;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText email, password;
    private Button btn_login;
    TextView textView;

    private ProgressBar loading;
    private static String URL_LOGIN = "https://electronic-pass.000webhostapp.com/epass-user/loginuser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loading = findViewById(R.id.loading);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        //textView = findViewById(R.id.textView);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPass = password.getText().toString().trim();

                if (!mEmail.isEmpty() || !mPass.isEmpty()){
                    login(mEmail, mPass);
                } else{
                    email.setError("Please insert email");
                    password.setError("Please insert password");
                }

            }
        });


    }
    private void login(final String email, final String password){

        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.INVISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")){

                                for (int i = 0; i < jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String name = object.getString("name").trim();
                                    String email = object.getString("species").trim();




                                    Toast.makeText(Login.this,
                                            "Success Login. \nYour Name : "
                                                    +name+"\nYour Username : "
                                                    +email, Toast.LENGTH_SHORT)
                                            .show();

                                    Intent intent = new Intent(Login.this,MainActivity.class);
                                    startActivity(intent);





                                    loading.setVisibility(View.INVISIBLE);
                                    btn_login.setVisibility(View.VISIBLE);

                                }

                                //add to remove and show buttons
                            }else  {
                                loading.setVisibility(View.INVISIBLE);
                                btn_login.setVisibility(View.VISIBLE);
                                Toast.makeText(Login.this, "Email or password incorrect!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.INVISIBLE);
                            btn_login.setVisibility(View.VISIBLE);
                            // Toast.makeText(Login.this, "Error "+e.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(Login.this, "Email or password incorrect!", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.INVISIBLE);
                        btn_login.setVisibility(View.VISIBLE);
                        //Toast.makeText(Login.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(Login.this, "Please Connect to internet!", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map <String, String> params = new HashMap<>();
                params.put("species", email);
                params.put("breed", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        //    super.onBackPressed();
        //     System.exit(0);
        finishAffinity(); //Force Exit
    }
}
