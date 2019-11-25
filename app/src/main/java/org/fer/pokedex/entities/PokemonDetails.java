package org.fer.pokedex.entities;

public class PokemonDetails {

    private String name;
    private int id;
    private int baseExperience;
    private int weight;
    private String[] types;
    private String image;

    public PokemonDetails(String name, int id, int baseExperience, int weight, String[] types) {
        this.name = name;
        this.id = id;
        this.baseExperience = baseExperience;
        this.weight = weight;
        this.types = types;

        String assetId = String.valueOf(id);

        if(assetId.length() == 1) {
            assetId = "00" + id;
        } else if (assetId.length() == 2) {
            assetId = "0" + id;
        }

        this.image = "https://assets.pokemon.com/assets/cms2/img/pokedex/full/" + assetId + ".png";
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getBaseExperience() {
        return baseExperience;
    }

    public int getWeight() {
        return weight;
    }

    public String[] getTypes() {
        return types;
    }

    public String getImage() {
        return image;
    }
}