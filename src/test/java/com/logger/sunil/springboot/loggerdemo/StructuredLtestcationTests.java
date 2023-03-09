public void sendAndReceive(Object request) {
		MyMessageCallback callback = new MyMessageCallback();
		List<EndpointInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new MyEndpointInterceptor());
		getWebServiceTemplate().setInterceptors(interceptors.toArray(new EndpointInterceptor[interceptors.size()]));
		getWebServiceTemplate().marshalSendAndReceive(request, callback);
		}

		}




public class MyEndpointInterceptor extends AbstractEndpointInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(MyEndpointInterceptor.class);

	@Override
	public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
		MDC.put("requestId", messageContext.getRequest().getPayloadSource().toString());
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
		String requestId = MDC.get("requestId");
		logger.info("SOAP response for request {}: {}", requestId, messageContext.getResponse().getPayloadSource().toString());
		return true;
	}

}

public class MyEndpointInterceptor extends AbstractEndpointInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(MyEndpointInterceptor.class);

	@Override
	public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {
		MDC.put("requestId", messageContext.getRequest().getPayloadSource().toString());
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
		String requestId = MDC.get("requestId");
		logger.info("SOAP response for request {}: {}", requestId, messageContext.getResponse().getPayloadSource().toString());
		return true;
	}

}
