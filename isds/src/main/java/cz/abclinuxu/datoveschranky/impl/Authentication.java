package cz.abclinuxu.datoveschranky.impl;

import javax.net.ssl.SSLSocketFactory;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import java.security.KeyStore;
import java.util.Map;
import java.util.logging.Logger;

import cz.abclinuxu.datoveschranky.common.Config;
import cz.abclinuxu.datoveschranky.common.Utils;

/**
 *
 * Autentizace
 * 
 */
public abstract class Authentication {
    // copied from mvn:com.sun.xml.ws/jaxws-rt JAXWSProperties
    public static final String SSL_SOCKET_FACTORY = "com.sun.xml.ws.transport.https.client.SSLSocketFactory";

    protected final Config config;
    // protected SSLSocketFactory socketFactory = null;
    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    protected Authentication(Config config) {
        this.config = config;
        // KeyStore keyStore = config.getKeyStore();
        // this.socketFactory = Utils.createSSLSocketFactory(keyStore);
    }

    public <T> T createService(Service serviceBuilder, Class<T> serviceClass, String servicePostfix) {
        T service = serviceBuilder.getPort(serviceClass);
        configureService(((BindingProvider) service).getRequestContext(), servicePostfix);
        return service;
    }

    protected void configureService(Map<String, Object> requestContext, String servicePostfix) {
        requestContext.put(SSL_SOCKET_FACTORY, this.createSSLSocketFactory());
        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, config.getServiceURL() + servicePostfix);
        this.configureServiceOverride(requestContext, servicePostfix);
    }

    protected SSLSocketFactory createSSLSocketFactory() {
        KeyStore keyStore = config.getKeyStore();
        return Utils.createSSLSocketFactory(keyStore);
    }

    protected abstract void configureServiceOverride(Map<String, Object> requestContext, String servicePostfix);
}
