package co.com.binariasystems.orion.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import co.com.binariasystems.orion.web.Orion;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;


@WebServlet(
		urlPatterns = {"/main/*","/VAADIN/*"}, 
		name = "Orion-VServlet", asyncSupported = true, loadOnStartup = 1,
		initParams={@WebInitParam(
						name="org.atmosphere.cpr.AtmosphereInterceptor", 
						value="co.com.binariasystems.fmw.vweb.util.atmosphere.SpringContextPushInterceptor,org.atmosphere.interceptor.ShiroInterceptor")}
)
@VaadinServletConfiguration(ui = Orion.class, productionMode = false)
public class OrionVaadinServlet extends VaadinServlet{
	private OrionSessionListener sessionListener = new OrionSessionListener();
	private OrionSystemMessagesProvider systemMessagesProvider;
	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();
		
		systemMessagesProvider = new OrionSystemMessagesProvider(getServletContext().getContextPath());
		getService().addSessionInitListener(sessionListener);
		getService().addSessionDestroyListener(sessionListener);
		getService().setSystemMessagesProvider(systemMessagesProvider);
	}
}
