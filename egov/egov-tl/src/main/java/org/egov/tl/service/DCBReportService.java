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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DCBReportService {
    @PersistenceContext
    private EntityManager entityManager;

    public SQLQuery prepareQuery(final String licensenumber, final String mode,
            final String reportType) {
        StringBuilder query = new StringBuilder();
        final StringBuilder selectQry1 = new StringBuilder();
        final StringBuilder selectQry2 = new StringBuilder();
        StringBuilder fromQry = new StringBuilder();
        StringBuilder whereQry = new StringBuilder();
        final StringBuilder groupByQry = new StringBuilder();
        selectQry2
                .append("  cast(SUM(arr_demand) as bigint) AS arr_demand,cast(SUM(curr_demand) as bigint) AS curr_demand,cast(SUM(arr_coll) as bigint) AS arr_coll,cast(SUM(curr_coll) as bigint) AS curr_coll,"
                        + "cast(SUM(arr_balance) as bigint) AS arr_balance,cast(SUM(curr_balance) as bigint) AS curr_balance ");
        fromQry = new StringBuilder(" from egtl_mv_dcb_view dcbinfo,eg_boundary boundary ");

        if (mode.equalsIgnoreCase("license")) {
            selectQry1
                    .append("select distinct dcbinfo.licenseNumber as licenseNumber ,cast(dcbinfo.licenseId as integer) as licenseid,dcbinfo.username as \"username\", ");
            fromQry = new StringBuilder(" from egtl_mv_dcb_view dcbinfo ");
            if (licensenumber != null && !"".equals(licensenumber))
                whereQry = whereQry.append(" where  dcbinfo.licenseNumber = '" + licensenumber.toUpperCase() + "'");
            groupByQry.append("group by dcbinfo.licenseNumber,dcbinfo.licenseId,dcbinfo.username ");
            if (licensenumber != null && !"".equals(licensenumber))
                whereQry.append(" and ");
            else
                whereQry.append(" where ");
            whereQry.append(" dcbinfo.licenseNumber is not null  ");
        }

        query = selectQry1.append(selectQry2).append(fromQry).append(whereQry).append(groupByQry);
        final SQLQuery finalQuery = entityManager.unwrap(Session.class).createSQLQuery(query.toString());
        // finalQuery.setResultTransformer(new AliasToBeanResultTransformer(DCBReportResult.class));
        return finalQuery;
    }
}
