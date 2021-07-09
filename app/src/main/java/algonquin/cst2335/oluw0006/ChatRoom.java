package algonquin.cst2335.oluw0006;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    MyOpenHelper opener = new MyOpenHelper(this);
    SQLiteDatabase db = opener.getWritableDatabase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chatlayout);

        Button send = findViewById(R.id.sendbutton);
        Button receive = findViewById(R.id.receivebutton);
        EditText field = findViewById(R.id.editmessage);



        chatList = findViewById(R.id.myrecycler);

        Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);
        if (results != null && results.moveToFirst()) {
            int _idCol = results.getColumnIndex("_id");
            int messageCol = results.getColumnIndex("col_message");
            int sendCol = results.getColumnIndex("col_send_receive");
            int timeCol = results.getColumnIndex("col_time_sent");

            while (results.moveToNext()) {
                long id = results.getInt(_idCol);
                String message = results.getString(messageCol);
                String time = results.getString(timeCol);
                int sendOrReceive = results.getInt(sendCol);

                messages.add(new ChatMessage(message, sendOrReceive, time, id));

                results.close();
            }
        }

        adt = new MyChatAdapter();
        chatList.setAdapter(adt);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        chatList.setLayoutManager(layoutManager);

        send.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            ChatMessage thisMessage = new ChatMessage(field.getText().toString(), 1, currentDateandTime);

            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());

            long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);

            thisMessage.setId(newId);

            messages.add(thisMessage);
            field.setText("");
            adt.notifyItemInserted(messages.size() - 1);
        });

        receive.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            ChatMessage thisMessage = new ChatMessage(field.getText().toString(), 2, currentDateandTime);

            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, thisMessage.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, thisMessage.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, thisMessage.getTimeSent());

            long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);

            thisMessage.setId(newId);

            messages.add(thisMessage);
            field.setText("");
            adt.notifyItemInserted(messages.size() - 1);
        });

    }
        private class MyRowViews extends RecyclerView.ViewHolder {
            TextView messageText;
            TextView timeText;
            int position = -1;

            public MyRowViews(View itemView) {
                super(itemView);

                itemView.setOnClickListener(click -> {

                    MyRowViews newRow = adt.onCreateViewHolder(null, adt.getItemViewType(position));

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                    builder.setMessage("Do you want to delete the message: " + messageText.getText())
                            .setTitle("Question: ")

                            .setNegativeButton("No", (dialog, cl) -> {
                            })
                            .setPositiveButton("Yes", (dialog, cl) -> {

                                ChatMessage removedMessage = messages.get(position);
                                messages.remove(position);
                                adt.notifyItemRemoved(position);

                                db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[]{Long.toString(removedMessage.getId())});

                                Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                        .setAction("Undo", clk -> {
                                            db.execSQL("Insert into " + MyOpenHelper.TABLE_NAME + " values('" + removedMessage.getId() +
                                                    "','" + removedMessage.getMessage() +
                                                    "','" + removedMessage.getSendOrReceive() +
                                                    "','" + removedMessage.getTimeSent() + "');");

                                            messages.add(position, removedMessage);
                                            adt.notifyItemInserted(position);

                                        })
                                        .show();
                            }).create().show();
                });

                messageText = itemView.findViewById(R.id.message);
                timeText = itemView.findViewById(R.id.time);
            }

            public void setPosition(int p) {
                position = p;
            }
        }

        private class MyChatAdapter extends RecyclerView.Adapter<MyRowViews> {

            public int getItemViewType(int position) {
                ChatMessage message = messages.get(position);

                return position;
            }

            @Override
            public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {

                LayoutInflater inflater = getLayoutInflater();

                int layoutID;
                if (viewType == 1) // send
                    layoutID = R.layout.sent_message;
                else
                    layoutID = R.layout.receive_layout;
                View loadedRow = inflater.inflate(layoutID, parent, false);

                return new MyRowViews(loadedRow);
            }

            @Override
            public void onBindViewHolder(MyRowViews holder, int position) {
                holder.messageText.setText(messages.get(position).getMessage());
                holder.timeText.setText(messages.get(position).getTimeSent());
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
            long id;

            public void setId(long l) {
                id = l;
            }

            public long getId() {
                return id;
            }

            public ChatMessage(String message, int sendOrReceive, String timeSent) {
                this.message = message;
                this.sendOrReceive = sendOrReceive;
                this.timeSent = timeSent;
            }

            public ChatMessage(String message, int sendOrReceive, String timeSent, long id) {
                this.message = message;
                this.sendOrReceive = sendOrReceive;
                this.timeSent = timeSent;
                setId(id);
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




