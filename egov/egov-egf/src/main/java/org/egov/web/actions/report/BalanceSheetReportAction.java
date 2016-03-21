/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.web.actions.report;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.services.report.BalanceSheetScheduleService;
import org.egov.services.report.BalanceSheetService;
import org.egov.utils.Constants;
import org.egov.utils.ReportHelper;
import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;

import net.sf.jasperreports.engine.JasperPrint;

@ParentPackage("egov")
@Results({
    @Result(name = "allScheduleDetailedResults", location = "balanceSheetReport-allScheduleDetailedResults.jsp"),
    @Result(name = "report", location = "balanceSheetReport-report.jsp"),
    @Result(name = "scheduleResults", location = "balanceSheetReport-scheduleResults.jsp"),
    @Result(name = "allScheduleResults", location = "balanceSheetReport-allScheduleResults.jsp"),
    @Result(name = "balanceSheet-PDF", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
            Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "application/pdf", Constants.CONTENT_DISPOSITION,
    "no-cache;filename=BalanceSheet.pdf" }),
    @Result(name = "balanceSheet-XLS", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
            Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "application/xls", Constants.CONTENT_DISPOSITION,
    "no-cache;filename=BalanceSheet.xls" }),
    @Result(name = "balanceSheet-HTML", type = "stream", location = Constants.INPUT_STREAM, params = { Constants.INPUT_NAME,
            Constants.INPUT_STREAM, Constants.CONTENT_TYPE, "text/html" })
})
public class BalanceSheetReportAction extends BaseFormAction {
    /**
     *
     */
    private static final long serialVersionUID = 7914013458428148999L;
    private static final String BALANCE_SHEET_PDF = "balanceSheet-PDF";
    private static final String BALANCE_SHEET_XLS = "balanceSheet-XLS";
    InputStream inputStream;
    ReportHelper reportHelper;
    Statement balanceSheet = new Statement();
    private Date todayDate;
    FinancialYearDAO financialYearDAO;
    CFinancialYear financialYear=new CFinancialYear();
    
    @Autowired
    private EgovMasterDataCaching masterDataCache;
    
    public FinancialYearDAO getFinancialYearDAO() {
		return financialYearDAO;
	}

	public void setFinancialYearDAO(FinancialYearDAO financialYearDAO) {
		this.financialYearDAO = financialYearDAO;
	}

	public Date getFromDate() {
        return balanceSheetService.getFromDate(balanceSheet);
    }

    public Date getToDate() {
        return balanceSheetService.getToDate(balanceSheet);
    }

    public void setFromDate(final Date fromDate) {
    }

    public Date getCurrentYearfromDate() {
        return getFromDate();
    }

    public void setCurrentYearfromDate(final Date currentYearfromDate) {
    }

    public Date getCurrentYeartoDate() {
        return getToDate();
    }

    public void setCurrentYeartoDate(final Date currentYeartoDate) {
    }

    public void setToDate(final Date toDate) {
    }

    public Date getPreviousYearfromDate() {
        return balanceSheetService.getPreviousYearFor(getFromDate());
    }

    public Date getPreviousYeartoDate() {
        return balanceSheetService.getPreviousYearFor(getToDate());
    }

    public void setPreviousYearfromDate(final Date previousYearfromDate) {
    }

    public void setPreviousYeartoDate(final Date previousYeartoDate) {
    }

    private StringBuffer header = new StringBuffer();
    // private String heading;
    BalanceSheetService balanceSheetService;
    BalanceSheetScheduleService balanceSheetScheduleService;
    public static final Locale LOCALE = new Locale("en", "IN");
    public static final SimpleDateFormat DDMMYYYYFORMATS = new SimpleDateFormat("dd/MM/yyyy", LOCALE);

    public void setBalanceSheetService(final BalanceSheetService balanceSheetService) {
        this.balanceSheetService = balanceSheetService;
    }

    public void setBalanceSheetScheduleService(final BalanceSheetScheduleService balanceSheetScheduleService) {
        this.balanceSheetScheduleService = balanceSheetScheduleService;
    }

