<!-- #-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2016>  eGovernments Foundation
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
	<div class="panel-heading">
	<div class="panel-body">
	<table class="table table-striped table-bordered" id="legacyDemandDetails"  style="display: none;">	
			<thead>
			      <tr>
			        <th class="text-center">Installment</th>
			        <th class="text-center">Tax</th>
					<th class="text-right">Demand</th>
					<th class="text-right">Collection</th>
			      </tr>
		    </thead>
		<tbody> 
	 	<%-- <tr class="data-fetchedFromDB">
			<td class="text-center"><form:input type="text" class="form-control"   path="demandDetailBeanList[0].installment" id="demandDetailBeanList0installment" readonly="true" value=""/></td>
			<td class="text-center"><form:input type="text" class="form-control"  path="demandDetailBeanList[0].reasonMasterDesc" id="demandDetailBeanList0reasonMasterDesc" /></td>
			<td class="text-right"><form:input type="text" class="form-control table-input text-right patternvalidation quantity" data-pattern="decimalvalue" path="demandDetailBeanList[0].actualAmount" id="demandDetailBeanList0actualAmount" maxlength="8" /></td>
			<td class="text-right"><form:input type="text" class="form-control table-input text-right patternvalidation quantity" data-pattern="decimalvalue" path="demandDetailBeanList[0].actualCollection" id="demandDetailBeanList0actualCollection" maxlength="8" /></td>
	      </tr>   --%>
		</tbody>
		</table>  
	</div>
</div>