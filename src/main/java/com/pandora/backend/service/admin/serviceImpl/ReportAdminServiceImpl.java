package com.pandora.backend.service.admin.serviceImpl;

import com.pandora.backend.repository.admin.ReportAdminRepository;
import com.pandora.backend.service.admin.ReportAdminService;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.util.Map;

@Service
public class ReportAdminServiceImpl implements ReportAdminService {


    private final ReportAdminRepository repository;

    public ReportAdminServiceImpl(ReportAdminRepository repository) {
        this.repository = repository;
    }

    @Override
    public JasperPrint loadReport(String reportFileName, Map<String, Object> parameters, Connection dataSource) throws JRException {
        String reportPath = "reports/" + reportFileName;
        JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return jasperPrint;
    }

}
