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
<style type="text/css">
#container iframe {
	width: 100%;
	height: 2000px;
	border: none;
}

#container {
	width: 100%;
	height: 550px;
	padding: 0;
	overflow-y: scroll;
}

@media print {
	input#btnPrint {
		display: none;
	}
}

@media print {
	input#printPDF {
		display: none;
	}
}

@media print {
	input#printXLS {
		display: none;
	}
}
</style>

<body>
	<div id="container">
		<iframe id="report" name="report"
			src='/EGF/budget/budgetReport!ajaxGenerateDepartmentWiseHtml.action?model.financialYear.id=<s:property value="model.financialYear.id"/>&model.department.id=<s:property value="model.department.id"/>&model.type=<s:property value="model.type"/>'></iframe>
	</div>

	<s:form name="budgetDetailReportForm" action="budgetReport"
		theme="simple">
		<input type="hidden" name="model.financialYear.id"
			value='<s:property value="model.financialYear.id"/>' />
		<input type="hidden" name="model.department.id"
			value='<s:property value="model.department.id"/>' />
		<input type="hidden" name="model.type"
			value='<s:property value="model.type"/>' />
		<div id="buttons">
			<input type="button" id="btnPrint"
				onclick="javascript:parent.report.print();" value="PRINT"
				class="buttonsubmit" />
			<s:submit value="SAVE AS PDF" method="generateDepartmentWisePdf"
				cssClass="buttonsubmit" />
			<s:submit value="SAVE AS EXCEL" method="generateDepartmentWiseXls"
				cssClass="buttonsubmit" />
		</div>
	</s:form>
</body>