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

    private TextView tvLoading;


    private final int IMAGE_PICK_CODE=100; // كود مخصص لطلب اختيار صورة
    private final int PERMISSION_CODE=101; // كود مخصص لطلب صلاحيات

    private ImageButton ivLogo;
    private Button btnUpload; // زر الرفع
    private Uri toUploadimageUri; // مسار الصورة المراد رفعها
    private Uri downloaduri; // مسار الصورة بعد الرفع

    private perfume_product perfume; // عنصر من نوع perfume_product

    private void pickImageFromGallery() {
        // Intent ضمني لاختيار صورة
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                String[] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE); // نطلب الصلاحية
            } else {
                pickImageFromGallery(); // الصلاحية موجودة، نفتح المعرض
            }
        } else {
            pickImageFromGallery(); // الجهاز قديم، نفتح المعرض مباشرة
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            toUploadimageUri = data.getData(); // نحفظ URI للصورة
            imgBtn1.setImageURI(toUploadimageUri); // نعرض الصورة في الزر
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // تم منح الصلاحية
                pickImageFromGallery();
            } else {
                // تم رفض الصلاحية
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);


        ivLogo = findViewById(R.id.ivLogo);
        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });


        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    // ننتظر 3 ثواني
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // بعد 3 ثواني، ننتقل إلى شاشة التسجيل
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(splash_screen.this, sign_up.class));
                    }
                });
            }
        };
        thread.start();



        tvLoading = findViewById(R.id.tvLoading);



 //       ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
 //           Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
 //           v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
 //           return insets;
 //       });
    }
}