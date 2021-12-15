package com.aem.idea.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;

@Model(adaptables = {Resource.class,SlingHttpServletRequest.class},defaultInjectionStrategy= DefaultInjectionStrategy.OPTIONAL)
public class LogoModel {
Logger log = LoggerFactory.getLogger(LogoModel.class);	

	@Inject @Via("resource")
	private String image;
	
	@ValueMapValue (via = "resource")
	private String altternate;
	
	@Inject @Default (values = "https://www.google.com/")@Via("resource")
	private String link;
	
	@Inject @Via("resource")
	private String target;
	
	@Inject @Named("sling:resourceType") @Via("resource")
	private String rrt;
	
	private String message;
	
	@ScriptVariable
	private Page currentPage;
	
	@SlingObject
	ResourceResolver resourceresolver;
	
	@SlingObject
	Resource resource;
	
    @RequestAttribute
	private String ideaa;
    
    public String getMessage() {
		return message;
	}

	@PostConstruct
	private void init() throws RepositoryException{
		log.debug(":::::::::request is coming in Postconstructor method");
		
		message="hello banglore";
		message+= "this is current page details:::::"+currentPage.getTitle();	
	  resource=resourceresolver.getResource("/content/idea/en/logomodel");
		Node node=resource.adaptTo(Node.class);
		
		log.debug("::::::::::"+node.getName()+"::::::"+node.getPath());
		
	}
	
	public String getIdeaa() {
		return ideaa;
	}

	public String getRrt() {
		return rrt;
	}

	public String getImage() {
		return image;
	}

	public String getAltternate() {
		return altternate;
	}

	public String getLink() {
		return link;
	}

	public String getTarget() {
		return target;
	}
}
