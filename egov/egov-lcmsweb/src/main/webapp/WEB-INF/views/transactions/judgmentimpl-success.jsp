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
	class="form-horizontal form-groups-bordered"
	modelAttribute="judgmentImpl" id="judgmentImplform">
	<div class="row">
		<div class="col-md-12">
			<c:if test="${not empty message}">
				<div role="alert">${message}</div>
			</c:if>
			<div class="main-content">
				<div class="row">
					<div class="col-md-12">
						<div class="panel panel-primary" data-collapsed="0">
							<div class="panel-heading">
								<div class="panel-title">Judgment Implementation Details</div>
							</div>
							<div class="panel-body custom">
								<div class="row add-border">
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.iscomplied" />
									</div>
									<div class="col-sm-3 add-margin view-content">
										${judgmentImpl.judgmentImplIsComplied}</div>
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.dateofcompliance" />
									</div>
									<div class="col-sm-3 add-margin view-content">
										<fmt:formatDate pattern="MM/dd/yyyyy"
											value="${judgmentImpl.dateOfCompliance}" var="datecomp" />
										<c:out value="${datecomp}" />
									</div>
								</div>
								<div class="row add-border">
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.compliancereport" />
									</div>
									<div class="col-sm-3 add-margin view-content">
										${judgmentImpl.complianceReport}</div>
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.reason" />
									</div>
									<div class="col-sm-3 add-margin view-content">
										${judgmentImpl.implementationFailure}</div>
								</div>
								<div class="row add-border">
								<div class="col-xs-3 add-margin">
										<spring:message code="lbl.details" />
									</div>
									<div class="col-sm-3 add-margin view-content">
										${judgmentImpl.details}</div>
										</div>
										
										<div class="row add-border">
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.srnumber" />
									</div>
									<div class="col-sm-3 add-margin view-content">
										${judgmentImpl.appeal[0].srNumber}</div>
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.appealfieldon" />
									</div>
									<div class="col-sm-3 add-margin view-content">
										<fmt:formatDate pattern="MM/dd/yyyyy"
											value="${judgmentImpl.appeal[0].appealFiledOn}" var="datecomp" />
										<c:out value="${datecomp}" />
									</div>
								</div>
								
									<div class="row add-border">
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.appealfieldby" />
									</div>
									<div class="col-sm-3 add-margin view-content">
										${judgmentImpl.appeal[0].appealFiledBy}</div>
									
								</div>
								
										<div class="row add-border">
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.canumber" />
									</div>
									<div class="col-sm-3 add-margin view-content">
										${judgmentImpl.contempt[0].caNumber}</div>
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.receiveddate" />
									</div>
									<div class="col-sm-3 add-margin view-content">
										<fmt:formatDate pattern="MM/dd/yyyyy"
											value="${judgmentImpl.contempt[0].receivingDate}" var="datecomp" />
										<c:out value="${datecomp}" />
									</div>
								</div>
									<div class="row add-border">
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.appearancecomm" />
									</div>
									<div class="col-sm-3 add-margin view-content">
										${judgmentImpl.contempt[0].iscommapprRequired}</div>
									<div class="col-xs-3 add-margin">
										<spring:message code="lbl.dateofapp" />
									</div>
									<div class="col-sm-3 add-margin view-content">
										<fmt:formatDate pattern="MM/dd/yyyyy"
											value="${judgmentImpl.contempt[0].commappDate}" var="datecomp" />
										<c:out value="${datecomp}" />
									</div>
								</div>
								
							</div>
						</div>
					</div>
				</div>
			</div>
				<jsp:include page="appealdocuments-view.jsp"></jsp:include>
		<input type="hidden" name="appealDocList" value="${appealDocList}" />
			
				<div class="form-group text-center" >
			<a onclick="self.close()" class="btn btn-default" href="javascript:void(0)"><spring:message code="lbl.close"/></a>
	
					<!-- <a href="javascript:void(0)" class="btn btn-default"
						onclick="self.close()">Close</a> -->
					<%-- 	<form:button type='button' class='btn btn-default' ><spring:message code='lbl.close' /></form:button> --%>
				
			</div>
		</div>
	</div>
</form:form>
