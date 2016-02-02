package au.com.mountainpass.hyperstate.server.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.catalina.startup.Tomcat;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NHttpClientConnectionManager;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.StringUtils;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;

import au.com.mountainpass.hyperstate.client.AutowiringDeserializer;
import au.com.mountainpass.hyperstate.client.EntityWrapperProxyDeserializer;
import au.com.mountainpass.hyperstate.client.SpringBeanHandlerInstantiator;

@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@ComponentScan("au.com.mountainpass.hyperstate")
public class HyperstateTestConfiguration implements
        ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    @Value("${au.com.mountainpass.hyperstate.test.proxy.max.connections.total:100}")
    private int proxyMaxConnectionsTotal;

    @Value("${au.com.mountainpass.hyperstate.test.proxy.max.connections.route:20}")
    private int proxyMaxConnectionsRoute;

    @Value("${au.com.mountainpass.hyperstate.test.proxy.read.timeout.ms:60000}")
    private int proxyReadTimeoutMs;

    @Value("${server.ssl.protocol:TLS}")
    String sslProtocol;

    @Value("${javax.net.ssl.trustStore:}")
    private String trustStore;

    @Value("${javax.net.ssl.trustStorePassword:changeit}")
    private String trustStorePassword;

    @Value("${javax.net.ssl.trustStoreType:JKS}")
    private String trustStoreType;

    public String getTrustStoreLocation() {
        if (StringUtils.hasLength(trustStore)) {
            return trustStore;
        }
        String locationProperty = System
                .getProperty("javax.net.ssl.trustStore");
        if (StringUtils.hasLength(locationProperty)) {
            return locationProperty;
        } else {
            return systemDefaultTrustStoreLocation();
        }
    }

    public String systemDefaultTrustStoreLocation() {
        String javaHome = System.getProperty("java.home");
        FileSystemResource location = new FileSystemResource(
                javaHome + "/lib/security/jssecacerts");
        if (location.exists()) {
            return location.getFilename();
        } else {
            return javaHome + "/lib/security/cacerts";
        }
    }

    @Bean
    KeyStore trustStore()
            throws KeyStoreException, IOException, NoSuchAlgorithmException,
            CertificateException, FileNotFoundException {
        KeyStore ks = KeyStore.getInstance(trustStoreType);

        File trustFile = new File(getTrustStoreLocation());
        ks.load(new FileInputStream(trustFile),
                trustStorePassword.toCharArray());
        return ks;
    }

    @Bean
    TrustManagerFactory trustManagerFactory() throws NoSuchAlgorithmException {
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        return tmf;
    }

    @Bean
    public SSLContext sslContext() throws Exception {
        SSLContext sslContext = SSLContext.getInstance(sslProtocol);
        TrustManagerFactory tmf = trustManagerFactory();
        KeyStore ks = trustStore();
        tmf.init(ks);
        sslContext.init(null, tmf.getTrustManagers(), null);
        return sslContext;
    }

    @Bean
    public SSLConnectionSocketFactory sslSocketFactory() throws Exception {
        SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(
                sslContext());
        return sf;
    }

    @Bean
    Registry<SchemeIOSessionStrategy> schemeIOSessionStrategyRegistry()
            throws Exception {
        return RegistryBuilder.<SchemeIOSessionStrategy> create()
                .register("http", NoopIOSessionStrategy.INSTANCE)
                .register("https", new SSLIOSessionStrategy(sslContext()))
                .build();
    }

    @Bean
    public NHttpClientConnectionManager nHttpClientConntectionManager()
            throws Exception {
        PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(
                new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT),
                schemeIOSessionStrategyRegistry());
        connectionManager.setMaxTotal(proxyMaxConnectionsTotal);
        connectionManager.setDefaultMaxPerRoute(proxyMaxConnectionsRoute);
        return connectionManager;
    }

    @Bean
    RequestConfig httpClientRequestConfig() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(proxyReadTimeoutMs).build();
        return config;
    }

    @Bean
    public HttpAsyncClientBuilder httpAsyncClientBuilder() throws Exception {
        return HttpAsyncClientBuilder.create().setSSLContext(sslContext())
                .setConnectionManager(nHttpClientConntectionManager())
                .setDefaultRequestConfig(httpClientRequestConfig());
    }

    @Bean // (destroyMethod = "close")
    public CloseableHttpAsyncClient httpAsyncClient() throws Exception {
        CloseableHttpAsyncClient client = httpAsyncClientBuilder().build();
        client.start();
        return client;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public String getTrustStoreType() {
        return trustStoreType;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:locale/messages");
        messageSource.setCacheSeconds(3600); // refresh cache once per hour
        return messageSource;
    }

    @Value("${server.ssl.key-store}")
    String keyStore;

    @Value("${server.ssl.key-store-password}")
    String keyStorePassword;

    @Value("${server.ssl.key-password}")
    String keyPassword;

    @Value("${server.ssl.key-alias}")
    String keyAlias;

    @Value("${au.com.mountainpass.hyperstate.test.ssl.hostname}")
    String sslHostname;

    @Value("${javax.net.ssl.trustStore:build/truststore.jks}")
    private String trustStoreFile;

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private int port;

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatFactory()
            throws Exception {
        hyperstateTestKeyStoreManager();
        return new TomcatEmbeddedServletContainerFactory() {

            @Override
            protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(
                    Tomcat tomcat) {
                return super.getTomcatEmbeddedServletContainer(tomcat);
            }
        };
    }

    @Bean
    public HyperstateTestKeyStoreManager hyperstateTestKeyStoreManager()
            throws Exception {
        if (getTrustStoreLocation().equals(systemDefaultTrustStoreLocation())) {
            LOGGER.warn(
                    "Trust Store location {} appears to be set to system default. The Self signed cert for testing will not be added and the tests will likely fail.",
                    getTrustStoreLocation());
            return new HyperstateTestKeyStoreManager(keyStore, keyStorePassword,
                    keyPassword, keyAlias, sslHostname, null, null, null);
        }
        return new HyperstateTestKeyStoreManager(keyStore, keyStorePassword,
                keyPassword, keyAlias, sslHostname, getTrustStoreLocation(),
                getTrustStorePassword(), getTrustStoreType());
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public URI getBaseUri() {
        return URI.create("https://" + sslHostname + ":" + getPort());
    }

    @Bean
    public HttpClientBuilder httpClientBuilder() throws Exception {
        HttpClientConnectionManager connectionManager = httpClientConnectionManager();
        RequestConfig config = httpClientRequestConfig();
        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(config)
                .setSSLSocketFactory(sslSocketFactory())
                .setSslcontext(sslContext()).disableRedirectHandling();
    }

    // @Bean
    // RequestConfig httpClientRequestConfig() {
    // RequestConfig config = RequestConfig.custom()
    // .setConnectTimeout(proxyReadTimeoutMs).build();
    // return config;
    // }

    @Bean
    Registry<ConnectionSocketFactory> httpConnectionSocketFactoryRegistry()
            throws Exception {
        return RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http",
                        PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory()).build();
    }

    @Bean
    HttpClientConnectionManager httpClientConnectionManager() throws Exception {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                httpConnectionSocketFactoryRegistry());
        connectionManager.setMaxTotal(proxyMaxConnectionsTotal);
        connectionManager.setDefaultMaxPerRoute(proxyMaxConnectionsRoute);
        return connectionManager;
    }

    @Bean
    public CloseableHttpClient httpClient() throws Exception {
        return httpClientBuilder().build();
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory httpClientFactory()
            throws Exception {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                httpClient());
        factory.setReadTimeout(200000);
        return factory;
    }

    @Value("${security.user.name:user}")
    String name;

    @Value("${security.user.password:password}")
    String password;

    @Autowired
    ObjectMapper objectMapper;

    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(
                objectMapper);
        return converter;
    }

    @Bean
    public RestTemplate restTemplate() throws Exception {
        RestTemplate restTemplate = new RestTemplate(httpClientFactory());
        List<HttpMessageConverter<?>> messageConverters = restTemplate
                .getMessageConverters();
        for (int i = 0; i < messageConverters.size(); ++i) {
            if (messageConverters
                    .get(i) instanceof MappingJackson2HttpMessageConverter) {
                messageConverters.set(i, mappingJacksonHttpMessageConverter());
            }
        }
        restTemplate.setMessageConverters(messageConverters);
        // restTemplate.setInterceptors(
        // Arrays.asList(new ClientHttpRequestInterceptor[] {
        // basicAuthHttpRequestIntercepter() }));
        return restTemplate;
    }

    @Bean
    AsyncClientHttpRequestFactory asyncHttpClientFactory() throws Exception {
        HttpComponentsAsyncClientHttpRequestFactory factory = new HttpComponentsAsyncClientHttpRequestFactory(
                httpClient(), asyncHttpClient());
        factory.setReadTimeout(200000);
        return factory;
    }

    @Bean
    CloseableHttpAsyncClient asyncHttpClient() throws Exception {
        return asyncHttpClientBuilder().build();
    }

    @Bean
    HttpAsyncClientBuilder asyncHttpClientBuilder() throws Exception {
        NHttpClientConnectionManager connectionManager = nHttpClientConntectionManager();
        RequestConfig config = httpClientRequestConfig();
        return HttpAsyncClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setConnectionManagerShared(true)
                .setDefaultRequestConfig(config).setSSLContext(sslContext());
    }

    @Bean
    public AsyncRestTemplate asyncRestTemplate() throws Exception {
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate(
                asyncHttpClientFactory(), restTemplate());
        List<HttpMessageConverter<?>> messageConverters = asyncRestTemplate
                .getMessageConverters();
        for (int i = 0; i < messageConverters.size(); ++i) {
            if (messageConverters
                    .get(i) instanceof MappingJackson2HttpMessageConverter) {
                messageConverters.set(i, mappingJacksonHttpMessageConverter());
            }
        }
        asyncRestTemplate.setMessageConverters(messageConverters);
        return asyncRestTemplate;
    }

    @Autowired
    private ApplicationContext context;

    @Bean(name = "customObjectMapperBuilder")
    @Primary
    @Profile({ "integration", "ui-integration" })
    Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.applicationContext(context);
        HandlerInstantiator handlerInstantiator = new SpringBeanHandlerInstantiator(
                context);
        // context.getAutowireCapableBeanFactory());
        builder.handlerInstantiator(handlerInstantiator);

        SimpleModule module = new SimpleModule();
        module.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(
                    DeserializationConfig config, BeanDescription beanDesc,
                    JsonDeserializer<?> deserializer) {
                return new AutowiringDeserializer(context, deserializer);
            }
        });

        SimpleModule module2 = new SimpleModule();
        module2.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(
                    DeserializationConfig config, BeanDescription beanDesc,
                    JsonDeserializer<?> deserializer) {
                return new EntityWrapperProxyDeserializer(context,
                        deserializer);
            }
        });
        builder.modules(module, module2);

        return builder;
    }

    @Override
    public void onApplicationEvent(
            EmbeddedServletContainerInitializedEvent event) {
        this.port = event.getEmbeddedServletContainer().getPort();
    }
}
