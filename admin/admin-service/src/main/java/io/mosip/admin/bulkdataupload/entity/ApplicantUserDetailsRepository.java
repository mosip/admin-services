package io.mosip.admin.bulkdataupload.entity;

import io.mosip.kernel.core.dataaccess.spi.repository.BaseRepository;
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
