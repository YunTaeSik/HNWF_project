package com.ai.project.hnwf_project.traing;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ai.project.hnwf_project.data.SaJoData;
import com.ai.project.hnwf_project.util.GetHangle;
import com.ai.project.hnwf_project.util.GetNearValue;

import static java.lang.Math.exp;

/**
 * Created by YunTaeSik on 2016-11-30.
 */
public class TraningService extends Service {
    private int input_count = 7;
    private int hidden_count = 7;
    private int out_count = 3;
    private double n = 0.05;

    private double first_w_man_one[][] = new double[input_count][hidden_count];
    private double first_w_man_two[][] = new double[input_count][hidden_count];

    private double first_w_girl_one[][] = new double[input_count][hidden_count]; //여자 가운데 글자 가중치
    private double first_w_girl_two[][] = new double[input_count][hidden_count];//여자 마지막 글자 기증치

    private double second_w_man_one[][] = new double[hidden_count][out_count];
    private double second_w_man_two[][] = new double[hidden_count][out_count];

    private double second_w_gril_one[][] = new double[hidden_count][out_count];
    private double second_w_gril_two[][] = new double[hidden_count][out_count];
    //남자 인풋
    private double input_collection_man[][] = {SaJoData.man_Input_1, SaJoData.man_Input_2, SaJoData.man_Input_3, SaJoData.man_Input_4};
    //여자 인풋
    private double input_collection_girl[][] = {SaJoData.man_Input_1, SaJoData.man_Input_2, SaJoData.man_Input_3, SaJoData.man_Input_4};

    private double hidden_man[] = new double[hidden_count];
    private double hidden_girl[] = new double[hidden_count];
    private double out_man_one[][] = new double[input_collection_man.length][out_count];
    private double out_man_two[][] = new double[input_collection_man.length][out_count];

    private double out_girl_one[][] = new double[input_collection_man.length][out_count];
    private double out_girl_two[][] = new double[input_collection_man.length][out_count];

    private double target_man_one[][] = SaJoData.man_target_one;
    private double target_man_two[][] = SaJoData.man_target_two;

    private double target_girl_one[][] = SaJoData.man_target_one;
    private double target_girl_two[][] = SaJoData.man_target_two;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Set_weight();
        asyncTask_Man_One asyncTask_man_one = new asyncTask_Man_One();
        asyncTask_man_one.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // 남자 가운데 글자

        asyncTask_Man_Two asyncTask_man_two = new asyncTask_Man_Two();
        asyncTask_man_two.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // 남자  마지막 글자

        asyncTask_Gril_One asyncTask_gril_one = new asyncTask_Gril_One();
        asyncTask_gril_one.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // 여자 가운데 글자

