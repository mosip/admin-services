package io.mosip.admin.bulkdataupload.repositories;

import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.UserDetails;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

@Repository
public interface UserDetailsRepository extends BaseRepository<UserDetails,String> {

}
