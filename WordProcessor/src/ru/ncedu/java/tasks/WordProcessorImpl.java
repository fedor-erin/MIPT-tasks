package ru.ncedu.java.tasks;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordProcessorImpl implements WordProcessor {
	private String store;
        
	@Override
	public String getText() {
		return store;
	}

	@Override
	public void setSource(String src) {
		if(src == null) {
			throw new IllegalArgumentException();
		}
		store = src;
	}

	@Override
	public void setSourceFile(String srcFile) throws IOException {
		if(srcFile == null) {
			throw new IllegalArgumentException();
		}
		byte[] encoded = Files.readAllBytes(Paths.get(srcFile));
		store = new String(encoded);
	}
	
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	@Override
	public void setSource(FileInputStream fis) throws IOException {
		if(fis == null) {
			throw new IllegalArgumentException();
		}
		store = convertStreamToString(fis);
	}

	
	@Override 
	public Set<String> wordsStartWith(String begin) { 
	Set<String> set = new HashSet<String>(); 
	String s = store.toLowerCase(); 
	String[] mas = s.split("[\\s]+"); 
	if (begin == null){ 
		for (String str : mas){ 
			set.add(str); 
		} 
		return set; 
	} 
	Pattern p = Pattern.compile("^"+begin+"[^\\s]*"); 
	for (String str : mas){ 
		Matcher m = p.matcher(str); 
		while(m.find()) { 
			set.add(m.group()); 
		} 
	} 
	return set; 
	}
}