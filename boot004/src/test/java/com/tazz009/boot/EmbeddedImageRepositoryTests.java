package com.tazz009.boot;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@DataMongoTest
@Slf4j
public class EmbeddedImageRepositoryTests {

	@Autowired
	ImageRepository repository;

	@Autowired
	MongoOperations operations;
	
	/**
	 * To avoid {@code block()} calls, use blocking
	 * {@link MongoOperations} during setup.
	 */
	@Before
	public void setUp() {
		operations.dropCollection(Image.class);

		operations.insert(new Image("1",
			"learning-spring-boot-cover.jpg"));
		operations.insert(new Image("2",
			"learning-spring-boot-2nd-edition-cover.jpg"));
		operations.insert(new Image("3",
			"bazinga.png"));

		operations.findAll(Image.class).forEach(image -> {
			log.info(image.toString());
		});
	}
	
	@Test
	public void findAllShouldWork() {
		Flux<Image> images = repository.findAll();
		StepVerifier.create(images)
			.recordWith(ArrayList::new)
			.expectNextCount(3)
			.consumeRecordedWith(results -> {
					assertThat(results).hasSize(3);
					assertThat(results)
						.extracting(Image::getName)
						.contains(
							"learning-spring-boot-cover.jpg",
							"learning-spring-boot-2nd-edition-cover.jpg",
							"bazinga.png");
			})
//			.consumeRecordedWith(new Consumer<Collection<Image>>() {
//
//				@Override
//				public void accept(Collection<Image> results) {
//					assertThat(results).hasSize(3);
//					assertThat(results).extracting("name")
//					.contains(
//					"learning-spring-boot-cover.jpg",
//					"learning-spring-boot-2nd-edition-cover.jpg",
//					"bazinga.png");					
//				}
//			})
			.expectComplete()
			.verify();
	}

	@Test
	public void findByNameShouldWork() {
		Mono<Image> image = repository.findByName("bazinga.png");

		StepVerifier.create(image)
			.expectNextMatches(results -> {
					assertThat(results.getName()).isEqualTo("bazinga.png");
					assertThat(results.getId()).isEqualTo("3");
					return true;
			});
//			.expectNextMatches(new Predicate<Image>() {
//
//				@Override
//				public boolean test(Image results) {
//					assertThat(results.getName()).isEqualTo("bazinga.png");
//					assertThat(results.getId()).isEqualTo("3");
//					return true;
//				}
//				
//			});
	}
	
}
