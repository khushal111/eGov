/*#-------------------------------------------------------------------------------
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
#-------------------------------------------------------------------------------*/
$(document).ready(function(){
	
	$('#propertyIdentifier').blur(function(){
		validateSewerageConnection();
	});
	
	
	$('#shscNumber').blur(function(){
		validateSewerageConnectionNumber();
	});
	
	$('#executionDate').blur(function(){
		clear_formDemandDetailTable();
	});
	
	
	function validateSewerageConnection() {
		propertyID=$('#propertyIdentifier').val()
		if(propertyID != '') {
			$.ajax({
				url: "/stms/ajaxconnection/check-connection-exists",      
				type: "GET",
				data: {
					propertyID : propertyID  
				},
				dataType: "json",
				success: function (response) { 
					if(response != '') {
							resetPropertyDetails();
							bootbox.alert(response);
					}
					else {
						loadPropertyDetails();
					}
				}, 
				error: function (response) {
					resetPropertyDetails();
					bootbox.alert("connection validation failed");
				}
			});
		}		
	}
	
	
	function validateSewerageConnectionNumber(){
		shscNumber=$('#shscNumber').val()
		if(shscNumber != '') {
			$.ajax({
				url: "/stms/ajaxconnection/check-shscnumber-exists",      
				type: "GET",
				data: {
					shscNumber : shscNumber  
				},
				dataType: "json",
				success: function (response) { 
					if(response != '') {
							$('#shscNumber').val('');
							bootbox.alert(response);
					}
				}, 
				error: function (response) {
					$('#shscNumber').val('');
					bootbox.alert("connection validation failed");
				}
			});
		}	
	}
	
	function clear_formDemandDetailTable(){
		var resultsTable = document.getElementById('legacyDemandDetails');
		var tablength = resultsTable.rows.length;
		if(tablength>1){
			for(var i = tablength; i >1 ;i--){   
			  resultsTable.deleteRow(i-1);
			}
		}
		formDemandDetailTable(); 
	}
	
	
	function formDemandDetailTable(){
		executionDate=$('#executionDate').val()
		if(executionDate != '') {
			$.ajax({
				url: "/stms/ajaxconnection/getlegacy-demand-details",      
				type: "GET",
				data: {
					executionDate : executionDate  
				},
				dataType: "json",
				success: function (response) { 
					if(response.length > 0) {
						$('#legacyDemandDetails').show();
						for(i=0; i<response.length; i++) {
							addDemandDetailRow(response[i].installment,response[i].reasonMaster,
									response[i].actualAmount,response[i].actualCollection,
									response[i].installmentId,response[i].demandReasonId);
						}
						bootbox.alert(response);
					}
				}, 
				error: function (response) {
					$('#executionDate').val('');
					bootbox.alert("connection validation failed"); 
				}
			});
		}	
	}
});


