package co.com.binariasystems.orion.web.utils;

import co.com.binariasystems.fmw.constants.FMWConstants;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;

import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

public class ApplicationColumnGenerator implements ColumnGenerator {
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		if(columnId == null) return null;
		ApplicationDTO application = (ApplicationDTO)itemId;
		return new StringBuilder().append(application.getApplicationId())
		.append(FMWConstants.PIPE).append(application.getName()).toString();
	}

}
