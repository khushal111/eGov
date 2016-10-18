/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.mrs.web.controller.application.reissue;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.mrs.application.MarriageUtils;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.service.MarriageApplicantService;
import org.egov.mrs.domain.service.MarriageDocumentService;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.mrs.domain.service.ReIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/reissue")
public class ViewReIssueController extends GenericWorkFlowController {
    
    private final ReIssueService reIssueService;
    private final MarriageRegistrationService marriageRegistrationService;
    
    @Autowired
    private MarriageApplicantService marriageApplicantService;
    
    @Autowired
    private MarriageDocumentService marriageDocumentService;
    
    @Autowired
    private MarriageUtils utils;
    
    @Autowired
    public ViewReIssueController(final ReIssueService reIssueService, final MarriageRegistrationService marriageRegistrationService) {
        this.reIssueService = reIssueService;
        this.marriageRegistrationService = marriageRegistrationService;
    }
    
    @RequestMapping(value = "/{reIssueId}", method = RequestMethod.GET)
    public String viewRegistration(@PathVariable final Long reIssueId, @RequestParam(required = false) String mode,
            final Model model) throws IOException {
        
        final ReIssue reIssue = reIssueService.get(reIssueId);
        final MarriageRegistration registration = reIssue.getRegistration();
        
        model.addAttribute("documents", marriageDocumentService.getReIssueApplicantDocs());
        model.addAttribute("reissue", reIssue);
        model.addAttribute("mode", mode);
        
        marriageRegistrationService.prepareDocumentsForView(registration);
        marriageApplicantService.prepareDocumentsForView(registration.getHusband());
        marriageApplicantService.prepareDocumentsForView(registration.getWife());
        marriageApplicantService.prepareDocumentsForView(reIssue.getApplicant());
                
        String screen = null;
       
        if (!reIssue.getStatus().getCode().equalsIgnoreCase(ReIssue.ReIssueStatus.APPROVED.toString())) {
            
            if (mode == null)
                mode = utils.isLoggedInUserApprover() ? "view" : mode;
            
            screen = mode != null && mode.equalsIgnoreCase("view") ? "reissue-view" : "reissue-form";
        } else
            screen = "reissue-view";

        prepareWorkflow(model, reIssue, new WorkflowContainer());
        return screen;
    }


    /**
     * Obtains the workflow paramaters from the HttpServletRequest
     *
     * @param workflowContainer
     * @param request
     */
    private void obtainWorkflowParameters(final WorkflowContainer workflowContainer, final HttpServletRequest request) {
        if (request.getParameter("approvalComent") != null)
            workflowContainer.setApproverComments(request.getParameter("approvalComent"));
        if (request.getParameter("workFlowAction") != null)
            workflowContainer.setWorkFlowAction(request.getParameter("workFlowAction"));
        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            workflowContainer.setApproverPositionId(Long.valueOf(request.getParameter("approvalPosition")));
    }
}