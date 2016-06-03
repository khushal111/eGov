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


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.joda.org/joda/time/tags" prefix="joda"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<form:form  method ="post" action="" class="form-horizontal form-groups-bordered" modelAttribute="usageType" id="usageTypeMasterform" >
<div class="row">
	<div class="col-md-12">
		<c:if test="${not empty message}">
            <div class= role="alert">${message}</div>
        </c:if>
<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title"><spring:message code="title.usage.master.details"/></div>
	</div>
<div class="panel-body">
	<div class="row add-border">
		<div class="col-md-3 col-xs-3 add-margin"><spring:message code="lbl.code"/> </div>
			<div class="col-md-3 col-xs-3 add-margin view-content"><c:out value="${usageType.code}"/></div>
				<div class="col-md-3 col-xs-3 add-margin"><spring:message code="lbl.usagetype"/></div>
						<div class="col-md-3 col-xs-3 add-margin view-content">
							<c:out value="${usageType.name}"/>
						</div>
					</div>
					<div class="row add-border">
						 <div class="col-md-3 col-xs-6 add-margin"><spring:message code="lbl.status"/></div>
							<div class="col-md-3 col-xs-6 add-margin view-content">
								<c:choose>
									<c:when test="${usageType.active == 'true'}">
										<c:out value="ACTIVE" />
									</c:when> 
									<c:otherwise>
										<c:out value="INACTIVE" />
									</c:otherwise>
								</c:choose>
							</div> 
						</div>
					</div>
				</div>
			</div>
		</div>
<input type="hidden" value="${mode}" id="mode" />
				<div class="row text-center">
					<div class="row">
						<c:if test="${mode == 'create'}"> 
							<button type="button" class="btn btn-primary" id="addnewid"><spring:message code="lbl.addnew" /></button> 
						</c:if>
						<a href="javascript:void(0)" class="btn btn-default" onclick="self.close()"><spring:message code="lbl.close" /></a>
					</div>
				</div>
					
</form:form>
<link rel="stylesheet"
	href="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/css/datatables.responsive.css' context='/egi'/>">
<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/jquery.dataTables.min.js' context='/egi'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/dataTables.bootstrap.js' context='/egi'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/global/js/jquery/plugins/datatables/responsive/js/datatables.responsive.js' context='/egi'/>" type="text/javascript"></script>
<script src="<c:url value='/resources/js/app/usage-type-master.js?rnd=${app_release_no}'/>"></script>