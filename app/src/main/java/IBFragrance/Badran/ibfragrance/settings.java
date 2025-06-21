package IBFragrance.Badran.ibfragrance;
// تعريف الحزمة

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class settings extends AppCompatActivity {
    // كلاس يمثل صفحة الإعدادات في التطبيق

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   // استدعاء دالة إنشاء الصفحة في الكلاس الأب
        EdgeToEdge.enable(this);               // تفعيل عرض التطبيق من الحافة للحافة (بدون هوامش سوداء)
        setContentView(R.layout.activity_settings);  // ربط الصفحة بملف تخطيط XML خاص بالإعدادات

        // إضافة مستمع (Listener) لتهيئة الحواف (الهامش) بحيث لا تتداخل مع شريط النظام (status/navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // الحصول على حجم الحواف الخاصة بنظام التشغيل (الأعلى، الأسفل، اليمين، اليسار)
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // ضبط الحشو padding للعرض بناء على الحواف لتجنب تداخل المحتوى مع النظام
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;  // إعادة قيمة الحواف المعدلة
        });
    }
}
