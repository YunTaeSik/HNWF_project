package com.ai.project.hnwf_project.traing;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ai.project.hnwf_project.R;
import com.ai.project.hnwf_project.data.SaJoData;
import com.ai.project.hnwf_project.db.DBManager;
import com.ai.project.hnwf_project.main.MainActivity;
import com.ai.project.hnwf_project.util.Contact;
import com.ai.project.hnwf_project.util.GetHangle;
import com.ai.project.hnwf_project.util.GetNearValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Math.exp;

/**
 * Created by YunTaeSik on 2016-11-30.
 */
public class TraningService extends Service {
    private int input_count = 7;
    private int hidden_count = 8;
    private int out_count = 3;
    // private double n = 0.0005;
    private double n = 0.005;

    private double first_w_man_one[][] = new double[input_count][hidden_count];
    private double first_w_man_two[][] = new double[input_count][hidden_count];

    private double first_w_girl_one[][] = new double[input_count][hidden_count]; //여자 가운데 글자 가중치
    private double first_w_girl_two[][] = new double[input_count][hidden_count];//여자 마지막 글자 기증치

    private double second_w_man_one[][] = new double[hidden_count][out_count];
    private double second_w_man_two[][] = new double[hidden_count][out_count];

    private double second_w_gril_one[][] = new double[hidden_count][out_count];
    private double second_w_gril_two[][] = new double[hidden_count][out_count];
    /*   private double input_collection_man[][] = {
               SaJoData.man_Input_1, SaJoData.man_Input_2, SaJoData.man_Input_3, SaJoData.man_Input_4, SaJoData.man_Input_5, SaJoData.man_Input_6, SaJoData.man_Input_7, SaJoData.man_Input_8

       };
       private double input_collection_girl[][] = {
               SaJoData.girl_Input_1, SaJoData.girl_Input_2, SaJoData.girl_Input_3, SaJoData.girl_Input_4, SaJoData.girl_Input_5, SaJoData.girl_Input_6, SaJoData.girl_Input_7, SaJoData.girl_Input_8

       };*/
    //남자 인풋
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
            SaJoData.man_Input_163, SaJoData.man_Input_164, SaJoData.man_Input_165, SaJoData.man_Input_166, SaJoData.man_Input_167, SaJoData.man_Input_168};
    //여자 인풋
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
            SaJoData.girl_Input_161, SaJoData.girl_Input_162, SaJoData.girl_Input_163, SaJoData.girl_Input_164, SaJoData.girl_Input_165, SaJoData.girl_Input_166};

    private double hidden_man_one[] = new double[hidden_count];
    private double hidden_man_two[] = new double[hidden_count];
    private double hidden_girl_one[] = new double[hidden_count];
    private double hidden_girl_two[] = new double[hidden_count];

    private double out_man_one[][] = new double[input_collection_man.length][out_count];
    private double out_man_two[][] = new double[input_collection_man.length][out_count];

    private double out_girl_one[][] = new double[input_collection_girl.length][out_count];
    private double out_girl_two[][] = new double[input_collection_girl.length][out_count];

    private double target_man_one[][] = SaJoData.man_target_one;
    private double target_man_two[][] = SaJoData.man_target_two;

    private double target_girl_one[][] = SaJoData.girl_target_one;
    private double target_girl_two[][] = SaJoData.girl_target_two;

    private DBManager dbManager;
    private int dbcount = 0;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Set_weight();

        dbManager = new DBManager(getApplicationContext(), "WEIGHT", null, 1);
        SQLiteDatabase db = dbManager.getWritableDatabase();


        Cursor cursor_man_one = db.query("'" + Contact.MAN_WEIGHT_ONE + "'", null, null, null, null, null, null);
        if (cursor_man_one.getCount() < 1) {
            asyncTask_Man_One asyncTask_man_one = new asyncTask_Man_One();
            asyncTask_man_one.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // 남자 가운데 글자
        } else {
            dbcount = 4;
        }
        cursor_man_one.close();

        Cursor cursor_man_two = db.query("'" + Contact.MAN_WEIGHT_TWO + "'", null, null, null, null, null, null);
        if (cursor_man_two.getCount() < 1) {
            asyncTask_Man_Two asyncTask_man_two = new asyncTask_Man_Two();
            asyncTask_man_two.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // 남자  마지막 글자
        } else {
            dbcount = 4;
        }
        cursor_man_two.close();

        Cursor cursor_girl_one = db.query("'" + Contact.GIRL_WEIGHT_ONE + "'", null, null, null, null, null, null);
        if (cursor_girl_one.getCount() < 1) {
            asyncTask_Gril_One asyncTask_gril_one = new asyncTask_Gril_One();
            asyncTask_gril_one.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // 여자 가운데 글자
        } else {
            dbcount = 4;
        }
        cursor_girl_one.close();

        Cursor cursor_girl_two = db.query("'" + Contact.GIRL_WEIGHT_TWO + "'", null, null, null, null, null, null);
        if (cursor_girl_two.getCount() < 1) {
            asyncTask_Gril_Two asyncTask_gril_two = new asyncTask_Gril_Two();
            asyncTask_gril_two.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // 여자 마지막 글자
        } else {
            dbcount = 4;
        }
        cursor_girl_two.close();
        if (dbcount >= 4) {
            sendBroadcast(new Intent(Contact.WEIGHT_TRAING));
            notiFication();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void Set_weight() { //초기 가중치
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

        for (int i = 0; i < input_count; i++) {
            for (int j = 0; j < hidden_count; j++) {
                first_w_girl_one[i][j] = Math.random();
            }

        }
        for (int i = 0; i < hidden_count; i++) {
            for (int j = 0; j < out_count; j++) {
                second_w_gril_one[i][j] = Math.random();
            }
        }
        for (int i = 0; i < input_count; i++) {
            for (int j = 0; j < hidden_count; j++) {
                first_w_girl_two[i][j] = Math.random();
            }

        }
        for (int i = 0; i < hidden_count; i++) {
            for (int j = 0; j < out_count; j++) {
                second_w_gril_two[i][j] = Math.random();
            }
        }
    }

    private void Traning_ManOne() {
        for (int all = 0; all < input_collection_man.length; all++) {
            for (int k = 0; k < hidden_count; k++) {
                double sum = 0;
                for (int i = 0; i < input_count; i++) {
                    sum += input_collection_man[all][i] * first_w_man_one[i][k];
                }
                hidden_man_one[k] = 1 / (1 + exp(-sum));
            }
            for (int k = 0; k < out_count; k++) {
                double out_sum = 0;
                for (int i = 0; i < hidden_count; i++) {
                    out_sum += hidden_man_one[i] * second_w_man_one[i][k];
                }
                out_man_one[all][k] = 1 / (1 + exp(-out_sum));
            }
            for (int k = 0; k < out_count; k++) {
                double differential = (n * (target_man_one[all][k] - out_man_one[all][k]) * (1 - out_man_one[all][k]) * out_man_one[all][k]);

                for (int i = 0; i < input_count; i++) {
                    for (int j = 0; j < hidden_count; j++) {
                        first_w_man_one[i][j] += (hidden_man_one[j] * (1 - hidden_man_one[j]) * differential * second_w_man_one[j][k] * input_collection_man[all][i]);
                    }
                }
                for (int i = 0; i < second_w_man_one.length; i++) {
                    second_w_man_one[i][k] += (differential * hidden_man_one[i]);
                }
            }

        }
    }


    private class asyncTask_Man_One extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            for (int traning = 0; traning < 30000; traning++) {
                Log.e("traing", String.valueOf(traning));
                Traning_ManOne();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer o) {
            super.onPostExecute(o);
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

            Log.e("완성 남자 가운데 글자", sumHangle);

            SQLiteDatabase db = dbManager.getWritableDatabase();
            JSONObject json = new JSONObject();
            String jsonArray = null;
            try {
                json.put("first_w_man_one", new JSONArray(first_w_man_one));
                json.put("second_w_man_one", new JSONArray(second_w_man_one));
                jsonArray = json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ContentValues values = new ContentValues();
            values.put("json", jsonArray);
            db.insert("'" + Contact.MAN_WEIGHT_ONE + "'", null, values);
            db.close();

            dbcount++;
            if (dbcount >= 4) {
                sendBroadcast(new Intent(Contact.WEIGHT_TRAING));
                notiFication();
            }
        }
    }

    ;

    private void Traning_ManTwo() {
        for (int all = 0; all < input_collection_man.length; all++) {
            for (int k = 0; k < hidden_count; k++) {
                double sum = 0;
                for (int i = 0; i < input_count; i++) {
                    sum += input_collection_man[all][i] * first_w_man_two[i][k];
                }
                hidden_man_two[k] = 1 / (1 + exp(-sum));
            }
            for (int k = 0; k < out_count; k++) {
                double out_sum = 0;
                for (int i = 0; i < hidden_count; i++) {
                    out_sum += hidden_man_two[i] * second_w_man_two[i][k];
                }
                out_man_two[all][k] = 1 / (1 + exp(-out_sum));
            }
            for (int k = 0; k < out_count; k++) {
                double differential = (n * (target_man_two[all][k] - out_man_two[all][k]) * (1 - out_man_two[all][k]) * out_man_two[all][k]);

                for (int i = 0; i < input_count; i++) {
                    for (int j = 0; j < hidden_count; j++) {
                        first_w_man_two[i][j] += (hidden_man_two[j] * (1 - hidden_man_two[j]) * differential * second_w_man_two[j][k] * input_collection_man[all][i]);
                    }
                }
                for (int i = 0; i < second_w_man_two.length; i++) {
                    second_w_man_two[i][k] += (differential * hidden_man_two[i]);
                }
            }

        }
    }

    private class asyncTask_Man_Two extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            for (int traning = 0; traning < 30000; traning++) {
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
            Log.e("완성 남성 마지막 글자", sumHangle);

            SQLiteDatabase db = dbManager.getWritableDatabase();
            JSONObject json = new JSONObject();
            String jsonArray = null;
            try {
                json.put("first_w_man_two", new JSONArray(first_w_man_two));
                json.put("second_w_man_two", new JSONArray(second_w_man_two));
                jsonArray = json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ContentValues values = new ContentValues();
            values.put("json", jsonArray);
            db.insert("'" + Contact.MAN_WEIGHT_TWO + "'", null, values);
            db.close();

            dbcount++;
            if (dbcount >= 4) {
                sendBroadcast(new Intent(Contact.WEIGHT_TRAING));
                notiFication();
            }
        }
    }

    ;

    private void Traning_GrilOne() {
        for (int all = 0; all < input_collection_girl.length; all++) {
            for (int k = 0; k < hidden_count; k++) {
                double sum = 0;
                for (int i = 0; i < input_count; i++) {
                    sum += input_collection_girl[all][i] * first_w_girl_one[i][k];
                }
                hidden_girl_one[k] = 1 / (1 + exp(-sum));
            }
            for (int k = 0; k < out_count; k++) {
                double out_sum = 0;
                for (int i = 0; i < hidden_count; i++) {
                    out_sum += hidden_girl_one[i] * second_w_gril_one[i][k];
                }
                out_girl_one[all][k] = 1 / (1 + exp(-out_sum));
            }
            for (int k = 0; k < out_count; k++) {
                double differential = (n * (target_girl_one[all][k] - out_girl_one[all][k]) * (1 - out_girl_one[all][k]) * out_girl_one[all][k]);

                for (int i = 0; i < input_count; i++) {
                    for (int j = 0; j < hidden_count; j++) {
                        first_w_girl_one[i][j] += (hidden_girl_one[j] * (1 - hidden_girl_one[j]) * differential * second_w_gril_one[j][k] * input_collection_girl[all][i]);
                    }
                }
                for (int i = 0; i < second_w_gril_one.length; i++) {
                    second_w_gril_one[i][k] += (differential * hidden_girl_one[i]);
                }
            }

        }
    }

    private class asyncTask_Gril_One extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            for (int traning = 0; traning < 30000; traning++) {
                Traning_GrilOne();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            SQLiteDatabase db = dbManager.getWritableDatabase();
            JSONObject json = new JSONObject();
            String jsonArray = null;
            try {
                json.put("first_w_girl_one", new JSONArray(first_w_girl_one));
                json.put("second_w_gril_one", new JSONArray(second_w_gril_one));
                jsonArray = json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ContentValues values = new ContentValues();
            values.put("json", jsonArray);
            db.insert("'" + Contact.GIRL_WEIGHT_ONE + "'", null, values);
            db.close();
            dbcount++;
            if (dbcount >= 4) {
                sendBroadcast(new Intent(Contact.WEIGHT_TRAING));
                notiFication();
            }
        }
    }

    ;

    private void Traning_GrilTwo() {
        for (int all = 0; all < input_collection_girl.length; all++) {
            for (int k = 0; k < hidden_count; k++) {
                double sum = 0;
                for (int i = 0; i < input_count; i++) {
                    sum += input_collection_girl[all][i] * first_w_girl_two[i][k];
                }
                hidden_girl_two[k] = 1 / (1 + exp(-sum));
            }
            for (int k = 0; k < out_count; k++) {
                double out_sum = 0;
                for (int i = 0; i < hidden_count; i++) {
                    out_sum += hidden_girl_two[i] * second_w_gril_two[i][k];
                }
                out_girl_two[all][k] = 1 / (1 + exp(-out_sum));
            }
            for (int k = 0; k < out_count; k++) {
                double differential = (n * (target_girl_two[all][k] - out_girl_two[all][k]) * (1 - out_girl_two[all][k]) * out_girl_two[all][k]);

                for (int i = 0; i < input_count; i++) {
                    for (int j = 0; j < hidden_count; j++) {
                        first_w_girl_two[i][j] += (hidden_girl_two[j] * (1 - hidden_girl_two[j]) * differential * second_w_gril_two[j][k] * input_collection_girl[all][i]);
                    }
                }
                for (int i = 0; i < second_w_gril_two.length; i++) {
                    second_w_gril_two[i][k] += (differential * hidden_girl_two[i]);
                }
            }

        }
    }

    private class asyncTask_Gril_Two extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            for (int traning = 0; traning < 30000; traning++) {
                Traning_GrilTwo();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            SQLiteDatabase db = dbManager.getWritableDatabase();
            JSONObject json = new JSONObject();
            String jsonArray = null;
            try {
                json.put("first_w_girl_two", new JSONArray(first_w_girl_two));
                json.put("second_w_gril_two", new JSONArray(second_w_gril_two));
                jsonArray = json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ContentValues values = new ContentValues();
            values.put("json", jsonArray);
            db.insert("'" + Contact.GIRL_WEIGHT_TWO + "'", null, values);
            db.close();
            dbcount++;
            if (dbcount >= 4) {
                sendBroadcast(new Intent(Contact.WEIGHT_TRAING));
                notiFication();
            }
        }
    }

    private void notiFication() {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ai_image);
        mBuilder.setTicker(getString(R.string.app_name));
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setContentTitle("훈련완료!!!");
        mBuilder.setContentText("작명 하러 가시겠습니까?");
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBuilder.setPriority(Notification.PRIORITY_MAX);
            nm.notify(111, mBuilder.build());
        }
    }

}
