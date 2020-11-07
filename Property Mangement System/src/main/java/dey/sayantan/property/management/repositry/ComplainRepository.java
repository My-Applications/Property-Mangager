package dey.sayantan.property.management.repositry;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import dey.sayantan.property.management.model.Complain;

public interface ComplainRepository<P> extends CrudRepository<Complain, Long> {

	@Query(value = "select * from #{#entityName} e inner join"
			+ "( select uuid, max(timestamp) as maxTimestamp from  #{#entityName} where is_deleted = false group by uuid) e0"
			+ " on e.uuid = e0.uuid and e.timestamp = e0.maxTimestamp and e.is_deleted = false order by e.complain_date asc", nativeQuery = true)
	List<Complain> findAll();

	@Query("select e from #{#entityName} e where e.uuid = :uuid "
			+ " and e.timestamp = (select max(e.timestamp) from e where e.uuid = :uuid"
			+ " and e.isDeleted = false) and e.isDeleted = false")
	Complain findByUuid(@Param("uuid") UUID uuid);

	@Query(value = "select uuid, max(timestamp) as max_timestamp from #{#entityName} e "
			+ "where is_deleted =false group by uuid", nativeQuery = true)
	List<Complain> sample();

	@Query(value = "select * from #{#entityName} e inner join"
			+ "( select uuid, max(timestamp) as maxTimestamp from  #{#entityName} where is_deleted = false and tenant_uuid = :tenantUuid group by uuid) e0"
			+ " on e.uuid = e0.uuid and e.timestamp = e0.maxTimestamp and e.is_deleted = false", nativeQuery = true)
	List<Complain> findAllByTenantUuid(UUID tenantUuid);

}
