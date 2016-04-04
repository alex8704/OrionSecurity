package co.com.binariasystems.orion.business.utils;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import co.com.binariasystems.commonsmodel.constants.SystemConstants;
import co.com.binariasystems.fmw.constants.FMWConstants;
import co.com.binariasystems.fmw.ioc.IOCHelper;
import co.com.binariasystems.fmw.security.crypto.CredentialsCrypto;
import co.com.binariasystems.fmw.security.crypto.CredentialsCryptoId;
import co.com.binariasystems.fmw.security.crypto.CredentialsCryptoProvider;
import co.com.binariasystems.fmw.security.crypto.EncryptionRequest;
import co.com.binariasystems.fmw.security.crypto.MatchingRequest;
import co.com.binariasystems.fmw.security.crypto.impl.EncryptionResult;
import co.com.binariasystems.fmw.util.ObjectUtils;
import co.com.binariasystems.fmw.util.messagebundle.PropertiesManager;
import co.com.binariasystems.fmw.util.pagination.ListPage;

public class OrionBusinessUtils implements OrionBusinessConstants, SystemConstants {
	private static PropertiesManager configProperties;
	private static CredentialsCrypto credentialsCrypto;
	public static String getApplicationName(){
		return getConfigProperties().getString(APP_NAME_PROP);
	}
	
	public static String getApplicationVersion(){
		return getConfigProperties().getString(APP_VERSION_PROP);
	}
	
	public static String getMainDataSourceName(){
		return getConfigProperties().getString(MAIN_DSOURCE_PROP);
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
	
	public static Sort builSpringSort(co.com.binariasystems.fmw.business.domain.Sort sort){
		return builSpringSort(sort, null);
	}
	
	public static Sort builSpringSort(co.com.binariasystems.fmw.business.domain.Sort sort, co.com.binariasystems.fmw.business.domain.Order defaultOrder){
		if(sort == null || sort.getOrders().isEmpty()){
			return defaultOrder != null ? new Sort(new Sort.Order(Direction.fromStringOrNull(defaultOrder.getDirection().name()), defaultOrder.getProperty())):
				null;
		}
		Sort.Order[] orders = new Sort.Order[sort.getOrders().size()];
		for(int i = 0; i < sort.getOrders().size(); i++){
			orders[i] = new Sort.Order(Direction.fromStringOrNull(sort.getOrders().get(i).getDirection().name()), sort.getOrders().get(i).getProperty());
		}
		return new Sort(orders);
	}
	
	public static PageRequest buildPageRequest(co.com.binariasystems.fmw.business.domain.PageRequest pageRequest){
		return buildPageRequest(pageRequest, null);
	}
	
	public static PageRequest buildPageRequest(co.com.binariasystems.fmw.business.domain.PageRequest pageRequest,  co.com.binariasystems.fmw.business.domain.Order defaultOrder){
		return new PageRequest(pageRequest.getPage() - 1, pageRequest.getSize(), builSpringSort(pageRequest.getSort(), defaultOrder));
	}
	
	public static <F, T> co.com.binariasystems.fmw.business.domain.Page<T> toPage(Page<F> page){
		return toPage(page, null);
	}
	
	public static <F, T> co.com.binariasystems.fmw.business.domain.Page<T> toPage(Page<F> page, Class<T> targetContentClazz){
		if(targetContentClazz != null)
			return new co.com.binariasystems.fmw.business.domain.Page<T>(page.getTotalPages(), page.getTotalElements(), ObjectUtils.transferProperties(page.getContent(), targetContentClazz));
		return new co.com.binariasystems.fmw.business.domain.Page<T>(page.getTotalPages(), page.getTotalElements(), (List<T>) page.getContent());
	}
	
	public static <T> ListPage<T> pageToListPage(co.com.binariasystems.fmw.business.domain.Page<T> page){
		return new ListPage<T>(page.getContent(), page.getTotalElements());
	}
}
