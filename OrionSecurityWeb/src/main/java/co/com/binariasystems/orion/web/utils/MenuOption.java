package co.com.binariasystems.orion.web.utils;

import java.util.Collection;
import java.util.List;

import co.com.binariasystems.fmw.vweb.uicomponet.treemenu.MenuElement;

public class MenuOption extends MenuElement {
	public MenuOption() {}
	
	public MenuOption(String caption) {
		super(caption);
	}
	
	public MenuOption(String caption, String description) {
		super(caption, description);
	}

	public MenuOption(String caption, String description, String path) {
		super(caption, description);
		this.path = path;
	}

	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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
