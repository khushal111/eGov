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
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.infra.utils.DateUtils;
import org.egov.lcms.transactions.entity.Hearings;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.repository.HearingsRepository;
import org.egov.lcms.utils.LegalCaseUtil;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@Transactional(readOnly = true)
public class HearingsService {

    @Autowired
    private HearingsRepository hearingsRepository;

    @Autowired
    private LegalCaseUtil legalCaseUtil;

    @Transactional
    public Hearings persist(final Hearings hearings) {
        updateNextDate(hearings, hearings.getLegalCase());
        final EgwStatus statusObj = legalCaseUtil.getStatusForModuleAndCode(LcmsConstants.MODULE_TYPE_LEGALCASE,
                LcmsConstants.LEGALCASE_STATUS_IN_PROGRESS);
        hearings.setStatus(statusObj);
        hearings.getLegalCase().setStatus(statusObj);
        return hearingsRepository.save(hearings);
    }

    public List<Hearings> findAll() {
        return hearingsRepository.findAll(new Sort(Sort.Direction.ASC, ""));
    }

    public Hearings findById(final Long id) {
        return hearingsRepository.findOne(id);
    }

    public List<Hearings> findByLCNumber(final String lcNumber) {
        return hearingsRepository.findByLegalCase_lcNumber(lcNumber);
    }

    public void updateNextDate(final Hearings hearings, final LegalCase legalCase) {

        if (!DateUtils.compareDates(legalCase.getNextDate(), hearings.getHearingDate()))
            legalCase.setNextDate(hearings.getHearingDate());
        else {
            final List<Date> hearingDateList = new ArrayList<Date>(0);
            hearingDateList.add(hearings.getHearingDate());
            final Iterator<Hearings> iteratorHearings = legalCase.getHearings().iterator();
            while (iteratorHearings.hasNext()) {
                final Hearings hearingsObj = iteratorHearings.next();
                if (!hearingsObj.getId().equals(hearings.getId()))
                    hearingDateList.add(hearingsObj.getHearingDate());
            }

            legalCase.setNextDate(Collections.max(hearingDateList));
        }

    }

    public BindingResult validateDate(final Hearings hearings, final LegalCase legalCase, final BindingResult errors)
    {

        if (!DateUtils.compareDates(hearings.getHearingDate(), hearings.getLegalCase().getCaseDate()))
            errors.rejectValue("hearingDate", "ValidateDate.hearing.casedate");
        final List<Hearings> hearingsList = legalCase.getHearings();
        int count = 0;
        for (final Hearings hearings2 : hearingsList)
            if (DateUtils.compareDates(hearings2.getHearingDate(), new Date()))
                count++;
        if (count >= 1)
            errors.rejectValue("hearingDate", "ValidateDate.hearing.futuredate");
        return errors;
    }

}
