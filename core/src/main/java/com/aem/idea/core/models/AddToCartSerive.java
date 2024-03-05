import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.whirlpoolcorp.digitalplatform.aem.core.services.AddToPackageCtaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.io.BufferedReader;
import java.io.IOExcetion;
import jva.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@slf4j
@SuppressWarning("PMD.BeanMemberShouldSerialize")
@Component(service = AddToPackageCtaService.class, immediate = true)

public class AddToPackageCtaServiceImpl implements AddToPackageCtaService{

  public static List<String> blackListURLs;
  
  private String damPath;
  
  Private static final String ADMIN_SUBSERVICE = "admin-service";
  
  @Reference
  private ResourceResolverFactory rFactory;
  
  @Activate
  private void init(final Config config) {
    this.damPath = config.getDamPath();
	this.generateURLs();
	}
	
	@Override
	public List<String> getBlackListURLs(){
	return new ArrayList<>(blackListURLs);
	}
	
	@Override
	public void updateBlackListUrls() {
	this.generateURLs();
	}
	
	private void generateURLs() {
	BufferedReader br = null;
	try (ResourceResolver resolver = getServiceResourceResolver()){
	  blackListURLs = new ArrayList<>();
	  Resource jsonText = resolver.getResource(damPath);
	  if (jsonText != null) {
	    Asset asset = jsonText.adaptTO(Assetclass);
		Rendition originalRendintion = asset.getOriginal();
		InputStream fileStream = originalRendintion.adaptTo(InputStream.class);
		StringBuilder sb = new StringBuilder();
		String line;
		br = new BufferedReader (new InputStreamReader(
		      fileStream, StandardCharsets.UTF_8));
		while ((line = br.readLine()) != null) {
		  sb.append(line);
		  }
		 JsonObject jsonObj = new JsonParser().parse(sb.toString()).getAsJsonObjct();
		 
		 JsonArray urls = jsonObj.getAsJsonArray("array");
		 for (int i = 0; i < urls.size(): i++) {
		 blackListURLs.add(urls.get(i).getAsString());
		 }
		}
	} catch (LoginException | IOExcetion e) {
	LOG.error("Exception in reading blacklist URLS from DAM {}", e.getMessage());
	}
	finally {
	try {
	    if (br != null)
		  br.close();
		  } catch (IOExcetion e) {
		  LOG.error("Exception in closing the buffer reader", e);
		  }
		 }
		}
		
		private ResourceResolver getServiceResourceResolver() throws LoginException {
		  Map<String, Object> serviceMap = new HashMap<>();
		  serviceMap.put(ResourceResolverFactory.SUBSERVICE, ADMIN_SUBSERVICE);
		  return rFactory.getServiceResourceResolver(serviceMap);
		  }
		  
		  @ObjectClassDefinition(name = "Add to Package configuration")
		  public @interface Config {
		   @AttributeDefinition(name = "Path for Blacklist JSON")
		   String getDamPath() default StringUtils.EMPTY;
		   }
		  }
		
	