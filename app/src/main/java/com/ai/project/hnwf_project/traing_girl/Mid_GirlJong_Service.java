package com.ai.project.hnwf_project.traing_girl;

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

import static java.lang.Math.exp;

/**
 * Created by sky87 on 2016-12-13.
 */

public class Mid_GirlJong_Service extends Service {
    private int input_count = 8;
    private int hidden_count = 20;
    private int out_count = 1;
    private double n = 0.05;


    private double first_w[][] = new double[input_count][hidden_count];
    private double second_w[][] = new double[hidden_count][out_count];

    private double input_collection_girl[][] = {
            SaJoData.girl_Input_1, SaJoData.girl_Input_2, SaJoData.girl_Input_3, SaJoData.girl_Input_4, SaJoData.girl_Input_5, SaJoData.girl_Input_6, SaJoData.girl_Input_7, SaJoData.girl_Input_8,
            SaJoData.girl_Input_9, SaJoData.girl_Input_10, SaJoData.girl_Input_11, SaJoData.girl_Input_12, SaJoData.girl_Input_13, SaJoData.girl_Input_14, SaJoData.girl_Input_15, SaJoData.girl_Input_16,
            SaJoData.girl_Input_17, SaJoData.girl_Input_18, SaJoData.girl_Input_19, SaJoData.girl_Input_20, SaJoData.girl_Input_21, SaJoData.girl_Input_22, SaJoData.girl_Input_23, SaJoData.girl_Input_24,
            SaJoData.girl_Input_25, SaJoData.girl_Input_26, SaJoData.girl_Input_27, SaJoData.girl_Input_28, SaJoData.girl_Input_29, SaJoData.girl_Input_30, SaJoData.girl_Input_31, SaJoData.girl_Input_32,
            SaJoData.girl_Input_33, SaJoData.girl_Input_34, SaJoData.girl_Input_35, SaJoData.girl_Input_36, SaJoData.girl_Input_37, SaJoData.girl_Input_38, SaJoData.girl_Input_39, SaJoData.girl_Input_40,
            SaJoData.girl_Input_41, SaJoData.girl_Input_42, SaJoData.girl_Input_43, SaJoData.girl_Input_44, SaJoData.girl_Input_45, SaJoData.girl_Input_46, SaJoData.girl_Input_47, SaJoData.girl_Input_48,
            SaJoData.girl_Input_49, SaJoData.girl_Input_50, SaJoData.girl_Input_51, SaJoData.girl_Input_52, SaJoData.girl_Input_53, SaJoData.girl_Input_54, SaJoData.girl_Input_55, SaJoData.girl_Input_56,
            SaJoData.girl_Input_57, SaJoData.girl_Input_58, SaJoData.girl_Input_59, SaJoData.girl_Input_60, SaJoData.girl_Input_61, SaJoData.girl_Input_62, SaJoData.girl_Input_63, SaJoData.girl_Input_64,
            SaJoData.girl_Input_65, SaJoData.girl_Input_66, SaJoData.girl_Input_67, SaJoData.girl_Input_68, SaJoData.girl_Input_69, SaJoData.girl_Input_70, SaJoData.girl_Input_71, SaJoData.girl_Input_72,
            SaJoData.girl_Input_73, SaJoData.girl_Input_74, SaJoData.girl_Input_75, SaJoData.girl_Input_76, SaJoData.girl_Input_77, SaJoData.girl_Input_78, SaJoData.girl_Input_79, SaJoData.girl_Input_80,
            SaJoData.girl_Input_81, SaJoData.girl_Input_82, SaJoData.girl_Input_83, SaJoData.girl_Input_84, SaJoData.girl_Input_85, SaJoData.girl_Input_86, SaJoData.girl_Input_87, SaJoData.girl_Input_88,
            SaJoData.girl_Input_89, SaJoData.girl_Input_90, SaJoData.girl_Input_91, SaJoData.girl_Input_92, SaJoData.girl_Input_93, SaJoData.girl_Input_94, SaJoData.girl_Input_95, SaJoData.girl_Input_96,
            SaJoData.girl_Input_97, SaJoData.girl_Input_98, SaJoData.girl_Input_99, SaJoData.girl_Input_100, SaJoData.girl_Input_101, SaJoData.girl_Input_102, SaJoData.girl_Input_103, SaJoData.girl_Input_104,
            SaJoData.girl_Input_105, SaJoData.girl_Input_106, SaJoData.girl_Input_107, SaJoData.girl_Input_108, SaJoData.girl_Input_109, SaJoData.girl_Input_110, SaJoData.girl_Input_111, SaJoData.girl_Input_112,
            SaJoData.girl_Input_113, SaJoData.girl_Input_114, SaJoData.girl_Input_115, SaJoData.girl_Input_116, SaJoData.girl_Input_117, SaJoData.girl_Input_118, SaJoData.girl_Input_119, SaJoData.girl_Input_120,
            SaJoData.girl_Input_121, SaJoData.girl_Input_122, SaJoData.girl_Input_123, SaJoData.girl_Input_124, SaJoData.girl_Input_125, SaJoData.girl_Input_126, SaJoData.girl_Input_127, SaJoData.girl_Input_128,
            SaJoData.girl_Input_129, SaJoData.girl_Input_130, SaJoData.girl_Input_131, SaJoData.girl_Input_132, SaJoData.girl_Input_133, SaJoData.girl_Input_134, SaJoData.girl_Input_135, SaJoData.girl_Input_136,
            SaJoData.girl_Input_137, SaJoData.girl_Input_138, SaJoData.girl_Input_139, SaJoData.girl_Input_140, SaJoData.girl_Input_141, SaJoData.girl_Input_142, SaJoData.girl_Input_143, SaJoData.girl_Input_144,
            SaJoData.girl_Input_145, SaJoData.girl_Input_146, SaJoData.girl_Input_147, SaJoData.girl_Input_148, SaJoData.girl_Input_149, SaJoData.girl_Input_150, SaJoData.girl_Input_151, SaJoData.girl_Input_152,
            SaJoData.girl_Input_153, SaJoData.girl_Input_154, SaJoData.girl_Input_155, SaJoData.girl_Input_156, SaJoData.girl_Input_157, SaJoData.girl_Input_158, SaJoData.girl_Input_159, SaJoData.girl_Input_160,
            SaJoData.girl_Input_161, SaJoData.girl_Input_162, SaJoData.girl_Input_163, SaJoData.girl_Input_164, SaJoData.girl_Input_165, SaJoData.girl_Input_166,SaJoData.girl_Input_167, SaJoData.girl_Input_168,
            SaJoData.girl_Input_169, SaJoData.girl_Input_170, SaJoData.girl_Input_171, SaJoData.girl_Input_172};