    public void setReportHelper(final ReportHelper reportHelper) {
        this.reportHelper = reportHelper;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Statement getBalanceSheet() {
        return balanceSheet;
    }

    public BalanceSheetReportAction() {
        addRelatedEntity("department", Department.class);
        addRelatedEntity("function", CFunction.class);
        addRelatedEntity("fund", Fund.class);
        addRelatedEntity("functionary", Functionary.class);
        addRelatedEntity("financialYear", CFinancialYear.class);
        addRelatedEntity("field", Boundary.class);
    }

    @Override
    public void prepare() {
        HibernateUtil.getCurrentSession().setDefaultReadOnly(true);
        HibernateUtil.getCurrentSession().setFlushMode(FlushMode.MANUAL);
        super.prepare();
        if (!parameters.containsKey("showDropDown")) {
            addDropdownData("departmentList", masterDataCache.get("egi-department"));
            addDropdownData("fundList", masterDataCache.get("egi-fund"));
            addDropdownData("functionList", masterDataCache.get("egi-function"));
        //    addDropdownData("functionaryList", masterCache.get("egi-functionary"));
          //  addDropdownData("fieldList", masterCache.get("egi-ward"));
            // addDropdownData("financialYearList",
            // getPersistenceService().findAllBy("from CFinancialYear where isActive=true and isActiveForPosting=true order by finYearRange desc "));
            addDropdownData("financialYearList", persistenceService.findAllBy("from CFinancialYear order by finYearRange desc "));
        }
    }

    protected void setRelatedEntitesOn() {
        setTodayDate(new Date());
        if (balanceSheet.getFinancialYear() != null && balanceSheet.getFinancialYear().getId() != null)
            balanceSheet.setFinancialYear((CFinancialYear) getPersistenceService().find("from CFinancialYear where id=?",
                    balanceSheet.getFinancialYear().getId()));
        if (balanceSheet.getDepartment() != null && balanceSheet.getDepartment().getId() != null
                && balanceSheet.getDepartment().getId() != 0) {
            balanceSheet.setDepartment((Department) getPersistenceService().find("from Department where id=?",
                    balanceSheet.getDepartment().getId()));
            persistenceService.find("from Department where id=?", balanceSheet.getDepartment()
                    .getId());
            header.append(" in " + balanceSheet.getDepartment().getName());
        } else
            balanceSheet.setDepartment(null);
/*        if (balanceSheet.getField() != null && balanceSheet.getField().getId() != null && balanceSheet.getField().getId() != 0) {
            balanceSheet.setField((Boundary) getPersistenceService().find("from Boundary where id=?",
                    balanceSheet.getField().getId()));
            header.append(" in " + balanceSheet.getField().getName());
        }*/
        if (balanceSheet.getFund() != null && balanceSheet.getFund().getId() != null && balanceSheet.getFund().getId() != 0) {
            balanceSheet.setFund((Fund) getPersistenceService().find("from Fund where id=?", balanceSheet.getFund().getId()));
            header.append(" for " + balanceSheet.getFund().getName());
        }
        if (balanceSheet.getFunction() != null && balanceSheet.getFunction().getId() != null
                && balanceSheet.getFunction().getId() != 0) {
            balanceSheet.setFunction((CFunction) getPersistenceService().find("from CFunction where id=?",
                    balanceSheet.getFunction().getId()));
            header.append(" for " + balanceSheet.getFunction().getName());
        }
 /*       if (balanceSheet.getFunctionary() != null && balanceSheet.getFunctionary().getId() != null
                && balanceSheet.getFunctionary().getId() != 0) {
            balanceSheet.setFunctionary((Functionary) getPersistenceService().find("from Functionary where id=?",
                    balanceSheet.getFunctionary().getId()));
            header.append(" in " + balanceSheet.getFunctionary().getName());
        }*/
        if (balanceSheet.getAsOndate() != null)
            header.append(" as on " + DDMMYYYYFORMATS.format(balanceSheet.getAsOndate()));
        header.toString();
    }

    @Override
    public Object getModel() {
        return balanceSheet;
    }

    @SkipValidation
    @Action(value = "/report/balanceSheetReport-generateBalanceSheetReport")
    public String generateBalanceSheetReport() {
        return "report";
    }

    @Action(value = "/report/balanceSheetReport-generateBalanceSheetSubReport")
    public String generateBalanceSheetSubReport() {
        populateDataSourceForSchedule();
        return "scheduleResults";
    }

    @Action(value = "/report/balanceSheetReport-generateScheduleReport")
    public String generateScheduleReport() {
        populateDataSourceForAllSchedules();
        return "allScheduleResults";
    }

    /* for Detailed */
    @SkipValidation
    @Action(value = "/report/balanceSheetReport-generateScheduleReportDetailed")
    public String generateScheduleReportDetailed() {
        populateDataSourceForAllSchedulesDetailed();
        return "allScheduleDetailedResults";
    }

    private void populateDataSourceForSchedule() {
        setRelatedEntitesOn();
        if (balanceSheet.getFund() != null && balanceSheet.getFund().getId() != null) {
            final List<Fund> selFund = new ArrayList<Fund>();
            selFund.add(balanceSheet.getFund());
            balanceSheet.setFunds(selFund);
        } else
            balanceSheet.setFunds(balanceSheetService.getFunds());
        balanceSheetScheduleService.populateDataForSchedule(balanceSheet, parameters.get("majorCode")[0]);
    }

    private void populateDataSourceForAllSchedules() {
        setRelatedEntitesOn();
        if (balanceSheet.getFund() != null && balanceSheet.getFund().getId() != null && balanceSheet.getFund().getId() != 0) {
            final List<Fund> selFund = new ArrayList<Fund>();
            selFund.add(balanceSheet.getFund());
            balanceSheet.setFunds(selFund);
        } else
            balanceSheet.setFunds(balanceSheetService.getFunds());
        balanceSheetScheduleService.populateDataForAllSchedules(balanceSheet);
    }

    /* for detailed */
    private void populateDataSourceForAllSchedulesDetailed() {
        setRelatedEntitesOn();
        if (balanceSheet.getFund() != null && balanceSheet.getFund().getId() != null && balanceSheet.getFund().getId() != 0) {
            final List<Fund> selFund = new ArrayList<Fund>();
            selFund.add(balanceSheet.getFund());
            balanceSheet.setFunds(selFund);
        } else
            balanceSheet.setFunds(balanceSheetService.getFunds());
        balanceSheetScheduleService.populateDataForAllSchedulesDetailed(balanceSheet);
    }
    @Action(value = "/report/balanceSheetReport-printBalanceSheetReport")
    public String printBalanceSheetReport() {
        populateDataSource();
        return "report";
    }

    @Action(value = "/report/balanceSheetReport-generateBalanceSheetPdf")
    public String generateBalanceSheetPdf() throws Exception {
        populateDataSource();
        final JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,
                getText("report.heading"),
                header.toString(),
                getCurrentYearToDate(), getPreviousYearToDate(), true);
        inputStream = reportHelper.exportPdf(inputStream, jasper);
        return BALANCE_SHEET_PDF;
    }

