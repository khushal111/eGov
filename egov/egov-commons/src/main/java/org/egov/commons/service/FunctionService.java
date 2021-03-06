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

package org.egov.commons.service;

import org.egov.commons.CFunction;
import org.egov.commons.repository.FunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FunctionService {

	private final FunctionRepository functionRepository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public FunctionService(final FunctionRepository functionRepository) {
		this.functionRepository = functionRepository;
	}

	@Transactional
	public CFunction create(final CFunction function) {
		return functionRepository.save(function);
	}

	@Transactional
	public CFunction update(final CFunction function) {
		return functionRepository.save(function);
	}

	public List<CFunction> findAll() {
		return functionRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	public CFunction findByName(String name) {
		return functionRepository.findByName(name);
	}

	public CFunction findByCode(String code) {
		return functionRepository.findByCode(code);
	}

	public CFunction findOne(Long id) {
		return functionRepository.findOne(id);
	}
	public List<CFunction> findAllIsNotLeafTrue() {
		return functionRepository.findByIsNotLeaf(true);
	}
	
	public List<CFunction> search(CFunction function){
		
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<CFunction> createQuery = cb.createQuery(CFunction.class);
		Root<CFunction> functions = createQuery.from(CFunction.class);
		createQuery.select(functions);
		Metamodel m = entityManager.getMetamodel();
		javax.persistence.metamodel.EntityType<CFunction> CFunction_ = m.entity(CFunction.class);
    
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(function.getName()!=null)
		{
		String name="%"+function.getName().toLowerCase()+"%";
		predicates.add(cb.isNotNull(functions.get("name")));
		predicates.add(cb.like(cb.lower(functions.get(CFunction_.getDeclaredSingularAttribute("name", String.class))),name));
		}
		if(function.getCode()!=null)
		{
		String code="%"+function.getCode().toLowerCase()+"%";
		predicates.add(cb.isNotNull(functions.get("code")));
		predicates.add(cb.like(cb.lower(functions.get(CFunction_.getDeclaredSingularAttribute("code", String.class))),code));
		}
		if(function.getIsActive()==true)
		{
			predicates.add(cb.equal(functions.get(CFunction_.getDeclaredSingularAttribute("isActive", Boolean.class)),true));
		}
		if(function.getParentId()!=null)
		{
			/*predicates.add(cb.isNotNull(functions.get("id")));
			predicates.add(cb.equal(functions.get(CFunction_.getDeclaredSingularAttribute("id", Long.class)),function.getParentId().getId()));*/
			predicates.add(cb.equal(functions.get("parentId"), function.getParentId()));
		}
		
		
		
		createQuery.where(predicates.toArray(new Predicate[]{}));
		TypedQuery<CFunction> query=entityManager.createQuery(createQuery);

		List<CFunction> resultList = query.getResultList();
		return resultList;
	}
	
	
	
}