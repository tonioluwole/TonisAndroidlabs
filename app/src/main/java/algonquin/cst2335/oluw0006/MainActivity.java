package algonquin.cst2335.oluw0006;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.Toast;

import static java.lang.Character.isDigit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


/**
 * @author Eric Torunski
 * @version 1.0
 */

public class MainActivity extends AppCompatActivity {
    String description = null;
    String iconName = null;
    String current = null;
    String min = null;
    String max = null;
    String humidity = null;
    String unit = null;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button forecastBtn = findViewById(R.id.btn);
        EditText cityText = findViewById(R.id.ets);

        forecastBtn.setOnClickListener( (click) ->{
            String cityName = cityText.getText().toString();

            AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Getting forecast")
                    .setMessage("We're calling people in " +  cityName + " to look outside their windows and tell us what the weather is like over there")
                    .setView( new ProgressBar(MainActivity.this))
                    .show();

            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute( () -> {
                try {
                    String newCityName = URLEncoder.encode(cityText.getText().toString(), "UTF-8");

                    String stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                            + newCityName
                            + "&appid=7e943c97096a9784391a981c4d878b22&units=Metric&mode=xml";

                    URL url = new URL(stringURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput( in  , "UTF-8");

                    //Can't figure the image part out
                    /*Bitmap image = null;
                    URL imgUrl = new URL( "https://openweathermap.org/img/w/" + iconName + ".png" );
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());

                        ImageView iv = findViewById(R.id.iv);
                        iv.setImageBitmap(image);
                    }

                    FileOutputStream fOut = null;
                    try {
                        fOut = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }*/

                    while (xpp.next()!=XmlPullParser.END_DOCUMENT){
                        switch (xpp.getEventType())
                        {
                            case XmlPullParser.START_TAG:
                                if (xpp.getName().equals("temperature")) {
                                    current = xpp.getAttributeValue(null, "value"); //gets current temp
                                    min = xpp.getAttributeValue(null, "min"); //gets min temp
                                    max = xpp.getAttributeValue(null, "max"); //gets max temp
                                }
                                if (xpp.getName().equals("weather")) {
                                    description = xpp.getAttributeValue(null, "value"); //gets weather description
                                    iconName = xpp.getAttributeValue(null, "icon"); //gets icon name
                                }
                                if (xpp.getName().equals("humidity")) {
                                    unit = xpp.getAttributeValue(null, "unit");
                                    humidity = xpp.getAttributeValue(null, "value") + unit;//gets humidity
                                }

                                break;

                            case XmlPullParser.END_TAG:

                                break;

                            case XmlPullParser.TEXT:

                                break;
                        }

                        runOnUiThread( (  )  -> {

                            TextView tv = findViewById(R.id.temp);
                            tv.setText("The current temperature is " + current);
                            tv.setVisibility(View.VISIBLE);

                            tv = findViewById(R.id.max);
                            tv.setText("The max temperature is " + max);
                            tv.setVisibility(View.VISIBLE);

                            tv = findViewById(R.id.min);
                            tv.setText("The min temperature is " + min);
                            tv.setVisibility(View.VISIBLE);

                            tv = findViewById(R.id.humidity);
                            tv.setText("Humidity: " + humidity);
                            tv.setVisibility(View.VISIBLE);

                            tv = findViewById(R.id.description);
                            tv.setText("Description: " + description);
                            tv.setVisibility(View.VISIBLE);

                        /*ImageView iv = findViewById(R.id.iv);
                        iv.setImageBitmap(image);
                        iv.setVisibility(View.VISIBLE);*/

                            dialog.hide();
                        });
                    }
                }
                catch (IOException | XmlPullParserException ioe) {
                    Log.e("Connection errorssssss:", ioe.getMessage());
                }
            });
        });
    }
}