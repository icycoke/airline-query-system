package com.airline.query.sequential;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.*;
import java.util.Date;

/**
 * A class represent a sequential query for airlines
 */
public class SequentialQuery {
    /**
     * Proceed a sequential query for airlines
     *
     * @param startDate the start time of the query period T
     * @param endDate   the end time of the query period T
     */
    static void query(Date startDate, Date endDate) throws IOException {
        // The path of the input airline data
        String inputPath = "./input/input.txt";
        // The path of the output result of the query
        String outputPath = "./output/sequential/output";

        // The input file
        File inputFile = new File(inputPath);
        // The output file
        File outputFile = new File(outputPath);

        // The start time of the query
        long startTime = System.currentTimeMillis();

        try {
            // A buffer reader contains the file reader of the input file
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            // A buffer writer contains the file writer of the output file
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

            // The current line
            String curLine;

            // Read each line and write it into output file if the date of the airline is in T
            while ((curLine = br.readLine()) != null) {
                // The array contains column data of current line
                String[] strArr;

                strArr = curLine.split("\"");
                if (strArr.length == 14) {
                    // The array contains year, month, and day of current line
                    String[] dateArr = strArr[1].split("-");
                    if (dateArr.length == 3) {
                        // The date of the airline
                        Date curDate = new Date(Integer.valueOf(dateArr[0]) - 1900,
                                Integer.valueOf(dateArr[1]) - 1,
                                Integer.valueOf(dateArr[2]));
                        if (curDate.compareTo(startDate) >= 0 && curDate.compareTo(endDate) <= 0) {
                            bw.write(curLine);
                            bw.write("\n");
                        }
                    }
                }
            }

            br.close();
            bw.close();
        } catch (IOException e) {
            System.out.println("Error!");
            e.printStackTrace();
        } finally {
            // The end time of the query
            long endTime = System.currentTimeMillis();
            System.out.println("Query finished!");
            System.out.println("Time cost: " + (endTime - startTime) + "ms");
        }
    }
}
