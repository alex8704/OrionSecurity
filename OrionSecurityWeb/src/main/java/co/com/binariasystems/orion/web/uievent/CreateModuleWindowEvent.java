package co.com.binariasystems.orion.web.uievent;

import co.com.binariasystems.fmw.vweb.mvp.event.SimpleUIEvent;

public class CreateModuleWindowEvent extends SimpleUIEvent<String> {

	public CreateModuleWindowEvent(String id) {
		super(id);
	}

}
