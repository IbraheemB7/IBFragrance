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