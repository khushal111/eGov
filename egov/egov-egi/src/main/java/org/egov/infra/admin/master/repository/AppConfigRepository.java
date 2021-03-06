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

package org.egov.infra.admin.master.repository;

import org.egov.infra.admin.master.entity.AppConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.egov.infra.admin.master.entity.AppConfig.FETCH_WITH_VALUES;

@Repository
public interface AppConfigRepository extends JpaRepository<AppConfig, Long> {

    @EntityGraph(value = FETCH_WITH_VALUES, type = EntityGraph.EntityGraphType.FETCH)
    AppConfig findByModule_NameAndKeyName(String moduleName, String keyName);

    AppConfig findById(Long id);

    @EntityGraph(value = FETCH_WITH_VALUES, type = EntityGraph.EntityGraphType.FETCH)
    AppConfig findByKeyName(final String keyName);

    @EntityGraph(value = FETCH_WITH_VALUES, type = EntityGraph.EntityGraphType.LOAD)
    List<AppConfig> findByModule_Name(String moduleName);

    @EntityGraph(value = FETCH_WITH_VALUES, type = EntityGraph.EntityGraphType.FETCH)
    Page<AppConfig> findByModule_Name(String moduleName, Pageable pageable);
}
