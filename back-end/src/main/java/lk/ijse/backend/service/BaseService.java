package lk.ijse.backend.service;

import java.util.List;

public interface BaseService <T>{
    T save(T dto); // Save a new record
    T update(String id, T dto); // Update an existing record
    void delete(String id); // Delete a record by ID
    T findById(String id); // Find a record by ID
    List<T> findAll(); // Find all records
}
