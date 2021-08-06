package algonquin.cst2335.oluw0006;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ChatRoom extends AppCompatActivity {

    boolean isTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_layout);

        isTablet = findViewById(R.id.detailsRoom) !=null;

        messageListFragment chatFragment = new messageListFragment();
        FragmentManager fMgr = getSupportFragmentManager();
        FragmentTransaction tx = fMgr.beginTransaction();

        tx.add(R.id.fragmentRoom, chatFragment);
        tx.commit();

        //or getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, chatFragment).commit();
    }

    public void userClickedMessage(messageListFragment.ChatMessage chatMessage, int position) {
        MessageDetailsFragment mdFragment = new MessageDetailsFragment(chatMessage, position);

        if(isTablet){
            getSupportFragmentManager().beginTransaction().add(R.id.detailsRoom, mdFragment).commit();
        }else {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoom, mdFragment).commit();
        }
    }

}



