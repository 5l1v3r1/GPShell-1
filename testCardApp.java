package cccs.on_card.id;

import javacard.framework.*;
import javacard.security.*;

// Applet AID: 0x00:0x01:0x02:0x03:0x04:0x05:0x06:0x07:0x0b:0x1		
// I arbitrarily chose the general structure for my Application Identifiers( i.e AIDs )[ all are 10 bytes long ]
// All packages start with the general sequence 0x00:0x01:0x02:0x03:0x04:0x05:0x06:0x07, then individual 
// packages are labelled 0x0a, 0x0b, ...( e.g, this is 0x0b, and 0x1 is this particular class's number

public class Identity extends javacard.framework.Applet
{
	// define the applet's CLAss byte

	final static byte CLA = ( byte )0x04;

	// read-only INStruction commands

	final static byte GET_STUDENT_ID = ( byte )0x01;
	final static byte GET_FIRST_NAME = ( byte )0x02;
	final static byte GET_LAST_NAME = ( byte )0x03;
	
	// constants

	final static short MAX_ID_SIZE = ( short )11;		// *** WATCH THIS!!! **
	final static short MAX_NAME_SIZE = ( short )7;		// *** WATCH THIS!!! **
		
	//final static short ALL_INFO_SIZE = MAX_ID_SIZE + ( 2 * MAX_NAME_SIZE );

	// fields

	byte[] studentID = null;
	byte[] firstName = null;
	byte[] lastName = null;

	/**
	 * Initialize all the fields of this applet
	 *
	 * Example input:-
	 *
	 * STUDENT_ID	: E27-0193/01	: 69 50 55 45 48 49 57 51 47 48 49				: ASCII
	 * STUDENT_ID	: E27-0193/01	: 0x45 0x32 0x37 0x2d 0x30 0x31 0x39 0x33 0x2f 0x30 0x31	: HEX
	 * FNAME	: MICHAEL	: 77 73 67 72 65 69 76 0x00 0x00 0x00				: ASCII
	 * FNAME	: MICHAEL	: 0x4d 0x49 0x43 0x48 0x41 0x45 0x4c 0x00 0x00 0x00		: HEX
	 * LNAME	: KAMUNGE	: 75 65 77 85 78 71 69 0x00 0x00 0x00				: ASCII
	 * LNAME	: KAMUNGE	: 0x4b 0x41 0x4d 0x55 0x4e 0x47 0x45 0x00 0x00 0x00		: HEX
	 *
	 *
	 * input	: 4532372d303139332f30314d49434841454c0000004b414d554e4745000000
	 * length	: 31 bytes( 0x1F )
	 *
	 **/
	
	private Identity( byte[] bArray, short bOffset, byte bLength )
	{
		studentID = new byte[ MAX_ID_SIZE ];
		firstName = new byte[ MAX_NAME_SIZE ];
		lastName = new byte[ MAX_NAME_SIZE ];

		studentID[ 0 ] = ( byte )0x45;
		studentID[ 1 ] = ( byte )0x32;
		studentID[ 2 ] = ( byte )0x37;
		studentID[ 3 ] = ( byte )0x2d;
		studentID[ 4 ] = ( byte )0x30;
		studentID[ 5 ] = ( byte )0x31;
		studentID[ 6 ] = ( byte )0x39;
		studentID[ 7 ] = ( byte )0x33;
		studentID[ 8 ] = ( byte )0x2f;
		studentID[ 9 ] = ( byte )0x30;
		studentID[ 10 ] = ( byte )0x31;

		     
		firstName[ 0 ] = ( byte )0x4d;
		firstName[ 1 ] = ( byte )0x49;
		firstName[ 2 ] = ( byte )0x43;
		firstName[ 3 ] = ( byte )0x48;
		firstName[ 4 ] = ( byte )0x41;
		firstName[ 5 ] = ( byte )0x45;
		firstName[ 6 ] = ( byte )0x4c;

		
		lastName[ 0 ] = ( byte )0x4b;
		lastName[ 1 ] = ( byte )0x41;
		lastName[ 2 ] = ( byte )0x4d;
		lastName[ 3 ] = ( byte )0x55;
		lastName[ 4 ] = ( byte )0x4e;
		lastName[ 5 ] = ( byte )0x47;
		lastName[ 6 ] = ( byte )0x45;
		
		register();

	} // constructor

	/* Installs a new instance of this applet and passes the input to the applet constructor */

	public static void install( byte[] bArray, short bOffset, byte bLength )
	{
		new Identity( bArray, bOffset, bLength );
	}

	/* The process method which represents all commands */

	public void process( APDU apdu ) throws ISOException
	{	
		byte[] buffer = apdu.getBuffer();

		if( selectingApplet() )
		{
			return;
		}

		if( buffer[ ISO7816.OFFSET_CLA ] != CLA )
		{
			ISOException.throwIt( ISO7816.SW_CLA_NOT_SUPPORTED );
		}

		switch( buffer[ ISO7816.OFFSET_INS ] )
		{
			case GET_STUDENT_ID:
			{
				getStudentID( apdu );
				return;
			}

			case GET_FIRST_NAME:
			{
				getFirstName( apdu );
				return;
			}

			case GET_LAST_NAME:
			{
				getLastName( apdu );
				return;
			}

			default:
			{
                 		ISOException.throwIt( ISO7816.SW_INS_NOT_SUPPORTED );
			}

		} // switch

	} // process

	private void getLastName( APDU apdu )
	{
		byte[] buffer = apdu.getBuffer();
		Util.arrayCopy( lastName, ( short )0, buffer, ( short )0, MAX_NAME_SIZE );
		apdu.setOutgoingAndSend( ( short )0, MAX_NAME_SIZE );
	
	} // getLastName
 
	private void getFirstName( APDU apdu )
	{
		byte[] buffer = apdu.getBuffer();
		Util.arrayCopy( firstName, ( short )0, buffer, ( short )0, MAX_NAME_SIZE );
		apdu.setOutgoingAndSend( ( short )0, MAX_NAME_SIZE );
	
	} // getFirstName

	private void getStudentID( APDU apdu )
     	{
        	byte[] buffer = apdu.getBuffer();
         	Util.arrayCopy( studentID, ( short )0, buffer, ( short )0, MAX_ID_SIZE );
         	apdu.setOutgoingAndSend( ( short )0, MAX_ID_SIZE );
     	
	} // getStudentID

} // class
