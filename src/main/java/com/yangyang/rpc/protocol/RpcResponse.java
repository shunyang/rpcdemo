package com.yangyang.rpc.protocol;
/**
 * RPC响应
 */
public class RpcResponse {
	private String requestId;
	private Object result;
	private Throwable error;
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public Throwable getError() {
		return error;
	}
	public void setError(Throwable error) {
		this.error = error;
	}
	
	public boolean isError(){
		if (error != null) {
			return true;
		}
		return false;
	}
}
