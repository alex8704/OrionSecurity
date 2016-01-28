package co.com.binariasystems.orion.web.utils;

import co.com.binariasystems.fmw.constants.FMWConstants;
import co.com.binariasystems.orion.model.dto.ResourceDTO;

import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

public class ResourceColumnGenerator implements ColumnGenerator {
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		if(columnId == null) return null;
		ResourceDTO resource = (ResourceDTO)itemId;
		return new StringBuilder().append(resource.getResourceId())
		.append(FMWConstants.PIPE).append(resource.getName()).toString();
	}

}
