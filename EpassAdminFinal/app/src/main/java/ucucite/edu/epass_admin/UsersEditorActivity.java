package ucucite.edu.epass_admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersEditorActivity extends AppCompatActivity {


    private EditText mName, mUsername, mPassword, mCpassword;
    private CircleImageView mPicture;
    private FloatingActionButton mFabChoosePic;


    private String name, username, password, picture, cpassword, etd;
    private int id;

    private Menu action;
    private Bitmap bitmap;

    private UsersApiInterface usersApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_editor);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mName = findViewById(R.id.name);
        mUsername = findViewById(R.id.uname);
        mPassword = findViewById(R.id.Password);
        mCpassword = findViewById(R.id.confirmpassword);
        mPicture = findViewById(R.id.picture);

        mFabChoosePic = findViewById(R.id.fabChoosePic);



        mFabChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });



        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        name = intent.getStringExtra("name");
        username = intent.getStringExtra("species");
        password = intent.getStringExtra("breed");
        cpassword = intent.getStringExtra("birth");
        picture = intent.getStringExtra("picture");



        setDataFromIntentExtra();

    }

    private void setDataFromIntentExtra() {

        if (id != 0) {

            readMode();
            getSupportActionBar().setTitle("Info " + name.toString());

            mName.setText(name);
            mUsername.setText(username);
            mPassword.setText(password);
            mCpassword.setText(cpassword);


            RequestOptions requestOptions = new RequestOptions();
            requestOptions.skipMemoryCache(true);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.placeholder(R.drawable.logo);
            requestOptions.error(R.drawable.logo);

            Glide.with(UsersEditorActivity.this)
                    .load(picture)
                    .apply(requestOptions)
                    .into(mPicture);


        } else {
            getSupportActionBar().setTitle("Add Travellers");
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);

        if (id == 0){

            action.findItem(R.id.menu_edit).setVisible(false);
            action.findItem(R.id.menu_delete).setVisible(false);
            action.findItem(R.id.menu_save).setVisible(true);

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();

                return true;
            case R.id.menu_edit:
                //Edit

                editMode();
                getSupportActionBar().setTitle("Edit " + name.toString());

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mName, InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_delete).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);

                return true;
            case R.id.menu_save:
                //Save

                //validation for password matching
                if(!mPassword.getText().toString().equals(mCpassword.getText().toString())) {

                    Toast.makeText(this, "Password don't Match!", Toast.LENGTH_SHORT).show();

                }

                else if (id == 0) {

                    if (TextUtils.isEmpty(mName.getText().toString()) ||
                            TextUtils.isEmpty(mUsername.getText().toString()) ||
                            TextUtils.isEmpty(mPassword.getText().toString()) ||
                            TextUtils.isEmpty(mCpassword.getText().toString())  ){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setMessage("Please complete the field!");
                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }

                    else {

                        postData("insert");
                        action.findItem(R.id.menu_edit).setVisible(true);
                        action.findItem(R.id.menu_save).setVisible(false);
                        action.findItem(R.id.menu_delete).setVisible(true);

                        readMode();

                    }

                } else {

                    updateData("update", id);
                    action.findItem(R.id.menu_edit).setVisible(true);
                    action.findItem(R.id.menu_save).setVisible(false);
                    action.findItem(R.id.menu_delete).setVisible(true);

                    readMode();
                }

                return true;
            case R.id.menu_delete:

                AlertDialog.Builder dialog = new AlertDialog.Builder(UsersEditorActivity.this);
                dialog.setMessage("Delete this data?");
                dialog.setPositiveButton("Yes" ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteData("delete", id, picture);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                mPicture.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //inserting data

    private void postData(final String key) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        readMode();

        String name = mName.getText().toString().trim();
        String species = mUsername.getText().toString().trim();
        String breed = mPassword.getText().toString().trim();
        String birth = mCpassword.getText().toString().trim();

        String picture = null;
        if (bitmap == null) {
            picture = "";
        } else {
            picture = getStringImage(bitmap);
        }

        usersApiInterface = UsersApiClient.getApiClient().create(UsersApiInterface.class);

        Call<Userstravellers> call = usersApiInterface.insertPersons(key, name, species, breed, birth, picture);

        call.enqueue(new Callback<Userstravellers>() {
            @Override
            public void onResponse(Call<Userstravellers> call, Response<Userstravellers> response) {

                progressDialog.dismiss();

                Log.i(UsersEditorActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")){
                    finish();
                } else {
                    Toast.makeText(UsersEditorActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Userstravellers> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UsersEditorActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    //code for updating data
    private void updateData(final String key, final int id) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        readMode();
        getSupportActionBar().setTitle("Info " + name.toString());

        String name = mName.getText().toString().trim();
        String species = mUsername.getText().toString().trim();
        String breed = mPassword.getText().toString().trim();
        String birth = mCpassword.getText().toString().trim();

        String picture = null;
        if (bitmap == null) {
            picture = "";
        } else {
            picture = getStringImage(bitmap);
        }

        usersApiInterface = UsersApiClient.getApiClient().create(UsersApiInterface.class);

        Call<Userstravellers> call = usersApiInterface.updatePersons(key, id,name, species, breed, birth, picture);

        call.enqueue(new Callback<Userstravellers>() {
            @Override
            public void onResponse(Call<Userstravellers> call, Response<Userstravellers> response) {

                progressDialog.dismiss();

                Log.i(UsersEditorActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")){
                    Toast.makeText(UsersEditorActivity.this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UsersEditorActivity.this, UsersMainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(UsersEditorActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Userstravellers> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UsersEditorActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Code for deleting data
    private void deleteData(final String key, final int id, final String pic) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();

        readMode();

        usersApiInterface = UsersApiClient.getApiClient().create(UsersApiInterface.class);

        Call<Userstravellers> call = usersApiInterface.deletePersons(key, id, pic);

        call.enqueue(new Callback<Userstravellers>() {
            @Override
            public void onResponse(Call<Userstravellers> call, Response<Userstravellers> response) {

                progressDialog.dismiss();

                Log.i(UsersEditorActivity.class.getSimpleName(), response.toString());

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")){
                    Toast.makeText(UsersEditorActivity.this, message, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(EditorActivity.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UsersEditorActivity.this, message, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(EditorActivity.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Userstravellers> call, Throwable t) {
                progressDialog.dismiss();
                //Toast.makeText(EditorActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(UsersEditorActivity.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(UsersEditorActivity.this, UsersMainActivity.class);
                startActivity(intent);
            }
        });

    }

    void readMode(){

        mName.setFocusableInTouchMode(false);
        mUsername.setFocusableInTouchMode(false);
        mPassword.setFocusableInTouchMode(false);

        mCpassword.setFocusableInTouchMode(false);
        mName.setFocusable(false);
        mUsername.setFocusable(false);
        mPassword.setFocusable(false);

        mCpassword.setFocusable(false);

        // mGenderSpinner.setEnabled(false);
        //mBirth.setEnabled(false);

        mFabChoosePic.setVisibility(View.INVISIBLE);

    }

    private void editMode(){

        mName.setFocusableInTouchMode(true);
        mUsername.setFocusableInTouchMode(true);
        mPassword.setFocusableInTouchMode(true);

        mCpassword.setFocusableInTouchMode(true);
        //    mGenderSpinner.setEnabled(true);
        // mplacetogo.setEnabled(true);

        mFabChoosePic.setVisibility(View.VISIBLE);
    }

}
