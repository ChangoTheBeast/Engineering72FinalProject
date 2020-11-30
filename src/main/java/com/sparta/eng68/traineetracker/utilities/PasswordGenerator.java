package com.sparta.eng68.traineetracker.utilities;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class PasswordGenerator {

    private static Random random = new Random();

    public static String generatePassword() {

        char[] passwordArray = new char[10];

        for (int i = 0; i < 10; i++) {
            int intGen = random.nextInt(61);

            if (intGen < 26) {
                passwordArray[i] = generateRandoma_z();
            } else if (intGen < 52) {
                passwordArray[i] = generateRandomA_Z();
            } else {
                passwordArray[i] = generateRandom0_9();
            }

        }

        return new String(passwordArray);
    }

    public static char generateRandomA_Z() {
        int i = random.nextInt(26) + 65;
        return (char) i;
    }

    public static char generateRandoma_z() {
        int i = random.nextInt(26) + 97;
        return (char) i;
    }

    public static char generateRandom0_9() {
        int i = random.nextInt(10) + 48;
        return (char) i;
    }

    public static void writeToFile(String username, String password) {
        try{
            File file = new File("src/main/resources/login.csv");
            file.createNewFile();

            CSVLoginWriter csvLoginWriter = new CSVLoginWriter(file);
            csvLoginWriter.writeStringRecordToFile(username + "," + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
