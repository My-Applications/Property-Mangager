package dey.sayantan.property.management.repositry;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import dey.sayantan.property.management.model.Property;

public interface PropertyRepository<P> extends CrudRepository<Property, Long> {

	@Query(value = "select * from #{#entityName} e inner join"
			+ "( select uuid, max(timestamp) as maxTimestamp from  #{#entityName} where is_deleted = false group by uuid) e0"
			+ " on e.uuid = e0.uuid and e.timestamp = e0.maxTimestamp and e.is_deleted = false order by e.timestamp desc", nativeQuery = true)
	List<Property> findAll();

	@Query("select e from #{#entityName} e where e.uuid = :uuid "
			+ " and e.timestamp = (select max(e.timestamp) from e where e.uuid = :uuid"
			+ " and e.isDeleted = false) and e.isDeleted = false")
	Property findByUuid(@Param("uuid") UUID uuid);

	@Query(value = "select uuid, max(timestamp) as max_timestamp from #{#entityName} e "
			+ "where is_deleted =false group by uuid", nativeQuery = true)
	List<Property> sample();
}
