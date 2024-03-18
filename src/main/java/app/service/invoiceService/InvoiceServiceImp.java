package app.service.invoiceService;

import app.DTO.response.QuantityInvoiceDTO;
import app.DTO.response.InvoiceDTO;
import app.repository.invoice.InvoiceRepository;
import app.repository.store.StoreRepository;
import app.utils.ExcelUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImp implements InvoiceService {

    public static final String LIST_M_INVOICE = "ListMInvoice";
    public static final String LIST_ME_INVOICE = "ListMeInvoice";
    public static final String LIST_S_INVOICE = "ListSInvoice";
    @Autowired
    @Qualifier("mariadbInvoicesJdbcTemplate")
    private JdbcTemplate mariadbInvoicesJdbcTemplate;
    @Value("${default.number.Thread}")
    private int DEFAULT_NUMBER_THREAD;
    @Autowired
    private ExcelUtils excelUtils;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public String getListMe_Invoice(HttpServletResponse response) throws IOException {

        try {
            boolean fileExists = excelUtils.fileExist(LIST_ME_INVOICE, response);
            if (fileExists) {
                return "thành công";
            }

            List<Integer> listMe_Invoice = new ArrayList<Integer>();
            List<QuantityInvoiceDTO> listUnrelease = new ArrayList<QuantityInvoiceDTO>();
            List<QuantityInvoiceDTO> listRelease = new ArrayList<QuantityInvoiceDTO>();
            List<QuantityInvoiceDTO> listInvoiceCQT = new ArrayList<QuantityInvoiceDTO>();


            CompletableFuture<Void> getListMe_Invoice = CompletableFuture.runAsync(() -> {
                try {
                    List<Integer> listStoreId = storeRepository.queryListStoreIdMeInvoice();
                    listMe_Invoice.addAll(listStoreId);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
            CompletableFuture<Void> getListUnreleasedInvoices = CompletableFuture.runAsync(() -> {
                try {
                    List<QuantityInvoiceDTO> listUnreleasedInvoices = invoiceRepository.queryUnreleasedMeInvoices();
                    listUnrelease.addAll(listUnreleasedInvoices);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
            CompletableFuture<Void> getListReleasedInvoices = CompletableFuture.runAsync(() -> {
                try {
                    List<QuantityInvoiceDTO> listReleasedInvoices = invoiceRepository.queryReleasedMeInvoices();
                    listRelease.addAll(listReleasedInvoices);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
            CompletableFuture<Void> getListCQTInvoices = CompletableFuture.runAsync(() -> {
                try {
                    List<QuantityInvoiceDTO> listCQTInvoices = invoiceRepository.queryCQTMeInvoices();
                    listInvoiceCQT.addAll(listCQTInvoices);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
            CompletableFuture.allOf(getListMe_Invoice, getListUnreleasedInvoices, getListReleasedInvoices, getListCQTInvoices).join();
            List<InvoiceDTO> invoiceExport = mergeData(listMe_Invoice, listUnrelease, listRelease, listInvoiceCQT);
            excelUtils.exportDataToExcel(response, LIST_ME_INVOICE, InvoiceDTO.class, invoiceExport);
            return "thành công";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getListM_Invoice(HttpServletResponse response) throws IOException {

        try {
            boolean fileExists = excelUtils.fileExist(LIST_M_INVOICE, response);
            if (fileExists) {
                return "thành công";
            }
            List<Integer> listM_Invoice = new ArrayList<Integer>();
            List<QuantityInvoiceDTO> listUnrelease = new ArrayList<QuantityInvoiceDTO>();
            List<QuantityInvoiceDTO> listRelease = new ArrayList<QuantityInvoiceDTO>();
            List<QuantityInvoiceDTO> listInvoiceCQT = new ArrayList<QuantityInvoiceDTO>();


            CompletableFuture<Void> getListM_Invoice = CompletableFuture.runAsync(() -> {
                System.out.println("Start");
                try {
                    List<Integer> listStoreId = storeRepository.queryListStoreIdMInvoice();
                    listM_Invoice.addAll(listStoreId);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
                System.out.println("Completed");
            });
            CompletableFuture<Void> getListUnreleasedInvoices = CompletableFuture.runAsync(() -> {
                try {
                    List<QuantityInvoiceDTO> listUnreleasedInvoices = invoiceRepository.queryUnreleasedMInvoices();
                    listUnrelease.addAll(listUnreleasedInvoices);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
            CompletableFuture<Void> getListReleasedInvoices = CompletableFuture.runAsync(() -> {
                System.out.println("Start");
                try {
                    List<QuantityInvoiceDTO> listReleasedInvoices = invoiceRepository.queryReleasedMInvoices();
                    listRelease.addAll(listReleasedInvoices);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
                System.out.println("Completed");
            });
            CompletableFuture<Void> getListCQTInvoices = CompletableFuture.runAsync(() -> {
                System.out.println("Start");
                try {
                    List<QuantityInvoiceDTO> listCQTInvoices = invoiceRepository.queryCQTMInvoices();
                    listInvoiceCQT.addAll(listCQTInvoices);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
                System.out.println("Completed");
            });
            CompletableFuture.allOf(getListM_Invoice, getListUnreleasedInvoices, getListReleasedInvoices, getListCQTInvoices).join();
            List<InvoiceDTO> invoiceExport = mergeData(listM_Invoice, listUnrelease, listRelease, listInvoiceCQT);
            excelUtils.exportDataToExcel(response, LIST_M_INVOICE, InvoiceDTO.class, invoiceExport);

            return "thành công";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getListS_Invoice(HttpServletResponse response) throws IOException {

        try {
            boolean fileExists = excelUtils.fileExist(LIST_S_INVOICE, response);
            if (fileExists) {
                return "thành công";
            }
            List<Integer> listS_Invoice = new ArrayList<Integer>();
            List<QuantityInvoiceDTO> listUnrelease = new ArrayList<QuantityInvoiceDTO>();
            List<QuantityInvoiceDTO> listRelease = new ArrayList<QuantityInvoiceDTO>();
            List<QuantityInvoiceDTO> listInvoiceCQT = new ArrayList<QuantityInvoiceDTO>();

            CompletableFuture<Void> getListS_Invoice = CompletableFuture.runAsync(() -> {
                try {
                    List<Integer> listStoreId = storeRepository.queryListStoreIdSInvoice();
                    listS_Invoice.addAll(listStoreId);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
            CompletableFuture<Void> getListUnreleasedInvoices = CompletableFuture.runAsync(() -> {
                try {
                    List<QuantityInvoiceDTO> listUnreleasedInvoices = invoiceRepository.queryUnreleasedSInvoices();
                    listUnrelease.addAll(listUnreleasedInvoices);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
            CompletableFuture<Void> getListReleasedInvoices = CompletableFuture.runAsync(() -> {
                try {
                    String sql = "select store_id as storeId, count(*) as quantity from e_invoices where  status = 'signed' and tax_partner_number is null and type = 's_invoice' group by store_id;";
                    List<QuantityInvoiceDTO> listReleasedInvoices = invoiceRepository.queryReleasedSInvoices();
                    listRelease.addAll(listReleasedInvoices);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
            CompletableFuture<Void> getListCQTInvoices = CompletableFuture.runAsync(() -> {
                try {
                    List<QuantityInvoiceDTO> listCQTInvoices = invoiceRepository.queryCQTSInvoices();
                    listInvoiceCQT.addAll(listCQTInvoices);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            });
            CompletableFuture.allOf(getListS_Invoice, getListUnreleasedInvoices, getListReleasedInvoices, getListCQTInvoices).join();
            List<InvoiceDTO> invoiceExport = mergeData(listS_Invoice, listUnrelease, listRelease, listInvoiceCQT);
            excelUtils.exportDataToExcel(response, LIST_S_INVOICE, InvoiceDTO.class, invoiceExport);
            return "thành công";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<InvoiceDTO> mergeData(List<Integer> listInvoice,
                                       List<QuantityInvoiceDTO> listUnrelease,
                                       List<QuantityInvoiceDTO> listRelease,
                                       List<QuantityInvoiceDTO> listInvoiceCQT) {

        Map<Integer, Number> quantityUnrelease = listUnrelease.stream().collect(Collectors.toMap(
                obj -> obj.getStoreId(),
                obj -> obj.getQuantityInvoice()
        ));
        Map<Integer, Number> quantityRelease = listRelease.stream().collect(Collectors.toMap(
                obj -> obj.getStoreId(),
                obj -> obj.getQuantityInvoice()
        ));
        Map<Integer, Number> quantityCQT = listInvoiceCQT.stream().collect(Collectors.toMap(
                obj -> obj.getStoreId(),
                obj -> obj.getQuantityInvoice()
        ));
        List<InvoiceDTO> listInvoiceDto = new ArrayList<>();
        listInvoice.stream().forEach(storeId -> {
            InvoiceDTO invoiceDTO = new InvoiceDTO(storeId, quantityUnrelease.get(storeId), quantityRelease.get(storeId), quantityCQT.get(storeId));
            listInvoiceDto.add(invoiceDTO);
        });
        return listInvoiceDto;
    }
}
