package co.com.binariasystems.orion.business.specification;


public final class ResourceSpecifications {
	private ResourceSpecifications(){}
	
//	public static Specification<SegtResource> rolesAndApplicationIn(final List<SegtRole> roles, final SegtApplication application){
//		return new Specification<SegtResource>() {
//			@Override
//			public Predicate toPredicate(Root<SegtResource> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//				Predicate predicate = cb.and(
//						cb.isMember(roles, root.get(SegtResource_.authorizedRoles)),
//						cb.equal(root.get(SegtResource_.application), application));
//				
//				query.orderBy(cb.asc(root.get(SegtResource_.module)), cb.asc(root.get(SegtResource_.resourceId)));
//				
//				return predicate;
//			}
//		};
//	}
}
