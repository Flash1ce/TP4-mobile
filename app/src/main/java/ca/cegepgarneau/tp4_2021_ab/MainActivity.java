package ca.cegepgarneau.tp4_2021_ab;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import ca.cegepgarneau.tp4_2021_ab.databinding.ActivityMainBinding;
import ca.cegepgarneau.tp4_2021_ab.listeVendeur.ListeVendeurFragment;

/**
 * Activité principale
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private Fragment fragmentCourant;


    /**
     * Quand l'activité est en cours de création. Permet d'instancier des variables
     * de classe ou de vérifier le bundle savedInstanceState.
     *
     * @param savedInstanceState Bundle des données sauvegardés onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Instance du service d'authentification
        mAuth = FirebaseAuth.getInstance();

        // Si token user déjà valide, sinon redirigé sur login.
        if (mAuth.getCurrentUser() != null) {
            // Menu du bas.
            BottomNavigationView navView = findViewById(R.id.nav_view);
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_lstVendeur, R.id.navigation_lstClient)
                    .build();

            navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Si token user n'est plus valide
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    /**
     * Création du menu dans la toolbar
     *
     * @param menu
     * @return un boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Quand un item du menu est cliqué. Admin ou déconnexion
     *
     * @param item item cliqué
     * @return un boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Active ou désactive le mode admin.
            case R.id.menuAdmin:
                setFragmentCourant();
                setAdminMode();
                break;
            // Permet de ce déconecté de l'app.
            case R.id.menuDeconnexion:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Toast.makeText(this, getString(R.string.msgDeconnexion), Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @return un boolean
     */
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    /**
     * Permet de récupérer le fragment courrant et le mettre dans la variable.
     */
    private void setFragmentCourant() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        fragmentCourant = navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

    /**
     * Permet de gérer le mode admin si il est on ou off.
     */
    private void setAdminMode() {
        if (fragmentCourant instanceof ListeVendeurFragment) {
            ((ListeVendeurFragment) fragmentCourant).updateStatusAdmin();
        } else {
            Toast.makeText(this, R.string.msgAdminRefus, Toast.LENGTH_SHORT).show();
        }
    }
}