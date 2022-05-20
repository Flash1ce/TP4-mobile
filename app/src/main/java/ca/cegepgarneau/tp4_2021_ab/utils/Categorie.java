package ca.cegepgarneau.tp4_2021_ab.utils;

import java.util.ArrayList;
import java.util.List;

import ca.cegepgarneau.tp4_2021_ab.R;

/**
 * Énumération des catégories existentes.
 */
public enum Categorie {
    PROTEINE("Protéine"),
    BCAA("BCAA"),
    CREATINE("Créatine");

    private String strVal;

    Categorie(String toString) {
        strVal = toString;
    }

    @Override
    public String toString() {
        return strVal;
    }

    /**
     * Permet d'obtenir l'image en fonction de la catégorie.
     * @return return l'image. R.drawable (un int)
     */
    public int getImageCategorie(){
        switch (this) {
            case PROTEINE:
                return R.drawable.proteine;
            case BCAA:
                return R.drawable.bcaa;
            case CREATINE:
                return R.drawable.creatine;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

    /**
     * Permet d'obtenir la liste de tous les catégories en string.
     * @return retourne liste de catégories en string.
     */
    public static List<String> getAllCategories() {
        List<String> lstCat = new ArrayList<>();
        lstCat.add(Categorie.PROTEINE.toString());
        lstCat.add(Categorie.BCAA.toString());
        lstCat.add(Categorie.CREATINE.toString());
        return lstCat;
    }

    /**
     * Permet d'obtenir l'id de la catégorie dans le spinner.
     * @return return l'id sous forme de int.
     */
    public int getIdCategorieSpinner(){
        switch (this) {
            case PROTEINE:
                return 0;
            case BCAA:
                return 1;
            case CREATINE:
                return 2;
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
