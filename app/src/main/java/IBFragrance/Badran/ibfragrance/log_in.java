package IBFragrance.Badran.ibfragrance;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class log_in extends AppCompatActivity {
    private TextView tvLoginTitle;
    private EditText etEmailAddress;
    private EditText etPasswordLogin;
    private Button btnLogIn;
    private Button btnForgotPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        tvLoginTitle = findViewById(R.id.tvLoginTitle);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);









//      ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//          Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//          v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
 //         return insets;
 //       });
    }


}