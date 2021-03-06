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
package org.egov.lcms.transactions.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.egov.lcms.masters.entity.AdvocateMaster;
import org.egov.lcms.masters.service.AdvocateMasterService;
import org.egov.lcms.transactions.entity.BipartisanDetails;
import org.egov.lcms.transactions.entity.CounterAffidavit;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.entity.LegalCaseAdvocate;
import org.egov.lcms.transactions.entity.LegalCaseDepartment;
import org.egov.lcms.transactions.entity.LegalCaseDocuments;
import org.egov.lcms.transactions.entity.Pwr;
import org.egov.lcms.transactions.entity.PwrDocuments;
import org.egov.lcms.transactions.repository.LegalCaseRepository;
import org.egov.lcms.transactions.repository.PwrDocumentsRepository;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LegalCaseService {

    private final LegalCaseRepository legalCaseRepository;

    @Autowired
    private PwrDocumentsRepository pwrDocumentsRepository;

    @Autowired
    private AdvocateMasterService advocateMasterService;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @Autowired
    public LegalCaseService(final LegalCaseRepository legalCaseRepository) {
        this.legalCaseRepository = legalCaseRepository;

    }

    public LegalCase findById(final Long Id) {
        return legalCaseRepository.findOne(Id);
    }

    public LegalCase findByLcNumber(final String lcnumber) {
        return legalCaseRepository.findByLcNumber(lcnumber);
    }

    public LegalCase getLegalCaseByCaseNumber(final String caseNumber) {
        return legalCaseRepository.findByCaseNumber(caseNumber);
    }

    @Transactional
    public LegalCase persist(LegalCase legalcase) {
        legalcase.setCaseNumber(
                legalcase.getCaseNumber() + (legalcase.getWpYear() != null ? "/" + legalcase.getWpYear() : ""));
        legalcase.setStatus(legalCaseUtil.getStatusForModuleAndCode(LcmsConstants.MODULE_TYPE_LEGALCASE,
                LcmsConstants.LEGALCASE_STATUS_CREATED));
        final List<LegalCaseDocuments> legalDoc = legalCaseUtil.getLegalCaseDocumentList(legalcase);
        legalcase = prepareChildEntities(legalcase);
        processAndStoreApplicationDocuments(legalcase, legalDoc);
        updateNextDate(legalcase, legalcase.getPwrList());
        return legalCaseRepository.save(legalcase);
    }

    @Transactional
    public LegalCase update(final LegalCase legalcase) {
        updateLegalCaseDeptAndPwr(legalcase, legalcase.getPwrList(), legalcase.getLegalCaseDepartment());
        processAndStorePwrDocuments(legalcase);
        return legalCaseRepository.save(legalcase);
    }

    @Transactional
    public void updateLegalCaseDeptAndPwr(final LegalCase legalcase, final List<Pwr> pwrList,
            final List<LegalCaseDepartment> legalDept) {
        final List<LegalCaseDepartment> legalcaseDetails = new ArrayList<LegalCaseDepartment>(0);
        final List<Pwr> pwrListtemp = new ArrayList<Pwr>(0);
        final List<CounterAffidavit> caListtemp = new ArrayList<CounterAffidavit>(0);
        for (final Pwr legalpwr : pwrList) {
            legalpwr.setLegalCase(legalcase);
            legalpwr.setCaFilingdate(new Date());
            pwrListtemp.add(legalpwr);
        }
        legalcase.getPwrList().clear();
        legalcase.setPwrList(pwrListtemp);
        for (final CounterAffidavit counterAffidavit : legalcase.getCounterAffidavits()) {
            counterAffidavit.setLegalCase(legalcase);
            caListtemp.add(counterAffidavit);
        }
        legalcase.getCounterAffidavits().clear();
        legalcase.setCounterAffidavits(caListtemp);
        for (final LegalCaseDepartment legaldeptObj : legalDept) {
            legaldeptObj.setLegalCase(legalcase);
            legaldeptObj.setPosition(legalCaseUtil.getPositionByName(legaldeptObj.getPosition().getName()));
            legaldeptObj.setDepartment(legalCaseUtil.getDepartmentByName(legaldeptObj.getDepartment().getName()));
            legalcaseDetails.add(legaldeptObj);
        }
        legalcase.getLegalCaseDepartment().clear();
        legalcase.setLegalCaseDepartment(legalcaseDetails);

    }

    public List<LegalCaseDocuments> getLegalCaseDocList(final LegalCase legalCase) {
        return legalCase.getLegalCaseDocuments();
    }

    public List<PwrDocuments> getPwrDocList(final LegalCase legalCase) {
        return legalCase.getPwrList().get(0).getPwrDocuments();
    }

    public LegalCase prepareChildEntities(final LegalCase legalcase) {
        final List<Pwr> pwrListtemp = new ArrayList<Pwr>();
        legalcase.getBipartisanDetails().clear();
        if (legalcase != null)
            for (final BipartisanDetails bipartObj : legalcase.getBipartisanPetitionerDetailsList())
                if (bipartObj.getName() != null && !"".equals(bipartObj.getName())) {
                    bipartObj.setSerialNumber(bipartObj.getSerialNumber() != null ? bipartObj.getSerialNumber() : 111l);
                    bipartObj.setIsRepondent(Boolean.FALSE);
                    if (bipartObj.getIsRespondentGovernment() == null)
                        bipartObj.setIsRespondentGovernment(Boolean.FALSE);
                    bipartObj.setLegalCase(legalcase);
                    legalcase.getBipartisanDetails().add(bipartObj);
                }
        for (final BipartisanDetails bipartObjtemp : legalcase.getBipartisanRespondentDetailsList())
            if ((bipartObjtemp.getId() == null || bipartObjtemp.getId() != null)
                    && bipartObjtemp.getName() != null && !"".equals(bipartObjtemp.getName())) {

                bipartObjtemp.setSerialNumber(
                        bipartObjtemp.getSerialNumber() != null ? bipartObjtemp.getSerialNumber() : 111l);
                bipartObjtemp.setLegalCase(legalcase);
                if (bipartObjtemp.getIsRespondentGovernment() == null)
                    bipartObjtemp.setIsRespondentGovernment(Boolean.FALSE);
                bipartObjtemp.setIsRepondent(Boolean.TRUE);
                legalcase.getBipartisanDetails().add(bipartObjtemp);
            }
        final Set<BipartisanDetails> uniqueSet = new LinkedHashSet<BipartisanDetails>(legalcase.getBipartisanDetails());
        legalcase.getBipartisanDetails().clear();
        legalcase.getBipartisanDetails().addAll(uniqueSet);
        if (!legalcase.getPwrList().isEmpty()) {
            for (final Pwr legalpwr : legalcase.getPwrList()) {
                legalpwr.setLegalCase(legalcase);
                // legalpwr.setCaFilingdate(new Date());
                pwrListtemp.add(legalpwr);
            }
            legalcase.getPwrList().clear();
            legalcase.setPwrList(pwrListtemp);
        }
        return legalcase;
    }

    @Transactional
    public LegalCase saveStandingCouncilEntity(final LegalCaseAdvocate legalCaseAdvocate) {
        LegalCaseAdvocate legalCaseAdvocatetemp = null;
        AdvocateMaster seniorLegalMaster = null;
        final AdvocateMaster advocateName = advocateMasterService
                .findByName(legalCaseAdvocate.getAdvocateMaster().getName());
        if (legalCaseAdvocate.getSeniorAdvocate().getName() != null)
            seniorLegalMaster = advocateMasterService
                    .findByName(legalCaseAdvocate.getSeniorAdvocate().getName());
        if (!legalCaseAdvocate.getLegalCase().getLegalCaseAdvocates().isEmpty()) {
            legalCaseAdvocatetemp = legalCaseAdvocate.getLegalCase().getLegalCaseAdvocates().get(0);
            legalCaseAdvocatetemp.setAdvocateMaster(advocateName);
            legalCaseAdvocatetemp.setAssignedtodate(legalCaseAdvocate.getAssignedtodate());
            legalCaseAdvocatetemp.setVakalatdate(legalCaseAdvocate.getVakalatdate());
            legalCaseAdvocatetemp.getLegalCase().setIsSenioradvrequired(legalCaseAdvocate.getIsSeniorAdvocate());
            legalCaseAdvocatetemp.setIsActive(Boolean.TRUE);
            legalCaseAdvocatetemp.setChangeAdvocate(legalCaseAdvocate.getChangeAdvocate());
            legalCaseAdvocatetemp.setChangeSeniorAdvocate(legalCaseAdvocate.getChangeSeniorAdvocate());
            legalCaseAdvocatetemp.setSeniorAdvocate(seniorLegalMaster);
            legalCaseAdvocatetemp.setAssignedtodateForsenior(legalCaseAdvocate.getAssignedtodateForsenior());
            legalCaseAdvocatetemp.setOrderdate(legalCaseAdvocate.getOrderdate());
            legalCaseAdvocatetemp.setOrdernumber(legalCaseAdvocate.getOrdernumber());
            legalCaseAdvocatetemp.setOrderdateJunior(legalCaseAdvocate.getOrderdateJunior());
            legalCaseAdvocatetemp.setOrdernumberJunior(legalCaseAdvocate.getOrdernumberJunior());
            legalCaseAdvocate.getLegalCase().getLegalCaseAdvocates().add(legalCaseAdvocatetemp);

        } else {
            legalCaseAdvocate.setAdvocateMaster(advocateName);
            legalCaseAdvocate.getLegalCase().setIsSenioradvrequired(legalCaseAdvocate.getIsSeniorAdvocate());
            legalCaseAdvocate.setSeniorAdvocate(seniorLegalMaster);
            legalCaseAdvocate.setIsActive(Boolean.TRUE);
            legalCaseAdvocate.getLegalCase().getLegalCaseAdvocates().add(legalCaseAdvocate);
        }
        return legalCaseRepository.save(legalCaseAdvocate.getLegalCase());

    }

    public void processAndStoreApplicationDocuments(final LegalCase legalcase,
            final List<LegalCaseDocuments> legalDoc) {
        if (legalcase.getId() == null) {
            if (!legalcase.getLegalCaseDocuments().isEmpty())
                for (final LegalCaseDocuments applicationDocument : legalcase.getLegalCaseDocuments()) {
                    applicationDocument.setLegalCase(legalcase);
                    applicationDocument.setDocumentName("LegalCase");
                    applicationDocument.setSupportDocs(legalCaseUtil.addToFileStore(applicationDocument.getFiles()));
                }
        } else {
            for (final LegalCaseDocuments applicationDocument : legalcase.getLegalCaseDocuments()) {
                applicationDocument.setLegalCase(legalcase);
                applicationDocument.setDocumentName("LegalCase");
                applicationDocument.getSupportDocs().addAll(legalCaseUtil.addToFileStore(applicationDocument.getFiles()));
                legalcase.getLegalCaseDocuments().clear();
                legalcase.getLegalCaseDocuments().add(applicationDocument);
            }
            legalcase.getLegalCaseDocuments().addAll(legalDoc);

        }
    }

    @Transactional
    public void processAndStorePwrDocuments(final LegalCase legalcase) {
        final List<PwrDocuments> pwrDocList = new ArrayList<PwrDocuments>();
        if (!legalcase.getPwrList().get(0).getPwrDocuments().isEmpty())
            for (final PwrDocuments pwr : legalcase.getPwrList().get(0).getPwrDocuments())
                if (pwr != null && pwr.getId() == null) {
                    pwr.setPwr(legalcase.getPwrList().get(0));
                    pwr.setDocumentName("Pwr");
                    pwr.setSupportDocs(legalCaseUtil.addToFileStore(pwr.getFiles()));
                    pwrDocList.add(pwr);
                    pwrDocumentsRepository.save(pwr);
                }
    }

    @Transactional
    public LegalCase save(final LegalCase legalcase) {
        return legalCaseRepository.save(legalcase);
    }

    public void updateNextDate(final LegalCase legalCase, final List<Pwr> pwr) {

        if (pwr.get(0).getCaFilingdate() != null)
            legalCase.setNextDate(pwr.get(0).getCaFilingdate());
        else if (pwr.get(0).getCaDueDate() != null)
            legalCase.setNextDate(pwr.get(0).getCaDueDate());
        else if (pwr.get(0).getPwrDueDate() != null)
            legalCase.setNextDate(pwr.get(0).getPwrDueDate());
        else
            legalCase.setNextDate(legalCase.getCaseDate());

    }
}
