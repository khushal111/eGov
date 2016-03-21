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
/*
 * Created on Jan 17, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.dao.budget;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.egov.commons.CChartOfAccounts;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.dao.GenericDAO;
import org.egov.model.budget.BudgetDetail;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.budget.BudgetUsage;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public interface BudgetDetailsDAO extends GenericDAO {
    public boolean consumeEncumbranceBudget(Long financialyearid, Integer moduleid, String referencenumber, Integer departmentid,
            Long functionid, Integer functionaryid, Integer schemeid, Integer subschemeid, Integer fieldid,
            List<Long> budgetheadid, Integer fundid, double amount, String appropriationnumber) throws ValidationException;

    public BudgetUsage consumeEncumbranceBudget(String appropriationnumber, Long financialyearid, Integer moduleid,
            String referencenumber, Integer departmentid, Long functionid, Integer functionaryid, Integer schemeid,
            Integer subschemeid, Integer fieldid, List<Long> budgetheadid, Integer fundid, double amount)
                    throws ValidationException;

    public void releaseEncumbranceBudget(Long financialyearid, Integer moduleid, String referencenumber, Integer departmentid,
            Long functionid, Integer functionaryid, Integer schemeid, Integer subschemeid, Integer fieldid,
            List<Long> budgetheadid, Integer fundid, double amount, String appropriationnumber) throws ValidationException;

    public BudgetUsage releaseEncumbranceBudget(String appropriationnumber, Long financialyearid, Integer moduleid,
            String referencenumber, Integer departmentid, Long functionid, Integer functionaryid, Integer schemeid,
            Integer subschemeid, Integer fieldid, List<Long> budgetheadid, Integer fundid, double amount)
                    throws ValidationException;

    public BigDecimal getActualBudgetUtilized(Map<String, Object> paramMap) throws ValidationException;

    public BigDecimal getSanctionedPlanningBudget(Map<String, Object> paramMap) throws ValidationException;

    public BigDecimal getPlanningBudgetAvailable(Long financialyearid, Integer departmentid, Long functionid,
            Integer functionaryid, Integer schemeid, Integer subschemeid, Integer boundaryid, List<Long> budgetheadid,
            Integer fundid) throws ValidationException;

    public BigDecimal getBudgetedAmtForYear(Map<String, Object> paramMap) throws ValidationException;

    public BigDecimal getBudgetedAmtForYearAsOnDate(Map<String, Object> paramMap, Date asOnDate) throws ValidationException;

    public BigDecimal getPlanningPercentForYear(Map<String, Object> paramMap) throws ValidationException;

    // This will be used for works report
    public Map<String, BigDecimal> getAggregateBudgetedAmtForYear(Map<String, Object> paramMap) throws ValidationException;

    public BigDecimal getBudgetedAmtForYearRegardingBEorRE(Map<String, Object> paramMap, String type) throws ValidationException;

    public boolean budgetaryCheck(Map<String, Object> paramMap) throws ValidationException;

    public boolean budgetaryCheckForBill(Map<String, Object> paramMap) throws ValidationException;

    public List<BudgetUsage> getListBudgetUsage(Map<String, Object> queryParamMap) throws ValidationException;

    public BigDecimal getBillAmountForBudgetCheck(Map<String, Object> paramMap) throws ValidationException;

    public BigDecimal getActualBudgetUtilizedForBudgetaryCheck(Map<String, Object> paramMap) throws ValidationException;

    public List<BudgetGroup> getBudgetHeadByGlcode(CChartOfAccounts coa) throws ValidationException;

    public List<BudgetGroup> getBudgetHeadForGlcodeList(List<CChartOfAccounts> coa) throws ValidationException;

    public BigDecimal getPlannigBudgetBy(Integer fundId, Integer deptId, Date asOnDate);

    public BigDecimal getPlanningBudgetUsage(BudgetDetail bd);
}