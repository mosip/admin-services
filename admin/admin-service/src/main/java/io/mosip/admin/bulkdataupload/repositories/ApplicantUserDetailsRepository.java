package io.mosip.admin.bulkdataupload.repositories;

import io.mosip.admin.bulkdataupload.entity.ApplicantUserDetailsEntity;
import io.mosip.admin.bulkdataupload.entity.BiometricType;
import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * @author Dhanendra
 *
 */
@Repository
public interface ApplicantUserDetailsRepository extends BaseRepository<ApplicantUserDetailsEntity, String> {


    long countByUserIdAndLoginDate(String userId, LocalDate loginDate);

}
