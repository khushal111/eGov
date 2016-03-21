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
<%@ include file="/includes/taglibs.jsp"%>
<%@ page language="java"%>
<html>
<head>
<title><s:text name="function.create" /></title>
<script type="text/javascript">

						function onLoadTask() {
							var close = '<s:property value="close"/>';
							var success = '<s:property value="success"/>';

							if (success == 'yes') {
								bootbox.alert("Function Created Successfully");
								} else if((success == 'no')){
									bootbox.alert("Function Could Not be Created");
									}
							
							if (close == 'true') {
								window.close();
							}

							
						}
						
						function validate(){
							if(document.getElementById('code').value == null || document.getElementById('code').value==''){
								bootbox.alert("Please enter Function Code");
								return false;
							}
							if(document.getElementById('name').value == null || document.getElementById('name').value==''){
								bootbox.alert("Please enter Function Name");
								return false;
							}
							return true;
						}
						function setClose() {
							var close = document.getElementById('close');    
							   close.value = true;
							   return true;
							}
					</script>
</head>
<body onload="onLoadTask();">
	<div class="formmainbox">
		<div class="subheadnew">
			<s:text name="function.create" />
		</div>
		<div style="color: red">
			<s:actionmessage theme="simple" />
			<s:actionerror />
			<s:fielderror />
		</div>
		<s:form name="funcForm" action="function" theme="simple">
			<%@include file="function-form.jsp"%>
			<div class="buttonbottom">
				<s:submit name="create" value="Save & New" cssClass="buttonsubmit"
					onclick="javascript: return validate();return submitForm();" />
				<s:submit name="create" value="Save & Close" cssClass="buttonsubmit"
					onclick="return submitForm();validate();setClose();" />
				<s:hidden name="close" id="close" />
				<input type="button" id="Close" value="Close"
					onclick="javascript:window.close()" class="button" />
			</div>
	</div>
	<s:token />
	</s:form>
	</div>
	<script type="text/javascript">
		<s:if test="%{clearValues == true}">
			document.getElementById('code').value = "";
			document.getElementById('name').value = "";
			document.getElementById('function.function.id').value = "";
			document.forms[0].isActive.checked=false;
		</s:if>
		function submitForm() {
			document.forms[0].action = '${pageContext.request.contextPath}/masters/function-create.action';
			document.forms[0].submit();
		}
	</script>
</body>
</html>