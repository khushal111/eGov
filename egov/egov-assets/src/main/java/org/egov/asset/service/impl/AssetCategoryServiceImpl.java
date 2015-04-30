/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.asset.service.impl;

import org.apache.log4j.Logger;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryNumberGenrator;
import org.egov.asset.service.AppService;
import org.egov.asset.service.AssetCategoryService;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.services.PersistenceService;

/**
 * This class will expose all asset category related operations.
 */
public class AssetCategoryServiceImpl extends BaseServiceImpl<AssetCategory, Long> implements AssetCategoryService {

    private static final Logger logger = Logger.getLogger(AssetCategoryServiceImpl.class);
    private AppService appService;
    private AssetCategoryNumberGenrator assetCategoryNumberGenrator;

    public AssetCategoryServiceImpl(final PersistenceService<AssetCategory, Long> persistenceService) {
        super(persistenceService);
    }

    @Override
    public void setAssetCategoryNumber(final AssetCategory entity) {
        final CFinancialYear financialYear = new CFinancialYear();
        if (entity != null && (entity.getId() == null || entity.getCode() == null || "".equals(entity.getCode()))
                && isAssetCategoryCodeAutoGenerated()) {
            if(logger.isDebugEnabled())
                logger.debug("---Auto generating Asset Category code....");
            entity.setCode(assetCategoryNumberGenrator.getAssetCategoryNumber(entity, financialYear));
        }
    }

    @Override
    public boolean isAssetCategoryCodeAutoGenerated() {
        final String isAutoGenerated = appService.getUniqueAppConfigValue("IS_ASSET_CATEGORYCODE_AUTOGENERATED");
        if ("yes".equalsIgnoreCase(isAutoGenerated))
            return true;
        return false;
    }

    public void setAppService(final AppService appService) {
        this.appService = appService;
    }

    public void setAssetCategoryNumberGenrator(final AssetCategoryNumberGenrator assetCategoryNumberGenrator) {
        this.assetCategoryNumberGenrator = assetCategoryNumberGenrator;
    }

}
