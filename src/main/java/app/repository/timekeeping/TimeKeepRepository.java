package app.repository.timekeeping;

import app.DTO.response.QuantityTimeKeepDTO;
import app.DTO.response.StoreTimeKeepDTO;

import java.util.List;

public interface TimeKeepRepository {
    public List<QuantityTimeKeepDTO> queryListSuccessTimeKeep();
    public List<QuantityTimeKeepDTO> queryListNotDetermined();
    public List<QuantityTimeKeepDTO> queryListOutRange();
    public List<QuantityTimeKeepDTO> queryListCases();
    public List<QuantityTimeKeepDTO> queryListMaxEmployees();
    public List<QuantityTimeKeepDTO> queryListMinEmployees();
}
