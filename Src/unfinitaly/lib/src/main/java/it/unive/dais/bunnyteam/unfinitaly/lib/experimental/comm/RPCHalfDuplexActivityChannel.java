package it.unive.dais.bunnyteam.unfinitaly.lib.experimental.comm;

import it.unive.dais.bunnyteam.unfinitaly.lib.experimental.IActivity;
import it.unive.dais.bunnyteam.unfinitaly.lib.experimental.TyIntent;


public class RPCHalfDuplexActivityChannel<Sender extends IActivity<?>,
										  Receiver extends IActivity<?>,
										  Cmd extends Command<Receiver>>
extends HalfDuplexActivityChannel<Sender, Receiver, Cmd> {
			
	@SuppressWarnings("unused")
	private final Receiver __recv = null;
	
	@Deprecated
	public RPCHalfDuplexActivityChannel() {
		super("__recv");		
	}

	public RPCHalfDuplexActivityChannel(Class<Receiver> recvcl) {
		super(recvcl);
	}
			
	public void evalCommand(Receiver recv) {
		TyIntent<Cmd> i = TyIntent.create(recv.getIntent());
		i.get().get().apply(recv);
	}
	
}
