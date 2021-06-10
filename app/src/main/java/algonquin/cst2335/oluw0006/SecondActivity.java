package algonquin.cst2335.oluw0006;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2058){
            if (resultCode == RESULT_OK) {
                ImageView profileImage = findViewById(R.id.imageView);
                Bitmap thumbnail = data.getParcelableExtra("data");
                profileImage.setImageBitmap(thumbnail);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Previous page getter
        Intent fromPrevious = getIntent();

        //Email address getter
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");

        //Text view on top of page
        TextView tView = findViewById(R.id.textView);

        //Displaying login image
        tView.setText("Welcome back " + emailAddress);

        //Phone call intent
        Button phoneButton = findViewById(R.id.button);
        TextView phoneNumber = findViewById(R.id.editTextPhone);

        Intent call = new Intent(Intent.ACTION_DIAL);
        call.setData(Uri.parse("tel:" + phoneNumber));

        phoneButton.setOnClickListener(  clk -> {
            startActivityForResult( call, 5432 );
        } );

        //Camera intent
        Button cameraButton = findViewById(R.id.changePicture);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        cameraButton.setOnClickListener(clk -> {
            startActivityForResult( cameraIntent, 2058 );
        } );


    }
}