package ru.ncedu.java.tasks;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.LinkedList;

public class StringFilterImpl implements StringFilter {
    private Set<String> mySet = new HashSet<>();
    private interface Filter {
        public boolean check(String s, String pattern); 
    }
    public void add(String s) {
        if (s == null){
            mySet.add(null);
        }
        else{
            mySet.add(s.toLowerCase());
        }
    }
    public boolean remove(String s) {
        if (s == null){
            return mySet.remove(s);
        }
        else{
            return mySet.remove(s.toLowerCase());
        }
    }
    public void removeAll() {
        this.mySet.clear();
    }
    public Collection<String> getCollection() {
        return this.mySet;
    }
    private Iterator<String> getIterator(Filter f, String p) {
        List<String> list = new LinkedList<>();
        for (String s: mySet) {
            if (s != null) {
                if (p == null || f.check(s,p)) {
                    list.add(s);
                }
            }
        }
        return list.iterator();
    }
    public Iterator<String> getStringsContaining(String chars) {
        Filter f = new Filter() {
            public boolean check(String s, String p) {
                return s.contains(p);
            }
        };
        return getIterator(f, chars);
    }
    public Iterator<String> getStringsStartingWith(String begin) {
        Filter f = new Filter() {
            public boolean check(String s, String p) {
                return s.startsWith(p.toLowerCase());
            }
        };
        return getIterator(f, begin);
    }
    public Iterator<String> getStringsByNumberFormat(String format) {
        Filter f = new Filter() {
            public boolean check(String s, String p) {
                if (s.length() != p.length()){
                    return false;
                }
                for (int i = 0; i != s.length(); i++) {
                    if (p.charAt(i) == '#') {
                        if (!Character.isDigit(s.charAt(i))) {
                            return false;
                        }
                    } else {
                        if (p.charAt(i) != s.charAt(i)) {
                                return false;
                        }
                    }
                }
                return true;
            }
        };
        return getIterator(f, format);
    }
    public Iterator<String> getStringsByPattern(String pattern) {
        Filter f = new Filter() {
            public boolean check(String s, String p) {
                int ind = p.indexOf('*');
                if (ind == -1){
                    return s.compareTo(p) == 0;
                }
                if (!s.regionMatches(false, 0, p, 0, ind)){
                    return false;
                }
                String subStr = p.substring(ind+1);
                for (int i = ind; i <= s.length(); i++) 
                    if (check(s.substring(i), subStr)){
                        return true;
                    }
                return false;
            }
        };
        return getIterator(f, pattern);
    }
}