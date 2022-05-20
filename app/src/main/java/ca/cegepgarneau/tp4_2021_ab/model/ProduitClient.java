package ca.cegepgarneau.tp4_2021_ab.model;

import ca.cegepgarneau.tp4_2021_ab.utils.Categorie;

/**
 * Produit pour la liste client.
 */
public class ProduitClient {
    private String id;
    private String nom;
    private String categorie;
    private double prix;
    private int quantite;

    /**
     * Permet de créer un produit pour la liste client.
     *
     * @param pNom       Nom du produit
     * @param pCategorie Catégorie du produit
     * @param pPrix      Prix du produit
     */
    public ProduitClient(String pId, String pNom, Categorie pCategorie, double pPrix, int pQuantite) {
        this.id = pId;
        this.nom = pNom;
        this.categorie = pCategorie.toString();
        this.prix = pPrix;
        this.quantite = pQuantite;
    }

    /**
     * Permet d'obtenir l'id du produit.
     *
     * @return
     */
    public String getId() {
        return this.id;
    }

    /**
     * Permet de set l'id
     *
     * @param pId id du produit.
     */
    public void setId(String pId) {
        this.id = pId;
    }

    /**
     * Permet d'obtenir le nom du produit.
     *
     * @return nom du produit
     */
    public String getNom() {
        return this.nom;
    }


    /**
     * Permet d'obtenir la catégorie du produit
     *
     * @return la catégorie
     */
    public Categorie getCategorie() {
        if (categorie.equals(Categorie.PROTEINE.toString())) return Categorie.PROTEINE;
        else if (categorie.equals(Categorie.BCAA.toString())) return Categorie.BCAA;
        else return Categorie.CREATINE;
    }

    /**
     * Permet d'obtenir le prix du produit.
     *
     * @return prix
     */
    public double getPrix() {
        return this.prix;
    }

    /**
     * Permet d'obtenir la quantité de produit.
     *
     * @return
     */
    public int getQuantite() {
        return this.quantite;
    }

    /**
     * Permet de mettre à jour la quantité et retourne la nouvelle valeur.
     *
     * @param pQuantite Quantité à ajouter
     * @return Nouvelle valeur de quantité.
     */
    public int updateQuantite(int pQuantite) {
        return (quantite += pQuantite);
    }

    /**
     * Permet de comparer 2 objets ProduitClient (Ne prend pas en compte la quantité)
     *
     * @param other autre object a comparé.
     * @return True ou False
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ProduitClient)) {
            return false;
        }

        ProduitClient that = (ProduitClient) other;

        // Égalité custom
        return this.nom.equals(that.getNom())
                && this.categorie.equals(that.getCategorie().toString())
                && this.prix == that.getPrix()
                && this.id.equals(that.getId());
    }

    /**
     * Override du hashCode
     *
     * @return Retourne le hashcode.
     */
    @Override
    public int hashCode() {
        int hashCode = 1;

        hashCode = hashCode * 37 + this.nom.hashCode();
        hashCode = hashCode * 37 + this.categorie.hashCode();

        return hashCode;
    }
}
