package app.repository.store;

import app.DTO.response.*;

import java.util.List;

public interface StoreRepository {
    public List<StoreTimeKeepDTO> queryListStoreTimeKeep();
    public List<StoreIdDTO> queryListStoreTaxItem();
    public List<StoreIdDTO> queryListStoreSpecialTime();
    public List<StoreIdDTO> queryListStoreDefaultItem();
    public List<StoreIdDTO> queryListStoreDebt();
    public List<StoreAccumulateDTO> queryStoreAccumulateDTOS(Integer params);
    public List<Integer> queryListStoreIdMeInvoice();
    public List<Integer> queryListStoreIdMInvoice();
    public List<Integer> queryListStoreIdSInvoice();
    public List<Integer> queryListStoreAHM();
    public List<Integer> queryListStoreGrab();
    public List<Integer> queryListStoreMPOS();
    public List<Integer> queryListStoreOCB();
    public List<Integer> queryListStoreTCB();
    public List<Integer> queryListActiveStore();
    public List<Integer> queryListTrialStore();
    public List<Integer> queryListValidStore(Object[] params);
    public List<Integer> queryListExpiredStore(Object[] params);
    public List<DataStoreConnectWebOrderDTO> listStoreConnectWeborder();
    public List<StoreOrdersDTO> listStoreIdCustomer();
}
