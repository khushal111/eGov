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

import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository(value = "egBillDAO")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EgBillHibernateDao implements EgBillDao {

	@PersistenceContext
	private EntityManager entityManager;

	private Session getCurrentSession() {
		return entityManager.unwrap(Session.class);
	}

	@Override
	public EgBillType getBillTypeByCode(String code) {
		final Query qry = this.getCurrentSession().createQuery(
				"from EgBillType bt where bt.code =:BTCODE");
		qry.setString("BTCODE", code);
		qry.setMaxResults(1);
		return (EgBillType) qry.uniqueResult();
	}

	@Override
	public EgBill findById(Long id, boolean lock) {
		return (EgBill) getCurrentSession().load(EgBill.class, id);
	}

	@Override
	public List<EgBill> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EgBill create(EgBill egBill) {
	    getCurrentSession().saveOrUpdate(egBill);
	    getCurrentSession().flush();
	    return egBill;
	}

	@Override
	public void delete(EgBill egBill) {
		// TODO Auto-generated method stub

	}

	@Override
	public EgBill update(EgBill egBill) {
		// TODO Auto-generated method stub
		return null;
	}
}