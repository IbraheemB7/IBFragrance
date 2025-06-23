package IBFragrance.Badran.ibfragrance;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import IBFragrance.Badran.ibfragrance.data.Perfume;

public class AddPerfumeActivity extends AppCompatActivity {

    private EditText etName, etPrice;
    private ImageView ivImage;
    private Button btnSelectImage, btnAdd;

    private Uri selectedImageUri = null;

    private StorageReference storageRef;
    private DatabaseReference databaseRef;

    // لاختيار الصورة من المعرض
    private ActivityResultLauncher<String> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_perfume);

        etName = findViewById(R.id.etPerfumeName);
        etPrice = findViewById(R.id.etPerfumePrice);
        ivImage = findViewById(R.id.ivPerfumeImage);
        btnSelectImage = findViewById(R.id.btnAddPerfume);
        btnAdd = findViewById(R.id.btnAddPerfume);

        // تهيئة مراجع Firebase
        storageRef = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("perfumes");

        // تهيئة اختيار الصورة
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        Glide.with(this).load(uri).into(ivImage);
                    }
                }
        );

        btnSelectImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        btnAdd.setOnClickListener(v -> addPerfume());
    }

    private void addPerfume() {
        String name = etName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();

        if (name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "الرجاء إدخال الاسم والسعر", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = databaseRef.push().getKey();

        if (selectedImageUri != null) {
            // رفع الصورة أولاً
            StorageReference imageRef = storageRef.child("perfume_images/" + id + ".jpg");
            imageRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot ->
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        savePerfumeToDatabase(id, name, price, imageUrl);
                    })
            ).addOnFailureListener(e -> {
                Toast.makeText(this, "فشل رفع الصورة: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            // إذا ما في صورة، نحفظ بدون رابط صورة
            savePerfumeToDatabase(id, name, price, "");
        }
    }

    private void savePerfumeToDatabase(String id, String name, String price, String imageUrl) {
        Perfume perfume = new Perfume(id, name, price, imageUrl);
        databaseRef.child(id).setValue(perfume)
                .addOnSuccessListener(unused -> Toast.makeText(this, "تم إضافة العطر", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "فشل الإضافة: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
