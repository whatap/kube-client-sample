package whatap.lang.http;

import whatap.util.IntKeyLinkedMap;

public class HttpStatus {
	static IntKeyLinkedMap<String> statMsg = new IntKeyLinkedMap<String>();
	static {
		statMsg.put(100, "ontinue");
		statMsg.put(101, "Switching Protocols");
		statMsg.put(102, "Processing");
		statMsg.put(103, "Checkpoint");
		statMsg.put(200, "OK");
		statMsg.put(201, "Created");
		statMsg.put(202, "Accepted");
		statMsg.put(203, "Non-Authoritative Information");
		statMsg.put(204, "No Content");
		statMsg.put(205, "Reset Content");
		statMsg.put(206, "Partial Content");
		statMsg.put(207, "Multi-Status");
		statMsg.put(208, "Already Reported");
		statMsg.put(226, "IM Used");
		statMsg.put(300, "Multiple Choices");
		statMsg.put(301, "Moved Permanently");
		statMsg.put(302, "Found");
		statMsg.put(302, "Moved Temporarily");
		statMsg.put(303, "See Other");
		statMsg.put(304, "Not Modified");
		statMsg.put(305, "Use Proxy");
		statMsg.put(307, "Temporary Redirect");
		statMsg.put(308, "Permanent Redirect");
		statMsg.put(400, "Bad Request");
		statMsg.put(401, "Unauthorized");
		statMsg.put(402, "Payment Required");
		statMsg.put(403, "Forbidden");
		statMsg.put(404, "Not Found");
		statMsg.put(405, "Method Not Allowed");
		statMsg.put(406, "Not Acceptable");
		statMsg.put(407, "Proxy Authentication Required");
		statMsg.put(408, "Request Timeout");
		statMsg.put(409, "Conflict");
		statMsg.put(410, "Gone");
		statMsg.put(411, "Length Required");
		statMsg.put(412, "Precondition failed");
		statMsg.put(413, "Payload Too Large");
		statMsg.put(413, "Request Entity Too Large");
		statMsg.put(414, "URI Too Long");
		statMsg.put(414, "Request-URI Too Long");
		statMsg.put(415, "Unsupported Media Type");
		statMsg.put(416, "Requested Range Not Satisfiable");
		statMsg.put(417, "Expectation Failed");
		statMsg.put(418, "I'm a teapot");
		statMsg.put(422, "Unprocessable Entity");
		statMsg.put(423, "Locked");
		statMsg.put(424, "Failed Dependency");
		statMsg.put(425, "Too Early");
		statMsg.put(426, "Upgrade Required");
		statMsg.put(428, "Precondition Required");
		statMsg.put(429, "Too Many Requests");
		statMsg.put(431, "Request Header Fields Too Large");
		statMsg.put(451, "Unavailable For Legal Reasons");
		statMsg.put(500, "Internal Server Error");
		statMsg.put(501, "Not Implemented");
		statMsg.put(502, "Bad Gateway");
		statMsg.put(503, "Service Unavailable");
		statMsg.put(504, "Gateway Timeout");
		statMsg.put(505, "HTTP Version Not Supported");
		statMsg.put(506, "Variant Also Negotiates}");
		statMsg.put(507, "Insufficient Storage}");
		statMsg.put(508, "Loop Detected}");
		statMsg.put(509, "Bandwidth Limit Exceeded}");
		statMsg.put(510, "Not Extended}");
		statMsg.put(511, "Network Authentication Required");
	}

	public static String reason(int code) {
		return statMsg.get(code);
	}

	public static String reason(int code, String def) {
		String msg = statMsg.get(code);
		return msg != null ? msg : def;
	}
}
