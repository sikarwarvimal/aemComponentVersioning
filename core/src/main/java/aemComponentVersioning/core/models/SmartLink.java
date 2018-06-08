package aemComponentVersioning.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/***
 *
 * This is a Sling Model Class which is being adapted as SmartLink node resource.
 * This class has logic for AEM Component Smart Link.
 * @author VimalKumar
 *
 */
@Model(adaptables = Resource.class)
public class SmartLink {

    private static final Logger LOG = LoggerFactory.getLogger(SmartLink.class);
    private String linkUrl;
    private String styleClasses;

    @Inject
    public SmartLink(Resource resource) {
        final ValueMap valueMap = resource.adaptTo(ValueMap.class);

        final String linkUrlValue = valueMap.get("linkUrl", String.class);
        final Boolean isButtonValue = valueMap.get("isButton", Boolean.class);
        final String buttonSizeValue = valueMap.get("buttonSize", String.class);

        this.linkUrl = linkUrlValue;

        if(isButtonValue != null && isButtonValue) {
            this.styleClasses = "btn " +buttonSizeValue;
        }
    }

    public String getLinkUrl() {
        if (this.linkUrl != null && this.linkUrl.startsWith("/content")) {
            return this.linkUrl + ".html";
        } else {
            return this.linkUrl;
        }
    }

    public String getStyleClasses() {
        return this.styleClasses;
    }
}
