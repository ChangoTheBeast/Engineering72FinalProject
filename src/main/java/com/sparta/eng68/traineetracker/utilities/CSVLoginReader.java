package com.sparta.eng68.traineetracker.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVLoginReader {

    private String filePath;
    private BufferedReader bufferedReader;

    //Constructor for associating a CSVReader object with a file
    public CSVLoginReader(String filePath) throws IOException {
        this.filePath = filePath;
        bufferedReader = new BufferedReader(new FileReader(filePath));

        //Skips the header values on the firstLine
        String header = bufferedReader.readLine();
    }

    //Returns the next line in the file
    public String getNextCSVLine() throws IOException{
        String nextLine = bufferedReader.readLine();
        if (nextLine == null) {
            return null;
        }
        return nextLine;
    }

    public String getFilePath() {
        return filePath;
    }

    public void close() throws IOException{
        bufferedReader.close();
    }

}

