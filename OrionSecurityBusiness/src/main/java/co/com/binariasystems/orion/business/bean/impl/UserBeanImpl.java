package co.com.binariasystems.orion.business.bean.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.com.binariasystems.fmw.security.crypto.impl.EncryptionResult;
import co.com.binariasystems.fmw.util.ObjectUtils;
import co.com.binariasystems.fmw.util.pagination.ListPage;
import co.com.binariasystems.orion.business.bean.UserBean;
import co.com.binariasystems.orion.business.dao.UserDAO;
import co.com.binariasystems.orion.business.entity.SegtRole;
import co.com.binariasystems.orion.business.entity.SegtUser;
import co.com.binariasystems.orion.business.specification.UserSpecifications;
import co.com.binariasystems.orion.business.utils.OrionBusinessUtils;
import co.com.binariasystems.orion.model.dto.RoleDTO;
import co.com.binariasystems.orion.model.dto.UserCredentialsDTO;
import co.com.binariasystems.orion.model.dto.UserDTO;

@Service
@Transactional
public class UserBeanImpl implements UserBean{
	@Autowired
	private UserDAO dao;
	
	@Override
	public ListPage<UserDTO> findAll(int page, int pageSize) {
		Page<SegtUser> resultPage = dao.findAll(new PageRequest(page - 1, pageSize, new Sort(new Sort.Order(Direction.DESC, "userId"))));
		return new ListPage<UserDTO>(ObjectUtils.transferProperties(resultPage.getContent(), UserDTO.class), resultPage.getTotalElements());
	}

	@Override
	public ListPage<UserDTO> findAll(UserDTO user, int page, int pageSize) {
		Page<SegtUser> resultPage = dao.findAll(UserSpecifications.filledFieldsEqualsTo(user),
				new PageRequest(page - 1, pageSize, new Sort(new Sort.Order(Direction.DESC, "userId"))));
		return new ListPage<UserDTO>(ObjectUtils.transferProperties(resultPage.getContent(), UserDTO.class), resultPage.getTotalElements());
	}

	@Override
	public UserDTO save(UserDTO user) {
		SegtUser userToSave = user.getUserId() == null ? ObjectUtils.transferProperties(user, SegtUser.class) : dao.findOne(user.getUserId());
		if(userToSave.getUserId() != null){
			UserCredentialsDTO storedCredentials = ObjectUtils.transferProperties(userToSave.getCredentials(), UserCredentialsDTO.class);
			/**
			 * Validacion de cambio de credenciales cuando se trata de un usuario a actualizar, se valida si el valor ingresado
			 * desde pantalla no es nulo y si no hace match con el password actual, para entonces proceder a actualizarlo
			 */
			if(StringUtils.isNotEmpty(user.getCredentials().getPassword()) && !credentialsMatches(user.getCredentials().getPassword(), storedCredentials)){
				EncryptionResult ecnryptResult = OrionBusinessUtils.encryptPassword(user.getCredentials().getPassword());
				user.setCredentials(new UserCredentialsDTO(ecnryptResult.getEncryptedValue(), ecnryptResult.getHexSalt()));
			}else
				user.setCredentials(storedCredentials);
			ObjectUtils.transferProperties(user, userToSave);
		}else{
			/**
			 * Para Usuarios nuevos se usa el numero de identificacion como password por defecto,  si el usuario no asigna uno
			 */
			String plainPassword = StringUtils.isEmpty(user.getCredentials().getPassword()) ? user.getIdentificationNumber() : user.getCredentials().getPassword();
			EncryptionResult ecnryptResult = OrionBusinessUtils.encryptPassword(plainPassword);
			user.setCredentials(new UserCredentialsDTO(ecnryptResult.getEncryptedValue(), ecnryptResult.getHexSalt()));
		}
		return ObjectUtils.transferProperties(dao.save(userToSave), UserDTO.class);
	}

	@Override
	public UserDTO save(UserDTO user, List<RoleDTO> assignedRoles) {
		SegtUser userToSave = ObjectUtils.transferProperties(user, SegtUser.class);
		userToSave.setAssignedRoles(ObjectUtils.transferProperties(assignedRoles, SegtRole.class));
		return ObjectUtils.transferProperties(dao.save(userToSave), UserDTO.class);
	}
	
	private boolean credentialsMatches(String providedCredentials, UserCredentialsDTO storedCredentials){
		return OrionBusinessUtils.credentialsMatches(providedCredentials, storedCredentials.getPassword(), storedCredentials.getPasswordSalt());
	}
}
