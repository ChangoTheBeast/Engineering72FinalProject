package com.sparta.eng68.traineetracker;

import com.sparta.eng68.traineetracker.entities.CSVUser;
import com.sparta.eng68.traineetracker.utilities.Role;
import org.junit.jupiter.api.Test;

public class CSVUserTests {

    @Test
    void testCSVUSerToString() {
        CSVUser user = new CSVUser("michael", "abc123", Role.TRAINEE);

        System.out.println(user.toString());
    }

}
