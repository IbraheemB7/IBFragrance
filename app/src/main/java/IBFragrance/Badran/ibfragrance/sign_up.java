
package IBFragrance.Badran.ibfragrance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import IBFragrance.Badran.ibfragrance.data.MyUser;

public class sign_up extends AppCompatActivity {

    // تعريف حقول الإدخال وأزرار الواجهة
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
        EdgeToEdge.enable(this);                            // تفعيل عرض الشاشة من الحافة للحافة
        setContentView(R.layout.activity_sign_up);          // ربط الصفحة بملف التصميم

        // إذا كان المستخدم مسجل الدخول مسبقًا يتم تحويله لصفحة المنتجات
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, products.class));
        }

        // ربط متغيرات الكود بعناصر الواجهة في ملف XML
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn = findViewById(R.id.btn_LogIn);

        // عند الضغط على زر تسجيل الدخول يتم الانتقال لصفحة تسجيل الدخول
        btnLogIn.setOnClickListener(v -> {
            Intent intent = new Intent(sign_up.this, log_in.class);
            startActivity(intent);
        });

        // إضافة مستمع للزر btnSignUp لتسجيل الاشتراك
        btnSignUp.setOnClickListener(v -> {
            // قراءة الحقول والتحقق منها ثم تسجيل المستخدم في Firebase
            readAndValidateFields();
        });
    }

    // دالة لقراءة قيم الحقول والتحقق من صحتها
    private void readAndValidateFields() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String emailAddress = etEmailAddress.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        boolean isAllOk = true;

        // التحقق من الاسم الأول
        if (firstName.isEmpty()) {
            etFirstName.setError("First name is required");
            isAllOk = false;
        }

        // التحقق من الاسم الأخير
        if (lastName.isEmpty()) {
            etLastName.setError("Last name is required");
            isAllOk = false;
        }

        // التحقق من صحة البريد الإلكتروني
        if (emailAddress.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            etEmailAddress.setError("Valid email is required");
            isAllOk = false;
        }

        // التحقق من كلمة المرور (6 أحرف على الأقل)
        if (password.isEmpty() || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            isAllOk = false;
        }

        // التحقق من صحة رقم الهاتف
        if (phone.isEmpty() || !android.util.Patterns.PHONE.matcher(phone).matches()) {
            etPhone.setError("Valid phone number is required");
            isAllOk = false;
        }

        // إذا كانت كل القيم صحيحة، يتم إنشاء المستخدم في Firebase
        if (isAllOk) {
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.createUserWithEmailAndPassword(emailAddress, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("SignUpActivity", "createUserWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                // حفظ بيانات المستخدم الإضافية في قاعدة البيانات
                                saveUser_FB(firstName, lastName, emailAddress, password, phone);
                            } else {
                                Log.w("SignUpActivity", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(sign_up.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    // كائن المستخدم لتخزين البيانات في قاعدة Firebase Realtime Database
    MyUser user = new MyUser();

    // دالة لحفظ بيانات المستخدم الإضافية في قاعدة البيانات
    private void saveUser_FB(String firstName, String lastName, String emailAddress, String password, String phone) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = database.child("users");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // تعيين البيانات في كائن المستخدم
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(emailAddress);
        user.setPassword(password);
        user.setPhone(phone);
        user.setID(uid);

        // تخزين بيانات المستخدم في المسار الخاص به ضمن قاعدة البيانات
        usersRef.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(sign_up.this, "Succeeded to add user", Toast.LENGTH_SHORT).show();
                    finish(); // إغلاق صفحة التسجيل بعد نجاح العملية
                } else {
                    Toast.makeText(sign_up.this, "Failed to add user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