    private double hidden[] = new double[hidden_count];

    private double out[][] = new double[input_collection_girl.length][out_count];
    private double target[][] = SaJoData.girl_target_one;
    private DBManager dbManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dbManager = new DBManager(getApplicationContext(), "WEIGHT", null, 1);

        SQLiteDatabase db = dbManager.getWritableDatabase();
        Cursor cursor = db.query("'" + Contact.MID_GIRL_WEIGHT_JONG + "'", null, null, null, null, null, null);
        if (cursor.getCount() < 1) {
            Set_weight();
            asyncTask asyncTask = new asyncTask();
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // 남자 가운데 글자
        } else {
            sendBroadcast(new Intent(Contact.WEIGHT_TRAING));
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void Set_weight() { //초기 가중치
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

    private void Traning_ManOne() {
        for (int all = 0; all < input_collection_girl.length; all++) {
            for (int k = 0; k < hidden_count - 1; k++) {
                double sum = 0;
                for (int i = 0; i < input_count; i++) {
                    sum += input_collection_girl[all][i] * first_w[i][k];
                }
                hidden[k] = 1 / (1 + exp(-sum));
            }
            hidden[hidden_count - 1] = -1;
            for (int k = 0; k < out_count; k++) {
                double out_sum = 0;
                for (int i = 0; i < hidden_count; i++) {
                    out_sum += hidden[i] * second_w[i][k];
                }
                out[all][k] = 1 / (1 + exp(-out_sum));
            }
            for (int k = 0; k < out_count; k++) {
                double differential = (n * (target[all][2] - out[all][k]) * (1 - out[all][k]) * out[all][k]);

                for (int i = 0; i < input_count; i++) {
                    for (int j = 0; j < hidden_count; j++) {
                        first_w[i][j] += (hidden[j] * (1 - hidden[j]) * differential * second_w[j][k] * input_collection_girl[all][i]);
                    }
                }
                for (int i = 0; i < second_w.length; i++) {
                    second_w[i][k] += (differential * hidden[i]);
                }
            }

        }
    }


    private class asyncTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            for (int traning = 0; traning < 500000; traning++) {
                Traning_ManOne();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer o) {
            super.onPostExecute(o);
            String sumHangle = "";
            for (int i = 0; i < out.length; i++) {
                for (int j = 0; j < out[i].length; j++) {
                    sumHangle += GetHangle.Girl_JongSung_String[(int) GetNearValue.getNearJONG(out[i][j])];
                }
            }
            Log.e("가운데 여자 종성", sumHangle);
            SQLiteDatabase db = dbManager.getWritableDatabase();
            JSONObject json = new JSONObject();
            String jsonArray = null;
            try {
                json.put("first_w", new JSONArray(first_w));
                json.put("second_w", new JSONArray(second_w));
                jsonArray = json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ContentValues values = new ContentValues();
            values.put("json", jsonArray);
            db.insert("'" + Contact.MID_GIRL_WEIGHT_JONG + "'", null, values);
            db.close();

            sendBroadcast(new Intent(Contact.WEIGHT_TRAING));
        }
    }

    ;
}