package algonquin.cst2335.oluw0006;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.Toast;

import static java.lang.Character.isDigit;


/**
 * @author Eric Torunski
 * @version 1.0
 */

public class MainActivity extends AppCompatActivity {

    /** This holds the text at the centre of the stream*/
    private TextView tv = null;

    /** This holds the password that the user will enter*/
    private EditText et = null;

    /** This is the button that checks the password*/
    private Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.tv);
        EditText et = findViewById(R.id.et);
        Button btn = findViewById(R.id.btn);

        btn.setOnClickListener( clk -> {
            String password = et.getText().toString();

            checkPasswordComplexity( password );
            if(checkPasswordComplexity(password) == false) {
                tv.setText("You Shall Not Pass!");
            }else {
                tv.setText("Your password is complex enough");
            }
        });
    }

    /** This function checks for special characters in a single char
     *
     * @param c character to be checked for special character
     * @return returns true if there's a special character, false if not
     */
    boolean isSpecialCharacter(char c) {
        switch (c){
            case '#':
            case '?':
            case '&':
            case '!':
            case '@':
            case '$':
            case '%':
            case '^':
            case '*':
                return true;
            default:
                return false;
        }
    }

    /** This function checks the complexity of a user entered string
     *
     * @param pw The String object that we are checking
     * @return Returns true if the password is complex enough, false if not complex enough.
     */

    boolean checkPasswordComplexity (String pw){

        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (int i = 0; i < pw.length(); i++){

            char c = pw.charAt(i);

            if (isDigit(c)){
                foundNumber = true;
            } else if (Character.isUpperCase(c)){
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
            else {
                return false;
            }
        }

        if(!foundUpperCase)
        {
            Toast.makeText(getApplicationContext(), "You're missing an uppercase letter",Toast.LENGTH_SHORT).show(); ;// Say that they are missing an upper case letter;
            return false;
        }

        else if( ! foundLowerCase)
        {
            Toast.makeText(getApplicationContext(), "You're missing an lowercase letter",Toast.LENGTH_SHORT).show(); // Say that they are missing a lower case letter;
            return false;
        }

        else if( ! foundNumber)
        {
            Toast.makeText(getApplicationContext(), "You're missing a number",Toast.LENGTH_SHORT).show(); // Say that they are missing a lower case letter;
            return false;
        }

        else if(! foundSpecial)
        {
            Toast.makeText(getApplicationContext(), "You're missing a special character",Toast.LENGTH_SHORT).show(); // Say that they are missing a lower case letter;
            return false;
        }



        else
            return true; //only get here if they're all true
    };


}