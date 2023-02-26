package step.learning.basics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button calcButton = findViewById(R.id.calcButton);
        calcButton.setOnClickListener(this::btnCalcClick);

        Button gameButton = findViewById(R.id.gameButton);
        gameButton.setOnClickListener(this::btnGameClick);

        Button ratesButton = findViewById(R.id.ratesButton);
        ratesButton.setOnClickListener(this::btnRatesClick);

        Button chatButton = findViewById(R.id.chatButton);
        chatButton.setOnClickListener(this::btnChatClick);

        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this::btnExitClick);
    }

    private void btnCalcClick(View v){
        Intent calcIntent = new Intent(this, CalcActivity.class);
        startActivity(calcIntent);
    }
    private void btnGameClick(View v){
        Intent gameIntent = new Intent(this, GameActivity.class);
        startActivity(gameIntent);
    }
    private void btnRatesClick(View v){
        Intent ratesIntent = new Intent(this, RatesActivity.class);
        startActivity(ratesIntent);
    }
    private void btnChatClick(View v) {
        Intent chatIntent = new Intent(
                MainActivity.this,
                ChatActivity.class
        );
        startActivity(chatIntent);
    }
    private void btnExitClick(View v){
        finish();
    }
}