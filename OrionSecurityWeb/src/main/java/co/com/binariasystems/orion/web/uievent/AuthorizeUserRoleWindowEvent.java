package co.com.binariasystems.orion.web.uievent;

import co.com.binariasystems.fmw.vweb.mvp.event.SimpleUIEvent;

public class AuthorizeUserRoleWindowEvent extends SimpleUIEvent<String> {

	public AuthorizeUserRoleWindowEvent(String id) {
		super(id);
	}

}
