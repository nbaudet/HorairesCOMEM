package ch.Comem;

import java.util.ArrayList;

import com.Wsdl2Code.WebServices.Service.ScheduleEntity;

public class HoWiSchedule {
	
	private String day;
	private ArrayList<String> am, pm;
	
	/**
	 * Construit l'horaire d'une journ�e en arrangeant les horaires le matin ou l'apr�s-midi.
	 * @param horaire
	 */
	public HoWiSchedule(String day, ScheduleEntity horaire) {
		if(day != null && horaire != null){
			this.day = day;
			// TODO Gestion des horaires matin et apr�s-midi
		}
		else {
			day = new String();
			am = pm = new ArrayList<String>();
		}
	}
	
	//public String getJour()
}
