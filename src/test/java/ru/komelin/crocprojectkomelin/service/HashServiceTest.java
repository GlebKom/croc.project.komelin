package ru.komelin.crocprojectkomelin.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HashServiceTest {

	private HashService hashService;
	private final BigDecimal hashDecimal = new BigDecimal(10);
	private final String hashString = "MTA";

	@BeforeEach
	public void setup() {
		this.hashService = new HashServiceImpl();
	}

	@Test
	public void getHash() {
		String result = hashService.generateHash(hashDecimal);
		assertEquals(result, hashString);
	}

}
