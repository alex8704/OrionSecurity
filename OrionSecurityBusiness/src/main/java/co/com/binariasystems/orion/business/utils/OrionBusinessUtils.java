package co.com.binariasystems.orion.business.utils;

import co.com.binariasystems.fmw.constants.FMWConstants;
import co.com.binariasystems.fmw.ioc.IOCHelper;
import co.com.binariasystems.fmw.security.crypto.CredentialsCrypto;
import co.com.binariasystems.fmw.security.crypto.CredentialsCryptoId;
import co.com.binariasystems.fmw.security.crypto.CredentialsCryptoProvider;
import co.com.binariasystems.fmw.security.crypto.EncryptionRequest;
import co.com.binariasystems.fmw.security.crypto.MatchingRequest;
import co.com.binariasystems.fmw.security.crypto.impl.EncryptionResult;
import co.com.binariasystems.fmw.util.messagebundle.PropertiesManager;

public class OrionBusinessUtils implements OrionBusinessConstants {
	private static PropertiesManager configProperties;
	private static CredentialsCrypto credentialsCrypto;
	public static String getApplicationName(){
		return System.getProperty(APPLICATION_NAME_PROPERTY);
	}
	
	public static String getApplicationVersion(){
		return System.getProperty(APPLICATION_VERSION_PROPERTY);
	}
	
	public static String getMainDataSourceName(){
		return System.getProperty(MAIN_DATASOURCE_PROPERTY);
	}
	
	public static PropertiesManager getConfigProperties(){
		if(configProperties == null){
			configProperties = PropertiesManager.forPath("/configuration.properties", false, IOCHelper.getBean(FMWConstants.DEFAULT_LOADER_CLASS, Class.class));
		}
		return configProperties;
	}
	
	public static CredentialsCrypto getCredentialsCrypto(){
		if(credentialsCrypto == null){
			credentialsCrypto = CredentialsCryptoProvider.get(CredentialsCryptoId.SHIRO);
		}
		return credentialsCrypto;
	}
	
	public static boolean isStoredCredentialsHexEncoded(){
		return Boolean.valueOf(getConfigProperties().getString("credentialsMatcher.storedCredentialsHexEncoded")).booleanValue();
	}
	
	public static String credentialsPrivateSalt(){
		return getConfigProperties().getString("credentialsMatcher.privateSalt");
	}
	
	public static String credentialsHashAlgorithm(){
		return getConfigProperties().getString("credentialsMatcher.hashAlgorithm");
	}
	
	public static int credentialsHashIterations(){
		return Integer.parseInt(getConfigProperties().getString("credentialsMatcher.hashIterations"));
	}
	
	public static boolean credentialsMatches(String plainCredentials, String hashedCredentials, String hasedCredentialsSalt){;
		MatchingRequest matchingRequest = new  MatchingRequest();
		matchingRequest.setAlgorithmName(credentialsHashAlgorithm());
		matchingRequest.setHashIterations(credentialsHashIterations());
		matchingRequest.setHexEncoded(isStoredCredentialsHexEncoded());
		matchingRequest.setPrivateSalt(credentialsPrivateSalt());
		matchingRequest.setProvidedPassword(plainCredentials);
		matchingRequest.setStoredPassword(hashedCredentials);
		matchingRequest.setStoredPasswordSalt(hasedCredentialsSalt);
		return getCredentialsCrypto().credentialsMatches(matchingRequest);
	}
	
	public static String encryptPasswordSimple(String password){
		EncryptionRequest request = new EncryptionRequest(password, 
				credentialsPrivateSalt(), credentialsHashAlgorithm(), credentialsHashIterations(), isStoredCredentialsHexEncoded());
		return getCredentialsCrypto().encryptPasswordSimple(request);
	}
	public static EncryptionResult encryptPassword(String password){
		EncryptionRequest request = new EncryptionRequest(password, 
				credentialsPrivateSalt(), credentialsHashAlgorithm(), credentialsHashIterations(), isStoredCredentialsHexEncoded());
		return getCredentialsCrypto().encryptPassword(request);
	}
}
