package com.sparta.eng72.traineetracker;

import com.sparta.eng72.traineetracker.entities.CSVUser;
import com.sparta.eng72.traineetracker.utilities.Role;
import org.junit.jupiter.api.Test;

public class CSVUserTests {

    @Test
    void testCSVUSerToString() {
        CSVUser user = new CSVUser("michael", "abc123", Role.TRAINEE);

        System.out.println(user.toString());
    }

}
