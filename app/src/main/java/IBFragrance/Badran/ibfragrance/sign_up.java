package IBFragrance.Badran.ibfragrance;

import android.annotation.SuppressLint;
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
import com.google.firebase.firestore.FirebaseFirestore;

import IBFragrance.Badran.ibfragrance.data.MyUser;

public class sign_up extends AppCompatActivity {

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmailAddress;
    private EditText etPassword;
    private EditText etPhone;
    private Button btnSignUp;
    private Button btnLogIn;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn = findViewById(R.id.btnLogIn);

        btnLogIn.setOnClickListener(v -> {
            // Navigate to the login activity
            Intent intent = new Intent(sign_up.this, log_in.class);
            startActivity(intent);
});



//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
private void readAndValidateFields() {
    String firstName = etFirstName.getText().toString().trim();
    String lastName = etLastName.getText().toString().trim();
    String emailAddress = etEmailAddress.getText().toString().trim();
    String password = etPassword.getText().toString().trim();
    String phone = etPhone.getText().toString().trim();

    boolean isAllOk = true;

    if (firstName.isEmpty()) {
        etFirstName.setError("First name is required");
        isAllOk = false;
    }

    if (lastName.isEmpty()) {
        etLastName.setError("Last name is required");
        isAllOk = false;
    }

    if (emailAddress.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
        etEmailAddress.setError("Valid email is required");
        isAllOk = false;
    }

    if (password.isEmpty() || password.length() < 6) {
        etPassword.setError("Password must be at least 6 characters");
        isAllOk = false;
    }

    if (phone.isEmpty() || !android.util.Patterns.PHONE.matcher(phone).matches()) {
        etPhone.setError("Valid phone number is required");
        isAllOk = false;
    }

    if (isAllOk) {
        // Prepare to save
        // You can add code here to save the data to your database or server
        FirebaseAuth auth = FirebaseAuth.getInstance();
        // إنشاء مستخدم جديد باستخدام البريد الإلكتروني وكلمة المرور
        // Create a new user with email and password
        auth.createUserWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {//there is server listener
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignUpActivity", "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            // Navigate to the main activity
                            saveUser_FB(firstName, lastName, emailAddress, password, phone);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignUpActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(sign_up.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

    MyUser user = new MyUser();
    private void saveUser_FB(String firstName, String lastName, String emailAddress, String password, String phone) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(emailAddress);
        user.setPassword(password);
        user.setPhone(phone);
        user.setID(uid);



        db.collection("MyUsers").document(uid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(sign_up.this, "Succeeded to add user", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(sign_up.this, "Failed to add user", Toast.LENGTH_SHORT).show();
                }
                }
      });
    }



}