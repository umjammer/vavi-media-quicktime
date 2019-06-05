/*
 * QuickTime for Java SDK Sample Code

   Usage subject to restrictions in SDK License Agreement
 * Copyright: © 1996-1999 Apple Computer, Inc.

 */
package Music;

import quicktime.Errors;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.std.StdQTException;
import quicktime.std.music.NoteAllocator;
import quicktime.std.music.NoteChannel;
import quicktime.std.music.NoteRequest;
import quicktime.std.music.ToneDescription;

public class NotesToneTest implements Errors {
	public static void main (String[] args) {
		try {
			System.out.println ("Starting QTMA Test");
			QTSession.open();
			
			// A NoteAllocator is required for using NoteChannels
			// However QTJava provides a default NA:
			//	NoteAllocator na = NoteAllocator.getDefault();
			// and this is used for the TD/NR and NC constructors if the
			// application does not provide one.
			
			System.out.println ("Pick Instrument:");
			ToneDescription toneDesc = new ToneDescription ();
				//print out an uninitialized ToneDescription
			System.out.println (toneDesc);
			
			try {
				// Have the user choose an instrument and print out the choice
				toneDesc.pickInstrument (NoteAllocator.getDefault(), "Choose an Instrument...", 0);
				System.out.println (toneDesc);
			} catch (StdQTException e) {
				if (e.errorCode() != userCanceledErr) return;
			}
				
				// Make a Tone Description using GMIDI inst. no. 25
			System.out.println ("StuffTone:25");
			toneDesc = new ToneDescription (25);
			System.out.println (toneDesc);

				// Play a note (60) at maximum velocity (127) for 2000msecs then turn it off.
			System.out.println ("PlayNote:Inst,25, Note,60");
			NoteChannel noteChan = new NoteChannel(new NoteRequest(toneDesc));
			noteChan.playNote (60, 127);
				try { Thread.sleep (2000); } 
				catch (InterruptedException e) { e.printStackTrace(); }
			noteChan.playNote (60, 0);
		}
		catch (QTException qte) {
			qte.printStackTrace();
		}
		finally {
			System.out.println ("Finished QTMA Test");
			QTSession.close();
		}
	}
}
