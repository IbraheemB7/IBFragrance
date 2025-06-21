package IBFragrance.Badran.ibfragrance;
// تعريف الحزمة (Package) اللي فيها الكلاس

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
// استيراد عناصر الواجهة

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
// استيراد مكتبات لدعم الحواف في الواجهة (الكود تبعها غير مفعل حالياً)

public class perfume_product extends AppCompatActivity {
    // تعريف كلاس يمثل واجهة عرض منتج عطر

    // عناصر الواجهة (صورة، اسم، سعر، كمية، زر إزالة)
    private ImageView ivProductsImage;
    private TextView tvProductsName;
    private TextView tvProductsPrice;
    private TextView tvProductsQuantity;
    private Button btnRemoveItem;

    // منشئ فارغ (مطلوب إذا بدك تستخدم الكلاس كـ Activity)
    public perfume_product() {
    }

    // منشئ فيه كل عناصر الواجهة كوسائط (مش استخدام شائع في Activities)
    public perfume_product(ImageView ivProductsImage, TextView tvProductsName, TextView tvProductsPrice, TextView tvProductsQuantity, Button btnRemoveItem) {
        this.ivProductsImage = ivProductsImage;
        this.tvProductsName = tvProductsName;
        this.tvProductsPrice = tvProductsPrice;
        this.tvProductsQuantity = tvProductsQuantity;
        this.btnRemoveItem = btnRemoveItem;
    }

    // Getter and Setter لكل عنصر من عناصر الواجهة
    public ImageView getIvProductsImage() {
        return ivProductsImage;
    }

    public void setIvProductsImage(ImageView ivProductsImage) {
        this.ivProductsImage = ivProductsImage;
    }

    public TextView getTvProductsName() {
        return tvProductsName;
    }

    public void setTvProductsName(TextView tvProductsName) {
        this.tvProductsName = tvProductsName;
    }

    public TextView getTvProductsPrice() {
        return tvProductsPrice;
    }

    public void setTvProductsPrice(TextView tvProductsPrice) {
        this.tvProductsPrice = tvProductsPrice;
    }

    public TextView getTvProductsQuantity() {
        return tvProductsQuantity;
    }

    public void setTvProductsQuantity(TextView tvProductsQuantity) {
        this.tvProductsQuantity = tvProductsQuantity;
    }

    public Button getBtnRemoveItem() {
        return btnRemoveItem;
    }

    public void setBtnRemoveItem(Button btnRemoveItem) {
        this.btnRemoveItem = btnRemoveItem;
    }

    // دالة تحويل الكائن إلى نص (تستخدم للطباعة أو الديباغ)
    @Override
    public String toString() {
        return "perfume_product{" +
                "ivProductsImage=" + ivProductsImage +
                ", tvProductsName=" + tvProductsName +
                ", tvProductsPrice=" + tvProductsPrice +
                ", tvProductsQuantity=" + tvProductsQuantity +
                ", btnRemoveItem=" + btnRemoveItem +
                '}';
    }

    @SuppressLint("MissingInflatedId") // تجاهل تحذير نقص ID في XML (مؤقت)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // دالة إنشاء الصفحة
        EdgeToEdge.enable(this); // تفعيل العرض من الحافة للحافة
        setContentView(R.layout.activity_perfume_product); // ربط الصفحة بملف XML

        // ربط عناصر الواجهة مع المكونات في XML
        ivProductsImage = findViewById(R.id.ivProductsImage);
        tvProductsName = findViewById(R.id.tvProductsName);
        tvProductsPrice = findViewById(R.id.tvProductsPrice);
        tvProductsQuantity = findViewById(R.id.tvProductsQuantity);
        btnRemoveItem = findViewById(R.id.btnRemoveItem);

        // كود لإدارة الهوامش غير مفعل حالياً
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}
