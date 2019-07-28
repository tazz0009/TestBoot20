package com.tazz009.boot;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Image {

	@Id
	final private String id;
	final private String name;
	
}
