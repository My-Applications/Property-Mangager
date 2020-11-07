package dey.sayantan.property.management.repositry;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import dey.sayantan.property.management.model.PropertyOccupancy;

public interface PropertyOccupancyRepository<P> extends CrudRepository<PropertyOccupancy, Long> {

	@Query(value = "select * from property_occupancy e inner join"
			+ "( select uuid, max(timestamp) as maxTimestamp from  property_occupancy where is_deleted = false group by uuid) e0"
			+ " on e.uuid = e0.uuid and e.timestamp = e0.maxTimestamp and e.is_deleted = false order by e.timestamp desc", nativeQuery = true)
	List<PropertyOccupancy> findAll();

	@Query("select e from property_occupancy e where e.tenantUuid = :uuid "
			+ " and e.timestamp = (select max(e.timestamp) from e where e.tenantUuid = :uuid"
			+ " and e.isDeleted = false) and e.isDeleted = false")
	PropertyOccupancy findByTenantUuid(@Param("uuid") UUID uuid);

	@Query(value = "select uuid, max(timestamp) as max_timestamp from property_occupancy e "
			+ "where is_deleted =false group by uuid", nativeQuery = true)
	List<PropertyOccupancy> sample();
}
