package common;

public class CommonFunction {
	
	/**
	 * removePart()
	 * - to remove the middle part of a string for extracting from string array
	 * 
	 * @param str - original string
	 * @param key - keep words before this
	 * @param item - keep words after this
	 * @return concatenated string before and after the removed part
	 */
	public static String removePart(String str, String key, String item)
	{
		String tmp = null;
		
		//tmp = (str.substring(0, str.toLowerCase().indexOf(key))).trim() + (str.substring(str.toLowerCase().indexOf(item)+item.length())).trim();
		
		tmp = (str.substring(0, str.toLowerCase().indexOf(key.toLowerCase()))).trim() + (str.substring(str.toLowerCase().indexOf(item.toLowerCase())+item.length())).trim();
		
		return tmp;
	}
	

	/**
	 * trimArray()
	 * - to trim every element inside the array
	 * @param array to be trimmed
	 * @return array trimmed
	 */
	public static String[] trimArray(String[] array)
	{
		for (int i=0; i<array.length; i++) array[i] = array[i].trim();
		return array;
	}
	
	/**
	 * isCharacter()
	 * - check if the whole string passed in is consisted of alphabets only
	 * @param str
	 * @return true if alphabets only, false otherwise
	 */
	public static boolean isCharacter(String str)
	{
		char[] ch = str.toLowerCase().toCharArray();
		for (int i=0; i<ch.length; i++) {
			if (ch[i]<97 || ch[i]>122)
				return false;
		}
		return true;
	}

}
