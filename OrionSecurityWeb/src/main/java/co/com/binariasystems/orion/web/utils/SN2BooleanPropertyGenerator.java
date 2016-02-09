package co.com.binariasystems.orion.web.utils;

import co.com.binariasystems.commonsmodel.enumerated.SN2Boolean;

import com.vaadin.data.Item;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;

public class SN2BooleanPropertyGenerator extends PropertyValueGenerator<Resource>{

	@Override
	public Resource getValue(Item item, Object itemId, Object propertyId) {
		if(propertyId == null) return null;
		SN2Boolean value = (SN2Boolean) item.getItemProperty(propertyId).getValue();
		return new ThemeResource((value != null && value.booleanValue()) ? "img/yes.png" : "img/no.png");
	}

	@Override
	public Class<Resource> getType() {
		return Resource.class;
	}

}
