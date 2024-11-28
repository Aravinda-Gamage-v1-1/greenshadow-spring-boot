package lk.ijse.backend.util;

import java.util.regex.Pattern;

public class RegexUtilForId {
    // Base regex for validating IDs with a prefix and UUID (e.g., USER-0be201fe-e1e1-42bd-86cb-5afcb602f841)
    private static final String PREFIX_UUID_REGEX = "^[A-Z]+-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";

    // General validation method for any ID with prefix-UUID format
    public static boolean isValidPrefixedUuid(String id) {
        return Pattern.matches(PREFIX_UUID_REGEX, id);
    }

    // Specific validation methods for each ID type
    public static boolean isValidUserId(String userId) {
        return userId.startsWith("USER-") && isValidPrefixedUuid(userId);
    }

    public static boolean isValidStaffId(String staffId) {
        return staffId.startsWith("STAFF-") && isValidPrefixedUuid(staffId);
    }

    public static boolean isValidFieldId(String fieldId) {
        return fieldId.startsWith("FIELD-") && isValidPrefixedUuid(fieldId);
    }

    public static boolean isValidCropId(String cropId) {
        return cropId.startsWith("CROP-") && isValidPrefixedUuid(cropId);
    }

    public static boolean isValidLogId(String logId) {
        return logId.startsWith("LOG-") && isValidPrefixedUuid(logId);
    }

    public static boolean isValidVehicleId(String vehicleId) {
        return vehicleId.startsWith("VEHICLE-") && isValidPrefixedUuid(vehicleId);
    }

    public static boolean isValidEquipmentId(String equipmentId) {
        return equipmentId.startsWith("EQUIP-") && isValidPrefixedUuid(equipmentId);
    }
}
