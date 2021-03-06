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

package org.egov.tl.service;

import org.egov.commons.Installment;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Position;
import org.egov.tl.entity.FeeMatrixDetail;
import org.egov.tl.entity.License;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.entity.LicenseDemand;
import org.egov.tl.entity.LicenseDocument;
import org.egov.tl.entity.LicenseDocumentType;
import org.egov.tl.entity.NatureOfBusiness;
import org.egov.tl.entity.WorkflowBean;
import org.egov.tl.entity.enums.ApplicationType;
import org.egov.tl.repository.LicenseRepository;
import org.egov.tl.utils.Constants;
import org.egov.tl.utils.LicenseNumberUtils;
import org.hibernate.CacheMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static java.math.BigDecimal.ZERO;
import static org.egov.tl.utils.Constants.APPLICATION_STATUS_CREATED_CODE;
import static org.egov.tl.utils.Constants.BUTTONAPPROVE;
import static org.egov.tl.utils.Constants.BUTTONREJECT;
import static org.egov.tl.utils.Constants.GENERATECERTIFICATE;
import static org.egov.tl.utils.Constants.LICENSE_STATUS_ACKNOWLEDGED;
import static org.egov.tl.utils.Constants.LICENSE_STATUS_ACTIVE;
import static org.egov.tl.utils.Constants.TRADELICENSEMODULE;
import static org.egov.tl.utils.Constants.WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING;
import static org.egov.tl.utils.Constants.WORKFLOW_STATE_REJECTED;

@Transactional(readOnly = true)
public abstract class AbstractLicenseService<T extends License> {

    public static final String ARREAR = "arrear";
    @Autowired
    @Qualifier("entityQueryService")
    protected PersistenceService entityQueryService;
    
    @Autowired
    protected InstallmentHibDao installmentDao;

    @Autowired
    protected LicenseNumberUtils licenseNumberUtils;
   
    @Autowired
    protected DocumentTypeService documentTypeService;

    
    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    protected FileStoreService fileStoreService;

    @Autowired
    protected FeeMatrixService feeMatrixService;

    @Autowired
    @Qualifier("licenseDocumentTypeService")
    protected PersistenceService<LicenseDocumentType, Long> licenseDocumentTypeService;

    @Autowired
    protected TradeLicenseUpdateIndexService updateIndexService;

    @Autowired
    protected SecurityUtils securityUtils;

    @Autowired
    protected DemandGenericHibDao demandGenericDao;

    @Autowired
    protected ValidityService validityService;

    protected SimpleWorkflowService<T> licenseWorkflowService;

    @Autowired
    protected LicenseRepository licenseRepository;

    @Autowired
    protected LicenseStatusService licenseStatusService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    protected LicenseAppTypeService licenseAppTypeService;

    @Autowired
    protected PositionMasterService positionMasterService;

    @Autowired
    protected NatureOfBusinessService natureOfBusinessService;

    protected abstract LicenseAppType getLicenseApplicationTypeForRenew();

    protected abstract LicenseAppType getLicenseApplicationType();

    protected abstract Module getModuleName();

    protected abstract NatureOfBusiness getNatureOfBusiness();

    protected abstract void sendEmailAndSMS(T license, String currentAction);

    public void setLicenseWorkflowService(SimpleWorkflowService<T> licenseWorkflowService) {
        this.licenseWorkflowService = licenseWorkflowService;
    }

    public T getLicenseById(Long id) {
        return (T) this.licenseRepository.findOne(id);
    }

