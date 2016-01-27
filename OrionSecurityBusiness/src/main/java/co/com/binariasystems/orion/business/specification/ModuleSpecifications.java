package co.com.binariasystems.orion.business.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import co.com.binariasystems.orion.business.entity.SegtApplication;
import co.com.binariasystems.orion.business.entity.SegtModule;
import co.com.binariasystems.orion.business.entity.SegtModule_;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;

public final class ModuleSpecifications {
	private ModuleSpecifications(){}
	
	public static Specification<SegtModule> applicationAndParentModuleEqualsTo(final ApplicationDTO application, final ModuleDTO module){
		return new Specification<SegtModule>() {
			@Override
			public Predicate toPredicate(Root<SegtModule> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate resp = cb.equal(root.get(SegtModule_.applicationId), new SegtApplication(application.getApplicationId()));
				if(module != null)
					resp = cb.and(resp,cb.equal(root.get(SegtModule_.parentModule), new SegtModule(module.getModuleId())));
				return resp;
			}
			
		};
	}
}
