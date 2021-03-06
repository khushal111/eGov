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
package org.egov.lcms.web.controller.transactions;

import java.util.List;

import javax.validation.Valid;

import org.egov.lcms.masters.service.JudgmentTypeService;
import org.egov.lcms.transactions.entity.Judgment;
import org.egov.lcms.transactions.entity.LegalCase;
import org.egov.lcms.transactions.service.JudgmentService;
import org.egov.lcms.transactions.service.LegalCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/judgment")
public class EditJudgmentController {

    @Autowired
    private JudgmentTypeService judgmentTypeService;

    @Autowired
    private LegalCaseService legalCaseService;

    @Autowired
    private JudgmentService judgmentService;

    @ModelAttribute
    private LegalCase getLegalCase(@RequestParam("lcNumber") final String lcNumber) {
        final LegalCase legalCase = legalCaseService.findByLcNumber(lcNumber);
        return legalCase;
    }

    private void prepareNewForm(final Model model) {
        model.addAttribute("judgmentTypes", judgmentTypeService.findAll());
    }

    @RequestMapping(value = "/edit/", method = RequestMethod.GET)
    public String edit(@RequestParam("lcNumber") final String lcNumber, final Model model) {
        final List<Judgment> judgementList = getLegalCase(lcNumber).getJudgment();
        final Judgment judgmentObj = judgementList.get(0);
        prepareNewForm(model);
        model.addAttribute("judgment", judgmentObj);
        model.addAttribute("judgmentDocList",
                judgmentService.getJudgmentDocList(judgmentObj));
        model.addAttribute("mode", "edit");
        return "judgment-edit";
    }

    @RequestMapping(value = "/edit/", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final Judgment judgment, @RequestParam("lcNumber") final String lcNumber,
            final BindingResult errors, final Model model, final RedirectAttributes redirectAttrs) {
        if (errors.hasErrors()) {
            prepareNewForm(model);
            return "judgment-edit";
        }
        judgmentService.persist(judgment);
        model.addAttribute("judgmentDocList", judgmentService.getJudgmentDocList(judgment));
        redirectAttrs.addFlashAttribute("judgment", judgment);
        model.addAttribute("message", "Judgment updated successfully.");
        model.addAttribute("mode", "edit");
        return "judgment-success";
    }

}
