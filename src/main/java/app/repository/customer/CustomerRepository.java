package app.repository.customer;

import app.DTO.response.QuantityDebtDTO;

import java.util.List;

public interface CustomerRepository {
    public QuantityDebtDTO queryListCustomer(Object[] param);
    public QuantityDebtDTO queryListDebtCollection(Object[] param);
    public QuantityDebtDTO queryListWriteDebt(Object[] param);

    public List<Integer> listMerchantId();

}
