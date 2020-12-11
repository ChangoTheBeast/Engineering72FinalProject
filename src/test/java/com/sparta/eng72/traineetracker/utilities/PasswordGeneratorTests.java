package com.sparta.eng72.traineetracker.utilities;

import com.sparta.eng72.traineetracker.utilities.PasswordGenerator;
import org.junit.jupiter.api.Test;

public class PasswordGeneratorTests {

    @Test
    void testA_ZCharacters() {
        for (int i = 0; i < 100; i++) {
            System.out.println(PasswordGenerator.generateRandomA_Z());
        }
    }

    @Test
    void testa_zCharacters() {
        for (int i = 0; i < 100; i++) {
            System.out.println(PasswordGenerator.generateRandoma_z());
        }
    }

    @Test
    void test0_9Characters() {
        for (int i = 0; i < 100; i++) {
            System.out.println(PasswordGenerator.generateRandom0_9());
        }
    }

    @Test
    void testGeneratePassword() {
        for (int i = 0; i < 20; i++) {
            System.out.println(PasswordGenerator.generatePassword());
        }
    }
}
