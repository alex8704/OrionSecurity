package co.com.binariasystems.orion.web.utils;

import co.com.binariasystems.orion.model.dto.RoleDTO;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

public class RoleSelectableColumnGenerator implements ColumnGenerator {
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		if (columnId == null)
			return null;
		final RoleDTO role = (RoleDTO) itemId;
		final CheckBox checkBox = new CheckBox(role.getName(), role.isSelected());
		checkBox.setDescription(role.getDescription());
		checkBox.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				role.setSelected(checkBox.getValue().booleanValue());
			}
		});
		return checkBox;
	}
}
