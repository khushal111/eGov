<!--  #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#      accountability and the service delivery of the government  organizations.
#   
#       Copyright (C) <2015>  eGovernments Foundation
#   
#       The updated version of eGov suite of products as by eGovernments Foundation 
#       is available at http://www.egovernments.org
#   
#       This program is free software: you can redistribute it and/or modify
#       it under the terms of the GNU General Public License as published by
#       the Free Software Foundation, either version 3 of the License, or
#       any later version.
#   
#       This program is distributed in the hope that it will be useful,
#       but WITHOUT ANY WARRANTY; without even the implied warranty of
#       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#       GNU General Public License for more details.
#   
#       You should have received a copy of the GNU General Public License
#       along with this program. If not, see http://www.gnu.org/licenses/ or 
#       http://www.gnu.org/licenses/gpl.html .
#   
#       In addition to the terms of the GPL license to be adhered to in using this
#       program, the following additional terms are to be complied with:
#   
#   	1) All versions of this program, verbatim or modified must carry this 
#   	   Legal Notice.
#   
#   	2) Any misrepresentation of the origin of the material is prohibited. It 
#   	   is required that all modified versions of this material be marked in 
#   	   reasonable ways as different from the original version.
#   
#   	3) This license does not grant any rights to any user of the program 
#   	   with regards to rights under trademark law for use of the trade names 
#   	   or trademarks of eGovernments Foundation.
#   
#     In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------  -->
<html>
<head>
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/voucherHelper.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/javascript/contra.js"></script>
<script type="text/javascript"
	src="/EGF/resources/javascript/ajaxCommonFunctions.js"></script>
<style type="text/css">
@media print {
	input#button1 {
		display: none;
	}
}

@media print {
	input#button2 {
		display: none;
	}
}
</style>

</head>
<body onload="onloadTask();">
	<s:form action="contraBTC" theme="simple" name="cbtcform">
		<s:push value="model">
			<jsp:include page="../budget/budgetHeader.jsp">
				<jsp:param value="Cash Withdrawal" name="heading" />
			</jsp:include>
			<div class="formmainbox">
				<div class="subheadnew">View Cash Withdrawal</div>
				<div id="listid" style="display: block">
					<br />
				</div>
				<div align="center">
					<font style='color: red;'><p class="error-block"
							id="lblError"></p></font>
					<s:if test="%{not close}">
						<span class="mandatory"> <s:actionerror /> <s:fielderror />
							<s:actionmessage />
						</span>
					</s:if>
				</div>
				<%@include file="contraBTC-form.jsp"%>
		</s:push>
		<br />
		<div id="buttons">
			<input name="button" type="button" class="buttonsubmit" id="button1"
				value="Print" onclick="window.print()" />&nbsp;
			<s:submit value="Close" onclick="javascript: self.close()"
				id="button2" cssClass="button" />
		</div>
		<div id="resultGrid"></div>
		</div>
	</s:form>
	<SCRIPT type="text/javascript">
function onloadTask(){
	disableControls(0,true);
	var message = '<s:property value="message"/>';
	if(message != null && message != '')
		showMessage(message);
	var element = document.getElementById('accountNumber');
	if(element != undefined){
		populateAvailableBalance(element)
		if(element.value != -1){
			populateNarration(element)
		}
	}
}

function showMessage(message){
	var close = <s:property value="close"/>;
	var voucherHeaderId = <s:property value="voucherHeader.id"/>;
	bootbox.alert(message);
	if(close == true){
		self.close();
	}
	document.forms[0].action = "${pageContext.request.contextPath}/voucher/preApprovedVoucher!loadvoucherview.action?vhid="+voucherHeaderId;
	document.forms[0].submit();
	
}

</SCRIPT>
</body>
</html>