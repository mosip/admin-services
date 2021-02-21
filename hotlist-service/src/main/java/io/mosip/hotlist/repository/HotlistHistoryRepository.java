package io.mosip.hotlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.mosip.hotlist.entity.HotlistHistory;

@Repository
public interface HotlistHistoryRepository extends JpaRepository<HotlistHistory, String>{

}
