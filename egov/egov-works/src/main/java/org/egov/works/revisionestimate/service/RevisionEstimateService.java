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
package org.egov.works.revisionestimate.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.service.UOMService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Position;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimate.EstimateStatus;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.entity.MeasurementSheet;
import org.egov.works.abstractestimate.repository.ActivityRepository;
import org.egov.works.letterofacceptance.service.WorkOrderActivityService;
import org.egov.works.master.service.ScheduleCategoryService;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate.RevisionEstimateStatus;
import org.egov.works.revisionestimate.entity.SearchRevisionEstimate;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.revisionestimate.repository.RevisionEstimateRepository;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrderActivity;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
@Transactional(readOnly = true)
public class RevisionEstimateService {

    private static final Logger LOG = LoggerFactory.getLogger(RevisionEstimateService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final RevisionEstimateRepository revisionEstimateRepository;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<RevisionAbstractEstimate> revisionAbstractEstimateWorkflowService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private UOMService uomService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private ScheduleCategoryService scheduleCategoryService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    WorkOrderActivityService workOrderActivityService;

    @Autowired
    MBHeaderService mbHeaderService;

    @Autowired
    public RevisionEstimateService(final RevisionEstimateRepository revisionEstimateRepository) {
        this.revisionEstimateRepository = revisionEstimateRepository;
    }

    public List<RevisionAbstractEstimate> findApprovedRevisionEstimatesByParent(final Long id) {
        return revisionEstimateRepository.findByParent_IdAndStatus(id,
                RevisionAbstractEstimate.RevisionEstimateStatus.APPROVED.toString());
    }

    public RevisionAbstractEstimate getRevisionEstimateById(final Long id) {
        return revisionEstimateRepository.findOne(id);
    }

    public List<User> getRevisionEstimateCreatedByUsers() {
        return revisionEstimateRepository.findRevisionEstimateCreatedByUsers();
    }

    public List<String> getRevisionEstimateByEstimateNumberLike(final String revisionEstimateNumber) {
        return revisionEstimateRepository.findDistinctEstimateNumberContainingIgnoreCase("%" + revisionEstimateNumber + "%");
    }

    @Transactional
    public RevisionAbstractEstimate createRevisionEstimate(final RevisionAbstractEstimate revisionEstimate,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        mergeSorAndNonSorActivities(revisionEstimate);
        final AbstractEstimate abstractEstimate = revisionEstimate.getParent();
        final List<RevisionAbstractEstimate> revisionEstimates = revisionEstimateRepository
                .findByParent_Id(abstractEstimate.getId());

        Integer reCount = revisionEstimates.size();
        if (revisionEstimate.getId() != null)
            reCount = reCount - 1;
        if (revisionEstimate.getId() == null) {
            revisionEstimate.setParent(abstractEstimate);
            revisionEstimate.setEstimateDate(new Date());
            revisionEstimate.setEstimateNumber(abstractEstimate.getEstimateNumber()
                    + "/RE".concat(Integer.toString(reCount)));
            revisionEstimate.setName("Revision Estimate for: " + abstractEstimate.getName());
            revisionEstimate.setDescription("Revision Estimate for: " + abstractEstimate.getDescription());
            revisionEstimate.setNatureOfWork(abstractEstimate.getNatureOfWork());
            revisionEstimate.setExecutingDepartment(abstractEstimate.getExecutingDepartment());
            revisionEstimate.setUserDepartment(abstractEstimate.getUserDepartment());
            revisionEstimate.setWard(abstractEstimate.getWard());
            revisionEstimate.setDepositCode(abstractEstimate.getDepositCode());
            revisionEstimate.setFundSource(abstractEstimate.getFundSource());
            revisionEstimate.setParentCategory(abstractEstimate.getParentCategory());
        }

        saveChangeQuantityActivities(revisionEstimate);

        revisionEstimateRepository.save(revisionEstimate);

        createRevisionEstimateWorkflowTransition(revisionEstimate, approvalPosition, approvalComent, additionalRule,
                workFlowAction);

        revisionEstimateRepository.save(revisionEstimate);
        return revisionEstimate;
    }

    private void saveChangeQuantityActivities(final RevisionAbstractEstimate revisionEstimate) {
        for (final Activity activity : revisionEstimate.getChangeQuantityActivities()) {
            final Activity act = activityRepository.findOne(activity.getParent().getId());
            activity.setParent(act);
            activity.setAbstractEstimate(revisionEstimate);
            activity.setSchedule(act.getSchedule());
            activity.setNonSor(act.getNonSor());
            activity.setUom(act.getUom());
            activity.setRate(act.getRate());
            if ("-".equals(activity.getSignValue()))
                activity.setRevisionType(RevisionType.REDUCED_QUANTITY);
            else
                activity.setRevisionType(RevisionType.ADDITIONAL_QUANTITY);
            activity.setEstimateRate(activity.getRate() * activity.getQuantity());
        }
        revisionEstimate.getActivities().addAll(revisionEstimate.getChangeQuantityActivities());
    }

    @Transactional
    public RevisionAbstractEstimate updateRevisionEstimate(final RevisionAbstractEstimate revisionEstimate,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction, final String removedCQIds)
            throws ValidationException, IOException {

        RevisionAbstractEstimate updateRevisionEstimate = null;

        if ((EstimateStatus.NEW.toString().equals(revisionEstimate.getEgwStatus().getCode())
                || EstimateStatus.REJECTED.toString().equals(revisionEstimate.getEgwStatus().getCode()))
                && !WorksConstants.CANCEL_ACTION.equals(workFlowAction)) {

            mergeSorAndNonSorActivities(revisionEstimate);
            mergeChangeQuantityActivities(revisionEstimate);

            List<Activity> activities = new ArrayList<Activity>(revisionEstimate.getActivities());
            activities = removeDeletedActivities(activities, removedCQIds);
            revisionEstimate.setActivities(activities);
        }

        updateRevisionEstimate = revisionEstimateRepository.save(revisionEstimate);

        revisionEstimateStatusChange(revisionEstimate, workFlowAction);

        createRevisionEstimateWorkflowTransition(revisionEstimate, approvalPosition, approvalComent, additionalRule,
                workFlowAction);

        revisionEstimateRepository.save(updateRevisionEstimate);

        return updateRevisionEstimate;
    }

    private List<Activity> removeDeletedActivities(final List<Activity> activities, final String removedActivityIds) {
        final List<Activity> activityList = new ArrayList<Activity>();
        if (null != removedActivityIds) {
            final String[] ids = removedActivityIds.split(",");
            final List<String> strList = new ArrayList<String>();
            for (final String str : ids)
                strList.add(str);
            for (final Activity activity : activities)
                if (activity.getId() != null) {
                    if (!strList.contains(activity.getId().toString()))
                        activityList.add(activity);
                } else
                    activityList.add(activity);
        } else
            return activities;
        return activityList;
    }

    public void createRevisionEstimateWorkflowTransition(final RevisionAbstractEstimate revisionEstimate,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;
        final String currState = "";
        WorkFlowMatrix wfmatrix = null;

        if (null != revisionEstimate.getId())
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(revisionEstimate.getCreatedBy().getId());
        if (WorksConstants.REJECT_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
            if (wfInitiator.equals(userAssignment))
                revisionEstimate.transition(true).end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withDateInfo(currentDate.toDate())
                        .withNatureOfTask(WorksConstants.WORKFLOWTYPE_DISPLAYNAME_REVISION_ESTIMATE);
            else
                revisionEstimate.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(WorksConstants.WF_STATE_REJECTED)
                        .withDateInfo(currentDate.toDate()).withOwner(wfInitiator.getPosition()).withNextAction("")
                        .withNatureOfTask(WorksConstants.WORKFLOWTYPE_DISPLAYNAME_REVISION_ESTIMATE);
        } else if (WorksConstants.SAVE_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
            wfmatrix = revisionAbstractEstimateWorkflowService.getWfMatrix(revisionEstimate.getStateType(), null, null,
                    additionalRule, WorksConstants.NEW, null);
            if (revisionEstimate.getState() == null)
                revisionEstimate.transition(true).start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(WorksConstants.NEW)
                        .withDateInfo(currentDate.toDate()).withOwner(wfInitiator.getPosition())
                        .withNextAction(WorksConstants.ESTIMATE_ONSAVE_NEXTACTION_VALUE)
                        .withNatureOfTask(WorksConstants.WORKFLOWTYPE_DISPLAYNAME_REVISION_ESTIMATE);
        } else {
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);
            if (null == revisionEstimate.getState()) {
                wfmatrix = revisionAbstractEstimateWorkflowService.getWfMatrix(revisionEstimate.getStateType(), null, null,
                        additionalRule, currState, null);
                revisionEstimate.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState()).withDateInfo(new Date())
                        .withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(WorksConstants.WORKFLOWTYPE_DISPLAYNAME_REVISION_ESTIMATE);
            } else if (WorksConstants.CANCEL_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
                final String stateValue = WorksConstants.WF_STATE_CANCELLED;
                wfmatrix = revisionAbstractEstimateWorkflowService.getWfMatrix(revisionEstimate.getStateType(), null, null,
                        additionalRule, revisionEstimate.getCurrentState().getValue(), null);
                revisionEstimate.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(pos).withNextAction("")
                        .withNatureOfTask(WorksConstants.WORKFLOWTYPE_DISPLAYNAME_REVISION_ESTIMATE);
            } else if (WorksConstants.APPROVE_ACTION.toString().equalsIgnoreCase(workFlowAction)) {
                wfmatrix = revisionAbstractEstimateWorkflowService.getWfMatrix(revisionEstimate.getStateType(), null, null,
                        additionalRule, revisionEstimate.getCurrentState().getValue(), null);
                revisionEstimate.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(WorksConstants.WORKFLOWTYPE_DISPLAYNAME_REVISION_ESTIMATE);
                revisionEstimate.transition(true).end().withSenderName(user.getName()).withComments(approvalComent)
                        .withDateInfo(currentDate.toDate());
            } else {
                wfmatrix = revisionAbstractEstimateWorkflowService.getWfMatrix(revisionEstimate.getStateType(), null, null,
                        additionalRule, revisionEstimate.getCurrentState().getValue(), null);
                revisionEstimate.transition(true).withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent).withStateValue(wfmatrix.getNextState())
                        .withDateInfo(currentDate.toDate()).withOwner(pos).withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(WorksConstants.WORKFLOWTYPE_DISPLAYNAME_REVISION_ESTIMATE);
            }
        }
    }

    private void mergeSorAndNonSorActivities(final RevisionAbstractEstimate revisionEstimate) {
        for (final Activity activity : revisionEstimate.getNonTenderedActivities())
            if (activity.getId() == null) {
                activity.setAbstractEstimate(revisionEstimate);
                revisionEstimate.addActivity(activity);
            } else
                for (final Activity oldActivity : revisionEstimate.getSORActivities())
                    if (oldActivity.getId().equals(activity.getId()))
                        updateActivity(oldActivity, activity);
        for (final Activity activity : revisionEstimate.getLumpSumActivities())
            if (activity.getId() == null) {
                activity.setAbstractEstimate(revisionEstimate);
                revisionEstimate.addActivity(activity);
            } else
                for (final Activity oldActivity : revisionEstimate.getNonSORActivities())
                    if (oldActivity.getId().equals(activity.getId()))
                        updateActivity(oldActivity, activity);
        if (LOG.isDebugEnabled())
            for (final Activity ac : revisionEstimate.getActivities())
                LOG.debug(ac.getMeasurementSheetList().size() + "    " + ac.getQuantity());

        for (final Activity ac : revisionEstimate.getNonTenderedActivities())
            for (final MeasurementSheet ms : ac.getMeasurementSheetList())
                if (ms.getActivity() == null)
                    ms.setActivity(ac);

        for (final Activity ac : revisionEstimate.getLumpSumActivities())
            for (final MeasurementSheet ms : ac.getMeasurementSheetList())
                if (ms.getActivity() == null)
                    ms.setActivity(ac);
    }

    private void mergeChangeQuantityActivities(final RevisionAbstractEstimate revisionEstimate) {
        for (final Activity activity : revisionEstimate.getChangeQuantityActivities())
            if (activity.getId() == null) {
                final Activity act = activityRepository.findOne(activity.getParent().getId());
                activity.setParent(act);
                activity.setAbstractEstimate(revisionEstimate);
                activity.setSchedule(act.getSchedule());
                activity.setNonSor(act.getNonSor());
                activity.setUom(act.getUom());
                activity.setRate(act.getRate());
                if ("-".equals(activity.getSignValue()))
                    activity.setRevisionType(RevisionType.REDUCED_QUANTITY);
                else
                    activity.setRevisionType(RevisionType.ADDITIONAL_QUANTITY);
                activity.setEstimateRate(activity.getRate() * activity.getQuantity());
                revisionEstimate.addActivity(activity);
            } else
                for (final Activity oldActivity : revisionEstimate.getActivities())
                    if (oldActivity.getId() != null && oldActivity.getId().equals(activity.getId()))
                        updateChangeQuantityActivity(oldActivity, activity);
        if (LOG.isDebugEnabled())
            for (final Activity ac : revisionEstimate.getActivities())
                LOG.debug(ac.getMeasurementSheetList().size() + "    " + ac.getQuantity());

        for (final Activity ac : revisionEstimate.getChangeQuantityActivities())
            for (final MeasurementSheet ms : ac.getMeasurementSheetList())
                if (ms.getActivity() == null)
                    ms.setActivity(ac);
    }

    private void updateChangeQuantityActivity(final Activity oldActivity, final Activity activity) {
        final Activity parent = activityRepository.findOne(activity.getParent().getId());
        oldActivity.setParent(parent);
        oldActivity.setSchedule(parent.getSchedule());
        oldActivity.setAmt(activity.getAmt());
        oldActivity.setNonSor(parent.getNonSor());
        oldActivity.setQuantity(activity.getQuantity());
        oldActivity.setRate(parent.getRate());
        oldActivity.setServiceTaxPerc(activity.getServiceTaxPerc());
        oldActivity.setEstimateRate(activity.getRate() * activity.getQuantity());
        oldActivity.setUom(parent.getUom());
        oldActivity.setMeasurementSheetList(mergeMeasurementSheet(oldActivity, activity));
        if ("+".equals(activity.getSignValue()))
            oldActivity.setRevisionType(RevisionType.ADDITIONAL_QUANTITY);
        else
            oldActivity.setRevisionType(RevisionType.REDUCED_QUANTITY);
    }

    private List<MeasurementSheet> mergeMeasurementSheet(final Activity oldActivity, final Activity activity) {
        final List<MeasurementSheet> newMsList = new LinkedList<MeasurementSheet>(oldActivity.getMeasurementSheetList());
        for (final MeasurementSheet msnew : activity.getMeasurementSheetList()) {
            if (msnew.getId() == null) {
                msnew.setActivity(oldActivity);
                oldActivity.getMeasurementSheetList().add(msnew);
                continue;
            }

            for (final MeasurementSheet msold : oldActivity.getMeasurementSheetList())
                if (msnew.getId().longValue() == msold.getId().longValue()) {
                    msold.setLength(msnew.getLength());
                    msold.setWidth(msnew.getWidth());
                    msold.setDepthOrHeight(msnew.getDepthOrHeight());
                    msold.setNo(msnew.getNo());
                    msold.setActivity(msnew.getActivity());
                    msold.setIdentifier(msnew.getIdentifier());
                    msold.setRemarks(msnew.getRemarks());
                    msold.setSlNo(msnew.getSlNo());
                    msold.setQuantity(msnew.getQuantity());
                    newMsList.add(msold);

                }

        }
        final List<MeasurementSheet> toRemove = new LinkedList<MeasurementSheet>();
        for (final MeasurementSheet msold : oldActivity.getMeasurementSheetList()) {
            Boolean found = false;
            if (LOG.isDebugEnabled()) {
                LOG.debug(oldActivity.getMeasurementSheetList().size() + "activity.getMeasurementSheetList()");
                LOG.debug(msold.getId() + "------msold.getId()");
            }
            if (msold.getId() == null)
                continue;

            for (final MeasurementSheet msnew : activity.getMeasurementSheetList())
                if (msnew.getId() == null) {
                    // found=true;
                } else if (msnew.getId().longValue() == msold.getId().longValue()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(msnew.getId() + "------msnew.getId()");
                        LOG.debug(msnew.getRemarks() + "------remarks");
                    }

                    found = true;
                }

            if (!found)
                toRemove.add(msold);

        }

        for (final MeasurementSheet msremove : toRemove) {
            if (LOG.isInfoEnabled())
                LOG.info("...........Removing rows....................Of MeasurementSheet" + msremove.getId());
            oldActivity.getMeasurementSheetList().remove(msremove);
        }

        return oldActivity.getMeasurementSheetList();

    }

    private void updateActivity(final Activity oldActivity, final Activity activity) {
        oldActivity.setSchedule(activity.getSchedule());
        oldActivity.setAmt(activity.getAmt());
        oldActivity.setNonSor(activity.getNonSor());
        oldActivity.setQuantity(activity.getQuantity());
        oldActivity.setRate(activity.getRate());
        oldActivity.setServiceTaxPerc(activity.getServiceTaxPerc());
        oldActivity.setEstimateRate(activity.getEstimateRate());
        oldActivity.setUom(activity.getUom());
        oldActivity.setMeasurementSheetList(mergeMeasurementSheet(oldActivity, activity));
    }

    public void populateHeaderActivities(final RevisionAbstractEstimate revisionEstimate,
            final List<RevisionAbstractEstimate> revisionAbstractEstimates, final Model model) {
        // Adding Original Activities
        final List<Activity> sorActivities = new ArrayList<>(revisionEstimate.getParent().getSORActivities());
        final List<Activity> nonSorActivities = new ArrayList<>(revisionEstimate.getParent().getNonSORActivities());

        final List<Activity> nonTenderedActivities = new ArrayList<>();
        final List<Activity> lumpSumActivities = new ArrayList<>();

        final Map<Long, String> previousEstimates = new HashMap<>();

        // Populating Revision Estimate Activities
        for (final RevisionAbstractEstimate re : revisionAbstractEstimates)
            if (revisionEstimate == null || revisionEstimate.getCreatedDate() == null || revisionEstimate != null
                    && revisionEstimate.getCreatedDate() != null
                    && re.getCreatedDate().before(revisionEstimate.getCreatedDate())) {
                for (final Activity activity : re.getActivities())
                    if (activity.getParent() == null && activity.getSchedule() != null)
                        populateNonTenderedLumpSumActivities(activity, nonTenderedActivities);
                    else if (activity.getParent() == null && activity.getSchedule() == null)
                        populateNonTenderedLumpSumActivities(activity, lumpSumActivities);
                    else if (activity.getParent() != null && activity.getSchedule() != null)
                        populateChangeQuantityActivities(activity, sorActivities, nonTenderedActivities);
                    else if (activity.getParent() != null && activity.getSchedule() == null)
                        populateChangeQuantityActivities(activity, nonSorActivities, lumpSumActivities);
                previousEstimates.put(re.getId(), re.getEstimateNumber());
            }

        revisionEstimate.getSorActivities().addAll(sorActivities);
        revisionEstimate.getNonSorActivities().addAll(nonSorActivities);
        revisionEstimate.getChangeQuantityNTActivities().addAll(nonTenderedActivities);
        revisionEstimate.getChangeQuantityLSActivities().addAll(lumpSumActivities);

        model.addAttribute("previousEstimates", previousEstimates);
    }

    private void populateNonTenderedLumpSumActivities(final Activity activity, final List<Activity> activities) {
        if (activities.isEmpty())
            activities.add(activity);
        else {
            boolean flag = false;
            for (final Activity act : activities)
                if (activity.getSchedule() != null && activity.getSchedule().getId().equals(act.getSchedule().getId())) {
                    flag = true;
                    act.setQuantity(act.getQuantity() + activity.getQuantity());
                } else if (activity.getSchedule() == null && activity.getNonSor().getId().equals(act.getNonSor().getId())) {
                    flag = true;
                    act.setQuantity(act.getQuantity() + activity.getQuantity());
                }
            if (!flag)
                activities.add(activity);
        }
    }

    private void populateChangeQuantityActivities(final Activity activity, final List<Activity> activities,
            final List<Activity> nonTenderedLumpSumActivities) {
        for (final Activity sa : activities)
            if (activity.getParent().getId().equals(sa.getId()))
                if (activity.getRevisionType() != null
                        && activity.getRevisionType().equals(RevisionType.ADDITIONAL_QUANTITY)) {
                    sa.setQuantity(sa.getQuantity() + activity.getQuantity());
                    sa.setQuantityChanged(true);
                } else if (activity.getRevisionType() != null
                        && activity.getRevisionType().equals(RevisionType.REDUCED_QUANTITY)) {
                    sa.setQuantity(sa.getQuantity() - activity.getQuantity());
                    sa.setQuantityChanged(true);
                }
        for (final Activity sa : nonTenderedLumpSumActivities)
            if (activity.getParent().getId().equals(sa.getId()))
                if (activity.getRevisionType() != null
                        && activity.getRevisionType().equals(RevisionType.ADDITIONAL_QUANTITY)) {
                    sa.setQuantity(sa.getQuantity() + activity.getQuantity());
                    sa.setQuantityChanged(true);
                } else if (activity.getRevisionType() != null
                        && activity.getRevisionType().equals(RevisionType.REDUCED_QUANTITY)) {
                    sa.setQuantity(sa.getQuantity() - activity.getQuantity());
                    sa.setQuantityChanged(true);
                }
    }

    public void loadViewData(final RevisionAbstractEstimate revisionEstimate, final WorkOrderEstimate workOrderEstimate,
            final Model model) {
        final List<AppConfigValues> values = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_SHOW_SERVICE_FIELDS);
        final AppConfigValues value = values.get(0);
        if (value.getValue().equalsIgnoreCase("Yes"))
            model.addAttribute("isServiceVATRequired", true);
        else
            model.addAttribute("isServiceVATRequired", false);
        model.addAttribute("uoms", uomService.findAll());
        final List<RevisionAbstractEstimate> revisionAbstractEstimates = findApprovedRevisionEstimatesByParent(
                workOrderEstimate.getEstimate().getId());
        populateHeaderActivities(revisionEstimate, revisionAbstractEstimates, model);
        model.addAttribute("revisionEstimate", revisionEstimate);
        model.addAttribute("exceptionaluoms", worksUtils.getExceptionalUOMS());
        model.addAttribute("workOrderDate",
                DateUtils.getDefaultFormattedDate(workOrderEstimate.getWorkOrder().getWorkOrderDate()));
        model.addAttribute("workOrderEstimate", workOrderEstimate);
        model.addAttribute("scheduleCategories", scheduleCategoryService.getAllScheduleCategories());
        model.addAttribute("workOrder", workOrderEstimate.getWorkOrder());
        model.addAttribute("stateType", revisionEstimate.getClass().getSimpleName());

    }

    public void revisionEstimateStatusChange(final RevisionAbstractEstimate revisionEstimate, final String workFlowAction) {
        if (null != revisionEstimate && null != revisionEstimate.getEgwStatus()
                && null != revisionEstimate.getEgwStatus().getCode())
            if (WorksConstants.SAVE_ACTION.equals(workFlowAction))
                revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.REVISIONABSTRACTESTIMATE,
                        RevisionEstimateStatus.NEW.toString()));
            else if (WorksConstants.CANCEL_ACTION.equals(workFlowAction)
                    && RevisionEstimateStatus.NEW.toString().equals(revisionEstimate.getEgwStatus().getCode()))
                revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.REVISIONABSTRACTESTIMATE,
                        RevisionEstimateStatus.CANCELLED.toString()));
            else if (RevisionEstimateStatus.NEW.toString().equals(revisionEstimate.getEgwStatus().getCode()))
                revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.REVISIONABSTRACTESTIMATE,
                        RevisionEstimateStatus.CREATED.toString()));
            else if (WorksConstants.APPROVE_ACTION.equals(workFlowAction))
                revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.REVISIONABSTRACTESTIMATE,
                        RevisionEstimateStatus.APPROVED.toString()));
            else if (WorksConstants.REJECT_ACTION.equals(workFlowAction))
                revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.REVISIONABSTRACTESTIMATE,
                        RevisionEstimateStatus.REJECTED.toString()));
            else if (RevisionEstimateStatus.REJECTED.toString().equals(revisionEstimate.getEgwStatus().getCode())
                    && WorksConstants.CANCEL_ACTION.equals(workFlowAction))
                revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.REVISIONABSTRACTESTIMATE,
                        RevisionEstimateStatus.CANCELLED.toString()));
            else if (RevisionEstimateStatus.REJECTED.toString().equals(revisionEstimate.getEgwStatus().getCode())
                    && WorksConstants.FORWARD_ACTION.equals(workFlowAction))
                revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(WorksConstants.REVISIONABSTRACTESTIMATE,
                        RevisionEstimateStatus.RESUBMITTED.toString()));
    }

    public List<SearchRevisionEstimate> searchRevisionEstimates(final SearchRevisionEstimate searchRevisionEstimate) {
        Query query = null;
        query = entityManager.unwrap(Session.class)
                .createSQLQuery(getQueryForSearchRevisionEstimates(searchRevisionEstimate))
                .addScalar("id", LongType.INSTANCE)
                .addScalar("aeId", LongType.INSTANCE)
                .addScalar("woId", LongType.INSTANCE)
                .addScalar("revisionEstimateNumber")
                .addScalar("reDate", DateType.INSTANCE)
                .addScalar("loaNumber", StringType.INSTANCE)
                .addScalar("contractorName", StringType.INSTANCE)
                .addScalar("estimateNumber", StringType.INSTANCE)
                .addScalar("reValue", BigDecimalType.INSTANCE)
                .addScalar("revisionEstimateStatus", StringType.INSTANCE)
                .addScalar("currentOwner", StringType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(SearchRevisionEstimate.class));
        query = setParameterForSearchRevisionEstimates(searchRevisionEstimate, query);
        return query.list();
    }

    private String getQueryForSearchRevisionEstimates(final SearchRevisionEstimate searchRevisionEstimate) {
        final StringBuilder filterConditions = new StringBuilder();

        if (searchRevisionEstimate != null) {

            if (searchRevisionEstimate.getRevisionEstimateNumber() != null)
                filterConditions.append(" AND aec.estimateNumber =:estimateNumber ");

            if (searchRevisionEstimate.getFromDate() != null)
                filterConditions.append(" AND aec.estimateDate >=:fromDate ");

            if (searchRevisionEstimate.getToDate() != null)
                filterConditions.append(" AND aec.estimateDate <=:toDate ");

            if (searchRevisionEstimate.getLoaNumber() != null)
                filterConditions.append(" AND wo.workOrder_Number like =:loaNumber ");

            if (searchRevisionEstimate.getCreatedBy() != null)
                filterConditions.append(" AND aec.createdBy =:createdBy ");

            if (searchRevisionEstimate.getStatus() != null)
                filterConditions.append(" AND aec.status =:status ");

        }
        final StringBuilder query = new StringBuilder();
        query.append(" SELECT distinct re.id                     AS id, ");
        query.append(" aep.id                           AS aeId ,  ");
        query.append(" wo.id                            AS woId ,  ");
        query.append(" aec.estimateNumber               AS revisionEstimateNumber ,  ");
        query.append(" aec.estimateDate                 AS reDate ,  ");
        query.append(" wo.workOrder_Number              AS loaNumber ,  ");
        query.append(" contractor.name                  AS contractorName,  ");
        query.append(" aep.estimateNumber               AS estimateNumber,  ");
        query.append(" aec.estimateValue                AS reValue,  ");
        query.append(" status.description               AS revisionEstimateStatus, ");
        query.append(" u.username                       AS currentOwner  ");
        query.append(" FROM egw_revision_estimate re,egw_abstractestimate aec,egw_abstractestimate aep,");
        query.append(" egw_workorder wo,egw_workorder_estimate woe,egw_contractor contractor,egw_status status,eg_user u ");
        query.append(" WHERE aec.parent = aep.id ");
        query.append(" AND aec.id = re.id ");
        query.append(" AND aep.id = woe.abstractestimate_id ");
        query.append(" AND woe.workorder_id = wo.id ");
        query.append(" AND wo.contractor_id = contractor.id ");
        query.append(" AND aec.createdby = u.id ");
        query.append(" AND aec.status = status.id ");
        query.append(filterConditions.toString());
        return query.toString();
    }

    private Query setParameterForSearchRevisionEstimates(final SearchRevisionEstimate searchRevisionEstimate, final Query query) {

        if (searchRevisionEstimate != null) {

            if (searchRevisionEstimate.getRevisionEstimateNumber() != null)
                query.setString("estimateNumber", searchRevisionEstimate.getRevisionEstimateNumber());

            if (searchRevisionEstimate.getFromDate() != null)
                query.setDate("fromDate", searchRevisionEstimate.getFromDate());

            if (searchRevisionEstimate.getToDate() != null)
                query.setDate("toDate", searchRevisionEstimate.getToDate());

            if (searchRevisionEstimate.getLoaNumber() != null)
                query.setString("loaNumber", searchRevisionEstimate.getLoaNumber());

            if (searchRevisionEstimate.getCreatedBy() != null)
                query.setLong("createdBy", searchRevisionEstimate.getCreatedBy());

            if (searchRevisionEstimate.getStatus() != null)
                query.setLong("status", searchRevisionEstimate.getStatus());

        }

        return query;
    }

    public List<Activity> searchActivities(final Long estimateId, final String sorType) {
        final StringBuilder queryStr = new StringBuilder(500);
        queryStr.append(
                "select act from Activity act where (act.abstractEstimate.egwStatus.code = :aeStatus or act.abstractEstimate.egwStatus.code = :reStatus) ");
        if (estimateId != null)
            queryStr.append(
                    " and (act.abstractEstimate.id = :estimateId or act.abstractEstimate.parent.id = :estimateId )");
        if (sorType != null && "SOR".equalsIgnoreCase(sorType))
            queryStr.append(" and act.schedule != null");
        if (sorType != null && "Non Sor".equalsIgnoreCase(sorType))
            queryStr.append(" and act.schedule = null");

        queryStr.append(" order by act.id");

        Query query = entityManager.unwrap(Session.class)
                .createQuery(queryStr.toString());

        query = setParameterForSearchActivities(estimateId, query);
        return query.list();
    }

    private Query setParameterForSearchActivities(final Long estimateId, final Query query) {
        if (estimateId != null)
            query.setLong("estimateId", estimateId);
        query.setString("aeStatus", EstimateStatus.ADMIN_SANCTIONED.toString());
        query.setString("reStatus", RevisionEstimateStatus.APPROVED.toString());
        return query;
    }

    public void validateChangeQuantityActivities(final RevisionAbstractEstimate revisionEstimate,
            final BindingResult bindErrors) {
        for (final Activity activity : revisionEstimate.getChangeQuantityActivities()) {
            if (activity.getQuantity() <= 0)
                bindErrors.reject("error.quantity.zero", "error.quantity.zero");
            if (activity.getRate() <= 0)
                bindErrors.reject("error.rates.zero", "error.rates.zero");

            if ("-".equals(activity.getSignValue())) {
                final WorkOrderActivity workOrderActivity = workOrderActivityService
                        .getWorkOrderActivityByActivity(activity.getParent().getId());
                if (workOrderActivity != null) {
                    final Double consumedQuantity = mbHeaderService.getPreviousCumulativeQuantity(-1L, workOrderActivity.getId());
                    if ("-".equals(activity.getSignValue())
                            && workOrderActivity.getApprovedQuantity() - consumedQuantity - activity.getQuantity() < 0)
                        bindErrors.reject("error.change.quantity", "error.change.quantity");
                }
            }
        }
    }
}