package lk.ijse.backend.service;

import lk.ijse.backend.dto.impl.LogDTO;

import java.util.Map;

public interface LogService extends BaseService<LogDTO> {
    Map<String,Object> getRelatedEntitiesAsDTOs(String logId);
}
