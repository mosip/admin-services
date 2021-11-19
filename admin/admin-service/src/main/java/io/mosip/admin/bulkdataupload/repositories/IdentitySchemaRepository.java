package io.mosip.admin.bulkdataupload.repositories;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.mosip.admin.bulkdataupload.entity.IdentitySchema;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;

@Repository
public interface IdentitySchemaRepository extends BaseRepository<IdentitySchema, String>{


}
