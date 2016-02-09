package co.com.binariasystems.orion.web.utils;

import java.util.Locale;

import co.com.binariasystems.commonsmodel.enumerated.SN2Boolean;

import com.vaadin.data.util.converter.Converter;

public class SN2BooleanConverter implements Converter<Boolean, SN2Boolean> {
	@Override
	public SN2Boolean convertToModel(Boolean value, Class<? extends SN2Boolean> targetType, Locale locale) throws ConversionException {
		return SN2Boolean.fromBoolean(value != null && value);
	}

	@Override
	public Boolean convertToPresentation(SN2Boolean value, Class<? extends Boolean> targetType, Locale locale) throws ConversionException {
		return Boolean.valueOf(value != null && value.booleanValue());
	}

	@Override
	public Class<SN2Boolean> getModelType() {
		return SN2Boolean.class;
	}

	@Override
	public Class<Boolean> getPresentationType() {
		return Boolean.class;
	}

}
