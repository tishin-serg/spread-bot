package com.tishinserg.spreadbot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Profile("test")
@ActiveProfiles("test")
class SpreadBotApplicationTests {

	@Test
	void contextLoads() {
	}

}
