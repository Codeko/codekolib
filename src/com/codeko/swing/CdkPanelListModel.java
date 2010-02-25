package com.codeko.swing;

import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Copyright Codeko Informática 2008
 * www.codeko.com
 * @author Codeko
 */
public class CdkPanelListModel<T extends JPanel> implements ListModel {

    Vector<T> data = new Vector<T>();
    Vector<ListDataListener> listDataListeners = new Vector<ListDataListener>();

    public int getSize() {
        return data.size();

    }

    public T getElementAt(int index) {
        if (index > -1 && index < getData().size()) {
            return data.elementAt(index);
        }
        return null;
    }

    public void add(T panel) {
        getData().add(panel);
        ListDataEvent lde = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, getData().size() - 1, getData().size() - 1);
        for (ListDataListener l : getListDataListeners()) {
            l.intervalAdded(lde);
        }
    }

    public void insertElementAt(T panel, int index) {
        getData().insertElementAt(panel, index);
        ListDataEvent lde = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index);
        for (ListDataListener l : getListDataListeners()) {
            l.intervalAdded(lde);
        }
    }

    public boolean remove(T panel) {
        return removeElementAt(getData().indexOf(panel));
    }

    public boolean removeElementAt(int index) {
        boolean ret = false;
        if (index > -1 && index < getData().size()) {
            ret = true;
            getData().removeElementAt(index);
            ListDataEvent lde = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index);
            for (ListDataListener l : getListDataListeners()) {
                l.intervalAdded(lde);
            }
        }
        return ret;
    }

    public void firePanelChangued(T panel) {
        int index = getData().indexOf(panel);
        if (index > -1) {
            ListDataEvent lde = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index, index);
            for (ListDataListener l : getListDataListeners()) {
                l.intervalAdded(lde);
            }
        }
    }

    public void clear() {
        if (!getData().isEmpty()) {
            int min = 0;
            int max = getData().size() - 1;
            getData().removeAllElements();
            ListDataEvent lde = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, min, max);
            for (ListDataListener l : getListDataListeners()) {
                l.intervalAdded(lde);
            }
        }
    }

    private Vector<T> getData() {
        return data;
    }

    private Vector<ListDataListener> getListDataListeners() {
        return listDataListeners;
    }

    public void addListDataListener(ListDataListener l) {
        getListDataListeners().add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        getListDataListeners().remove(l);
    }
}
