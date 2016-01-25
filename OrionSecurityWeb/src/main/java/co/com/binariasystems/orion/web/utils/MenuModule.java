package co.com.binariasystems.orion.web.utils;

import java.util.Collection;

import co.com.binariasystems.fmw.vweb.uicomponet.treemenu.MenuElement;

public class MenuModule extends MenuElement{
	public MenuModule() {}
	public MenuModule(String caption) {
		super(caption);
	}

	public MenuModule(String caption, String description) {
		super(caption, description);
	}
	
	public MenuModule(String caption, String description, MenuElement... childs) {
		super(caption, description);
		addChilds(childs);
	}
	
	public MenuModule(String caption, String description, Collection<MenuElement> childs) {
		super(caption, description);
		addChilds(childs);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MenuModule))
			return false;
		MenuModule other = (MenuModule) obj;
		if (caption == null) {
			if (other.caption != null)
				return false;
		} else if (!caption.equals(other.caption))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caption == null) ? 0 : caption.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		return result;
	}
}
