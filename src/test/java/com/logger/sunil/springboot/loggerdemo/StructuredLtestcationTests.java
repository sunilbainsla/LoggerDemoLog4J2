public class SoapLoggingHandler implements SOAPHandler<SOAPMessageContext> {

	private static final String MDC_PROTOCOL_KEY = "requestProtocol";
	private static final String MDC_ACTION_KEY = "soapAction";

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean isOutbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (!isOutbound) {
			// incoming message
			try {
				SOAPMessage message = context.getMessage();
				SOAPBody body = message.getSOAPBody();
				String action = context.getSOAPAction();
				MDC.put(MDC_ACTION_KEY, action);
				MDC.put(MDC_PROTOCOL_KEY, "SOAP");
			} catch (SOAPException e) {
				e.printStackTrace();
			}
		} else {
			// outgoing message
			MDC.put(MDC_PROTOCOL_KEY, "SOAP");
		}
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public void close(MessageContext context) {
	}
}







......

		SoapLoggingHandler soapHandler = new SoapLoggingHandler();
		BindingProvider bindingProvider = (BindingProvider) port;
		bindingProvider.getBinding().setHandlerChain(Collections.singletonList(soapHandler));
