package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;

public class BaseRegisterVLTResult {
	
	   	private String assessmentNo;
	    private String ward;
	   	private String ownerName;
	   	private String surveyNo;
	   	private BigDecimal taxationRate;
		private BigDecimal marketValue;
		private BigDecimal documentValue;
		private BigDecimal higherValueForImposedtax;
	    private BigDecimal propertyTaxFirstHlf;
	    private BigDecimal libraryCessTaxFirstHlf;
	    private BigDecimal propertyTaxSecondHlf;
	    private BigDecimal libraryCessTaxSecondHlf;
	    private BigDecimal currTotal;
	    private BigDecimal penaltyFines;
	    private String arrearPeriod;
	    private BigDecimal arrearPropertyTax;
	    private BigDecimal arrearLibraryTax;
	    private BigDecimal arrearPenaltyFines;
	    private BigDecimal arrearTotal;
	    
		public String getAssessmentNo() {
			return assessmentNo;
		}
		public void setAssessmentNo(String assessmentNo) {
			this.assessmentNo = assessmentNo;
		}
		public String getWard() {
			return ward;
		}
		public void setWard(String ward) {
			this.ward = ward;
		}
		public String getOwnerName() {
			return ownerName;
		}
		public void setOwnerName(String ownerName) {
			this.ownerName = ownerName;
		}
		public String getSurveyNo() {
			return surveyNo;
		}
		public void setSurveyNo(String surveyNo) {
			this.surveyNo = surveyNo;
		}
		public BigDecimal getTaxationRate() {
			return taxationRate;
		}
		public void setTaxationRate(BigDecimal taxationRate) {
			this.taxationRate = taxationRate;
		}
		public BigDecimal getMarketValue() {
			return marketValue;
		}
		public void setMarketValue(BigDecimal marketValue) {
			this.marketValue = marketValue;
		}
		public BigDecimal getDocumentValue() {
			return documentValue;
		}
		public void setDocumentValue(BigDecimal documentValue) {
			this.documentValue = documentValue;
		}
		public BigDecimal getHigherValueForImposedtax() {
			return higherValueForImposedtax;
		}
		public void setHigherValueForImposedtax(BigDecimal higherValueForImposedtax) {
			this.higherValueForImposedtax = higherValueForImposedtax;
		}
		public BigDecimal getPropertyTaxFirstHlf() {
			return propertyTaxFirstHlf;
		}
		public void setPropertyTaxFirstHlf(BigDecimal propertyTaxFirstHlf) {
			this.propertyTaxFirstHlf = propertyTaxFirstHlf;
		}
		public BigDecimal getLibraryCessTaxFirstHlf() {
			return libraryCessTaxFirstHlf;
		}
		public void setLibraryCessTaxFirstHlf(BigDecimal libraryCessTaxFirstHlf) {
			this.libraryCessTaxFirstHlf = libraryCessTaxFirstHlf;
		}
		public BigDecimal getPropertyTaxSecondHlf() {
			return propertyTaxSecondHlf;
		}
		public void setPropertyTaxSecondHlf(BigDecimal propertyTaxSecondHlf) {
			this.propertyTaxSecondHlf = propertyTaxSecondHlf;
		}
		public BigDecimal getLibraryCessTaxSecondHlf() {
			return libraryCessTaxSecondHlf;
		}
		public void setLibraryCessTaxSecondHlf(BigDecimal libraryCessTaxSecondHlf) {
			this.libraryCessTaxSecondHlf = libraryCessTaxSecondHlf;
		}
		public BigDecimal getCurrTotal() {
			return currTotal;
		}
		public void setCurrTotal(BigDecimal currTotal) {
			this.currTotal = currTotal;
		}
		public BigDecimal getPenaltyFines() {
			return penaltyFines;
		}
		public void setPenaltyFines(BigDecimal penaltyFines) {
			this.penaltyFines = penaltyFines;
		}
		public String getArrearPeriod() {
			return arrearPeriod;
		}
		public void setArrearPeriod(String arrearPeriod) {
			this.arrearPeriod = arrearPeriod;
		}
		public BigDecimal getArrearPropertyTax() {
			return arrearPropertyTax;
		}
		public void setArrearPropertyTax(BigDecimal arrearPropertyTax) {
			this.arrearPropertyTax = arrearPropertyTax;
		}
		public BigDecimal getArrearLibraryTax() {
			return arrearLibraryTax;
		}
		public void setArrearLibraryTax(BigDecimal arrearLibraryTax) {
			this.arrearLibraryTax = arrearLibraryTax;
		}
		public BigDecimal getArrearPenaltyFines() {
			return arrearPenaltyFines;
		}
		public void setArrearPenaltyFines(BigDecimal arrearPenaltyFines) {
			this.arrearPenaltyFines = arrearPenaltyFines;
		}
		public BigDecimal getArrearTotal() {
			return arrearTotal;
		}
		public void setArrearTotal(BigDecimal arrearTotal) {
			this.arrearTotal = arrearTotal;
		}
	    
}