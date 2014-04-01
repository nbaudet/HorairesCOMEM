package ch.Comem;

import java.util.ArrayList;

import com.Wsdl2Code.WebServices.Service.ScheduleEntity;
import com.Wsdl2Code.WebServices.Service.VectorScheduleEntity;

public class ScheduleFactory {

	private ArrayList<String> horaires;
	
	public ScheduleFactory(Object data) {
		VectorScheduleEntity vse = (VectorScheduleEntity) data;
		
		for(int i = 0; i < vse.size(); i++){
			ScheduleEntity se = (ScheduleEntity) vse.getProperty(i);
			String seToAdd = new String(se.toString()); // Lève une nullPointerException...
			this.horaires.add(seToAdd);
		}
	}
	
	protected ArrayList<String> getHoraires(){
		return this.horaires;
	}

}
