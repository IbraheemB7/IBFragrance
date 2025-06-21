package IBFragrance.Badran.ibfragrance;
// تعريف الباكيج (المجلد البرمجي) اللي فيه هذا الكلاس

import android.content.Intent;
// استيراد كلاس Intent اللي بستخدم لفتح نشاط (Activity) ثاني

import android.os.Bundle;
// لاستقبال البيانات عند إنشاء النشاط

import android.view.View;
// للتعامل مع مكونات الشاشة

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
// استيراد العناصر المستخدمة من الواجهة: زر، تخطيط، نصوص

import androidx.activity.EdgeToEdge;
// لتفعيل عرض الشاشة من الحافة للحافة

import androidx.appcompat.app.AppCompatActivity;
// الكلاس الأساسي اللي كل Activity لازم ترث منه

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
// مكتبات للتعامل مع هوامش الشاشة (تم تعطيلها في الكود لاحقًا)

import androidx.recyclerview.widget.RecyclerView;
// لاستيراد قائمة عرض العناصر (RecyclerView) للسلة

public class cart extends AppCompatActivity {
// تعريف كلاس cart وهو صفحة السلة، ووراثته من AppCompatActivity

    private TextView tvYourCart;
    private TextView tvCartSummary;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private RecyclerView rvCartItems;
    private LinearLayout totalSectionLayout;
    // تعريف العناصر الموجودة في واجهة السلة وربطها لاحقًا بملف XML

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // استدعاء دالة onCreate عند فتح الصفحة

        EdgeToEdge.enable(this);
        // تفعيل خاصية عرض الواجهة من حافة الشاشة لحافتها (للتصميم الحديث)

        setContentView(R.layout.activity_cart);
        // ربط هذا الكلاس بواجهة XML الخاصة به: activity_cart.xml

        tvYourCart = findViewById(R.id.tvYourCart);
        // ربط عنصر عنوان السلة من الواجهة

        tvCartSummary = findViewById(R.id.tvCartSummary);
        // ربط نص الملخص من الواجهة

        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        // ربط النص اللي بعرض السعر الإجمالي

        btnCheckout = findViewById(R.id.btnCheckout);
        // ربط زر الدفع/الإتمام

        rvCartItems = findViewById(R.id.rvCartItems);
        // ربط الـ RecyclerView اللي بعرض عناصر السلة

        totalSectionLayout = findViewById(R.id.totalSectionLayout);
        // ربط القسم السفلي اللي فيه السعر الإجمالي وغيره

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cart.this, checkout.class);
                // إنشاء Intent للانتقال إلى صفحة الدفع checkout

                startActivity(intent);
                // بدء النشاط الجديد (فتح صفحة الدفع)
            }
        });

        // الكود التالي تم تعليقه (مش شغال حاليًا)
        // وظيفته كانت تعديل الحواف الخارجية للواجهة لتتناسب مع شاشات مختلفة

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}
