package com.ai.project.hnwf_project.traing_man;

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

public class Mid_ManJong_Service extends Service {
    private int input_count = 8;
    private int hidden_count = 20;
    private int out_count = 1;
    private double n = 0.05;


    private double first_w[][] = new double[input_count][hidden_count];
    private double second_w[][] = new double[hidden_count][out_count];

    private double input_collection_man[][] = {
            SaJoData.man_Input_1, SaJoData.man_Input_2, SaJoData.man_Input_3, SaJoData.man_Input_4, SaJoData.man_Input_5, SaJoData.man_Input_6, SaJoData.man_Input_7, SaJoData.man_Input_8,
            SaJoData.man_Input_9, SaJoData.man_Input_10, SaJoData.man_Input_11, SaJoData.man_Input_12, SaJoData.man_Input_13, SaJoData.man_Input_14, SaJoData.man_Input_15, SaJoData.man_Input_16,
            SaJoData.man_Input_17, SaJoData.man_Input_18, SaJoData.man_Input_19, SaJoData.man_Input_20, SaJoData.man_Input_21, SaJoData.man_Input_22, SaJoData.man_Input_23, SaJoData.man_Input_24,
            SaJoData.man_Input_25, SaJoData.man_Input_26, SaJoData.man_Input_27, SaJoData.man_Input_28, SaJoData.man_Input_29, SaJoData.man_Input_30, SaJoData.man_Input_31, SaJoData.man_Input_32,
            SaJoData.man_Input_33, SaJoData.man_Input_34, SaJoData.man_Input_35, SaJoData.man_Input_36, SaJoData.man_Input_37, SaJoData.man_Input_38, SaJoData.man_Input_39, SaJoData.man_Input_40,
            SaJoData.man_Input_41, SaJoData.man_Input_42, SaJoData.man_Input_43, SaJoData.man_Input_44, SaJoData.man_Input_45, SaJoData.man_Input_46, SaJoData.man_Input_47, SaJoData.man_Input_48,
            SaJoData.man_Input_49, SaJoData.man_Input_50, SaJoData.man_Input_51, SaJoData.man_Input_52, SaJoData.man_Input_53, SaJoData.man_Input_54, SaJoData.man_Input_56, SaJoData.man_Input_57,
            SaJoData.man_Input_58, SaJoData.man_Input_59, SaJoData.man_Input_60, SaJoData.man_Input_61, SaJoData.man_Input_62, SaJoData.man_Input_63, SaJoData.man_Input_64, SaJoData.man_Input_65,
            SaJoData.man_Input_67, SaJoData.man_Input_68, SaJoData.man_Input_69, SaJoData.man_Input_70, SaJoData.man_Input_71, SaJoData.man_Input_72, SaJoData.man_Input_73, SaJoData.man_Input_74,
            SaJoData.man_Input_75, SaJoData.man_Input_76, SaJoData.man_Input_77, SaJoData.man_Input_78, SaJoData.man_Input_79, SaJoData.man_Input_80, SaJoData.man_Input_81, SaJoData.man_Input_82,
            SaJoData.man_Input_83, SaJoData.man_Input_84, SaJoData.man_Input_85, SaJoData.man_Input_86, SaJoData.man_Input_87, SaJoData.man_Input_88, SaJoData.man_Input_89, SaJoData.man_Input_90,
            SaJoData.man_Input_91, SaJoData.man_Input_92, SaJoData.man_Input_93, SaJoData.man_Input_94, SaJoData.man_Input_95, SaJoData.man_Input_96, SaJoData.man_Input_97, SaJoData.man_Input_98,
            SaJoData.man_Input_99, SaJoData.man_Input_100, SaJoData.man_Input_101, SaJoData.man_Input_102, SaJoData.man_Input_103, SaJoData.man_Input_104, SaJoData.man_Input_105, SaJoData.man_Input_106,
            SaJoData.man_Input_107, SaJoData.man_Input_108, SaJoData.man_Input_109, SaJoData.man_Input_110, SaJoData.man_Input_111, SaJoData.man_Input_112, SaJoData.man_Input_113, SaJoData.man_Input_114,
            SaJoData.man_Input_115, SaJoData.man_Input_116, SaJoData.man_Input_117, SaJoData.man_Input_118, SaJoData.man_Input_119, SaJoData.man_Input_120, SaJoData.man_Input_121, SaJoData.man_Input_122,
            SaJoData.man_Input_123, SaJoData.man_Input_124, SaJoData.man_Input_125, SaJoData.man_Input_126, SaJoData.man_Input_127, SaJoData.man_Input_128, SaJoData.man_Input_129, SaJoData.man_Input_130,
            SaJoData.man_Input_131, SaJoData.man_Input_132, SaJoData.man_Input_133, SaJoData.man_Input_134, SaJoData.man_Input_135, SaJoData.man_Input_136, SaJoData.man_Input_137, SaJoData.man_Input_138,
            SaJoData.man_Input_139, SaJoData.man_Input_140, SaJoData.man_Input_141, SaJoData.man_Input_142, SaJoData.man_Input_143, SaJoData.man_Input_144, SaJoData.man_Input_145, SaJoData.man_Input_146,
            SaJoData.man_Input_147, SaJoData.man_Input_148, SaJoData.man_Input_149, SaJoData.man_Input_150, SaJoData.man_Input_151, SaJoData.man_Input_152, SaJoData.man_Input_153, SaJoData.man_Input_154,
            SaJoData.man_Input_155, SaJoData.man_Input_156, SaJoData.man_Input_157, SaJoData.man_Input_158, SaJoData.man_Input_159, SaJoData.man_Input_160, SaJoData.man_Input_161, SaJoData.man_Input_162,
            SaJoData.man_Input_163, SaJoData.man_Input_164, SaJoData.man_Input_165, SaJoData.man_Input_166, SaJoData.man_Input_167, SaJoData.man_Input_168, SaJoData.man_Input_169, SaJoData.man_Input_170,
            SaJoData.man_Input_171, SaJoData.man_Input_172, SaJoData.man_Input_173, SaJoData.man_Input_174, SaJoData.man_Input_175, SaJoData.man_Input_176, SaJoData.man_Input_177, SaJoData.man_Input_178,
            SaJoData.man_Input_179, SaJoData.man_Input_180
    };

    private double hidden[] = new double[hidden_count];

    private double out[][] = new double[input_collection_man.length][out_count];
    private double target[][] = SaJoData.man_target_one;

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
        Cursor cursor = db.query("'" + Contact.MID_MAN_WEIGHT_JONG + "'", null, null, null, null, null, null);
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
        for (int all = 0; all < input_collection_man.length; all++) {
            for (int k = 0; k < hidden_count - 1; k++) {
                double sum = 0;
                for (int i = 0; i < input_count; i++) {
                    sum += input_collection_man[all][i] * first_w[i][k];
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
                        first_w[i][j] += (hidden[j] * (1 - hidden[j]) * differential * second_w[j][k] * input_collection_man[all][i]);
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
                    sumHangle += GetHangle.Man_JongSung_String[(int) GetNearValue.getNearJONG(out[i][j])];
                }
            }
            Log.e("가운데 남자 종성", sumHangle);

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
            db.insert("'" + Contact.MID_MAN_WEIGHT_JONG + "'", null, values);
            db.close();

            sendBroadcast(new Intent(Contact.WEIGHT_TRAING));
        }
    }

    ;
}