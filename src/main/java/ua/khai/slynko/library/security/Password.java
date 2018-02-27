package ua.khai.slynko.library.security;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Password hash class
 * 
 * @author O.Slynko
 *
 */
public final class Password {

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };
	private static final Logger LOG = Logger.getLogger(Password.class);

	public static String hash(String str) {
		MessageDigest digest;
		StringBuffer hexString = new StringBuffer();
		try
		{
			digest = MessageDigest.getInstance("SHA-512");
			digest.update(str.getBytes("UTF-8"));
			for (byte d : digest.digest()) {
				hexString.append(getFirstHexDigit(d)).append(getSecondHexDigit(d));
			}
		}
		catch (NoSuchAlgorithmException | UnsupportedEncodingException e)
		{
			LOG.error(e);
		}
		return hexString.toString();
	}

	private static char getFirstHexDigit(byte x) {
		return HEX_DIGITS[(0xFF & x) / 16];
	}

	private static char getSecondHexDigit(byte x) {
		return HEX_DIGITS[(0xFF & x) % 16];
	}
}