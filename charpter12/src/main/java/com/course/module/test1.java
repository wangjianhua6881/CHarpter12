package com.course.module;

public class test1 {
    public static void main(String[] args) {

        for (int i = 1; i < 10; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(i  + "*" + j + "=" + i*j + "\t");
            }
            System.out.println();
        }

        System.out.println("---------------分割线---------------");

        int[] a = new int[]{5,6,9,7,1,2,3,4};

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length - i -1; j++) {
                if (a[j] > a[j+1]){
                    int b = a[j];
                    a[j] = a[j+1];
                    a[j+1] = b;
                }
            }
        }

        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }


}
