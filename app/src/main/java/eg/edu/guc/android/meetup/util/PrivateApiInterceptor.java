package eg.edu.guc.android.meetup.util;

import retrofit.RequestInterceptor;

public class PrivateApiInterceptor implements RequestInterceptor {
	private String token;

	public PrivateApiInterceptor(String token) {
		this.token = token;
	}

	@Override
	public void intercept(RequestFacade request) {
		request.addHeader("Authorization", token);
	}
}
