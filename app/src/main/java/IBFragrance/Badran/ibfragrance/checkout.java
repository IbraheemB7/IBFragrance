package IBFragrance.Badran.ibfragrance;
// تعريف الباكيج (المجلد البرمجي) اللي فيه الكلاس

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
// استيراد عناصر واجهة المستخدم: أزرار ونصوص

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
// مكتبات لدعم الهوامش الحديثة للشاشة (تم تعطيلها لاحقًا)

import com.google.android.material.textfield.TextInputEditText;
// استيراد عنصر إدخال النص من Material Design

public class checkout extends AppCompatActivity {
// تعريف كلاس صفحة الدفع "checkout" ووراثته من AppCompatActivity

    // تعريف عناصر الواجهة المستخدمة في الصفحة
    private TextView tvCheckout;
    private TextView tvTotalPriceCheckout;
    private TextView tvBillingInfo;
    private TextInputEditText etFullName;
    private TextInputEditText etEmail;
    private TextView tvShippingInfo;
    private TextInputEditText etShippingAddress;
    private TextInputEditText etPhoneNumber;
    private Button btnPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // استدعاء عند إنشاء الصفحة

        EdgeToEdge.enable(this);
        // تفعيل عرض المحتوى من الحافة للحافة (تصميم حديث)

        setContentView(R.layout.activity_checkout);
        // ربط هذا الكلاس بواجهة XML الخاصة به: activity_checkout.xml

        // ربط كل عنصر في الكود بالعناصر الموجودة في ملف XML
        tvCheckout = findViewById(R.id.tvCheckout);
        tvTotalPriceCheckout = findViewById(R.id.tvTotalPriceCheckout);
        tvBillingInfo = findViewById(R.id.tvBillingInfo);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        tvShippingInfo = findViewById(R.id.tvShippingInfo);
        etShippingAddress = findViewById(R.id.etShippingAddress);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        // الكود التالي غير مفعل حالياً، وظيفته تعديل الهوامش لتتناسب مع حواف الشاشة
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    // دالة خاصة لقراءة القيم من الحقول والتحقق من صحتها
    private boolean readAndValidateFields() {
        // استخراج النص من كل حقل وإزالة الفراغات الزائدة
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String shippingAddress = etShippingAddress.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        // التحقق إذا الاسم فارغ
        if (fullName.isEmpty()) {
            etFullName.setError("Full name is required");
            return false;
        }

        // التحقق من صحة الإيميل باستخدام pattern جاهز
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            return false;
        }

        // التحقق من عنوان الشحن
        if (shippingAddress.isEmpty()) {
            etShippingAddress.setError("Shipping address is required");
            return false;
        }

        // التحقق من رقم الهاتف وطوله
        if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
            etPhoneNumber.setError("Valid phone number is required");
            return false;
        }


        return true; // رجوع بقيمة true إذا كل الحقول صحيحة
    }
}
