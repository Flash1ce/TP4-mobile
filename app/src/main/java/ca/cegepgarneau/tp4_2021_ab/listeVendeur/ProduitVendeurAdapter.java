package ca.cegepgarneau.tp4_2021_ab.listeVendeur;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import ca.cegepgarneau.tp4_2021_ab.R;
import ca.cegepgarneau.tp4_2021_ab.databinding.RowvendeurBinding;
import ca.cegepgarneau.tp4_2021_ab.listeClient.ListeClientViewModel;
import ca.cegepgarneau.tp4_2021_ab.model.ProduitClient;
import ca.cegepgarneau.tp4_2021_ab.model.ProduitVendeur;
import ca.cegepgarneau.tp4_2021_ab.utils.Categorie;

/**
 * Adapter pour la liste vendeur.
 */
public class ProduitVendeurAdapter extends RecyclerView.Adapter<ProduitVendeurAdapter.ProduitHolder> {
    public int positionCourant = -1;
    public boolean estAdmin;
    private RowvendeurBinding binding;
    private List<ProduitVendeur> lstProduits;
    private Fragment fragmentCourant;
    private ListeClientViewModel listeClientViewModel;

    /**
     * Quand la view est en cours de création.
     * @param parent le parent
     * @param viewType
     * @return le produitHolder
     */
    @NonNull
    @Override
    public ProduitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Version de l'inflater prenant une view parente en paramètre (héritée de MainActivity)
        binding = RowvendeurBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = binding.getRoot();

        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        NavHostFragment navHostFragment = (NavHostFragment) activity.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        fragmentCourant = navHostFragment.getChildFragmentManager().getFragments().get(0);

        listeClientViewModel = new ViewModelProvider(fragmentCourant.getActivity()).get(ListeClientViewModel.class);

        return new ProduitHolder(view);
    }

    /**
     * Binding avec le holder des éléments contenus dans la view
     * @param holder le holder
     * @param position position de l'item dans la liste.
     */
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ProduitHolder holder, int position) {
        if (lstProduits != null) {
            ProduitVendeur produitCurrent = lstProduits.get(position);
            positionCourant = position;

            // passe les bonnes valeurs aux éléments du holder
            holder.tv_name.setText(produitCurrent.getNom());
            Categorie categorie = produitCurrent.getCategorie();
            holder.tv_categorie.setText(categorie.toString());
            holder.iv_loc_cat.setImageResource(categorie.getImageCategorie());
            holder.tv_price.setText((new DecimalFormat("###,###.##").format(produitCurrent.getPrix())));

            holder.itemView.setOnClickListener(view -> {
                if (!estAdmin) {
                    View addProduitClient = fragmentCourant.getLayoutInflater().inflate(R.layout.quantite_dialogue, null);

                    EditText etQuantite = addProduitClient.findViewById(R.id.etQuantite);
                    BtnAddClientHandler addClientHandler = new BtnAddClientHandler(etQuantite, produitCurrent);

                    new android.app.AlertDialog.Builder(fragmentCourant.getContext())
                            .setTitle(R.string.quantite)
                            .setView(addProduitClient)
                            .setNegativeButton(R.string.cancel, null)
                            .setPositiveButton(R.string.ok, addClientHandler)
                            .show();
                }
            });
        }
    }

    /**
     * Permet d'obtenir le produit sélectionné.
     * @return le produit vendeur sélectionné.
     */
    public ProduitVendeur getSelectedProduit() {
        if (positionCourant != -1 && lstProduits.size() > 0)
            return lstProduits.get(positionCourant);
        return null;
    }

    /**
     * Permet de set la variable estAdmin
     * @param pEstAdmin boolean, vrai = admin, faux = pas admin.
     */
    public void setEstAdmin(boolean pEstAdmin) {
        this.estAdmin = pEstAdmin;
    }

    /**
     * Permet d'obtenir le nombre d'items.
     * @return retourne int du nombre d'items
     */
    @Override
    public int getItemCount() {
        if (lstProduits != null)
            return lstProduits.size();
        else return 0;
    }

    /**
     * Permet de set la liste de produits.
     * @param pLstProduits liste de produits vendeur.
     */
    public void setProduits(List<ProduitVendeur> pLstProduits) {
        lstProduits = pLstProduits;
        notifyDataSetChanged();
    }

    /**
     * Référence les éléments de la vue
     */
    class ProduitHolder extends RecyclerView.ViewHolder {
        public ImageView iv_loc_cat;
        public TextView tv_name;
        public TextView tv_price;
        public TextView tv_categorie;

        /**
         * Constructeur du produitHolder.
         * @param itemView item de la view.
         */
        public ProduitHolder(View itemView) {
            super(itemView);
            this.iv_loc_cat = binding.img;
            this.tv_name = binding.txtName;
            this.tv_price = binding.txtPrix;
            this.tv_categorie = binding.txtCategorie;

            // Quand un long click est effectué
            itemView.setOnLongClickListener(view -> {
                positionCourant = getAbsoluteAdapterPosition();
                return false;
            });
        }
    }

    private class BtnAddClientHandler implements DialogInterface.OnClickListener {

        private final EditText etQuantite;
        private final ProduitVendeur produitVendeur;

        /**
         * Constructeur du addClientHandler
         * @param pEtQuantite la quantité
         * @param pProduitVendeur le produit vendeur
         */
        public BtnAddClientHandler(EditText pEtQuantite, ProduitVendeur pProduitVendeur) {
            this.etQuantite = pEtQuantite;
            this.produitVendeur = pProduitVendeur;
        }

        /**
         * Quand est cliqué
         * @param p_dialog le dialog
         * @param p_which
         */
        @Override
        public void onClick(DialogInterface p_dialog, int p_which) {
            String quantite = etQuantite.getText().toString();
            if (!quantite.isEmpty() && parseInt(quantite) > 0) {
                listeClientViewModel.addUpdateProduitClient(
                        new ProduitClient(
                                produitVendeur.getId(),
                                produitVendeur.getNom(),
                                produitVendeur.getCategorie(),
                                produitVendeur.getPrix(),
                                parseInt(quantite))
                );

                NavController navController = Navigation.findNavController(binding.getRoot());
                navController.navigate(R.id.navigation_lstClient);
            } else {
                Toast.makeText(fragmentCourant.getActivity(), "La quantité doit être au moins de 1", Toast.LENGTH_SHORT).show();
            }
        }
    }
}