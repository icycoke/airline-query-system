package com.flight.query.sequential;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Input the time limit (min): ");
        String inputString = sc.nextLine();
        int timeLimit = Integer.valueOf(inputString).intValue();
        List<List<Airport>> res = SequentialQuery.ttcQuery(timeLimit);
        for (List<Airport> pair : res) {
            System.out.println(pair.get(0).getId() + " " + pair.get(1).getId());
        }
        System.out.println(res.size());
    }
}
