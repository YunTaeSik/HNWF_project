package com.ai.project.hnwf_project.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.project.hnwf_project.R;
import com.ai.project.hnwf_project.db.DBManager;
import com.ai.project.hnwf_project.util.Contact;
import com.ai.project.hnwf_project.util.GetHangle;
import com.ai.project.hnwf_project.util.GetNearValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Math.exp;

public class NewInformationFragment extends Fragment implements View.OnClickListener {
    private int input_count = 8;
    private int hidden_count = 20;
    private int out_count = 1;

    private double first_w_mid_man_cho[][] = new double[input_count][hidden_count];
    private double first_w_mid_man_jung[][] = new double[input_count][hidden_count];
    private double first_w_mid_man_jong[][] = new double[input_count][hidden_count];

    private double first_w_last_man_cho[][] = new double[input_count][hidden_count];
    private double first_w_last_man_jung[][] = new double[input_count][hidden_count];
    private double first_w_last_man_jong[][] = new double[input_count][hidden_count];

    private double second_w_mid_man_cho[][] = new double[hidden_count][out_count];
    private double second_w_mid_man_jung[][] = new double[hidden_count][out_count];
    private double second_w_mid_man_jong[][] = new double[hidden_count][out_count];

    private double second_w_last_man_cho[][] = new double[hidden_count][out_count];
    private double second_w_last_man_jung[][] = new double[hidden_count][out_count];
    private double second_w_last_man_jong[][] = new double[hidden_count][out_count];

    private double first_w_mid_girl_cho[][] = new double[input_count][hidden_count]; //여자 가운데 글자 가중치
    private double first_w_mid_girl_jung[][] = new double[input_count][hidden_count]; //여자 가운데 글자 가중치
    private double first_w_mid_girl_jong[][] = new double[input_count][hidden_count]; //여자 가운데 글자 가중치

    private double first_w_last_girl_cho[][] = new double[input_count][hidden_count];//여자 마지막 글자 기증치
    private double first_w_last_girl_jung[][] = new double[input_count][hidden_count];//여자 마지막 글자 기증치
    private double first_w_last_girl_jong[][] = new double[input_count][hidden_count];//여자 마지막 글자 기증치

    private double second_w_mid_girl_cho[][] = new double[hidden_count][out_count];
    private double second_w_mid_girl_jung[][] = new double[hidden_count][out_count];
    private double second_w_mid_girl_jong[][] = new double[hidden_count][out_count];

    private double second_w_last_girl_cho[][] = new double[hidden_count][out_count];
    private double second_w_last_girl_jung[][] = new double[hidden_count][out_count];
    private double second_w_last_girl_jong[][] = new double[hidden_count][out_count];

    private double hidden_mid_man_cho[] = new double[hidden_count];
    private double hidden_mid_man_jung[] = new double[hidden_count];
    private double hidden_mid_man_jong[] = new double[hidden_count];

    private double hidden_last_man_cho[] = new double[hidden_count];
    private double hidden_last_man_jung[] = new double[hidden_count];
    private double hidden_last_man_jong[] = new double[hidden_count];

    private double hidden_mid_girl_cho[] = new double[hidden_count];
    private double hidden_mid_girl_jung[] = new double[hidden_count];
    private double hidden_mid_girl_jong[] = new double[hidden_count];

    private double hidden_last_girl_cho[] = new double[hidden_count];
    private double hidden_last_girl_jung[] = new double[hidden_count];
    private double hidden_last_girl_jong[] = new double[hidden_count];


    private double out_mid_man_cho[] = new double[out_count];
    private double out_mid_man_jung[] = new double[out_count];
    private double out_mid_man_jong[] = new double[out_count];

    private double out_last_man_cho[] = new double[out_count];
    private double out_last_man_jung[] = new double[out_count];
    private double out_last_man_jong[] = new double[out_count];

    private double out_mid_girl_cho[] = new double[out_count];
    private double out_mid_girl_jung[] = new double[out_count];
    private double out_mid_girl_jong[] = new double[out_count];

    private double out_last_girl_cho[] = new double[out_count];
    private double out_last_girl_jung[] = new double[out_count];
    private double out_last_girl_jong[] = new double[out_count];


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RadioGroup sex_grounp;
    private RadioButton boy_btn;
    private RadioButton girl_btn;
    private LinearLayout naming_layout_btn;
    private EditText year_edit;
    private EditText month_edit;
    private EditText date_edit;
    private EditText time_edit;
    private EditText name_edit;
    private TextView result_text;
    private String SEX = "남";

