package IBFragrance.Badran.ibfragrance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

import IBFragrance.Badran.ibfragrance.data.CartAdapter;
import IBFragrance.Badran.ibfragrance.data.Perfume;

public class cart extends AppCompatActivity {

    private TextView tvTotalPrice;
    private Button btnCheckout;
    private RecyclerView rvCartItems;

    private List<Perfume> cartItems = new ArrayList<>();
    private CartAdapter cartAdapter;

    private FirebaseAuth auth;
    private DatabaseReference databaseRef;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        rvCartItems = findViewById(R.id.rvCartItems);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(this, cartItems);
        rvCartItems.setAdapter(cartAdapter);

        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "الرجاء تسجيل الدخول لعرض السلة", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        uid = auth.getCurrentUser().getUid();

        loadCartItems();

        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(cart.this, checkout.class);
            startActivity(intent);
        });
    }

    private void loadCartItems() {
        databaseRef.child("cart").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                cartItems.clear();
                double total = 0;

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Perfume perfume = itemSnapshot.getValue(Perfume.class);
                    if (perfume != null) {
                        cartItems.add(perfume);

                        // حساب السعر الإجمالي (افتراضياً السعر نص String, تحتاج تحويل)
                        try {
                            total += Double.parseDouble(perfume.getPrice());
                        } catch (NumberFormatException e) {
                            // إذا السعر مش عدد صالح، تجاهل أو تعامل مع الخطأ
                        }
                    }
                }

                cartAdapter.notifyDataSetChanged();
                tvTotalPrice.setText(String.format("السعر الإجمالي: %.2f ₪", total));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(cart.this, "خطأ في تحميل بيانات السلة: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
