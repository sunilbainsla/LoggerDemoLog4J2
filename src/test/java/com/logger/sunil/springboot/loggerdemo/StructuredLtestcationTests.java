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

public class SoapRequestInterceptor extends ClientInterceptorAdapter {

	@Override
	public boolean handleRequest(MessageContext messageContext) throws IOException {
		SoapMessage soapMessage = (SoapMessage) messageContext.getRequest();
		SoapHeaderElement messageId = soapMessage.getSoapHeader().examineHeaderElements("MessageID").next();
		String messageIdValue = messageId.getText();
		System.out.println("Sending SOAP request message with MessageID: " + messageIdValue);
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext) throws IOException {
		SoapMessage soapMessage = (SoapMessage) messageContext.getResponse();
		SoapHeaderElement messageId = soapMessage.getSoapHeader().examineHeaderElements("MessageID").next();
		String messageIdValue = messageId.getText();
		System.out.println("Received SOAP response message with MessageID: " + messageIdValue);
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext) throws IOException {
		SoapMessage soapMessage = (SoapMessage) messageContext.getResponse();
		SoapHeaderElement messageId = soapMessage.getSoapHeader().examineHeaderElements("MessageID").next();
		String messageIdValue = messageId.getText();
		System.out.println("Received SOAP fault message with MessageID: " + messageIdValue);
		return true;
	}
}
