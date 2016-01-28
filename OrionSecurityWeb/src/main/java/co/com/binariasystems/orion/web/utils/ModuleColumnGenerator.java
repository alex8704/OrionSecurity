package co.com.binariasystems.orion.web.utils;

import co.com.binariasystems.fmw.constants.FMWConstants;
import co.com.binariasystems.orion.model.dto.ModuleDTO;

import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

public class ModuleColumnGenerator implements ColumnGenerator {
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		if(columnId == null) return null;
		ModuleDTO module = (ModuleDTO)itemId;
		return new StringBuilder().append(module.getModuleId())
		.append(FMWConstants.PIPE).append(module.getName()).toString();
	}

}
