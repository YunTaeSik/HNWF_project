package com.ai.project.hnwf_project.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ai.project.hnwf_project.R;
import com.ai.project.hnwf_project.util.GetHangle;

import static java.lang.Math.exp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText name_input;
    private Button succes_btn;

    private int input_count = 8;
    private int hidden_count = 8;
    private int out_count = 6;
    private double first_w[][] = new double[input_count][hidden_count];
    private double second_w[][] = new double[hidden_count][out_count];
    /*   private double Input_one[] = {11, 17, 4, 2016, 11, 27, 3, 1};
       private double Input_two[] = {0, 20, 16, 2016, 11, 27, 13, 1};
       private double Input_three[] = {14, 11, 0, 2016, 2, 8, 12, 2};
       private double Input_four[] = {7, 0, 1, 2020, 5, 16, 17, 1};*/
    private double Input_one[] = {0.11, 0.17, 0.04, 0, 0.11, 0.27, 0.03, 0.1};
    private double Input_two[] = {0, 20, 0.16, 0, 0.11, 0.27, 0.13, 0.1};
    private double Input_three[] = {0.14, 0.11, 0, 0, 0.02, 0.08, 0.12, 0.2};
    private double Input_four[] = {0.07, 0, 0.01, 0.04, 0.05, 0.16, 0.17, 0.1};
    /*private double Input_one[] = {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
    private double Input_two[] = {0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2};
    private double Input_three[] = {0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3};
    private double Input_four[] = {0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4};*/
    private double input_collection[][] = {Input_one, Input_two, Input_three, Input_four};
    private double hidden[] = new double[hidden_count];
    private double out[][] = new double[input_collection.length][out_count];
    // private double target[][] = {{9, 20, 0, 11, 13, 0}, {6, 16, 4, 9, 4, 0}, {11, 7, 4, 9, 4, 4}, {0, 6, 21, 1, 4, 4}};
    private double target[][] = {{0.09, 0.20, 0, 0.11, 0.13, 0}, {0.06, 0.16, 0.04, 0.09, 0.04, 0}, {0.11, 0.07, 0.04, 0.09, 0.04, 0.04}
            , {0, 0.06, 0.21, 0.01, 0.04, 0.04}};
    /*  private double target[][] = {{0.2, 0.3, 0.4, 0.5, 0.6, 0.7}, {0.4, 0.5, 0.6, 0.7, 0.8, 0.9},
              {0.6, 0.7, 0.8, 0.9, 0.9, 0.9}, {0.8, 0.8, 0.8, 0.8, 0.8, 0.8}};*/
    private double n = 0.001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name_input = (EditText) findViewById(R.id.name_input);
        succes_btn = (Button) findViewById(R.id.succes_btn);
    /*    String a = "안";
        Log.e("test", hangulToJaso(a));
        hangulToJaso(a);*/
        Set_weight();
        asyncTask.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.succes_btn:
                break;
        }
    }

    public static String hangulToJaso(String str) {
        // 유니코드 한글 문자열을 입력 받음
        int a, b, c; // 자소 버퍼: 초성/중성/종성 순
        String result = "";

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(0);
            if (ch >= 0xAC00 && ch <= 0xD7A3) { // "AC00:가" ~ "D7A3:힣" 에 속한 글자면 분해
                c = ch - 0xAC00;
                a = c / (21 * 28);
                c = c % (21 * 28);
                b = c / 28;
                c = c % 28;
                result = result + GetHangle.ChoSung_String[a] + GetHangle.JungSung_String[b];
                if (c != 0)
                    result = result + GetHangle.JongSung_String[c]; // c가 0이 아니면, 즉 받침이 있으면
            } else {
                result = result + ch;
            }
        }
        return result;
    }

    private void Set_weight() {
        for (int i = 0; i < input_count; i++) {
            for (int j = 0; j < hidden_count; j++) {
                first_w[i][j] = Math.random();
            }
        }
        for (int i = 0; i < hidden_count; i++) {
            for (int j = 0; j < out_count; j++) {
                second_w[i][j] = Math.random();
            }
        }
    }

    private void Traning() {
        for (int all = 0; all < input_collection.length; all++) {

            for (int k = 0; k < hidden_count; k++) {
                double sum = 0;
                for (int i = 0; i < input_count; i++) {
                    sum += input_collection[all][i] * first_w[i][k];
                }
                hidden[k] = 1 / (1 + exp(-sum));
            }
            for (int k = 0; k < out_count; k++) {
                double out_sum = 0;
                for (int i = 0; i < hidden_count; i++) {
                    out_sum += hidden[i] * second_w[i][k];
                }
                out[all][k] = 1 / (1 + exp(-out_sum));
            }
            for (int k = 0; k < out_count; k++) {
                double differential = (n * (target[all][k] - out[all][k]) * (1 - out[all][k]) * out[all][k]);

                for (int i = 0; i < input_count; i++) {
                    for (int j = 0; j < hidden_count; j++) {
                        first_w[i][j] += (hidden[j] * (1 - hidden[j]) * differential * second_w[j][k] * input_collection[all][i]);
                    }
                }
                for (int i = 0; i < second_w.length; i++) {
                    second_w[i][k] += (differential * hidden[i]);
                }
            }

        }
    }


    private AsyncTask asyncTask = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {
            for (int traning = 0; traning < 10000000; traning++) {
                Traning();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            for (int i = 0; i < out.length; i++) {
                for (int j = 0; j < out[i].length; j++) {
                    Log.e("test", String.valueOf(out[i][j]));
                }
            }
        }
    };
}
