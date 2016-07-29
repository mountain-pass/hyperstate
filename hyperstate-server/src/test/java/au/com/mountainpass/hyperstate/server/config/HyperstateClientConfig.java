package au.com.mountainpass.hyperstate.server.config;

import java.util.List;

import javax.net.ssl.SSLContext;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class HyperstateClientConfig {

    @Value("${au.com.mountainpass.hyperstate.test.proxy.max.connections.total:100}")
    private int proxyMaxConnectionsTotal;

    @Value("${au.com.mountainpass.hyperstate.test.proxy.max.connections.route:20}")
    private int proxyMaxConnectionsRoute;

    @Value("${au.com.mountainpass.hyperstate.test.proxy.read.timeout.ms:60000}")
    private int proxyReadTimeoutMs;

    @Autowired
    private SSLConnectionSocketFactory sslSocketFactory;

    @Autowired
    private SSLContext sslContext;

    @Bean
    Registry<ConnectionSocketFactory> httpConnectionSocketFactoryRegistry()
            throws Exception {
        return RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http",
                        PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory).build();
    }

    @Bean
    HttpClientConnectionManager httpClientConnectionManager() throws Exception {
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                httpConnectionSocketFactoryRegistry());
        connectionManager.setMaxTotal(proxyMaxConnectionsTotal);
        connectionManager.setDefaultMaxPerRoute(proxyMaxConnectionsRoute);
        return connectionManager;
    }

    @Bean
    RequestConfig httpClientRequestConfig() {
        final RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(proxyReadTimeoutMs).build();
        return config;
    }

    @Bean
    public HttpClientBuilder httpClientBuilder() throws Exception {
        final HttpClientConnectionManager connectionManager = httpClientConnectionManager();
        final RequestConfig config = httpClientRequestConfig();
        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(config)
                .setSSLSocketFactory(sslSocketFactory).setSSLContext(sslContext)
                .disableRedirectHandling();
    }

    @Bean
    public CloseableHttpClient httpClient() throws Exception {
        return httpClientBuilder().build();
    }

    @Bean
    public Registry<SchemeIOSessionStrategy> schemeIOSessionStrategyRegistry()
            throws Exception {
        return RegistryBuilder.<SchemeIOSessionStrategy> create()
                .register("http", NoopIOSessionStrategy.INSTANCE)
                .register("https", new SSLIOSessionStrategy(sslContext))
                .build();
    }

    @Bean
    public NHttpClientConnectionManager nHttpClientConntectionManager()
            throws Exception {
        final PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(
                new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT),
                schemeIOSessionStrategyRegistry());
        connectionManager.setMaxTotal(proxyMaxConnectionsTotal);
        connectionManager.setDefaultMaxPerRoute(proxyMaxConnectionsRoute);
        return connectionManager;
    }

    @Bean
    HttpAsyncClientBuilder asyncHttpClientBuilder() throws Exception {
        final NHttpClientConnectionManager connectionManager = nHttpClientConntectionManager();
        final RequestConfig config = httpClientRequestConfig();
        return HttpAsyncClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setConnectionManagerShared(true)
                .setDefaultRequestConfig(config).setSSLContext(sslContext);
    }

    @Bean
    CloseableHttpAsyncClient asyncHttpClient() throws Exception {
        return asyncHttpClientBuilder().build();
    }

    @Bean
    AsyncClientHttpRequestFactory asyncHttpClientFactory() throws Exception {
        final HttpComponentsAsyncClientHttpRequestFactory factory = new HttpComponentsAsyncClientHttpRequestFactory(
                httpClient(), asyncHttpClient());
        factory.setReadTimeout(200000);
        return factory;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory httpClientFactory()
            throws Exception {
        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                httpClient());
        factory.setReadTimeout(200000);
        return factory;
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(
                objectMapper);
        return converter;
    }

    @Bean
    public RestTemplate restTemplate() throws Exception {
        final RestTemplate restTemplate = new RestTemplate(httpClientFactory());
        final List<HttpMessageConverter<?>> messageConverters = restTemplate
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
    public AsyncRestTemplate asyncRestTemplate() throws Exception {
        final AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate(
                asyncHttpClientFactory(), restTemplate());
        final List<HttpMessageConverter<?>> messageConverters = asyncRestTemplate
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

    @Bean
    public CloseableHttpAsyncClient httpAsyncClient() throws Exception {
        final CloseableHttpAsyncClient client = httpAsyncClientBuilder()
                .build();
        client.start();
        return client;
    }

    @Bean
    public HttpAsyncClientBuilder httpAsyncClientBuilder() throws Exception {
        return HttpAsyncClientBuilder.create().setSSLContext(sslContext)
                .setConnectionManager(nHttpClientConntectionManager())
                .setDefaultRequestConfig(httpClientRequestConfig());
    }

}
