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
import java.util.HashSet;
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
        cartAdapter = new CartAdapter(this, cartItems, new HashSet<>(), true);
        rvCartItems.setAdapter(cartAdapter);

        // زر حذف من السلة
        cartAdapter.setOnItemRemoveListener(position -> {
            Perfume removedPerfume = cartItems.get(position);
            removeItemFromCartInFirebase(removedPerfume.getId());
        });

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
            double totalPrice = calculateTotalPrice();
            Intent intent = new Intent(cart.this, checkout.class);
            intent.putExtra("TOTAL_PRICE", totalPrice);
            startActivity(intent);
        });
    }

    private void loadCartItems() {
        DatabaseReference cartRef = databaseRef.child("cartItems").child(uid);
        DatabaseReference perfumesRef = databaseRef.child("perfumes");

        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                cartItems.clear();

                if (!snapshot.exists()) {
                    cartAdapter.notifyDataSetChanged();
                    tvTotalPrice.setText("Total Price: 0.00 ₪");
                    return;
                }

                final double[] total = {0};
                final int itemsCount = (int) snapshot.getChildrenCount();
                final int[] loadedCount = {0};

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    CartItem cartItem = itemSnapshot.getValue(CartItem.class);
                    if (cartItem != null) {
                        String perfumeId = cartItem.perfumeId;
                        perfumesRef.child(perfumeId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot perfumeSnapshot) {
                                Perfume perfume = perfumeSnapshot.getValue(Perfume.class);
                                if (perfume != null) {
                                    cartItems.add(perfume);
                                    try {
                                        total[0] += Double.parseDouble(perfume.getPrice()) * cartItem.quantity;
                                    } catch (NumberFormatException e) {
                                        // تجاهل خطأ تحويل السعر
                                    }
                                }
                                loadedCount[0]++;
                                if (loadedCount[0] == itemsCount) {
                                    cartAdapter.notifyDataSetChanged();
                                    tvTotalPrice.setText(String.format("Total Price: %.2f ₪", total[0]));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                loadedCount[0]++;
                                if (loadedCount[0] == itemsCount) {
                                    cartAdapter.notifyDataSetChanged();
                                    tvTotalPrice.setText(String.format("Total Price: %.2f ₪", total[0]));
                                }
                            }
                        });
                    } else {
                        loadedCount[0]++;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(cart.this, "خطأ في تحميل بيانات السلة: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double calculateTotalPrice() {
        double total = 0;
        for (Perfume p : cartItems) {
            try {
                total += Double.parseDouble(p.getPrice());
            } catch (NumberFormatException e) {
                // تجاهل خطأ التحويل
            }
        }
        return total;
    }

    private void removeItemFromCartInFirebase(String perfumeId) {
        if (perfumeId == null || perfumeId.isEmpty()) return;

        databaseRef.child("cartItems").child(uid).child(perfumeId).removeValue()
                .addOnSuccessListener(unused -> Toast.makeText(cart.this, "تم حذف المنتج من السلة", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(cart.this, "فشل حذف المنتج: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // كلاس مساعد لتمثيل عنصر السلة حسب Firebase
    public static class CartItem {
        public String perfumeId;
        public String name;
        public String price;
        public String imageUrl;
        public int quantity;

        public CartItem() {
            // مطلوب من Firebase
        }

        public CartItem(String perfumeId, String name, String price, String imageUrl, int quantity) {
            this.perfumeId = perfumeId;
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
            this.quantity = quantity;
        }
    }
}
