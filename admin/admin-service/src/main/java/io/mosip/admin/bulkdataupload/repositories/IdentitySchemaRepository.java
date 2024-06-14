package io.mosip.admin.bulkdataupload.repositories;


import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.IdentitySchema;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

@Repository
public interface IdentitySchemaRepository extends BaseRepository<IdentitySchema, String>{


}
