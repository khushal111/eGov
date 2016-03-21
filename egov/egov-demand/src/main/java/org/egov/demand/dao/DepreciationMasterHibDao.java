/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/

package org.egov.demand.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.commons.Installment;
import org.egov.demand.model.DepreciationMaster;
import org.egov.infra.admin.master.entity.Module;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "depreciationMasterDAO")
@Transactional(readOnly = true)
public class DepreciationMasterHibDao implements DepreciationMasterDao {

	@PersistenceContext
	private EntityManager entityManager;

	private Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.egov.infstr.DCB.dao.DepreciationMasterDao#getDepreciationMaster(java
	 * .lang.Integer)
	 */
	@Override
	public DepreciationMaster getDepreciationMaster(Module mod, Integer year) {
		Query qry = getCurrentSession().createQuery(
				"from DepreciationMaster DM where DM.module=:mod and DM.year=:year ");// and
																						// DM.IsHistory='N'
																						// ");
		qry.setEntity("mod", mod);
		qry.setInteger("year", year);
		return (DepreciationMaster) qry.uniqueResult();
	}

	/**
	 * Added By Rajalakshmi D.N. on 07/05/2007 Description : Returns the
	 * Non-History Depreciation for the Given Module, for the given Year and for
	 * the given Installment
	 * 
	 * @param Module
	 *            ,Year and Installment
	 * @return DepreciationMaster record
	 */

	@Override
	public DepreciationMaster getNonHistDepMasterByModuleInsYr(Module mod, Integer year,
			Installment insYear) {
		Query qry = getCurrentSession()
				.createQuery(
						"from DepreciationMaster DM where DM.module=:mod and DM.year=:year and DM.IsHistory='N' and DM.startInstallment=:insYear ");
		qry.setEntity("mod", mod);
		qry.setInteger("year", year);
		qry.setEntity("insYear", insYear);
		return (DepreciationMaster) qry.uniqueResult();
	}

	@Override
	public List getDepreciationsForModule(Module mod) {
		// TODO Auto-generated method stub
		Query qry = getCurrentSession().createQuery(
				"from DepreciationMaster DM where DM.module=:module");
		qry.setEntity("module", mod);
		return qry.list();
	}

	@Override
	public List getDepreciationsForModulebyHistory(Module mod) {

		Query qry = getCurrentSession().createQuery(
				"from DepreciationMaster DM where DM.module=:module and DM.IsHistory='N'");
		qry.setEntity("module", mod);
		return qry.list();
	}

	@Override
	public Float getDepreciationPercent(Integer year) {
		Query qry = getCurrentSession().createQuery(
				"select DM.depreciationPct from DepreciationMaster DM where DM.year=:year");
		qry.setInteger("year", year);
		return (Float) qry.uniqueResult();
	}

	@Override
	public List getAllNonHistoryDepreciationRates() {
		Query qry = getCurrentSession().createQuery(
				"from DepreciationMaster DM where  and DM.IsHistory='N'");
		return qry.list();
	}

	@Override
	public Float getLeastDepreciationPercent(Integer year) {
		Float leastDeprePercent = null;
		if (year != null && year != 0) {
			Float deprePercent = getDepreciationPercent(year);
			if (deprePercent == null || deprePercent == 0) {
				Query qry = getCurrentSession().createQuery(
						"select min(DM.year) from DepreciationMaster DM ");
				Integer leastDepreYear = (Integer) qry.uniqueResult();
				leastDeprePercent = getDepreciationPercent(leastDepreYear);
			} else
				leastDeprePercent = deprePercent;

		}
		return leastDeprePercent;
	}

	@Override
	public DepreciationMaster findById(Integer id, boolean lock) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DepreciationMaster> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DepreciationMaster create(DepreciationMaster depreciationMaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(DepreciationMaster depreciationMaster) {
		// TODO Auto-generated method stub

	}

	@Override
	public DepreciationMaster update(DepreciationMaster depreciationMaster) {
		// TODO Auto-generated method stub
		return null;
	}

}