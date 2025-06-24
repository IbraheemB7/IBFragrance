package IBFragrance.Badran.ibfragrance;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class checkout extends AppCompatActivity {

    private TextView tvCheckout;
    private TextView tvTotalPriceCheckout;
    private TextView tvBillingInfo;
    private TextInputEditText etFullName;
    private TextInputEditText etEmail;
    private TextView tvShippingInfo;
    private TextInputEditText etShippingAddress;
    private TextInputEditText etPhoneNumber;
    private Button btnPlaceOrder;

    private FirebaseAuth auth;
    private DatabaseReference ordersRef;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        tvCheckout = findViewById(R.id.tvCheckout);
        tvTotalPriceCheckout = findViewById(R.id.tvTotalPriceCheckout);
        tvBillingInfo = findViewById(R.id.tvBillingInfo);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        tvShippingInfo = findViewById(R.id.tvShippingInfo);
        etShippingAddress = findViewById(R.id.etShippingAddress);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "الرجاء تسجيل الدخول", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        uid = auth.getCurrentUser().getUid();
        ordersRef = FirebaseDatabase.getInstance().getReference("orders").child(uid);

        // عرض السعر المرسل من صفحة السلة
        double totalPrice = getIntent().getDoubleExtra("TOTAL_PRICE", 0.0);
        tvTotalPriceCheckout.setText(String.format("Total Price: %.2f ₪", totalPrice));

        btnPlaceOrder.setOnClickListener(v -> {
            if (!readAndValidateFields()) return;
            placeOrder(totalPrice);
        });
    }

    private boolean readAndValidateFields() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String shippingAddress = etShippingAddress.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        if (fullName.isEmpty()) {
            etFullName.setError("الاسم الكامل مطلوب");
            return false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("الإيميل غير صالح");
            return false;
        }
        if (shippingAddress.isEmpty()) {
            etShippingAddress.setError("عنوان الشحن مطلوب");
            return false;
        }
        if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
            etPhoneNumber.setError("رقم هاتف صالح مطلوب");
            return false;
        }
        return true;
    }

    private void placeOrder(double totalPrice) {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String shippingAddress = etShippingAddress.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        String orderId = ordersRef.push().getKey();
        if (orderId == null) {
            Toast.makeText(this, "خطأ في إنشاء معرف الطلب", Toast.LENGTH_SHORT).show();
            return;
        }

        Order order = new Order(fullName, email, shippingAddress, phoneNumber, totalPrice, System.currentTimeMillis());

        ordersRef.child(orderId).setValue(order)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "تم تقديم الطلب بنجاح!", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "فشل تقديم الطلب: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    // كلاس مساعد لتعريف الطلب
    public static class Order {
        public String fullName;
        public String email;
        public String shippingAddress;
        public String phoneNumber;
        public double totalPrice;
        public long timestamp;

        public Order() {
            // مطلوب من Firebase
        }

        public Order(String fullName, String email, String shippingAddress, String phoneNumber, double totalPrice, long timestamp) {
            this.fullName = fullName;
            this.email = email;
            this.shippingAddress = shippingAddress;
            this.phoneNumber = phoneNumber;
            this.totalPrice = totalPrice;
            this.timestamp = timestamp;
        }
    }
}
