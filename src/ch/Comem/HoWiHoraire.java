package ch.Comem;

import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Iterator;

import com.Wsdl2Code.WebServices.Service.ScheduleEntity;
import com.Wsdl2Code.WebServices.Service.VectorScheduleEntity;

/**
 * Cette classe permet de générer des horaires selon les préférences settées pour un widget.
 * @author nicolas
 *
 */
public class HoWiHoraire {

	private TreeMap<String, HoWiHoraireJournalier> horaire;
	
	/**
	 * Constructeur, prend en paramètres un Vector de ScheduleEntity retourné par le webservice.
	 * @param data (Obligatoire) Un Vector de ScheduleEntity
	 */
	public HoWiHoraire(Object data) {
		VectorScheduleEntity vse = (VectorScheduleEntity) data;
		
		this.horaire = new TreeMap<String, HoWiHoraireJournalier>();
		
		// Pour chaque horaire retourné
		for(int i = 0; i < vse.size(); i++) {
			ScheduleEntity se = (ScheduleEntity) vse.getProperty(i);
			
			// Si on a déjà un horaire pour ce jour on l'ajoute au bon endroit
			if(this.horaire.containsKey(se.date)) {
				HoWiHoraireJournalier hj = this.horaire.get(se.date);
				hj.addHoraire(se);
			}
			// Sinon on crée un nouvel horaire journalier et on l'ajoute à l'horaire
			else {
				this.horaire.put(se.date, new HoWiHoraireJournalier(se.day, se.date, se));
			}
		}
	}
	
	/**
	 * Retourne l'horaire sous forme d'une liste d'HoWiSchedule.
	 * @return La liste des cours pour les préférences du widget
	 */
	protected TreeMap<String, HoWiHoraireJournalier> getHoWiHoraireJournalier() {
		return this.horaire;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, HoWiHoraireJournalier>> it = this.horaire.entrySet().iterator();
	    while (it.hasNext()) {
	    	HoWiHoraireJournalier hj = (HoWiHoraireJournalier) it.next().getValue();
	    	sb.append(hj.toString());
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	    return sb.toString();
	}
}
