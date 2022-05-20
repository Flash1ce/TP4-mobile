package ca.cegepgarneau.tp4_2021_ab.listeClient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.DecimalFormat;
import java.util.List;

import ca.cegepgarneau.tp4_2021_ab.databinding.FragmentListeClientBinding;
import ca.cegepgarneau.tp4_2021_ab.model.ProduitClient;

/**
 * Fragment de la liste client (panier), affiche des items.
 */
public class ListeClientFragment extends Fragment {

    private ListeClientViewModel listeClientViewModel;
    private FragmentListeClientBinding binding;
    private LiveData<List<ProduitClient>> lstProduitClient;
    private ProduitClientAdapter produitClientAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Est éxécuté quand la view est en cours de création. Le layout est inflate.
     * @param inflater Layout
     * @param container Peut contenir des view "enfant"
     * @param savedInstanceState Bundle de données quand la view est détruite.
     * @return Retourne la view root.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListeClientBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listeClientViewModel = new ViewModelProvider(getActivity()).get(ListeClientViewModel.class);

        binding.lstClient.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.lstClient.setHasFixedSize(true);
        produitClientAdapter = new ProduitClientAdapter();
        binding.lstClient.setAdapter(produitClientAdapter);

        return root;
    }

    /**
     * Est éxécuté quand la view est créé.
     * @param view La view
     * @param savedInstanceState Bundle de données quand la view est détruite.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lstProduitClient = listeClientViewModel.getAllProduitClient();

        // Observeur sur la liste produitClient, ajoute les nouveaux items et met a jours le solde.
        lstProduitClient.observe(getViewLifecycleOwner(), lstProduitClient -> {
            produitClientAdapter.setProduits(lstProduitClient);
            binding.txtSolde.setText(new DecimalFormat("###,###.##")
                    .format(listeClientViewModel.getPrixTotal()));
        });
    }
}