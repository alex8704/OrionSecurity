package co.com.binariasystems.orion.web.servlet;

import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;

public class OrionSessionListener implements SessionInitListener, SessionDestroyListener {

	@Override
	public void sessionDestroy(SessionDestroyEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionInit(SessionInitEvent event) throws ServiceException {
		// TODO Auto-generated method stub

	}

}
