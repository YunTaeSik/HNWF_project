package com.ai.project.hnwf_project.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ai.project.hnwf_project.R;
import com.ai.project.hnwf_project.data.SaJoData;
import com.ai.project.hnwf_project.db.DBManager;
import com.ai.project.hnwf_project.traing.TrainingService;
import com.ai.project.hnwf_project.util.GetHangle;
import com.ai.project.hnwf_project.util.GetNearValue;

import java.util.ArrayList;

import static java.lang.Math.exp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText name_input;
    private Button succes_btn;

    private int input_count = 8;
    private int hidden_count = 8;
    private int out_count = 6;
    private double first_w[][] = new double[input_count][hidden_count];
    private double second_w[][] = new double[hidden_count][out_count];
    private double input_collection[][] = {SaJoData.Input_1, SaJoData.Input_2, SaJoData.Input_3, SaJoData.Input_4};
    private double hidden[] = new double[hidden_count];
    private double out[][] = new double[input_collection.length][out_count];
    private double target[][] = SaJoData.target;
    private double n = 0.005;

    private String sumHangle = "";

    private ArrayList<ArrayList<Double>> first_wList = new ArrayList();
    private ArrayList<ArrayList<Double>> second_wList = new ArrayList();
    private DBManager dbManager;
    private String jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name_input = (EditText) findViewById(R.id.name_input);
        succes_btn = (Button) findViewById(R.id.succes_btn);
        startService(new Intent(this, TrainingService.class));
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, TrainingService.class));
        super.onDestroy();
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

    private void Get_weight() {
        for (int i = 0; i < input_count; i++) {
            ArrayList<Double> doubles = new ArrayList<>();
            for (int j = 0; j < hidden_count; j++) {
                doubles.add(first_w[i][j]);
            }
            first_wList.add(doubles);
        }
        for (int i = 0; i < hidden_count; i++) {
            ArrayList<Double> doubles = new ArrayList<>();
            for (int j = 0; j < out_count; j++) {
                doubles.add(second_w[i][j]);
            }
            second_wList.add(doubles);
        }
        Log.e("list_Test", first_wList.toString());
        Log.e("list_Test", second_wList.toString());
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
            for (int traning = 0; traning < 1000; traning++) {
                Log.e("traing", String.valueOf(traning));
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
                    if (j == 0 || j == 3) {
                        Log.e("초성", String.valueOf(GetNearValue.getNearCHO(out[i][j])));
                        sumHangle += GetHangle.ChoSung_String[(int) GetNearValue.getNearCHO(out[i][j])];
                        //Log.e("초성", String.valueOf(out[i][j] * 12));
                    } else if (j == 1 || j == 4) {
                        Log.e("중성", String.valueOf(GetNearValue.getNearJUNG(out[i][j])));
                        sumHangle += GetHangle.JungSung_String[(int) GetNearValue.getNearJUNG(out[i][j])];
                    } else if (j == 2 || j == 5) {
                        Log.e("종성", String.valueOf(GetNearValue.getNearJONG(out[i][j])));
                        sumHangle += GetHangle.JongSung_String[(int) GetNearValue.getNearJONG(out[i][j])];
                    }
                }
            }
            Log.e("완성", sumHangle);
        }
    };

}
