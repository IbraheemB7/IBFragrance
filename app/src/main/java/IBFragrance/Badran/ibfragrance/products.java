package IBFragrance.Badran.ibfragrance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import IBFragrance.Badran.ibfragrance.data.CartAdapter;
import IBFragrance.Badran.ibfragrance.data.Perfume;

public class products extends AppCompatActivity {

    private ImageView ivSelectedImage;
    private Button btnAddPerfume, btnYourCart, btnSelectImage;
    private ProgressBar progressBarUpload;
    private EditText etProductName, etProductPrice;

    private Uri selectedImageUri;

    private RecyclerView rvPerfumes;
    private CartAdapter adapter;
    private List<Perfume> perfumeList = new ArrayList<>();

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    private DatabaseReference cartRef;

    private FirebaseAuth auth;
    private String uid;

    private ActivityResultLauncher<String> pickImage;
    private ActivityResultLauncher<String> requestReadMediaImagesPermission;
    private ActivityResultLauncher<String> requestReadMediaVideoPermission;
    private ActivityResultLauncher<String> requestReadExternalStoragePermission;

    private Set<String> cartPerfumeIds = new HashSet<>(); // لتخزين IDs المنتجات الموجودة بالسلة

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_products);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        ivSelectedImage = findViewById(R.id.ivSelectedImage);
        btnAddPerfume = findViewById(R.id.btnAddPerfume);
        btnYourCart = findViewById(R.id.btnYourCart);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        progressBarUpload = findViewById(R.id.progressBarUpload);
        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);

        rvPerfumes = findViewById(R.id.rvPerfumes);
        rvPerfumes.setLayoutManager(new LinearLayoutManager(this));

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "الرجاء تسجيل الدخول", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        uid = auth.getCurrentUser().getUid();
        cartRef = databaseRef.child("cartItems").child(uid);

        adapter = new CartAdapter(this, perfumeList, cartPerfumeIds, false);
        rvPerfumes.setAdapter(adapter);

        adapter.setOnItemAddListener(position -> {
            Perfume selected = perfumeList.get(position);
            addPerfumeToCart(selected);
        });

        adapter.setOnItemRemoveListener(position -> {
            Perfume selected = perfumeList.get(position);
            removePerfumeFromCart(selected);
        });

        pickImage = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        selectedImageUri = result;
                        ivSelectedImage.setImageURI(result);
                        ivSelectedImage.setVisibility(ImageView.VISIBLE);
                    }
                });

        requestReadMediaImagesPermission = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (!isGranted) {
                        Toast.makeText(this, "تم رفض إذن الصور", Toast.LENGTH_SHORT).show();
                    }
                });

        requestReadMediaVideoPermission = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                });

        requestReadExternalStoragePermission = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                });

        ivSelectedImage.setOnClickListener(v -> {
            checkAndRequestPermissions();
            pickImage.launch("image/*");
        });

        btnSelectImage.setOnClickListener(v -> {
            checkAndRequestPermissions();
            pickImage.launch("image/*");
        });

        btnAddPerfume.setOnClickListener(v -> addNewPerfume());

        btnYourCart.setOnClickListener(v -> startActivity(new Intent(this, cart.class)));

        loadPerfumesFromFirebase();

        loadCartItemsIds();
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadMediaImagesPermission.launch(android.Manifest.permission.READ_MEDIA_IMAGES);
            }
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_VIDEO)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadMediaVideoPermission.launch(android.Manifest.permission.READ_MEDIA_VIDEO);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadExternalStoragePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void addNewPerfume() {
        String name = etProductName.getText().toString().trim();
        String price = etProductPrice.getText().toString().trim();

        if (name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "يرجى إدخال اسم العطر والسعر", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri == null) {
            Toast.makeText(this, "يرجى اختيار صورة للعطر", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadPerfumeImageAndSave(selectedImageUri, name, price);
    }

    private void uploadPerfumeImageAndSave(Uri imageUri, String name, String price) {
        progressBarUpload.setVisibility(ProgressBar.VISIBLE);
        String fileName = "perfume_images/" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child(fileName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        imageRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String downloadUrl = uri.toString();
                                    savePerfumeToDatabase(name, price, downloadUrl);
                                })
                                .addOnFailureListener(e -> {
                                    progressBarUpload.setVisibility(ProgressBar.GONE);
                                    Toast.makeText(this, "فشل الحصول على رابط الصورة: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                })
                )
                .addOnFailureListener(e -> {
                    progressBarUpload.setVisibility(ProgressBar.GONE);
                    Toast.makeText(this, "فشل رفع صورة العطر: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void savePerfumeToDatabase(String name, String price, String imageUrl) {
        String perfumeId = databaseRef.child("perfumes").push().getKey();

        if (perfumeId == null) {
            Toast.makeText(this, "خطأ في إنشاء معرف للعطر", Toast.LENGTH_SHORT).show();
            progressBarUpload.setVisibility(ProgressBar.GONE);
            return;
        }

        Perfume perfume = new Perfume(perfumeId, name, price, imageUrl);

        databaseRef.child("perfumes").child(perfumeId).setValue(perfume)
                .addOnSuccessListener(unused -> {
                    progressBarUpload.setVisibility(ProgressBar.GONE);
                    Toast.makeText(this, "تمت إضافة العطر بنجاح", Toast.LENGTH_SHORT).show();
                    clearInputs();
                })
                .addOnFailureListener(e -> {
                    progressBarUpload.setVisibility(ProgressBar.GONE);
                    Toast.makeText(this, "فشل إضافة العطر: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void clearInputs() {
        etProductName.setText("");
        etProductPrice.setText("");
        ivSelectedImage.setImageURI(null);
        ivSelectedImage.setVisibility(ImageView.GONE);
        selectedImageUri = null;
    }

    private void loadPerfumesFromFirebase() {
        databaseRef.child("perfumes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                perfumeList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Perfume perfume = ds.getValue(Perfume.class);
                    if (perfume != null) {
                        perfumeList.add(perfume);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(products.this, "فشل تحميل العطور", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCartItemsIds() {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartPerfumeIds.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CartItem item = ds.getValue(CartItem.class);
                    if (item != null) {
                        cartPerfumeIds.add(item.perfumeId);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(products.this, "فشل تحميل السلة: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPerfumeToCart(Perfume perfume) {
        if (perfume == null) return;

        // المفتاح هنا هو perfumeId
        cartRef.child(perfume.getId()).setValue(new CartItem(perfume.getId(), perfume.getName(), perfume.getPrice(), perfume.getImageUrl(), 1))
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, perfume.getName() + " تم إضافته للسلة", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "فشل إضافة العنصر للسلة: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void removePerfumeFromCart(Perfume perfume) {
        if (perfume == null) return;

        cartRef.child(perfume.getId()).removeValue()
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, perfume.getName() + " تم إزالته من السلة", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "فشل إزالة العنصر من السلة: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // كلاس مساعد يمثل عنصر في السلة
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
