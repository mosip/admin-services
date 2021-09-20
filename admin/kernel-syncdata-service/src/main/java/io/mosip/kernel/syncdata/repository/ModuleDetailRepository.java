package io.mosip.kernel.syncdata.repository;

import io.mosip.kernel.syncdata.entity.ModuleDetail;
import io.mosip.kernel.syncdata.entity.id.IdAndLanguageCodeID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleDetailRepository extends JpaRepository<ModuleDetail, IdAndLanguageCodeID> {

    ModuleDetail findByNameAndLangCode(String name, String langCode);
}

