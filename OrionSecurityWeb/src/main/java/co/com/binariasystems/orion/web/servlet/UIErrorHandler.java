package co.com.binariasystems.orion.web.servlet;

import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;
import java.util.logging.Logger;

import co.com.binariasystems.fmw.util.exception.FMWExceptionUtils;
import co.com.binariasystems.fmw.vweb.util.VWebUtils;

import com.vaadin.data.Buffered;
import com.vaadin.data.Validator;
import com.vaadin.event.ListenerMethod.MethodException;
import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.server.ClientConnector.ConnectorErrorEvent;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorEvent;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.ErrorMessage.ErrorLevel;
import com.vaadin.server.ServerRpcManager.RpcInvocationException;
import com.vaadin.server.SystemError;
import com.vaadin.server.UserError;
import com.vaadin.shared.Connector;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

public class UIErrorHandler implements ErrorHandler {
	@Override
    public void error(ErrorEvent event) {
        doDefault(event);
    }

    public static void doDefault(ErrorEvent event) {
        Throwable t = event.getThrowable();
        if (t instanceof SocketException) {
            // Most likely client browser closed socket
            getLogger().info(
                    "SocketException in CommunicationManager."
                            + " Most likely client (browser) closed socket.");
            return;
        }

        t = findRelevantThrowable(t);

        // Finds the original source of the error/exception
        AbstractComponent component = findAbstractComponent(event);
        if (component != null) {
            // Shows the error in AbstractComponent
            ErrorMessage errorMessage = UIErrorHandler.getErrorMessageForException(t);
            component.setComponentError(errorMessage);
        }

        // also print the error on console
       //getLogger().log(Level.SEVERE, "", t);
    }

    /**
     * Vaadin wraps exceptions in its own and due to reflection usage there
     * might be also other irrelevant exceptions that make no sense for Vaadin
     * users (~developers using Vaadin). This method tries to choose the
     * relevant one to be reported.
     * 
     * @since 7.2
     * @param t
     *            a throwable passed to ErrorHandler
     * @return the throwable that is relevant for Vaadin users
     */
    public static Throwable findRelevantThrowable(Throwable t) {
        try {
            if ((t instanceof RpcInvocationException)
                    && (t.getCause() instanceof InvocationTargetException)) {
                /*
                 * RpcInvocationException (that always wraps irrelevant
                 * java.lang.reflect.InvocationTargetException) might only be
                 * relevant for core Vaadin developers.
                 */
                return findRelevantThrowable(t.getCause().getCause());
            } else if (t instanceof MethodException) {
                /*
                 * Method exception might only be relevant for core Vaadin
                 * developers.
                 */
                return t.getCause();
            }
        } catch (Exception e) {
            // NOP, just return the original one
        }
        return t;
    }

    private static Logger getLogger() {
        return Logger.getLogger(DefaultErrorHandler.class.getName());
    }

    /**
     * Returns the AbstractComponent associated with the given error if such can
     * be found
     * 
     * @param event
     *            The error to investigate
     * @return The {@link AbstractComponent} to error relates to or null if
     *         could not be determined or if the error does not relate to any
     *         AbstractComponent.
     */
    public static AbstractComponent findAbstractComponent(
            com.vaadin.server.ErrorEvent event) {
        if (event instanceof ConnectorErrorEvent) {
            Component c = findComponent(((ConnectorErrorEvent) event)
                    .getConnector());
            if (c instanceof AbstractComponent) {
                return (AbstractComponent) c;
            }
        }

        return null;
    }

    /**
     * Finds the nearest component by traversing upwards in the hierarchy. If
     * connector is a Component, that Component is returned. Otherwise, looks
     * upwards in the hierarchy until it finds a {@link Component}.
     * 
     * @return A Component or null if no component was found
     */
    public static Component findComponent(Connector connector) {
        if (connector instanceof Component) {
            return (Component) connector;
        }
        if (connector.getParent() != null) {
            return findComponent(connector.getParent());
        }

        return null;
    }
    
    public static ErrorMessage getErrorMessageForException(Throwable t) {
        if (null == t) {
            return null;
        } else if (t instanceof ErrorMessage) {
            // legacy case for custom error messages
            return (ErrorMessage) t;
        } else if (t instanceof Validator.InvalidValueException) {
            UserError error = new UserError(
                    ((Validator.InvalidValueException) t).getHtmlMessage(),
                    ContentMode.HTML, ErrorLevel.ERROR);
            return error;
        } else if (t instanceof Buffered.SourceException) {
            // no message, only the causes to be painted
            UserError error = new UserError( ((Buffered.SourceException) t).getMessage());
            return error;
        } else {
            return new SystemError(FMWExceptionUtils.prettyMessageException(t, VWebUtils.getCurrentUserLocale()).getMessage());
        }
    }
}