        asyncTask_Gril_Two asyncTask_gril_two = new asyncTask_Gril_Two();
        asyncTask_gril_two.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); // 여자 마지막 글자
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


    private class asyncTask_Man_One extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            for (int traning = 0; traning < 100000; traning++) {
                Traning_ManOne();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
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

    private class asyncTask_Man_Two extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            for (int traning = 0; traning < 100000; traning++) {
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
                hidden_girl[k] = 1 / (1 + exp(-sum));
            }
            for (int k = 0; k < out_count; k++) {
                double out_sum = 0;
                for (int i = 0; i < hidden_count; i++) {
                    out_sum += hidden_girl[i] * second_w_gril_one[i][k];
                }
                out_girl_one[all][k] = 1 / (1 + exp(-out_sum));
            }
            for (int k = 0; k < out_count; k++) {
                double differential = (n * (target_girl_one[all][k] - out_girl_one[all][k]) * (1 - out_girl_one[all][k]) * out_girl_one[all][k]);

                for (int i = 0; i < input_count; i++) {
                    for (int j = 0; j < hidden_count; j++) {
                        first_w_girl_one[i][j] += (hidden_girl[j] * (1 - hidden_girl[j]) * differential * second_w_gril_one[j][k] * input_collection_girl[all][i]);
                    }
                }
                for (int i = 0; i < second_w_gril_one.length; i++) {
                    second_w_gril_one[i][k] += (differential * hidden_girl[i]);
                }
            }

        }
    }

    private class asyncTask_Gril_One extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            for (int traning = 0; traning < 100000; traning++) {
                Traning_GrilOne();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            String sumHangle = "";
            for (int i = 0; i < out_girl_one.length; i++) {
                for (int j = 0; j < out_girl_one[i].length; j++) {
                    Log.e("test", String.valueOf(out_girl_one[i][j]));
                    if (j == 0 || j == 3) {
                        Log.e("초성", String.valueOf(GetNearValue.getNearCHO(out_girl_one[i][j])));
                        sumHangle += GetHangle.Girl_ChoSung_String[(int) GetNearValue.getNearCHO(out_girl_one[i][j])];
                    } else if (j == 1 || j == 4) {
                        Log.e("중성", String.valueOf(GetNearValue.getNearJUNG(out_girl_one[i][j])));
                        sumHangle += GetHangle.Girl_JungSung_String[(int) GetNearValue.getNearJUNG(out_girl_one[i][j])];
                    } else if (j == 2 || j == 5) {
                        Log.e("종성", String.valueOf(GetNearValue.getNearJONG(out_girl_one[i][j])));
                        sumHangle += GetHangle.Girl_JongSung_String[(int) GetNearValue.getNearJONG(out_girl_one[i][j])];
                    }
                }
            }
            Log.e("완성 여자 가운데 글자", sumHangle);
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
                hidden_girl[k] = 1 / (1 + exp(-sum));
            }
            for (int k = 0; k < out_count; k++) {
                double out_sum = 0;
                for (int i = 0; i < hidden_count; i++) {
                    out_sum += hidden_girl[i] * second_w_gril_two[i][k];
                }
                out_girl_two[all][k] = 1 / (1 + exp(-out_sum));
            }
            for (int k = 0; k < out_count; k++) {
                double differential = (n * (target_girl_two[all][k] - out_girl_two[all][k]) * (1 - out_girl_two[all][k]) * out_girl_two[all][k]);

                for (int i = 0; i < input_count; i++) {
                    for (int j = 0; j < hidden_count; j++) {
                        first_w_girl_two[i][j] += (hidden_girl[j] * (1 - hidden_girl[j]) * differential * second_w_gril_two[j][k] * input_collection_girl[all][i]);
                    }
                }
                for (int i = 0; i < second_w_gril_two.length; i++) {
                    second_w_gril_two[i][k] += (differential * hidden_girl[i]);
                }
            }

        }
    }

    private class asyncTask_Gril_Two extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            for (int traning = 0; traning < 100000; traning++) {
                Traning_GrilTwo();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            String sumHangle = "";
            for (int i = 0; i < out_girl_two.length; i++) {
                for (int j = 0; j < out_girl_two[i].length; j++) {
                    Log.e("test", String.valueOf(out_girl_two[i][j]));
                    if (j == 0 || j == 3) {
                        Log.e("초성", String.valueOf(GetNearValue.getNearCHO(out_girl_two[i][j])));
                        sumHangle += GetHangle.Girl_ChoSung_String[(int) GetNearValue.getNearCHO(out_girl_two[i][j])];
                    } else if (j == 1 || j == 4) {
                        Log.e("중성", String.valueOf(GetNearValue.getNearJUNG(out_girl_two[i][j])));
                        sumHangle += GetHangle.Girl_JungSung_String[(int) GetNearValue.getNearJUNG(out_girl_two[i][j])];
                    } else if (j == 2 || j == 5) {
                        Log.e("종성", String.valueOf(GetNearValue.getNearJONG(out_girl_two[i][j])));
                        sumHangle += GetHangle.Girl_JongSung_String[(int) GetNearValue.getNearJONG(out_girl_two[i][j])];
                    }
                }
            }
            Log.e("완성 여자 마지막 글자", sumHangle);
        }
    }

    ;
}
