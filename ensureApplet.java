package classicapplet1;

import javacard.framework.*;
import javacardx.biometry.BioBuilder;
import javacardx.biometry.OwnerBioTemplate;
import javacardx.biometry.SharedBioTemplate;
import javacardx.biometry.BioException;


public class JavaBiometrics extends Applet implements SharedBioTemplate{


        public final static byte CLA = (byte)0xCF;
     public final static byte INS_ENROLL = (byte)0x10;
     public final static byte MATCH_TRY_LIMIT = (byte)3;

     public final static byte INVALID_DATA = (byte)0x77;
     public final static byte ERROR_MATCH_FAILED = (byte)0x9101;
     public static final byte CARD_ENROLL_SUCCESS = (byte)0x9000;
     public static final byte CARD_ENROL_FAILED = (byte)0x6900;


     private OwnerBioTemplate bio_temp;
     
    /**
     * Installs this applet.
     * 
     * @param bArray
     *            the array containing installation parameters
     * @param bOffset
     *            the starting offset in bArray
     * @param bLength
     *            the length in bytes of the parameter data in bArray
     */
    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new JavaBiometrics(bArray, bOffset, bLength);
    }

    /**
     * Only this class's install method should create the applet object.
     */
    protected JavaBiometrics(byte[] bArray, short bOffset, short bLength) {
    byte aidLen = bArray[bOffset];
          if(aidLen == (byte)0)
               register();
          else
               register(bArray, (short)(bOffset+1), aidLen);

          bio_temp = BioBuilder.buildBioTemplate(BioBuilder.FINGERPRINT, MATCH_TRY_LIMIT);

        
    }

public boolean select()
{
return true;
}


    /**
     * Processes an incoming APDU.
     * 
     * @see APDU
     * @param apdu
     *            the incoming APDU
     */
    public void process(APDU apdu) {
        //get the incoming APDU buffer
     byte []buffer = apdu.getBuffer();

     //Get the CLA; mask out the logical-channel info
     buffer[ISO7816.OFFSET_CLA] = (byte)(buffer[ISO7816.OFFSET_CLA] & (byte)0xFC);

     //If the INS Select, return -no need to process select
     if(buffer[ISO7816.OFFSET_CLA] == 0 && buffer[ISO7816.OFFSET_INS] == (byte)(0xA4))
          return;

     //If unrecognized class, return "Unsupported class."
     if(buffer[ISO7816.OFFSET_CLA] != CLA)
          ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);

     switch(buffer[ISO7816.OFFSET_INS])
     {
     case INS_ENROLL:
          enroll(apdu);
          break;
     default:
          ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
     }

        
    }

public void enroll(APDU apdu)
{
        byte[] buffer = apdu.getBuffer();
        short bytesRead = apdu.setIncomingAndReceive();
        bio_temp.init(buffer, ISO7816.OFFSET_CDATA, bytesRead);
        bio_temp.doFinal();
}

 
    public Shareable getShareableInterfaceObject(AID clientAID, byte parameter) {
        return this;
    }

///////////// These methods implemets the ShareableBio interface///////////////
 public boolean isInitialized() {
        return bio_temp.isInitialized();
    }

    public boolean isValidated() {
        return bio_temp.isValidated();
    }

    public void reset() {
        bio_temp.reset();
    }

    public byte getTriesRemaining() {
        return bio_temp.getTriesRemaining();
    }

    public byte getBioType() {
        return bio_temp.getBioType();
    }

    public short getVersion(byte[] dest, short offset) {
        return bio_temp.getVersion(dest, offset);
    }

    public short getPublicTemplateData(short publicOffset, byte[] dest, short destOffset, short length)
            throws BioException {
        return bio_temp.getPublicTemplateData(publicOffset, dest, destOffset, length);
    }

    public short initMatch(byte[] candidate, short offset, short length) throws BioException {
        return bio_temp.initMatch(candidate, offset, length);
    }

    public short match(byte[] candidate, short offset, short length) throws BioException {
        return bio_temp.match(candidate, offset, length);
    }

}
