package app.repository.invoice;

import app.DTO.response.QuantityInvoiceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InvoiceRepositoryImp implements InvoiceRepository {
    @Autowired
    @Qualifier("mariadbInvoicesJdbcTemplate")
    private JdbcTemplate mariadbInvoicesJdbcTemplate;

    @Override
    public List<QuantityInvoiceDTO> queryUnreleasedMeInvoices() {
        String sql = "select store_id as storeId, count(*) as quantity from e_invoices where  status = 'created_order' and type = 'me_invoice' group by store_id;";
        List<QuantityInvoiceDTO> listUnreleasedInvoices = getInvoiceDTOS(sql);
        return listUnreleasedInvoices;
    }
    @Override
    public List<QuantityInvoiceDTO> queryReleasedMeInvoices() {
        String sql = "select store_id as storeId, count(*) as quantity from e_invoices where  status = 'signed' and tax_partner_number is null and type = 'me_invoice' group by store_id;";
        List<QuantityInvoiceDTO> listReleasedInvoices = getInvoiceDTOS(sql);
        return listReleasedInvoices;
    }

    @Override
    public List<QuantityInvoiceDTO> queryCQTMeInvoices() {
        String sql = "select store_id as storeId, count(*) as quantity from e_invoices where  status = 'signed' and tax_partner_number is not null and type = 'me_invoice' group by store_id;";
        List<QuantityInvoiceDTO> listCQTInvoices = getInvoiceDTOS(sql);
        return listCQTInvoices;
    }

    @Override
    public List<QuantityInvoiceDTO> queryUnreleasedMInvoices() {
        String sql = "select store_id as storeId, count(*) as quantity from e_invoices where  status = 'created_order' and type = 'm_invoice' group by store_id;";
        List<QuantityInvoiceDTO> listUnreleasedInvoices = getInvoiceDTOS(sql);
        return listUnreleasedInvoices;
    }

    @Override
    public List<QuantityInvoiceDTO> queryReleasedMInvoices() {
        String sql = "select store_id as storeId, count(*) as quantity from e_invoices where  status = 'signed' and tax_partner_number is null and type = 'm_invoice' group by store_id;";
        List<QuantityInvoiceDTO> listUnreleasedInvoices = getInvoiceDTOS(sql);
        return listUnreleasedInvoices;
    }

    @Override
    public List<QuantityInvoiceDTO> queryCQTMInvoices() {
        String sql = "select store_id as storeId, count(*) as quantity from e_invoices where  status = 'signed' and tax_partner_number is not null and type = 'm_invoice' group by store_id;";
        List<QuantityInvoiceDTO> listUnreleasedInvoices = getInvoiceDTOS(sql);
        return listUnreleasedInvoices;
    }

    @Override
    public List<QuantityInvoiceDTO> queryUnreleasedSInvoices() {
        String sql = "select store_id as storeId, count(*) as quantity from e_invoices where  status = 'created_order' and type = 's_invoice' group by store_id;";
        List<QuantityInvoiceDTO> listUnreleasedInvoices = getInvoiceDTOS(sql);
        return listUnreleasedInvoices;
    }

    @Override
    public List<QuantityInvoiceDTO> queryReleasedSInvoices() {
        String sql = "select store_id as storeId, count(*) as quantity from e_invoices where  status = 'signed' and tax_partner_number is null and type = 's_invoice' group by store_id;";
        List<QuantityInvoiceDTO> listUnreleasedInvoices = getInvoiceDTOS(sql);
        return listUnreleasedInvoices;
    }

    @Override
    public List<QuantityInvoiceDTO> queryCQTSInvoices() {
        String sql = "select store_id as storeId, count(*) as quantity from e_invoices where  status = 'signed' and tax_partner_number is not null and type = 's_invoice' group by store_id;";
        List<QuantityInvoiceDTO> listUnreleasedInvoices = getInvoiceDTOS(sql);
        return listUnreleasedInvoices;
    }

    private List<QuantityInvoiceDTO> getInvoiceDTOS(String sql) {
        List<QuantityInvoiceDTO> listInvoices = mariadbInvoicesJdbcTemplate.query(sql, (rs, rownum) -> {
            return new QuantityInvoiceDTO(rs.getInt("storeid"),
                    rs.getInt("quantity")
            );
        });
        return listInvoices;
    }
}
