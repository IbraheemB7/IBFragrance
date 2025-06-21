package IBFragrance.Badran.ibfragrance;
// تعريف الباكيج اللي الكلاس موجود فيها

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class products extends AppCompatActivity {

    private static final String TAG = "products"; // لتعقب الأخطاء في Logcat

    // عناصر الواجهة
    private TextView tvProductName;
    private TextView tvProductPrice;
    private ImageView ivProductImage;
    private Button btnAddToCart;
    private Button btnYourCart;
    private ImageView ivSelectedImage;           // صورة يتم اختيارها من الجهاز
    private Uri selectedImageUri;                // رابط الصورة المختارة
    private ActivityResultLauncher<String> pickImage; // مشغّل لاختيار صورة من المعرض

    // مشغّلات لطلب أذونات الوصول للصور والفيديو والتخزين
    private ActivityResultLauncher<String> requestReadMediaImagesPermission;
    private ActivityResultLauncher<String> requestReadMediaVideoPermission;
    private ActivityResultLauncher<String> requestReadExternalStoragePermission;

    // إنشاء قائمة الخيارات العلوية (Menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // ربط القائمة بملف XML
        return true;
    }

    // تنفيذ أحداث عند اختيار عنصر من القائمة العلوية
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itmLogOut) {
            Toast.makeText(this, "LoggedOut Successfully", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.itmSettings) {
            Intent i = new Intent(products.this, settings.class);
            startActivity(i);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // دالة لفحص الأذونات حسب إصدار أندرويد
    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // أندرويد 13+: فحص إذن قراءة الصور
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadMediaImagesPermission.launch(android.Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                Log.d(TAG, "READ_MEDIA_IMAGES permission already granted");
                Toast.makeText(this, "إذن قراءة الصور ممنوح بالفعل", Toast.LENGTH_SHORT).show();
            }
        } else {
            // أندرويد أقل من 13: فحص إذن التخزين الخارجي
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadExternalStoragePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission already granted (for older versions)");
                Toast.makeText(this, "إذن قراءة التخزين ممنوح بالفعل (للإصدارات الأقدم)", Toast.LENGTH_SHORT).show();
            }
        }

        // فحص إذن الفيديو لأندرويد 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_VIDEO)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadMediaVideoPermission.launch(android.Manifest.permission.READ_MEDIA_VIDEO);
            } else {
                Log.d(TAG, "READ_MEDIA_VIDEO permission already granted");
                Toast.makeText(this, "إذن قراءة الفيديو ممنوح بالفعل", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // دالة إنشاء الصفحة
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // عرض من الحافة للحافة
        setContentView(R.layout.activity_products); // ربط الصفحة بملف XML

        // ربط عنصر صورة المستخدم مع الواجهة
        ivSelectedImage = findViewById(R.id.ivSelectedImage);

        // تسجيل مشغّل لاختيار صورة من الجهاز
        pickImage = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            selectedImageUri = result; // حفظ الصورة المختارة
                            ivSelectedImage.setImageURI(result); // عرضها
                            ivSelectedImage.setVisibility(View.VISIBLE); // إظهار العنصر
                        }
                    }
                });

        // عند الضغط على الصورة، يتم فتح المعرض لاختيار صورة
        ivSelectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage.launch("image/*"); // اختيار صورة فقط
            }
        });

        // تسجيل مشغّلات طلب الأذونات المختلفة
        requestReadMediaImagesPermission = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Log.d(TAG, "READ_MEDIA_IMAGES permission granted");
                        Toast.makeText(this, "تم منح إذن قراءة الصور", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "READ_MEDIA_IMAGES permission denied");
                        Toast.makeText(this, "تم رفض إذن قراءة الصور", Toast.LENGTH_SHORT).show();
                    }
                });

        requestReadMediaVideoPermission = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Log.d(TAG, "READ_MEDIA_VIDEO permission granted");
                        Toast.makeText(this, "تم منح إذن قراءة الفيديو", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "READ_MEDIA_VIDEO permission denied");
                        Toast.makeText(this, "تم رفض إذن قراءة الفيديو", Toast.LENGTH_SHORT).show();
                    }
                });

        requestReadExternalStoragePermission = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Log.d(TAG, "READ_EXTERNAL_STORAGE permission granted");
                        Toast.makeText(this, "تم منح إذن قراءة التخزين الخارجي", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "READ_EXTERNAL_STORAGE permission denied");
                        Toast.makeText(this, "تم رفض إذن قراءة التخزين الخارجي", Toast.LENGTH_SHORT).show();
                    }
                });

        // ربط باقي عناصر المنتج في الواجهة
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        ivProductImage = findViewById(R.id.ivProductImage);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnYourCart = findViewById(R.id.btnYourCart);

        // عند الضغط على زر "سلتي" يتم الانتقال إلى صفحة السلة
        btnYourCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(products.this, cart.class);
                startActivity(intent);
            }
        });

        // الكود التالي غير مفعل لضبط الحواف تلقائياً
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}
