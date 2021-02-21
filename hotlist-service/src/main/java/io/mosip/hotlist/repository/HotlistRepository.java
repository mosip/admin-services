package io.mosip.hotlist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.mosip.hotlist.entity.Hotlist;

@Repository
public interface HotlistRepository extends JpaRepository<Hotlist, String>{

	Optional<Hotlist> findByIdHashAndIdType(String idHash, String idType);
	
	Boolean existsByIdHashAndIdType(String idHash, String idType);
}
