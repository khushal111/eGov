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
package org.egov.infra.web.controller.admin.masters.appConfig;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.admin.master.entity.AppConfig;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/appConfig/update")
public class SearchAppConfigController {

    private AppConfigService appConfigValueService;
   
    @Autowired
    public SearchAppConfigController(AppConfigService appConfigValueService) {
        this.appConfigValueService = appConfigValueService;
    }

    @ModelAttribute
    public AppConfig appConfigModel() {
        return new AppConfig();
    }

    @ModelAttribute(value = "appConfigList")
    public List<AppConfig> listAppConfig() {
        return appConfigValueService.findAll();
    }
    @ModelAttribute(value = "modulesList")
    public List<Module> findAllModules() {
        return appConfigValueService.findAllModules();
    } 
    
    @RequestMapping(method = RequestMethod.GET)
    public String showAppConfigValues(Model model) {

        return "appConfig-list";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String search(@ModelAttribute AppConfig appConfig, final BindingResult errors,
            RedirectAttributes redirectAttrs, HttpServletRequest request) {

        if (errors.hasErrors())
            return "appConfig-list";
    
        return "redirect:/appConfig/update/" + appConfig.getKeyName()+","+appConfig.getModule().getName() ;
    }

}