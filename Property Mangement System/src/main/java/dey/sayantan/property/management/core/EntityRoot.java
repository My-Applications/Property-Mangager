package dey.sayantan.property.management.core;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public class EntityRoot {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, unique = true, updatable = false)
	@JsonIgnore
	protected Long rowId;

	@Column(name = "uuid", length = 36, nullable = false, updatable = false)
	protected UUID uuid;

	@JsonIgnore
	@Column(name = "timestamp", nullable = false, updatable = false)
	protected Instant timestamp;

	@Column(nullable = false)
	@JsonIgnore
	protected Boolean isDeleted = false;

	public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
