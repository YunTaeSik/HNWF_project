package com.ai.project.hnwf_project.util;

/**
 * Created by sky87 on 2016-11-28.
 */

public class GetNearValue {
    public static double getNearCHO(double output) {
        double data[] = null;
        data = new double[]{0.0 / 11.0, 1.0 / 11.0, 2.0 / 11.0, 3.0 / 11.0, 4.0 / 11.0, 5.0 / 11.0,
                6.0 / 11.0, 7.0 / 11.0, 8.0 / 11.0, 9.0 / 11.0, 10.0 / 11.0, 11.0 / 11.0};
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
        data = new double[]{0.0 / 13.0, 1.0 / 13.0, 2.0 / 13.0, 3.0 / 13.0, 4.0 / 13.0, 5.0 / 13.0
                , 6.0 / 13.0, 7.0 / 13.0, 8.0 / 13.0, 9.0 / 13.0, 10.0 / 13.0
                , 11.0 / 13.0, 12.0 / 13.0, 13.0 / 13.0}; // 데이터
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

        data = new double[]{0.0 / 4.0, 1.0 / 4.0, 2.0 / 4.0, 3.0 / 4.0, 4.0 / 4.0};
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
