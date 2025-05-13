package IBFragrance.Badran.ibfragrance;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class perfume_product extends AppCompatActivity {

    private ImageView ivProductsImage;
    private TextView tvProductsName;
    private TextView tvProductsPrice;
    private TextView tvProductsQuantity;
    private Button btnRemoveItem;


    public perfume_product() {
    }

    public perfume_product(ImageView ivProductsImage, TextView tvProductsName, TextView tvProductsPrice, TextView tvProductsQuantity, Button btnRemoveItem) {
        this.ivProductsImage = ivProductsImage;
        this.tvProductsName = tvProductsName;
        this.tvProductsPrice = tvProductsPrice;
        this.tvProductsQuantity = tvProductsQuantity;
        this.btnRemoveItem = btnRemoveItem;
    }

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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfume_product);

        ivProductsImage = findViewById(R.id.ivProductsImage);
        tvProductsName = findViewById(R.id.tvProductsName);
        tvProductsPrice = findViewById(R.id.tvProductsPrice);
        tvProductsQuantity = findViewById(R.id.tvProductsQuantity);
        btnRemoveItem = findViewById(R.id.btnRemoveItem);




//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}