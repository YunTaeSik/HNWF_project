package com.ai.project.hnwf_project.intro;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.ai.project.hnwf_project.R;
import com.ai.project.hnwf_project.main.MainActivity;
import com.ai.project.hnwf_project.traing_girl.Last_GirlCho_Service;
import com.ai.project.hnwf_project.traing_girl.Last_GirlJong_Service;
import com.ai.project.hnwf_project.traing_girl.Last_GirlJung_Service;
import com.ai.project.hnwf_project.traing_girl.Mid_GirlCho_Service;
import com.ai.project.hnwf_project.traing_girl.Mid_GirlJong_Service;
import com.ai.project.hnwf_project.traing_girl.Mid_GirlJung_Service;
import com.ai.project.hnwf_project.traing_man.Last_ManCho_Service;
import com.ai.project.hnwf_project.traing_man.Last_ManJong_Service;
import com.ai.project.hnwf_project.traing_man.Last_ManJung_Service;
import com.ai.project.hnwf_project.traing_man.Mid_ManCho_Service;
import com.ai.project.hnwf_project.traing_man.Mid_ManJong_Service;
import com.ai.project.hnwf_project.traing_man.Mid_ManJung_Service;
import com.ai.project.hnwf_project.util.Contact;


/**
 * Created by YunTaeSik on 2016-08-15.
 */
public class IntroActivity extends Activity implements Animation.AnimationListener {

    private Handler h;//핸들러 선언
    private Animation alphaAni;
    private Animation alphaAni_one;
    private Animation alphaAni_two;
    private Animation alphaAni_three;
    private Animation alphaAni_four;
    private TextView[] loading_circle = new TextView[4];
    private TextView taring_text;
    private int traningCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //인트로화면이므로 타이틀바를 없앤다
        setContentView(R.layout.activity_intro);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        registerReceiver(broadcastReceiver, new IntentFilter(Contact.WEIGHT_TRAING));
        //startService(new Intent(this, TraningService.class));
        startService();

        loading_circle[0] = (TextView) findViewById(R.id.loading_circle_one);
        loading_circle[1] = (TextView) findViewById(R.id.loading_circle_two);
        loading_circle[2] = (TextView) findViewById(R.id.loading_circle_three);
        loading_circle[3] = (TextView) findViewById(R.id.loading_circle_four);
        taring_text = (TextView) findViewById(R.id.taring_text);


        alphaAni_one = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
        alphaAni_two = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_two);
        alphaAni_three = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_three);
        alphaAni_four = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_four);


        // loge_text.startAnimation(alphaAni);
        loading_circle[0].startAnimation(alphaAni_one);
        alphaAni_one.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loading_circle[1].startAnimation(alphaAni_two);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        alphaAni_two.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loading_circle[2].startAnimation(alphaAni_three);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        alphaAni_three.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loading_circle[3].startAnimation(alphaAni_four);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        alphaAni_four.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loading_circle[0].startAnimation(alphaAni_one);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {


    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Contact.WEIGHT_TRAING)) {
                traningCount++;
                Log.e("traningCount", String.valueOf(traningCount));
                if (traningCount >= 12) {
                    taring_text.setText("훈련완료!");
                    Intent i = new Intent(IntroActivity.this, MainActivity.class); //인텐트 생성(현 액티비티, 새로 실행할 액티비티)
                    startActivity(i);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
          /*      taring_text.setText("훈련완료!");
                Intent i = new Intent(IntroActivity.this, MainActivity.class); //인텐트 생성(현 액티비티, 새로 실행할 액티비티)
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);*/
            }
        }
    };

    private void startService() {
        startService(new Intent(this, Mid_ManCho_Service.class));
        startService(new Intent(this, Mid_ManJung_Service.class));
        startService(new Intent(this, Mid_ManJong_Service.class));

        startService(new Intent(this, Last_ManCho_Service.class));
        startService(new Intent(this, Last_ManJung_Service.class));
        startService(new Intent(this, Last_ManJong_Service.class));

        startService(new Intent(this, Mid_GirlCho_Service.class));
        startService(new Intent(this, Mid_GirlJung_Service.class));
        startService(new Intent(this, Mid_GirlJong_Service.class));

        startService(new Intent(this, Last_GirlCho_Service.class));
        startService(new Intent(this, Last_GirlJung_Service.class));
        startService(new Intent(this, Last_GirlJong_Service.class));


    }

}
