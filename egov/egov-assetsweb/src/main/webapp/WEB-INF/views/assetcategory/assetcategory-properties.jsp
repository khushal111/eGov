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
  
<%@ include file="/includes/taglibs.jsp"%>
<!-- <input type="hidden" id="typeaheadSourceType" /> -->
<c:choose>
	<c:when test="${not empty assetCategory.getCategoryProperties()}">
		<div class="panel-heading">
			<div class="panel-title">${assetCategory.name} Details</div>
		</div>
		<c:forEach items="${assetCategory.categoryProperties}"
			var="categoryProperties" varStatus="vs">
			<c:if test="${categoryProperties.isMandatory==true}">
				<c:set var="mandatoryLable" value='<span class="mandatory"></span>' />
				<c:set var="required" value='required="required" ' />
			</c:if>
			<c:if test="${empty categoryProperties.localText}">
				<c:set var="localText" value="" />
			</c:if>
			<c:if test="${not empty categoryProperties.localText}">
				<c:set var="localText" value="/${categoryProperties.localText}" />
			</c:if>
			<c:if test="${vs.index%2!=0}">
				<c:set var="labelClass" value='col-sm-2 control-label text-right' />
			</c:if>
			<c:if test="${vs.index%2==0}">
				<div class="form-group">
				<c:set var="labelClass" value='col-sm-3 control-label text-right' />
			</c:if>
			<label class="col-sm-3 control-label text-right">
				${categoryProperties.name}${localText} ${mandatoryLable} </label>
			<div class="col-sm-3 add-margin">
				<input type="hidden" name="categoryProperties[${vs.index}].id"
					value="${categoryProperties.id}">

				<c:if test="${categoryProperties.dataType=='String'}">
					<input type="text" name="categoryProperties[${vs.index}].value"
						id="${categoryProperties.name}" class="form-control text-left"
						${required} />
				</c:if>
				<c:if test="${categoryProperties.dataType=='Number'}">
					<input type="text" name="categoryProperties[${vs.index}].value"
						id="${categoryProperties.name}"
						class="form-control text-right patternvalidation"
						data-pattern="number" ${required} />
				</c:if>
				<c:if test="${categoryProperties.dataType=='Enumeration'}">
					<select name="categoryProperties[${vs.index}].value"
						id="${categoryProperties.name}" class="form-control"
						data-pattern="number" ${required}>
						<option value="">
							<spring:message code="lbl.select" />
						</option>
						<c:forTokens items="${categoryProperties.enumValues}" delims=","
							var="val">
							<option value="${val}">${val}</option>
						</c:forTokens>
					</select>
				</c:if>
				<c:if test="${categoryProperties.dataType=='Date'}">
					<input type="text"
						<%-- name="categoryProperties[${vs.index}].${categoryProperties.name}" --%>
						name="categoryProperties[${vs.index}].value"
						id="${categoryProperties.name}" class="form-control datepicker"
						data-date-end-date="0d" data-inputmask="'mask': 'd/m/y'"
						value="${properties[vs.index].value}"
						${required} data-set-date="dd-mm-yyyy" /> <!-- yyyy-mm-dd -->
				</c:if>
				<c:if test="${categoryProperties.dataType=='DateTime'}">
					<input type="text"
						name="categoryProperties[${vs.index}].value"
						id="${categoryProperties.name}" class="form-control datetimepicker"
						value="${properties[vs.index].value}"
						${required} />
				</c:if>
				<c:if test="${categoryProperties.dataType=='MasterData'}">
					<input type="hidden" name="categoryProperties[${vs.index}].value"
						id="categoryProperties" value="">
					<input type="hidden" name="categoryProperties[${vs.index}].id"
						id="${categoryProperties.enumValues}" value="">
					<input type="text" name="${categoryProperties.name}" value=""
						id="${categoryProperties.id}" class="form-control autocomplete"
						data-hidden-elem2="categoryProperties[${vs.index}].value"
						data-hidden-elem="categoryProperties[${vs.index}].id"
						data-source-type="${categoryProperties.id}" ${required}/>
				</c:if>

			</div>
			<c:if test="${vs.index%2==1}">
				</div>
			</c:if>
		</c:forEach>
	</c:when>
</c:choose>
