package com.flight.query.sequential;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
        List<List<Airport>> res = SequentialQuery.ttcQuery(startDate, endDate);
        for (List<Airport> pair : res) {
            System.out.println(pair.get(0).getId() + " " + pair.get(1).getId());
        }
        System.out.println(res.size());
    }
}
