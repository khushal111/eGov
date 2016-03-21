<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#------------------------------------------------------------------------------- -->
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<html>
<head>
<title><s:text name='transferProperty' /></title>
<link rel="stylesheet" href="<c:url value='/resources/global/css/font-icons/font-awesome-4.3.0/css/font-awesome.min.css' context='/egi'/>">

<link
	href="<c:url value='/resources/global/css/bootstrap/bootstrap-datepicker.css' context='/egi'/>"
	rel="stylesheet" type="text/css" />
<script
	src="<c:url value='/resources/global/js/bootstrap/bootstrap-datepicker.js' context='/egi'/>"></script>
<script type="text/javascript">
	jQuery.noConflict();
	jQuery("#loadingMask").remove();
	function loadOnStartUp() {
		enableBlock();
		try {
			jQuery(".datepicker").datepicker({
				format : "dd/mm/yyyy",
				autoclose:true
			});
		} catch (e) {
			console.warn("No Date Picker " + e);
		}

		var aadhartextboxes = jQuery('.txtaadhar');
		console.log(aadhartextboxes);
		aadhartextboxes.each(function() {
			if (jQuery(this).val()) {
				getAadharDetailsForTransferee(this);
			}
		});
		loadDesignationFromMatrix();
	}
</script>
</head>
<body onload="loadOnStartUp();">
	<div class="formmainbox">
		<s:if test="%{hasErrors()}">
			<div class="errorstyle" id="property_error_area">
				<div class="errortext">
					<s:actionerror />
					<s:fielderror />
				</div>
			</div>
		</s:if>
		<s:form action="save" name="transferform" theme="simple"
			enctype="multipart/form-data">
			<s:push value="model">
				<s:token />
				<div class="headingbg">
					<s:text name="transferortitle" />
				</div>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="bluebox2" style="width: 5%;">&nbsp;</td>
						<td class="bluebox" style="width: 20%"><s:text name="prop.Id"></s:text>
							:</td>
						<td class="bluebox"><span class="bold"><s:property
									value="basicproperty.upicNo" default="N/A" /></span> <s:hidden
								name="assessmentNo" id="assessmentNo"
								value="%{basicproperty.upicNo}" />
								<s:hidden name="meesevaApplicationNumber" id="meesevaApplicationNumber" value="%{meesevaApplicationNumber}" />
			</td>
						<td class="bluebox">&nbsp;</td>
						<td style="width: 25%;">&nbsp;</td>
					</tr>
					<tr>
						<td class="bluebox2">&nbsp;</td>
						<td class="bluebox"><s:text name="PropertyAddress"></s:text>
							:</td>
						<td class="bluebox"><span class="bold"><s:property
									value="basicproperty.address" default="N/A"/></span></td>
						<td class="bluebox"><s:text name="Zone"></s:text> :</td>
						<td class="bluebox"><span class="bold"><s:property
									value="basicproperty.propertyID.zone.name" default="N/A"/></span></td>
					</tr>

					<tr>
						<td class="greybox2">&nbsp;</td>
						<td class="greybox"><s:text name="Ward" /> :</td>
						<td class="greybox"><span class="bold"><s:property
									value="basicproperty.propertyID.ward.name" default="N/A"/></span></td>
						<td class="greybox"><s:text name="block" /> :</td>
						<td class="greybox"><span class="bold"><s:property
									value="basicproperty.propertyID.area.name" default="N/A"/></span></td>
					</tr>

					<tr>
						<td class="greybox2">&nbsp;</td>
						<td class="greybox"><s:text name="currentpropertytax" /> :</td>
						<td class="greybox"><span class="bold">Rs. <s:property
									value="currentPropertyTax" /> /-
						</span></td>
					</tr>
					<tr>
						<td colspan="5">
							<div class="headingsmallbg">
								<span class="bold"><s:text name="ownerdetails.title"></s:text></span>
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="5">
							<table class="tablebottom" id="" width="100%" border="0"
								cellpadding="0" cellspacing="0">
								<tbody>
									<tr>
										<th class="bluebgheadtd"><s:text name="adharno" /></th>
										<th class="bluebgheadtd"><s:text name="MobileNumber" /></th>
										<th class="bluebgheadtd"><s:text name="OwnerName" /></th>
										<th class="bluebgheadtd"><s:text name="gender" /></th>
										<th class="bluebgheadtd"><s:text name="EmailAddress" /></th>
										<th class="bluebgheadtd"><s:text name="GuardianRelation" /></th>
										<th class="bluebgheadtd"><s:text name="Guardian" /></th>
									</tr>
									<s:iterator value="basicproperty.propertyOwnerInfo"
										status="status">
										<tr>
											<td class="blueborderfortd" align="center">
												<s:if test='%{owner.aadhaarNumber == ""}'>
							        				<span
												class="bold">N/A</span>
							        			</s:if>
							        			<s:else>
													<span
												class="bold"><s:property value="%{owner.aadhaarNumber}" default="N/A" />  </span>      			
							        			</s:else>
											</td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
													value="owner.mobileNumber" /></span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
													value="owner.name" /></span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
													value="owner.gender" /></span></td>
											<td class="blueborderfortd" align="center">
												<s:if test='%{owner.emailId == ""}'><span
												class="bold">N/A</span></s:if>
	        		   							<s:else><span
												class="bold"><s:property value="%{owner.emailId}" /></span></s:else>
											</td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
													value="owner.guardianRelation" default="N/A" /></span></td>
											<td class="blueborderfortd" align="center"><span
												class="bold"><s:property
													value="owner.guardian" default="N/A" /></span></td>
										</tr>
									</s:iterator>
								</tbody>
							</table>
						</td>
					</tr>
					<%@ include file="transfereeDetailsForm.jsp"%>
					<tr>
						<td class="greybox2">&nbsp;</td>
						<td class="greybox"><s:text name="transferreason"></s:text> <span
							class="mandatory1">*</span> :</td>
						<td class="greybox"><s:select name="mutationReason"
								id="transRsnId" list="dropdownData.MutationReason" listKey="id"
								listValue="mutationName" headerKey="-1"
								headerValue="%{getText('default.select')}"
								value="%{mutationReason.id}" onchange="enableBlock();" />
						</td>
						<td class="greybox reasonRow"><s:text name="saleDetls" /> <span
							class="mandatory1">*</span> :</td>
						<td class="greybox reasonRow"><s:textarea cols="30" rows="2"
								name="saleDetail" id="saleDetail"
								onchange="return validateMaxLength(this);"
								onblur="trim(this,this.value);"></s:textarea></td>
					</tr>

					<tr>
						<td class="greybox2">&nbsp;</td>
						<td class="greybox"><s:text name="docNum" /><span
							class="mandatory1">*</span> :</td>
						<td class="greybox"><s:textfield name="deedNo" id="docNum"
								maxlength="64" onblur="checkZero(this);validateRegDocNumber(this,'Registration Document Number')" /></td>
						<td class="greybox"><s:text name="docDate" /><span
							class="mandatory1">*</span> :</td>
						<td class="greybox"><s:date name="deedDate" var="docDate"
								format="dd/MM/yyyy" /> <s:textfield name="deedDate"
								id="deedDate" maxlength="10" value="%{docDate}"
								autocomplete="off"
								onkeyup="DateFormat(this,this.value,event,false,'3')"
								onblur="validateDateFormat(this);" cssClass="datepicker" /></td>
					</tr>
					<s:if test="%{!documentTypes.isEmpty()}">
						<%@ include file="../common/DocumentUploadForm.jsp"%>
					</s:if>
				</table>
				<s:if test="%{propertyByEmployee == true}">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<%@ include file="../workflow/commonWorkflowMatrix.jsp"%>
						</tr>
						<tr>
							<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>
						</tr>
					</table>
				</s:if>
				<s:else>
					<tr align="center">
						<%@ include file="../workflow/commonWorkflowMatrix-button.jsp"%>
					</tr>
				</s:else>
			</s:push>
		</s:form>
		<div align="left" class="mandatory1" style="font-size: 11px">*
			Mandatory Fields</div>
	</div>
	<script type="text/javascript">
		function enableSaleDtls(obj) {
			var selectedValue = obj.options[obj.selectedIndex].text;
			if (selectedValue == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@MUTATIONRS_SALES_DEED}" />') {
				document.getElementById("saleDetail").readOnly = false;
				document.getElementById("saleDetail").className = "";
			} else {
				document.getElementById("saleDetail").value = "";
				document.getElementById("saleDetail").className = "hiddentext";
				document.getElementById("saleDetail").readOnly = true;
			}

			if (selectedValue == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@MUTATIONRS_OTHERS}" />') {
				document.getElementById("mutationRsnRow").style.display = "";
			} else {
				document.getElementById("mutationRsnRow").style.display = "none";
			}
		}
		function confirmClose() {
			var result = confirm("Do you want to close the window?");
			if (result == true) {
				window.close();
				return true;
			} else {
				return false;
			}
		}
		function enableBlock() {
			var obj = document.getElementById("transRsnId");
			if (obj != null || obj != "undefined") {
				var selectedValue = obj.options[obj.selectedIndex].text;
				if (selectedValue == '<s:property value="%{@org.egov.ptis.constants.PropertyTaxConstants@MUTATIONRS_SALES_DEED}" />') {
					jQuery("#saleDetail").val("");
					jQuery("td.reasonRow").show();
				} else {
					jQuery("td.reasonRow").hide();
				}
			}
		}
		
	</script>
	<script src="<c:url value='/resources/global/js/egov/inbox.js' context='/egi'/>"></script>
</body>
</html>