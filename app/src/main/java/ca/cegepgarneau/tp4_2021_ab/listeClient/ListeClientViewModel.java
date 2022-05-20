package ca.cegepgarneau.tp4_2021_ab.listeClient;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import ca.cegepgarneau.tp4_2021_ab.model.ProduitClient;

/**
 * View model de la liste client.
 */
public class ListeClientViewModel extends AndroidViewModel {

    private final MutableLiveData<List<ProduitClient>> lstProduitClient = new MutableLiveData<>();
    private double solde = 0;
    /**
     * Constructeur du viewModel
     * @param application application
     */
    public ListeClientViewModel(Application application) {
        super(application);

        lstProduitClient.setValue(new ArrayList<>());
    }

    /**
     * Permet d'obtenir tous les produits du client.
     * @return retourne la liste des produits.
     */
    public LiveData<List<ProduitClient>> getAllProduitClient() {
        return lstProduitClient;
    }

    /**
     * Permet d'ajouter ou de modifier la quantité d'un produit qui est déjà dans la liste.
     * @param pProduitClient le produit à ajouter.
     */
    public void addUpdateProduitClient(ProduitClient pProduitClient) {
        boolean productEstPresent = false;
        int index = 0;

        // Vérification si le produit est présent si oui récupération de son index.
        while (index < lstProduitClient.getValue().size() && !productEstPresent)
            if (lstProduitClient.getValue().get(index).equals(pProduitClient))
                productEstPresent = true;
            else index += 1;

        // Ajout ou modification du produit dans la liste client.
        if (productEstPresent) {
            lstProduitClient.getValue().get(index).updateQuantite(pProduitClient.getQuantite());
            solde += (lstProduitClient.getValue().get(index).getPrix() * pProduitClient.getQuantite());
        } else {
            lstProduitClient.getValue().add(pProduitClient);
            solde += (pProduitClient.getPrix() * pProduitClient.getQuantite());
        }
    }

    /**
     * Permet d'obtenir le solde de la liste.
     * @return le solde
     */
    public double getPrixTotal() {return solde;}
}
