package com.fqwl.hycommonsdk.util.http;
/**
 * HTTP状态码
 * @author fan
 *
 */
public enum HttpStatus {

	NotAccess(0), //
	Continue(100), // 
	SwitchingProtocols(101), //
	OK(200), // 
	Created(201), // 
	Accepted(202), // 
	NonAuthoritativeInformation(203), //
	NoContent(204), // 
	ResetContent(205), // 
	PartialContent(206), //
	MultipleChoices(300), // 
	Ambiguous(300), //
	MovedPermanently(301), // 
	Moved(301), //
	Found(302), //
	Redirect(302), // 
	SeeOther(303), // 
	RedirectMethod(303), // 
	NotModified(304), //
	UseProxy(305), // 
	Unused(306), //
	TemporaryRedirect(307), // 
	RedirectKeepVerb(307), //
	BadRequest(400), // 
	Unauthorized(401), // 
	PaymentRequired(402), //
	Forbidden(403), // 
	NotFound(404), // 
	MethodNotAllowed(405), //
	NotAcceptable(406), //
	ProxyAuthenticationRequired(407), // 
	RequestTimeout(408), //
	Conflict(409), //
	Gone(410), //
	LengthRequired(411), // 
	PreconditionFailed(412), //
	RequestEntityTooLarge(413), //
	RequestUriTooLong(414), // 
	UnsupportedMediaType(415), //
	RequestedRangeNotSatisfiable(416), // 
	ExpectationFailed(417), // 
	InternalServerError(500), // 
	NotImplemented(501), // 
	BadGateway(502), // 
	ServiceUnavailable(503), // 
	GatewayTimeout(504), //
	HttpVersionNotSupported(505);

	private int Code;

	private HttpStatus(int code) {
		this.Code = code;
	}

	public int getCode() {
		return this.Code;
	}

	public static HttpStatus valueOf(int value) {
		switch (value) {
		case 0:
			return NotAccess;
		case 100:
			return Continue;
		case 101:
			return SwitchingProtocols;
		case 200:
			return OK;
		case 201:
			return Created;
		case 202:
			return Accepted;
		case 203:
			return NonAuthoritativeInformation;
		case 204:
			return NoContent;
		case 205:
			return ResetContent;
		case 206:
			return PartialContent;
		case 300:
			return MultipleChoices;
		case 301:
			return MovedPermanently;
		case 302:
			return Found;
		case 303:
			return SeeOther;
		case 304:
			return NotModified;
		case 305:
			return UseProxy;
		case 306:
			return Unused;
		case 307:
			return TemporaryRedirect;
		case 400:
			return BadRequest;
		case 401:
			return Unauthorized;
		case 402:
			return PaymentRequired;
		case 403:
			return Forbidden;
		case 404:
			return NotFound;
		case 405:
			return MethodNotAllowed;
		case 406:
			return NotAcceptable;
		case 407:
			return ProxyAuthenticationRequired;
		case 408:
			return RequestTimeout;
		case 409:
			return Conflict;
		case 410:
			return Gone;
		case 411:
			return LengthRequired;
		case 412:
			return PreconditionFailed;
		case 413:
			return RequestEntityTooLarge;
		case 414:
			return RequestUriTooLong;
		case 415:
			return UnsupportedMediaType;
		case 416:
			return RequestedRangeNotSatisfiable;
		case 417:
			return ExpectationFailed;
		case 500:
			return InternalServerError;
		case 501:
			return NotImplemented;
		case 502:
			return BadGateway;
		case 503:
			return ServiceUnavailable;
		case 504:
			return GatewayTimeout;
		case 505:
			return HttpVersionNotSupported;
		default:
			return null;
		}
	}

}
