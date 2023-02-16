package step.learning.basics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CalcActivity extends AppCompatActivity {
    private TextView tvHistory;
    private TextView tvResult;
    private String minusSign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        tvHistory = findViewById(R.id.tvHistory);
        tvResult = findViewById(R.id.tvResult);
        minusSign = getString(R.string.btn_calc_minus);
        tvHistory.setText("");
        tvResult.setText("0");
        for (int i = 0; i < 10; i++) {
            findViewById(
                    getResources().getIdentifier(
                    "button_digit_" + i,
                    "id",
                    getPackageName()
            )).setOnClickListener(this::digitClick);
        }
        findViewById(R.id.button_plus_minus).setOnClickListener(this::pmClick);
        findViewById(R.id.button_inverse).setOnClickListener(this::inverseClick);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("history", tvHistory.getText());
        outState.putCharSequence("result", tvResult.getText());
        Log.d(CalcActivity.class.getName(), "Дані збережено");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvHistory.setText(savedInstanceState.getCharSequence("history"));
        tvResult.setText(savedInstanceState.getCharSequence("result"));
        Log.d(CalcActivity.class.getName(), "Дані відновлено");
    }


    @SuppressLint("SetTextI18n")
    private void pmClick(View v){
        String currentText = (String)tvResult.getText();
        if(currentText.equals("0")) return;
        if (currentText.startsWith(minusSign)){
            tvResult.setText(currentText.substring(1));
        } else tvResult.setText(minusSign + currentText);
    }

    private void inverseClick(View v){
        String currentText = (String)tvResult.getText();
        //String currentHistory = (String)tvHistory.getText();
        if(currentText.equals("0")) return;

        tvHistory.setText(String.format("1/(%s) =",
                currentText.length() > 10
                        ? currentText.substring(0, 10)
                        : currentText ));
        tvResult.setText(
                String.valueOf(1 / Double.parseDouble(currentText))
        );
    }

    @SuppressLint("SetTextI18n")
    private void digitClick(View v) {
        String currentText = (String)tvResult.getText();
        String enteredText = (String)((Button)v).getText();
        if(currentText.length() >= 10) return;
        if (currentText.equals("0")) {
            tvResult.setText(enteredText);
        } else {
            tvResult.setText(currentText + enteredText);
        }
    }

    private void showResult(double arg){
        String result = String.valueOf(arg);
        int finLength = 10;
        if(result.contains("-")) finLength++;
        if(result.contains(".")) finLength++;
        if(result.length() >= finLength){
            result = result.substring(0, finLength);
        }
        tvResult.setText(result.replace("-", minusSign));
    }
}