/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.web.controller.mb;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.matrix.service.CustomizedWorkFlowService;
import org.egov.works.letterofacceptance.entity.SearchRequestLetterOfAcceptance;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.utils.WorksConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/workorder/searchform")
public class SearchWorkOrderForMBHeaderController extends GenericWorkFlowController {

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    protected CustomizedWorkFlowService customizedWorkFlowService;

    @RequestMapping(method = RequestMethod.GET)
    public String showSearchWorkOrder(
            @ModelAttribute final SearchRequestLetterOfAcceptance searchRequestLetterOfAcceptance, final Model model) {

        final List<Department> departments = lineEstimateService.getUserDepartments(securityUtils.getCurrentUser());
        if (departments != null && !departments.isEmpty())
            searchRequestLetterOfAcceptance.setDepartmentName(departments.get(0).getId());
        searchRequestLetterOfAcceptance.setEgwStatus(WorksConstants.WO_STATUS_WOCOMMENCED.toUpperCase());
        model.addAttribute("departments", lineEstimateService.getUserDepartments(securityUtils.getCurrentUser()));
        model.addAttribute("searchRequestLetterOfAcceptance", searchRequestLetterOfAcceptance);
        final MBHeader mbHeader = new MBHeader();
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        prepareWorkflow(model, mbHeader, workflowContainer);
        List<String> validActions = Collections.emptyList();
        validActions = customizedWorkFlowService.getNextValidActions(mbHeader.getStateType(),
                workflowContainer.getWorkFlowDepartment(), workflowContainer.getAmountRule(),
                workflowContainer.getAdditionalRule(), WorksConstants.NEW, workflowContainer.getPendingActions(),
                mbHeader.getCreatedDate());
        model.addAttribute("stateType", mbHeader.getClass().getSimpleName());
        if (mbHeader.getState() != null && mbHeader.getState().getNextAction() != null)
            model.addAttribute("nextAction", mbHeader.getState().getNextAction());
        model.addAttribute("validActionList", validActions);
        model.addAttribute("currentDate", new Date());
        model.addAttribute("mbHeader", mbHeader);
        model.addAttribute("documentDetails", mbHeader.getDocumentDetails());
        return "workorder-search";
    }

}