package algonquin.cst2335.oluw0006;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.text.Editable;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mytext = findViewById(R.id.textview);

        EditText myedit = findViewById(R.id.myedittext);

        ImageButton imgbtn = findViewById(R.id.animage);

        Button btn = findViewById(R.id.mybutton);
        imgbtn.setOnClickListener( (v) -> {mytext.setText("Your edit text has: " + myedit.getText());});

        CheckBox  mycheck = findViewById(R.id.mycb);
        mycheck.setOnCheckedChangeListener( (cbtn, ischecked) -> {
            if (ischecked)
                btn.setText("Checkbox is on");
            else
                btn.setText("Checkbox is off");
        });

        RadioButton myradio = findViewById(R.id.myradio);
        myradio.setOnCheckedChangeListener( (cbtn, ischecked) -> {
            Context context = getApplicationContext();
            CharSequence text = "Hello toast!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        });
    }
}