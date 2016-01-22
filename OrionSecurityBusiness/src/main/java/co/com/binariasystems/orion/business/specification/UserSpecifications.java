package co.com.binariasystems.orion.business.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import co.com.binariasystems.orion.business.entity.SegtUser;
import co.com.binariasystems.orion.business.entity.SegtUserCredentials_;
import co.com.binariasystems.orion.business.entity.SegtUser_;
import co.com.binariasystems.orion.model.dto.AuthenticationDTO;

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
}