    @Transactional
    public void create(T license, WorkflowBean workflowBean) {
        license.setLicenseAppType(getLicenseApplicationType());
        raiseNewDemand(license);
        license.getLicensee().setLicense(license);
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
        license.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_CREATED_CODE));
        license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        processAndStoreDocument(license.getDocuments(), license);
        transitionWorkFlow(license, workflowBean);
        licenseRepository.save(license);
        sendEmailAndSMS(license, workflowBean.getWorkFlowAction());
        updateIndexService.updateTradeLicenseIndexes(license);

    }

    private BigDecimal raiseNewDemand(T license) {
        LicenseDemand ld = new LicenseDemand();
        Module moduleName = this.getModuleName();
        BigDecimal totalAmount = ZERO;
        Installment installment = this.installmentDao.getInsatllmentByModuleForGivenDate(moduleName,
                license.getApplicationDate());
        ld.setIsHistory("N");
        ld.setEgInstallmentMaster(installment);
        ld.setLicense(license);
        ld.setIsLateRenewal('0');
        ld.setCreateDate(new Date());
        List<FeeMatrixDetail> feeMatrixDetails = this.feeMatrixService.findFeeList(license);
        for (FeeMatrixDetail fm : feeMatrixDetails) {
            EgDemandReasonMaster reasonMaster = this.demandGenericDao
                    .getDemandReasonMasterByCode(fm.getFeeMatrix().getFeeType().getName(), moduleName);
            EgDemandReason reason = this.demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(reasonMaster, installment,
                    moduleName);
            if (fm.getFeeMatrix().getFeeType().getName().contains("Late"))
                continue;

            if (reason != null) {
                ld.getEgDemandDetails().add(EgDemandDetails.fromReasonAndAmounts(fm.getAmount(), reason, ZERO));
                totalAmount = totalAmount.add(fm.getAmount());
            }
        }

        ld.setBaseDemand(totalAmount);
        license.setLicenseDemand(ld);
        return totalAmount;
    }

    public License updateDemandForChangeTradeArea(T license) {
        LicenseDemand licenseDemand = license.getLicenseDemand();
        Module moduleName = this.getModuleName();
        Installment installment = this.installmentDao.getInsatllmentByModuleForGivenDate(moduleName,
                license.getApplicationDate());
        Set<EgDemandDetails> demandDetails = licenseDemand.getEgDemandDetails();
        List<FeeMatrixDetail> feeList = this.feeMatrixService.findFeeList(license);
        for (EgDemandDetails dmd : demandDetails)
            for (FeeMatrixDetail fm : feeList)
                if (installment.getId().equals(dmd.getEgDemandReason().getEgInstallmentMaster().getId()) &&
                        (dmd.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(fm.getFeeMatrix().getFeeType().getName()))) {
                        dmd.setAmount(fm.getAmount());
                        dmd.setModifiedDate(new Date());
                    }
        this.recalculateBaseDemand(licenseDemand);
        return license;

    }

    @Transactional
    public BigDecimal recalculateDemand(List<FeeMatrixDetail> feeList, T license) {
        Installment installment = this.installmentDao.getInsatllmentByModuleForGivenDate(this.getModuleName(), new Date());
        BigDecimal totalAmount = ZERO;
        LicenseDemand licenseDemand = license.getCurrentDemand();
        // Recalculating current demand detail according to fee matrix
        for (EgDemandDetails dmd : licenseDemand.getEgDemandDetails())
            for (FeeMatrixDetail fm : feeList)
                if (installment.getId().equals(dmd.getEgDemandReason().getEgInstallmentMaster().getId()) &&
                        (dmd.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(fm.getFeeMatrix().getFeeType().getName()))) {
                        dmd.setAmount(fm.getAmount());
                        dmd.setAmtCollected(ZERO);
                        totalAmount = totalAmount.add(fm.getAmount());
                    }
        this.recalculateBaseDemand(licenseDemand);
        return totalAmount;
    }

    @Transactional
    public void createLegacyLicense(T license, Map<Integer, Integer> legacyInstallmentwiseFees,
                                    Map<Integer, Boolean> legacyFeePayStatus) {
        if (!licenseRepository.findByOldLicenseNumber(license.getOldLicenseNumber())
                .isEmpty())
            throw new ValidationException("TL-001", "TL-001", license.getOldLicenseNumber());
        this.addLegacyDemand(legacyInstallmentwiseFees, legacyFeePayStatus, license);
        processAndStoreDocument(license.getDocuments(), license);
        license.setLicenseAppType(getLicenseApplicationType());
        license.getLicensee().setLicense(license);
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACTIVE));
        license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        license.setLegacy(true);
        license.setActive(true);
        license.setLicenseNumber(licenseNumberUtils.generateLicenseNumber());
        this.validityService.applyLicenseValidity(license);
        licenseRepository.save(license);
    }

    private void addLegacyDemand(Map<Integer, Integer> legacyInstallmentwiseFees,
                                 Map<Integer, Boolean> legacyFeePayStatus,
                                 T license) {
        LicenseDemand licenseDemand = new LicenseDemand();
        licenseDemand.setIsHistory("N");
        licenseDemand.setCreateDate(new Date());
        licenseDemand.setLicense(license);
        licenseDemand.setIsLateRenewal('0');
        Module module = this.getModuleName();
        for (Entry<Integer, Integer> legacyInstallmentwiseFee : legacyInstallmentwiseFees.entrySet())
            if (legacyInstallmentwiseFee.getValue() != null && legacyInstallmentwiseFee.getValue() > 0) {
                Installment installment = this.installmentDao.fetchInstallmentByModuleAndInstallmentNumber(module,
                        legacyInstallmentwiseFee.getKey());

                licenseDemand.setEgInstallmentMaster(installment);
                BigDecimal demandAmount = BigDecimal.valueOf(legacyInstallmentwiseFee.getValue());
                BigDecimal amtCollected = legacyFeePayStatus.get(legacyInstallmentwiseFee.getKey()) == null
                        || !legacyFeePayStatus.get(legacyInstallmentwiseFee.getKey()) ? ZERO : demandAmount;
                licenseDemand.getEgDemandDetails().add(
                        EgDemandDetails.fromReasonAndAmounts(demandAmount,
                                this.demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                                        this.demandGenericDao.getDemandReasonMasterByCode("License Fee", module),
                                        installment, module),
                                amtCollected));
                licenseDemand.setBaseDemand(demandAmount.add(licenseDemand.getBaseDemand()));
                licenseDemand.setAmtCollected(amtCollected.add(licenseDemand.getAmtCollected()));
            }
        license.setLicenseDemand(licenseDemand);

    }

    @Transactional
    public void updateLegacyLicense(T license, Map<Integer, Integer> updatedInstallmentFees,
                                    Map<Integer, Boolean> legacyFeePayStatus) {
        this.updateLegacyDemand(license, updatedInstallmentFees, legacyFeePayStatus);
        processAndStoreDocument(license.getDocuments(), license);
        licenseRepository.save(license);
    }

    private void updateLegacyDemand(T license, Map<Integer, Integer> updatedInstallmentFees,
                                    Map<Integer, Boolean> legacyFeePayStatus) {
        LicenseDemand licenseDemand = license.getCurrentDemand();

        // Update existing demand details
        Iterator<EgDemandDetails> demandDetails = licenseDemand.getEgDemandDetails().iterator();
        while (demandDetails.hasNext()) {
            EgDemandDetails demandDetail = demandDetails.next();
            Integer installmentNumber = demandDetail.getEgDemandReason().getEgInstallmentMaster()
                    .getInstallmentNumber();
            Integer updatedFee = updatedInstallmentFees.get(installmentNumber);
            Boolean feePaymentStatus = legacyFeePayStatus.get(installmentNumber);
            if (updatedFee != null) {
                BigDecimal updatedDemandAmt = BigDecimal.valueOf(updatedFee);
                demandDetail.setAmount(updatedDemandAmt);
                if (feePaymentStatus != null && feePaymentStatus)
                    demandDetail.setAmtCollected(updatedDemandAmt);
                else
                    demandDetail.setAmtCollected(ZERO);

            } else
                demandDetails.remove();
            updatedInstallmentFees.put(installmentNumber, 0);
        }

        // Create demand details which is newly entered
        Module module = this.getModuleName();
        for (Entry<Integer, Integer> updatedInstallmentFee : updatedInstallmentFees.entrySet())
            if (updatedInstallmentFee.getValue() != null && updatedInstallmentFee.getValue() > 0) {
                Installment installment = this.installmentDao.fetchInstallmentByModuleAndInstallmentNumber(module,
                        updatedInstallmentFee.getKey());
                BigDecimal demandAmount = BigDecimal.valueOf(updatedInstallmentFee.getValue());
                BigDecimal amtCollected = legacyFeePayStatus.get(updatedInstallmentFee.getKey()) == null
                        || !legacyFeePayStatus.get(updatedInstallmentFee.getKey()) ? ZERO : demandAmount;
                licenseDemand.getEgDemandDetails().add(
                        EgDemandDetails.fromReasonAndAmounts(demandAmount,
                                this.demandGenericDao.getDmdReasonByDmdReasonMsterInstallAndMod(
                                        this.demandGenericDao.getDemandReasonMasterByCode("License Fee", module),
                                        installment, module),
                                amtCollected));
            }
        // Recalculating BasedDemand
        this.recalculateBaseDemand(licenseDemand);

    }

    public void recalculateBaseDemand(LicenseDemand licenseDemand) {
        licenseDemand.setAmtCollected(ZERO);
        licenseDemand.setBaseDemand(ZERO);
        for (EgDemandDetails demandDetail : licenseDemand.getEgDemandDetails()) {
            licenseDemand.setAmtCollected(licenseDemand.getAmtCollected().add(demandDetail.getAmtCollected()));
            licenseDemand.setBaseDemand(licenseDemand.getBaseDemand().add(demandDetail.getAmount()));
        }
    }

    @Transactional
    public void renew(T license, WorkflowBean workflowBean) {
        license.setApplicationNumber(licenseNumberUtils.generateApplicationNumber());
        recalculateDemand(this.feeMatrixService.findFeeList(license), license);
        license.setStatus(licenseStatusService.getLicenseStatusByName(LICENSE_STATUS_ACKNOWLEDGED));
        license.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(TRADELICENSEMODULE, APPLICATION_STATUS_CREATED_CODE));
        Position pos = null;
        license.setLicenseAppType(this.getLicenseApplicationTypeForRenew());
        User currentUser = this.securityUtils.getCurrentUser();
        if (workflowBean.getApproverPositionId() != null && workflowBean.getApproverPositionId() != -1)
            pos = positionMasterService.getPositionById(workflowBean.getApproverPositionId());
        WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
        license.reinitiateTransition().start().withSenderName(currentUser.getUsername() + "::" + currentUser.getName())
                .withComments(workflowBean.getApproverComments())
                .withStateValue(wfmatrix.getNextState()).withDateInfo(new DateTime().toDate()).withOwner(pos)
                .withNextAction(wfmatrix.getNextAction());
        this.licenseRepository.save(license);
        sendEmailAndSMS(license, workflowBean.getWorkFlowAction());
        updateIndexService.updateTradeLicenseIndexes(license);
    }

    @Transactional
    public void transitionWorkFlow(T license, WorkflowBean workflowBean) {
        DateTime currentDate = new DateTime();
        User user = this.securityUtils.getCurrentUser();
        Assignment userAssignment = this.assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;

        if (null != license.getId())
            wfInitiator = this.getWorkflowInitiator(license);

        if (wfInitiator != null && BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            if ( wfInitiator.equals(userAssignment)) {
                license.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
                if (license.getLicenseAppType() != null
                        && license.getLicenseAppType().getName().equals(Constants.RENEWAL_LIC_APPTYPE))
                    license.setLicenseAppType(this.getLicenseApplicationType());

            } else {
                String stateValue = license.getCurrentState().getValue().split(":")[0] + ":" + WORKFLOW_STATE_REJECTED;
                license.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction(WF_STATE_SANITORY_INSPECTOR_APPROVAL_PENDING);
            }

        } else if (GENERATECERTIFICATE.equalsIgnoreCase(workflowBean.getWorkFlowAction()))
            license.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(workflowBean.getApproverComments())
                    .withDateInfo(currentDate.toDate());
        else {
            if (null != workflowBean.getApproverPositionId() && workflowBean.getApproverPositionId() != -1)
                pos = positionMasterService.getPositionById(workflowBean.getApproverPositionId());
            if (BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
                Assignment commissionerUsr = this.assignmentService.getPrimaryAssignmentForUser(user.getId());
                pos = commissionerUsr.getPosition();
            }
            if (null == license.getState()) {
                WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, workflowBean.getAdditionaRule(), workflowBean.getCurrentState(), null);
                license.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            } else if ("END".equalsIgnoreCase(license.getCurrentState().getNextAction())) {
                license.transition(true).end().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            } else {
                WorkFlowMatrix wfmatrix = this.licenseWorkflowService.getWfMatrix(license.getStateType(), null,
                        null, workflowBean.getAdditionaRule(), license.getCurrentState().getValue(), null);
                license.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            }

        }
    }

    protected Assignment getWorkflowInitiator(T license) {
        return this.assignmentService.getPrimaryAssignmentForUser(license.getCreatedBy().getId());
    }

    @Transactional
    public void processAndStoreDocument(List<LicenseDocument> documents, License license) {
        documents.forEach(document -> {
            document.setType(this.licenseDocumentTypeService.load(document.getType().getId(), LicenseDocumentType.class));
            if (!(document.getUploads().isEmpty() || document.getUploadsContentType().isEmpty())) {
                int fileCount = 0;
                for (File file : document.getUploads()) {
                    FileStoreMapper fileStore = this.fileStoreService.store(file,
                            document.getUploadsFileName().get(fileCount),
                            document.getUploadsContentType().get(fileCount++), "EGTL");
                    document.getFiles().add(fileStore);
                }
                document.setEnclosed(true);
            } else if (document.getType().isMandatory() && document.getFiles().isEmpty()) {
                document.getFiles().clear();
                throw new ValidationException("TL-004", "TL-004", document.getType().getName());
            }
            document.setDocDate(new Date());
            document.setLicense(license);
        });
    }
    
    public List<LicenseDocumentType> getDocumentTypesByApplicationType(ApplicationType applicationType) {
        return this.documentTypeService.getDocumentTypesByApplicationType(applicationType);
    }

    public List<NatureOfBusiness> getAllNatureOfBusinesses() {
        return natureOfBusinessService.findAll();
    }

    public T getLicenseByLicenseNumber(String licenseNumber) {
        return (T)this.licenseRepository.findByLicenseNumber(licenseNumber);
    }

    public T getLicenseByApplicationNumber(String applicationNumber) {
        return (T)this.licenseRepository.findByApplicationNumber(applicationNumber);
    }

    public List<Installment> getLastFiveYearInstallmentsForLicense() {
        List<Installment> installmentList = this.installmentDao.fetchInstallments(this.getModuleName(), new Date(), 6);
        Collections.reverse(installmentList);
        return installmentList;
    }

    public Map<String, Map<String, BigDecimal>> getOutstandingFee(T license) {
        Map<String, Map<String, BigDecimal>> outstandingFee = new HashMap<>();
        Installment currentInstallmentYear = this.installmentDao.getInsatllmentByModuleForGivenDate(this.getModuleName(), new Date());
        LicenseDemand licenseDemand = license.getCurrentDemand();
        for (EgDemandDetails demandDetail : licenseDemand.getEgDemandDetails()) {
            String demandReason = demandDetail.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster();
            Installment installmentYear = demandDetail.getEgDemandReason().getEgInstallmentMaster();
            Map<String, BigDecimal> feeByTypes;
            if (outstandingFee.containsKey(demandReason))
                feeByTypes = outstandingFee.get(demandReason);
            else {
                feeByTypes = new HashMap<>();
                feeByTypes.put(ARREAR, ZERO);
                feeByTypes.put("current", ZERO);
            }
            BigDecimal demandAmount = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());
            if (installmentYear.equals(currentInstallmentYear))
                feeByTypes.put("current", demandAmount);
            else
                feeByTypes.put(ARREAR, feeByTypes.get(ARREAR).add(demandAmount));
            outstandingFee.put(demandReason, feeByTypes);
        }

        return outstandingFee;

    }

    public List<T> getAllLicensesByNatureOfBusiness(String natureOfBusiness) {
        return this.entityQueryService.getSession().createCriteria(License.class)
                .createAlias("natureOfBusiness", "nb", JoinType.LEFT_OUTER_JOIN).add(Restrictions.eq("nb.name", natureOfBusiness))
                .setCacheMode(CacheMode.IGNORE).list();
    }

    @Transactional
    public void save(License license) {
        licenseRepository.save(license);
    }

}