    private DBManager dbManager;
    private Animation animation;

    public NewInformationFragment() {
        // Required empty public constructor
    }

    public static InformationFragment newInstance(String param1, String param2) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        sex_grounp = (RadioGroup) view.findViewById(R.id.sex_grounp);
        boy_btn = (RadioButton) view.findViewById(R.id.boy_btn);
        girl_btn = (RadioButton) view.findViewById(R.id.girl_btn);
        naming_layout_btn = (LinearLayout) view.findViewById(R.id.naming_layout_btn);
        year_edit = (EditText) view.findViewById(R.id.year_edit);
        month_edit = (EditText) view.findViewById(R.id.month_edit);
        date_edit = (EditText) view.findViewById(R.id.date_edit);
        time_edit = (EditText) view.findViewById(R.id.time_edit);
        name_edit = (EditText) view.findViewById(R.id.name_edit);
        result_text = (TextView) view.findViewById(R.id.result_text);

        NEWgetWeight(); //가중치 얻기

        naming_layout_btn.setOnClickListener(this);
        boy_btn.setOnClickListener(this);
        girl_btn.setOnClickListener(this);
        boy_btn.setChecked(true);

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.result_animation);
        result_text.startAnimation(animation);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.naming_layout_btn:
                if (year_edit.getText().length() > 0 && month_edit.getText().length() > 0 && date_edit.getText().length() > 0 && time_edit.getText().length() > 0 && name_edit.getText().length() > 0) {
                    char[] chars = hangulToJaso(name_edit.getText().toString());
                    String cho = String.valueOf(chars[0]);
                    String jung = String.valueOf(chars[1]);
                    String jong = String.valueOf(chars[2]);
                    double cho_index = 0;
                    double jung_index = 0;
                    double jong_index = 0;
                    if (SEX.equals("남")) {
                        for (int i = 0; i < GetHangle.Man_ChoSung_String.length; i++) {
                            if (GetHangle.Man_ChoSung_String[i].equals(cho)) {
                                cho_index = (double) i;
                                Log.e("test", String.valueOf(cho_index));
                            }
                        }
                        for (int i = 0; i < GetHangle.Man_JungSung_String.length; i++) {
                            if (GetHangle.Man_JungSung_String[i].equals(jung)) {
                                jung_index = (double) i;
                                Log.e("test", String.valueOf(jung_index));
                            }
                        }
                        for (int i = 0; i < GetHangle.Man_JongSung_String.length; i++) {
                            if (GetHangle.Man_JongSung_String[i].equals(jong)) {
                                jong_index = (double) i;
                                Log.e("test", String.valueOf(jong_index));
                            }
                        }
                        double year = Double.parseDouble(year_edit.getText().toString()) - 2016.0;
                        double month = Double.parseDouble(month_edit.getText().toString());
                        double date = Double.parseDouble(date_edit.getText().toString());
                        double time = Double.parseDouble(time_edit.getText().toString());

                        double input[] = {cho_index / 11.0, jung_index / 13.0, jong_index / 4.0, year / 100.0, month / 12.0, date / 31.0, time / 24.0, -1.0};
                        NewSetNameing_Man(input);
                    } else if (SEX.equals("여")) {
                        for (int i = 0; i < GetHangle.Girl_ChoSung_String.length; i++) {
                            if (GetHangle.Girl_ChoSung_String[i].equals(cho)) {
                                cho_index = (double) i;
                            }
                        }
                        for (int i = 0; i < GetHangle.Girl_JungSung_String.length; i++) {
                            if (GetHangle.Girl_JungSung_String[i].equals(jung)) {
                                jung_index = (double) i;
                            }
                        }
                        for (int i = 0; i < GetHangle.Girl_JongSung_String.length; i++) {
                            if (GetHangle.Girl_JongSung_String[i].equals(jong)) {
                                jong_index = (double) i;
                            }
                        }
                        double year = Double.parseDouble(year_edit.getText().toString()) - 2016.0;
                        double month = Double.parseDouble(month_edit.getText().toString());
                        double date = Double.parseDouble(date_edit.getText().toString());
                        double time = Double.parseDouble(time_edit.getText().toString());

                        double input[] = {cho_index / 11.0, jung_index / 13.0, jong_index / 4.0, year / 100.0, month / 12.0, date / 31.0, time / 24.0, -1.0};
                        NewSetNameing_Girl(input);
                    }
                } else {
                    Toast.makeText(getContext(), "정보를 전부 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.boy_btn:
                SEX = "남";
                break;
            case R.id.girl_btn:
                SEX = "여";
                break;
        }

    }


    private void NEWgetWeight() {
        dbManager = new DBManager(getContext(), "WEIGHT", null, 1);
        SQLiteDatabase db = dbManager.getWritableDatabase();
        Cursor MID_MAN_WEIGHT_CHO = db.query("'" + Contact.MID_MAN_WEIGHT_CHO + "'", null, null, null, null, null, null);
        Cursor MID_MAN_WEIGHT_JUNG = db.query("'" + Contact.MID_MAN_WEIGHT_JUNG + "'", null, null, null, null, null, null);
        Cursor MID_MAN_WEIGHT_JONG = db.query("'" + Contact.MID_MAN_WEIGHT_JONG + "'", null, null, null, null, null, null);

        Cursor LAST_MAN_WEIGHT_CHO = db.query("'" + Contact.LAST_MAN_WEIGHT_CHO + "'", null, null, null, null, null, null);
        Cursor LAST_MAN_WEIGHT_JUNG = db.query("'" + Contact.LAST_MAN_WEIGHT_JUNG + "'", null, null, null, null, null, null);
        Cursor LAST_MAN_WEIGHT_JONG = db.query("'" + Contact.LAST_MAN_WEIGHT_JONG + "'", null, null, null, null, null, null);

        Cursor MID_GIRL_WEIGHT_CHO = db.query("'" + Contact.MID_GIRL_WEIGHT_CHO + "'", null, null, null, null, null, null);
        Cursor MID_GIRL_WEIGHT_JUNG = db.query("'" + Contact.MID_GIRL_WEIGHT_JUNG + "'", null, null, null, null, null, null);
        Cursor MID_GIRL_WEIGHT_JONG = db.query("'" + Contact.MID_GIRL_WEIGHT_JONG + "'", null, null, null, null, null, null);

        Cursor LAST_GIRL_WEIGHT_CHO = db.query("'" + Contact.LAST_GIRL_WEIGHT_CHO + "'", null, null, null, null, null, null);
        Cursor LAST_GIRL_WEIGHT_JUNG = db.query("'" + Contact.LAST_GIRL_WEIGHT_JUNG + "'", null, null, null, null, null, null);
        Cursor LAST_GIRL_WEIGHT_JONG = db.query("'" + Contact.LAST_GIRL_WEIGHT_JONG + "'", null, null, null, null, null, null);

        MID_MAN_WEIGHT_CHO.moveToNext();
        MID_MAN_WEIGHT_JUNG.moveToNext();
        MID_MAN_WEIGHT_JONG.moveToNext();

        LAST_MAN_WEIGHT_CHO.moveToNext();
        LAST_MAN_WEIGHT_JUNG.moveToNext();
        LAST_MAN_WEIGHT_JONG.moveToNext();

        MID_GIRL_WEIGHT_CHO.moveToNext();
        MID_GIRL_WEIGHT_JUNG.moveToNext();
        MID_GIRL_WEIGHT_JONG.moveToNext();

        LAST_GIRL_WEIGHT_CHO.moveToNext();
        LAST_GIRL_WEIGHT_JUNG.moveToNext();
        LAST_GIRL_WEIGHT_JONG.moveToNext();

        String json_mid_man_cho = MID_MAN_WEIGHT_CHO.getString(1);
        String json_mid_man_jung = MID_MAN_WEIGHT_JUNG.getString(1);
        String json_mid_man_jong = MID_MAN_WEIGHT_JONG.getString(1);

        String json_last_man_cho = LAST_MAN_WEIGHT_CHO.getString(1);
        String json_last_man_jung = LAST_MAN_WEIGHT_JUNG.getString(1);
        String json_last_man_jong = LAST_MAN_WEIGHT_JONG.getString(1);


        String json_mid_girl_cho = MID_GIRL_WEIGHT_CHO.getString(1);
        String json_mid_girl_jung = MID_GIRL_WEIGHT_JUNG.getString(1);
        String json_mid_girl_jong = MID_GIRL_WEIGHT_JONG.getString(1);

        String json_last_girl_cho = LAST_GIRL_WEIGHT_CHO.getString(1);
        String json_last_girl_jung = LAST_GIRL_WEIGHT_JUNG.getString(1);
        String json_last_girl_jong = LAST_GIRL_WEIGHT_JONG.getString(1);

        //남자 가운데 글자 초성 가중치 얻기 시작
        JSONObject jsnobject = null;
        try {
            jsnobject = new JSONObject(json_mid_man_cho);
            JSONArray first_w = jsnobject.getJSONArray("first_w");
            JSONArray second_w = jsnobject.getJSONArray("second_w");

            for (int i = 0; i < first_w.length(); i++) {
                for (int j = 0; j < first_w.getJSONArray(i).length(); j++) {
                    first_w_mid_man_cho[i][j] = first_w.getJSONArray(i).getDouble(j);
                }
            }


            for (int i = 0; i < second_w.length(); i++) {
                for (int j = 0; j < second_w.getJSONArray(i).length(); j++) {
                    second_w_mid_man_cho[i][j] = second_w.getJSONArray(i).getDouble(j);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //남자 가운데 글자 초성 가중치 얻기 끝
        //남자 가운데 글자 중성 가중치 얻기 시작
        try {
            jsnobject = new JSONObject(json_mid_man_jung);
            JSONArray first_w = jsnobject.getJSONArray("first_w");
            JSONArray second_w = jsnobject.getJSONArray("second_w");
            for (int i = 0; i < first_w.length(); i++) {
                for (int j = 0; j < first_w.getJSONArray(i).length(); j++) {
                    first_w_mid_man_jung[i][j] = first_w.getJSONArray(i).getDouble(j);
                }
            }
            for (int i = 0; i < second_w.length(); i++) {
                for (int j = 0; j < second_w.getJSONArray(i).length(); j++) {
                    second_w_mid_man_jung[i][j] = second_w.getJSONArray(i).getDouble(j);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //남자 가운데 글자 중성 가중치 얻기 끝
        // 남자 가운데 글자 종성 가중치 얻기 시작
        try {
            jsnobject = new JSONObject(json_mid_man_jong);
            JSONArray first_w = jsnobject.getJSONArray("first_w");
            JSONArray second_w = jsnobject.getJSONArray("second_w");
            for (int i = 0; i < first_w.length(); i++) {
                for (int j = 0; j < first_w.getJSONArray(i).length(); j++) {
                    first_w_mid_man_jong[i][j] = first_w.getJSONArray(i).getDouble(j);
                }
            }
            for (int i = 0; i < second_w.length(); i++) {
                for (int j = 0; j < second_w.getJSONArray(i).length(); j++) {
                    second_w_mid_man_jong[i][j] = second_w.getJSONArray(i).getDouble(j);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //남자 가운데 글자 종성 가중치 얻기 끝

        // 남자 마지막 글자 초성 가중치 얻기 시작
        try {
            jsnobject = new JSONObject(json_last_man_cho);
            JSONArray first_w = jsnobject.getJSONArray("first_w");
            JSONArray second_w = jsnobject.getJSONArray("second_w");
            for (int i = 0; i < first_w.length(); i++) {
                for (int j = 0; j < first_w.getJSONArray(i).length(); j++) {
                    first_w_last_man_cho[i][j] = first_w.getJSONArray(i).getDouble(j);
                }
            }
            for (int i = 0; i < second_w.length(); i++) {
                for (int j = 0; j < second_w.getJSONArray(i).length(); j++) {
                    second_w_last_man_cho[i][j] = second_w.getJSONArray(i).getDouble(j);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //남자 마지막 글자 초성 가중치 얻기 끝

        // 남자 마지막 글자 중성 가중치 얻기 시작
        try {
            jsnobject = new JSONObject(json_last_man_jung);
            JSONArray first_w = jsnobject.getJSONArray("first_w");
            JSONArray second_w = jsnobject.getJSONArray("second_w");
            for (int i = 0; i < first_w.length(); i++) {
                for (int j = 0; j < first_w.getJSONArray(i).length(); j++) {
                    first_w_last_man_jung[i][j] = first_w.getJSONArray(i).getDouble(j);
                }
            }
            for (int i = 0; i < second_w.length(); i++) {
                for (int j = 0; j < second_w.getJSONArray(i).length(); j++) {
                    second_w_last_man_jung[i][j] = second_w.getJSONArray(i).getDouble(j);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //남자 마지막 글자 중성 가중치 얻기 끝
        // 남자 마지막 글자 종성 가중치 얻기 시작
        try {
            jsnobject = new JSONObject(json_last_man_jong);
            JSONArray first_w = jsnobject.getJSONArray("first_w");
            JSONArray second_w = jsnobject.getJSONArray("second_w");
            for (int i = 0; i < first_w.length(); i++) {
                for (int j = 0; j < first_w.getJSONArray(i).length(); j++) {
                    first_w_last_man_jong[i][j] = first_w.getJSONArray(i).getDouble(j);
                }
            }
            for (int i = 0; i < second_w.length(); i++) {
                for (int j = 0; j < second_w.getJSONArray(i).length(); j++) {
                    second_w_last_man_jong[i][j] = second_w.getJSONArray(i).getDouble(j);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //남자 마지막 글자 종성 가중치 얻기 끝

        // 여자 가운데 글자 초성 가중치 얻기 시작
        try {
            jsnobject = new JSONObject(json_mid_girl_cho);
            JSONArray first_w = jsnobject.getJSONArray("first_w");
            JSONArray second_w = jsnobject.getJSONArray("second_w");

            for (int i = 0; i < first_w.length(); i++) {
                for (int j = 0; j < first_w.getJSONArray(i).length(); j++) {
                    first_w_mid_girl_cho[i][j] = first_w.getJSONArray(i).getDouble(j);
                }
            }
            for (int i = 0; i < second_w.length(); i++) {
                for (int j = 0; j < second_w.getJSONArray(i).length(); j++) {
                    second_w_mid_girl_cho[i][j] = second_w.getJSONArray(i).getDouble(j);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 여자 가운데 글자 초성 가중치 얻기 종료

        // 여자 가운데 글자 중성 가중치 얻기 시작
        try {
            jsnobject = new JSONObject(json_mid_girl_jung);
            JSONArray first_w = jsnobject.getJSONArray("first_w");
            JSONArray second_w = jsnobject.getJSONArray("second_w");

            for (int i = 0; i < first_w.length(); i++) {
                for (int j = 0; j < first_w.getJSONArray(i).length(); j++) {
                    first_w_mid_girl_jung[i][j] = first_w.getJSONArray(i).getDouble(j);
                }
            }
            for (int i = 0; i < second_w.length(); i++) {
                for (int j = 0; j < second_w.getJSONArray(i).length(); j++) {
                    second_w_mid_girl_jung[i][j] = second_w.getJSONArray(i).getDouble(j);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 여자 가운데 글자 중성 가중치 얻기 종료

        // 여자 가운데 글자 종성 가중치 얻기 시작
        try {
            jsnobject = new JSONObject(json_mid_girl_jong);
            JSONArray first_w = jsnobject.getJSONArray("first_w");
            JSONArray second_w = jsnobject.getJSONArray("second_w");

            for (int i = 0; i < first_w.length(); i++) {
                for (int j = 0; j < first_w.getJSONArray(i).length(); j++) {
                    first_w_mid_girl_jong[i][j] = first_w.getJSONArray(i).getDouble(j);
                }
            }
            for (int i = 0; i < second_w.length(); i++) {
                for (int j = 0; j < second_w.getJSONArray(i).length(); j++) {
                    second_w_mid_girl_jong[i][j] = second_w.getJSONArray(i).getDouble(j);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 여자 가운데 글자 종성 가중치 얻기 종료

        // 여자 마지막 글자 초성 가중치 얻기 시작
        try {
            jsnobject = new JSONObject(json_last_girl_cho);
            JSONArray first_w = jsnobject.getJSONArray("first_w");
            JSONArray second_w = jsnobject.getJSONArray("second_w");

            for (int i = 0; i < first_w.length(); i++) {
                for (int j = 0; j < first_w.getJSONArray(i).length(); j++) {
                    first_w_last_girl_cho[i][j] = first_w.getJSONArray(i).getDouble(j);
                }
            }
            for (int i = 0; i < second_w.length(); i++) {
                for (int j = 0; j < second_w.getJSONArray(i).length(); j++) {
                    second_w_last_girl_cho[i][j] = second_w.getJSONArray(i).getDouble(j);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 여자 마지막 글자 초성 가중치 얻기 종료

        // 여자 마지막 글자 중성 가중치 얻기 시작
        try {
            jsnobject = new JSONObject(json_last_girl_jung);
            JSONArray first_w = jsnobject.getJSONArray("first_w");
            JSONArray second_w = jsnobject.getJSONArray("second_w");

            for (int i = 0; i < first_w.length(); i++) {
                for (int j = 0; j < first_w.getJSONArray(i).length(); j++) {
                    first_w_last_girl_jung[i][j] = first_w.getJSONArray(i).getDouble(j);
                }
            }
            for (int i = 0; i < second_w.length(); i++) {
                for (int j = 0; j < second_w.getJSONArray(i).length(); j++) {
                    second_w_last_girl_jung[i][j] = second_w.getJSONArray(i).getDouble(j);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 여자 마지막 글자 중성 가중치 얻기 종료
        // 여자 마지막 글자 종성 가중치 얻기 시작
        try {
            jsnobject = new JSONObject(json_last_girl_jong);
            JSONArray first_w = jsnobject.getJSONArray("first_w");
            JSONArray second_w = jsnobject.getJSONArray("second_w");

            for (int i = 0; i < first_w.length(); i++) {
                for (int j = 0; j < first_w.getJSONArray(i).length(); j++) {
                    first_w_last_girl_jong[i][j] = first_w.getJSONArray(i).getDouble(j);
                }
            }
            for (int i = 0; i < second_w.length(); i++) {
                for (int j = 0; j < second_w.getJSONArray(i).length(); j++) {
                    second_w_last_girl_jong[i][j] = second_w.getJSONArray(i).getDouble(j);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 여자 마지막 글자 종성 가중치 얻기 종료


    }

    private char[] hangulToJaso(String str) {
        int a, b, c; // 자소 버퍼: 초성/중성/종성 순
        char[] chars = new char[3];
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(0);
            if (ch >= 0xAC00 && ch <= 0xD7A3) { // "AC00:가" ~ "D7A3:힣" 에 속한 글자면 분해
                c = ch - 0xAC00;
                a = c / (21 * 28);
                c = c % (21 * 28);
                b = c / 28;
                c = c % 28;
                chars[0] = GetHangle.ChoSung[a];
                chars[1] = GetHangle.JungSung[b];

                //  result = result + GetHangle.ChoSung[a] + GetHangle.JungSung[b];
                if (c != 0) {
                    chars[2] = GetHangle.JongSung[c];
                } else {
                    chars[2] = ' ';
                }
            } else {
            }
        }
        return chars;
    }

    //  입력 성초성 12개 , 성중성14개, 성종성5개, 년//향후 100년 ,월//12개,일//31개,시//24개
    private void NewSetNameing_Man(double input[]) {
        for (int k = 0; k < hidden_count - 1; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_mid_man_cho[i][k];
            }
            hidden_mid_man_cho[k] = 1 / (1 + exp(-sum));
        }
        hidden_mid_man_cho[hidden_count - 1] = -1;

        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_mid_man_cho[i] * second_w_mid_man_cho[i][k];
            }
            out_mid_man_cho[k] = 1 / (1 + exp(-out_sum)); //남자 가운데 초성
        }

        for (int k = 0; k < hidden_count - 1; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_mid_man_jung[i][k];
            }
            hidden_mid_man_jung[k] = 1 / (1 + exp(-sum));
        }
        hidden_mid_man_jung[hidden_count - 1] = -1;

        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_mid_man_jung[i] * second_w_mid_man_jung[i][k];
            }
            out_mid_man_jung[k] = 1 / (1 + exp(-out_sum)); //남자 가운데 중성
        }

        for (int k = 0; k < hidden_count - 1; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_mid_man_jong[i][k];
            }
            hidden_mid_man_jong[k] = 1 / (1 + exp(-sum));
        }
        hidden_mid_man_jong[hidden_count - 1] = -1;

        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_mid_man_jong[i] * second_w_mid_man_jong[i][k];
            }
            out_mid_man_jong[k] = 1 / (1 + exp(-out_sum)); //남자 가운데 종성
        }

        for (int k = 0; k < hidden_count - 1; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_last_man_cho[i][k];
            }
            hidden_last_man_cho[k] = 1 / (1 + exp(-sum));
        }
        hidden_last_man_cho[hidden_count - 1] = -1;

        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_last_man_cho[i] * second_w_last_man_cho[i][k];
            }
            out_last_man_cho[k] = 1 / (1 + exp(-out_sum)); //남자 마지막 초성
        }

        for (int k = 0; k < hidden_count - 1; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_last_man_jung[i][k];
            }
            hidden_last_man_jung[k] = 1 / (1 + exp(-sum));
        }
        hidden_last_man_jung[hidden_count - 1] = -1;

        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_last_man_jung[i] * second_w_last_man_jung[i][k];
            }
            out_last_man_jung[k] = 1 / (1 + exp(-out_sum)); //남자 마지막 초성
        }

        for (int k = 0; k < hidden_count - 1; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_last_man_jong[i][k];
            }
            hidden_last_man_jong[k] = 1 / (1 + exp(-sum));
        }
        hidden_last_man_jong[hidden_count - 1] = -1;

        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_last_man_jong[i] * second_w_last_man_jong[i][k];
            }
            out_last_man_jong[k] = 1 / (1 + exp(-out_sum)); //남자 마지막 초성
        }

        String cho_one = GetHangle.Man_ChoSung_String[(int) GetNearValue.getNearCHO(out_mid_man_cho[0])];
        String jung_one = GetHangle.Man_JungSung_String[(int) GetNearValue.getNearJUNG(out_mid_man_jung[0])];
        String jong_one = GetHangle.Man_JongSung_String[(int) GetNearValue.getNearJONG(out_mid_man_jong[0])];

        Log.e("test", cho_one + jung_one + jong_one);

        String cho_two = GetHangle.Man_ChoSung_String[(int) GetNearValue.getNearCHO(out_last_man_cho[0])];
        String jung_two = GetHangle.Man_JungSung_String[(int) GetNearValue.getNearJUNG(out_last_man_jung[0])];
        String jong_two = GetHangle.Man_JongSung_String[(int) GetNearValue.getNearJONG(out_last_man_jong[0])];

        Log.e("test", cho_two + jung_two + jong_two);
        //한글 합치기 가운데 글자 시작//
        char[] a = cho_one.toCharArray();
        char[] b = jung_one.toCharArray();
        char[] c = jong_one.toCharArray();
        int cho_index = 0, jung_index = 0, jong_index = 0;
        for (int i = 0; i < GetHangle.ChoSung.length; i++) {
            if (a[0] == GetHangle.ChoSung[i]) {
                cho_index = i;
            }
        }
        for (int i = 0; i < GetHangle.JungSung.length; i++) {
            if (b[0] == GetHangle.JungSung[i]) {
                jung_index = i;
            }
        }
        for (int i = 0; i < GetHangle.JongSung.length; i++) {
            if (c[0] == GetHangle.JongSung[i]) {
                jong_index = i;
            }
        }
        //한글 합치기 가운데 글자 끝//
        //한글 합치기 마지막 글자 시작//
        char[] a2 = cho_two.toCharArray();
        char[] b2 = jung_two.toCharArray();
        char[] c2 = jong_two.toCharArray();
        int cho_index2 = 0, jung_index2 = 0, jong_index2 = 0;
        for (int i = 0; i < GetHangle.ChoSung.length; i++) {
            if (a2[0] == GetHangle.ChoSung[i]) {
                cho_index2 = i;
            }
        }
        for (int i = 0; i < GetHangle.JungSung.length; i++) {
            if (b2[0] == GetHangle.JungSung[i]) {
                jung_index2 = i;
            }
        }
        for (int i = 0; i < GetHangle.JongSung.length; i++) {
            if (c2[0] == GetHangle.JongSung[i]) {
                jong_index2 = i;
            }
        }
        //한글 합치기 마지막 글자 끝//
        char characterValue_one = (char) (0xAC00 + (cho_index * 21 * 28) + (jung_index * 28) + jong_index);
        char characterValue_two = (char) (0xAC00 + (cho_index2 * 21 * 28) + (jung_index2 * 28) + jong_index2);
        String naming = name_edit.getText().toString() + characterValue_one + characterValue_two;
        result_text.setText(naming);
    }

    private void NewSetNameing_Girl(double input[]) {

        for (int k = 0; k < hidden_count - 1; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_mid_girl_cho[i][k];
            }
            hidden_mid_girl_cho[k] = 1 / (1 + exp(-sum));
        }
        hidden_mid_girl_cho[hidden_count - 1] = -1;

        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_mid_girl_cho[i] * second_w_mid_girl_cho[i][k];
            }
            out_mid_girl_cho[k] = 1 / (1 + exp(-out_sum)); //남자 가운데 초성
        }

        for (int k = 0; k < hidden_count - 1; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_mid_girl_jung[i][k];
            }
            hidden_mid_girl_jung[k] = 1 / (1 + exp(-sum));
        }
        hidden_mid_girl_jung[hidden_count - 1] = -1;

        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_mid_girl_jung[i] * second_w_mid_girl_jung[i][k];
            }
            out_mid_girl_jung[k] = 1 / (1 + exp(-out_sum)); //남자 가운데 중성
        }

        for (int k = 0; k < hidden_count - 1; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_mid_girl_jong[i][k];
            }
            hidden_mid_girl_jong[k] = 1 / (1 + exp(-sum));
        }
        hidden_mid_girl_jong[hidden_count - 1] = -1;

        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_mid_girl_jong[i] * second_w_mid_girl_jong[i][k];
            }
            out_mid_girl_jong[k] = 1 / (1 + exp(-out_sum)); //남자 가운데 종성
        }

        for (int k = 0; k < hidden_count - 1; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_last_girl_cho[i][k];
            }
            hidden_last_girl_cho[k] = 1 / (1 + exp(-sum));
        }
        hidden_last_girl_cho[hidden_count - 1] = -1;

        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_last_girl_cho[i] * second_w_last_girl_cho[i][k];
            }
            out_last_girl_cho[k] = 1 / (1 + exp(-out_sum)); //남자 마지막 초성
        }

        for (int k = 0; k < hidden_count - 1; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_last_girl_jung[i][k];
            }
            hidden_last_girl_jung[k] = 1 / (1 + exp(-sum));
        }
        hidden_last_girl_jung[hidden_count - 1] = -1;

        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_last_girl_jung[i] * second_w_last_girl_jung[i][k];
            }
            out_last_girl_jung[k] = 1 / (1 + exp(-out_sum)); //남자 마지막 초성
        }

        for (int k = 0; k < hidden_count - 1; k++) {
            double sum = 0;
            for (int i = 0; i < input_count; i++) {
                sum += input[i] * first_w_last_girl_jong[i][k];
            }
            hidden_last_girl_jong[k] = 1 / (1 + exp(-sum));
        }
        hidden_last_girl_jong[hidden_count - 1] = -1;

        for (int k = 0; k < out_count; k++) {
            double out_sum = 0;
            for (int i = 0; i < hidden_count; i++) {
                out_sum += hidden_last_girl_jong[i] * second_w_last_girl_jong[i][k];
            }
            out_last_girl_jong[k] = 1 / (1 + exp(-out_sum)); //남자 마지막 초성
        }


        String cho_one = GetHangle.Girl_ChoSung_String[(int) GetNearValue.getNearCHO(out_mid_girl_cho[0])];
        String jung_one = GetHangle.Girl_JungSung_String[(int) GetNearValue.getNearJUNG(out_mid_girl_jung[0])];
        String jong_one = GetHangle.Girl_JongSung_String[(int) GetNearValue.getNearJONG(out_mid_girl_jong[0])];

        String cho_two = GetHangle.Girl_ChoSung_String[(int) GetNearValue.getNearCHO(out_last_girl_cho[0])];
        String jung_two = GetHangle.Girl_JungSung_String[(int) GetNearValue.getNearJUNG(out_last_girl_jung[0])];
        String jong_two = GetHangle.Girl_JongSung_String[(int) GetNearValue.getNearJONG(out_last_girl_jong[0])];

        //한글 합치기 가운데 글자 시작//
        char[] a = cho_one.toCharArray();
        char[] b = jung_one.toCharArray();
        char[] c = jong_one.toCharArray();
        int cho_index = 0, jung_index = 0, jong_index = 0;
        for (int i = 0; i < GetHangle.ChoSung.length; i++) {
            if (a[0] == GetHangle.ChoSung[i]) {
                cho_index = i;
            }
        }
        for (int i = 0; i < GetHangle.JungSung.length; i++) {
            if (b[0] == GetHangle.JungSung[i]) {
                jung_index = i;
            }
        }
        for (int i = 0; i < GetHangle.JongSung.length; i++) {
            if (c[0] == GetHangle.JongSung[i]) {
                jong_index = i;
            }
        }
        //한글 합치기 가운데 글자 끝//
        //한글 합치기 마지막 글자 시작//
        char[] a2 = cho_two.toCharArray();
        char[] b2 = jung_two.toCharArray();
        char[] c2 = jong_two.toCharArray();
        int cho_index2 = 0, jung_index2 = 0, jong_index2 = 0;
        for (int i = 0; i < GetHangle.ChoSung.length; i++) {
            if (a2[0] == GetHangle.ChoSung[i]) {
                cho_index2 = i;
            }
        }
        for (int i = 0; i < GetHangle.JungSung.length; i++) {
            if (b2[0] == GetHangle.JungSung[i]) {
                jung_index2 = i;
            }
        }
        for (int i = 0; i < GetHangle.JongSung.length; i++) {
            if (c2[0] == GetHangle.JongSung[i]) {
                jong_index2 = i;
            }
        }
        //한글 합치기 마지막 글자 끝//
        char characterValue_one = (char) (0xAC00 + (cho_index * 21 * 28) + (jung_index * 28) + jong_index);
        char characterValue_two = (char) (0xAC00 + (cho_index2 * 21 * 28) + (jung_index2 * 28) + jong_index2);
        String naming = name_edit.getText().toString() + characterValue_one + characterValue_two;
        result_text.setText(naming);
    }
}
