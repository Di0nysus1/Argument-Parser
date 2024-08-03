package de.dion.argumentparser;

import java.io.File;

public class Example {
	
	//This is an Example class, you can delete this if you want
	
	
	public static void main(String[] args) {
		
		//java -jar Program.jar -help
		//java -jar Program.jar -penis2 -driverpath file.exe -headless true
		
		ArgumentParser ap = new ArgumentParser();
		ap.add(new ValueArgument("headless", true, "Determines if the browser runs in headless mode. If TRUE, the windows will be invisible."));
		ap.add(new ValueArgument("lang", "en:de", "Specifies the language pair for translation.").alias("language"));
		ap.add(new ValueArgument("translator", "deepl", "Specifies the translation service to use. Possible values: \"google\" or \"deepl\""));
		ap.add(new ValueArgument("driverpath", "C:\\path\\to\\geckodriver.exe", "Path to the Geckodriver binary.").alias("path").required());
		ap.add(new EmptyArgument("penis2").description("Ein Test Argument"));
		ap.parseArguments(args);
		
		boolean headless = ap.getBoolean("headless");
		File driverPath = ap.getFile("driverpath");
		String lang = ap.getString("lang");
		String translator = ap.getString("translator");
		
		
		System.out.println(ap.getArgument("penis2").hasBeenSet());
		System.out.println(headless);
	}
}
