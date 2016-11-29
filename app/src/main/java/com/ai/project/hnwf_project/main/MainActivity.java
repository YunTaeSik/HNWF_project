package com.ai.project.hnwf_project.main;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.ai.project.hnwf_project.util.Contact;
import com.ai.project.hnwf_project.util.GetHangle;
import com.ai.project.hnwf_project.util.GetNearValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Math.exp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText name_input;
    private Button succes_btn;

    private int input_count = 7;
    private int hidden_count = 7;
    private int out_count = 3;

    private double first_w_man_one[][] = new double[input_count][hidden_count];
    private double first_w_man_two[][] = new double[input_count][hidden_count];
    private double second_w_man_one[][] = new double[hidden_count][out_count];
    private double second_w_man_two[][] = new double[hidden_count][out_count];
    private double input_collection_man[][] = {SaJoData.man_Input_1, SaJoData.man_Input_2, SaJoData.man_Input_3, SaJoData.man_Input_4};

    private double hidden_man[] = new double[hidden_count];
    private double out_man_one[][] = new double[input_collection_man.length][out_count];
    private double out_man_two[][] = new double[input_collection_man.length][out_count];
    private double target_man_one[][] = SaJoData.man_target_one;
    private double target_man_two[][] = SaJoData.man_target_two;

    private ArrayList<ArrayList<Double>> first_wList_MANOne = new ArrayList();
    private ArrayList<ArrayList<Double>> second_wList_MANOne = new ArrayList();

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
        name_input = (EditText) findViewById(R.id.name_input);
        succes_btn = (Button) findViewById(R.id.succes_btn);

        dbManager = new DBManager(getApplicationContext(), "WEIGHT", null, 1);
        db_man = dbManager.getWritableDatabase();
