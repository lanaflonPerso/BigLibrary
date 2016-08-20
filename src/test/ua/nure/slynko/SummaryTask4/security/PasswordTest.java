package ua.nure.slynko.SummaryTask4.security;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import ua.khai.slynko.Library.security.Password;

public class PasswordTest {

	@Test
	public void testHash() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		assertEquals(Password.hash("abc"),
				"DDAF35A193617ABACC417349AE20413112E6FA4E89A97EA20A9EEEE64B55D39A2192992A274FC1A836BA3C23A3FEEBBD454D4423643CE80E2A9AC94FA54CA49F");
	}

	@Test
	public void testDefaultConstructor() {
		new Password();
	}
}
