/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.bpa.services.extd.integration.AutoDcr;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.1-b03-
 * Generated source version: 2.0
 * 
 */
@WebServiceClient(name = "Service1", targetNamespace = "http://tempuri.org/", wsdlLocation = "http://218.248.24.71/AutoDCRBPSService/AutoDCRDetails.asmx?wsdl")
public class Service1
    extends Service
{

    private final static URL SERVICE1_WSDL_LOCATION;

    static {
        URL url = null;
        try {
            url = new URL("http://218.248.24.71/AutoDCRBPSService/AutoDCRDetails.asmx?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        SERVICE1_WSDL_LOCATION = url;
    }

    public Service1(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public Service1() {
        super(SERVICE1_WSDL_LOCATION, new QName("http://tempuri.org/", "Service1"));
    }

    /**
     * 
     * @return
     *     returns Service1Soap
     */
    @WebEndpoint(name = "Service1Soap")
    public Service1Soap getService1Soap() {
        return (Service1Soap)super.getPort(new QName("http://tempuri.org/", "Service1Soap"), Service1Soap.class);
    }

    /**
     * 
     * @return
     *     returns Service1Soap
     */
    @WebEndpoint(name = "Service1Soap12")
    public Service1Soap getService1Soap12() {
        return (Service1Soap)super.getPort(new QName("http://tempuri.org/", "Service1Soap12"), Service1Soap.class);
    }

    /**
     * 
     * @return
     *     returns Service1HttpGet
     */
    @WebEndpoint(name = "Service1HttpGet")
    public Service1HttpGet getService1HttpGet() {
        return (Service1HttpGet)super.getPort(new QName("http://tempuri.org/", "Service1HttpGet"), Service1HttpGet.class);
    }

    /**
     * 
     * @return
     *     returns Service1HttpPost
     */
    @WebEndpoint(name = "Service1HttpPost")
    public Service1HttpPost getService1HttpPost() {
        return (Service1HttpPost)super.getPort(new QName("http://tempuri.org/", "Service1HttpPost"), Service1HttpPost.class);
    }

}