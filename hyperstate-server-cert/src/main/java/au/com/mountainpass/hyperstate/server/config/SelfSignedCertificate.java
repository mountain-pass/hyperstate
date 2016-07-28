package au.com.mountainpass.hyperstate.server.config;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V1CertificateGenerator;

public class SelfSignedCertificate {

    static {
        // adds the Bouncy castle provider to java security
        Security.addProvider(new BouncyCastleProvider());
    }

    private KeyPair keyPair;
    private X509Certificate cert;

    public SelfSignedCertificate(String domainName, String keyAlias)
            throws Exception {
        this.keyPair = createKeyPair();
        this.cert = createSelfSignedCertificate(keyPair, domainName, keyAlias);
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

    private KeyPair createKeyPair()
            throws NoSuchAlgorithmException, NoSuchProviderException {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator
                .getInstance("RSA", "BC");
        keyPairGenerator.initialize(2048, new SecureRandom());
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public X509Certificate getCertificate() {
        return this.cert;
    }

    public PrivateKey getPrivateKey() {
        return this.keyPair.getPrivate();
    }
}
