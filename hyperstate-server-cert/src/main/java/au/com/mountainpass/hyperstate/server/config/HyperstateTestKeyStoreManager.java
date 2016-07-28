package au.com.mountainpass.hyperstate.server.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HyperstateTestKeyStoreManager {

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public HyperstateTestKeyStoreManager(final String keyStore,
            final String keyStorePassword, final String keyPassword,
            final String keyAlias, final String sslHostname,
            final String trustStoreFile, final String trustStorePassword,
            final String trustStoreType) throws Exception {
        createCertificateAndStore(keyStore, keyStorePassword, keyPassword,
                keyAlias, sslHostname, trustStoreFile, trustStorePassword,
                trustStoreType);
    }

    private void createCertificateAndStore(final String keyStore,
            final String keyStorePassword, final String keyPassword,
            final String keyAlias, final String domainName,
            final String trustStoreFile, final String trustStorePassword,
            final String trustStoreType) throws Exception {
        SelfSignedCertificate cert = new SelfSignedCertificate(domainName,
                keyAlias);
        final PrivateKey privateKey = cert.getPrivateKey();

        X509Certificate selfSignedCertificate = cert.getCertificate();

        addPrivateKeyToKeyStore(keyStore, keyStorePassword, keyPassword,
                keyAlias, privateKey, selfSignedCertificate);

        addCertToTrustStore(keyAlias, trustStoreFile, trustStorePassword,
                trustStoreType, selfSignedCertificate);

    }

    private void addCertToTrustStore(final String keyAlias,
            final String trustStoreFile, final String trustStorePassword,
            final String trustStoreType, final X509Certificate cert)
                    throws KeyStoreException, IOException,
                    NoSuchAlgorithmException, CertificateException,
                    FileNotFoundException {
        if (trustStoreFile != null) {
            final KeyStore ks = KeyStore.getInstance(trustStoreType);
            final File trustFile = new File(trustStoreFile);
            ks.load(null, null);
            ks.setCertificateEntry(keyAlias, cert);
            final FileOutputStream fos = new FileOutputStream(trustFile);
            ks.store(fos, trustStorePassword.toCharArray());
            fos.close();
        }
    }

    private void addPrivateKeyToKeyStore(final String keyStore,
            final String keyStorePassword, final String keyPassword,
            final String keyAlias, final PrivateKey privateKey,
            X509Certificate selfSignedCertificate) throws KeyStoreException,
                    FileNotFoundException, IOException,
                    NoSuchAlgorithmException, CertificateException {
        final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

        // load an empty key store
        ks.load(null, keyStorePassword.toCharArray());

        // add the key
        ks.setKeyEntry(keyAlias, privateKey, keyPassword.toCharArray(),
                new java.security.cert.Certificate[] { selfSignedCertificate });
        // Write the key store to disk.
        final FileOutputStream fos = new FileOutputStream(keyStore);
        ks.store(fos, keyStorePassword.toCharArray());
        fos.close();
    }

}
