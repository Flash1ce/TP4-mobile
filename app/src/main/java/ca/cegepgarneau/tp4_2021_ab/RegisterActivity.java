package ca.cegepgarneau.tp4_2021_ab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import ca.cegepgarneau.tp4_2021_ab.databinding.ActivityRegisterBinding;

/**
 * Activité pour l'inscription de l'utilisateur.
 */
public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;

    /**
     * Est éxécuté lors de la création de l'activité. Permet d'instancier des variables
     * de classe ou de vérifier le bundle savedInstanceState.
     * @param savedInstanceState Bundle des données sauvegardés onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Instance du service d'authentification
        mAuth = FirebaseAuth.getInstance();

        if (savedInstanceState != null && savedInstanceState.containsKey("mdp") && savedInstanceState.containsKey("email")) {
            binding.etMdp.setText(savedInstanceState.getString("mdp"));
            binding.etEmail.setText(savedInstanceState.getString("email"));
        }

        // Si token user déjà valide
        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(this, getString(R.string.dejaCo), Toast.LENGTH_SHORT).show();
        }

        // Listener du bouton d'inscription quand il est cliqué.
        binding.btnRegister.setOnClickListener(view -> {
            createAccount(binding.etEmail.getText().toString(), binding.etMdp.getText().toString());
        });

        // Listener du bouton login, permet de retourner à l'activité de connexion.
        binding.btnSwitchModeLogin.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    /**
     * Permet de créé un compte et de se connecter à firebase
     * @param courriel courriel de l'utilisateur
     * @param mdp mot de passe de l'utilisateur.
     */
    private void createAccount(String courriel, String mdp) {
        if (courriel.isEmpty() || mdp.isEmpty()) {
            Toast.makeText(RegisterActivity.this, getString(R.string.msgValeurVideError), Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(courriel, mdp)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Succès et accèes a l'utilisateur.
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(RegisterActivity.this,
                                    getString(R.string.registerSuccess) + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        }
                    })
                    .addOnFailureListener(this, e -> {
                        if (e instanceof FirebaseAuthWeakPasswordException) {
                            Toast.makeText(RegisterActivity.this, getString(R.string.msgMdpError), Toast.LENGTH_SHORT).show();
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(RegisterActivity.this, getString(R.string.msgEmailError), Toast.LENGTH_SHORT).show();
                        } else if (e instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RegisterActivity.this, getString(R.string.msgEmailDejaUti), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, getString(R.string.msgInscriptionError), Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        }
    }
}