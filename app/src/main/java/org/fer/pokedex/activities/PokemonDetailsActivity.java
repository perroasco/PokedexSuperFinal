package org.fer.pokedex.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.fer.pokedex.R;
import org.fer.pokedex.adapters.RowTypesAdapter;
import org.fer.pokedex.database.AppDatabase;
import org.fer.pokedex.entities.Pokemon;
import org.fer.pokedex.entities.PokemonDetails;
import org.fer.pokedex.interfaces.AsyncTaskHandler;
import org.fer.pokedex.network.PokemonDetailsAsyncTask;

import java.util.Arrays;

public class PokemonDetailsActivity extends AppCompatActivity implements AsyncTaskHandler {

    ImageView image, favorite;
    TextView name, types, weight, experience, id;
    RecyclerView rvDetailsTypes;

    AppDatabase database;

    // Pokemon info
    String url;
    String pokemonName;
    Pokemon favoritePokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);

        image = findViewById(R.id.image);
        favorite = findViewById(R.id.favorite);
        name = findViewById(R.id.name);
        types = findViewById(R.id.detatils_type);
        weight = findViewById(R.id.weight);
        experience = findViewById(R.id.experience);
        id = findViewById(R.id.id);
        rvDetailsTypes = findViewById(R.id.rv_details_types);

        url = getIntent().getStringExtra("URL");

        PokemonDetailsAsyncTask pokemonDetailsAsyncTask = new PokemonDetailsAsyncTask();
        pokemonDetailsAsyncTask.handler = this;
        pokemonDetailsAsyncTask.execute(url);

        database = AppDatabase.getDatabase(this);
    }

    @Override
    public void onTaskEnd(Object result) {
        PokemonDetails details = (PokemonDetails) result;
        pokemonName = details.getName();
        Glide.with(this).load(details.getImage()).into(image);
        name.setText(details.getName());
        weight.setText("Weight: " + details.getWeight());
        experience.setText("Base EXP: "  + details.getBaseExperience());
        id.setText("ID: " + details.getId());

        rvDetailsTypes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvDetailsTypes.setAdapter(new RowTypesAdapter(this, Arrays.asList(details.getTypes())));

        favoritePokemon = database.pokemonDao().findByName(details.getName());

        if (favoritePokemon != null) {
            Glide.with(this).load(R.drawable.favorite).into(favorite);
        }
    }

    public void onClickType(View view) {
            Intent intent = new Intent(this, PokemonTypeActivity.class);
            startActivity(intent);
    }

    public void onClickFavorite(View view) {
        if(favoritePokemon != null) {
            showAlert(this);
        }
        else {
            Pokemon pokemon = new Pokemon(pokemonName, url);
            database.pokemonDao().insertAll(pokemon);
            Glide.with(this).load(R.drawable.favorite).into(favorite);
        }
    }

    private void showAlert(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure?");

        // Add the buttons
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                database.pokemonDao().delete(favoritePokemon);
                favoritePokemon = null;
                Glide.with(context).load(R.drawable.favorite_empty).into(favorite);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // Show
        dialog.show();
    }
}
