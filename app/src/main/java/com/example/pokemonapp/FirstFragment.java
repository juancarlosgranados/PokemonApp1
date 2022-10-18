package com.example.pokemonapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.pokemonapp.databinding.FragmentFirstBinding;
import com.example.pokemonapp.databinding.LvPokemonRawBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private PokemonAdapter adapter;
    private ArrayList<Pokemon> items;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {


        items = new ArrayList<>();//ES DE POKEMONS
        adapter = new PokemonAdapter(
                getContext(),
                R.layout.lv_pokemon_raw,
                items
        );

        binding.lvPokemons.setAdapter(adapter);

        refresh();

        super.onViewCreated(view, savedInstanceState);
        PokemonViewModel viewModel = new ViewModelProvider(getActivity()).get(PokemonViewModel.class);
viewModel.getPokemones().observe(getActivity(),pokemons -> {
    adapter.clear();
    adapter.addAll(pokemons);
});

    }


    public void refresh() {
        Toast.makeText(getContext(), "Refrescando...", Toast.LENGTH_LONG).show();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
     String tipo= preferences.getString("Tipo", "");
     if(!tipo.equals("")){

     }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            PokemonApi api = new PokemonApi();
            ArrayList<Pokemon>  pokemons = api.getPokemons();

            handler.post(() -> {
                adapter.clear();
                adapter.addAll(pokemons);
            });
        });
    }


    // TACHADO -- DEPENDENCIAS
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.refresh){
            refresh();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    class PokemonAdapter extends ArrayAdapter<Pokemon> {
        public PokemonAdapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Obtenim l'objecte en la possició corresponent


            // INSTANCIAR POKEMON - EL POSITION

            Pokemon pokemon = getItem(position);
            //Pokemon pokemon = new Pokemon();
            Log.w("XXXX", pokemon.toString());

            LvPokemonRawBinding binding = null;
            // Mirem a veure si la View s'està reutilitzant, si no es així "inflem" la View
            // https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView#row-view-recycling
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.lv_pokemon_raw, parent, false);
            }

            // Unim el codi en les Views del Layout
            TextView txtpokemon = convertView.findViewById(R.id.txtpokemon);
            ImageView imgPokemon = convertView.findViewById(R.id.imgPokemon);

            // Fiquem les dades dels objectes (provinents del JSON) en el layout
            txtpokemon.setText(pokemon.getName());
           Glide.with(getContext()).load(pokemon.getImage()).into(imgPokemon);

            // Retornem la View replena per a mostrar-la

            return convertView;
        }
    }
}