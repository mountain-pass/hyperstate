package au.com.mountainpass.hyperstate.server.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HyperstateTestKeyStoreManager {

    static {
        // adds the Bouncy castle provider to java security
        Security.addProvider(new BouncyCastleProvider());
    }

    public final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public HyperstateTestKeyStoreManager(final String keyStore,
            final String keyStorePassword, final String keyPassword,
            final String keyAlias, final String sslHostname,
            final String trustStoreFile, final String trustStorePassword,
            final String trustStoreType) throws Exception {
        createCertificateAndStore(keyStore, keyStorePassword, keyPassword, keyAlias,
                sslHostname, trustStoreFile, trustStorePassword,
                trustStoreType);
    }

    private void createCertificateAndStore(final String keyStore,
            final String keyStorePassword, final String keyPassword,
            final String keyAlias, final String domainName,
            final String trustStoreFile, final String trustStorePassword,
            final String trustStoreType) throws Exception {
        final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

        ks.load(null, keyStorePassword.toCharArray());
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator
                .getInstance("RSA", "BC");
        keyPairGenerator.initialize(2048, new SecureRandom());
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();

        X509Certificate selfSignedCertificate = createSelfSignedCertificate(
                keyPair, domainName, keyAlias);

        addPrivateKeyToKeyStore(keyStore, keyStorePassword, keyPassword,
                keyAlias, ks, keyPair, new java.security.cert.Certificate[] {
                        selfSignedCertificate });

        addCertToTrustStore(keyAlias, trustStoreFile, trustStorePassword,
                trustStoreType, selfSignedCertificate);

    }

    private X509Certificate createSelfSignedCertificate(final KeyPair keyPair,
            final String domainName, final String keyAlias) throws Exception {
        // generate a key pair

        // see
        // http://www.bouncycastle.org/wiki/display/JA1/X.509+Public+Key+Certificate+and+Certification+Request+Generation

        final Date startDate = new Date();
        final Date expiryDate = new Date(
                System.currentTimeMillis() + (1000L * 60 * 60 * 24));
        final BigInteger serialNumber = BigInteger
                .valueOf(Math.abs((long) (new SecureRandom().nextInt()))); // serial
        // number for
        // certificate

        final X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
        final X500Principal dnName = new X500Principal("CN=" + domainName);
        certGen.setSerialNumber(serialNumber);
        certGen.setIssuerDN(dnName);
        certGen.setNotBefore(startDate);
        certGen.setNotAfter(expiryDate);
        certGen.setSubjectDN(dnName); // note: same as issuer
        certGen.setPublicKey(keyPair.getPublic());
        certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
        final X509Certificate cert = certGen.generate(keyPair.getPrivate(),
                "BC");

        return cert;
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
            final String keyAlias, final KeyStore ks, final KeyPair keyPair,
            Certificate[] chain) throws KeyStoreException,
                    FileNotFoundException, IOException,
                    NoSuchAlgorithmException, CertificateException {
        ks.setKeyEntry(keyAlias, keyPair.getPrivate(),
                keyPassword.toCharArray(), chain);
        // Store away the keystore.
        final FileOutputStream fos = new FileOutputStream(keyStore);
        ks.store(fos, keyStorePassword.toCharArray());
        fos.close();
    }

}
