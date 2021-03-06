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
package org.egov.wtms.web.controller.reports;

import java.text.ParseException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.egov.wtms.application.entity.BaseRegisterResult;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BaseRegisterReportService {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public SQLQuery getBaseRegisterReportDetails(final String ward) throws ParseException {

        final StringBuilder queryStr = new StringBuilder();
        queryStr.append(
                "select dcbinfo.hscno as \"consumerNo\",dcbinfo.propertyid as \"assementNo\", dcbinfo.username as \"ownerName\",dcbinfo.categorytype as \"categoryType\",dcbinfo.username as \"period\",");
        queryStr.append(
                "dcbinfo.houseno as \"doorNo\", dcbinfo.connectiontype as \"connectionType\" , dcbinfo.arr_demand as \"arrears\" ,  dcbinfo.curr_demand as \"current\" ,  ");
        queryStr.append(
                "dcbinfo.arr_coll as \"arrearsCollection\" ,  dcbinfo.curr_coll as \"currentCollection\" , dcbinfo.arr_demand+dcbinfo.curr_demand as \"totalDemand\"  ");
        queryStr.append("from egwtr_mv_dcb_view dcbinfo");
        queryStr.append(
                " INNER JOIN eg_boundary wardboundary on dcbinfo.wardid = wardboundary.id INNER JOIN eg_boundary localboundary on dcbinfo.locality = localboundary.id");

        if (StringUtils.isNotBlank(ward))
            queryStr.append(" and wardboundary.id = :ward");
        final SQLQuery finalQuery = getCurrentSession().createSQLQuery(queryStr.toString());
        if (StringUtils.isNotBlank(ward))
            finalQuery.setLong("ward", Long.valueOf(ward));
        finalQuery.setResultTransformer(new AliasToBeanResultTransformer(BaseRegisterResult.class));
        return finalQuery;

    }

}
