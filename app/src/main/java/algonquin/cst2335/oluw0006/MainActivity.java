 package algonquin.cst2335.oluw0006;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    @Override
    protected void onStart() {
        super.onStart();
        Log.w("MainActivity","In onStart() = The application is now visible on screen");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity","In onResume() = The application is now visible on screen");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity","In onPause() = The application no longer responds to user input");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w("MainActivity","In onStop() = The application is no longer visible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("MainActivity","In onDestroy() = Any memory used by the application is freed");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.w("MainActivity","In onCreate() = Loading Widgets");
        Button loginButton = findViewById(R.id.loginButton);

        EditText emailet = findViewById(R.id.emailet);

        Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);

        loginButton.setOnClickListener(  clk -> {
            nextPage.putExtra( "EmailAddress", emailet.getText().toString() );
            startActivity( nextPage );
        } );
    }
}