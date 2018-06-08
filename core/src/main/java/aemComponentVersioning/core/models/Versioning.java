package aemComponentVersioning.core.models;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/***
 *
 * This is a Sling Model Class which is being adapted as AEM Component node resource.
 * This class has logic to update the component version based on the selected version number.
 * "sling:resourceType" version change logic has been implemented.
 * @author VimalKumar
 *
 */

@Model(adaptables = Resource.class)
public class Versioning {

    private static final Logger LOG = LoggerFactory.getLogger(Versioning.class);

    @Inject
    public Versioning(final Resource resource) {
        final ValueMap valueMap = resource.adaptTo(ValueMap.class);
        final String versionNumber = valueMap.get("versionNumber", String.class);
        final String slingResourceType = valueMap.get("sling:resourceType", String.class);

        final String path = slingResourceType.substring(0, slingResourceType.lastIndexOf("/"));
        final String currentVersion = path.substring(path.lastIndexOf("/") + 1);
        if(versionNumber != null && !versionNumber.equals(currentVersion)) {
            try {
                String newVersionPath = path.substring(0, path.lastIndexOf("/")) + "/" + versionNumber;
                String componentName = slingResourceType.substring(slingResourceType.lastIndexOf("/"));
                String newComponentVersionPath = newVersionPath + componentName;

                final ModifiableValueMap properties = resource.adaptTo(ModifiableValueMap.class);
                if (properties != null) {
                    properties.put("sling:resourceType", newComponentVersionPath);
                    resource.getResourceResolver().commit();
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
