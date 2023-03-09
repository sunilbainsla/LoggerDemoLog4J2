import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.support.interceptor.ClientInterceptorAdapter;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapMessage;

public class SoapRequestInterceptor extends ClientInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(SoapRequestInterceptor.class);

	@Override
	public boolean handleRequest(MessageContext messageContext) {
		SoapMessage soapMessage = (SoapMessage) messageContext.getRequest();
		String soapRequest = convertSoapMessageToString(soapMessage);

		// Log the SOAP request
		logger.debug("SOAP request: {}", soapRequest);

		return super.handleRequest(messageContext);
	}

	@Override
	public boolean handleResponse(MessageContext messageContext) {
		SoapMessage soapMessage = (SoapMessage) messageContext.getResponse();
		String soapResponse = convertSoapMessageToString(soapMessage);

		// Log the SOAP response
		logger.debug("SOAP response: {}", soapResponse);

		return super.handleResponse(messageContext);
	}

	private String convertSoapMessageToString(SoapMessage soapMessage) {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerException e) {
			throw new RuntimeException("Failed to create transformer", e);
		}
		StringWriter writer = new StringWriter();
		try {
			transformer.transform(new DOMSource(soapMessage.getEnvelope().getSource()), new StreamResult(writer));
		} catch (TransformerException e) {
			throw new RuntimeException("Failed to transform SOAP message", e);
		}
		return writer.toString();
	}
}


	@Override
	public boolean handleResponse(MessageContext messageContext) {
		SoapMessage soapMessage = (SoapMessage) messageContext.getResponse();
		String soapResponse = convertSoapMessageToString(soapMessage);

		// Get the HTTP response code
		Integer httpResponseCode = (Integer) messageContext.getResponse().getProperty(MessageContext.HTTP_RESPONSE_CODE);
		if (httpResponseCode != null) {
			// Add the HTTP response code to the MDC
			MDC.put("httpResponseCode", httpResponseCode.toString());
		}

		// Log the SOAP response
		logger.debug("SOAP response: {}", soapResponse);

		return super.handleResponse(messageContext);
	}
