package team.asaichi.oneColorMJ;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TrophyManager {
	private SharedPreferences pref;
	private Editor e;
	public TrophyManager(SharedPreferences p) {
		pref = p;
		e = pref.edit();
	}
	
	public void setTrophy(String key) {
		e.putInt(key, getTrophy(key) + 1);
		e.commit();
	}
	
	public int getTrophy(String key) {
		return pref.getInt(key, 0);
	}
}
