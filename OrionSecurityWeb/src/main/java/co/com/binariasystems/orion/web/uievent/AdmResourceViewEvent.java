package co.com.binariasystems.orion.web.uievent;

import co.com.binariasystems.fmw.vweb.mvp.event.SimpleUIEvent;

public class AdmResourceViewEvent extends SimpleUIEvent<String> {
	public AdmResourceViewEvent(String id) {
		super(id);
	}
}
