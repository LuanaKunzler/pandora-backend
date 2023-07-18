package com.pandora.backend.service.admin;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Map;

public interface ReportAdminService {
    public JasperPrint loadReport(String reportFileName, Map<String, Object> parameters, Connection dataSource)
            throws JRException;
}
