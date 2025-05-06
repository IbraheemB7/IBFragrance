package IBFragrance.Badran.ibfragrance;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class cart extends AppCompatActivity {
    private TextView tvYourCart;
    private TextView tvCartSummary;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private RecyclerView rvCartItems;
    private LinearLayout totalSectionLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        tvYourCart=findViewById(R.id.tvYourCart);
        tvCartSummary=findViewById(R.id.tvCartSummary);
        tvTotalPrice=findViewById(R.id.tvTotalPrice);
        btnCheckout=findViewById(R.id.btnCheckout);
        rvCartItems=findViewById(R.id.rvCartItems);
        totalSectionLayout=findViewById(R.id.totalSectionLayout);








//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}