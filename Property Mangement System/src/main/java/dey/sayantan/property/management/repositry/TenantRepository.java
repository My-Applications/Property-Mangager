package dey.sayantan.property.management.repositry;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import dey.sayantan.property.management.model.Tenant;

public interface TenantRepository<P> extends CrudRepository<Tenant, Long> {

	@Query(value = "select * from #{#entityName} e inner join"
			+ "( select uuid, max(timestamp) as maxTimestamp from  #{#entityName} where is_deleted = false group by uuid) e0"
			+ " on e.uuid = e0.uuid and e.timestamp = e0.maxTimestamp and e.is_deleted = false order by e.tenant_name asc", nativeQuery = true)
	List<Tenant> findAll();

	@Query("select e from #{#entityName} e where e.uuid = :uuid "
			+ " and e.timestamp = (select max(e.timestamp) from e where e.uuid = :uuid"
			+ " and e.isDeleted = false) and e.isDeleted = false")
	Tenant findByUuid(@Param("uuid") UUID uuid);

	@Query("select e from #{#entityName} e where e.registrationId = :rid"
			+ " and e.timestamp = (select max(e.timestamp) from e "
			+ "where e.registrationId = :rid and e.isDeleted = false) and e.isDeleted= false")
	Tenant findByRegistrationId(@Param("rid") String registrationID);

	@Query(value = "select uuid, max(timestamp) as max_timestamp from #{#entityName} e "
			+ "where is_deleted =false group by uuid", nativeQuery = true)
	List<Tenant> sample();
}
