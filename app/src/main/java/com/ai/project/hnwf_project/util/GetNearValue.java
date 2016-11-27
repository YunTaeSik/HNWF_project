package com.ai.project.hnwf_project.util;

/**
 * Created by sky87 on 2016-11-28.
 */

public class GetNearValue {
    public static double getNearCHO(double output) {
        double data[] = null;
        data = new double[]{0.0 / 12.0, 1.0 / 12.0, 2.0 / 12.0, 3.0 / 12.0, 4.0 / 12.0, 5.0 / 12.0,
                6.0 / 12.0, 7.0 / 12.0, 8.0 / 12.0, 9.0 / 12.0, 10.0 / 12.0, 11.0 / 12.0, 12.0 / 12.0};
        double min = 1.0;    // 기준데이터 최소값 - Interger형의 최대값으로 값을 넣는다.
        double nearData = 0.0;                       // 가까운 값을 저장할 변수
        // 2. process
        for (int i = 0; i < data.length; i++) {
            double a = Math.abs(data[i] - output);  // 절대값을 취한다.
            if (min > a) {
                min = a;
                nearData = i; //제일 근접한 포지션 가지고오기
            }
        }
        return nearData;
    }

    public static double getNearJUNG(double output) {
        double data[] = null;
        data = new double[]{0.0 / 18.0, 1.0 / 18.0, 2.0 / 18.0, 3.0 / 18.0, 4.0 / 18.0, 5.0 / 18.0
                , 6.0 / 18.0, 7.0 / 18.0, 8.0 / 18.0, 9.0 / 18.0, 10.0 / 18.0
                , 11.0 / 18.0, 12.0 / 18.0, 13.0 / 18.0, 14.0 / 18.0, 15.0 / 18.0
                , 16.0 / 18.0, 17.0 / 18.0, 18.0 / 18.0}; // 데이터
        double min = 1.0;    // 기준데이터 최소값 - Interger형의 최대값으로 값을 넣는다.
        double nearData = 0.0;                       // 가까운 값을 저장할 변수
        // 2. process
        for (int i = 0; i < data.length; i++) {
            double a = Math.abs(data[i] - output);  // 절대값을 취한다.
            if (min > a) {
                min = a;
                nearData = i; //제일 근접한 포지션 가지고오기
            }
        }
        return nearData;
    }

    public static double getNearJONG(double output) {
        double data[] = null;

        data = new double[]{0.0 / 5.0, 1.0 / 5.0, 2.0 / 5.0, 3.0 / 5.0, 4.0 / 5.0, 5.0 / 5.0};
        double min = 1.0;    // 기준데이터 최소값 - Interger형의 최대값으로 값을 넣는다.
        double nearData = 0.0;                       // 가까운 값을 저장할 변수
        // 2. process
        for (int i = 0; i < data.length; i++) {
            double a = Math.abs(data[i] - output);  // 절대값을 취한다.
            if (min > a) {
                min = a;
                nearData = i; //제일 근접한 포지션 가지고오기
            }
        }
        return nearData;
    }
}
