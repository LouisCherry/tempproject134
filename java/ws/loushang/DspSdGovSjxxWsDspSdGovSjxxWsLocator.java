/**
 * DspSdGovSjxxWsDspSdGovSjxxWsLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package ws.loushang;
public class DspSdGovSjxxWsDspSdGovSjxxWsLocator extends org.apache.axis.client.Service
		implements
			ws.loushang.DspSdGovSjxxWsDspSdGovSjxxWs {

	private static final long serialVersionUID = -7960057416769810395L;

	public DspSdGovSjxxWsDspSdGovSjxxWsLocator() {
	}

	public DspSdGovSjxxWsDspSdGovSjxxWsLocator(org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public DspSdGovSjxxWsDspSdGovSjxxWsLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName)
			throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	// Use to get a proxy class for IDspSdGovSjxxInterfHttpSoap11Endpoint
	private java.lang.String IDspSdGovSjxxInterfHttpSoap11Endpoint_address = "http://59.206.216.20:8000/sdfda/services/dspSdGovSjxxWs.dspSdGovSjxxWs.IDspSdGovSjxxInterfHttpSoap11Endpoint/";

	public java.lang.String getIDspSdGovSjxxInterfHttpSoap11EndpointAddress() {
		return IDspSdGovSjxxInterfHttpSoap11Endpoint_address;
	}

	// The WSDD service name defaults to the port name.
	private java.lang.String IDspSdGovSjxxInterfHttpSoap11EndpointWSDDServiceName = "IDspSdGovSjxxInterfHttpSoap11Endpoint";

	public java.lang.String getIDspSdGovSjxxInterfHttpSoap11EndpointWSDDServiceName() {
		return IDspSdGovSjxxInterfHttpSoap11EndpointWSDDServiceName;
	}

	public void setIDspSdGovSjxxInterfHttpSoap11EndpointWSDDServiceName(java.lang.String name) {
		IDspSdGovSjxxInterfHttpSoap11EndpointWSDDServiceName = name;
	}

	public ws.loushang.DspSdGovSjxxWsDspSdGovSjxxWsPortType getIDspSdGovSjxxInterfHttpSoap11Endpoint()
			throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(IDspSdGovSjxxInterfHttpSoap11Endpoint_address);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getIDspSdGovSjxxInterfHttpSoap11Endpoint(endpoint);
	}

	public ws.loushang.DspSdGovSjxxWsDspSdGovSjxxWsPortType getIDspSdGovSjxxInterfHttpSoap11Endpoint(
			java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
		try {
			ws.loushang.IDspSdGovSjxxInterfSoap11BindingStub _stub = new ws.loushang.IDspSdGovSjxxInterfSoap11BindingStub(
					portAddress, this);
			_stub.setPortName(getIDspSdGovSjxxInterfHttpSoap11EndpointWSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setIDspSdGovSjxxInterfHttpSoap11EndpointEndpointAddress(java.lang.String address) {
		IDspSdGovSjxxInterfHttpSoap11Endpoint_address = address;
	}

	/**
	 * For the given interface, get the stub implementation.
	 * If this service has no port for the given interface,
	 * then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		try {
			if (ws.loushang.DspSdGovSjxxWsDspSdGovSjxxWsPortType.class.isAssignableFrom(serviceEndpointInterface)) {
				ws.loushang.IDspSdGovSjxxInterfSoap11BindingStub _stub = new ws.loushang.IDspSdGovSjxxInterfSoap11BindingStub(
						new java.net.URL(IDspSdGovSjxxInterfHttpSoap11Endpoint_address), this);
				_stub.setPortName(getIDspSdGovSjxxInterfHttpSoap11EndpointWSDDServiceName());
				return _stub;
			}
		} catch (java.lang.Throwable t) {
			throw new javax.xml.rpc.ServiceException(t);
		}
		throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  "
				+ (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
	}

	/**
	 * For the given interface, get the stub implementation.
	 * If this service has no port for the given interface,
	 * then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface)
			throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		java.lang.String inputPortName = portName.getLocalPart();
		if ("IDspSdGovSjxxInterfHttpSoap11Endpoint".equals(inputPortName)) {
			return getIDspSdGovSjxxInterfHttpSoap11Endpoint();
		} else {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	public javax.xml.namespace.QName getServiceName() {
		return new javax.xml.namespace.QName("http://loushang.ws", "dspSdGovSjxxWs.dspSdGovSjxxWs");
	}

	private java.util.HashSet ports = null;

	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new javax.xml.namespace.QName("http://loushang.ws", "IDspSdGovSjxxInterfHttpSoap11Endpoint"));
		}
		return ports.iterator();
	}

	/**
	* Set the endpoint address for the specified port name.
	*/
	public void setEndpointAddress(java.lang.String portName, java.lang.String address)
			throws javax.xml.rpc.ServiceException {
		if ("IDspSdGovSjxxInterfHttpSoap11Endpoint".equals(portName)) {
			setIDspSdGovSjxxInterfHttpSoap11EndpointEndpointAddress(address);
		} else { // Unknown Port Name
			throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}

	/**
	* Set the endpoint address for the specified port name.
	*/
	public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address)
			throws javax.xml.rpc.ServiceException {
		setEndpointAddress(portName.getLocalPart(), address);
	}
}
