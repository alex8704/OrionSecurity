package co.com.binariasystems.orion.web.utils;

import java.util.Collection;
import java.util.List;

import co.com.binariasystems.fmw.vweb.uicomponet.treemenu.MenuElement;

public class MenuAction extends MenuElement{
	private Object actionId;
	public MenuAction() {}
	
	public MenuAction(String caption) {
		super(caption);
	}
	
	public MenuAction(String caption, String description) {
		super(caption, description);
	}
	
	public MenuAction(String caption, String description, Object actionId) {
		super(caption, description);
		this.actionId = actionId;
	}
	
	public Object getActionId() {
		return actionId;
	}

	public void setActionId(Object actionId) {
		this.actionId = actionId;
	}
	
	@Override
	public MenuElement addChild(MenuElement child){
		throw new UnsupportedOperationException(getClass().getName()+" not allow childs");
	}
	@Override
	public MenuElement addChilds(MenuElement... child){
		throw new UnsupportedOperationException(getClass().getName()+" not allow childs");
	}
	@Override
	public MenuElement addChilds(Collection<MenuElement> child){
		throw new UnsupportedOperationException(getClass().getName()+" not allow childs");
	}
	
	@Override
	public List<MenuElement> getChilds() {
		return null;
	}
}
