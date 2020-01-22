## HNWF_project
  
  
**프로젝트 기간**  
2016.11 ~ 2016.12

**소속**  
한성대학교 프로젝트
  
**담당역할**  
인공지능 머신러닝 알고리즘 제작 (3층) , 훈련데이터 적용, Android App 제작

**주사용 기술**  
알고리즘 제작, UI 적용, 간단한 Animation 적용 등등
  
  
**프로젝트 내용**  
한성대학교 인공지능 수업에서 배운 머신러닝 알고리즘을 제작하여 사주팔자와 이름간에 관계를 파악하는 프로젝트를 진행했습니다.  
사주팔자(생년월시등)을 입력값으로 좋은 이름을 출력값을 넣어 훈련을 시켜 결과값을 도출하게 만들었습니다.  
데이터가 부족하여 완벽한 이름 결과값을 도출할 수 없었지만, 머신러닝의 어려움 과 동양학에 대해 알 수 있었던 좋은 프로젝트였습니다.  

## ANN  

**입력값**  
성초성/ 성중성/ 성종성/ 년/월/일/시  

```java
public static double man_Input_1[] = {2.0 / 11.0, 9.0 / 13.0, 0.0 / 4.0, 0.0 / 100.0, 11.0 / 12.0, 27.0 / 31.0, 3.0 / 24.0, -1.0}; // 윤 2016 11 27 3 남
public static double man_Input_2[] = {6.0 / 11.0, 1.0 / 13.0, 4.0 / 4.0, 0.0 / 100.0, 1.0 / 12.0, 1.0 / 31.0, 2.0 / 24.0, -1.0}; // 김 2016 1 1 2 남
```
  
**출력값**  
초성/중성/종성  
```java
public final static String[] Man_ChoSung_String = {  // 12개
            "ㅈ", "ㅎ", "ㅇ", "ㅅ", "ㅁ",
            "ㄷ", "ㄱ", "ㄴ", "ㄹ", "ㅂ",
            "ㅌ", "ㅊ"};
public final static String[] Man_JungSung_String = {  //14개
            "ㅜ", "ㅣ", "ㅕ", "ㅓ", "ㅗ",
            "ㅖ", "ㅐ", "ㅡ", "ㅝ", "ㅠ",
            "ㅏ", "ㅘ", "ㅚ", "ㅛ"};
public final static String[] Man_JongSung_String = {   //5개
            "ㄴ", " ", "ㅇ", "ㄱ", "ㅁ"};
```
  
**초기 가중치**
```java
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
```
  
  **트레이닝 알고리즘**  
  ```java
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
                double differential = (n * (target[all][0] - out[all][k]) * (1 - out[all][k]) * out[all][k]);

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
  ```
  
## 이미지  

<div>
   <img src="https://user-images.githubusercontent.com/23161645/72855786-f8644480-3cfb-11ea-84e4-dc9e56ee107d.jpg" hspace=8 width="250">
   <img src="https://user-images.githubusercontent.com/23161645/72855788-fbf7cb80-3cfb-11ea-8ae8-375c4d9d00b7.jpg" hspace=8 width="250">
   <img src="https://user-images.githubusercontent.com/23161645/72855790-fd28f880-3cfb-11ea-8435-2c35ed84fc92.jpg" hspace=8 width="250">
</div>

