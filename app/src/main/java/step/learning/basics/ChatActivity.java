package step.learning.basics;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    private String chatUrl;
    private String content;
    private LinearLayout chatContainer;

    private EditText etUserName;
    private EditText etUserMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        new Thread(this::loadUrl).start();

        chatUrl = getString(R.string.chat_api_url);
        chatContainer = findViewById(R.id.chat_container);
        etUserName = findViewById(R.id.et_user_name);
        etUserMessage = findViewById(R.id.et_chat_message);

        findViewById(R.id.btn_chat_send).setOnClickListener(this::setButtonClick);

    }

    private void setButtonClick(View view){
        String author = etUserName.getText().toString();
    }

    private void loadUrl() {
        try (InputStream urlStream = new URL(chatUrl).openStream()) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            byte[] chunk = new byte[4096];
            int length;
            while ((length = urlStream.read(chunk)) > -1) {
                bytes.write(chunk, 0, length);
            }
            content = new String(bytes.toByteArray(), StandardCharsets.UTF_8);
            bytes.close();
            new Thread(this::parseContent).start();
        } catch (android.os.NetworkOnMainThreadException ignored) {
            Log.d("ChatActivity::loadUrl", "NetworkOnMainThreadException");
        } catch (MalformedURLException ex) {
            Log.d("ChatActivity::loadUrl", "MalformedURLException: " + ex.getMessage());
        } catch (IOException ex) {
            Log.d("ChatActivity::loadUrl", "IOException: " + ex.getMessage());
        }
    }

    private void showChatMessage() {
        TextView tvMessage = new TextView(this);
        tvMessage.setText(content);
        tvMessage.setPadding(10, 5, 10, 5);
        chatContainer.addView(tvMessage);
    }

    private void parseContent() {
        try {
            JSONObject object = new JSONObject(content);
            JSONArray array;
            if (object.getString("status").equals("success")) {
                array = object.getJSONArray("data");
                StringBuilder sb = new StringBuilder();
                int length = array.length();
                JSONObject item;
                for (int i = 0; i < length; ++i) {
                    item = array.getJSONObject(i);
                    sb.append(
                            String.format(
                                    "%s: %s - %s%n",
                                    item.getString("moment"),
                                    item.getString("author"),
                                    item.getString("txt")
                            )
                    );
                }
                content = sb.toString();
                runOnUiThread(this::showChatMessage);
            }
        } catch (JSONException ex) {
            Log.d(
                    "ChatActivity::parseContent",
                    "JSONException: " + ex.getMessage()
            );
        }
    }

    public static class ChatMessage {

        private UUID id;
        private String author;
        private String text;
        private Date moment;
        private UUID idReply;
        private String replyPreview;
        private static final SimpleDateFormat scanFormat = new SimpleDateFormat("MMM d, yyyy KK:mm:ss a", Locale.US);

        public ChatMessage() {
        }

        public ChatMessage(JSONObject object) throws Exception {
            setId(UUID.fromString(object.getString("id")));
            setAuthor(object.getString("author"));
            setText(object.getString("txt"));
            try {
                setMoment(scanFormat.parse(object.getString("moment")));
            } catch (ParseException ex) {
                throw new JSONException("Date (moment) parse error: " + object.getString("moment"));
            }
            if (object.has("idReply")) {
                setIdReply(UUID.fromString(object.getString("idReply")));
            }
            if (object.has("replyPreview")) {
                setReplyPreview(object.getString("replyPreview"));
            }
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Date getMoment() {
            return moment;
        }

        public void setMoment(Date moment) {
            this.moment = moment;
        }

        public UUID getIdReply() {
            return idReply;
        }

        public void setIdReply(UUID idReply) {
            this.idReply = idReply;
        }

        public String getReplyPreview() {
            return replyPreview;
        }

        public void setReplyPreview(String replyPreview) {
            this.replyPreview = replyPreview;
        }
    }
}

