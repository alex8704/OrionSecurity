package co.com.binariasystems.orion.business.helper;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

public class AccessTokenHelper {
	public static String generateRandomTokenStr() {
		String randomUUID = UUID.randomUUID().toString();
        
        for(int i = 0; i < 4; ++i){
            randomUUID = randomUUID.replaceFirst("-", RandomStringUtils.randomAlphabetic(2));
        }
        
        Long currentTime = System.currentTimeMillis();
        String currentTimeHex = String.format("%x", currentTime);
        randomUUID = randomUUID + currentTimeHex;
        return randomUUID.toUpperCase();
	}
	public static void main(String[] args) {
		System.out.println(generateRandomTokenStr());
	}
}
