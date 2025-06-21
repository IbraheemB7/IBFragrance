package IBFragrance.Badran.ibfragrance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splash_screen extends AppCompatActivity {

    private TextView tvLoading;   // عنصر نصي لعرض حالة التحميل

    private final int IMAGE_PICK_CODE = 100;   // كود لطلب اختيار صورة من المعرض
    private final int PERMISSION_CODE = 101;   // كود لطلب صلاحية الوصول للملفات

    private ImageButton ivLogo;    // زر صورة (لوجو) - غير مستخدم حاليا
    private Button btnUpload;      // زر رفع صورة - غير مستخدم حاليا
    private Uri toUploadimageUri;  // رابط الصورة المراد رفعها - غير مستخدم حاليا
    private Uri downloaduri;       // رابط الصورة بعد الرفع - غير مستخدم حاليا

    private perfume_product perfume; // عنصر كائن من نوع perfume_product - غير مستخدم حاليا

    // دالة لفتح المعرض لاختيار صورة
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);  // إنشاء نية (Intent) لاختيار صورة
        intent.setType("image/*");                        // تحديد نوع الملف المطلوب: صورة فقط
        startActivityForResult(intent, IMAGE_PICK_CODE);  // بدء النشاط مع كود الطلب
    }

    // دالة لفحص صلاحية قراءة التخزين وطلبها إذا لم تمنح
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // فقط للأندرويد 6 أو أحدث
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {      // إذا لم تمنح الصلاحية
                String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);  // نطلب الصلاحية من المستخدم
            } else {
                pickImageFromGallery();  // الصلاحية موجودة مسبقاً، نفتح المعرض مباشرة
            }
        } else {
            pickImageFromGallery();      // للإصدارات الأقدم من 6، نفتح المعرض مباشرة
        }
    }

    // التعامل مع نتيجة طلب الصلاحية
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {  // إذا كان الرد لطلب صلاحية القراءة
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // تم منح الصلاحية، نفتح المعرض لاختيار صورة
                pickImageFromGallery();
            } else {
                // رفض الصلاحية، نظهر رسالة للمستخدم
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // نقطة البداية عند إنشاء النشاط
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);                    // تفعيل العرض من الحافة للحافة
        setContentView(R.layout.activity_splash_screen); // ربط الشاشة بملف التصميم

        // إنشاء Thread جديد لانتظار 3 ثواني قبل الانتقال للشاشة التالية
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000); // الانتظار 3 ثواني (3000 مللي ثانية)
                } catch (InterruptedException e) {
                    e.printStackTrace(); // طباعة الخطأ في حالة حصول مقاطعة
                }
                // بعد انتهاء الانتظار، تنفيذ كود تغيير الشاشة في الواجهة الرئيسية (UI thread)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // الانتقال إلى شاشة التسجيل (sign_up)
                        startActivity(new Intent(splash_screen.this, sign_up.class));
                    }
                });
            }
        };
        thread.start();  // بدء تنفيذ Thread الانتظار

        // كود التعامُل مع الـ window insets (معطل مؤقتاً)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}
