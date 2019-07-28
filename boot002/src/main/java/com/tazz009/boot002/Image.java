package com.tazz009.boot002;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Image {

	private int id;
	private String name;
	
	public Image(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
}
