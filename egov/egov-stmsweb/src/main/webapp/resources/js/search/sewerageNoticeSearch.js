/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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

var tableContainer;
jQuery(document).ready(function($) {
	$('.table-header').hide();
	tableContainer = $("#sewerageNoticeSearchResults");
	document.onkeydown=function(evt){
		var keyCode = evt ? (evt.which ? evt.which : evt.keyCode) : event.keyCode;
		if(keyCode == 13){
			submitButton();	
		}
	}
});



$("#mergeanddownload").click(function(event){
	
	if(validateNoticeForm())
	{
		$.ajax({
			url: "/stms/reports/search-NoticeResultSize?"+$('#sewerageNoticeSearch').serialize(),      
			type: "GET",
			dataType: "json",
			async:false,
			beforeSend:function(){
				$('.loader-class').modal('show');
			},
			success: function (response) { 
				$('.loader-class').modal('hide');
				if(response <= 500 && response > 0){
					window.open('/stms/reports/searchNotices-mergeAndDownload?'+$('#sewerageNoticeSearch').serialize());
				}
				else if(response <= 0){
					bootbox.alert("No data available,Please refine your search");
				} else{
					bootbox.alert("Please refine your search,result contain more than 500 records");
				}
			},
			error: function() {
				$('.loader-class').modal('hide');
				bootbox.alert("Something, went wrong!");
		    }
		});
		
	}
	return false;
});  

$("#zipanddownload").click(function(event){
	
	if(validateNoticeForm())
	{
		$.ajax({
			url: "/stms/reports/search-NoticeResultSize?"+$('#sewerageNoticeSearch').serialize(),      
			type: "GET",
			dataType: "json",
			async:false,
			beforeSend:function(){
				$('.loader-class').modal('show');
			},
			success: function (response) { 
				$('.loader-class').modal('hide');
				if(response <= 500 && response > 0){
					window.open('/stms/reports/searchNotices-seweragezipAndDownload?'+$('#sewerageNoticeSearch').serialize());
				}
				else if(response <= 0){
					bootbox.alert("No data available,Please refine your search");
				} else{
					bootbox.alert("Please refine your search,result contain more than 500 records");
				}
			},
			error: function() {
				$('.loader-class').modal('hide');
				bootbox.alert("Something, went wrong!");
		    }
		});
		
	}
	
	return false;
});

	  
$("#searchSewerageNotice").click(function(event){
	if(validateNoticeForm())
	{
		submitButton();
	}
	event.preventDefault();
});

function validateNoticeForm()
{
	var shscNumber=$('#shscNumber').val();
	var applicantName=$('#applicantName').val();
	var mobileNumber=$('#mobileNumber').val();
	var wardName=$('#app-mobno').val();
	var doorNo=$('#app-appcodo').val();
	var noticetype=$('#noticetype').val();
	var noticeGeneratedFrom=$('#noticeGeneratedFrom').val();
	var noticeGeneratedTo=$('#noticeGeneratedTo').val();
	
	if(!noticetype){
		bootbox.alert("Please select Notice Type");
		return false;
	}
	else if(!shscNumber && !applicantName  && !mobileNumber
			&& !wardName && !doorNo && !noticeGeneratedFrom && !noticeGeneratedTo)
	{
				bootbox.alert("Please Enter Atleast One Input Value For Searching Along With Notice Type");
				return false;
	}
	else if(noticeGeneratedFrom || noticeGeneratedTo){
		
		if(!noticeGeneratedFrom){
			bootbox.alert("Please enter From date");
			return false;
		}
		else if(!noticeGeneratedTo){
			bootbox.alert("Please enter To date");
			return false;
		}
		console.log(compareDate(noticeGeneratedFrom, noticeGeneratedTo));
		if(compareDate(noticeGeneratedFrom, noticeGeneratedTo)<0){
			bootbox.alert("Please select From Date should be less than To Date");
			return false;
		}
	} 
	return true;
}


	function submitButton() {
		tableContainer = $("#sewerageNoticeSearchResults");
		var noticetype=$('#noticetype').val();
		$('#searchResultDiv').show();
		$.post("/stms/reports/searchResult",$('#sewerageNoticeSearch').serialize())
		.done(function(searchResult) {
		console.log(JSON.stringify(searchResult));
		tableContainer.dataTable({
		destroy : true,
		"sPaginationType" : "bootstrap",
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row'<'col-md-3 col-xs-12'i><'col-md-3 col-xs-6 col-right'l><'col-xs-12 hidden col-md-3 col-right'<'export-data'T>><'col-md-3 col-xs-6 text-right'p>>",
		"aLengthMenu" : [[10,25,50,-1 ],[10,25,50,"All" ] ],
		"autoWidth" : false,
		searchable : true,
		data : searchResult,
		columns : [{title : 'Applicant Name',data : 'resource.searchable.consumername'},
		           {title : 'Notice No',class : 'row-detail',
		           "render": function ( data, type, full, meta ) {
		        	   var data;
		        	   if(full!=null && full.resource!= undefined && full.resource.searchable.estimationnumber != undefined && noticetype =='EM') {
		        			   data = full.resource.searchable.estimationnumber;
		        		   
		           } else {
		        	   if(full!=null && full.resource!= undefined && full.resource.searchable.workordernumber != undefined && noticetype =='WO') {
		        		   data = full.resource.searchable.workordernumber;
		        	   }
		           }
		        			   
		        	   
			            return '<a target="_blank" href="/stms/reports/searchNotices-showSewerageNotice/'+data+'/'+noticetype+'">'+data+'</a>';} },
		           {title : 'Notice Gen Date',
			            	"render": function ( data, type, full, meta ) {
			            		if(full.resource.clauses.workorderdate == undefined){
			            			if(full!=null && full.resource!= undefined && full.resource.clauses.estimationdate != undefined) {
	        							var regDateSplit = full.resource.clauses.estimationdate.split("T")[0].split("-");		
	        							return regDateSplit[2] + "/" + regDateSplit[1] + "/" + regDateSplit[0];
	        						}
			            		}
			            		else {
			            			if(full!=null && full.resource!= undefined && full.resource.clauses.workorderdate != undefined) {
	        							var regDateSplit = full.resource.clauses.workorderdate.split("T")[0].split("-");		
	        							return regDateSplit[2] + "/" + regDateSplit[1] + "/" + regDateSplit[0];
	        						}
			            		}
			            		
			            	}},
		           {title : 'S.H.S.C Number',data : 'resource.searchable.shscnumber'},
		           {title : 'Door No',data : 'resource.clauses.doorno'},
		           {title : 'Address',data : 'resource.searchable.address', width: "25%"}],
		           "aaSorting": [[2, 'asc']]
		});
		
		$('.table-header').show();
		if(tableContainer.fnGetData().length > 1000) {
			$('#searchResultDiv').hide();
			$('#search-exceed-msg').show();
		} else {
			$('#search-exceed-msg').hide();
			$('#searchResultDiv').show();
		}
		});
	}