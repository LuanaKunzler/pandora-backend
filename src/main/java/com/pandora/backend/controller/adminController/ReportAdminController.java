package com.pandora.backend.controller.adminController;

import com.pandora.backend.model.entity.Sale;
import com.pandora.backend.service.admin.ReportAdminService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReportAdminController extends ApiAdminController {
    private final ReportAdminService service;
    private final DataSource dataSource;

    public ReportAdminController(ReportAdminService service, DataSource dataSource) {
        this.service = service;
        this.dataSource = dataSource;
    }

    @GetMapping("/report/periodic-sales")
    public ResponseEntity<byte[]> generatePeriodicSalesReport(@RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                              @RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate)
            throws JRException, IOException, SQLException {

        Connection connection = dataSource.getConnection();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start_date", startDate);
        parameters.put("end_date", endDate);

        JasperPrint jasperPrint = service.loadReport("periodicoVendas.jrxml", parameters, connection);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
        byte[] reportBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "periodic-sales-report.pdf");

        return ResponseEntity.ok().headers(headers).body(reportBytes);
    }

}
