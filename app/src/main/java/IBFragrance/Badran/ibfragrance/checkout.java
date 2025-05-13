package IBFragrance.Badran.ibfragrance;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        tvCheckout=findViewById(R.id.tvCheckout);
        tvTotalPriceCheckout=findViewById(R.id.tvTotalPriceCheckout);
        tvBillingInfo=findViewById(R.id.tvBillingInfo);
        etFullName=findViewById(R.id.etFullName);
        etEmail=findViewById(R.id.etEmail);
        tvShippingInfo=findViewById(R.id.tvShippingInfo);
        etShippingAddress=findViewById(R.id.etShippingAddress);
        etPhoneNumber=findViewById(R.id.etPhoneNumber);
        btnPlaceOrder=findViewById(R.id.btnPlaceOrder);

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }


    private boolean readAndValidateFields() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String shippingAddress = etShippingAddress.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();

        if (fullName.isEmpty()) {
            etFullName.setError("Full name is required");
            return false;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email is required");
            return false;
        }

        if (shippingAddress.isEmpty()) {
            etShippingAddress.setError("Shipping address is required");
            return false;
        }

        if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
            etPhoneNumber.setError("Valid phone number is required");
            return false;
        }

        // Prepare to save
        // You can add code here to save the data to your database or server

        return true;
    }

}