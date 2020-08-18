package pro.acutus.keycloak.freemarkerplus;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;

import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.keycloak.forms.login.LoginFormsPages;
import org.keycloak.forms.login.freemarker.FreeMarkerLoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.theme.FreeMarkerUtil;
import org.keycloak.theme.Theme;

public class FreeMarkerPlusLoginFormsProvider extends FreeMarkerLoginFormsProvider {

    private static final Logger LOG = Logger.getLogger(FreeMarkerPlusLoginFormsProvider.class.getName());

    public FreeMarkerPlusLoginFormsProvider(KeycloakSession session, FreeMarkerUtil freeMarker) {
        super(session, freeMarker);
    }

    @Override
    protected void createCommonAttributes(Theme theme, Locale locale, Properties messagesBundle, UriBuilder baseUriBuilder, LoginFormsPages page) {
        super.createCommonAttributes(theme, locale, messagesBundle, baseUriBuilder, page);
        this.attributes.put("uri", this.uriInfo);
        try {
            String redirectUrlStr = this.uriInfo.getQueryParameters().getFirst("redirect_uri");
            if (redirectUrlStr != null) {
                URI redirectUri = new URI(redirectUrlStr);
                this.attributes.put("redirectUri", new ResteasyUriInfo(redirectUri));
            } else {
                LOG.log(Level.INFO, "redirect_uri is missing");
            }
        } catch (URISyntaxException ex) {
            LOG.log(Level.SEVERE, "redirect_uri missing or invalid", ex);
        }
    }

}
