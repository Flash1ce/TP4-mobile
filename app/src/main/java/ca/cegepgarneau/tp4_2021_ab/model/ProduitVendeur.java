package ca.cegepgarneau.tp4_2021_ab.model;

import ca.cegepgarneau.tp4_2021_ab.utils.Categorie;

/**
 * Produit pour la liste vendeur.
 */
public class ProduitVendeur {
    private String nom;
    private String categorie;
    private double prix;
    private String id;

    /**
     * Constructeur vide.
     */
    public ProduitVendeur() {}

    /**
     * Permet de créer un produit pour la liste vendeur.
     *
     * @param pNom       Nom du produit
     * @param pCategorie Catégorie du produit
     * @param pPrix      Prix du produit
     */
    public ProduitVendeur(String pNom, String pCategorie, double pPrix) {
        this.nom = pNom;
        this.categorie = pCategorie;
        this.prix = pPrix;
    }

    /**
     * Permet d'obtenir l'id
     * @return
     */
    public String getId() {
        return this.id;
    }

    /**
     * Permet de set l'id.
     * @param pId
     */
    public void setId(String pId) {
        this.id = pId;
    }

    /**
     * Permet d'obtenir le nom du produit.
     *
     * @return Nom du produit
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Permet de set le nom du produit.
     *
     * @param pNom Nom du produit
     */
    public void setNom(String pNom) {
        this.nom = pNom;
    }

    /**
     * Permet d'obtenir la catégorie du produit
     *
     * @return La catégorie
     */
    public Categorie getCategorie() {
        if (categorie.equals(Categorie.PROTEINE.toString())) return Categorie.PROTEINE;
        else if (categorie.equals(Categorie.BCAA.toString())) return Categorie.BCAA;
        else return Categorie.CREATINE;
    }

    /**
     * Permet de set la catégorie du produit.
     *
     * @param pCategorie La catégorie
     */
    public void setCategorie(String pCategorie) {
        this.categorie = pCategorie;
    }

    /**
     * Permet d'obtenir le prix du produit.
     *
     * @return Le prix
     */
    public double getPrix() {
        return this.prix;
    }

    /**
     * Permet de set le prix du produit.
     *
     * @param pPrix Le prix
     */
    public void setPrix(double pPrix) {
        this.prix = pPrix;
    }
}
