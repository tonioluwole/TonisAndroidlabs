package algonquin.cst2335.oluw0006;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.Toast;

import static java.lang.Character.isDigit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button forecastBtn = findViewById(R.id.btn);
        EditText cityText = findViewById(R.id.ets);

        forecastBtn.setOnClickListener( (click) ->{
            Executor newThread = Executors.newSingleThreadExecutor();
            newThread.execute( () -> {
                try {
                    String cityName = URLEncoder.encode(cityText.getText().toString(), "UTF-8");

                    String stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                            + cityName
                            + "&appid=7e943c97096a9784391a981c4d878b22&units=Metric";

                    URL url = new URL(stringURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    String text = (new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8)))
                            .lines()
                            .collect(Collectors.joining("\n"));

                    //Entire JSON URL Result
                    JSONObject theDocument = new JSONObject( text );

                    //Coordinates - JSON Object
                    JSONObject coord = theDocument.getJSONObject( "coord" );

                    //Weather - (JSON) Array
                    JSONArray weatherArray = theDocument.getJSONArray ( "weather" );

                    //Only JSON Object in the weather array at position 0
                    JSONObject position0 = weatherArray.getJSONObject(0);

                    //Visibility - JSON Integer
                    int vis = theDocument.getInt("visibility");

                    //City name - JSON string
                    String name = theDocument.getString( "name" );

                    String description = position0.getString("description");
                    String iconName = position0.getString("icon");

                    JSONObject mainObject = theDocument.getJSONObject("main");
                    double current = mainObject.getDouble("temp");
                    double min = mainObject.getDouble("temp_min");
                    double max = mainObject.getDouble("temp_max");
                    int humidity = mainObject.getInt("humidity");

                    //Commented out because i couldnt figure out wheer to place this. App loads JSON without image
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

                    runOnUiThread( (  )  -> {
                        TextView tv1 = findViewById(R.id.temp);
                        TextView tv2 = findViewById(R.id.max);
                        TextView tv3 = findViewById(R.id.min);
                        TextView tv4 = findViewById(R.id.humidity);
                        TextView tv5 = findViewById(R.id.description);

                        tv1.setText("The current temperature is " + current);
                        tv1.setVisibility(View.VISIBLE);

                        tv2.setText("The max temperature is " + max);
                        tv2.setVisibility(View.VISIBLE);

                        tv3.setText("The min temperature is " + min);
                        tv3.setVisibility(View.VISIBLE);

                        tv4.setText("Humidity: " + humidity);
                        tv4.setVisibility(View.VISIBLE);

                        tv5.setText("Description: " + description);
                        tv5.setVisibility(View.VISIBLE);

                        /*ImageView iv = findViewById(R.id.iv);
                        iv.setImageBitmap(image);
                        iv.setVisibility(View.VISIBLE);*/
                    });
                }
                catch (IOException | JSONException ioe) {
                    Log.e("Connection errorssssss:", ioe.getMessage());
                }
            });
        });
    }
}