/*
        db_man.execSQL("CREATE TABLE if not exists '" + Contact.MAN_WEIGHT_ONE + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");
        cursor_man = db_man.query("'" + Contact.MAN_WEIGHT_ONE + "'", null, null, null, null, null, null);
        if (cursor_man.getCount() > 0) {
            startService(new Intent(this, ManTrainingONEService.class));
        } else {  //처음 DB가없을때*/
        Set_weight_ManOne();
        asyncTask_Man_One.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        asyncTask_Man_Two.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //  }
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
/*
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
    }*/

    private void Traning_ManOne() {
        for (int all = 0; all < input_collection_man.length; all++) {
            for (int k = 0; k < hidden_count; k++) {
                double sum = 0;
                for (int i = 0; i < input_count; i++) {
                    sum += input_collection_man[all][i] * first_w_man_one[i][k];
                }
                hidden_man[k] = 1 / (1 + exp(-sum));
            }
            for (int k = 0; k < out_count; k++) {
                double out_sum = 0;
                for (int i = 0; i < hidden_count; i++) {
                    out_sum += hidden_man[i] * second_w_man_one[i][k];
                }
                out_man_one[all][k] = 1 / (1 + exp(-out_sum));
            }
            for (int k = 0; k < out_count; k++) {
                double differential = (n * (target_man_one[all][k] - out_man_one[all][k]) * (1 - out_man_one[all][k]) * out_man_one[all][k]);

                for (int i = 0; i < input_count; i++) {
                    for (int j = 0; j < hidden_count; j++) {
                        first_w_man_one[i][j] += (hidden_man[j] * (1 - hidden_man[j]) * differential * second_w_man_one[j][k] * input_collection_man[all][i]);
                    }
                }
                for (int i = 0; i < second_w_man_one.length; i++) {
                    second_w_man_one[i][k] += (differential * hidden_man[i]);
                }
            }

        }
    }

    private void Set_weight_ManOne() {
        for (int i = 0; i < input_count; i++) {
            for (int j = 0; j < hidden_count; j++) {
                first_w_man_one[i][j] = Math.random();
            }

        }
        for (int i = 0; i < hidden_count; i++) {
            for (int j = 0; j < out_count; j++) {
                second_w_man_one[i][j] = Math.random();
            }
        }
        for (int i = 0; i < input_count; i++) {
            for (int j = 0; j < hidden_count; j++) {
                first_w_man_two[i][j] = Math.random();
            }

        }
        for (int i = 0; i < hidden_count; i++) {
            for (int j = 0; j < out_count; j++) {
                second_w_man_two[i][j] = Math.random();
            }
        }
    }

    private void Get_weight_ManOne() {
        for (int i = 0; i < input_count; i++) {
            ArrayList<Double> doubles = new ArrayList<>();
            for (int j = 0; j < hidden_count; j++) {
                doubles.add(first_w_man_one[i][j]);
            }
            first_wList_MANOne.add(doubles);
        }
        for (int i = 0; i < hidden_count; i++) {
            ArrayList<Double> doubles = new ArrayList<>();
            for (int j = 0; j < out_count; j++) {
                doubles.add(second_w_man_one[i][j]);
            }
            second_wList_MANOne.add(doubles);
        }
    }

    private AsyncTask asyncTask_Man_One = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {
            for (int traning = 0; traning < 50000; traning++) {
                Log.e("traing", String.valueOf(traning));
                Traning_ManOne();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Get_weight_ManOne();
            JSONObject json = new JSONObject();
            try {
                json.put("first_wList", new JSONArray(first_wList_MANOne));
                json.put("second_wList", new JSONArray(second_wList_MANOne));
                jsonArray_man = json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ContentValues values = new ContentValues();
            values.put("json", jsonArray_man);
            db_man.insert("'" + Contact.MAN_WEIGHT_ONE + "'", null, values);
            String sumHangle = "";
            for (int i = 0; i < out_man_one.length; i++) {
                for (int j = 0; j < out_man_one[i].length; j++) {
                    Log.e("test", String.valueOf(out_man_one[i][j]));
                    if (j == 0 || j == 3) {
                        Log.e("초성", String.valueOf(GetNearValue.getNearCHO(out_man_one[i][j])));
                        sumHangle += GetHangle.Man_ChoSung_String[(int) GetNearValue.getNearCHO(out_man_one[i][j])];
                        //Log.e("초성", String.valueOf(out[i][j] * 12));
                    } else if (j == 1 || j == 4) {
                        Log.e("중성", String.valueOf(GetNearValue.getNearJUNG(out_man_one[i][j])));
                        sumHangle += GetHangle.Man_JungSung_String[(int) GetNearValue.getNearJUNG(out_man_one[i][j])];
                    } else if (j == 2 || j == 5) {
                        Log.e("종성", String.valueOf(GetNearValue.getNearJONG(out_man_one[i][j])));
                        sumHangle += GetHangle.Man_JongSung_String[(int) GetNearValue.getNearJONG(out_man_one[i][j])];
                    }
                }
            }
            Log.e("완성 첫글자", sumHangle);
        }
    };

    private void Traning_ManTwo() {
        for (int all = 0; all < input_collection_man.length; all++) {
            for (int k = 0; k < hidden_count; k++) {
                double sum = 0;
                for (int i = 0; i < input_count; i++) {
                    sum += input_collection_man[all][i] * first_w_man_two[i][k];
                }
                hidden_man[k] = 1 / (1 + exp(-sum));
            }
            for (int k = 0; k < out_count; k++) {
                double out_sum = 0;
                for (int i = 0; i < hidden_count; i++) {
                    out_sum += hidden_man[i] * second_w_man_two[i][k];
                }
                out_man_two[all][k] = 1 / (1 + exp(-out_sum));
            }
            for (int k = 0; k < out_count; k++) {
                double differential = (n * (target_man_two[all][k] - out_man_two[all][k]) * (1 - out_man_two[all][k]) * out_man_two[all][k]);

                for (int i = 0; i < input_count; i++) {
                    for (int j = 0; j < hidden_count; j++) {
                        first_w_man_two[i][j] += (hidden_man[j] * (1 - hidden_man[j]) * differential * second_w_man_two[j][k] * input_collection_man[all][i]);
                    }
                }
                for (int i = 0; i < second_w_man_two.length; i++) {
                    second_w_man_two[i][k] += (differential * hidden_man[i]);
                }
            }

        }
    }

    private AsyncTask asyncTask_Man_Two = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {
            for (int traning = 0; traning < 50000; traning++) {
                Log.e("traing", String.valueOf(traning));
                Traning_ManTwo();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            String sumHangle = "";
            for (int i = 0; i < out_man_two.length; i++) {
                for (int j = 0; j < out_man_two[i].length; j++) {
                    Log.e("test", String.valueOf(out_man_two[i][j]));
                    if (j == 0 || j == 3) {
                        Log.e("초성", String.valueOf(GetNearValue.getNearCHO(out_man_two[i][j])));
                        sumHangle += GetHangle.Man_ChoSung_String[(int) GetNearValue.getNearCHO(out_man_two[i][j])];
                    } else if (j == 1 || j == 4) {
                        Log.e("중성", String.valueOf(GetNearValue.getNearJUNG(out_man_two[i][j])));
                        sumHangle += GetHangle.Man_JungSung_String[(int) GetNearValue.getNearJUNG(out_man_two[i][j])];
                    } else if (j == 2 || j == 5) {
                        Log.e("종성", String.valueOf(GetNearValue.getNearJONG(out_man_two[i][j])));
                        sumHangle += GetHangle.Man_JongSung_String[(int) GetNearValue.getNearJONG(out_man_two[i][j])];
                    }
                }
            }
            Log.e("완성=두번째글자", sumHangle);
        }
    };

}
