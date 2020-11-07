package dey.sayantan.property.management.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResponseBody<E extends EntityRoot> {

	private UUID entityUuid;

	private List<String> errorList = new ArrayList<>();

	private E entity;

	public ResponseBody(UUID entityUuid, List<String> errorList, E entity) {
		super();
		this.entityUuid = entityUuid;
		this.errorList = errorList;
		this.entity = entity;
	}

	public UUID getEntityUuid() {
		return entityUuid;
	}

	public void setEntityUuid(UUID entityUuid) {
		this.entityUuid = entityUuid;
	}

	public List<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	public E getEntity() {
		return entity;
	}

	public void setEntity(E entity) {
		this.entity = entity;
	}

}
