package com.tazz009.boot;

import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class InitDatabase {
	@Bean
	CommandLineRunner init(MongoOperations operations) {
		return args -> {
			operations.dropCollection(Image.class);

			operations.insert(new Image(UUID.randomUUID().toString(),
				"btn_play_n.png"));
			operations.insert(new Image(UUID.randomUUID().toString(),
				"btn_search_1_n.png"));
			operations.insert(new Image(UUID.randomUUID().toString(),
				"btn_stop_n.png"));

			operations.findAll(Image.class).forEach(image -> {
				System.out.println(image.toString());
			});
		};
	}
}
