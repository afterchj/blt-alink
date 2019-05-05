package com.tpadsz.after.util;

import java.util.Random;

/**
 * Created by chenhao.lu on 2019/4/11.
 */
public class GenerateUtils {


    public static String getCharAndNumr(int length) {
        String val = "";
        String charOrNum;
        int num1 = 0;
        int num2 = 0;
        String reg = "^.*\\d{3}.*$";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            if (num1 == 5) {
                charOrNum = "num";
            } else if (num2 == 3) {
                if (val.matches(reg)) {
                    val = val.substring(0, val.length() - 1);
                    i--;
                    num2--;
                }
                charOrNum = "char";
            } else {
                charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            }
            int isDuplicateNum = 0;
            int randomValue;
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
//                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                do {
                    randomValue = 65 + random.nextInt(26);
                    char[] arr = val.toCharArray();
                    for (int j = 0; j < arr.length; j++) {
                        if(randomValue==arr[j]){
                            isDuplicateNum++;
                        }
                    }
                } while (isDuplicateNum==2);
                val += (char) randomValue;
                num1++;
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                randomValue = random.nextInt(10);
                if(num2==2){
                    do {
                        String[] arr = val.split("");
                        for (int j = 0; j < arr.length; j++) {
                            if(String.valueOf(randomValue).equals(arr[j])){
                                isDuplicateNum++;
                            }
                        }
                    } while (isDuplicateNum==2);
                }
                val += String.valueOf(randomValue);
                num2++;
            }
        }
        return val;
    }


    public static boolean check(String str) {
        char[] arr=str.toCharArray();
        boolean flag = true;
        for (int i = 1; i < arr.length - 1; i++) {
            int firstIndex = (int)arr[i-1];
            int secondIndex = (int)arr[i];
            int thirdIndex = (int)arr[i+1];
            if ((thirdIndex - secondIndex == 1) && (secondIndex - firstIndex == 1)) {
                flag = false;
            }
        }
        return flag;
    }


    public static String randomPwd() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    public static void main(String[] args) {
        String str="123T8QSY";
        while (!check(str)){
            System.out.println("str="+str);
            str=getCharAndNumr(8);
        }
        System.out.println(str);
    }
}
