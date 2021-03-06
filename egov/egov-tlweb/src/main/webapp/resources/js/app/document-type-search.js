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

$(document).ready(function(){
	tableContainer1 = $("#document-Table"); 
	$("#searchbtn").click(function (){
		tableContainer1.dataTable({
			"sPaginationType": "bootstrap",
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
			"bDestroy": true,
			"autoWidth": false,
			"ajax": {
				type:"POST",
				url:"search?"+$("#searchdocumentform").serialize(),
				dataSrc: function ( json ) {
	                //Make your callback here.
	                $('.report-section').removeClass('display-hide');
	                return json.data;
	            }       
			},
			"columns": [
						{ "data": "name"},
						{ "data": "applicationType"},
						{ "data": "mandatory"},
						{ "data" : null, 'sClass':"text-center","target":-1,"defaultContent": 
							
		                   '<button type="button" class="btn btn-xs btn-secondary edit add-margin"><span class="glyphicon glyphicon-edit"></span>&nbsp;Edit</button>'}
									,
									{ "data": "id", "visible":false }
									]
								});
	                     });

	$("#document-Table").on('click','tbody tr td .edit',function(event) {
		var id = tableContainer1.fnGetData($(this).parent().parent(),4);
		var url = '/tl/documenttype/edit/'+id ;
		window.open(url,id,'width=900, height=700, top=300, left=260,scrollbars=yes');
		
	});

});
