package com.aem.idea.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class)
public class ImagetitleModel {
	
	@Inject
   private String image;
	
	@Inject
	private String altternate;
	
	@Inject
	private String title;
	
	@Inject
	private String description;

	public String getImage() {
		return image;
	}

	public String getAltternate() {
		return altternate;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

}
