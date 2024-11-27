package lk.ijse.backend.util;

import java.util.Base64;
import java.util.UUID;

public class AppUtil {
    public static String generateUserId() {
        return "USER-" + UUID.randomUUID();
    }
    public static String generateStaffId() {
        return "STAFF-" + UUID.randomUUID();
    }
    public static String generateFieldId() {
        return "FIELD-" + UUID.randomUUID();
    }
    public static String generateCropId() {
        return "CROP-" + UUID.randomUUID();
    }
    public static String generateLogId() {
        return "LOG-" + UUID.randomUUID();
    }
    public static String generateVehicleId() {
        return "VEHICLE-" + UUID.randomUUID();
    }
    public static String generateEquipmentId() {
        return "EQUIP-" + UUID.randomUUID();
    }

    public static String imageToBase64(byte [] image){
        return Base64.getEncoder().encodeToString(image);
    }
}
