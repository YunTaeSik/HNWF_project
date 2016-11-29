package com.ai.project.hnwf_project.traing;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

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

/**
 * Created by sky87 on 2016-11-29.
 */

public class ManTrainingTWOService extends Service {
    private int input_count = 7;
    private int hidden_count = 7;
    private int out_count = 3;
    private double first_w[][] = new double[input_count][hidden_count];
    private double second_w[][] = new double[hidden_count][out_count];
    private double input_collection[][] = {SaJoData.man_Input_1, SaJoData.man_Input_2, SaJoData.man_Input_3, SaJoData.man_Input_4};
    private double hidden[] = new double[hidden_count];
    private double out[][] = new double[input_collection.length][out_count];
    private double target[][] = SaJoData.man_target_one;
    private double n = 0.1;

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
        getDB_Traing_Save();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean stopService(Intent name) {
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

    private void getDB_Traing_Save() {
        dbManager = new DBManager(getApplicationContext(), "WEIGHT", null, 1);
        db = dbManager.getWritableDatabase();
        db.execSQL("CREATE TABLE if not exists '" + Contact.MAN_WEIGHT_ONE + "'( _id INTEGER PRIMARY KEY AUTOINCREMENT, json TEXT);");
        cursor = db.query("'" + Contact.MAN_WEIGHT_ONE + "'", null, null, null, null, null, null);
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
            for (int i = 0; i < input_count; i++) {
                for (int j = 0; j < hidden_count; j++) {
                    first_w[i][j] = get_first_wList.get(i).get(j).doubleValue();
                }
            }
            for (int i = 0; i < hidden_count; i++) {
                for (int j = 0; j < out_count; j++) {
                    second_w[i][j] = get_second_wList.get(i).get(j).doubleValue();
                }
            }
        }
        asyncTask asyncTask = new asyncTask();
        asyncTask.execute();
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
        db.update("'" + Contact.MAN_WEIGHT_ONE + "'", values, "_id=?", new String[]{cursor_id});
        db.close();
    }

    private class asyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            for (int i = 0; i < 10000; i++) {
                Log.e("Traning", String.valueOf(i));
                Traning();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Get_weight();
            SaveWeight();
            String sumHangle = "";
            for (int i = 0; i < out.length; i++) {
                for (int j = 0; j < out[i].length; j++) {
                    Log.e("test", String.valueOf(out[i][j]));
                    if (j == 0 || j == 3) {
                        Log.e("초성", String.valueOf(GetNearValue.getNearCHO(out[i][j])));
                        sumHangle += GetHangle.Man_ChoSung_String[(int) GetNearValue.getNearCHO(out[i][j])];
                    } else if (j == 1 || j == 4) {
                        Log.e("중성", String.valueOf(GetNearValue.getNearJUNG(out[i][j])));
                        sumHangle += GetHangle.Man_JungSung_String[(int) GetNearValue.getNearJUNG(out[i][j])];
                    } else if (j == 2 || j == 5) {
                        Log.e("종성", String.valueOf(GetNearValue.getNearJONG(out[i][j])));
                        sumHangle += GetHangle.Man_JongSung_String[(int) GetNearValue.getNearJONG(out[i][j])];
                    }
                }
            }
            Log.e("완성", sumHangle);
        }
    }

    ;
}