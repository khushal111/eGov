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
<%@ page contentType="text/html" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0"
			style="text-align: left">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.measurementbook.details" />
				</div>
			</div>
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.mb.referencenumber" />
					</div>
					<div class="col-xs-2 add-margin view-content">
						<c:out default="N/A" value="${mbHeader.mbRefNo}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.mbentry.date" />
					</div>
					<div class="col-xs-2 add-margin view-content">
						<fmt:formatDate value="${mbHeader.mbDate}" pattern="dd/MM/yyyy" />
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.from.page.number" />
					</div>
					<div class="col-xs-2 add-margin view-content">
						<c:out default="N/A" value="${mbHeader.fromPageNo}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.to.page.number" />
					</div>
					<div class="col-xs-2 add-margin view-content">
						<c:out default="N/A" value="${mbHeader.toPageNo}"></c:out>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.measurement.abstract" />
					</div>
					<div class="col-xs-2 add-margin view-content">
						<c:out default="N/A" value="${mbHeader.mbAbstract}"></c:out>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary" data-collapsed="0"
			style="text-align: left">
			<div class="panel-heading">
				<div class="panel-title">
					<spring:message code="lbl.workdetails" />
				</div>
			</div>
			<div class="panel-body">
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.estimatenumber" />
					</div>
					<div class="col-xs-2 add-margin view-content">
						<c:out default="N/A" value="${mbHeader.workOrderEstimate.estimate.estimateNumber}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.nameofthework" />
					</div>
					<div class="col-xs-2 add-margin view-content">
						<c:out default="N/A" value="${mbHeader.workOrderEstimate.estimate.name}"></c:out>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.workidentificationnumber" /> 
					</div>
					<div class="col-xs-2 add-margin view-content">
						<c:out default="N/A" value="${mbHeader.workOrderEstimate.estimate.projectCode.code}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.loanumber" />
					</div>
					<div class="col-xs-2 add-margin view-content">
						<c:out default="N/A" value="${mbHeader.workOrderEstimate.workOrder.workOrderNumber}"></c:out>
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.contractor.name" />
					</div>
					<div class="col-xs-2 add-margin view-content">
						<c:out default="N/A" value="${mbHeader.workOrderEstimate.workOrder.contractor.name}"></c:out>
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.work.assigned" />
					</div>
					<div class="col-xs-2 add-margin view-content">
						<c:out default="N/A" value="${mbHeader.workOrderEstimate.workOrder.engineerIncharge.name}"></c:out> 
					</div>
				</div>
				<div class="row add-border">
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.mb.issued.date" />
					</div>
					<div class="col-xs-2 add-margin view-content">
						<fmt:formatDate value="${mbHeader.mbIssuedDate}" pattern="dd/MM/yyyy" />
					</div>
					<div class="col-xs-3 add-margin">
						<spring:message code="lbl.contractor.comments" /> 
					</div>
					<div class="col-xs-2 add-margin view-content">
						<c:out default="N/A" value="${mbHeader.contractorComments}"></c:out> 
					</div>
				</div>
			</div>
		</div>
	</div>
</div>