    @Action(value = "/report/balanceSheetReport-generateBalanceSheetXls")
    public String generateBalanceSheetXls() throws Exception {
        populateDataSource();
        JasperPrint jasper = null;
        if (!balanceSheet.getPeriod().equalsIgnoreCase("Yearly"))
            jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet, getText("report.heading"),
                    header.toString(),
                    getCurrentYearToDate(), getPreviousYearToDate(), true);
        else
            jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet, getText("report.heading"),
                    header.toString(),
                    getCurrentYearToDate(), getPreviousYearToDate(), true);
        inputStream = reportHelper.exportXls(inputStream, jasper);
        return BALANCE_SHEET_XLS;
    }

    @Action(value = "/report/balanceSheetReport-generateSchedulePdf")
    public String generateSchedulePdf() throws Exception {
        populateDataSourceForAllSchedules();
        final JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,
                getText("report.heading"),
                header.toString(),
                getCurrentYearToDate(), getPreviousYearToDate(), false);
        inputStream = reportHelper.exportPdf(inputStream, jasper);
        return BALANCE_SHEET_PDF;
    }

    @Action(value = "/report/balanceSheetReport-generateScheduleXls")
    public String generateScheduleXls() throws Exception {
        populateDataSourceForAllSchedules();
        final JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,
                getText("report.heading"),
                header.toString(),
                getCurrentYearToDate(), getPreviousYearToDate(), false);
        inputStream = reportHelper.exportXls(inputStream, jasper);
        return BALANCE_SHEET_XLS;
    }

    /* for detailed */
    @Action(value = "/report/balanceSheetReport-generateDetailedSchedulePdf")
    public String generateDetailedSchedulePdf() throws Exception {
        populateDataSourceForAllSchedulesDetailed();
        final JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,
                getText("report.heading"),
                header.toString(),
                getCurrentYearToDate(), getPreviousYearToDate(), false);
        inputStream = reportHelper.exportPdf(inputStream, jasper);
        return BALANCE_SHEET_PDF;
    }

    /* for detailed */
    @Action(value = "/report/balanceSheetReport-generateDetailedScheduleXls")
    public String generateDetailedScheduleXls() throws Exception {
        populateDataSourceForAllSchedulesDetailed();
        final JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,
                getText("report.heading"),
                header.toString(),
                getCurrentYearToDate(), getPreviousYearToDate(), false);
        inputStream = reportHelper.exportXls(inputStream, jasper);
        return BALANCE_SHEET_XLS;
    }

    @Action(value = "/report/balanceSheetReport-generateBalanceSheetSchedulePdf")
    public String generateBalanceSheetSchedulePdf() throws Exception {
        populateDataSourceForSchedule();
        final JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,
                getText("report.sub.schedule.heading"), header.toString(),
                getCurrentYearToDate(), getPreviousYearToDate(), false);
        inputStream = reportHelper.exportPdf(inputStream, jasper);
        return BALANCE_SHEET_PDF;
    }

    @Action(value = "/report/balanceSheetReport-generateBalanceSheetScheduleXls")
    public String generateBalanceSheetScheduleXls() throws Exception {
        populateDataSourceForSchedule();
        final JasperPrint jasper = reportHelper.generateFinancialStatementReportJasperPrint(balanceSheet,
                getText("report.sub.schedule.heading"), header.toString(),
                getCurrentYearToDate(), getPreviousYearToDate(), false);
        inputStream = reportHelper.exportXls(inputStream, jasper);
        return BALANCE_SHEET_XLS;
    }

    protected void populateDataSource() {
    	
    	setRelatedEntitesOn();
        
        if (balanceSheet.getFund() != null && balanceSheet.getFund().getId() != null) {
            final List<Fund> selFund = new ArrayList<Fund>();
            selFund.add(balanceSheet.getFund());
            balanceSheet.setFunds(selFund);
        } else
            balanceSheet.setFunds(balanceSheetService.getFunds());
        balanceSheetService.populateBalanceSheet(balanceSheet);
    }

    //TODO- This table is not used. Check reference and remove
  
    public String getCurrentYearToDate() {
        return balanceSheetService.getFormattedDate(balanceSheetService.getToDate(balanceSheet));
    }

    public String getPreviousYearToDate() {
        return balanceSheetService.getFormattedDate(balanceSheetService.getPreviousYearFor(balanceSheetService
                .getToDate(balanceSheet)));
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(final Date todayDate) {
        this.todayDate = todayDate;
    }

    public StringBuffer getHeader() {
        return header;
    }

    public void setHeader(final StringBuffer header) {
        this.header = header;
    }

	
}