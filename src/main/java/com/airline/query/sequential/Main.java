package com.airline.query.sequential;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Input the start date (YYYY-MM-DD): ");
        String[] startArr = sc.nextLine().split("-");
        System.out.println("Input the end date (YYYY-MM-DD): ");
        String[] endArr = sc.nextLine().split("-");
        Date startDate = new Date(Integer.valueOf(startArr[0]) - 1900,
                Integer.valueOf(startArr[1]) - 1,
                Integer.valueOf(startArr[2]));
        Date endDate = new Date(Integer.valueOf(endArr[0]) - 1900,
                Integer.valueOf(endArr[1]) - 1,
                Integer.valueOf(endArr[2]));
        SequentialQuery.query(startDate, endDate);
    }
}
