package org.egov.mrs.web.controller.application.reissue;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.service.MarriageApplicantService;
import org.egov.mrs.domain.service.MarriageDocumentService;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.mrs.domain.service.ReIssueService;
import org.egov.mrs.masters.entity.MarriageFee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller to correct the registration data
 *
 * @author nayeem
 *
 */

@Controller
@RequestMapping(value = "/reissue")
public class UpdateMrgReIssueController extends GenericWorkFlowController {
        
    private static final Logger LOG = Logger.getLogger(UpdateMrgReIssueController.class);
        
    @Autowired
    private FileStoreService fileStoreService;
        
    @Autowired
    private ReIssueService reIssueService;

    @Autowired
    private MarriageRegistrationService marriageRegistrationService;

    @Autowired
    private MarriageApplicantService marriageApplicantService;

    @Autowired
    private MarriageDocumentService marriageDocumentService;
    
    @Autowired
    protected ResourceBundleMessageSource messageSource;

    @Autowired
    private ReIssueService reissueService;
    
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String showReIssueForm(@PathVariable final Long id, final Model model) {
        final ReIssue reIssue = reIssueService.get(id);
        model.addAttribute("documents", marriageDocumentService.getReIssueApplicantDocs());
        model.addAttribute("reissue", reIssue);
        
        marriageRegistrationService.prepareDocumentsForView(reIssue.getRegistration());
        marriageApplicantService.prepareDocumentsForView(reIssue.getRegistration().getHusband());
        marriageApplicantService.prepareDocumentsForView(reIssue.getRegistration().getWife());
        marriageApplicantService.prepareDocumentsForView(reIssue.getApplicant());
        model.addAttribute("applicationHistory",
                reissueService.getHistory(reIssue));
        prepareWorkFlowForReIssue(reIssue, model);
        model.addAttribute("reIssue", reIssue);
        model.addAttribute("documents", marriageDocumentService.getGeneralDocuments());
        if(reIssue.getStatus().getCode().equalsIgnoreCase(ReIssue.ReIssueStatus.REJECTED.toString())){
            return "reissue-form"; 
        }
        return "reissue-view";
    }
    
    private void prepareWorkFlowForReIssue(final ReIssue reIssue, final Model model) {
        WorkflowContainer workFlowContainer = new WorkflowContainer();
        workFlowContainer.setAdditionalRule(MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        prepareWorkflow(model, reIssue, workFlowContainer);
        model.addAttribute("additionalRule", MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        model.addAttribute("stateType", reIssue.getClass().getSimpleName());  
    } 

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateReIssue(@RequestParam final Long id, @ModelAttribute ReIssue reIssue,
            @ModelAttribute final WorkflowContainer workflowContainer,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors) throws IOException {
        
        String workFlowAction = "";
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");
        
        if (errors.hasErrors())
            return "reissue-view"; 
         
        reIssue = reIssueService.get(id);
        if(workFlowAction != null && !workFlowAction.isEmpty()){
            workflowContainer.setWorkFlowAction(workFlowAction);
            workflowContainer.setApproverComments(request.getParameter("approvalComent"));
                if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_REJECT) || 
                    workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_CANCEL))
                    reIssueService.rejectReIssue(reIssue, workflowContainer,request);   
               else if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_APPROVE)) 
                   reIssueService.approveReIssue(reIssue, workflowContainer);   
               else if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_PRINTCERTIFICATE)) 
                   reIssueService.printCertificate(reIssue, workflowContainer, request);
               else{
                   workflowContainer.setApproverPositionId(Long.valueOf(request.getParameter("approvalPosition")));
                   reIssueService.forwardReIssue(id, reIssue,workflowContainer);
               }
        }
        // On print certificate, output registration certificate 
        if (workFlowAction != null && !workFlowAction.isEmpty()
                && workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_PRINTCERTIFICATE))
            return "redirect:/certificate/reissue?id="
                    + reIssue.getId();
       
        model.addAttribute("message", messageSource.getMessage("msg.update.reIssue", null, null)); 
        return "reissue-ack";
    }
}