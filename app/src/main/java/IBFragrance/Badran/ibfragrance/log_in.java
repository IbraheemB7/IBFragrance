package IBFragrance.Badran.ibfragrance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class log_in extends AppCompatActivity {
    private TextView tvLoginTitle;
    private EditText etEmailAddress;
    private EditText etPasswordLogin;
    private Button btnLogIn;
    private Button btnForgotPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        tvLoginTitle = findViewById(R.id.tvLoginTitle);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);


//      ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
 //         return insets;
 //       });
    }




    /**
     * Reads and validates the values of the email and password fields.
     *
     * @return true if all fields are valid, false otherwise
     */
    private boolean readAndValidateFieldsValues(){
        boolean isAllOk = true;

        String email = etEmailAddress.getText().toString().trim();
        String password = etPasswordLogin.getText().toString().trim();

        if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmailAddress.setError("Invalid email");
            isAllOk = false;
        }

        if(password.length() < 6){
            etPasswordLogin.setError("Password must be at least 6 characters");
            isAllOk = false;
        }

        return isAllOk;
    }


    /**
     * Checks the email and password fields against Firebase Authentication.
     */
    private void checkEmailPassw_FB() {
        if (readAndValidateFieldsValues()) {
            // Sign in to Firebase Authentication using the email and password
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String email = etEmailAddress.getText().toString().trim();
            String password = etPasswordLogin.getText().toString().trim();

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, navigate to the main activity
                                Log.d("LoginActivity", "signInWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                Intent intent = new Intent(log_in.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("LoginActivity", "signInWithEmail:failure", task.getException());
                                Toast.makeText(log_in.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


}