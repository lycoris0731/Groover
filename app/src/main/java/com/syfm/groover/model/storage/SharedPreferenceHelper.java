package com.syfm.groover.model.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.syfm.groover.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lycoris on 2015/09/26.
 */
public class SharedPreferenceHelper {
    private static SharedPreferences sp;
    private static Resources r;

    public static void create(Context context) {
        if (null == sp || null == r) {
            sp = PreferenceManager.getDefaultSharedPreferences(context);
            r = context.getResources();
        }
    }

    /**
     *
     * Login
     *
     */

    public static Map<String, String> getLoginInfo() {
        Map<String, String> info = new HashMap<String, String>();
        String serialNo = sp.getString(Const.SP_LOGIN_NESICA_ID, "");
        String password = sp.getString(Const.SP_LOGIN_PASSWORD, "");
        if(serialNo != "" && password != "") {
            info.put(Const.SP_LOGIN_NESICA_ID, serialNo);
            info.put(Const.SP_LOGIN_PASSWORD, password);
        }
        return info;
    }

    public static void setLoginInfo(String nesicaId, String password) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Const.SP_LOGIN_NESICA_ID, nesicaId);
        editor.putString(Const.SP_LOGIN_PASSWORD,  password);
        editor.commit();
    }

    /**
     *
     * MusicList
     *
     */

    public static Map<String, Integer> getMusicListViewPosition() {
        Map<String, Integer> positions = new HashMap<>();
        int position = sp.getInt(Const.SP_MUSIC_LIST_LIST_VIEW_POSITION, -1);
        int y        = sp.getInt(Const.SP_MUSIC_LIST_LIST_VIEW_Y, -1);
        if(position != -1 && y != -1) {
            positions.put(Const.SP_MUSIC_LIST_LIST_VIEW_POSITION, position);
            positions.put(Const.SP_MUSIC_LIST_LIST_VIEW_Y, y);
        }

        return positions;
    }

    public static void setMusicListViewPosition(int position, int y) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Const.SP_MUSIC_LIST_LIST_VIEW_POSITION, position);
        editor.putInt(Const.SP_MUSIC_LIST_LIST_VIEW_Y, y);
        editor.commit();
    }

    public static Map<String, String> getMusicSortInfo() {
        Map<String, String> info = new HashMap<>();
        String order_by = sp.getString(Const.SP_MUSIC_LIST_SORT_ORDER_BY, "");
        String sort_type = sp.getString(Const.SP_MUSIC_LIST_SORT_SORT_TYPE, "");
        if(order_by != "" && sort_type != "") {
            info.put(Const.SP_MUSIC_LIST_SORT_ORDER_BY, order_by);
            info.put(Const.SP_MUSIC_LIST_SORT_SORT_TYPE, sort_type);
        }

        return info;
    }

    public static void setMusicSortInfo(String sort_type, String order_by) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Const.SP_MUSIC_LIST_SORT_ORDER_BY, order_by);
        editor.putString(Const.SP_MUSIC_LIST_SORT_SORT_TYPE, sort_type);
        editor.commit();
    }

    /**
     *
     * Ranking
     *
     */

    public static String getRankingData(final String LEVEL_TYPE) {
        return sp.getString(LEVEL_TYPE, "");
    }

    public static void setRankingData(final String LEVEL_TYPE, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LEVEL_TYPE, value);
        editor.commit();
    }

    public static String getEventNameList() {
        return sp.getString(Const.SP_EVENT_LIST, "");
    }

    public static void setEventNameList(String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Const.SP_EVENT_LIST, value);
        editor.commit();
    }

    public static Map<String, Integer> getRankingListViewPosition(String pos_key, String y_key) {
        Map<String, Integer> positions = new HashMap<>();
        int position = sp.getInt(Const.SP_MUSIC_LIST_LIST_VIEW_POSITION, -1);
        int y        = sp.getInt(Const.SP_MUSIC_LIST_LIST_VIEW_Y, -1);
        if(position != -1 && y != -1) {
            positions.put(Const.SP_MUSIC_LIST_LIST_VIEW_POSITION, position);
            positions.put(Const.SP_MUSIC_LIST_LIST_VIEW_Y, y);
        }

        return positions;
    }

    public static void setRankingListViewPosition(int position, int y, String pos_key, String y_key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(pos_key, position);
        editor.putInt(y_key, y);
        editor.commit();
    }

    public static int getRankingSpinnerPosition(String key) {
        return sp.getInt(key, -1);
    }

    public static void setRankingSpinnerPosition(int position, String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, position);
        editor.commit();
    }

}
