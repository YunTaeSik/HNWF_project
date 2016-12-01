package com.ai.project.hnwf_project.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class InformationFragment extends Fragment implements View.OnClickListener {
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

    public InformationFragment() {
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

        getWeight(); //가중치 얻기

        naming_layout_btn.setOnClickListener(this);
        boy_btn.setOnClickListener(this);
        girl_btn.setOnClickListener(this);
        boy_btn.setChecked(true);
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

                        double input[] = {cho_index / 11.0, jung_index / 13.0, jong_index / 4.0, year / 100.0, month / 12.0, date / 31.0, time / 24.0};
                        //double input[] = {2.0 / 11.0, 9.0 / 13.0, 0.0 / 4.0, 0.0 / 100.0, 11.0 / 12.0, 27.0 / 31.0, 3.0 / 24.0};
                        SetNameing_Man(input);
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

                        double input[] = {cho_index / 11.0, jung_index / 13.0, jong_index / 4.0, year / 100.0, month / 12.0, date / 31.0, time / 24.0};
                        SetNameing_Girl(input);
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

    private void getWeight() {
        dbManager = new DBManager(getContext(), "WEIGHT", null, 1);
        SQLiteDatabase db = dbManager.getWritableDatabase();
        Cursor cursor_man_one = db.query("'" + Contact.MAN_WEIGHT_ONE + "'", null, null, null, null, null, null);
        Cursor cursor_man_two = db.query("'" + Contact.MAN_WEIGHT_TWO + "'", null, null, null, null, null, null);
        Cursor cursor_girl_one = db.query("'" + Contact.GIRL_WEIGHT_ONE + "'", null, null, null, null, null, null);
        Cursor cursor_girl_two = db.query("'" + Contact.GIRL_WEIGHT_TWO + "'", null, null, null, null, null, null);

        cursor_man_one.moveToNext();
        cursor_man_two.moveToNext();
        cursor_girl_one.moveToNext();
        cursor_girl_two.moveToNext();

        String json_man_one = cursor_man_one.getString(1);
        String json_man_two = cursor_man_two.getString(1);
        String json_girl_one = cursor_girl_one.getString(1);
        String json_girl_two = cursor_girl_two.getString(1);

        //남자 가운데 글자 가중치얻기 시작
        JSONObject jsnobject = null;
        try {
            jsnobject = new JSONObject(json_man_one);
            JSONArray jsonArray_manone_f = jsnobject.getJSONArray("first_w_man_one");
            JSONArray jsonArray_manone_s = jsnobject.getJSONArray("second_w_man_one");

            for (int i = 0; i < jsonArray_manone_f.length(); i++) {
                for (int j = 0; j < jsonArray_manone_f.getJSONArray(i).length(); j++) {
                    first_w_man_one[i][j] = jsonArray_manone_f.getJSONArray(i).getDouble(j);
                }
            }
            for (int i = 0; i < jsonArray_manone_s.length(); i++) {
                for (int j = 0; j < jsonArray_manone_s.getJSONArray(i).length(); j++) {
                    second_w_man_one[i][j] = jsonArray_manone_s.getJSONArray(i).getDouble(j);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //남자 가운데 글자 가중치얻기 종료
        //남자 마지막 글자 가중치얻기 시작
        try {
            jsnobject = new JSONObject(json_man_two);
            JSONArray jsonArray_mantwo_f = jsnobject.getJSONArray("first_w_man_two");
            JSONArray jsonArray_mantwo_s = jsnobject.getJSONArray("second_w_man_two");

            for (int i = 0; i < jsonArray_mantwo_f.length(); i++) {
                for (int j = 0; j < jsonArray_mantwo_f.getJSONArray(i).length(); j++) {
                    first_w_man_two[i][j] = jsonArray_mantwo_f.getJSONArray(i).getDouble(j);
                }
            }
            for (int i = 0; i < jsonArray_mantwo_s.length(); i++) {
                for (int j = 0; j < jsonArray_mantwo_s.getJSONArray(i).length(); j++) {
                    second_w_man_two[i][j] = jsonArray_mantwo_s.getJSONArray(i).getDouble(j);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //남자 마지막 글자 가중치얻기 종료
        // 여자 가운데 글자 가중치얻기 시작
        try {
            jsnobject = new JSONObject(json_girl_one);
            JSONArray jsonArray_girlone_f = jsnobject.getJSONArray("first_w_girl_one");
            JSONArray jsonArray_girlone_s = jsnobject.getJSONArray("second_w_girl_one");

            for (int i = 0; i < jsonArray_girlone_f.length(); i++) {
                for (int j = 0; j < jsonArray_girlone_f.getJSONArray(i).length(); j++) {
                    first_w_girl_one[i][j] = jsonArray_girlone_f.getJSONArray(i).getDouble(j);
                }
            }
            for (int i = 0; i < jsonArray_girlone_s.length(); i++) {
                for (int j = 0; j < jsonArray_girlone_s.getJSONArray(i).length(); j++) {
                    second_w_girl_one[i][j] = jsonArray_girlone_s.getJSONArray(i).getDouble(j);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //여자 가운데 글자 가중치얻기 종료   // 여자 가운데 글자 가중치얻기 시작
        try {
            jsnobject = new JSONObject(json_girl_two);
            JSONArray jsonArray_girltwo_f = jsnobject.getJSONArray("first_w_girl_two");
            JSONArray jsonArray_girltwo_s = jsnobject.getJSONArray("second_w_girl_two");

            for (int i = 0; i < jsonArray_girltwo_f.length(); i++) {
                for (int j = 0; j < jsonArray_girltwo_f.getJSONArray(i).length(); j++) {
                    first_w_girl_two[i][j] = jsonArray_girltwo_f.getJSONArray(i).getDouble(j);
                }
            }
            for (int i = 0; i < jsonArray_girltwo_s.length(); i++) {
                for (int j = 0; j < jsonArray_girltwo_s.getJSONArray(i).length(); j++) {
                    second_w_girl_two[i][j] = jsonArray_girltwo_s.getJSONArray(i).getDouble(j);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //여자 가운데 글자 가중치얻기 종료

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
        String cho_one = GetHangle.Man_ChoSung_String[(int) GetNearValue.getNearCHO(out_man_one[0])];
        String jung_one = GetHangle.Man_JungSung_String[(int) GetNearValue.getNearJUNG(out_man_one[1])];
        String jong_one = GetHangle.Man_JongSung_String[(int) GetNearValue.getNearJONG(out_man_one[2])];

        String cho_two = GetHangle.Man_ChoSung_String[(int) GetNearValue.getNearCHO(out_man_two[0])];
        String jung_two = GetHangle.Man_JungSung_String[(int) GetNearValue.getNearJUNG(out_man_two[1])];
        String jong_two = GetHangle.Man_JongSung_String[(int) GetNearValue.getNearJONG(out_man_two[2])];
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
        String cho_one = GetHangle.Girl_ChoSung_String[(int) GetNearValue.getNearCHO(out_girl_one[0])];
        String jung_one = GetHangle.Girl_JungSung_String[(int) GetNearValue.getNearJUNG(out_girl_one[1])];
        String jong_one = GetHangle.Girl_JongSung_String[(int) GetNearValue.getNearJONG(out_girl_one[2])];

        String cho_two = GetHangle.Girl_ChoSung_String[(int) GetNearValue.getNearCHO(out_girl_two[0])];
        String jung_two = GetHangle.Girl_JungSung_String[(int) GetNearValue.getNearJUNG(out_girl_two[1])];
        String jong_two = GetHangle.Girl_JongSung_String[(int) GetNearValue.getNearJONG(out_girl_two[2])];

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
