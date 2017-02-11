package cz.abclinuxu.datoveschranky.impl;

import cz.abclinuxu.datoveschranky.common.entities.Hash;
import cz.abclinuxu.datoveschranky.common.entities.TimeStamp;
import cz.abclinuxu.datoveschranky.common.DataBoxException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.SignerInformationVerifier;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.cms.jcajce.JcaX509CertSelectorConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.AlgorithmNameFinder;
import org.bouncycastle.operator.DefaultAlgorithmNameFinder;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.tsp.TimeStampTokenInfo;

/**
 *
 * Pomocná třída pro validaci časového razítka a podpisu zprávy. Jen prototyp!
 *
 * @author Vaclav Rosecky &lt;xrosecky 'at' gmail 'dot' com&gt;
 */
public class Validator {

    private final AlgorithmNameFinder algorithmNameFinder = new DefaultAlgorithmNameFinder();


    static {
        Provider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
    }
    private Collection<X509Certificate> certs = null;
    private boolean isValidating = false;
    private Logger logger = Logger.getLogger(Validator.class.getCanonicalName());

    public Validator(Collection<X509Certificate> certs, boolean validating) {
        this.certs = certs;
        isValidating = validating;
    }

    public Validator() {
        this.certs = new ArrayList<X509Certificate>();
        isValidating = false;
    }

    public TimeStamp readTimeStamp(byte[] timeStamp) {
        try {
            CMSSignedData data = new CMSSignedData(timeStamp);
            TimeStampToken tst = new TimeStampToken(data);
            TimeStampTokenInfo tsti = tst.getTimeStampInfo();
            X509Certificate cert = this.findCertificate(tst.getSID());
            if (isValidating) {
                try {
                    SignerInformationVerifier verifier = new JcaSimpleSignerInfoVerifierBuilder()
                            .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                            .build(cert);
                    tst.validate(verifier);
                } catch (Exception ex) {
                    if (ex instanceof RuntimeException) {
                        throw (RuntimeException) ex;
                    } else {
                        throw new DataBoxException(ex.toString(), ex);
                    }
                }
            }
            String algo = algorithmNameFinder.getAlgorithmName(tsti.getHashAlgorithm());
            byte[] hash = tsti.getMessageImprintDigest();
            return new TimeStamp(new Hash(algo, hash), cert, tsti.getGenTime());
        } catch (CMSException ex) {
            throw new DataBoxException("Chyba pri cteni casoveho razitka.", ex);
        } catch (TSPException ioe) {
            throw new DataBoxException("Chyba pri cteni casoveho razitka.", ioe);
        } catch (IOException ioe) {
            throw new DataBoxException("IO chyba pri cteni casoveho razitka.", ioe);
        }
    }

    /**
     * Vrátí obsah po odstranění PKCS7 obálky.
     */
    public byte[] readPKCS7(byte[] signedBytes) throws DataBoxException {
        try {
            CMSSignedData data = new CMSSignedData(signedBytes);
            verifySignature(data);
            CMSProcessable signedContent = data.getSignedContent();
            return (byte[]) signedContent.getContent();
        } catch (Exception ex) {
            throw new DataBoxException("Nemohu otevrit PKCS#7 obalku.", ex);
        }
    }

    /**
     * Vrátí obsah po odstranění PKCS7 obálky.
     */
    public InputStream readPKCS7(InputStream is) throws DataBoxException {
        try {
            CMSSignedData data = new CMSSignedData(is);
            CMSProcessable signedContent = data.getSignedContent();
            verifySignature(data);
            return (InputStream) signedContent.getContent();
        } catch (Exception ex) {
            throw new DataBoxException("Nemohu otevrit PKCS#7 obalku.", ex);
        }
    }

    private void verifySignature(CMSSignedData data) throws Exception {
        if (isValidating) {
            SignerInformationStore signerStore = data.getSignerInfos();
            Collection<SignerInformation> signers = signerStore.getSigners();
            for (SignerInformation signer : signers) {
                X509Certificate cert = this.findCertificate(signer.getSID());
                if (cert == null) {
                    throw new DataBoxException("Nemohu najit certifikat.");
                }
                SignerInformationVerifier verifier = new JcaSimpleSignerInfoVerifierBuilder()
                        .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                        .build(cert);
                if (!signer.verify(verifier)) {
                    throw new DataBoxException("Nemohu overit oproti certifikatu stazenou zpravu.");
                }
            }
        }
    }

    private X509Certificate findCertificate(SignerId signer) {
        // according to bouncycastle 1.56 release migration guide
        // To convert from SignerIds and RecipientIds use the JcaX509CertSelectorConverter class.
        // To convert from X509CertSelectors use the JcaX509SelectorConverter class.
        JcaX509CertSelectorConverter converter = new JcaX509CertSelectorConverter();
        logger.debug("Hledam certifikat pro " + String.valueOf(signer));
        return converter.getCertSelector(signer).getCertificate();
    }

}
