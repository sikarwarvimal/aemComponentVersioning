package aemComponentVersioning.core.models;

import com.day.cq.wcm.api.AuthoringUIMode;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = SlingHttpServletRequest.class)
public class Mode {
    private final SlingHttpServletRequest request;

    public Mode(SlingHttpServletRequest request) {
        this.request = request;
    }

    public boolean isTouchUI() {
        return AuthoringUIMode.TOUCH.equals(AuthoringUIMode.fromRequest(this.request));
    }
}
