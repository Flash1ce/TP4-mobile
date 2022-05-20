package ca.cegepgarneau.tp4_2021_ab.listeVendeur;

import static java.lang.Double.parseDouble;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.cegepgarneau.tp4_2021_ab.MainActivity;
import ca.cegepgarneau.tp4_2021_ab.R;
import ca.cegepgarneau.tp4_2021_ab.databinding.FragmentListeVendeurBinding;
import ca.cegepgarneau.tp4_2021_ab.model.ProduitVendeur;
import ca.cegepgarneau.tp4_2021_ab.utils.Categorie;

/**
 * Fragment de la liste vendeur.
 */
public class ListeVendeurFragment extends Fragment {
    // Database Firestore information
    private static final String KEY_PRIX = "PRIX";
    private static final String KEY_NOM = "NOM";
    private static final String KEY_CATEGORIE = "CATEGORIE";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collection = db.collection("produits");
    private ListenerRegistration registration;
    private final List<ProduitVendeur> lstProduits = new ArrayList<>();
    public boolean estAdmin;
    private FragmentListeVendeurBinding binding;
    private ProduitVendeurAdapter produitVendeurAdapter;

    private NotificationManagerCompat notificationManager;


    /**
     * Est éxécuté au démarage de l'application. Récupère les données dans la bd.
     */
    @Override
    public void onStart() {
        super.onStart();

        // Ajout du listener de la collection dans la bd de firestore.
        registration = collection.orderBy(KEY_NOM).addSnapshotListener((value, error) -> {
            lstProduits.clear();
            for (QueryDocumentSnapshot document : value) {
                // Création objet produit.
                ProduitVendeur produit = new ProduitVendeur();

                produit.setNom(document.getString(KEY_NOM));
                produit.setCategorie(document.getString(KEY_CATEGORIE));
                produit.setPrix(document.getDouble(KEY_PRIX));
                produit.setId(document.getId());

                lstProduits.add(produit);
            }
            produitVendeurAdapter.setProduits(lstProduits);
        });
    }

