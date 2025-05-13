package IBFragrance.Badran.ibfragrance;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class splash_screen extends AppCompatActivity {

    private ImageView ivIBFLogo;
    private TextView tvLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        ivIBFLogo = findViewById(R.id.ivIBFLogo);
        tvLoading = findViewById(R.id.tvLoading);



 //       ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
 //           Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
 //           v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
 //           return insets;
 //       });
    }
}