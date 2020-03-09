package ru.ncedu.java.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckerImpl implements Checker {

	@Override
	public Pattern getPLSQLNamesPattern() {
		Pattern pattern = Pattern.compile("[A-Za-z]{1}[A-Za-z0-9_$]{0,29}");
		return pattern;
	}

	@Override
	public Pattern getHrefURLPattern() {
		Pattern pattern = Pattern.compile("(?i)(<a[\\t\\n\\x0B\\f\\r ]*href[\\t\\n\\x0B\\f\\r ]*=[\\t\\n\\x0B\\f\\r ]*((\"?[A-Za-z0-9. ]*\"?)|([A-Za-z0-9.]*))[\\t\\n\\x0B\\f\\r ]*?>)");
		return pattern;
	}

	@Override
	public Pattern getEMailPattern() {
		Pattern pattern = Pattern.compile("[A-Za-z0-9][A-Za-z0-9-_[.]]{0,20}[A-Za-z0-9]@([A-Za-z0-9][A-Za-z0-9-]*[A-Za-z0-9](\\.))+(ru|com|org|net)");
		return pattern;
	}

	@Override
	public boolean checkAccordance(String inputString, Pattern pattern) throws IllegalArgumentException {
		if(inputString == null && pattern == null) {
			return true;
		}
		if(inputString == null && pattern != null || inputString != null && pattern == null) {
			throw new IllegalArgumentException();
		}
		Matcher matcher = pattern.matcher(inputString);
		if (matcher.matches()) {
			return true;
		}
		else return false;
	}

	@Override
	public List<String> fetchAllTemplates(StringBuffer inputString, Pattern pattern) throws IllegalArgumentException {
		List<String> myList = new ArrayList<String>();
		if(inputString == null || pattern == null) {
			throw new IllegalArgumentException();
		}
		Matcher matcher = pattern.matcher(inputString);
		while(matcher.find()) {
			myList.add(matcher.group(0));
        }
		return myList;
	}
}