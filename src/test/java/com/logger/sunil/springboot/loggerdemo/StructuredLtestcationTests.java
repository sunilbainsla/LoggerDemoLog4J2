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


	import org.junit.Test;
			import org.mockito.ArgumentCaptor;
			import org.mockito.Captor;
			import org.mockito.MockitoAnnotations;
			import org.springframework.ws.client.core.WebServiceTemplate;
			import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
			import org.springframework.ws.context.MessageContext;
			import org.springframework.ws.soap.SoapMessage;
			import static org.mockito.Mockito.*;
		import static org.junit.Assert.*;

public class SoapRequestInterceptorTest {

	@Captor
	private ArgumentCaptor<MessageContext> messageContextCaptor;

	@Test
	public void testSoapRequestInterceptor() throws Exception {
		// Mock the soap message and message context
		SoapMessage soapMessage = mock(SoapMessage.class);
		MessageContext messageContext = mock(MessageContext.class);
		when(messageContext.getRequest()).thenReturn(soapMessage);

		// Create an instance of SoapRequestInterceptor
		SoapRequestInterceptor soapRequestInterceptor = new SoapRequestInterceptor();

		// Call handleRequest method
		boolean result = soapRequestInterceptor.handleRequest(messageContext);
		assertTrue(result);

		// Verify that the SOAP request is logged
		verify(soapMessage, times(1)).getEnvelope();
		verify(messageContext, times(1)).getRequest();
		verify(messageContext, times(1)).setRequest(any(SoapMessage.class));
	}

}
