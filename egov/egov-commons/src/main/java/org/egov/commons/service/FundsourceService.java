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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.commons.repository.FundsourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FundsourceService {

    private final FundsourceRepository fundsourceRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public FundsourceService(final FundsourceRepository fundsourceRepository) {
        this.fundsourceRepository = fundsourceRepository;
    }

    @Transactional
    public Fundsource create(final Fundsource fundsource) {
        return fundsourceRepository.save(fundsource);
    }

    @Transactional
    public Fundsource update(final Fundsource fundsource) {
        return fundsourceRepository.save(fundsource);
    }

    public List<Fundsource> findAll() {
        return fundsourceRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public Fundsource findByName(String name) {
        return fundsourceRepository.findByName(name);
    }

    public Fundsource findByCode(String code) {
        return fundsourceRepository.findByCode(code);
    }

    public Fundsource findOne(final Long id) {
        return fundsourceRepository.findOne(id);
    }

    public List<Fundsource> search(final Fundsource fundsource) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Fundsource> createQuery = cb.createQuery(Fundsource.class);
        Root<Fundsource> fundsources = createQuery.from(Fundsource.class);
        createQuery.select(fundsources);
        Metamodel m = entityManager.getMetamodel();
        EntityType<Fundsource> Fundsource_ = m.entity(Fundsource.class);

        List<Predicate> predicates = new ArrayList<Predicate>();
        if (fundsource.getName() != null) {
            String name = "%" + fundsource.getName().toLowerCase() + "%";
            predicates.add(cb.isNotNull(fundsources.get("name")));
            predicates.add(cb.like(
                    cb.lower(fundsources.get(Fundsource_.getDeclaredSingularAttribute("name", String.class))), name));
        }
        if (fundsource.getCode() != null) {
            String code = "%" + fundsource.getCode().toLowerCase() + "%";
            predicates.add(cb.isNotNull(fundsources.get("code")));
            predicates.add(cb.like(
                    cb.lower(fundsources.get(Fundsource_.getDeclaredSingularAttribute("code", String.class))), code));
        }
        createQuery.where(predicates.toArray(new Predicate[] {}));
        TypedQuery<Fundsource> query = entityManager.createQuery(createQuery);
        List<Fundsource> resultList = query.getResultList();
        return resultList;
    }
}