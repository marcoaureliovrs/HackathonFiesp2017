package com.example.eizesazake.fiesp.service;

public class StringUtilsService {

	public static String stripsNonNumeralCharsFromMobNum(String mobNum) {

		// strips away non-numeral characters
		mobNum = mobNum.replace("(", "");
		mobNum = mobNum.replace(")", "");
		mobNum = mobNum.replace("-", "");

		return mobNum;

	}
	
	// strips away country code (ex.: "+55")
	public static String stripsAwayCountryCode(String lineNumber1) {

		String mobileNumComplete = lineNumber1.substring(3);
		
		return mobileNumComplete;

	}

	public static String getSubStringMobileNumState(String mobNum) {

		mobNum = mobNum.substring(0, 2);

		return mobNum;

	}

	public static String getSubStringMobileNum(String mobNum) {

		mobNum = mobNum.substring(2);

		return mobNum;

	}

	public static String brStandardMobileNumFormat(String mobileNumState, String mobileNum){
		
		mobileNum = mobileNum.substring(0, 4).concat("-".concat(mobileNum.substring(4)));
		
		mobileNum =  "(".concat(mobileNumState).concat(")").concat(mobileNum);
		return mobileNum;
	}
	
	public static String brStandardMobileNumFormat(String mobileNumComplete){
			
		mobileNumComplete = "(".concat(mobileNumComplete.substring(0, 2)).concat(")")
				.concat(mobileNumComplete.substring(2, 6).concat("-".concat(mobileNumComplete.substring(6))));
			
			return mobileNumComplete;
	}
	
	public static String smsPostServiceBuilder(){
		
		String message = null;
		
		return message;
	}
	
	// used for password creation
	public static final boolean containsDigit(final String s) {
		boolean containsDigit = false;

		if (s != null) {
			for (char c : s.toCharArray()) {
				if (Character.isDigit(c)) {
					containsDigit = true;
					break;
				}
			}
		}

		return containsDigit;
	}

	// used for password creation
	public static final boolean containsUpperCase(final String s) {
		boolean containsUpperCase = false;

		if (s != null) {
			for (char c : s.toCharArray()) {
				if (Character.isUpperCase(c)) {
					containsUpperCase = true;
					break;
				}
			}
		}

		return containsUpperCase;
	}

	// used for password creation
	public static boolean containsSpecialCharacter(String s) {
		boolean containsSpecialCharacter = false;

		if (s != null) {
			for (char c : s.toCharArray()) {

                if (Character.isLetter(c)) {
                    
                }else if (Character.isDigit(c)) {
                    
                }else{
                    containsSpecialCharacter = true;
                }
			}
		}

		return containsSpecialCharacter;
	}
	
	public static String formatJsonSpecialCharacters(String gson){
		
		// http://www.w3schools.com/tags/ref_gsonencode.asp
		// replacing "%" with "%25" must be located before
		// before all the other replace methods that contain
		// %XX, because, otherwise, it will end up replacing
		// the "%" of all other already replaced characters. 
		gson = gson.replace("%", "%25");
		
		gson = gson.replace(" ", "%20");
		gson = gson.replace("\"", "%22");
		gson = gson.replace("{", "%7B");
		gson = gson.replace("}", "%7D");
		
		// the character "&" gets replaced by "\u0026", 
		// when the string is processed on Android. 
		// So it must be replaced by "%26", in order
		// to send it via web service.
		// The same applies to all others with 
		// similar formats bellow.
		gson = gson.replace("\\u0026", "%26"); // &
		gson = gson.replace("\\u003d", "%3D"); // =
		gson = gson.replace("\\u003c", "%3C"); // <
		gson = gson.replace("\\u003e", "%3E"); // >
		gson = gson.replace("\\u0027", "%27"); // '
		
		gson = gson.replace("/", "");
		gson = gson.replace("\\", "");
		gson = gson.replace("#", "%23");
		gson = gson.replace("?", "%3F");
		gson = gson.replace(";", "%3B");
		
		gson = gson.replace("|", "%7C");
		gson = gson.replace("`", "%60");
		gson = gson.replace("^", "%5E");
		
		return gson;
	}
	
	public static String formatLatLng(String latLng){
		if (latLng.contains(",")){
			latLng = latLng.replace(",", ".");
		}
		return latLng;
	}
	
}
