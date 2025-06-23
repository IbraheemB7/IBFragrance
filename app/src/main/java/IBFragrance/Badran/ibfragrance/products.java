package IBFragrance.Badran.ibfragrance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import IBFragrance.Badran.ibfragrance.data.Perfume;

public class products extends AppCompatActivity {

    private static final String TAG = "products";

    // عناصر الواجهة
    private TextView tvProductName, tvProductPrice;
    private ImageView ivSelectedImage;
    private Button btnAddPerfume, btnYourCart, btnSelectImage;
    private ProgressBar progressBarUpload;

    // بدال TextView
    private EditText etProductName, etProductPrice;


    private Uri selectedImageUri;

    // Launchers لنتيجة اختيار الصورة وأذونات التخزين
    private ActivityResultLauncher<String> pickImage;
    private ActivityResultLauncher<String> requestReadMediaImagesPermission;
    private ActivityResultLauncher<String> requestReadMediaVideoPermission;
    private ActivityResultLauncher<String> requestReadExternalStoragePermission;

    // مراجع Firebase
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_products);

        // إعداد التولبار
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        // ربط العناصر بالواجهة
        ivSelectedImage = findViewById(R.id.ivSelectedImage);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        btnAddPerfume = findViewById(R.id.btnAddPerfume);  // زر إضافة عطر جديد
        btnYourCart = findViewById(R.id.btnYourCart);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        progressBarUpload = findViewById(R.id.progressBarUpload);
        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);


        // تهيئة Firebase
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        // تفعيل اختيار صورة من المعرض
        pickImage = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        selectedImageUri = result;
                        ivSelectedImage.setImageURI(result);
                        ivSelectedImage.setVisibility(View.VISIBLE);
                    }
                });

        // طلب الأذونات المناسبة
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

        // التعامل مع النقر على الصورة أو زر اختيار صورة
        ivSelectedImage.setOnClickListener(v -> {
            checkAndRequestPermissions();
            pickImage.launch("image/*");
        });

        btnSelectImage.setOnClickListener(v -> {
            checkAndRequestPermissions();
            pickImage.launch("image/*");
        });

        // زر إضافة العطر الجديد
        btnAddPerfume.setOnClickListener(v -> {
            addNewPerfume();
        });

        // زر الذهاب للسلة
        btnYourCart.setOnClickListener(v -> {
            startActivity(new Intent(this, cart.class));
        });
    }

    /**
     * التحقق وطلب الأذونات المناسبة لقراءة الصور من الجهاز
     */
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

    /**
     * دالة إضافة عطر جديد
     * تتحقق من إدخال البيانات، ثم ترفع الصورة، وبعدها تحفظ العطر في قاعدة البيانات
     */
    private void addNewPerfume() {
        String name = tvProductName.getText().toString().trim();
        String price = tvProductPrice.getText().toString().trim();

        // التحقق من وجود اسم وسعر العطر
        if (name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "يرجى إدخال اسم العطر والسعر", Toast.LENGTH_SHORT).show();
            return;
        }

        // التحقق من اختيار صورة للعطر
        if (selectedImageUri == null) {
            Toast.makeText(this, "يرجى اختيار صورة للعطر", Toast.LENGTH_SHORT).show();
            return;
        }

        // رفع الصورة وحفظ بيانات العطر
        uploadPerfumeImageAndSave(selectedImageUri, name, price);
    }

    /**
     * رفع صورة العطر إلى Firebase Storage ثم حفظ بيانات العطر في قاعدة البيانات
     * @param imageUri رابط الصورة المختارة
     * @param name اسم العطر
     * @param price سعر العطر
     */
    private void uploadPerfumeImageAndSave(Uri imageUri, String name, String price) {
        progressBarUpload.setVisibility(View.VISIBLE);

        // اسم الملف في التخزين، يتم توليده بشكل فريد
        String fileName = "perfume_images/" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child(fileName);

        // رفع الملف
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        // الحصول على رابط التحميل بعد الرفع
                        imageRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String downloadUrl = uri.toString();
                                    // حفظ بيانات العطر مع رابط الصورة
                                    savePerfumeToDatabase(name, price, downloadUrl);
                                })
                                .addOnFailureListener(e -> {
                                    progressBarUpload.setVisibility(View.GONE);
                                    Toast.makeText(this, "فشل الحصول على رابط الصورة: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                })
                )
                .addOnFailureListener(e -> {
                    progressBarUpload.setVisibility(View.GONE);
                    Toast.makeText(this, "فشل رفع صورة العطر: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * حفظ العطر في قاعدة بيانات Firebase Realtime Database
     * @param name اسم العطر
     * @param price سعر العطر
     * @param imageUrl رابط صورة العطر
     */
    private void savePerfumeToDatabase(String name, String price, String imageUrl) {
        String perfumeId = databaseRef.child("perfumes").push().getKey();

        if (perfumeId == null) {
            Toast.makeText(this, "خطأ في إنشاء معرف للعطر", Toast.LENGTH_SHORT).show();
            progressBarUpload.setVisibility(View.GONE);
            return;
        }

        // إنشاء كائن العطر
        Perfume perfume = new Perfume(perfumeId, name, price, imageUrl);

        // حفظه في المسار perfumes/perfumeId
        databaseRef.child("perfumes").child(perfumeId).setValue(perfume)
                .addOnSuccessListener(unused -> {
                    progressBarUpload.setVisibility(View.GONE);
                    Toast.makeText(this, "تمت إضافة العطر بنجاح", Toast.LENGTH_SHORT).show();
                    clearInputs();
                })
                .addOnFailureListener(e -> {
                    progressBarUpload.setVisibility(View.GONE);
                    Toast.makeText(this, "فشل إضافة العطر: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * مسح الحقول بعد إضافة العطر بنجاح
     */
    private void clearInputs() {
        tvProductName.setText("");
        tvProductPrice.setText("");
        ivSelectedImage.setImageURI(null);
        ivSelectedImage.setVisibility(View.GONE);
        selectedImageUri = null;
    }

    // عرض قائمة المينيو في التولبار
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // تأكد أن الملف موجود في res/menu
        return true;
    }

    // التعامل مع الضغط على عناصر المينيو
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.itmLogOut) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "تم تسجيل الخروج", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, sign_up.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
