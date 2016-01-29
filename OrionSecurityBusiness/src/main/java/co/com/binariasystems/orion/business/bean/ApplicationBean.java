package co.com.binariasystems.orion.business.bean;

import java.util.List;

import co.com.binariasystems.orion.model.dto.ApplicationDTO;

public interface ApplicationBean {
	public List<ApplicationDTO> findAll();
	public ApplicationDTO findById(Integer id);
}
