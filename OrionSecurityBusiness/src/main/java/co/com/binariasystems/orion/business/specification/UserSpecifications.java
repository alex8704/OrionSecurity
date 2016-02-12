package co.com.binariasystems.orion.business.specification;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import co.com.binariasystems.fmw.entity.util.FMWEntityConstants;
import co.com.binariasystems.orion.business.entity.SegtUser;
import co.com.binariasystems.orion.business.entity.SegtUserCredentials_;
import co.com.binariasystems.orion.business.entity.SegtUser_;
import co.com.binariasystems.orion.model.dto.AuthenticationDTO;
import co.com.binariasystems.orion.model.dto.UserDTO;

public final class UserSpecifications {
	private UserSpecifications() {}
	
	public static Specification<SegtUser> aliasAndPasswordEqualsTo(final AuthenticationDTO authInfo){
		return new Specification<SegtUser>() {
			@Override
			public Predicate toPredicate(Root<SegtUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.and(
						cb.equal(root.get(SegtUser_.loginAlias), authInfo.getUsername()),
						cb.equal(root.get(SegtUser_.credentials).get(SegtUserCredentials_.password), authInfo.getPassword())
						);
			}
			
		};
	}
	
	public static Specification<SegtUser> filledFieldsEqualsTo(final UserDTO user){
		return new Specification<SegtUser>() {
			@Override
			public Predicate toPredicate(Root<SegtUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Set<Predicate> predicates = new HashSet<Predicate>();
				if(user.getEmailAddress() != null)
					predicates.add(cb.equal(root.get(SegtUser_.emailAddress), user.getEmailAddress()));
				if(user.getFullName() != null)
					predicates.add(cb.like(root.get(SegtUser_.fullName), user.getFullName().replace(FMWEntityConstants.LIKE_SQLCOMPARING_COMIDIN_CHAR, "%")));
				if(user.getIdentificationNumber() != null)
					predicates.add(cb.equal(root.get(SegtUser_.identificationNumber), user.getIdentificationNumber()));
				if(user.getIsActive() != null)
					predicates.add(cb.equal(root.get(SegtUser_.isActive), user.getIsActive()));
				if(user.getIsBlockedByMaxRetries() != null)
					predicates.add(cb.equal(root.get(SegtUser_.isBlockedByMaxRetries), user.getIsBlockedByMaxRetries()));
				if(user.getEmailAddress() != null)
					predicates.add(cb.equal(root.get(SegtUser_.emailAddress), user.getEmailAddress()));
				if(user.getLoginAlias() != null)
					predicates.add(cb.equal(root.get(SegtUser_.loginAlias), user.getLoginAlias()));
				
				return cb.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
}
