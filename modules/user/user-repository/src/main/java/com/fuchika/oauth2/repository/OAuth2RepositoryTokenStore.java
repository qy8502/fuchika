package com.fuchika.oauth2.repository;

import com.fuchika.oauth2.model.OAuth2AuthenticationAccessToken;
import com.fuchika.oauth2.model.OAuth2AuthenticationRefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 22/05/2013
 */
public class OAuth2RepositoryTokenStore implements TokenStore {

    private final OAuth2AccessTokenRepository oAuth2AccessTokenRepository;

    private final OAuth2RefreshTokenRepository oAuth2RefreshTokenRepository;

    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    @Autowired
    public OAuth2RepositoryTokenStore(final OAuth2AccessTokenRepository oAuth2AccessTokenRepository,
                                      final OAuth2RefreshTokenRepository oAuth2RefreshTokenRepository) {
        this.oAuth2AccessTokenRepository = oAuth2AccessTokenRepository;
        this.oAuth2RefreshTokenRepository = oAuth2RefreshTokenRepository;
    }

    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    public OAuth2Authentication readAuthentication(String tokenId) {
        return oAuth2AccessTokenRepository.findByTokenId(tokenId).getAuthentication();
    }

    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        OAuth2AuthenticationAccessToken oAuth2AuthenticationAccessToken = new OAuth2AuthenticationAccessToken(token,
                authentication, authenticationKeyGenerator.extractKey(authentication));
        oAuth2AccessTokenRepository.save(oAuth2AuthenticationAccessToken);
    }

    public OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AuthenticationAccessToken token = oAuth2AccessTokenRepository.findByTokenId(tokenValue);
        if(token == null) {
            return null; //let spring security handle the invalid token
        }
        OAuth2AccessToken accessToken = token.getoAuth2AccessToken();
		return accessToken;
    }

    public void removeAccessToken(OAuth2AccessToken token) {
        OAuth2AuthenticationAccessToken accessToken = oAuth2AccessTokenRepository.findByTokenId(token.getValue());
        if(accessToken != null) {
            oAuth2AccessTokenRepository.delete(accessToken);
        }
    }

    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
         oAuth2RefreshTokenRepository.save(new OAuth2AuthenticationRefreshToken(refreshToken, authentication));
    }

    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        return oAuth2RefreshTokenRepository.findByTokenId(tokenValue).getoAuth2RefreshToken();
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return oAuth2RefreshTokenRepository.findByTokenId(token.getValue()).getAuthentication();
    }

    public void removeRefreshToken(OAuth2RefreshToken token) {
        oAuth2RefreshTokenRepository.delete(oAuth2RefreshTokenRepository.findByTokenId(token.getValue()));
    }

    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        oAuth2AccessTokenRepository.delete(oAuth2AccessTokenRepository.findByRefreshToken(refreshToken.getValue()));
    }

    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AuthenticationAccessToken token =  oAuth2AccessTokenRepository.findByAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
        return token == null ? null : token.getoAuth2AccessToken();
    }

    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        List<OAuth2AuthenticationAccessToken> tokens = oAuth2AccessTokenRepository.findByClientId(clientId);
        return extractAccessTokens(tokens);
    }

    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        List<OAuth2AuthenticationAccessToken> tokens = oAuth2AccessTokenRepository.findByClientIdAndUserName(clientId, userName);
        return extractAccessTokens(tokens);
    }

    private Collection<OAuth2AccessToken> extractAccessTokens(List<OAuth2AuthenticationAccessToken> tokens) {
        List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>();
        for(OAuth2AuthenticationAccessToken token : tokens) {
            accessTokens.add(token.getoAuth2AccessToken());
        }
        return accessTokens;
    }

}
