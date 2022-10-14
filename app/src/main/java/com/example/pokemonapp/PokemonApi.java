package com.example.pokemonapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class PokemonApi {



        public ArrayList<Pokemon> getPokemons() {
            String url = "https://pokeapi.co/api/v2/pokemon/";
            String BASE_URL = "https://pokeapi.co/api/v2/";

            Uri.Builder pokemonUri = Uri.parse(BASE_URL).buildUpon().appendPath("pokemon");
            Uri.Builder habilidadesUri = Uri.parse(BASE_URL).buildUpon().appendPath("ability");
            try {

                String result = HttpUtils.get(url);

                JSONObject jsonResult = new JSONObject(result);
                JSONArray results = jsonResult.getJSONArray("results");

                ArrayList<Pokemon> pokemons = new ArrayList<>();

                for (int i = 0; i < results.length(); i++) {
                    JSONObject pokemonJson = results.getJSONObject(i);

                    Pokemon pokemon = new Pokemon();
                    pokemon.setName(pokemonJson.getString("name"));
                    pokemon.setDetailsUrl(pokemonJson.getString("url"));

                   String resultdetails = HttpUtils.get(pokemon.getDetailsUrl());
                    JSONObject jsondetails= new JSONObject(resultdetails);
                    JSONObject sprites = jsondetails.getJSONObject("sprites");

                    pokemon.setImage(sprites.getString("front_default"));

                    String resultDetails = HttpUtils.get(pokemon.getDetailsUrl());
                    JSONObject jsonDetails = new JSONObject(resultDetails);
                    pokemon.setHeight(jsonDetails.getInt("height"));
                    pokemons.add(pokemon);
                }
                return pokemons;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


