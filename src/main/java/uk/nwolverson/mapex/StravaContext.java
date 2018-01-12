package uk.nwolverson.mapex;

import javastrava.auth.model.Token;
import javastrava.auth.model.TokenResponse;
import javastrava.service.Strava;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class StravaContext {
    @Autowired
    OAuth2ClientContext oAuth2ClientContext;

    public Strava getStrava() {
        TokenResponse tokenResponse = new TokenResponse();
        OAuth2AccessToken accessToken = oAuth2ClientContext.getAccessToken();
        tokenResponse.setAccessToken(accessToken.getValue());
        tokenResponse.setTokenType(accessToken.getTokenType());
        return new Strava(new Token(tokenResponse));
    }
}
