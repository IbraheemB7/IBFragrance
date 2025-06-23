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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class log_in extends AppCompatActivity {
    // تعريف عناصر الواجهة
    private TextView tvLoginTitle;
    private EditText etEmailAddress;
    private EditText etPasswordLogin;
    private Button btnLogIn;
    private Button btnForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // استدعاء دالة الإنشاء
        EdgeToEdge.enable(this); // تفعيل التصميم من الحافة للحافة
        setContentView(R.layout.activity_log_in); // ربط الكلاس بملف XML للواجهة

        // ربط عناصر الواجهة مع الكود
        tvLoginTitle = findViewById(R.id.tvLoginTitle);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogIn = findViewById(R.id.btn_LogIn);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);

        // ربط حدث الضغط على زر "تسجيل الدخول"
        btnLogIn.setOnClickListener(v -> {
            checkEmailPassw_FB(); // استدعاء دالة التحقق من Firebase
        });

        // الكود التالي غير مفعل حالياً، لضبط الهوامش حسب حواف الشاشة
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    /**
     * دالة لقراءة والتحقق من صحة البريد الإلكتروني وكلمة السر
     * @return false إذا الإدخال صحيح true إذا فيه خطأ
     */
    private boolean readAndValidateFieldsValues() {
        boolean isAllOk = true; // متغير لتحديد إذا كل الحقول صحيحة

        String email = etEmailAddress.getText().toString().trim(); // قراءة الإيميل
        String password = etPasswordLogin.getText().toString().trim(); // قراءة كلمة السر

        // التحقق من صحة الإيميل
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmailAddress.setError("Invalid email"); // عرض رسالة خطأ
            isAllOk = false;
        }

        // التحقق من طول كلمة السر
        if (password.length() < 6) {
            etPasswordLogin.setError("Password must be at least 6 characters");
            isAllOk = false;
        }

        return isAllOk; // ترجيع النتيجة النهائية
    }

    /**
     * دالة تسجيل الدخول إلى Firebase باستخدام البريد وكلمة السر
     */
    private void checkEmailPassw_FB() {
        if (readAndValidateFieldsValues()) {
            // إنشاء كائن مصادقة من Firebase
            FirebaseAuth auth = FirebaseAuth.getInstance();

            // قراءة البيانات من الحقول
            String email = etEmailAddress.getText().toString().trim();
            String password = etPasswordLogin.getText().toString().trim();

            // تنفيذ تسجيل الدخول باستخدام Firebase
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // تسجيل الدخول تم بنجاح
                                Log.d("LoginActivity", "signInWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser(); // المستخدم الحالي

                                // الانتقال للصفحة الرئيسية
                                Intent intent = new Intent(log_in.this, products.class);
                                startActivity(intent);
                                finish(); // إنهاء صفحة تسجيل الدخول
                            } else {
                                // فشل في تسجيل الدخول
                                Log.w("LoginActivity", "signInWithEmail:failure", task.getException());
                                Toast.makeText(log_in.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show(); // عرض رسالة فشل
                            }
                        }
                    });
        }
    }
}
