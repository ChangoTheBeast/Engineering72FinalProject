package com.sparta.eng72.traineetracker.utilities;

import com.sparta.eng72.traineetracker.entities.CSVUser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVLoginWriter {

    File fileName;
    BufferedWriter bufferedWriter;

    //Constructor which sets the filePath to write to
    public CSVLoginWriter (File fileName) throws IOException {
        this.fileName = fileName;
        bufferedWriter = new BufferedWriter(new FileWriter(fileName, true));
    }

    public void addNewUser(String username) {

        CSVUser user = new CSVUser(username, PasswordGenerator.generatePassword(), Role.FIRST_TIME_USER);


    }

    //Writes the csv String straight to the file
    public void writeStringRecordToFile(String record) throws IOException {
        bufferedWriter.write(record);
        bufferedWriter.newLine();
        close();
    }

    //Close the writer object
    public void close() throws IOException {
        bufferedWriter.close();
    }

}
