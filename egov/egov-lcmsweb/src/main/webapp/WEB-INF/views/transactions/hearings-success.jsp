<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ include file="/includes/taglibs.jsp"%>
<form:form method="post" action=""
	class="form-horizontal form-groups-bordered" modelAttribute="hearings"
	id="hearingform">
	<input type="hidden" id="lcNumber" name="lcNumber"
		value="${hearings.legalCase.lcNumber}" />
	<div class="row">
		<div class="col-md-12">
			<c:if test="${not empty message}">
				<div role="alert">${message}</div>
			</c:if>
			<div class="panel panel-primary" data-collapsed="0">
				<div class="panel-heading">
					<div class="panel-title">Hearing Details</div>
				</div>
				<div class="panel-body ">
					<div class="row add-border">
						<div class="col-md-3 col-xs-6 add-margin">
							<spring:message code="lbl.hearingdate" />
						</div>
						<div class="col-md-3 col-xs-6 add-margin view-content">
							<fmt:formatDate value="${hearings.hearingDate}" var="HD"
								pattern="dd/MM/yyyy" />
							<c:out value="${HD}" />
						</div>
						
					</div>
					<div class="row add-border">
						<div class="col-xs-3 add-margin">
							<spring:message code="lbl.purposeofhearing" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${hearings.purposeofHearings}</div>
							<div class="col-xs-3 add-margin">
							<spring:message
								code="lbl.outcomeofhearing" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${hearings.hearingOutcome}</div>
					   </div>
					   <div class="row add-border">
						<div class="col-xs-3 add-margin">
						<spring:message
								code="lbl.additionallawyer" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${hearings.additionalLawyers}</div>
							<div class="col-xs-3 add-margin">
							<spring:message
								code="lbl.standingcounsel" />
						</div>
						<div class="col-sm-3 add-margin view-content">
							${hearings.isStandingCounselPresent}</div>
					   </div>
				</div>
			</div>
		</div>
	</div>
	<div class="row text-center">
		<div class="add-margin">
			<a href="javascript:void(0)" class="btn btn-default"
				onclick="self.close()"><spring:message code="lbl.close" /></a>
		</div>
	</div>
</form:form>