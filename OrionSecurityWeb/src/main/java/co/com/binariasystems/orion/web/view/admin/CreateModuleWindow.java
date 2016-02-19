package co.com.binariasystems.orion.web.view.admin;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import co.com.binariasystems.fmw.exception.FMWException;
import co.com.binariasystems.fmw.ioc.IOCHelper;
import co.com.binariasystems.fmw.vweb.mvp.annotation.NoConventionString;
import co.com.binariasystems.fmw.vweb.mvp.annotation.validation.NullValidator;
import co.com.binariasystems.fmw.vweb.mvp.dispatcher.MVPUtils;
import co.com.binariasystems.fmw.vweb.mvp.event.UIEvent;
import co.com.binariasystems.fmw.vweb.mvp.eventbus.EventBus;
import co.com.binariasystems.fmw.vweb.uicomponet.Dimension;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.FormValidationException;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog;
import co.com.binariasystems.fmw.vweb.uicomponet.MessageDialog.Type;
import co.com.binariasystems.fmw.vweb.uicomponet.SearcherField;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.TextFieldBuilder;
import co.com.binariasystems.fmw.vweb.util.LocaleMessagesUtil;
import co.com.binariasystems.fmw.vweb.util.VWebUtils;
import co.com.binariasystems.orion.business.bean.ModuleBean;
import co.com.binariasystems.orion.model.dto.ApplicationDTO;
import co.com.binariasystems.orion.model.dto.ModuleDTO;
import co.com.binariasystems.orion.web.uievent.CreateModuleWindowEvent;
import co.com.binariasystems.orion.web.utils.OrionWebConstants;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class CreateModuleWindow extends Window {
	private FormPanel form;
	@NullValidator
	@PropertyId("name")
    private TextFieldBuilder nameTxt;
	@PropertyId("description")
    private TextFieldBuilder descriptionTxt;
	@PropertyId("parentModule")
    private SearcherField<ModuleDTO> parentModuleTxt;
    @NullValidator
    @PropertyId("applicationId")
    private SearcherField<ApplicationDTO> applicationTxt;
    @PropertyId("index")
    private TextFieldBuilder indexTxt;
    @NoConventionString(permitDescription=true)
    private ButtonBuilder saveBtn;
    @NoConventionString(permitDescription=true)
    private ButtonBuilder editBtn;
    @NoConventionString(permitDescription=true)
    private ButtonBuilder cancelBtn;
    
    private ClickListener clickListener;
    private BeanFieldGroup<ModuleDTO> fieldGroup;
    private Map<Button, MessageDialog> confirmMsgDialogMapping;
    private ModuleDTO moduleDTO = new ModuleDTO();
    private ModuleBean moduleBean;
    
    @Override
    public void attach() {
    	super.attach();
    	if(form == null)
    		init();
    	center();
    }
    
    @Override
    public void detach() {
    	super.detach();
    	onUnload();
    }
    
	private void init(){
		form = new FormPanel(2);
		applicationTxt = new SearcherField<ApplicationDTO>(ApplicationDTO.class);
		parentModuleTxt = new SearcherField<ModuleDTO>(ModuleDTO.class);
		nameTxt = new TextFieldBuilder();
		descriptionTxt = new TextFieldBuilder();
		indexTxt = new TextFieldBuilder();
		saveBtn = new ButtonBuilder();
		editBtn = new ButtonBuilder();
		cancelBtn = new ButtonBuilder();
		fieldGroup = new BeanFieldGroup<ModuleDTO>(ModuleDTO.class);
		confirmMsgDialogMapping = new HashMap<Button, MessageDialog>();
		
		form.add(applicationTxt)
		.add(parentModuleTxt)
		.add(nameTxt, Dimension.percent(100))
		.add(descriptionTxt, Dimension.percent(100))
		.add(indexTxt, Dimension.percent(100))
		.addCenteredOnNewRow(saveBtn, editBtn, cancelBtn);
		
		setContent(form);
		
		fieldGroup.setBuffered(false);
		fieldGroup.setItemDataSource(moduleDTO);
		fieldGroup.bindMemberFields(this);
		
		saveBtn.withIcon(FontAwesome.SAVE)
		.withData("create")
		.disable();
		editBtn.withIcon(FontAwesome.EDIT)
		.withData("edit")
		.disable();
		cancelBtn.withIcon(FontAwesome.CLOSE)
		.withData("cancel");
		
		applicationTxt.setReadOnly(true);
		parentModuleTxt.setReadOnly(true);
		nameTxt.withoutUpperTransform();
		descriptionTxt.withoutUpperTransform();
		indexTxt.withoutUpperTransform();
		
		confirmMsgDialogMapping.put(saveBtn, new MessageDialog(getText("common.message.creation_confirmation.title"), 
				getText("common.message.creation_confirmation.question"), Type.QUESTION));
		
		confirmMsgDialogMapping.put(editBtn, new MessageDialog(getText("common.message.edition_confirmation.title"), 
				getText("common.message.edition_confirmation.question"), Type.QUESTION));
		
		confirmMsgDialogMapping.put(cancelBtn, new MessageDialog(getText("common.message.cancelation_confirmation.title"), 
				getText("common.message.cancelation_confirmation.question"), Type.QUESTION));
		
		setWidth(750, Unit.PIXELS);
		setResizable(false);
		setModal(true);
		MVPUtils.applyConventionStringsForView(this, OrionWebConstants.ADMIN_VIEW_MESSAGES);
		try {
			
			MVPUtils.applyValidatorsForView(this);
			MVPUtils.injectIOCProviderDependencies(this, getClass());
			
		} catch (ParseException | FMWException e) {
			MessageDialog.showExceptions(e);
		}
		bindEvents();
	}
	
	private void bindEvents(){
		clickListener = new CreateModuleWindowClickListener();
		for(Button button : confirmMsgDialogMapping.keySet())
			button.addClickListener(clickListener);
		for(MessageDialog messageDialog : confirmMsgDialogMapping.values())
			messageDialog.addYesClickListener(clickListener);
	}
	
	public void show(ModuleDTO module){
		UI.getCurrent().addWindow(this);
		resetCurrentModule(module);
		onLoad();
	}
	
	private void onLoad(){
		toggleActionButtonsState();
		form.initFocus();
	}
	
	private void onUnload(){
		resetCurrentModule(null);
	}
	
	private void resetCurrentModule(ModuleDTO module){
		VWebUtils.resetBeanItemDS(fieldGroup.getItemDataSource(), module);
	}
	
	private void toggleActionButtonsState(){
		saveBtn.setEnabled(moduleDTO.getModuleId() == null);
		editBtn.setEnabled(moduleDTO.getModuleId() != null);
	}
	
	private void saveBtnClick() throws FormValidationException{
		form.validate();
		notifyPopupEvent((String)saveBtn.getData(), moduleBean.save(moduleDTO));
	}
	
	private void editBtnClick() throws FormValidationException{
		form.validate();
		notifyPopupEvent((String)editBtn.getData(), moduleBean.save(moduleDTO));
	}
	
	private void cancelBtnClick(){
		notifyPopupEvent((String)cancelBtn.getData(), moduleDTO);
	}
	
	private void notifyPopupEvent(String eventId, ModuleDTO targetModule){
		fireEvent(new CreateModuleWindowEvent(eventId).set("module", targetModule));
		close();
	}
	
	private String getText(String key){
		return LocaleMessagesUtil.getLocalizedMessage(OrionWebConstants.ADMIN_VIEW_MESSAGES, key);
	}
	
	protected void fireEvent(UIEvent<?> event){
		EventBus eventBus = IOCHelper.getBean(EventBus.class);
		eventBus.fireEvent(event);
	}
	
	private class CreateModuleWindowClickListener implements ClickListener{
		@Override public void buttonClick(ClickEvent event) {
			try{
				if(confirmMsgDialogMapping.containsKey(event.getButton()))
					confirmMsgDialogMapping.get(event.getButton()).show();
				if(confirmMsgDialogMapping.get(saveBtn).yesButton().equals(event.getButton()))
					saveBtnClick();
				if(confirmMsgDialogMapping.get(editBtn).yesButton().equals(event.getButton()))
					editBtnClick();
				if(confirmMsgDialogMapping.get(cancelBtn).yesButton().equals(event.getButton()))
					cancelBtnClick();
			}catch(FormValidationException ex){
				toggleActionButtonsState();
				MessageDialog.showExceptions(ex);
			}
		}
		
	}
}
