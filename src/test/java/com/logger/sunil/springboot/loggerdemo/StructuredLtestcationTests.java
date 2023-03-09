@Configuration
public class SoapClientConfig {

	@Bean
	public SoapRequestInterceptor soapRequestInterceptor() {
		return new SoapRequestInterceptor();
	}

	@Bean
	public WebServiceTemplate webServiceTemplate() {
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
		webServiceTemplate.setInterceptors(new ClientInterceptor[] { soapRequestInterceptor() });
		return webServiceTemplate;
	}
}



import org.springframework.ws.client.support.interceptor.ClientInterceptorAdapter;
		import org.springframework.ws.context.MessageContext;
		import org.springframework.ws.soap.SoapHeaderElement;
		import org.springframework.ws.soap.SoapMessage;
		import org.springframework.ws.soap.client.core.SoapActionCallback;

		import org.slf4j.MDC;
		import org.springframework.ws.client.support.interceptor.ClientInterceptorAdapter;
		import org.springframework.ws.context.MessageContext;
		import org.springframework.ws.soap.SoapHeaderElement;
		import org.springframework.ws.soap.SoapMessage;

public class SoapRequestInterceptor extends ClientInterceptorAdapter {

	@Override
	public boolean handleRequest(MessageContext messageContext) throws IOException {
		SoapMessage soapMessage = (SoapMessage) messageContext.getRequest();
		SoapHeaderElement messageIdHeader = soapMessage.getSoapHeader().examineHeaderElements("MessageID").next();
		String messageIdValue = messageIdHeader.getText();

		// Add MessageID value to MDC context
		MDC.put("MessageID", messageIdValue);

		// Log SOAP request message
		String logMessage = "Sending SOAP request message with MessageID: " + messageIdValue;
		System.out.println(logMessage);
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext) throws IOException {
		SoapMessage soapMessage = (SoapMessage) messageContext.getResponse();
		SoapHeaderElement messageIdHeader = soapMessage.getSoapHeader().examineHeaderElements("MessageID").next();
		String messageIdValue = messageIdHeader.getText();

		// Add MessageID value to MDC context
		MDC.put("MessageID", messageIdValue);

		// Log SOAP response message
		String logMessage = "Received SOAP response message with MessageID: " + messageIdValue;
		System.out.println(logMessage);
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext) throws IOException {
		SoapMessage soapMessage = (SoapMessage) messageContext.getResponse();
		SoapHeaderElement messageIdHeader = soapMessage.getSoapHeader().examineHeaderElements("MessageID").next();
		String messageIdValue = messageIdHeader.getText();

		// Add MessageID value to MDC context
		MDC.put("MessageID", messageIdValue);

		// Log SOAP fault message
		String logMessage = "Received SOAP fault message with MessageID: " + messageIdValue;
		System.out.println(logMessage);
		return true;
	}
}

}
