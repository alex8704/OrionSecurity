package co.com.binariasystems.orion.business.predicate;

import org.apache.commons.collections.Predicate;

public abstract class AbstractPredicate<T> implements Predicate{

	@SuppressWarnings("unchecked")
	@Override public boolean evaluate(Object object) {
		return isValid((T) object);
	}
	
	public abstract boolean isValid(T item);

}
