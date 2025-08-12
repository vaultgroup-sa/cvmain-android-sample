package co.za.vaultrgroup.cvmainsample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add a button to open configuration
        Button configButton = findViewById(R.id.config_button);
        configButton.setOnClickListener(v -> openConfiguration());
    }

    private void openConfiguration() {
        Intent intent = new Intent(this, co.za.vaultgroup.cvmain_android.ui.CvConfigurationActivity.class);
        startActivity(intent);
    }
}
