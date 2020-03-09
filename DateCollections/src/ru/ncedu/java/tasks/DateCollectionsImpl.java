package ru.ncedu.java.tasks;

import java.text.*;
import java.util.*;
import java.util.Map.*;

public class DateCollectionsImpl implements DateCollections {
    private SortedMap<String, Element> mainMap;
    private int dateStyle = DateFormat.MEDIUM;

    Comparator<String> Compare = new Comparator<String>() {
        @Override
        public int compare(String s1, String s2) {
            try {
                return toCalendar(s1).compareTo(toCalendar(s2));
            } catch (ParseException e) {
                throw new RuntimeException();
            }
        }
    };
    Comparator<Element> CompareBirth = new Comparator<Element>() {
        @Override
        public int compare(Element a, Element b) {
            return a.getBirthDate().compareTo(b.getBirthDate());
        }
    };
    Comparator<Element> CompareDeath = new Comparator<Element>() {
        @Override
        public int compare(Element a, Element b) {
            return a.getDeathDate().compareTo(b.getDeathDate());
        }
    };
    @Override
    public void setDateStyle(int dateStyle) {
        this.dateStyle = dateStyle;
    }
    @Override
    public Calendar toCalendar(String dateString) throws ParseException {
        Calendar c = Calendar.getInstance();
	c.setTime(DateFormat.getDateInstance(dateStyle).parse(dateString));
	return c;
    }
    @Override
    public String toString(Calendar date) {
        return DateFormat.getDateInstance(dateStyle).format(date.getTime());
    }
    @Override
    public void initMainMap(int elementsNumber, Calendar firstDate) {
        mainMap = new TreeMap<>(Compare);
        for (int i = 0; i != elementsNumber; ++i) {
            Calendar c = (Calendar) firstDate.clone();
            c.add(Calendar.DATE, i * 110);
            Random rnd = new Random();
            int lifetime = rnd.nextInt(2000);
            Element e = new Element(c, lifetime);
            mainMap.put(toString(c), e);
        }
    }
    @Override
    public void setMainMap(Map<String, Element> map) {
        this.mainMap = new TreeMap<>(Compare);
        for (Entry<String, Element> entry: map.entrySet()) {
            this.mainMap.put(entry.getKey(), entry.getValue());
        }
    }
    @Override
    public Map<String, Element> getMainMap() {
        return mainMap;
    }
    @Override
    public SortedMap<String, Element> getSortedSubMap() {
        SortedMap<String, Element> subMap = new TreeMap<>(Compare);
        Calendar now = Calendar.getInstance(); 
        for (Map.Entry<String, Element> entry : mainMap.entrySet()) {
            if (entry.getValue().getBirthDate().compareTo(now) > 0) {
                subMap.put(entry.getKey(), entry.getValue());
            }
        }
        return subMap;
    }
    @Override
    public List<Element> getMainList() {
            List<Element> list = new ArrayList<>(mainMap.values());
            Collections.sort(list, CompareBirth);
            return list;
    }
    @Override
    public void sortList(List<Element> list) {
            Collections.sort(list, CompareDeath);
    }
    @Override
    public void removeFromList(List<Element> list) {
        for (Iterator<Element> temp = list.iterator(); temp.hasNext();) {
            Element el = temp.next();
            int month = el.getBirthDate().get(Calendar.MONTH);
            if (month == Calendar.DECEMBER | month == Calendar.JANUARY | month == Calendar.FEBRUARY) {
                temp.remove();
            }
        }
    }
}