package aemComponentVersioning.core.models;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Model(adaptables = Resource.class)
public class ChangeVersion {

    private List<String> pageList;
    private ResourceResolver resourceResolver;

    @Inject
    public ChangeVersion(Resource resource,
                         @Named("linkUrl") final String linkUrlValue,
                         @Named("isButton") final Boolean isButtonValue) {

        resourceResolver = resource.getResourceResolver();

        // do query
        Query query;
        // build query builder
        final QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
        if (queryBuilder != null) {

            final Map<String, String> queryMap = new HashMap<>();

            queryMap.put("path", "/apps/aegis/common/components/content");
            queryMap.put("type","cq:Component");
            queryMap.put("property", "componentGroup");
            queryMap.put("group.p.not","true");
            queryMap.put("group.1_componentGroup",".hidden");
            queryMap.put("orderby", "jcr:title");
            queryMap.put("p.limit", "-1");

            query = queryBuilder.createQuery(PredicateGroup.create(queryMap), resourceResolver.adaptTo(Session.class));
            if (query != null) {
                SearchResult result = query.getResult();
                pageList = processPageList(result);
            }
        }
    }

    public List<String> getPageList() {
        return pageList;
    }
    private List<String> processPageList(SearchResult result) {
        final List<String> pageList = new ArrayList<>();

        for(Hit hit : result.getHits()) {
            try {
                String path = hit.getPath();
                PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
                if (pageManager != null) {
                    Resource resource = resourceResolver.getResource(path);
                    ValueMap valueMap = resource.adaptTo(ValueMap.class);
                    pageList.add(valueMap.get("jcr:title", String.class));
                }

            } catch (RepositoryException e) {
                //log.error("An Error occurred during the News search: {} " + e.getMessage());
            }
        }
        return pageList;
    }
}
