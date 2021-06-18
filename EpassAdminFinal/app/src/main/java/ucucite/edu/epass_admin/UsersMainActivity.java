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

public class    UsersMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private UsersAdapter usersAdapter;
    private List<Userstravellers> travellerslist;
    UsersApiInterface usersApiInterface;
    UsersAdapter.RecyclerViewClickListener listener;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_main);
        getSupportActionBar().setTitle("Account List" .toString());
        usersApiInterface = UsersApiClient.getApiClient().create(UsersApiInterface.class);

        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        listener = new UsersAdapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, final int position) {

                Intent intent = new Intent(UsersMainActivity.this, UsersEditorActivity.class);
                intent.putExtra("id", travellerslist.get(position).getId());
                intent.putExtra("name", travellerslist.get(position).getName());
                intent.putExtra("species", travellerslist.get(position).getSpecies());
                intent.putExtra("breed", travellerslist.get(position).getBreed());
                intent.putExtra("picture", travellerslist.get(position).getPicture());
                intent.putExtra("birth", travellerslist.get(position).getBirth());


                startActivity(intent);

            }


        };

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UsersMainActivity.this, UsersEditorActivity.class));
            }
        });

    }

    //code for search

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

     /*   SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName())
        );
        searchView.setQueryHint("Search Traveller Data...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                usersAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                usersAdapter.getFilter().filter(newText);
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);
*/
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
                            Intent intent = new Intent(UsersMainActivity.this,Login.class);
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

        Call<List<Userstravellers>> call = usersApiInterface.getPersons();
        call.enqueue(new Callback<List<Userstravellers>>() {
            @Override
            public void onResponse(Call<List<Userstravellers>> call, Response<List<Userstravellers>> response) {
                progressBar.setVisibility(View.GONE);
                travellerslist = response.body();
                Log.i(UsersMainActivity.class.getSimpleName(), response.body().toString());
                usersAdapter = new UsersAdapter(travellerslist, UsersMainActivity.this, listener);
                recyclerView.setAdapter(usersAdapter);
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Userstravellers>> call, Throwable t) {
                Toast.makeText(UsersMainActivity.this, "rp :"+
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
        Intent intent = new Intent(UsersMainActivity.this,ChooseSession.class);
        startActivity(intent);
    }

}

