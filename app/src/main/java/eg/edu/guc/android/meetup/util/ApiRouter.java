package eg.edu.guc.android.meetup.util;

import java.util.Date;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.converter.GsonConverter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ApiRouter {
	private static final String API_BASE_URL = "http://meetup-mohaazeem.c9users.io/api";

	private static PublicApiRoutes publicRouter;
	private static PrivateApiRoutes privateRouter;

	public static PublicApiRoutes withoutToken() {
		if (publicRouter == null) {
			publicRouter = buildRouter(PublicApiRoutes.class, null);
		}

		return publicRouter;
	}

	public static PrivateApiRoutes withToken(String token) {
		if (privateRouter == null) {
			privateRouter = buildRouter(PrivateApiRoutes.class, new PrivateApiInterceptor(token));
		}

		return privateRouter;
	}

	private static <T> T buildRouter(Class<T> T, RequestInterceptor requestInterceptor) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
		gsonBuilder.registerTypeAdapter(Date.class, new ApiDateTypeAdapter());
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();

		RestAdapter.Builder restAdapterBuilder = new RestAdapter.Builder();
		restAdapterBuilder.setEndpoint(API_BASE_URL);
		if (requestInterceptor != null) {
			restAdapterBuilder.setRequestInterceptor(requestInterceptor);
		}
		restAdapterBuilder.setConverter(new GsonConverter(gson));

		RestAdapter restAdapter = restAdapterBuilder.build();
		restAdapter.setLogLevel(LogLevel.FULL);
		return restAdapter.create(T);
	}
}
