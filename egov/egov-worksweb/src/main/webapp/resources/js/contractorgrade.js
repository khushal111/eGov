/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
function modifyGradeData() {
	var id = document.getElementById('id').value;
	window.location = 'contractorGrade-edit.action?mode=edit&id='+id;
}

function viewGradeData() {
	var id = document.getElementById('id').value;
	window.location = 'contractorGrade-edit.action?mode=view&id='+id;
}

function validateContractorGradeFormAndSubmit(){
	var grade = document.getElementById("grade").value;
	if(grade == ''){
		var message = document.getElementById('contractorGradeError').value;
        showMessage('contractorgrade_error', message);
		return false;
	}
	var description = document.getElementById("description").value;
	if(description == ''){
		var message = document.getElementById('contractorDescriptionError').value;
        showMessage('contractorgrade_error', message);
		return false;
	}
	var minAmount = document.getElementById("minAmount").value;
	var maxAmount = document.getElementById("maxAmount").value;	
	if(minAmount == ''){
		var message = document.getElementById('contractorMinamtError').value;
        showMessage('contractorgrade_error', message);
		return false;
	}
	if(maxAmount == ''){
		var message = document.getElementById('contractorMaxamtError').value;
        showMessage('contractorgrade_error', message);
		return false;
	}
	if(maxAmount <= 0){
		var message = document.getElementById('gradeValidError').value;
        showMessage('contractorgrade_error', message);
		return false;
	}
    if (maxAmount <= minAmount ) {
    	var message = document.getElementById('gradeError').value;
        showMessage('contractorgrade_error', message);
        return false;
    }
    return true;
}