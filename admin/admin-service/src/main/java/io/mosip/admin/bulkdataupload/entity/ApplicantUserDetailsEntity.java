package io.mosip.admin.bulkdataupload.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "applicant_login_detail", schema = "master")
public class ApplicantUserDetailsEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -8541947587557590379L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "usr_id", nullable = false, length = 64)
    private String userId;

    @Column(name = "login_date")
    private LocalDate loginDate;

}
