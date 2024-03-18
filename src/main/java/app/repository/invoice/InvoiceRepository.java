package app.repository.invoice;

import app.DTO.response.QuantityInvoiceDTO;

import java.util.List;

public interface InvoiceRepository {
    public List<QuantityInvoiceDTO> queryUnreleasedMeInvoices();
    public List<QuantityInvoiceDTO> queryReleasedMeInvoices();
    public List<QuantityInvoiceDTO> queryCQTMeInvoices();
    public List<QuantityInvoiceDTO> queryUnreleasedMInvoices();
    public List<QuantityInvoiceDTO> queryReleasedMInvoices();
    public List<QuantityInvoiceDTO> queryCQTMInvoices();
    public List<QuantityInvoiceDTO> queryUnreleasedSInvoices();
    public List<QuantityInvoiceDTO> queryReleasedSInvoices();
    public List<QuantityInvoiceDTO> queryCQTSInvoices();

}
