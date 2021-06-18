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

    public class AdminEditorActivity extends AppCompatActivity {


    private EditText mName, mHqpno, mBarangay, mplacetogo, metd, meta;
    private CircleImageView mPicture;
    private FloatingActionButton mFabChoosePic;


        private String name, hqpno, barangay, picture, placetogo, etd, eta;
        private int id;

        private Menu action;
        private Bitmap bitmap;

        private AdminApiInterface adminApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_editor);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mName = findViewById(R.id.name);
        mHqpno = findViewById(R.id.username);
        mBarangay = findViewById(R.id.p_password);
        mplacetogo = findViewById(R.id.p_cpassword);
        mPicture = findViewById(R.id.picture);
        metd = findViewById(R.id.gapo);
        meta = findViewById(R.id.eta);
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
        hqpno = intent.getStringExtra("species");
        barangay = intent.getStringExtra("breed");
        placetogo = intent.getStringExtra("birth");
        picture = intent.getStringExtra("picture");
        etd = intent.getStringExtra("gapo");
        eta = intent.getStringExtra("eta");


        setDataFromIntentExtra();

    }

        private void setDataFromIntentExtra() {

            if (id != 0) {

                readMode();
                getSupportActionBar().setTitle("Info " + name.toString());

                mName.setText(name);
                mHqpno.setText(hqpno);
                mBarangay.setText(barangay);
                mplacetogo.setText(placetogo);
                metd.setText(etd);
                meta.setText(eta);

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.skipMemoryCache(true);
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                requestOptions.placeholder(R.drawable.logo);
                requestOptions.error(R.drawable.logo);

                Glide.with(AdminEditorActivity.this)
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

                    if (id == 0) {

                        if (TextUtils.isEmpty(mName.getText().toString()) ||
                                TextUtils.isEmpty(mHqpno.getText().toString()) ||
                                TextUtils.isEmpty(mBarangay.getText().toString()) ||
                                TextUtils.isEmpty(mplacetogo.getText().toString()) ||
                                TextUtils.isEmpty(metd.getText().toString()) ||
                                TextUtils.isEmpty(meta.getText().toString()) ){
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

                    AlertDialog.Builder dialog = new AlertDialog.Builder(AdminEditorActivity.this);
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
            String species = mHqpno.getText().toString().trim();
            String breed = mBarangay.getText().toString().trim();

            String birth = mplacetogo.getText().toString().trim();
            String gapo = metd.getText().toString().trim();
            String eta = meta.getText().toString().trim();
            String picture = null;
            if (bitmap == null) {
                picture = "";
            } else {
                picture = getStringImage(bitmap);
            }

            adminApiInterface = AdminApiClient.getApiClient().create(AdminApiInterface.class);

            Call<Admintravellers> call = adminApiInterface.insertPersons(key, name, species, breed, birth, picture, gapo, eta);

            call.enqueue(new Callback<Admintravellers>() {
                @Override
                public void onResponse(Call<Admintravellers> call, Response<Admintravellers> response) {

                    progressDialog.dismiss();

                    Log.i(AdminEditorActivity.class.getSimpleName(), response.toString());

                    String value = response.body().getValue();
                    String message = response.body().getMassage();

                    if (value.equals("1")){
                        finish();
                    } else {
                        Toast.makeText(AdminEditorActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Admintravellers> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(AdminEditorActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
            String species = mHqpno.getText().toString().trim();
            String breed = mBarangay.getText().toString().trim();

            String birth = mplacetogo.getText().toString().trim();
            String gapo = metd.getText().toString().trim();
            String eta = meta.getText().toString().trim();
            String picture = null;
            if (bitmap == null) {
                picture = "";
            } else {
                picture = getStringImage(bitmap);
            }

            adminApiInterface = AdminApiClient.getApiClient().create(AdminApiInterface.class);

            Call<Admintravellers> call = adminApiInterface.updatePersons(key, id,name, species, breed, birth, picture, gapo, eta);

            call.enqueue(new Callback<Admintravellers>() {
                @Override
                public void onResponse(Call<Admintravellers> call, Response<Admintravellers> response) {

                    progressDialog.dismiss();

                    Log.i(AdminEditorActivity.class.getSimpleName(), response.toString());

                    String value = response.body().getValue();
                    String message = response.body().getMassage();

                    if (value.equals("1")){
                        Toast.makeText(AdminEditorActivity.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminEditorActivity.this, AdminMainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AdminEditorActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Admintravellers> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(AdminEditorActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        //Code for deleting data
        private void deleteData(final String key, final int id, final String pic) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Deleting...");
            progressDialog.show();

            readMode();

            adminApiInterface = AdminApiClient.getApiClient().create(AdminApiInterface.class);

            Call<Admintravellers> call = adminApiInterface.deletePersons(key, id, pic);

            call.enqueue(new Callback<Admintravellers>() {
                @Override
                public void onResponse(Call<Admintravellers> call, Response<Admintravellers> response) {

                    progressDialog.dismiss();

                    Log.i(AdminEditorActivity.class.getSimpleName(), response.toString());

                    String value = response.body().getValue();
                    String message = response.body().getMassage();

                    if (value.equals("1")){
                        Toast.makeText(AdminEditorActivity.this, message, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(EditorActivity.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AdminEditorActivity.this, message, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(EditorActivity.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Admintravellers> call, Throwable t) {
                    progressDialog.dismiss();
                    //Toast.makeText(EditorActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(AdminEditorActivity.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AdminEditorActivity.this, AdminMainActivity.class);
                    startActivity(intent);
                }
            });

        }

        void readMode(){

            mName.setFocusableInTouchMode(false);
            mHqpno.setFocusableInTouchMode(false);
            mBarangay.setFocusableInTouchMode(false);
            metd.setFocusableInTouchMode(false);
            mplacetogo.setFocusableInTouchMode(false);
            meta.setFocusableInTouchMode(false);
            mName.setFocusable(false);
            mHqpno.setFocusable(false);
            mBarangay.setFocusable(false);
            metd.setFocusable(false);
            mplacetogo.setFocusable(false);
            meta.setFocusable(false);

           // mGenderSpinner.setEnabled(false);
            //mBirth.setEnabled(false);

            mFabChoosePic.setVisibility(View.INVISIBLE);

        }

        private void editMode(){

            mName.setFocusableInTouchMode(true);
            mHqpno.setFocusableInTouchMode(false);
            mBarangay.setFocusableInTouchMode(true);
            metd.setFocusableInTouchMode(true);
            mplacetogo.setFocusableInTouchMode(true);
            meta.setFocusableInTouchMode(true);
        //    mGenderSpinner.setEnabled(true);
           // mplacetogo.setEnabled(true);

            mFabChoosePic.setVisibility(View.VISIBLE);
        }

    }