function loadPropertyDetails() {
	propertyID=$('#propertyIdentifier').val()
	if(propertyID != '') {
		$.ajax({
			url: "/ptis/rest/property/"+propertyID,      
			type: "GET",
			dataType: "json",
			success: function (response) { 
				var waterTaxDue = getWaterTaxDue(propertyID);
				//console.log(waterTaxDue['CURRENTWATERCHARGE']);  
						$('#propertyIdentifierError').html('');
						applicantName = '';
						for(i=0; i<response.ownerNames.length; i++) {
							if(applicantName == '')
								applicantName = response.ownerNames[i].ownerName;
							else 							
								applicantName = applicantName+ ', '+response.ownerNames[i].ownerName;
						}
						$("#applicantname").val(applicantName);
						$("#nooffloors").val(response.propertyDetails.noOfFloors);
						if(response.ownerNames[0].mobileNumber != '')
							$("#mobileNumber").val(response.ownerNames[0].mobileNumber);
						if(response.ownerNames[0].emailId != '')
							$("#email").val(response.ownerNames[0].emailId);
						$("#propertyaddress").val(response.propertyAddress);
						boundaryData = '';
						if(response.boundaryDetails.zoneName != null && response.boundaryDetails.zoneName != '')
							boundaryData = response.boundaryDetails.zoneName;
						if(response.boundaryDetails.wardName != null && response.boundaryDetails.wardName != '') {
							if(boundaryData == '')
								boundaryData = response.boundaryDetails.wardName;
							else
								boundaryData = boundaryData + " / " + response.boundaryDetails.wardName;
						}
						if(response.boundaryDetails.blockName != null && response.boundaryDetails.blockName != '') {
							if(boundaryData == '')
								boundaryData = response.boundaryDetails.blockName;
							else
								boundaryData = boundaryData + " / " +response.boundaryDetails.blockName; 
						}
						$("#aadhaar").val(response.ownerNames[0].aadhaarNumber);
						$("#locality").val(response.boundaryDetails.localityName);
						$("#zonewardblock").val(boundaryData);
						$("#propertytax").val(response.propertyDetails.currentTax);
						if(waterTaxDue['PROPERTYID']!="")
							$('#watercharges').val(waterTaxDue['CURRENTWATERCHARGE']);
						else
							$('#watercharges').val('N/A');
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	}		
}

function resetPropertyDetails() {
	$('#propertyIdentifier').val('');
	$('#applicantname').val('');
	$('#mobileNumber').val('');
	$('#email').val('');
	$('#aadhaar').val('');	
	$('#propertyaddress').val('');
	$('#locality').val('');
	$('#zonewardblock').val('');
	$('#nooffloors').val('');
	$('#propertytax').val('0.00');
	$('#watercharges').val('0.00');

}

function getWaterTaxDue(propertyID) {
	var result;
	if(propertyID != "") {
		$.ajax({
			url: "/stms/ajaxconnection/check-watertax-due",
				type: "GET",
				'async':false,
				cache: true,
				data: {
					assessmentNo : propertyID
				},
				dataType: "json",
		}).done(function(value) {
				result = value; 
		});
		return result;
	}
}

function initDemandDetailRow(instlmnt,reasondsc,demandamount,collectionamount){
	var table = document.getElementById('legacyDemandDetails');
	getControlInBranch(table.rows[1],'demandDetailBeanList0installment').value=instlmnt;
	getControlInBranch(table.rows[1],'demandDetailBeanList0reasonMasterDesc').value=reasondsc;
	getControlInBranch(table.rows[1],'demandDetailBeanList0actualAmount').value=demandamount;
	getControlInBranch(table.rows[1],'demandDetailBeanList0actualCollection').value=collectionamount;
	
}

function addDemandDetailRow(instlmntDesc,reasondsc,demandamount,collectionamount,instlmntId,dmndReasonId) {
    var table = document.getElementById('legacyDemandDetails');
    var rowCount = table.rows.length;
  
    var row = table.insertRow(rowCount); 
    var counts = rowCount;

	elementIndex = counts;
    var newRow = document.createElement("tr");
	var newCol = document.createElement("td");
	newRow.appendChild(newCol);
	 
    var cell1 = row.insertCell(0);
    cell1.className="text-center";
    var instllmnt = document.createElement("input");
    var att = document.createAttribute("class");
	att.value = "form-control";
	instllmnt.setAttributeNode(att); 
	instllmnt.type = "text";
	instllmnt.setAttribute("readonly", "readonly"); 
	instllmnt.setAttribute("required", "required");
	instllmnt.setAttribute("maxlength", "18");
	instllmnt.setAttribute("name", "demandDetailBeanList[" + (elementIndex-1) + "].installment");
	instllmnt.setAttribute("id", "demandDetailBeanList"+(elementIndex-1)+"installment");
	instllmnt.setAttribute("value", instlmntDesc);
	cell1.appendChild(instllmnt);
    
    newCol = document.createElement("td");        
    newRow.appendChild(newCol);
    var cell2 = row.insertCell(1);
    cell2.className = "text-center";
    var reasonDesc = document.createElement("input");
    var att = document.createAttribute("class");
	att.value = "form-control";
	reasonDesc.setAttributeNode(att); 
	reasonDesc.type = "text";
	reasonDesc.setAttribute("readonly", "readonly"); 
	reasonDesc.setAttribute("required", "required");
	reasonDesc.setAttribute("maxlength", "18");
	reasonDesc.setAttribute("name", "demandDetailBeanList[" + (elementIndex-1) + "].reasonMaster");
	reasonDesc.setAttribute("id", "demandDetailBeanList"+(elementIndex-1)+"reasonMaster");
	reasonDesc.setAttribute("value", reasondsc);
    cell2.appendChild(reasonDesc);
    
    newCol = document.createElement("td");
	newRow.appendChild(newCol);
    var cell3 = row.insertCell(2);
    cell3.className = "text-right";
    var actualAmount = document.createElement("input");
    actualAmount.setAttribute("class","form-control table-input text-right patternvalidation");
    actualAmount.setAttribute("data-pattern","decimalvalue"); 
    actualAmount.type = "text";
    actualAmount.setAttribute("maxlength", "8");
    actualAmount.setAttribute("name", "demandDetailBeanList[" + (elementIndex-1) + "].actualAmount");
    actualAmount.setAttribute("id", "demandDetailBeanList"+(elementIndex-1)+"actualAmount");
    actualAmount.setAttribute("value", demandamount);
    cell3.appendChild(actualAmount);  
    
    newCol = document.createElement("td");
	newRow.appendChild(newCol);
    var cell4 = row.insertCell(3);
    cell4.className = "text-right";
    var actualCollection = document.createElement("input");
    actualCollection.setAttribute("class","form-control table-input text-right patternvalidation");
    actualCollection.setAttribute("data-pattern","decimalvalue"); 
    actualCollection.type = "text";
    actualCollection.setAttribute("maxlength", "8");
    actualCollection.setAttribute("name", "demandDetailBeanList[" + (elementIndex-1) + "].actualCollection");
    actualCollection.setAttribute("id", "demandDetailBeanList"+(elementIndex-1)+"actualCollection");
    actualCollection.setAttribute("value", collectionamount);
    cell4.appendChild(actualCollection);  
    
    newCol = document.createElement("td");
	newRow.appendChild(newCol);
    var cell5 = row.insertCell(4);
    cell5.className = "text-right";
    var installmentId = document.createElement("input");
    installmentId.setAttribute("class","form-control");
    installmentId.setAttribute("data-pattern","decimalvalue"); 
    installmentId.type = "hidden";
    installmentId.setAttribute("name", "demandDetailBeanList[" + (elementIndex-1) + "].installmentId");
    installmentId.setAttribute("id", "demandDetailBeanList"+(elementIndex-1)+"installmentId");
    installmentId.setAttribute("value", instlmntId);
    cell5.appendChild(installmentId);
    
    newCol = document.createElement("td");
	newRow.appendChild(newCol);
    var cell6 = row.insertCell(5);
    cell6.className = "text-right";
    var demandReasonId = document.createElement("input");
    demandReasonId.setAttribute("class","form-control");
    demandReasonId.setAttribute("data-pattern","decimalvalue"); 
    demandReasonId.type = "hidden";
    demandReasonId.setAttribute("name", "demandDetailBeanList[" + (elementIndex-1) + "].demandReasonId");
    demandReasonId.setAttribute("id", "demandDetailBeanList"+(elementIndex-1)+"demandReasonId");
    demandReasonId.setAttribute("value", dmndReasonId);
    cell5.appendChild(demandReasonId); 
     
    patternvalidation();
}