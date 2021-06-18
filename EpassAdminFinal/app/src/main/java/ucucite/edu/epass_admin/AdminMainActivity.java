package ucucite.edu.epass_admin;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdminAdapter adminAdapter;
    private List<Admintravellers> travellerslist;
    AdminApiInterface adminApiInterface;
    AdminAdapter.RecyclerViewClickListener listener;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        adminApiInterface = AdminApiClient.getApiClient().create(AdminApiInterface.class);

        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getSupportActionBar().setTitle("Travellers List " .toString());

        listener = new AdminAdapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, final int position) {

                Intent intent = new Intent(AdminMainActivity.this, AdminEditorActivity.class);
                intent.putExtra("id", travellerslist.get(position).getId());
                intent.putExtra("name", travellerslist.get(position).getName());
                intent.putExtra("species", travellerslist.get(position).getSpecies());
                intent.putExtra("breed", travellerslist.get(position).getBreed());
                intent.putExtra("picture", travellerslist.get(position).getPicture());
                intent.putExtra("birth", travellerslist.get(position).getBirth());
                intent.putExtra("gapo", travellerslist.get(position).getGapo());
                intent.putExtra("eta", travellerslist.get(position).getEta());

                startActivity(intent);

            }


        };

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMainActivity.this, AdminEditorActivity.class));
            }
        });

    }

    //code for search

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

      /*  SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName())
        );
        searchView.setQueryHint("Search Traveller Data...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                adminAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adminAdapter.getFilter().filter(newText);
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //Intent intent = new Intent(MainActivity.this,Login.class);
            //startActivity(intent);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to Log-Out?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            overridePendingTransition(R.anim.nothing, R.anim.nothing);
                            Intent intent = new Intent(AdminMainActivity.this,Login.class);
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

    public void getPets(){

        Call<List<Admintravellers>> call = adminApiInterface.getPersons();
        call.enqueue(new Callback<List<Admintravellers>>() {
            @Override
            public void onResponse(Call<List<Admintravellers>> call, Response<List<Admintravellers>> response) {
                progressBar.setVisibility(View.GONE);
                travellerslist = response.body();
                Log.i(AdminMainActivity.class.getSimpleName(), response.body().toString());
                adminAdapter = new AdminAdapter(travellerslist, AdminMainActivity.this, listener);
                recyclerView.setAdapter(adminAdapter);
                adminAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Admintravellers>> call, Throwable t) {
                Toast.makeText(AdminMainActivity.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getPets();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.nothing);
        Intent intent = new Intent(AdminMainActivity.this,ChooseSession.class);
        startActivity(intent);
    }

}

