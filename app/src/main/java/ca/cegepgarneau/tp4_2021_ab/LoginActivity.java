package ca.cegepgarneau.tp4_2021_ab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import ca.cegepgarneau.tp4_2021_ab.databinding.ActivityLoginBinding;

/**
 * Activity d'authentification du user (Firebase)
 */
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    /**
     * Est éxécuté lors de la création de l'activité. Permet d'instancier des variables
     * de classe ou de vérifier le bundle savedInstanceState.
     * @param savedInstanceState Bundle des données sauvegardés onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState != null && savedInstanceState.containsKey("mdp") && savedInstanceState.containsKey("email")) {
            binding.etMdp.setText(savedInstanceState.getString("mdp"));
            binding.etEmail.setText(savedInstanceState.getString("email"));
        }

        // Instance du service d'authentification de firebase
        mAuth = FirebaseAuth.getInstance();

        // Si token user déjà valide
        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(this, R.string.dejaCo, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        // Listener sur le bouton connexion, permet de se connecter à l'app.
        binding.btnLogin.setOnClickListener(view -> {
            login(binding.etEmail.getText().toString(), binding.etMdp.getText().toString());
        });

        // Listener sur le bouton inscription, permet de retourner à la page d'inscription.
        binding.btnSwitchModeRegister.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    /**
     * Méthode qui permet de se connecter à firebase avec un email et mot de passe. le email n'est pas validé.
     *
     * @param courriel Courriel de l'utilisateur
     * @param mdp      Mot de passe de l'utilisateur.
     */
    private void login(String courriel, String mdp) {
        if (courriel.isEmpty() || mdp.isEmpty()) {
            Toast.makeText(LoginActivity.this, getString(R.string.msgValeurVideError), Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(courriel, mdp)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Réussie et accèes au user
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginActivity.this, getString(R.string.loginSuccess) + user.getEmail(), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(LoginActivity.this, getString(R.string.msgEmailError), Toast.LENGTH_SHORT).show();
                        } else if (e instanceof FirebaseAuthInvalidUserException) {
                            Toast.makeText(LoginActivity.this, getString(R.string.msgMdpError), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.msgEcheckConError), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * Quand l'activité est détruite, onSaveInstanceState
     * permet de sauvegarder les informations des champs.
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // insertion des données dans le bundle.
        outState.putString("email", binding.etEmail.getText().toString());
        outState.putString("mdp", binding.etMdp.getText().toString());

        super.onSaveInstanceState(outState);
    }

    /**
     * Quand l'activité est à nouveau affiché, permet de faire en sorte qu'elle ne
     * soit pas atteignable quand l'utilisateur est connecté.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Si token user déjà valide
        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(this, R.string.dejaCo, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }
}