    /**
     * Est éxécuté quand la view est en cours de création. Le layout est inflate.
     * @param inflater layout
     * @param container Peut contenir des view "enfant"
     * @param savedInstanceState Bundle de données quand la view est détruite.
     * @return Retourne la view root.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListeVendeurBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.lstVendeur.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.lstVendeur.setHasFixedSize(true);
        produitVendeurAdapter = new ProduitVendeurAdapter();
        binding.lstVendeur.setAdapter(produitVendeurAdapter);

        registerForContextMenu(binding.lstVendeur);

        return root;
    }

    /**
     * Est éxécuté quand la view est créé.
     * @param view la view
     * @param savedInstanceState Bundle de données quand la view est détruite.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Permet de récupérer la valeur du mode admin et de choisir le mode du fab.
        if (savedInstanceState != null && savedInstanceState.containsKey("admin")) {
            estAdmin = !savedInstanceState.getBoolean("admin");
            updateStatusAdmin();
        }
    }

    /**
     * Quand l'activité est détruite, onSaveInstanceState
     * permet de sauvegarder si le mode admin est activé lors de la rotation de l'écran.
     *
     * @param outState le bundle
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("admin", estAdmin);

        super.onSaveInstanceState(outState);
    }

    public void sendNotificationProduit(View v, ProduitVendeur pProduitVendeur) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    "ChaineMagasin",
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Channel 1");
            channel1.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), Notification.AUDIO_ATTRIBUTES_DEFAULT);

            NotificationManager manager = getContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }

        notificationManager = NotificationManagerCompat.from(getContext());
        // Intent à placer dans la notification afin de permettre un accès à l'app par clic
        // En règle générale : router vers l'activité qui présente les données annoncées par la notification
        Intent intent = new Intent(getActivity(), MainActivity.class);
        // PendingIntent : intent potentiel
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (getContext(), 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        // Constructeur
        Notification notification = new NotificationCompat.Builder(getContext(), "ChaineMagasin")
                .setSmallIcon(R.drawable.ic_newproduit)
                .setContentTitle(getString(R.string.nouveauProduiTitre))
                .setContentText(getString(R.string.nouveauProduiMsg) + " " + pProduitVendeur.getNom())
                .setContentIntent(contentPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Catégorie : permet un réglage spécifique du signal de notification sur l'appareil
                .setCategory(NotificationCompat.CATEGORY_PROMO)
                .build();
        notificationManager.notify(1, notification);
    }

    /**
     * Quand le fab est cliqué, permet d'ajouter un produit.
     */
    public void fabAddClick() {
        View setView = getLayoutInflater().inflate(R.layout.set, null);

        EditText etName = setView.findViewById(R.id.etName);
        EditText etPrice = setView.findViewById(R.id.etPrice);

        Spinner spinner = setView.findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                Categorie.getAllCategories())
        );

        BtnSetHandler setHandler = new BtnSetHandler(etName, etPrice, spinner);

        // Lancement du dialogue pour la création.
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.createProduct)
                .setView(setView)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, setHandler)
                .show();
    }

    /**
     * Permet de mettre à jour le status de admin. passer de on à off.
     */
    public void updateStatusAdmin() {
        estAdmin = !estAdmin;
        if (estAdmin) {
            // Ajouts du clickListener du fab
            binding.fabAdd.setVisibility(View.VISIBLE);
            binding.fabAdd.setOnClickListener(view1 -> fabAddClick());
        }
        else {
            // Désactivation du clickListener du fab.
            binding.fabAdd.setVisibility(View.INVISIBLE);
            binding.fabAdd.setOnClickListener(null);
        }

        produitVendeurAdapter.setEstAdmin(estAdmin);
    }

    /**
     * Sur la création du menu contextuel.
     * @param menu le menu contextuel
     * @param v la view
     * @param menuInfo les infos du menu
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (estAdmin) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.context, menu);
        }
    }

    /**
     * Quand un item du context menu est sélectionné.
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ProduitVendeur selectedItem = produitVendeurAdapter.getSelectedProduit();
        switch (item.getItemId()) {
            case R.id.menu_remove:
                collection.document(selectedItem.getId())
                        .delete()
                        .addOnSuccessListener(DocumentReference -> {
                            String msg = selectedItem.getNom() + " " + getString(R.string.msgRemove);
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), getString(R.string.msgRemoveError), Toast.LENGTH_SHORT).show();
                        });
                break;
            case R.id.menu_update:
                updateProduitVendeur(selectedItem);
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Pour mettre à jour le produit de la liste vendeur dans la bd firebase.
     * @param selectedItem item sélectionné à mettre à jour.
     */
    private void updateProduitVendeur(ProduitVendeur selectedItem) {
        View setView = getLayoutInflater().inflate(R.layout.set, null);

        EditText etName = setView.findViewById(R.id.etName);
        etName.setText(selectedItem.getNom());
        EditText etPrice = setView.findViewById(R.id.etPrice);
        etPrice.setText(Double.toString(selectedItem.getPrix()));

        // Spinner pour les catégories
        Spinner spinner = setView.findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                Categorie.getAllCategories())
        );
        spinner.setSelection(selectedItem.getCategorie().getIdCategorieSpinner());

        BtnSetHandler setHandler = new BtnSetHandler(etName, etPrice, spinner, selectedItem);

        // Dialog pour la modification.
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.updateProduct)
                .setView(setView)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.ok, setHandler)
                .show();
    }

    /**
     * Set handler
     */
    private class BtnSetHandler implements DialogInterface.OnClickListener {
        private final EditText etName;
        private final EditText etPrice;
        private final Spinner spinner;
        private final ProduitVendeur oldProduit;

        /**
         * Constructeur du setHandler (Création)
         * @param pEtName editText nom
         * @param pEtPrice editText prix
         * @param pSpinner spinner des catégories
         */
        public BtnSetHandler(EditText pEtName, EditText pEtPrice, Spinner pSpinner) {
            this.etName = pEtName;
            this.etPrice = pEtPrice;
            this.spinner = pSpinner;
            this.oldProduit = null;
        }

        /**
         * Constructeur du setHandler (Modification)
         * @param pEtName editText nom
         * @param pEtPrice editText prix
         * @param pSpinner spinner des catégories
         * @param pOldProduit le produit à modifier
         */
        public BtnSetHandler(EditText pEtName, EditText pEtPrice, Spinner pSpinner, ProduitVendeur pOldProduit) {
            this.etName = pEtName;
            this.etPrice = pEtPrice;
            this.spinner = pSpinner;
            oldProduit = pOldProduit;
        }

        /**
         * Quand le bouton est cliqué
         * @param p_dialog le dialogue
         * @param p_which
         */
        @Override
        public void onClick(DialogInterface p_dialog, int p_which) {
            // Validation que les champs ne soient pas vide.
            if (!etName.getText().toString().isEmpty() && !etPrice.getText().toString().isEmpty() && parseDouble(etPrice.getText().toString()) > 0) {
                ProduitVendeur produit = new ProduitVendeur(
                        etName.getText().toString().trim(),
                        spinner.getSelectedItem().toString(),
                        parseDouble(etPrice.getText().toString())
                );

                Map<String, Object> newProduit = new HashMap<>();
                if (oldProduit == null) {
                    // Création
                    newProduit.put(KEY_NOM, produit.getNom());
                    newProduit.put(KEY_CATEGORIE, produit.getCategorie().toString());
                    newProduit.put(KEY_PRIX, produit.getPrix());

                    // Ajout de l'objet sous forme de document
                    collection
                            .add(newProduit)
                            .addOnSuccessListener(DocumentReference -> {
                                String msg = produit.getNom() + " " + getString(R.string.addMsg);
                                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                sendNotificationProduit(getView(), produit);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), getString(R.string.addMsgError), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    // Modification
                    newProduit.put(KEY_NOM, produit.getNom());
                    newProduit.put(KEY_CATEGORIE, produit.getCategorie().toString());
                    newProduit.put(KEY_PRIX, produit.getPrix());

                    collection.document(oldProduit.getId())
                            .update(newProduit)
                            .addOnSuccessListener(DocumentReference -> {
                                Toast.makeText(getContext(), getString(R.string.msgUpdate), Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), getString(R.string.msgUpdateError), Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                Toast.makeText(getContext(), getString(R.string.msgErrorDataInva), Toast.LENGTH_SHORT).show();
            }
        }
    }

}