package ws.loushang;
public class DspSdGovSjxxWsDspSdGovSjxxWsPortTypeProxy implements ws.loushang.DspSdGovSjxxWsDspSdGovSjxxWsPortType {

	private String _endpoint = null;

	private ws.loushang.DspSdGovSjxxWsDspSdGovSjxxWsPortType dspSdGovSjxxWsDspSdGovSjxxWsPortType = null;

	public DspSdGovSjxxWsDspSdGovSjxxWsPortTypeProxy() {
		_initDspSdGovSjxxWsDspSdGovSjxxWsPortTypeProxy();
	}

	public DspSdGovSjxxWsDspSdGovSjxxWsPortTypeProxy(String endpoint) {
		_endpoint = endpoint;
		_initDspSdGovSjxxWsDspSdGovSjxxWsPortTypeProxy();
	}

	private void _initDspSdGovSjxxWsDspSdGovSjxxWsPortTypeProxy() {
		try {
			dspSdGovSjxxWsDspSdGovSjxxWsPortType = (new ws.loushang.DspSdGovSjxxWsDspSdGovSjxxWsLocator())
					.getIDspSdGovSjxxInterfHttpSoap11Endpoint();
			if (dspSdGovSjxxWsDspSdGovSjxxWsPortType != null) {
				if (_endpoint != null)
					((javax.xml.rpc.Stub) dspSdGovSjxxWsDspSdGovSjxxWsPortType)
							._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
				else
					_endpoint = (String) ((javax.xml.rpc.Stub) dspSdGovSjxxWsDspSdGovSjxxWsPortType)
							._getProperty("javax.xml.rpc.service.endpoint.address");
			}
		} catch (javax.xml.rpc.ServiceException serviceException) {
		}
	}

	public String getEndpoint() {
		return _endpoint;
	}

	public void setEndpoint(String endpoint) {
		_endpoint = endpoint;
		if (dspSdGovSjxxWsDspSdGovSjxxWsPortType != null)
			((javax.xml.rpc.Stub) dspSdGovSjxxWsDspSdGovSjxxWsPortType)
					._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
	}

	public ws.loushang.DspSdGovSjxxWsDspSdGovSjxxWsPortType getDspSdGovSjxxWsDspSdGovSjxxWsPortType() {
		if (dspSdGovSjxxWsDspSdGovSjxxWsPortType == null)
			_initDspSdGovSjxxWsDspSdGovSjxxWsPortTypeProxy();
		return dspSdGovSjxxWsDspSdGovSjxxWsPortType;
	}

	public java.lang.String sendApplyNO(java.lang.String args0, java.lang.String args1)
			throws java.rmi.RemoteException {
		if (dspSdGovSjxxWsDspSdGovSjxxWsPortType == null)
			_initDspSdGovSjxxWsDspSdGovSjxxWsPortTypeProxy();
		return dspSdGovSjxxWsDspSdGovSjxxWsPortType.sendApplyNO(args0, args1);
	}
}