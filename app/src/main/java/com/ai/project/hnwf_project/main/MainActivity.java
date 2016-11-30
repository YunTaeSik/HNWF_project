package com.ai.project.hnwf_project.main;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ai.project.hnwf_project.R;
import com.ai.project.hnwf_project.db.DBManager;
import com.ai.project.hnwf_project.util.GetHangle;

import static java.lang.Math.exp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int input_count = 7;
    private int hidden_count = 7;
    private int out_count = 3;

    private double first_w_man_one[][] = new double[input_count][hidden_count];
    private double first_w_man_two[][] = new double[input_count][hidden_count];

    private double first_w_girl_one[][] = new double[input_count][hidden_count]; //여자 가운데 글자 가중치
    private double first_w_girl_two[][] = new double[input_count][hidden_count];//여자 마지막 글자 기증치

    private double second_w_man_one[][] = new double[hidden_count][out_count];
    private double second_w_man_two[][] = new double[hidden_count][out_count];

    private double second_w_girl_one[][] = new double[hidden_count][out_count];
    private double second_w_girl_two[][] = new double[hidden_count][out_count];

    private double hidden_man_one[] = new double[hidden_count];
    private double hidden_man_two[] = new double[hidden_count];
    private double hidden_girl_one[] = new double[hidden_count];
    private double hidden_girl_two[] = new double[hidden_count];


    private double out_man_one[] = new double[out_count];
    private double out_man_two[] = new double[out_count];

    private double out_girl_one[] = new double[out_count];
    private double out_girl_two[] = new double[out_count];


    private DBManager dbManager;
    private Cursor cursor_man;
    private SQLiteDatabase db_man;
    private String jsonArray_man;

  /*  private double first_w_gril[][] = new double[input_count][hidden_count];
    private double second_w_gril[][] = new double[hidden_count][out_count];
    private double input_collection_gril[][] = {SaJoData.Input_1, SaJoData.Input_2, SaJoData.Input_3, SaJoData.Input_4};*/

    private double n = 0.05;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(getApplicationContext(), "WEIGHT", null, 1);
        db_man = dbManager.getWritableDatabase();

        //startService(new Intent(this, TraningService.class));
        hangulToJaso("안");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.succes_btn:
                break;
        }
    }

    private void SetNameing_Man(double input[]) {
        for (int k = 0; k < hidden_count; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_man_one[i][k];
            }
            hidden_man_one[k] = 1 / (1 + exp(-sum));
        }
        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_man_one[i] * second_w_man_one[i][k];
            }
            out_man_one[k] = 1 / (1 + exp(-out_sum)); //남자 가운데 이름
        }

        for (int k = 0; k < hidden_count; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_man_two[i][k];
            }
            hidden_man_two[k] = 1 / (1 + exp(-sum));
        }
        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_man_two[i] * second_w_man_two[i][k];
            }
            out_man_two[k] = 1 / (1 + exp(-out_sum)); //남자 마지막 이름
        }
    }

    private void SetNameing_Girl(double input[]) {
        for (int k = 0; k < hidden_count; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_girl_one[i][k];
            }
            hidden_girl_one[k] = 1 / (1 + exp(-sum));
        }
        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_girl_one[i] * second_w_girl_one[i][k];
            }
            out_girl_one[k] = 1 / (1 + exp(-out_sum)); //남자 가운데 이름
        }

        for (int k = 0; k < hidden_count; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_girl_two[i][k];
            }
            hidden_girl_two[k] = 1 / (1 + exp(-sum));
        }
        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_girl_two[i] * second_w_girl_two[i][k];
            }
            out_girl_two[k] = 1 / (1 + exp(-out_sum)); //남자 마지막 이름
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
                result = result + GetHangle.ChoSung[a] + GetHangle.JungSung[b];
                if (c != 0)
                    result = result + GetHangle.JongSung[c]; // c가 0이 아니면, 즉 받침이 있으면
            } else {
                result = result + ch;
            }
        }
        Log.e("result", result);
        return result;
    }
}
