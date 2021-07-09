package algonquin.cst2335.oluw0006;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ChatRoom extends AppCompatActivity {
    ArrayList<ChatMessage> messages = new ArrayList<>();
    MyChatAdapter adt;
    RecyclerView chatList;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chatlayout);

        Button send = findViewById(R.id.sendbutton);
        Button receive = findViewById(R.id.receivebutton);
        EditText field = findViewById(R.id.editmessage);

        MyOpenHelper opener = new MyOpenHelper();

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        chatList = findViewById(R.id.myrecycler);

        adt = new MyChatAdapter();
        chatList.setAdapter(adt);
        chatList.setLayoutManager(new LinearLayoutManager(this));

        send.setOnClickListener(  clk -> {
            ChatMessage thisMessage = new ChatMessage( field.getText().toString(), 1, currentDateandTime);
            messages.add(thisMessage);
            field.setText("");
            adt.notifyItemInserted(messages.size() - 1);
        } );

       receive.setOnClickListener(  clk -> {
            ChatMessage thisMessage = new ChatMessage( field.getText().toString(), 2, currentDateandTime);
            messages.add(thisMessage);
            field.setText("");
            adt.notifyItemInserted(messages.size() - 1);
        } );
    }

        public  class MyRowViews extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        int position =-1;

        public MyRowViews(View itemView) {
            super(itemView);

            itemView.setOnClickListener( click -> {

                MyRowViews newRow = adt.onCreateViewHolder(null, adt.getItemViewType(position));

                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                    builder.setMessage("Do you want to delete the message: " + messageText.getText())
                        .setTitle("Question: ")

                        .setNegativeButton("No",(dialog,cl)->{})
                        .setPositiveButton("Yes", (dialog,cl) -> {

                            position = getAbsoluteAdapterPosition();

                            ChatMessage removedMessage = messages.get(position);
                            messages.remove(position);
                            adt.notifyItemRemoved(position);

                            Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk -> {

                                        messages.add(position, removedMessage);
                                        adt.notifyItemInserted(position);

                                    })
                                    .show();
                    }).create().show();
            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }

            public void setPosition(int p) {position = p;}
        }

    public class MyChatAdapter extends RecyclerView.Adapter<MyRowViews>{

        public int getItemViewType(int position) {
            ChatMessage thisRow = messages.get(position);

            return position;
        }

        @Override
        public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = getLayoutInflater();

            int layoutID;
            if(viewType == 1) // send
                layoutID = R.layout.sent_message;
            else
                layoutID = R.layout.receive_layout;
            View loadedRow = inflater.inflate(R.layout.sent_message, parent, false);

            return new MyRowViews(loadedRow);
        }

        @Override
        public void onBindViewHolder(MyRowViews holder, int position) {
            holder.messageText.setText(messages.get(position).getMessage() );
            holder.timeText.setText( messages.get(position).getTimeSent());
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }
    }

    private class ChatMessage {
        String message;
        int sendOrReceive;
        String timeSent;

        public ChatMessage(String message, int sendOrReceive, String timeSent) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
        }

        public String getMessage() {
            return message;
        }

        public int getSendOrReceive() {
            return sendOrReceive;
        }

        public String getTimeSent() {
            return timeSent;
        }
    }

}



