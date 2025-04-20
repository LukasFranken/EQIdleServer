package base.game;

import org.springframework.web.reactive.function.client.WebClient;

public class GameServiceInterface {
	
	private WebClient gameServiceClient;
	
	public GameServiceInterface(String gameServiceAddress, int gameServicePort) {
		gameServiceClient = WebClient.builder()
				.codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1 * 1024 * 1024))
				.baseUrl("http://" + gameServiceAddress + ":" + gameServicePort)
			    .build();
	}

}
