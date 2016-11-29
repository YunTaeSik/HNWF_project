package com.ai.project.hnwf_project.traing;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ai.project.hnwf_project.data.SaJoData;
import com.ai.project.hnwf_project.db.DBManager;
import com.ai.project.hnwf_project.util.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Math.exp;

/**
 * Created by YunTaeSik on 2016-11-28.
 */
public class TrainingService extends Service {
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

    private DBManager dbManager;
    private Cursor cursor;
    private SQLiteDatabase db;
    private String jsonArray;
    private String cursor_id;
    private ArrayList<ArrayList<Double>> get_first_wList = new ArrayList();
    private ArrayList<ArrayList<Double>> get_second_wList = new ArrayList();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getDB();
        Get_weight(); // 가중치 얻기
        SaveWeight(); //DB 저장
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean stopService(Intent name) {
        Log.e("stopService", "stopService");

        return super.stopService(name);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
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

    private void getDB() {
        dbManager = new DBManager(getApplicationContext(), "WEIGHT", null, 1);
        db = dbManager.getWritableDatabase();
        db.execSQL("CREATE TABLE if not exists '" + Contact.WEIGHT + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");
        cursor = db.query("'" + Contact.WEIGHT + "'", null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            Log.e("test", "DB있다");
            cursor.moveToNext();
            cursor_id = cursor.getString(0);
            jsonArray = cursor.getString(1);
            JSONObject jsnobject = null;
            try {
                jsnobject = new JSONObject(jsonArray);
                JSONArray first_wList = jsnobject.getJSONArray("first_wList");
                get_first_wList = new ArrayList();
                ArrayList<Double> get_first_wList_item = new ArrayList();
                for (int i = 0; i < first_wList.length(); i++) {
                    for (int j = 0; j < first_wList.getJSONArray(i).length(); j++) {
                        get_first_wList_item.add(first_wList.getJSONArray(i).getDouble(j));
                    }
                    get_first_wList.add(get_first_wList_item);
                }
                JSONArray second_wList = jsnobject.getJSONArray("second_wList");
                get_second_wList = new ArrayList();
                ArrayList<Double> get_second_wList_item = new ArrayList();
                for (int i = 0; i < second_wList.length(); i++) {
                    for (int j = 0; j < second_wList.getJSONArray(i).length(); j++) {
                        get_second_wList_item.add(second_wList.getJSONArray(i).getDouble(j));
                    }
                    get_second_wList.add(get_second_wList_item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("get_first_wList", String.valueOf(get_first_wList.size()));
            for (int i = 0; i < get_first_wList.size(); i++) {
                for (int j = 0; j < get_first_wList.get(i).size(); j++) {
                    first_w[i][j] = get_first_wList.get(i).get(j).doubleValue();
                }
                Log.e("get_first_wList_item", String.valueOf(get_first_wList.get(i).size()));
            }
            for (int i = 0; i < get_second_wList.size(); i++) {
                for (int j = 0; j < get_second_wList.get(i).size(); j++) {
                    second_w[i][j] = get_second_wList.get(i).get(j).doubleValue();
                }
            }
        } else {
            Log.e("test", "DB없다");
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
            Get_weight();
            JSONObject json = new JSONObject();
            try {
                json.put("first_wList", new JSONArray(get_first_wList));
                json.put("second_wList", new JSONArray(get_second_wList));
                jsonArray = json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ContentValues values = new ContentValues();
            values.put("json", jsonArray);
            db.insert("'" + Contact.WEIGHT + "'", null, values);
        }

        for (int i = 0; i < 1000; i++) {
            Log.e("Traning", String.valueOf(i));
            Traning();
        }
    }

    private void Get_weight() {
        get_first_wList.clear();
        get_second_wList.clear();
        for (int i = 0; i < input_count; i++) {
            ArrayList<Double> doubles = new ArrayList<>();
            for (int j = 0; j < hidden_count; j++) {
                doubles.add(first_w[i][j]);
            }
            get_first_wList.add(doubles);
        }
        for (int i = 0; i < hidden_count; i++) {
            ArrayList<Double> doubles = new ArrayList<>();
            for (int j = 0; j < out_count; j++) {
                doubles.add(second_w[i][j]);
            }
            get_second_wList.add(doubles);
        }
    }

    private void SaveWeight() {
        JSONObject json = new JSONObject();
        try {
            json.put("first_wList", new JSONArray(get_first_wList));
            json.put("second_wList", new JSONArray(get_second_wList));
            jsonArray = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ContentValues values = new ContentValues();
        values.put("json", jsonArray);
        db.update("'" + Contact.WEIGHT + "'", values, "_id=?", new String[]{cursor_id});
        db.close();
    }
/*

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
    };*/
}
