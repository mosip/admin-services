package io.mosip.admin.constant;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(
        name = "SingleTypeType"
)
@XmlEnum
public enum BiometricType implements Serializable {
    @XmlEnumValue("Scent")
    SCENT("Scent"),
    @XmlEnumValue("Dna")
    DNA("DNA"),
    @XmlEnumValue("Dna")
    EAR("Ear "),
    @XmlEnumValue("Face")
    FACE("Face"),
    @XmlEnumValue("Finger")
    FINGER("Finger"),
    @XmlEnumValue("Foot")
    FOOT("Foot"),
    @XmlEnumValue("Vein")
    VEIN("Vein"),
    @XmlEnumValue("HandGeometry")
    HAND_GEOMETRY("HandGeometry"),
    @XmlEnumValue("Iris")
    IRIS("Iris"),
    @XmlEnumValue("Retina")
    RETINA("Retina"),
    @XmlEnumValue("Voice")
    VOICE("Voice"),
    @XmlEnumValue("Gait")
    GAIT("Gait"),
    @XmlEnumValue("Keystroke")
    KEYSTROKE("Keystroke"),
    @XmlEnumValue("LipMovement")
    LIP_MOVEMENT("LipMovement"),
    @XmlEnumValue("SignatureSign")
    SIGNATURE_SIGN("SignatureSign"),
    @XmlEnumValue("ExceptionPhoto")
    EXCEPTION_PHOTO("ExceptionPhoto");

    private final String value;

    private BiometricType(String v) {
        this.value = v;
    }

    public String value() {
        return this.value;
    }

    public static BiometricType fromValue(String v) {
        BiometricType[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            BiometricType c = var1[var3];
            if (c.value.equalsIgnoreCase(v)) {
                return c;
            }
        }

        throw new IllegalArgumentException(v);
    }
}
