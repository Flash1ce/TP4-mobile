package ca.cegepgarneau.tp4_2021_ab.listeClient;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import ca.cegepgarneau.tp4_2021_ab.databinding.RowclientBinding;
import ca.cegepgarneau.tp4_2021_ab.model.ProduitClient;
import ca.cegepgarneau.tp4_2021_ab.utils.Categorie;

/**
 * Adapter pour la liste client.
 */
public class ProduitClientAdapter extends RecyclerView.Adapter<ProduitClientAdapter.ProduitHolder> {
    private RowclientBinding binding;
    private List<ProduitClient> lstProduits;


    /**
     * Quand la view est en cours de création.
     * @param parent
     * @param viewType
     * @return produitHolder
     */
    @NonNull
    @Override
    public ProduitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowclientBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        View view = binding.getRoot();

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
            ProduitClient produitCurrent = lstProduits.get(position);

            // passe les bonnes valeurs aux éléments du holder.
            holder.tv_name.setText(produitCurrent.getNom());
            Categorie categorie = produitCurrent.getCategorie();
            holder.tv_categorie.setText(categorie.toString());
            holder.iv_loc_cat.setImageResource(categorie.getImageCategorie());
            holder.tv_price.setText((new DecimalFormat("###,###.##").format(produitCurrent.getPrix())));
            holder.tv_quantite.setText(Integer.toString(produitCurrent.getQuantite()));
        }
    }

    /**
     * Permet de récupérer le nombre d'item.
     * @return int du nombre d'items.
     */
    @Override
    public int getItemCount() {
        if (lstProduits != null)
            return lstProduits.size();
        else return 0;
    }

    /**
     * Permet de set la liste de produit et de notifyDataSetChanged
     * @param LstProduitClient liste des produits client.
     */
    public void setProduits(List<ProduitClient> LstProduitClient) {
        lstProduits = LstProduitClient;
        notifyDataSetChanged();
    }


    /**
     * Référence les éléments de la vue
     */
    public class ProduitHolder extends RecyclerView.ViewHolder {
        public ImageView iv_loc_cat;
        public TextView tv_name;
        public TextView tv_price;
        public TextView tv_categorie;
        public TextView tv_quantite;

        /**
         * Constructeur du ProduitHolder
         * @param itemView item de la vue.
         */
        public ProduitHolder(View itemView) {
            super(itemView);
            this.iv_loc_cat = binding.img;
            this.tv_name = binding.txtName;
            this.tv_price = binding.txtPrix;
            this.tv_categorie = binding.txtCategorie;
            this.tv_quantite = binding.quantite;
        }

    }
}
