package co.com.binariasystems.orion.web.controller.admin;

import co.com.binariasystems.fmw.annotation.Dependency;
import co.com.binariasystems.fmw.exception.FMWUncheckedException;
import co.com.binariasystems.fmw.util.pagination.ListPage;
import co.com.binariasystems.fmw.vweb.mvp.annotation.Init;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewController.OnUnLoad;
import co.com.binariasystems.fmw.vweb.mvp.annotation.ViewField;
import co.com.binariasystems.fmw.vweb.mvp.controller.AbstractViewController;
import co.com.binariasystems.fmw.vweb.uicomponet.FormPanel;
import co.com.binariasystems.fmw.vweb.uicomponet.Pager;
import co.com.binariasystems.fmw.vweb.uicomponet.builders.ButtonBuilder;
import co.com.binariasystems.fmw.vweb.uicomponet.pager.PageChangeEvent;
import co.com.binariasystems.fmw.vweb.uicomponet.pager.PageChangeHandler;
import co.com.binariasystems.orion.business.bean.UserBean;
import co.com.binariasystems.orion.model.dto.UserDTO;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.ui.Grid;

@ViewController
public class AdmUserViewController extends AbstractViewController implements PageChangeHandler<UserDTO, UserDTO> {
	@ViewField private FormPanel 					form;	
	@ViewField private	BeanItemContainer<UserDTO>	userGridItems;
	@ViewField private Grid							userGrid;
	@ViewField private Pager<UserDTO, UserDTO>		pager;
	@ViewField private ButtonBuilder				saveBtn,
													editBtn,
													searchBtn;
	@ViewField private BeanFieldGroup<UserDTO>		fieldGroup;
	
	@Dependency
	private UserBean userBean;
	private SelectionListener selectionListener;
	
	@Init
	public void init(){
		selectionListener = new AdmUserViewSelectionListener();
		pager.setPageChangeHandler(this);
		userGrid.addSelectionListener(selectionListener);
		form.initFocus();
	}
	
	@OnLoad
	public void onLoad(){
		pager.setFilterDto(fieldGroup.getItemDataSource().getBean());
	}
	
	@OnUnLoad
	public void onUnload(){
		cleanForm();
	}
	
	private void cleanForm(){
		for (Object propertyId : fieldGroup.getBoundPropertyIds())
			fieldGroup.getItemDataSource().getItemProperty(propertyId).setValue(null);
	}
	
	private void userGridSelect(){
		cleanForm();
		UserDTO selected = (UserDTO)userGrid.getSelectedRow();
		selected.getCredentials().setPassword(null);
		if(selected != null)
			setCurrentUser(selected);
	}
	
	private void setCurrentUser(UserDTO user){
		BeanItem<UserDTO> beanItem = new BeanItem<UserDTO>(user, UserDTO.class);
		beanItem.addNestedProperty("credentials.password");
		fieldGroup.setItemDataSource(beanItem);
	}

	@Override
	public ListPage<UserDTO> loadPage(PageChangeEvent<UserDTO> event) throws FMWUncheckedException {
		return userBean.findAll(event.getFilterDTO(), event.getPage(), event.getRowsByPage());
	}
	
	private class AdmUserViewSelectionListener implements SelectionListener{
		@Override public void select(SelectionEvent event) {
			if(userGrid.equals(event.getSource()))
				userGridSelect();
		}
	}
}
